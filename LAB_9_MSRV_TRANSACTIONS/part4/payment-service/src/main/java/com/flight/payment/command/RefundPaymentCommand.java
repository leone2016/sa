package com.flight.payment.command;

public record RefundPaymentCommand(String bookingId, String reason) {
}

