package com.mjdc.pts.controller;

import com.mjdc.pts.dto.ParkingLotDto;
import com.mjdc.pts.dto.ResponseDto;
import com.mjdc.pts.exception.AppExceptionHandler;
import com.mjdc.pts.exception.ControllerException;
import com.mjdc.pts.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/parking-lot")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    @PostMapping
    public ResponseEntity<?> createLot(@Valid @RequestBody final ParkingLotDto parkingLotDto) throws Exception {
        log.info("Creating parking lot... {}", parkingLotDto);
        final ResponseEntity<?> res;

        try {
            final ParkingLotDto createdParkingLotDto = parkingLotService.create(parkingLotDto);
            res = new ResponseEntity<>(ResponseDto.builder()
                .message("Successfully created")
                .data(createdParkingLotDto)
                .build(), HttpStatus.OK);
            log.info("Created parking lot {}", createdParkingLotDto);
        } catch (final Exception ex) {
            final String errorMessage = String.format("Failed creating parking lot %s %s ",
                parkingLotDto.getName(), ex.getMessage());
            log.error(errorMessage, ex);
            throw AppExceptionHandler.getExceptionThrow(ex, errorMessage);
        }
        return res;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> retrieveLot(@PathVariable final Long id) throws Exception {
        log.info("Retrieving parking lot... {}", id);
        final ResponseEntity<?> res;

        try {
            final Optional<ParkingLotDto> retrievedParkLotDtoOpt = parkingLotService.retrieveById(id);
            res = retrievedParkLotDtoOpt.map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid or non-existent park lot id"));

            log.info("Retrieved parking lot {}", retrievedParkLotDtoOpt.orElse(null));
        } catch (final Exception ex) {
            final String errorMessage = String.format("Failed retrieving parking lot %s %s ",
                id, ex.getMessage());
            log.error(errorMessage, ex);
            throw AppExceptionHandler.getExceptionThrow(ex, errorMessage);
        }
        return res;
    }
}
