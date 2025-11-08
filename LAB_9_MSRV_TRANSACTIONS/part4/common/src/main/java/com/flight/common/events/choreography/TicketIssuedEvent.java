package com.flight.common.events.choreography;

/**
 * Event emitted by the ticket service when a ticket is successfully issued.
 */
public record TicketIssuedEvent(
        String bookingId,
        String ticketId,
        String flightNumber,
        String seatNumber,
        String customerId
) {
}


