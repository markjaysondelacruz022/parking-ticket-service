package com.mjdc.pts.model;

import com.mjdc.pts.converter.SizeAttributeConverter;
import com.mjdc.pts.enumeration.PaymentStatus;
import com.mjdc.pts.enumeration.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "PARKING_TRANSACTION_DETAILS")
public class ParkingTransactionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TRANSACTION_ID", unique = true)
    private String transactionId;

    @Column(name = "TICKET_ID")
    private String ticketId;

    @Column(name = "DRIVER_NAME")
    private String driverName;

    @Column(name = "VEHICLE_DETAILS")
    private String vehicleDetails;

    @Column(name = "VEHICLE_PLATE_NUMBER")
    private String vehiclePlateNumber;

    @Convert(converter = SizeAttributeConverter.class)
    @Column(name = "VEHICLE_SIZE")
    private Size vehicleSize;

    @Column(name = "LOT", nullable = false)
    private String lot;

    @Column(name = "SLOT", nullable = false)
    private String slot;

    @Column(name = "ENTRANCE", nullable = false)
    private String entrance;

    @Convert(converter = SizeAttributeConverter.class)
    @Column(name = "SLOT_SIZE", nullable = false)
    private Size slotSize;

    @Column(name = "TOTAL_AMOUNT_DUE", nullable = false)
    private BigDecimal totalAmountDue;

    @Column(name = "AMOUNT_TENDER", nullable = false)
    private BigDecimal amountTender;

    @Column(name = "AMOUNT_CHANGE", nullable = false)
    private BigDecimal amountChange;

    @Column(name = "DATE_PARKED")
    private Date dateParked;

    @Column(name = "DATE_CHECKOUT")
    private Date dateCheckout;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_STATUS", nullable = false)
    private PaymentStatus paymentStatus;

    @CreationTimestamp
    @Column(name = "DATE_CREATED")
    private Date dateCreated;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "TRANSACTION_DETAILS_ID",  referencedColumnName = "ID",
        foreignKey = @ForeignKey(name = "PTD_TRANSACTION_DETAILS_ID_FK"), nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private List<ParkingTransactionItem> parkingTransactionItems;
}
