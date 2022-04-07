package com.mjdc.pts.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mjdc.pts.model.ParkingEntrance;
import com.mjdc.pts.model.ParkingSlot;
import com.mjdc.pts.util.DateUtil;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Set;

@Data
public class ParkingLotDto {

    private Long id;

    @NotBlank(message = "Required")
    private String name;

    @NotBlank(message = "Required")
    private String createdBy;

    private String updatedBy;

    @JsonFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    private Date dateCreated;

    @JsonFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    private Date dateUpdated;

    private Boolean isActive = true;

    private Set<ParkingSlot> parkingSlots;

    private Set<ParkingEntrance> parkingEntrances;


}
