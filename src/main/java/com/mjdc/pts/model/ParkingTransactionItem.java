package com.mjdc.pts.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mjdc.pts.converter.PriceTypeAttributeConverter;
import com.mjdc.pts.enumeration.PriceType;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity(name = "PARKING_TRANSACTION_ITEM")
public class ParkingTransactionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Convert(converter = PriceTypeAttributeConverter.class)
    @Column(name = "TYPE")
    private PriceType type;

    @Column(name = "UNIT_PRICE")
    private BigDecimal unitPrice;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "AMOUNT_DUE")
    private BigDecimal amountDue;

    @Column(name = "DESCRIPTION")
    private String description;

}
