package com.flight.ticket.event;

import com.flight.common.events.choreography.BookingEventConstants;
import com.flight.common.events.choreography.TicketIssueFailedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class TicketIssueFailedEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(TicketIssueFailedEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public TicketIssueFailedEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(TicketIssueFailedEvent event) {
        log.info("Publishing TicketIssueFailedEvent for booking {}", event.bookingId());
        rabbitTemplate.convertAndSend(BookingEventConstants.EXCHANGE, BookingEventConstants.ROUTING_TICKET_FAILED, event);
    }
}


