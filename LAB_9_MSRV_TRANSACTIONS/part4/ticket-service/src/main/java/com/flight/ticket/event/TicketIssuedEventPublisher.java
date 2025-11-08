package com.flight.ticket.event;

import com.flight.common.events.choreography.BookingEventConstants;
import com.flight.common.events.choreography.TicketIssuedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class TicketIssuedEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(TicketIssuedEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public TicketIssuedEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(TicketIssuedEvent event) {
        log.info("Publishing TicketIssuedEvent for booking {}", event.bookingId());
        rabbitTemplate.convertAndSend(BookingEventConstants.EXCHANGE, BookingEventConstants.ROUTING_TICKET_ISSUED, event);
    }
}


