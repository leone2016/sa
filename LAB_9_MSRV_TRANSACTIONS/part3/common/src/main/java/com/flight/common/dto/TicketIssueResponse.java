package com.flight.common.dto;

import com.flight.common.events.StepStatus;

/**
 * Details about the ticket issuance result.
 */
public record TicketIssueResponse(
        String bookingId,
        String ticketId,
        StepStatus status,
        String message
) {
}

