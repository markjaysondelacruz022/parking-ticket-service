package com.mjdc.pts.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mjdc.pts.util.DateUtil;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "PARKING_ENTRANCE")
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"PARKING_LOT_ID", "NAME"})
})
public class ParkingEntrance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", length = 50, nullable = false)
    private String name;

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

    @Column(name = "IS_ACTIVE", columnDefinition = "BIT(1) DEFAULT 1")
    private Boolean isActive = true;

    @JsonIgnore
    @OneToMany(mappedBy = "parkingEntrance", cascade = CascadeType.ALL)
    private List<ParkingEntranceSlot> parkingEntranceSlots;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "PARKING_LOT_ID", referencedColumnName = "ID", insertable = false, updatable = false, nullable = false)
    private ParkingLot parkingLot;

}
