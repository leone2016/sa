package com.flight.common.dto;

import com.flight.common.events.StepStatus;

/**
 * Response describing the outcome of a seat reservation attempt.
 */
public record SeatReservationResponse(
        String bookingId,
        String seatId,
        StepStatus status,
        String message
) {
}

