package com.flight.ticket.listener;

import com.flight.common.dto.TicketIssueRequest;
import com.flight.common.dto.TicketIssueResponse;
import com.flight.common.events.StepStatus;
import com.flight.common.events.choreography.PaymentCompletedEvent;
import com.flight.common.events.choreography.TicketIssuedEvent;
import com.flight.common.events.choreography.TicketIssueFailedEvent;
import com.flight.ticket.command.IssueTicketCommand;
import com.flight.ticket.config.TicketRabbitConfig;
import com.flight.ticket.event.TicketIssuedEventPublisher;
import com.flight.ticket.event.TicketIssueFailedEventPublisher;
import com.flight.ticket.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentCompletedEventListener {

    private static final Logger log = LoggerFactory.getLogger(PaymentCompletedEventListener.class);

    private final TicketService ticketService;
    private final TicketIssuedEventPublisher ticketIssuedEventPublisher;
    private final TicketIssueFailedEventPublisher ticketIssueFailedEventPublisher;

    public PaymentCompletedEventListener(TicketService ticketService,
                                         TicketIssuedEventPublisher ticketIssuedEventPublisher,
                                         TicketIssueFailedEventPublisher ticketIssueFailedEventPublisher) {
        this.ticketService = ticketService;
        this.ticketIssuedEventPublisher = ticketIssuedEventPublisher;
        this.ticketIssueFailedEventPublisher = ticketIssueFailedEventPublisher;
    }

    @RabbitListener(queues = TicketRabbitConfig.QUEUE_PAYMENT_COMPLETED)
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        log.info("Processing PaymentCompletedEvent for booking {}", event.bookingId());
        TicketIssueRequest request = new TicketIssueRequest(
                event.bookingId(),
                event.customerId(),
                event.flightNumber(),
                event.seatNumber()
        );
        TicketIssueResponse response = ticketService.issueTicket(new IssueTicketCommand(request));
        if (response.status() == StepStatus.SUCCESS) {
            ticketIssuedEventPublisher.publish(new TicketIssuedEvent(
                    response.bookingId(),
                    response.ticketId(),
                    event.flightNumber(),
                    event.seatNumber(),
                    event.customerId()
            ));
        } else {
            ticketIssueFailedEventPublisher.publish(new TicketIssueFailedEvent(
                    event.bookingId(),
                    response.message()
            ));
        }
    }
}


