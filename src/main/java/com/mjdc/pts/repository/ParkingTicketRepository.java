package com.mjdc.pts.repository;

import com.mjdc.pts.enumeration.ParkingStatus;
import com.mjdc.pts.model.ParkingTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ParkingTicketRepository extends JpaRepository<ParkingTicket, Long> {

    boolean existsByTicketId(String ticketId);

    boolean existsByVehiclePlateNumberAndParkingStatus(String vehiclePlateNumber, ParkingStatus parkingStatus);

    List<ParkingTicket> findByVehiclePlateNumberAndParkingStatusAndDateCheckoutGreaterThanEqual(String vehiclePlateNumber,
                                                                                                ParkingStatus parkingStatus,
                                                                                                LocalDateTime currentDateTimeMinusHour);

    @Query("select MAX( pt.ticketId) from ParkingTicket pt")
    String getMaxTicketId();

    Optional<ParkingTicket> findByTicketId(String ticketId);

    @Query(value = "select pt from ParkingTicket pt" +
        " left join ParkingTransactionDetails ptd" +
        " on ptd.ticketId = pt.ticketId" +
        " where ptd.id is null and (pt.driverName like %:searchKey% or pt.vehicleDetails like %:searchKey% or pt.vehiclePlateNumber like %:searchKey%)"
        ,countQuery = "select count(pt.id) from ParkingTicket pt" +
        " left join ParkingTransactionDetails ptd" +
        " on ptd.ticketId = pt.ticketId" +
        " where ptd.id is null and (pt.driverName like %:searchKey% or pt.vehicleDetails like %:searchKey% or pt.vehiclePlateNumber like %:searchKey%)")
    Page<ParkingTicket> getParkingTicketBySearchKeyPageable(final String searchKey, final Pageable pageable);
}
