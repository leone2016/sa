package com.flight.seat.repository;

import com.flight.seat.domain.SeatReservation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SeatReservationRepository extends MongoRepository<SeatReservation, String> {

    Optional<SeatReservation> findByBookingId(String bookingId);

    boolean existsByFlightNumberAndSeatNumber(String flightNumber, String seatNumber);
}

