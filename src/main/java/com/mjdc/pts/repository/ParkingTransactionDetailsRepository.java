package com.mjdc.pts.repository;

import com.mjdc.pts.model.ParkingTransactionDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ParkingTransactionDetailsRepository extends JpaRepository<ParkingTransactionDetails, Long> {

    boolean existsByTransactionId(String transactionId);

    Optional<ParkingTransactionDetails> findByTicketId(String ticketId);

    @Query("select MAX( ptd.transactionId) from ParkingTransactionDetails ptd")
    String getMaxTransactionId();

    @Query(value = "select pt from ParkingTransactionDetails pt" +
        " where pt.driverName like %:searchKey% or pt.vehicleDetails like %:searchKey% or pt.vehiclePlateNumber like %:searchKey%"
        ,countQuery = "select count(pt.id) from ParkingTransactionDetails pt" +
        " where pt.driverName like %:searchKey% or pt.vehicleDetails like %:searchKey% or pt.vehiclePlateNumber like %:searchKey%")
    Page<ParkingTransactionDetails> getParkingTransactionBySearchKeyPageable(final String searchKey, final Pageable pageable);
}
