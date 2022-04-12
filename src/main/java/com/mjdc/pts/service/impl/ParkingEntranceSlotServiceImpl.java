package com.mjdc.pts.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjdc.pts.dto.ParkingEntranceDto;
import com.mjdc.pts.dto.ParkingEntranceSlotDto;
import com.mjdc.pts.dto.ParkingSlotDto;
import com.mjdc.pts.enumeration.Availability;
import com.mjdc.pts.enumeration.Size;
import com.mjdc.pts.model.ParkingEntrance;
import com.mjdc.pts.model.ParkingEntranceSlot;
import com.mjdc.pts.model.ParkingSlot;
import com.mjdc.pts.repository.ParkingEntranceRepository;
import com.mjdc.pts.repository.ParkingEntranceSlotRepository;
import com.mjdc.pts.repository.ParkingSlotRepository;
import com.mjdc.pts.service.ParkingEntranceSlotService;
import com.mjdc.pts.service.ParkingSlotService;
import com.mjdc.pts.service.ParkingTicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParkingEntranceSlotServiceImpl implements ParkingEntranceSlotService {

    private final ParkingEntranceSlotRepository entranceSlotRepository;
    private final ParkingEntranceRepository entranceRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final ParkingSlotService parkingSlotService;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<ParkingEntranceSlotDto> retrieveNearestAvailableSlotFromEntrance(final Long entranceId,
                                                                                     final Size vehicleSize) {
        log.info("Retrieving entrance by id {}", entranceId);

        final Optional<ParkingEntrance> parkingEntranceOpt = entranceRepository.findById(entranceId);

        final AtomicReference<Optional<ParkingEntranceSlot>> availableParkingSlotOpt = new AtomicReference<>(Optional.empty());

        parkingEntranceOpt.ifPresent(parkingEntrance -> {
            log.info("Retrieve entrance {}", parkingEntrance);
            log.info("Retrieving available slot from entrance {}", parkingEntrance.getName());

            final List<ParkingEntranceSlot> list = entranceSlotRepository
                .findAllByParkingEntranceAndParkingSlot_AvailabilityAndParkingEntrance_IsActive(parkingEntrance,
                    Availability.OK, true);

            final List<ParkingEntranceSlot> availableParkingSLotFromEntDtoList = list.stream()
                .sorted(Comparator.comparing(ParkingEntranceSlot::getDistance))
                .filter(sortedEntranceSlot -> this.isVehicleFitWithSlot(sortedEntranceSlot
                    .getParkingSlot().getSize(), vehicleSize))
                .collect(Collectors.toList());
            log.info("Retrieved available slot from entrance {} | slots {}", parkingEntrance.getName(),
                availableParkingSLotFromEntDtoList.stream().map(av -> av.getParkingSlot().getName())
                    .collect(Collectors.toList()));

            if (!availableParkingSLotFromEntDtoList.isEmpty()) {
                availableParkingSlotOpt.set(Optional.of(availableParkingSLotFromEntDtoList.get(0)));
            }
        });
        availableParkingSlotOpt.get().ifPresent(availableSlot -> {

        });

        return availableParkingSlotOpt.get().map(availableSlot -> {
            final ParkingEntranceSlotDto parkingEntranceSlotDto = objectMapper.convertValue(availableSlot,
                ParkingEntranceSlotDto.class);

            parkingEntranceSlotDto.setParkingSlot(this.retrieveSlotIfEmpty(parkingEntranceSlotDto.getParkingSlot(),
                    parkingEntranceSlotDto.getParkingSlotId()));
            parkingEntranceSlotDto.setParkingEntrance(this.retrieveEntranceIfEmpty(parkingEntranceSlotDto.getParkingEntrance(),
                    parkingEntranceSlotDto.getParkingEntranceId()));

            return parkingEntranceSlotDto;
        });
    }

    @Override
    public ParkingEntranceDto retrieveEntranceIfEmpty(final ParkingEntranceDto parkingEntranceDto, final Long entranceId) {
        return Optional
            .ofNullable(parkingEntranceDto)
            .orElse(entranceRepository.findById(entranceId)
                .map(parkingEntrance -> objectMapper.convertValue(parkingEntrance, ParkingEntranceDto.class))
                .orElse(null));
    }

    @Override
    public ParkingSlotDto retrieveSlotIfEmpty(final ParkingSlotDto parkingSlotDto, final Long slotId) {
        return Optional
            .ofNullable(parkingSlotDto)
            .orElse(parkingSlotService.retrieveById(slotId).orElse(null));
    }

    @Override
    public boolean isExist(Long id) {
        return entranceSlotRepository.existsById(id);
    }

    @Override
    public void populateParkingEntranceSlot(final Long lotId) {
        log.info("Populating entrance slot for lot id {}", lotId);
        final List<ParkingEntrance> parkingEntranceList = entranceRepository.findByParkingLot_Id(lotId);
        final List<Long> parkingSlotIds = parkingSlotRepository.findByParkingLot_Id(lotId).stream()
            .map(ParkingSlot::getId).collect(Collectors.toList());
        parkingEntranceList.forEach(entrance -> {
            log.info("Populating entrance {}", entrance.getName());
            final List<Long> entranceSlotIds = entranceSlotRepository.findByParkingEntranceId(entrance.getId())
                .stream().map(ParkingEntranceSlot::getParkingSlotId).collect(Collectors.toList());

            final List<ParkingEntranceSlot> noEntranceSlots = parkingSlotIds.stream()
                .filter(slotIds -> !entranceSlotIds.contains(slotIds))
                .map(fSlotIds -> {
                    final ParkingEntranceSlot parkingEntranceSlot = new ParkingEntranceSlot();
                    parkingEntranceSlot.setParkingEntranceId(entrance.getId());
                    parkingEntranceSlot.setParkingSlotId(fSlotIds);
                    parkingEntranceSlot.setDistance(0);
                    return parkingEntranceSlot;
                })
                .collect(Collectors.toList());

            log.info("Populating entrance {} slots size {}", entrance.getName(), noEntranceSlots.size());
            final List<ParkingEntranceSlot> saveEntranceSlots = entranceSlotRepository.saveAll(noEntranceSlots);
            log.info("Populated entrance {} saved slots size {}", entrance.getName(), saveEntranceSlots.size());
        });

    }

    private boolean isVehicleFitWithSlot(final Size slotSize, final Size vehicleSize) {
        final List<Size> fitSlotSize = new ArrayList<>();
        switch (vehicleSize) {
            case SMALL:
                fitSlotSize.addAll(Arrays.asList(Size.SMALL, Size.MEDIUM, Size.LARGE));
                break;
            case MEDIUM:
                fitSlotSize.addAll(Arrays.asList(Size.MEDIUM, Size.LARGE));
                break;
            case LARGE:
                fitSlotSize.addAll(Collections.singletonList(Size.LARGE));
                break;
            default:
        }
        return fitSlotSize.stream().anyMatch(size -> size.equals(slotSize));
    }
}
