package com.mjdc.pts.service;

import com.mjdc.pts.dto.ParkingLotDto;

import java.util.List;
import java.util.Optional;

public interface ParkingLotService {

    ParkingLotDto create(ParkingLotDto parkingLotDto);
    Optional<ParkingLotDto> retrieveById(Long id);
    List<ParkingLotDto> retrieveParkLotListByActiveOptional(Boolean isActive);


}
