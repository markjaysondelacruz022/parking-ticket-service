package com.mjdc.pts.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mjdc.pts.enumeration.Availability;
import com.mjdc.pts.enumeration.Size;
import com.mjdc.pts.util.DateUtil;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
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

    @JsonFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    private Date dateCreated;

    @JsonFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    private Date dateUpdated;

    private List<ParkingSlotPriceDto> parkingSlotPrices;

    private Boolean isActive = true;

    private Availability availability = Availability.OK;

    private List<ParkingEntranceSlotDto> parkingEntranceSlots;

    private ParkingLotDto parkingLot;
}
