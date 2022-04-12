package com.mjdc.pts.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mjdc.pts.dto.ParkingEntranceSlotDto;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@Entity(name = "PARKING_ENTRANCE_SLOT")
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"PARKING_SLOT_ID", "PARKING_ENTRANCE_ID"})
})
public class ParkingEntranceSlot implements Comparable<ParkingEntranceSlotDto> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PARKING_ENTRANCE_ID")
    private Long parkingEntranceId;

    @Column(name = "PARKING_SLOT_ID")
    private Long parkingSlotId;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PARKING_ENTRANCE_ID", referencedColumnName = "ID",
        foreignKey = @ForeignKey(name = "PES_PARKING_ENTRANCE_ID_FK"),
        insertable = false, updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private ParkingEntrance parkingEntrance;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PARKING_SLOT_ID",  referencedColumnName = "ID",
        foreignKey = @ForeignKey(name = "PES_PARKING_SLOT_ID_FK"),
        insertable = false, updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private ParkingSlot parkingSlot;

    @Column(name = "DISTANCE", length = 5)
    private Integer distance;

    @Override
    public int compareTo(ParkingEntranceSlotDto o) {
        return this.getDistance().compareTo(o.getDistance());
    }
}
