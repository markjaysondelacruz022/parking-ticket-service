package com.mjdc.pts.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mjdc.pts.enumeration.PriceType;
import com.mjdc.pts.enumeration.Size;
import com.mjdc.pts.util.DateUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ParkingSlotPriceDto {

    private Long id;
    private BigDecimal price;
    private Size size;
    private Integer hour;
    private PriceType type;
    private String createdBy;

    private String updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_FORMAT)
    private LocalDateTime dateCreated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_FORMAT)
    private LocalDateTime dateUpdated;

}
