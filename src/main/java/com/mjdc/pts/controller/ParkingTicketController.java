package com.mjdc.pts.controller;

import com.mjdc.pts.dto.*;
import com.mjdc.pts.exception.AppExceptionHandler;
import com.mjdc.pts.service.ParkingEntranceSlotService;
import com.mjdc.pts.service.ParkingSlotService;
import com.mjdc.pts.service.ParkingTicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/parking-ticket")
public class ParkingTicketController {

    private final ParkingTicketService parkingTicketService;

    @PostMapping("/entrance/{entranceId}")
    public ResponseEntity<?> park(@PathVariable final Long entranceId,
                                  @Valid @RequestBody final ParkingTicketDto parkingTicketDto) throws Exception {
        log.info("Creating parking ticket ... {}", parkingTicketDto);
        final ResponseEntity<?> res;

        try {
            final Optional<ParkingTicketDto> createdTicketDto = parkingTicketService.park(entranceId, parkingTicketDto);

            res = createdTicketDto.map(ticketDto -> new ResponseEntity<>(ResponseDto.builder()
                .message("Successfully created")
                .data(createdTicketDto.orElse(null))
                .build(), HttpStatus.OK)).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,""));

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


}
