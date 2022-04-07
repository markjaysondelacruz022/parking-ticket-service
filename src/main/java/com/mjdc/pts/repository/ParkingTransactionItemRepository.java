package com.mjdc.pts.repository;

import com.mjdc.pts.model.ParkingTransactionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingTransactionItemRepository extends JpaRepository<ParkingTransactionItem, Long> {
}
