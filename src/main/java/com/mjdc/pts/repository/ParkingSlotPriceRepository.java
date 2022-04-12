package com.mjdc.pts.repository;

import com.mjdc.pts.enumeration.Size;
import com.mjdc.pts.model.ParkingSlotPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingSlotPriceRepository extends JpaRepository<ParkingSlotPrice, Long> {

    List<ParkingSlotPrice> findBySize(Size size);
}
