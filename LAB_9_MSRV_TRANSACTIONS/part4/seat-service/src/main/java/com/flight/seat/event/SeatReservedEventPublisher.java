package com.flight.seat.event;

import com.flight.common.events.choreography.BookingEventConstants;
import com.flight.common.events.choreography.SeatReservedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class SeatReservedEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(SeatReservedEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public SeatReservedEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(SeatReservedEvent event) {
        log.info("Publishing SeatReservedEvent for booking {}", event.bookingId());
        rabbitTemplate.convertAndSend(BookingEventConstants.EXCHANGE, BookingEventConstants.ROUTING_SEAT_RESERVED, event);
    }
}


