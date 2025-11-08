package com.flight.saga.exception;

public class SagaOrchestrationException extends RuntimeException {
    public SagaOrchestrationException(String message, Throwable cause) {
        super(message, cause);
    }
}

