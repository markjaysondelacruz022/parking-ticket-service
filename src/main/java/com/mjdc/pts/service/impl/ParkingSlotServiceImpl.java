package com.mjdc.pts.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjdc.pts.dto.ParkingEntranceSlotDto;
import com.mjdc.pts.dto.ParkingSlotDto;
import com.mjdc.pts.model.ParkingEntranceSlot;
import com.mjdc.pts.model.ParkingSlot;
import com.mjdc.pts.repository.ParkingSlotRepository;
import com.mjdc.pts.service.ParkingSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ParkingSlotServiceImpl implements ParkingSlotService {

    private final ParkingSlotRepository parkingSlotRepository;
    private final ObjectMapper objectMapper;

    @Override
    public ParkingSlotDto save(final ParkingSlotDto parkingSlotDto) {
        final ParkingSlot parkingSlot = objectMapper.convertValue(parkingSlotDto, ParkingSlot.class);
        this.setParkEntranceSlotsToEntity(parkingSlot, parkingSlotDto.getParkingEntranceSlots());
        final ParkingSlot savedParkingSlot = parkingSlotRepository.save(parkingSlot);
        final ParkingSlotDto mappedParkingSlotDto = objectMapper.convertValue(savedParkingSlot, ParkingSlotDto.class);
        this.setParkEntranceSlotsToDto(mappedParkingSlotDto, savedParkingSlot.getParkingEntranceSlots());
        return mappedParkingSlotDto;
    }

    private void setParkEntranceSlotsToDto(final ParkingSlotDto mappedParkingSlotDto,
                                           final List<ParkingEntranceSlot> parkingEntranceSlotList) {
        mappedParkingSlotDto.setParkingEntranceSlots(objectMapper
            .convertValue(parkingEntranceSlotList, new TypeReference<>() {}));
    }

    private void setParkEntranceSlotsToEntity(final ParkingSlot mappedParkingSlot,
                                           final List<ParkingEntranceSlotDto> parkEntranceSlotList) {
        mappedParkingSlot.setParkingEntranceSlots(objectMapper
            .convertValue(parkEntranceSlotList, new TypeReference<>() {}));
    }

    @Override
    public Optional<ParkingSlotDto> retrieveById(final Long id) {
        return parkingSlotRepository.findById(id)
            .map(parkingSlot -> {
                final ParkingSlotDto parkingSlotDto = objectMapper.convertValue(parkingSlot, ParkingSlotDto.class);
                this.setParkEntranceSlotsToDto(parkingSlotDto, parkingSlot.getParkingEntranceSlots());
                return parkingSlotDto;
            });
    }

    @Override
    public boolean isExist(Long id) {
        return parkingSlotRepository.existsById(id);
    }

    @Override
    public List<ParkingSlotDto> retrieveParkSlotListByLotIdAndActiveOptional(Long lotId, Boolean isActive) {
        return null;
    }

}
