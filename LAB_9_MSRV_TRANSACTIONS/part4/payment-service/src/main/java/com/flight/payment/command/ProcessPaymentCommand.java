package com.flight.payment.command;

import com.flight.common.dto.PaymentRequest;

public record ProcessPaymentCommand(PaymentRequest request) {
}

