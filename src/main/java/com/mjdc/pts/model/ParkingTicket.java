package com.mjdc.pts.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mjdc.pts.converter.ParkingStatusAttributeConverter;
import com.mjdc.pts.converter.SizeAttributeConverter;
import com.mjdc.pts.enumeration.ParkingStatus;
import com.mjdc.pts.enumeration.Size;
import com.mjdc.pts.util.DateUtil;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "PARKING_TICKET")
public class ParkingTicket implements Comparable<ParkingTicket>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TICKET_ID", nullable = false, unique = true)
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

    @Convert(converter = ParkingStatusAttributeConverter.class)
    @Column(name = "PARKING_STATUS")
    private ParkingStatus parkingStatus;

    @OneToOne
    @JoinColumn(name = "PARKING_ENTRANCE_SLOT_ID", referencedColumnName = "ID",
        foreignKey = @ForeignKey(name = "T_PARKING_ENTRANCE_SLOT_ID_FK"), nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private ParkingEntranceSlot parkingEntranceSlot;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_FORMAT)
    @Column(name = "DATE_PARKED")
    private LocalDateTime dateParked;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_FORMAT)
    @Column(name = "DATE_CHECKOUT")
    private LocalDateTime dateCheckout;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_FORMAT)
    @CreationTimestamp
    @Column(name = "DATE_CREATED")
    private LocalDateTime dateCreated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_FORMAT)
    @UpdateTimestamp
    @Column(name = "DATE_UPDATED")
    private LocalDateTime dateUpdated;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "PREV_PARKING_TRANSACTION_DETAILS_ID", referencedColumnName = "ID",
        foreignKey = @ForeignKey(name = "PT_PREV_PARKING_TRANSACTION_DETAILS_ID_FK"), updatable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private ParkingTransactionDetails previousParkingTransactionDetails;

    @Override
    public int compareTo(ParkingTicket o) {
        return o.getId().compareTo(this.getId());
    }
}
