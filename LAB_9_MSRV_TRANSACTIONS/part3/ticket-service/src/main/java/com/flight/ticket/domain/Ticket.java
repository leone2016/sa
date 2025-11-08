package com.flight.ticket.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "tickets")
public class Ticket {

    @Id
    private String id;
    private String bookingId;
    private String customerId;
    private String flightNumber;
    private String seatNumber;
    private TicketStatus status;
    private Instant issuedAt = Instant.now();

    public Ticket() {
    }

    public Ticket(String bookingId, String customerId, String flightNumber, String seatNumber, TicketStatus status) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.flightNumber = flightNumber;
        this.seatNumber = seatNumber;
        this.status = status;
        this.issuedAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }
}

