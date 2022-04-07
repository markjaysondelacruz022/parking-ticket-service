package com.mjdc.pts.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mjdc.pts.converter.PriceTypeAttributeConverter;
import com.mjdc.pts.enumeration.PriceType;
import com.mjdc.pts.util.DateUtil;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ParkingSlotPriceDto {

    private Long id;
    @NotNull(message = "Required")
    private BigDecimal price;

    @NotNull(message = "Required")
    private Integer hour;

    @NotNull(message = "Required")
    private PriceType type;

    @NotNull(message = "Required")
    private String createdBy;

    private String updatedBy;

    @JsonFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    private Date dateCreated;

    @JsonFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    private Date dateUpdated;

}
