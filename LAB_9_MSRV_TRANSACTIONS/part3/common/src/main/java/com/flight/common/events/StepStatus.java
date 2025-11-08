package com.flight.common.events;

/**
 * Represents the outcome of a single step in the saga.
 */
public enum StepStatus {
    SUCCESS,
    FAILED,
    COMPENSATED
}

