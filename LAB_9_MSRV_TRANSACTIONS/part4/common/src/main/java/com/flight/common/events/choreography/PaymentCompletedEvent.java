package com.flight.common.events.choreography;

import java.math.BigDecimal;

/**
 * Event emitted by the payment service when a payment succeeds.
 */
public record PaymentCompletedEvent(
        String bookingId,
        String paymentId,
        String customerId,
        BigDecimal amount,
        String currency,
        String flightNumber,
        String seatNumber
) {
}


