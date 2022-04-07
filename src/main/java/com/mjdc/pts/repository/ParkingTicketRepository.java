package com.mjdc.pts.repository;

import com.mjdc.pts.dto.ReferenceId;
import com.mjdc.pts.enumeration.ParkingStatus;
import com.mjdc.pts.model.ParkingTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ParkingTicketRepository extends JpaRepository<ParkingTicket, Long> {

    boolean existsByTicketId(String ticketId);

    boolean existsByVehiclePlateNumberAndParkingStatus(String vehiclePlateNumber, ParkingStatus parkingStatus);

    List<ParkingTicket> findByVehiclePlateNumberAndParkingStatusAndDateCheckoutGreaterThanEqual(String vehiclePlateNumber,
                                                                                                ParkingStatus parkingStatus,
                                                                                                Date currentDateTimeMinusHour);

    @Query("select new com.mjdc.pts.dto.ReferenceId(MAX( pt.ticketId)) from PARKING_TICKET pt")
    ReferenceId getMaxTicketId();

    Optional<ParkingTicket> findByTicketId(String ticketId);
}
