package com.mjdc.pts.service;

import com.mjdc.pts.dto.ParkingEntranceDto;
import com.mjdc.pts.dto.ParkingEntranceSlotDto;
import com.mjdc.pts.dto.ParkingSlotDto;
import com.mjdc.pts.enumeration.Size;

import java.util.Optional;

public interface ParkingEntranceSlotService {

    Optional<ParkingEntranceSlotDto> retrieveNearestAvailableSlotFromEntrance(final Long entranceId,
                                                                              final Size vehicleSize);

    boolean isExist(Long id);

    void populateParkingEntranceSlot(Long lotId);

    ParkingEntranceDto retrieveEntranceIfEmpty(ParkingEntranceDto parkingEntranceDto, Long entranceId);
    ParkingSlotDto retrieveSlotIfEmpty(ParkingSlotDto parkingSlotDto, Long slotId);
}
