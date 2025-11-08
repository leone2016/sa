package com.flight.payment.event;

import com.flight.common.events.SagaEvent;
import com.flight.common.events.StepStatus;

public class PaymentProcessedEvent extends SagaEvent {
    public PaymentProcessedEvent(String bookingId, StepStatus status, String message) {
        super(bookingId, "PAYMENT", status, message);
    }
}

