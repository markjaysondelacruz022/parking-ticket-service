package com.mjdc.pts.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mjdc.pts.converter.SizeAttributeConverter;
import com.mjdc.pts.enumeration.Availability;
import com.mjdc.pts.enumeration.Size;
import com.mjdc.pts.util.DateUtil;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "PARKING_SLOT")
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"PARKING_LOT_ID", "NAME"})
})
public class ParkingSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", length = 50, nullable = false)
    private String name;

    @Convert(converter = SizeAttributeConverter.class)
    @Column(name = "SIZE", nullable = false)
    private Size size;

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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "PARKING_SLOT_ID",  referencedColumnName = "ID",
        foreignKey = @ForeignKey(name = "PSP_PARKING_SLOT_ID_FK"), nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private List<ParkingSlotPrice> parkingSlotPrices;

    @Column(name = "IS_ACTIVE", columnDefinition = "BIT(1) DEFAULT 1")
    private Boolean isActive;

    @Enumerated
    @Column(name = "AVAILABILITY", columnDefinition = "BIT(1) DEFAULT 1")
    private Availability availability = Availability.OK;

    @JsonIgnore
    @OneToMany(mappedBy = "parkingSlot", cascade = CascadeType.ALL)
    private List<ParkingEntranceSlot> parkingEntranceSlots;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "PARKING_LOT_ID", referencedColumnName = "ID",
        insertable = false, updatable = false, nullable = false)
    private ParkingLot parkingLot;

}
