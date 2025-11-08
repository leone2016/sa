package com.flight.common.events.choreography;

/**
 * Event emitted by the ticket service when issuing a ticket fails.
 */
public record TicketIssueFailedEvent(
        String bookingId,
        String reason
) {
}


