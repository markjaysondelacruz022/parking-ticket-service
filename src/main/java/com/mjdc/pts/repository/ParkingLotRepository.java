package com.mjdc.pts.repository;

import com.mjdc.pts.dto.ParkingLotDto;
import com.mjdc.pts.model.ParkingLot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {

    @Query(value = "select new com.mjdc.pts.dto.ParkingLotDto(pl) from ParkingLot pl" +
        " where pl.name like %:searchKey% or pl.createdBy like %:searchKey% or pl.updatedBy like %:searchKey%"
    ,countQuery = "select count(pl) from ParkingLot pl" +
        " where pl.name like %:searchKey% or pl.createdBy like %:searchKey% or pl.updatedBy like %:searchKey%" )
    Page<ParkingLotDto> getParkingLotPageable(final String searchKey, final Pageable pageable);

    boolean existsByIdAndParkingEntrances_Id(Long id, Long parkingEntrances_id);

    @Query("select new com.mjdc.pts.dto.ParkingLotDto(pl.id, pl.name) from ParkingLot pl where pl.isActive = :isActive")
    List<ParkingLotDto> getParkingLotIdAndNamesByIsActive(boolean isActive);

}
