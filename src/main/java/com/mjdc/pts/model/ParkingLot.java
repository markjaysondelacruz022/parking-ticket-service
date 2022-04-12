package com.mjdc.pts.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mjdc.pts.util.DateUtil;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "PARKING_LOT")
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", length = 50, nullable = false, unique = true)
    private String name;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_FORMAT)
    @CreationTimestamp
    @Column(name = "DATE_CREATED")
    private LocalDateTime dateCreated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_FORMAT)
    @UpdateTimestamp
    @Column(name = "DATE_UPDATED")
    private LocalDateTime dateUpdated;

    @Column(name = "IS_ACTIVE", columnDefinition = "BIT(1) DEFAULT 1")
    private Boolean isActive = true;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "PARKING_LOT_ID", referencedColumnName = "ID",
        foreignKey = @ForeignKey(name = "PS_PARKING_LOT_ID"), nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private List<ParkingSlot> parkingSlots;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "PARKING_LOT_ID", referencedColumnName = "ID",
        foreignKey = @ForeignKey(name = "PE_PARKING_LOT_ID"), nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private List<ParkingEntrance> parkingEntrances;


}
