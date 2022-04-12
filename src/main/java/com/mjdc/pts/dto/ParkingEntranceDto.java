package com.mjdc.pts.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class ParkingEntranceDto {

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

    private List<ParkingEntranceSlotDto> parkingEntranceSlots;

    private ParkingLotDto parkingLot;

    public ParkingEntranceDto(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }
}
