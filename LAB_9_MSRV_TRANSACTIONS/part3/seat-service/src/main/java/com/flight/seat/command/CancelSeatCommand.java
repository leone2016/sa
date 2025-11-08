package com.flight.seat.command;

public record CancelSeatCommand(String bookingId, String reason) {
}

