package com.flight.common.events.choreography;

/**
 * Shared constants for routing choreography events across services.
 */
public final class BookingEventConstants {

    private BookingEventConstants() {
    }

    public static final String EXCHANGE = "booking.events";

    public static final String ROUTING_SEAT_RESERVED = "seat.reserved";
    public static final String ROUTING_PAYMENT_COMPLETED = "payment.completed";
    public static final String ROUTING_PAYMENT_FAILED = "payment.failed";
    public static final String ROUTING_TICKET_ISSUED = "ticket.issued";
    public static final String ROUTING_TICKET_FAILED = "ticket.failed";
    public static final String ROUTING_BOOKING_CANCELLED = "booking.cancelled";
}


