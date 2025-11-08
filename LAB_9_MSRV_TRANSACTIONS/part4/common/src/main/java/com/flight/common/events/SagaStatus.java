package com.flight.common.events;

/**
 * Represents the global state of a saga.
 */
public enum SagaStatus {
    STARTED,
    IN_PROGRESS,
    COMPLETED,
    COMPENSATING,
    FAILED
}

