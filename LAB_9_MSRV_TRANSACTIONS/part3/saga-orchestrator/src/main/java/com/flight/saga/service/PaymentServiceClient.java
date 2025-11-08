package com.flight.saga.service;

import com.flight.common.dto.PaymentRequest;
import com.flight.common.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment-service", url = "${services.payment}")
public interface PaymentServiceClient {

    @PostMapping("/api/payments/process")
    PaymentResponse processPayment(@RequestBody PaymentRequest request);

    @PostMapping("/api/payments/refund/{bookingId}")
    PaymentResponse refundPayment(@PathVariable String bookingId, @RequestParam(required = false) String reason);
}

