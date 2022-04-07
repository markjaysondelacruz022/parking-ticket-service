package com.mjdc.pts.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjdc.pts.dto.ParkingLotDto;
import com.mjdc.pts.model.ParkingLot;
import com.mjdc.pts.repository.ParkingLotRepository;
import com.mjdc.pts.service.ParkingEntranceSlotService;
import com.mjdc.pts.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Service
public class ParkingLotServiceImpl implements ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;
    private final ParkingEntranceSlotService parkingEntranceSlotService;
    private final ObjectMapper objectMapper;

    @Override
    public ParkingLotDto create(final ParkingLotDto parkingLotDto) {
        final ParkingLot parkingLot = objectMapper.convertValue(parkingLotDto, ParkingLot.class);
        final ParkingLotDto convertedSaveParkingLotDto = objectMapper.convertValue(parkingLotRepository
            .save(parkingLot), ParkingLotDto.class);
        parkingEntranceSlotService.populateParkingEntranceSlot(convertedSaveParkingLotDto.getId());
        return convertedSaveParkingLotDto;
    }

    @Override
    public Optional<ParkingLotDto> retrieveById(final Long id) {
        return parkingLotRepository.findById(id).map(parkingLot -> objectMapper
            .convertValue(parkingLot, ParkingLotDto.class));
    }

    @Override
    public List<ParkingLotDto> retrieveParkLotListByActiveOptional(final Boolean isActive) {
        return null;
    }

}
