package com.mjdc.pts.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mjdc.pts.model.ParkingEntrance;
import com.mjdc.pts.model.ParkingLot;
import com.mjdc.pts.model.ParkingSlot;
import com.mjdc.pts.util.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ParkingLotDto {

    private Long id;

    @NotBlank(message = "Required")
    private String name;

    @NotBlank(message = "Required")
    private String createdBy;

    private String updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_FORMAT)
    private LocalDateTime dateCreated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_FORMAT)
    private LocalDateTime dateUpdated;

    private Boolean isActive = true;

    private List<ParkingSlot> parkingSlots;

    private List<ParkingEntrance> parkingEntrances;

    public ParkingLotDto(final ParkingLot parkingLot) {
        this.id = parkingLot.getId();
        this.name = parkingLot.getName();
        this.createdBy = parkingLot.getCreatedBy();
        this.updatedBy = parkingLot.getUpdatedBy();
        this.dateCreated = parkingLot.getDateCreated();
        this.dateUpdated = parkingLot.getDateUpdated();
        this.isActive = parkingLot.getIsActive();
    }

    public ParkingLotDto(final Long id, final String name) {
        this.id = id;
        this.name = name;

    }


}
