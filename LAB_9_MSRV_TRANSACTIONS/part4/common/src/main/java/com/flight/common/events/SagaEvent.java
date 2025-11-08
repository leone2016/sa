package com.flight.common.events;

/**
 * Lightweight event object propagated between services if needed.
 */
public class SagaEvent {

    private final String bookingId;
    private final String step;
    private final StepStatus status;
    private final String message;

    public SagaEvent(String bookingId, String step, StepStatus status, String message) {
        this.bookingId = bookingId;
        this.step = step;
        this.status = status;
        this.message = message;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getStep() {
        return step;
    }

    public StepStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

