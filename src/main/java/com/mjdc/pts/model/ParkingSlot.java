package com.mjdc.pts.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mjdc.pts.converter.SizeAttributeConverter;
import com.mjdc.pts.enumeration.Availability;
import com.mjdc.pts.enumeration.Size;
import com.mjdc.pts.util.DateUtil;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "PARKING_SLOT")
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"PARKING_LOT_ID", "NAME"})
})
public class ParkingSlot implements Serializable {
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_FORMAT)
    @CreationTimestamp
    @Column(name = "DATE_CREATED")
    private LocalDateTime dateCreated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_FORMAT)
    @UpdateTimestamp
    @Column(name = "DATE_UPDATED")
    private LocalDateTime dateUpdated;

    @Column(name = "IS_ACTIVE", columnDefinition = "BIT(1) DEFAULT 1")
    private Boolean isActive;

    @Enumerated
    @Column(name = "AVAILABILITY", columnDefinition = "BIT(1) DEFAULT 1")
    private Availability availability = Availability.OK;

    @OneToMany(mappedBy = "parkingSlot", cascade = CascadeType.ALL)
    private List<ParkingEntranceSlot> parkingEntranceSlots;

    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "PARKING_LOT_ID", referencedColumnName = "ID",
        insertable = false, updatable = false, nullable = false)
    private ParkingLot parkingLot;

}
