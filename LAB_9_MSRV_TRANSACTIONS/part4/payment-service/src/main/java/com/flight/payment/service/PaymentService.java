package com.flight.payment.service;

import com.flight.common.dto.PaymentResponse;
import com.flight.payment.command.ProcessPaymentCommand;
import com.flight.payment.command.RefundPaymentCommand;

public interface PaymentService {

    PaymentResponse processPayment(ProcessPaymentCommand command);

    PaymentResponse refundPayment(RefundPaymentCommand command);
}

