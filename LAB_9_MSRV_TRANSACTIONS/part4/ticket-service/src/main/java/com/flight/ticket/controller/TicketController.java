package com.flight.ticket.controller;

import com.flight.common.dto.TicketIssueRequest;
import com.flight.common.dto.TicketIssueResponse;
import com.flight.ticket.command.CancelTicketCommand;
import com.flight.ticket.command.IssueTicketCommand;
import com.flight.ticket.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/api/tickets/issue")
    public ResponseEntity<TicketIssueResponse> issueTicket(@Valid @RequestBody TicketIssueRequest request) {
        TicketIssueResponse response = ticketService.issueTicket(new IssueTicketCommand(request));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/tickets/cancel/{bookingId}")
    public ResponseEntity<TicketIssueResponse> cancelTicket(
            @PathVariable String bookingId,
            @RequestParam(required = false) String reason
    ) {
        TicketIssueResponse response = ticketService.cancelTicket(new CancelTicketCommand(bookingId, reason));
        return ResponseEntity.ok(response);
    }
}

