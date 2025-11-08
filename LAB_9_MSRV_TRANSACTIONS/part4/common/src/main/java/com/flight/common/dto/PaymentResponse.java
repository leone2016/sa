package com.flight.common.dto;

import com.flight.common.events.StepStatus;

import java.math.BigDecimal;

/**
 * Response describing the payment processing result.
 */
public record PaymentResponse(
        String bookingId,
        String paymentId,
        BigDecimal amount,
        StepStatus status,
        String message
) {
}

