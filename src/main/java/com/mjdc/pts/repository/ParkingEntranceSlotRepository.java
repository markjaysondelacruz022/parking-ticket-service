package com.mjdc.pts.repository;

import com.mjdc.pts.enumeration.Availability;
import com.mjdc.pts.model.ParkingEntrance;
import com.mjdc.pts.model.ParkingEntranceSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingEntranceSlotRepository extends JpaRepository<ParkingEntranceSlot, Long> {

    List<ParkingEntranceSlot> findAllByParkingEntranceAndParkingSlot_AvailabilityAndParkingEntrance_IsActive(ParkingEntrance parkingEntrance,
                                                                                                             Availability parkingSlot_availability,
                                                                                                             Boolean parkingEntrance_isActive);
    boolean existsByParkingEntranceIdAndParkingSlotId(Long parkingEntranceId, Long parkingSlotId);

    List<ParkingEntranceSlot> findByParkingEntranceId(Long parkingEntranceId);
}
