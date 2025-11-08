package com.flight.payment.service;

import com.flight.common.dto.PaymentResponse;
import com.flight.common.events.StepStatus;
import com.flight.payment.command.ProcessPaymentCommand;
import com.flight.payment.command.RefundPaymentCommand;
import com.flight.payment.domain.Payment;
import com.flight.payment.domain.PaymentStatus;
import com.flight.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentResponse processPayment(ProcessPaymentCommand command) {
        var request = command.request();
        var existing = paymentRepository.findByBookingId(request.bookingId());
        if (existing.isPresent()) {
            var payment = existing.orElseThrow();
            return new PaymentResponse(
                    payment.getBookingId(),
                    payment.getId(),
                    payment.getAmount(),
                    payment.getStatus() == PaymentStatus.APPROVED ? StepStatus.SUCCESS : StepStatus.FAILED,
                    "Payment already processed"
            );
        }

        PaymentStatus status = determineStatus(request.paymentMethod());
        Payment payment = new Payment(
                request.bookingId(),
                request.customerId(),
                request.amount(),
                request.currency(),
                request.paymentMethod(),
                status
        );
        Payment saved = paymentRepository.save(payment);
        StepStatus stepStatus = status == PaymentStatus.APPROVED ? StepStatus.SUCCESS : StepStatus.FAILED;
        String message = status == PaymentStatus.APPROVED ? "Payment approved" : "Payment declined";
        return new PaymentResponse(saved.getBookingId(), saved.getId(), saved.getAmount(), stepStatus, message);
    }

    @Override
    public PaymentResponse refundPayment(RefundPaymentCommand command) {
        return paymentRepository.findByBookingId(command.bookingId())
                .map(payment -> {
                    payment.setStatus(PaymentStatus.REFUNDED);
                    Payment saved = paymentRepository.save(payment);
                    return new PaymentResponse(
                            saved.getBookingId(),
                            saved.getId(),
                            saved.getAmount(),
                            StepStatus.COMPENSATED,
                            command.reason() == null ? "Payment refunded" : command.reason()
                    );
                })
                .orElseGet(() -> new PaymentResponse(
                        command.bookingId(),
                        null,
                        null,
                        StepStatus.FAILED,
                        "Payment not found for refund"
                ));
    }

    private PaymentStatus determineStatus(String paymentMethod) {
        if (paymentMethod == null) {
            return PaymentStatus.DECLINED;
        }
        String normalized = paymentMethod.toUpperCase(Locale.ROOT);
        if (normalized.contains("DECLINE") || normalized.contains("FAIL")) {
            return PaymentStatus.DECLINED;
        }
        return PaymentStatus.APPROVED;
    }
}

