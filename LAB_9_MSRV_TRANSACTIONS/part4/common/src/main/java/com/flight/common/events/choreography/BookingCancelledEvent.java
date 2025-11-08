package com.flight.common.events.choreography;

/**
 * Event emitted when a booking is cancelled as part of compensation.
 */
public record BookingCancelledEvent(
        String bookingId,
        String reason
) {
}


