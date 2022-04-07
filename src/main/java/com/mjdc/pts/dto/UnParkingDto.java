package com.mjdc.pts.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mjdc.pts.enumeration.Availability;
import com.mjdc.pts.enumeration.Size;
import com.mjdc.pts.util.DateUtil;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class UnParkingDto {

    @NotBlank(message = "Required")
    private String ticketId;

    @NotNull(message = "Required")
    private BigDecimal amountTender;
}
