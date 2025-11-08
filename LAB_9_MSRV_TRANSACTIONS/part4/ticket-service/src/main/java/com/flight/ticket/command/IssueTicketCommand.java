package com.flight.ticket.command;

import com.flight.common.dto.TicketIssueRequest;

public record IssueTicketCommand(TicketIssueRequest request) {
}

