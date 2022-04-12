package com.mjdc.pts.service;

import com.mjdc.pts.dto.ParkingEntranceDto;
import com.mjdc.pts.dto.ParkingLotDto;
import com.mjdc.pts.model.ParkingEntrance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ParkingLotService {

    ParkingLotDto create(ParkingLotDto parkingLotDto);
    Optional<ParkingLotDto> retrieveById(Long id);
    Page<ParkingLotDto> retrievePageableSearch(String searchKey, Pageable pageable);
    List<ParkingLotDto> retrieveActiveIdAndNames();
    boolean isEntranceExistInParkingLot(Long lotId, Long entranceId);
    List<ParkingEntranceDto> retrieveParkingLotEntrances(Long lotId);


}
