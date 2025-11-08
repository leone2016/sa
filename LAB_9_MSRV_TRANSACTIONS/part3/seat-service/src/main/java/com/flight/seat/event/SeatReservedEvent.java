package com.flight.seat.event;

import com.flight.common.events.SagaEvent;
import com.flight.common.events.StepStatus;

public class SeatReservedEvent extends SagaEvent {
    public SeatReservedEvent(String bookingId, String message) {
        super(bookingId, "SEAT", StepStatus.SUCCESS, message);
    }
}

