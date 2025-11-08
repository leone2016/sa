package com.flight.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

/**
 * Request object for processing a payment.
 */
public record PaymentRequest(
        @NotBlank String bookingId,
        @NotBlank String customerId,
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
        @NotBlank String currency,
        @NotBlank String paymentMethod
) {
}

