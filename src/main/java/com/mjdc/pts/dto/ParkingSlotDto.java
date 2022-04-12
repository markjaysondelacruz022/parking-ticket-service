package com.mjdc.pts.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mjdc.pts.enumeration.Availability;
import com.mjdc.pts.enumeration.Size;
import com.mjdc.pts.util.DateUtil;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ParkingSlotDto {
    private Long id;

    @NotBlank(message = "Required")
    private String name;

    @NotNull(message = "Required")
    private Size size;

    @NotNull(message = "Required")
    private String createdBy;

    private String updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_FORMAT)
    private LocalDateTime dateCreated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_FORMAT)
    private LocalDateTime dateUpdated;

    private Boolean isActive = true;

    private Availability availability = Availability.OK;

    private List<ParkingEntranceSlotDto> parkingEntranceSlots;

    private ParkingLotDto parkingLot;
}
