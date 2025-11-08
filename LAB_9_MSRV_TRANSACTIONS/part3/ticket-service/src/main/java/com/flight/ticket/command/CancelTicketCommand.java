package com.flight.ticket.command;

public record CancelTicketCommand(String bookingId, String reason) {
}

