package com.flight.ticket.repository;

import com.flight.ticket.domain.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TicketRepository extends MongoRepository<Ticket, String> {

    Optional<Ticket> findByBookingId(String bookingId);
}

