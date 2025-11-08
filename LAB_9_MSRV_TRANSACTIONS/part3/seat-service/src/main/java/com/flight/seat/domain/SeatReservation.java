package com.flight.seat.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "seat_reservations")
@CompoundIndex(name = "booking_seat_idx", def = "{'bookingId': 1, 'seatNumber': 1}", unique = true)
public class SeatReservation {

    @Id
    private String id;
    private String bookingId;
    private String flightNumber;
    private String seatNumber;
    private String customerId;
    private SeatReservationStatus status = SeatReservationStatus.RESERVED;

    public SeatReservation() {
    }

    public SeatReservation(String bookingId, String flightNumber, String seatNumber, String customerId) {
        this.bookingId = bookingId;
        this.flightNumber = flightNumber;
        this.seatNumber = seatNumber;
        this.customerId = customerId;
        this.status = SeatReservationStatus.RESERVED;
    }

    public String getId() {
        return id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public SeatReservationStatus getStatus() {
        return status;
    }

    public void setStatus(SeatReservationStatus status) {
        this.status = status;
    }
}

