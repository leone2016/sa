package com.flight.common.events.choreography;

import java.math.BigDecimal;

/**
 * Event emitted by the seat service once a seat has been successfully reserved.
 * Carries the contextual information required by downstream services to continue
 * the saga choreography.
 */
public record SeatReservedEvent(
        String bookingId,
        String flightNumber,
        String seatNumber,
        String customerId,
        BigDecimal amount,
        String currency,
        String paymentMethod
) {
}


