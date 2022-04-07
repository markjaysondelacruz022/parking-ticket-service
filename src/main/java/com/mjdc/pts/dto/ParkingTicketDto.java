package com.mjdc.pts.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mjdc.pts.enumeration.ParkingStatus;
import com.mjdc.pts.enumeration.Size;
import com.mjdc.pts.util.DateUtil;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
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

    @JsonFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    private Date dateParked;

    @JsonFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    private Date dateCheckout;

    @JsonFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    private Date dateCreated;

    @JsonFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    private Date dateUpdated;
}
