package com.mjdc.pts.repository;

import com.mjdc.pts.model.ParkingSlotPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingSlotPriceRepository extends JpaRepository<ParkingSlotPrice, Long> {
}
