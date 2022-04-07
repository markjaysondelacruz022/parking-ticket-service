package com.mjdc.pts.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjdc.pts.dto.*;
import com.mjdc.pts.enumeration.*;
import com.mjdc.pts.exception.ParkingException;
import com.mjdc.pts.model.*;
import com.mjdc.pts.repository.ParkingEntranceRepository;
import com.mjdc.pts.repository.ParkingTicketRepository;
import com.mjdc.pts.repository.ParkingTransactionDetailsRepository;
import com.mjdc.pts.service.ParkingEntranceSlotService;
import com.mjdc.pts.service.ParkingSlotService;
import com.mjdc.pts.service.ParkingTicketService;
import com.mjdc.pts.util.DateUtil;
import com.mjdc.pts.util.UtilityHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class ParkingTicketServiceImpl implements ParkingTicketService {

    private final ParkingSlotService parkingSlotService;
    private final ParkingEntranceSlotService parkingEntranceSlotService;
    private final ParkingEntranceRepository parkingEntranceRepository;
    private final ParkingTicketRepository parkingTicketRepository;
    private final ParkingTransactionDetailsRepository parkingTransactionDetailsRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<ParkingTicketDto> park(final Long entranceId, final ParkingTicketDto parkingTicketDto) {

        if (parkingTicketRepository.existsByVehiclePlateNumberAndParkingStatus(parkingTicketDto.getVehiclePlateNumber(),
            ParkingStatus.PARKED)) {
            throw new ParkingException("There's an existing parked vehicle with the same plate number "
                .concat(parkingTicketDto.getVehiclePlateNumber()),
                HttpStatus.BAD_REQUEST);
        }

        final Date currentDateMinusOneHour = DateUtil.modifiedDate(DateUtil.getCurrentDateTime(),
            Calendar.HOUR, -1);

        List<ParkingTicket> prevParkingTickets = parkingTicketRepository
            .findByVehiclePlateNumberAndParkingStatusAndDateCheckoutGreaterThanEqual(parkingTicketDto.getVehiclePlateNumber(),
                ParkingStatus.CHECKOUT, currentDateMinusOneHour)
            .stream().sorted(Comparator.comparing(ParkingTicket::getId))
            .collect(Collectors.toList());

        Optional<ParkingTransactionDetails> prevTransactionDetailsOpt = Optional.empty();

        if (!prevParkingTickets.isEmpty()) {
            final ParkingTicket prevParkingTicket = prevParkingTickets.get(0);
            prevTransactionDetailsOpt = parkingTransactionDetailsRepository
                .findByTicketId(prevParkingTicket.getTicketId());
        }

        parkingTicketDto.setParkingStatus(ParkingStatus.PARKED);

        final Optional<ParkingEntranceSlotDto> checkedEntranceSlotDto = parkingEntranceSlotService
            .retrieveNearestAvailableSlotFromEntrance(entranceId,
                parkingTicketDto.getVehicleSize());

        if (checkedEntranceSlotDto.isEmpty()) {
            throw new ParkingException("No available parking slot",
                HttpStatus.BAD_REQUEST);
        } else {
            parkingTicketDto.setParkingEntranceSlot(checkedEntranceSlotDto.get());
        }

        final ParkingEntranceSlotDto parkingEntranceSlotDto = parkingTicketDto.getParkingEntranceSlot();


        Optional<ParkingEntranceDto> parkingEntranceDtoOpt = Optional.ofNullable(Optional
            .ofNullable(parkingEntranceSlotDto.getParkingEntrance())
            .orElse(parkingEntranceRepository.findById(parkingEntranceSlotDto.getParkingEntranceId())
                .map(parkingEntrance -> objectMapper.convertValue(parkingEntrance, ParkingEntranceDto.class))
                .orElse(null)));

        Optional<ParkingSlotDto> parkingSlotDtoOpt = Optional.ofNullable(Optional
            .ofNullable(parkingEntranceSlotDto.getParkingSlot())
            .orElse(parkingSlotService.retrieveById(parkingEntranceSlotDto.getParkingSlotId()).orElse(null)));

        if (parkingEntranceDtoOpt.isEmpty() || parkingSlotDtoOpt.isEmpty()) {
            throw new ParkingException("Can't find parking entrance or parking slot", HttpStatus.BAD_REQUEST);
        }
        parkingEntranceSlotDto.setParkingEntrance(parkingEntranceDtoOpt.get());
        parkingEntranceSlotDto.setParkingSlot(parkingSlotDtoOpt.get());
        parkingTicketDto.setParkingEntranceSlot(parkingEntranceSlotDto);

        final ParkingTicket parkingTicket = objectMapper.convertValue(parkingTicketDto, ParkingTicket.class);

        log.info("Generated ticket id");
        final String ticketId = this.generateTicketId(parkingTicketRepository.getMaxTicketId());
        parkingTicket.setTicketId(ticketId);
        parkingTicket.setDateParked(DateUtil.getCurrentDateTime());
        prevTransactionDetailsOpt.ifPresent(parkingTransactionDetails -> {
            parkingTicket.setDateParked(parkingTransactionDetails.getDateParked());
            parkingTicket.setPreviousParkingTransactionDetails(parkingTransactionDetails);
        });

        final ParkingTicketDto savedParkingTicketDto = objectMapper.convertValue(parkingTicketRepository
            .save(parkingTicket), ParkingTicketDto.class);

        this.updateParkingSlotAvailability(parkingEntranceSlotDto.getParkingSlot(), Availability.NOT);

        log.info("Saved Parking Ticket {}", savedParkingTicketDto);

        return Optional.ofNullable(savedParkingTicketDto);
    }

    private void findPreviousPaidTransaction(final ParkingTicket parkingTicket, final List<ParkingTransactionItemDto> paidTransactions) {

        Optional.ofNullable(parkingTicket.getPreviousParkingTransactionDetails())
            .ifPresent(transactionDetails -> {

                if (PaymentStatus.PAID.equals(transactionDetails.getPaymentStatus())) {
                    final ParkingTransactionItemDto parkingTransactionItemDto = new ParkingTransactionItemDto();
                    parkingTransactionItemDto.setDescription(transactionDetails.getTransactionId());
                    parkingTransactionItemDto.setType(PriceType.DEDUCTION);
                    parkingTransactionItemDto.setAmountDue(transactionDetails.getTotalAmountDue());
                    parkingTransactionItemDto.setQuantity(1);
                    paidTransactions.add(parkingTransactionItemDto);
                }
                parkingTicketRepository.findByTicketId(transactionDetails.getTicketId())
                    .ifPresent(parkingTicket1 -> findPreviousPaidTransaction(parkingTicket1, paidTransactions));
            } );
    }

    private void updateParkingSlotAvailability(final ParkingSlotDto parkingSlotDto, final Availability availability) {
        parkingSlotDto.setAvailability(availability);
        parkingSlotService.save(parkingSlotDto);
    }

    @Override
    public Optional<ParkingTransactionDetailsDto> unPark(final UnParkingDto unParkingDto) {
        final AtomicReference<Optional<ParkingTransactionDetailsDto>> transactionDetailsOptAr = new AtomicReference<>(Optional.empty());

        parkingTicketRepository.findByTicketId(unParkingDto.getTicketId()).ifPresent(parkingTicket -> {

            if (!parkingTicket.getParkingStatus().equals(ParkingStatus.PARKED)) {
                throw new ParkingException("Only Parked status ticket can be checkout", HttpStatus.BAD_REQUEST);
            }

            final String parkingTicketId = parkingTicket.getTicketId();

            final List<ParkingTransactionItem> parkingTransactionItemList = objectMapper
                .convertValue(calculateCurrentCharges(parkingTicketId), new TypeReference<>() {});

            final ParkingEntranceSlot entranceSlot = parkingTicket.getParkingEntranceSlot();
            final ParkingSlot parkingSlot = entranceSlot.getParkingSlot();
            final ParkingEntrance parkingEntrance = entranceSlot.getParkingEntrance();

            final BigDecimal totalAmount = calculateTotalAmountDue(parkingTransactionItemList);

            if (totalAmount.compareTo(unParkingDto.getAmountTender()) > 0) {
                throw new ParkingException("Insufficient amount tendered, total amount due is "
                    + totalAmount.toPlainString(), HttpStatus.BAD_REQUEST);
            }

            final String driverName = parkingTicket.getDriverName();
            final String vehicleDetails = parkingTicket.getVehicleDetails();
            final String vehiclePlateNumber = parkingTicket.getVehiclePlateNumber();
            final Size vehicleSize = parkingTicket.getVehicleSize();
            final String parkingLotName = parkingSlot.getParkingLot().getName();
            final String parkingSlotName = parkingSlot.getName();
            final String entranceName = parkingEntrance.getName();
            final Size slotSize = parkingSlot.getSize();

            final Date dateParked = parkingTicket.getDateParked();
            final Date dateCheckout = DateUtil.getCurrentDateTime();
            final PaymentStatus paymentStatus = PaymentStatus.PAID;

            final ParkingTransactionDetails parkingTransactionDetails = new ParkingTransactionDetails();
            parkingTransactionDetails.setTransactionId(generateTransactionId(parkingTransactionDetailsRepository
                .getMaxTransactionId()));
            parkingTransactionDetails.setTicketId(parkingTicketId);
            parkingTransactionDetails.setDriverName(driverName);
            parkingTransactionDetails.setVehicleDetails(vehicleDetails);
            parkingTransactionDetails.setVehiclePlateNumber(vehiclePlateNumber);
            parkingTransactionDetails.setVehicleSize(vehicleSize);
            parkingTransactionDetails.setLot(parkingLotName);
            parkingTransactionDetails.setSlot(parkingSlotName);
            parkingTransactionDetails.setEntrance(entranceName);
            parkingTransactionDetails.setSlotSize(slotSize);
            parkingTransactionDetails.setParkingTransactionItems(parkingTransactionItemList);
            parkingTransactionDetails.setTotalAmountDue(totalAmount);
            parkingTransactionDetails.setDateParked(dateParked);
            parkingTransactionDetails.setDateCheckout(dateCheckout);
            parkingTransactionDetails.setPaymentStatus(paymentStatus);
            parkingTransactionDetails.setAmountTender(unParkingDto.getAmountTender());
            parkingTransactionDetails.setAmountChange(unParkingDto.getAmountTender().subtract(totalAmount));

            final ParkingTransactionDetailsDto parkingTransactionDetailsDto =  objectMapper
                .convertValue(parkingTransactionDetailsRepository
                .save(parkingTransactionDetails), ParkingTransactionDetailsDto.class);
            transactionDetailsOptAr.set(Optional.of(parkingTransactionDetailsDto));

            this.updateParkingSlotAvailability(objectMapper.convertValue(parkingSlot, ParkingSlotDto.class),
                Availability.OK);
            parkingTicket.setParkingStatus(ParkingStatus.CHECKOUT);
            parkingTicket.setDateCheckout(dateCheckout);
            parkingTicketRepository.save(parkingTicket);

        });
        return transactionDetailsOptAr.get();
    }

    @Override
    public Optional<ParkingTransactionDetailsDto> retrieveCurrentCharges(String ticketId) {
        final List<ParkingTransactionItemDto> charges = calculateCurrentCharges(ticketId);
        Optional<ParkingTransactionDetailsDto> parkingTransactionDetailsDtoOpt = Optional.empty();

        if (!charges.isEmpty()) {
            final ParkingTransactionDetailsDto transactionDetailsDto = new ParkingTransactionDetailsDto();
            transactionDetailsDto.setTicketId(ticketId);
            transactionDetailsDto.setTotalAmountDue(calculateTotalAmountDue(objectMapper
                .convertValue(charges, new TypeReference<>() {})));
            transactionDetailsDto.setParkingTransactionItems(charges);
            parkingTransactionDetailsDtoOpt = Optional.of(transactionDetailsDto);
        }



        return parkingTransactionDetailsDtoOpt;
    }

    private BigDecimal sumAmount(final List<ParkingTransactionItem> parkingTransactionItemList,
                                 final PriceType... priceTypes) {
        return parkingTransactionItemList.stream()
            .filter(item -> Arrays.stream(priceTypes).anyMatch(type -> type.equals(item.getType())))
            .map(ParkingTransactionItem::getAmountDue)
            .reduce(new BigDecimal(0), BigDecimal::add);
    }

    private BigDecimal calculateTotalAmountDue(final List<ParkingTransactionItem> parkingTransactionItemList) {
        final BigDecimal deductions = sumAmount(parkingTransactionItemList, PriceType.DEDUCTION);

        final BigDecimal totalAmountDue = sumAmount(parkingTransactionItemList,
            PriceType.FLAT_RATE, PriceType.DAY_RATE, PriceType.PER_HOUR);

        return totalAmountDue.subtract(deductions);
    }

    private List<ParkingTransactionItemDto> calculateCurrentCharges(String ticketId) {
        final List<ParkingTransactionItemDto> parkingTransactionItemDtoList = new ArrayList<>();
        parkingTicketRepository.findByTicketId(ticketId).ifPresent(parkingTicket -> {

            if (!parkingTicket.getParkingStatus().equals(ParkingStatus.PARKED)) {
                throw new ParkingException("Only Parked status ticket can be calculated", HttpStatus.BAD_REQUEST);
            }

            final Date parkedDate = parkingTicket.getDateParked();
            final ParkingSlot parkingSlot = parkingTicket.getParkingEntranceSlot().getParkingSlot();
            final List<ParkingSlotPrice> parkingSlotPrices = parkingSlot.getParkingSlotPrices();

            final Integer hoursDiff = DateUtil.getHoursDiff(parkedDate, DateUtil.getCurrentDateTime());

            final AtomicReference<BigDecimal> charges = new AtomicReference<>(new BigDecimal(0));

            final Optional<ParkingSlotPrice> dayRatePriceOpt = parkingSlotPrices.stream()
                .filter(p -> PriceType.DAY_RATE.equals(p.getType())).findFirst();

            final Optional<ParkingSlotPrice> perHourPriceOpt = parkingSlotPrices.stream()
                .filter(p -> PriceType.PER_HOUR.equals(p.getType())).findFirst();

            final Optional<ParkingSlotPrice> flatRatePriceOpt = parkingSlotPrices.stream()
                .filter(p -> PriceType.FLAT_RATE.equals(p.getType())).findFirst();

            flatRatePriceOpt.ifPresent(fRate -> {
                final ParkingTransactionItemDto transactionItemDto = new ParkingTransactionItemDto();

                charges.set(charges.get().add(fRate.getPrice()));
                transactionItemDto.setType(fRate.getType());
                transactionItemDto.setDescription(fRate.getType().getValue());
                transactionItemDto.setUnitPrice(fRate.getPrice());
                transactionItemDto.setQuantity(1);
                transactionItemDto.setAmountDue(fRate.getPrice());
                parkingTransactionItemDtoList.add(transactionItemDto);

                final int excessHourQuantity = hoursDiff - fRate.getHour();
                this.addPerHourChanges(perHourPriceOpt, parkingTransactionItemDtoList, excessHourQuantity);
            });

            dayRatePriceOpt.ifPresent(dRate -> {
                if (hoursDiff.compareTo(dRate.getHour()) >= 0) {
                    parkingTransactionItemDtoList.clear();
                    final ParkingTransactionItemDto transactionItemDto = new ParkingTransactionItemDto();
                    transactionItemDto.setType(dRate.getType());
                    transactionItemDto.setDescription(dRate.getType().getValue());
                    transactionItemDto.setUnitPrice(dRate.getPrice());
                    transactionItemDto.setAmountDue(dRate.getPrice());
                    final int quantity = hoursDiff / dRate.getHour();
                    transactionItemDto.setQuantity(quantity);
                    transactionItemDto.setAmountDue(dRate.getPrice().multiply(new BigDecimal(quantity)));
                    parkingTransactionItemDtoList.add(transactionItemDto);
                    final int excessHourQuantity = hoursDiff % dRate.getHour();
                    this.addPerHourChanges(perHourPriceOpt, parkingTransactionItemDtoList, excessHourQuantity);
                }
            });

            this.findPreviousPaidTransaction(parkingTicket, parkingTransactionItemDtoList);

        });


        return parkingTransactionItemDtoList;
    }

    private void addPerHourChanges(final Optional<ParkingSlotPrice> perHourRatePriceOpt,
                                   final List<ParkingTransactionItemDto> parkingTransactionItemDtoList,
                                   final int excessHours) {
        if (excessHours > 0) {
            perHourRatePriceOpt.ifPresent(phRate -> {
                final ParkingTransactionItemDto transactionItemDto = new ParkingTransactionItemDto();
                transactionItemDto.setType(phRate.getType());
                transactionItemDto.setDescription(phRate.getType().getValue());
                transactionItemDto.setUnitPrice(phRate.getPrice());
                transactionItemDto.setQuantity(excessHours);
                transactionItemDto.setAmountDue(phRate.getPrice().multiply(new BigDecimal(excessHours)));
                parkingTransactionItemDtoList.add(transactionItemDto);
            });
        }

    }


    private String generateTicketId(final ReferenceId maxReferenceId) {

        final String value = Optional.ofNullable(maxReferenceId).map(ReferenceId::getValue)
            .orElse("0");
        log.info("Parking ticket max id {} ", value);
        final AtomicReference<String> ticketIdAr = new AtomicReference<>("");
        Optional.of(value).ifPresent(v -> {
            String ticketId;
            do {
                int ticketNumber = Integer.parseInt(value);
                ticketNumber = ticketNumber + 1;
                ticketId = UtilityHelper.addLeadingZero(ticketNumber, 10);
            } while (parkingTicketRepository.existsByTicketId(ticketId));
            ticketIdAr.set(ticketId);
        });
        log.info("Generated ticket id {} ", ticketIdAr.get());


        return ticketIdAr.get();
    }

    private String generateTransactionId(final ReferenceId maxTransactionId) {

        final String value = Optional.ofNullable(maxTransactionId).map(ReferenceId::getValue)
            .orElse("0");
        log.info("Transaction max id {} ", value);
        final AtomicReference<String> transactionIdAr = new AtomicReference<>("");
        Optional.of(value).ifPresent(v -> {
            String transactionId;
            do {
                int ticketNumber = Integer.parseInt(value);
                ticketNumber = ticketNumber + 1;
                transactionId = UtilityHelper.addLeadingZero(ticketNumber, 10);
            } while (parkingTransactionDetailsRepository.existsByTransactionId(transactionId));
            transactionIdAr.set(transactionId);
        });
        log.info("Generated transaction id {} ", transactionIdAr.get());


        return transactionIdAr.get();
    }
}
