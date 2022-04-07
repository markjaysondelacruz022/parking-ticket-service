package com.mjdc.pts.repository;

import com.mjdc.pts.model.ParkingEntrance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingEntranceRepository extends JpaRepository<ParkingEntrance, Long> {

    List<ParkingEntrance> findByParkingLot_Id(Long parkingLotId);
}
