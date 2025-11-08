package com.flight.seat.command;

import com.flight.common.dto.BookingRequest;

public record ReserveSeatCommand(BookingRequest request) {
}

