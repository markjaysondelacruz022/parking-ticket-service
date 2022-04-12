package com.mjdc.pts.controller;

import com.mjdc.pts.dto.*;
import com.mjdc.pts.exception.AppExceptionHandler;
import com.mjdc.pts.exception.ParkingException;
import com.mjdc.pts.service.ParkingLotService;
import com.mjdc.pts.service.ParkingTicketService;
import com.mjdc.pts.util.DateUtil;
import com.mjdc.pts.util.UtilityHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/parking-ticket")
public class ParkingTicketController {

    private final ParkingTicketService parkingTicketService;
    private final ParkingLotService parkingLotService;

    @PostMapping("lot/{lotId}/entrance/{entranceId}")
    public ResponseEntity<?> park(@PathVariable final Long lotId,
                                  @PathVariable final Long entranceId,
                                  @Valid @RequestBody final ParkingTicketDto parkingTicketDto) throws Exception {
        log.info("Creating parking ticket ... {}", parkingTicketDto);
        final ResponseEntity<?> res;

        try {

            final Optional<ParkingTicketDto> createdTicketDto = parkingTicketService
                .park(lotId, entranceId, parkingTicketDto);

            res = createdTicketDto.map(ticketDto -> new ResponseEntity<>(ResponseDto.builder()
                .message("Successfully created")
                .data(createdTicketDto.orElse(null))
                .build(), HttpStatus.OK)).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ""));

            log.info("Created parking ticket {}", createdTicketDto.orElse(null));
        } catch (final Exception ex) {
            final String errorMessage = String.format("Failed creating parking ticket driver %s %s ",
                parkingTicketDto.getDriverName(), ex.getMessage());
            log.error(errorMessage, ex);
            throw AppExceptionHandler.getExceptionThrow(ex, errorMessage);
        }
        return res;
    }

    @GetMapping("/{ticketId}/calculate")
    public ResponseEntity<?> calculateCharges(@PathVariable final String ticketId) throws Exception {
        log.info("Calculating charges of parking ticket id {}", ticketId);
        final ResponseEntity<?> res;

        try {
            final Optional<ParkingTransactionDetailsDto> calculatedCharges = parkingTicketService
                .retrieveCurrentCharges(ticketId);

            res = calculatedCharges.map(charges -> new ResponseEntity<>(ResponseDto.builder()
                .message("Successfully retrieve calculated charges")
                .data(charges)
                .build(), HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ticket id/No charges found"));

            log.info("Calculated charges of parking ticket id {} {}", ticketId, calculatedCharges);
        } catch (final Exception ex) {
            final String errorMessage = String.format("Failed calculating charges of ticket id %s %s ",
                ticketId, ex.getMessage());
            log.error(errorMessage, ex);
            throw AppExceptionHandler.getExceptionThrow(ex, errorMessage);
        }
        return res;

    }

    @PostMapping("/unpark")
    public ResponseEntity<?> unpark(@Valid @RequestBody final UnParkingDto unParkingDto) throws Exception {
        log.info("Unparking ticket id ... {}", unParkingDto.getTicketId());
        final ResponseEntity<?> res;

        try {
            final Optional<ParkingTransactionDetailsDto> createdTransactionDto = parkingTicketService
                .unPark(unParkingDto);
            res = createdTransactionDto.map(ticketDto -> new ResponseEntity<>(ResponseDto.builder()
                .message("Successfully generated transaction")
                .data(createdTransactionDto.orElse(null))
                .build(), HttpStatus.OK)).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Failed to generate transaction"));
            log.info("transaction ticket {}", createdTransactionDto.orElse(null));
        } catch (final Exception ex) {
            final String errorMessage = String.format("Failed un parking ticket id %s %s ",
                unParkingDto.getTicketId(), ex.getMessage());
            log.error(errorMessage, ex);
            throw AppExceptionHandler.getExceptionThrow(ex, errorMessage);
        }
        return res;
    }

    @PostMapping("/search")
    public ResponseEntity<?> retrieveTicketsSearch(@RequestParam final Integer pageNo,
                                            @RequestParam final Integer pageSize,
                                            @RequestParam(required = false,
                                                defaultValue = "dateCreated") final String sortBy,
                                            @RequestParam(required = false,
                                                defaultValue = "desc") final String sortOrder,
                                            @RequestBody final Map<String, String> body) throws Exception {
        log.info("Searching parking tickets... {}", body);
        final ResponseEntity<?> res;

        try {
            UtilityHelper.searchKeyChecker(body);
            final Sort sort = "asc".equalsIgnoreCase(sortOrder)
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
            final Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

            final Page<ParkingTicketDto> searchedParkingLots = parkingTicketService
                .retrieveTicketBySearchKeyPageable(Optional.ofNullable(body.get("searchKey")).orElse(""), pageable);

            res = new ResponseEntity<>(ResponseDto.builder()
                .message("Retrieved parking tickets")
                .data(searchedParkingLots).build(), HttpStatus.OK);

            log.info("Searched parking tickets size{}", searchedParkingLots.getContent().size());
        } catch (final Exception ex) {
            final String errorMessage = String.format("Failed searching parking tickets %s %s ",
                body, ex.getMessage());
            log.error(errorMessage, ex);
            throw AppExceptionHandler.getExceptionThrow(ex, errorMessage);
        }
        return res;
    }

    @PostMapping("/transaction/search")
    public ResponseEntity<?> retrieveTransactionSearch(@RequestParam final Integer pageNo,
                                                   @RequestParam final Integer pageSize,
                                                   @RequestParam(required = false,
                                                       defaultValue = "dateCreated") final String sortBy,
                                                   @RequestParam(required = false,
                                                       defaultValue = "desc") final String sortOrder,
                                                   @RequestBody final Map<String, String> body) throws Exception {
        log.info("Searching parking transaction... {}", body);
        final ResponseEntity<?> res;

        try {

            UtilityHelper.searchKeyChecker(body);

            final Sort sort = "asc".equalsIgnoreCase(sortOrder)
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
            final Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

            final Page<ParkingTransactionDetailsDto> searchedParkingLots = parkingTicketService
                .retrieveTransactionBySearchKeyPageable(Optional.ofNullable(body.get("searchKey")).orElse(""), pageable);

            res = new ResponseEntity<>(ResponseDto.builder()
                .message("Retrieved parking transactions")
                .data(searchedParkingLots).build(), HttpStatus.OK);

            log.info("Searched parking transactions size{}", searchedParkingLots.getContent().size());
        } catch (final Exception ex) {
            final String errorMessage = String.format("Failed searching parking transactions %s %s ",
                body, ex.getMessage());
            log.error(errorMessage, ex);
            throw AppExceptionHandler.getExceptionThrow(ex, errorMessage);
        }
        return res;
    }

}
