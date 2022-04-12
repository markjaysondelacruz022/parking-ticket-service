package com.mjdc.pts.service;

import com.mjdc.pts.dto.*;
import com.mjdc.pts.model.ParkingTransactionDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ParkingTicketService {

    Optional<ParkingTicketDto> park(Long lotId,
                                    Long entranceId,
                                    ParkingTicketDto parkingTicketDto);
    Optional<ParkingTransactionDetailsDto> unPark(UnParkingDto unParkingDto);
    Optional<ParkingTransactionDetailsDto> retrieveCurrentCharges(String ticketId);
    Optional<ParkingTicketDto> retrieveTicketByTicketId(String ticketId);
    Page<ParkingTicketDto> retrieveTicketBySearchKeyPageable(String searchKey, Pageable pageable);
    Page<ParkingTransactionDetailsDto> retrieveTransactionBySearchKeyPageable(String searchKey, Pageable pageable);
}
