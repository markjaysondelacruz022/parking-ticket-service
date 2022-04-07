package com.mjdc.pts.repository;

import com.mjdc.pts.dto.ReferenceId;
import com.mjdc.pts.model.ParkingTransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ParkingTransactionDetailsRepository extends JpaRepository<ParkingTransactionDetails, Long> {

    boolean existsByTransactionId(String transactionId);

    Optional<ParkingTransactionDetails> findByTicketId(String ticketId);

    @Query("select new com.mjdc.pts.dto.ReferenceId(MAX( ptd.transactionId)) from PARKING_TRANSACTION_DETAILS ptd")
    ReferenceId getMaxTransactionId();
}
