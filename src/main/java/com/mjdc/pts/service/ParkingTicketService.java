package com.mjdc.pts.service;

import com.mjdc.pts.dto.ParkingTicketDto;
import com.mjdc.pts.dto.ParkingTransactionDetailsDto;
import com.mjdc.pts.dto.ParkingTransactionItemDto;
import com.mjdc.pts.dto.UnParkingDto;

import java.util.List;
import java.util.Optional;

public interface ParkingTicketService {

    Optional<ParkingTicketDto> park(Long entranceId, ParkingTicketDto parkingTicketDto);
    Optional<ParkingTransactionDetailsDto> unPark(UnParkingDto unParkingDto);
    Optional<ParkingTransactionDetailsDto> retrieveCurrentCharges(String ticketId);

}
