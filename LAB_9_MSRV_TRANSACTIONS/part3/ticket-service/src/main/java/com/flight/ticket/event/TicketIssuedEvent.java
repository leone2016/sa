package com.flight.ticket.event;

import com.flight.common.events.SagaEvent;
import com.flight.common.events.StepStatus;

public class TicketIssuedEvent extends SagaEvent {
    public TicketIssuedEvent(String bookingId, String message) {
        super(bookingId, "TICKET", StepStatus.SUCCESS, message);
    }
}

