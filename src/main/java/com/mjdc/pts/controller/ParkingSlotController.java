package com.mjdc.pts.controller;

import com.mjdc.pts.dto.ParkingSlotDto;
import com.mjdc.pts.dto.ResponseDto;
import com.mjdc.pts.enumeration.Size;
import com.mjdc.pts.exception.AppExceptionHandler;
import com.mjdc.pts.service.ParkingEntranceSlotService;
import com.mjdc.pts.service.ParkingSlotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/parking-slot")
public class ParkingSlotController {

    private final ParkingSlotService parkingSlotService;
    private final ParkingEntranceSlotService parkingEntranceSlotService;

    @PostMapping
    public ResponseEntity<?> createSLot(@Valid @RequestBody final ParkingSlotDto parkingSlotDto) throws Exception {
        log.info("Creating parking slot... {}", parkingSlotDto);
        final ResponseEntity<?> res;

        try {
            final ParkingSlotDto createdParkLotDto = parkingSlotService.save(parkingSlotDto);
            res = new ResponseEntity<>(ResponseDto.builder()
                .message("Successfully created")
                .data(createdParkLotDto)
                .build(), HttpStatus.OK);
            log.info("Created parking slot {}", createdParkLotDto);
        } catch (final Exception ex) {
            final String errorMessage = String.format("Failed creating parking slot %s %s ",
                parkingSlotDto.getName(), ex.getMessage());
            log.error(errorMessage, ex);
            throw AppExceptionHandler.getExceptionThrow(ex, errorMessage);
        }
        return res;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSLot(@PathVariable final Long id,
                                        @Valid @RequestBody final ParkingSlotDto parkingSlotDto) throws Exception {
        log.info("Updating parking slot... {}", id);
        final ResponseEntity<?> res;

        try {

            if (Optional.ofNullable(parkingSlotDto.getId()).isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No park slot id found in request body");
            } else if (!id.equals(parkingSlotDto.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id parametes doesn't match");
            } else if (!parkingSlotService.isExist(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or non-existent park slot id");
            } else {
                final ParkingSlotDto createdParkLotDto = parkingSlotService.save(parkingSlotDto);
                res = new ResponseEntity<>(ResponseDto.builder()
                    .message("Successfully created")
                    .data(createdParkLotDto)
                    .build(), HttpStatus.OK);
                log.info("Created parking slot {}", createdParkLotDto);
            }

        } catch (final Exception ex) {

            final String errorMessage = String.format("Failed updating parking slot %s %s ",
                parkingSlotDto.getName(), ex.getMessage());
            log.error(errorMessage, ex);
            throw AppExceptionHandler.getExceptionThrow(ex, errorMessage);
        }
        return res;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSLot(@PathVariable final Long id) throws Exception {
        log.info("Retrieving parking slot... {}", id);
        final ResponseEntity<?> res;

        try {
            res = parkingSlotService.retrieveById(id).map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid or non-existent park slot id"));
        } catch (final Exception ex) {

            final String errorMessage = String.format("Failed retrieving parking slot %s %s ",
                id, ex.getMessage());
            log.error(errorMessage, ex);
            throw AppExceptionHandler.getExceptionThrow(ex, errorMessage);
        }
        return res;
    }

    @GetMapping("/available/entrance/{entranceId}")
    public ResponseEntity<?> getAvailableSlotFromEntrance(@PathVariable final Long entranceId,
                                                          @RequestParam final Size vehicleSize) throws Exception {
        log.info("Retrieving available and nearest parking slot from entrance {}...", entranceId);
        final ResponseEntity<?> res;

        try {
            res = parkingEntranceSlotService.retrieveNearestAvailableSlotFromEntrance(entranceId, vehicleSize)
                .map(parkingEntranceSlotDto -> new ResponseEntity<>(ResponseDto.builder()
                    .message("Successfully retrieved available slot")
                    .data(parkingEntranceSlotDto).build(), HttpStatus.OK))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid entrance id or no available slot from entrance ".concat(String.valueOf(entranceId))));
        } catch (final Exception ex) {
            final String errorMessage = String
                .format("Failed retrieving available and nearest parking slot from entrance %s %s ",
                entranceId, ex.getMessage());
            log.error(errorMessage, ex);
            throw AppExceptionHandler.getExceptionThrow(ex, errorMessage);
        }
        return res;
    }

}
