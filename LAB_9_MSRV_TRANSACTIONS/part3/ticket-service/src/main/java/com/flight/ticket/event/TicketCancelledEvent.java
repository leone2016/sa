package com.flight.ticket.event;

import com.flight.common.events.SagaEvent;
import com.flight.common.events.StepStatus;

public class TicketCancelledEvent extends SagaEvent {
    public TicketCancelledEvent(String bookingId, String message) {
        super(bookingId, "TICKET", StepStatus.COMPENSATED, message);
    }
}

