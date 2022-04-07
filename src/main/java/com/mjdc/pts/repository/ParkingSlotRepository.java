package com.mjdc.pts.repository;

import com.mjdc.pts.model.ParkingSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {

    List<ParkingSlot> findByParkingLot_Id(Long parkingLotId);
}
