package com.flight.common.events.choreography;

/**
 * Event emitted by the payment service when a payment authorization fails.
 */
public record PaymentFailedEvent(
        String bookingId,
        String reason
) {
}


