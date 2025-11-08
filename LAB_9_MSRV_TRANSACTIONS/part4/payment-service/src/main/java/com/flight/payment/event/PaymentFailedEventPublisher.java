package com.flight.payment.event;

import com.flight.common.events.choreography.BookingEventConstants;
import com.flight.common.events.choreography.PaymentFailedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentFailedEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(PaymentFailedEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public PaymentFailedEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(PaymentFailedEvent event) {
        log.info("Publishing PaymentFailedEvent for booking {}", event.bookingId());
        rabbitTemplate.convertAndSend(BookingEventConstants.EXCHANGE, BookingEventConstants.ROUTING_PAYMENT_FAILED, event);
    }
}


