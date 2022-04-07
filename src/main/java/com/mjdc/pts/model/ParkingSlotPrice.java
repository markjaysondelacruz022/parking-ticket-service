package com.mjdc.pts.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mjdc.pts.converter.PriceTypeAttributeConverter;
import com.mjdc.pts.enumeration.PriceType;
import com.mjdc.pts.util.DateUtil;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity(name = "PARKING_SLOT_PRICE")
public class ParkingSlotPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @Column(name = "HOUR", nullable = false)
    private Integer hour;

    @Convert(converter = PriceTypeAttributeConverter.class)
    @Column(name = "TYPE", nullable = false)
    private PriceType type;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @CreationTimestamp
    @JsonFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    @Column(name = "DATE_CREATED")
    private Date dateCreated;

    @UpdateTimestamp
    @JsonFormat(pattern = DateUtil.DATE_TIME_FORMAT)
    @Column(name = "DATE_UPDATED")
    private Date dateUpdated;

}
