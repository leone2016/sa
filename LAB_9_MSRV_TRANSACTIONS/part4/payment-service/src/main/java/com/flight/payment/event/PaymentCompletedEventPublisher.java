package com.flight.payment.event;

import com.flight.common.events.choreography.BookingEventConstants;
import com.flight.common.events.choreography.PaymentCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentCompletedEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(PaymentCompletedEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public PaymentCompletedEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(PaymentCompletedEvent event) {
        log.info("Publishing PaymentCompletedEvent for booking {}", event.bookingId());
        rabbitTemplate.convertAndSend(BookingEventConstants.EXCHANGE, BookingEventConstants.ROUTING_PAYMENT_COMPLETED, event);
    }
}


