package com.mjdc.pts.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mjdc.pts.enumeration.PriceType;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParkingTransactionItemDto {

    private Long id;
    private String ticketId;
    private String description;
    private PriceType type;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal amountDue;
}
