package com.flight.common.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request object for reserving a seat.
 */
public record SeatReservationRequest(
        @NotBlank String bookingId,
        @NotBlank String flightNumber,
        @NotBlank String seatNumber,
        @NotBlank String customerId
) {
}

