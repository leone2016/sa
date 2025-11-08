package com.flight.payment.controller;

import com.flight.common.dto.PaymentRequest;
import com.flight.common.dto.PaymentResponse;
import com.flight.payment.command.ProcessPaymentCommand;
import com.flight.payment.command.RefundPaymentCommand;
import com.flight.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/api/payments/process")
    public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.processPayment(new ProcessPaymentCommand(request));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/payments/refund/{bookingId}")
    public ResponseEntity<PaymentResponse> refundPayment(
            @PathVariable String bookingId,
            @RequestParam(required = false) String reason
    ) {
        PaymentResponse response = paymentService.refundPayment(new RefundPaymentCommand(bookingId, reason));
        return ResponseEntity.ok(response);
    }
}

