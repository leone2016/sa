package com.flight.saga.service;

import com.flight.common.dto.TicketIssueRequest;
import com.flight.common.dto.TicketIssueResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ticket-service", url = "${services.ticket}")
public interface TicketServiceClient {

    @PostMapping("/api/tickets/issue")
    TicketIssueResponse issueTicket(@RequestBody TicketIssueRequest request);

    @PostMapping("/api/tickets/cancel/{bookingId}")
    TicketIssueResponse cancelTicket(@PathVariable String bookingId, @RequestParam(required = false) String reason);
}

