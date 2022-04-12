package com.mjdc.pts.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mjdc.pts.enumeration.ParkingStatus;
import com.mjdc.pts.enumeration.Size;
import com.mjdc.pts.util.DateUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ParkingTicketDto {

    private Long id;
    private String ticketId;

    @NotBlank(message = "Required")
    private String driverName;

    @NotBlank(message = "Required")
    private String vehicleDetails;

    @NotBlank(message = "Required")
    private String vehiclePlateNumber;

    @NotNull(message = "Required")
    private Size vehicleSize;

    private ParkingStatus parkingStatus;

    private ParkingEntranceSlotDto parkingEntranceSlot;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_FORMAT)
    private LocalDateTime dateParked;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_FORMAT)
    private LocalDateTime dateCheckout;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_FORMAT)
    private LocalDateTime dateCreated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_FORMAT)
    private LocalDateTime dateUpdated;


}
