package com.mjdc.pts.repository;

import com.mjdc.pts.dto.ParkingEntranceDto;
import com.mjdc.pts.model.ParkingEntrance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParkingEntranceRepository extends JpaRepository<ParkingEntrance, Long> {

    List<ParkingEntrance> findByParkingLot_Id(Long parkingLotId);

    @Query("select new com.mjdc.pts.dto.ParkingEntranceDto(pe.id, pe.name) from ParkingEntrance pe " +
        "inner join ParkingLot pl " +
        "on pl.id = pe.parkingLot.id " +
        "where pl.isActive = :isActive " +
        "and pe.isActive = :isActive and pe.parkingLot.id = :lotId")
    List<ParkingEntranceDto> getEntrancesIdAndNamesByIsActiveAndLotId(Long lotId, boolean isActive);
}
