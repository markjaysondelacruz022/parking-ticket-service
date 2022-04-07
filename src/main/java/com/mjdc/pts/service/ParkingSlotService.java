package com.mjdc.pts.service;

import com.mjdc.pts.dto.ParkingSlotDto;

import java.util.List;
import java.util.Optional;

public interface ParkingSlotService {

    ParkingSlotDto save(ParkingSlotDto parkLotDto);
    Optional<ParkingSlotDto> retrieveById(Long id);
    boolean isExist(Long id);
    List<ParkingSlotDto> retrieveParkSlotListByLotIdAndActiveOptional(Long lotId, Boolean isActive);


}
