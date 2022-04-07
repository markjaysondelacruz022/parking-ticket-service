package com.mjdc.pts.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ParkingEntranceSlotDto implements Comparable<ParkingEntranceSlotDto> {

    private Long id;

    @NotNull(message = "Required")
    private Long parkingEntranceId;

    @NotNull(message = "Required")
    private Long parkingSlotId;

    private ParkingEntranceDto parkingEntrance;

    private ParkingSlotDto parkingSlot;

    @NotNull(message = "Required")
    private Integer distance;

    @Override
    public int compareTo(ParkingEntranceSlotDto o) {
        return this.getDistance().compareTo(o.getDistance());
    }
}
