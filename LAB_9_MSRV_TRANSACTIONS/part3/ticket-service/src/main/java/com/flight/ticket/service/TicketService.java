package com.flight.ticket.service;

import com.flight.common.dto.TicketIssueResponse;
import com.flight.ticket.command.CancelTicketCommand;
import com.flight.ticket.command.IssueTicketCommand;

public interface TicketService {

    TicketIssueResponse issueTicket(IssueTicketCommand command);

    TicketIssueResponse cancelTicket(CancelTicketCommand command);
}

