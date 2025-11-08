package com.flight.seat.service;

import com.flight.common.dto.SeatReservationResponse;
import com.flight.seat.command.CancelSeatCommand;
import com.flight.seat.command.ReserveSeatCommand;

public interface SeatReservationService {

    SeatReservationResponse reserveSeat(ReserveSeatCommand command);

    SeatReservationResponse cancelReservation(CancelSeatCommand command);
}

