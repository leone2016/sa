package com.flight.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Represents a booking request received by the saga orchestrator.
 */
public record BookingRequest(
        @NotBlank String bookingId,
        @NotBlank String flightNumber,
        @NotBlank String seatNumber,
        @NotBlank String customerId,
        @NotNull @Min(1) BigDecimal amount,
        @NotBlank String currency,
        @NotBlank String paymentMethod
) {
}

