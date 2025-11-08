package com.flight.common.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request to issue a ticket after successful seat reservation and payment.
 */
public record TicketIssueRequest(
        @NotBlank String bookingId,
        @NotBlank String customerId,
        @NotBlank String flightNumber,
        @NotBlank String seatNumber
) {
}

