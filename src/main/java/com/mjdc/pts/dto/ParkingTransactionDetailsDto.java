package com.mjdc.pts.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mjdc.pts.enumeration.PaymentStatus;
import com.mjdc.pts.enumeration.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParkingTransactionDetailsDto {
    private Long id;
    private String transactionId;
    private String ticketId;
    private String driverName;
    private String vehicleDetails;
    private String vehiclePlateNumber;
    private Size vehicleSize;
    private String lot;
    private String slot;
    private String entrance;
    private Size slotSize;
    private BigDecimal totalAmountDue;
    private BigDecimal amountTender;
    private BigDecimal amountChange;
    private PaymentStatus paymentStatus;
    private Date dateParked;
    private Date dateCheckout;
    private Date dateCreated;
    private List<ParkingTransactionItemDto> parkingTransactionItems;
}
