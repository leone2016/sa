package com.flight.seat.event;

import com.flight.common.events.choreography.BookingCancelledEvent;
import com.flight.common.events.choreography.BookingEventConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookingCancelledEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(BookingCancelledEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public BookingCancelledEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(BookingCancelledEvent event) {
        log.info("Publishing BookingCancelledEvent for booking {}", event.bookingId());
        rabbitTemplate.convertAndSend(BookingEventConstants.EXCHANGE, BookingEventConstants.ROUTING_BOOKING_CANCELLED, event);
    }
}


