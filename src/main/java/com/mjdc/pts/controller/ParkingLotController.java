package com.mjdc.pts.controller;

import com.mjdc.pts.dto.ParkingEntranceDto;
import com.mjdc.pts.dto.ParkingLotDto;
import com.mjdc.pts.dto.ResponseDto;
import com.mjdc.pts.exception.AppExceptionHandler;
import com.mjdc.pts.exception.ParkingException;
import com.mjdc.pts.service.ParkingLotService;
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
import java.util.List;
import java.util.Map;
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
            res = retrievedParkLotDtoOpt
                .map(parkingLotDto ->  new ResponseEntity<>(ResponseDto.builder()
                    .message("Successfully retrieved").data(parkingLotDto).build(), HttpStatus.OK))
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

    @PostMapping("/search")
    public ResponseEntity<?> retrieveSearch(@RequestParam final Integer pageNo,
                                            @RequestParam final Integer pageSize,
                                            @RequestParam(required = false,
                                                defaultValue = "dateCreated") final String sortBy,
                                            @RequestParam(required = false,
                                                defaultValue = "desc") final String sortOrder,
                                            @RequestBody final Map<String, String> body) throws Exception {
        log.info("Searching parking lots... {}", body);
        final ResponseEntity<?> res;

        try {

            if (body.isEmpty()) {
                throw new ParkingException("No request body found", HttpStatus.BAD_REQUEST);
            } else if (body.size() > 1) {
                throw new ParkingException("Invalid request body", HttpStatus.BAD_REQUEST);
            } else if (Optional.ofNullable(body.get("searchKey")).isEmpty()) {
                throw new ParkingException("Invalid request body search key found", HttpStatus.BAD_REQUEST);
            }

            final Sort sort = "asc".equalsIgnoreCase(sortOrder)
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
            final Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

            final Page<ParkingLotDto> searchedParkingLots = parkingLotService
                .retrievePageableSearch(Optional.ofNullable(body.get("searchKey")).orElse(""), pageable);

            res = new ResponseEntity<>(ResponseDto.builder()
                .message("Retrieved parking lot")
                .data(searchedParkingLots).build(), HttpStatus.OK);

            log.info("Searched parking lots size{}", searchedParkingLots.getContent().size());
        } catch (final Exception ex) {
            final String errorMessage = String.format("Failed searching parking lot %s %s ",
                body, ex.getMessage());
            log.error(errorMessage, ex);
            throw AppExceptionHandler.getExceptionThrow(ex, errorMessage);
        }
        return res;
    }

    @GetMapping("/id-name/active")
    public ResponseEntity<?> retrieveIdAndNames() throws Exception {
        log.info("Retrieving active parking lot id and name...");
        final ResponseEntity<?> res;

        try {
            final List<ParkingLotDto> retrievedActiveIdAndNames = parkingLotService.retrieveActiveIdAndNames();
            res = new ResponseEntity<>(ResponseDto.builder()
                .message("Successfully retrieve parking lots")
                .data(retrievedActiveIdAndNames).build(), HttpStatus.OK);

            log.info("Retrieved active parking lot ids and names size {}", retrievedActiveIdAndNames.size());
        } catch (final Exception ex) {
            final String errorMessage = String.format("Failed retrieving active parking lot id and name %s ", ex.getMessage());
            log.error(errorMessage, ex);
            throw AppExceptionHandler.getExceptionThrow(ex, errorMessage);
        }
        return res;
    }
    @GetMapping("/{lotId}/entrances/id-name/active")
    public ResponseEntity<?> retrieveEntranceIdAndNames(@PathVariable final Long lotId) throws Exception {
        log.info("Retrieving active parking lot entrances lot id {}...", lotId);
        final ResponseEntity<?> res;

        try {
            final List<ParkingEntranceDto> retrievedActiveIdAndNames = parkingLotService
                .retrieveParkingLotEntrances(lotId);
            res = new ResponseEntity<>(ResponseDto.builder()
                .message("Successfully retrieve parking lot entrances")
                .data(retrievedActiveIdAndNames).build(), HttpStatus.OK);

            log.info("Retrieved active parking lot entrances size {}", retrievedActiveIdAndNames.size());
        } catch (final Exception ex) {
            final String errorMessage = String.format("Failed retrieving active parking lot entrances %s ", ex.getMessage());
            log.error(errorMessage, ex);
            throw AppExceptionHandler.getExceptionThrow(ex, errorMessage);
        }
        return res;
    }
}
