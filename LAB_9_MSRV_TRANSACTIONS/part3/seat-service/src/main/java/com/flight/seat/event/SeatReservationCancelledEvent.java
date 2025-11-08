package com.flight.seat.event;

import com.flight.common.events.SagaEvent;
import com.flight.common.events.StepStatus;

public class SeatReservationCancelledEvent extends SagaEvent {
    public SeatReservationCancelledEvent(String bookingId, String message) {
        super(bookingId, "SEAT", StepStatus.COMPENSATED, message);
    }
}

