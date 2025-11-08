package com.flight.payment.event;

import com.flight.common.events.SagaEvent;
import com.flight.common.events.StepStatus;

public class PaymentRefundedEvent extends SagaEvent {
    public PaymentRefundedEvent(String bookingId, String message) {
        super(bookingId, "PAYMENT", StepStatus.COMPENSATED, message);
    }
}

