package com.flight.common.dto;

import com.flight.common.events.SagaStatus;

/**
 * Represents the outcome of a booking orchestration.
 */
public record BookingResponse(
        String bookingId,
        SagaStatus status,
        String message
) {
}

