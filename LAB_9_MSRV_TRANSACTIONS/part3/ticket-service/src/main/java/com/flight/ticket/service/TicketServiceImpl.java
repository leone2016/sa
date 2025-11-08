package com.flight.ticket.service;

import com.flight.common.dto.TicketIssueResponse;
import com.flight.common.events.StepStatus;
import com.flight.ticket.command.CancelTicketCommand;
import com.flight.ticket.command.IssueTicketCommand;
import com.flight.ticket.domain.Ticket;
import com.flight.ticket.domain.TicketStatus;
import com.flight.ticket.repository.TicketRepository;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public TicketIssueResponse issueTicket(IssueTicketCommand command) {
        var request = command.request();
        var existing = ticketRepository.findByBookingId(request.bookingId());
        if (existing.isPresent()) {
            var ticket = existing.orElseThrow();
            return new TicketIssueResponse(
                    ticket.getBookingId(),
                    ticket.getId(),
                    ticket.getStatus() == TicketStatus.ISSUED ? StepStatus.SUCCESS : StepStatus.FAILED,
                    "Ticket already processed"
            );
        }

        if (request.seatNumber().toUpperCase().contains("ERR")) {
            return new TicketIssueResponse(
                    request.bookingId(),
                    null,
                    StepStatus.FAILED,
                    "Ticket issuance failed due to invalid seat"
            );
        }

        Ticket ticket = new Ticket(
                request.bookingId(),
                request.customerId(),
                request.flightNumber(),
                request.seatNumber(),
                TicketStatus.ISSUED
        );
        Ticket saved = ticketRepository.save(ticket);
        return new TicketIssueResponse(
                saved.getBookingId(),
                saved.getId(),
                StepStatus.SUCCESS,
                "Ticket issued successfully"
        );
    }

    @Override
    public TicketIssueResponse cancelTicket(CancelTicketCommand command) {
        return ticketRepository.findByBookingId(command.bookingId())
                .map(ticket -> {
                    ticket.setStatus(TicketStatus.CANCELLED);
                    Ticket saved = ticketRepository.save(ticket);
                    return new TicketIssueResponse(
                            saved.getBookingId(),
                            saved.getId(),
                            StepStatus.COMPENSATED,
                            command.reason() == null ? "Ticket cancelled" : command.reason()
                    );
                })
                .orElseGet(() -> new TicketIssueResponse(
                        command.bookingId(),
                        null,
                        StepStatus.FAILED,
                        "Ticket not found"
                ));
    }
}

