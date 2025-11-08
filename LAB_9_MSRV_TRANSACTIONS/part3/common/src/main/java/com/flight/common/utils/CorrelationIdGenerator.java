package com.flight.common.utils;

import java.util.UUID;

/**
 * Utility for generating correlation identifiers across microservices.
 */
public final class CorrelationIdGenerator {

    private CorrelationIdGenerator() {
    }

    public static String newId() {
        return UUID.randomUUID().toString();
    }
}

