package com.flight.seat.command;

import com.flight.common.dto.SeatReservationRequest;

public record ReserveSeatCommand(SeatReservationRequest request) {
}

