package com.flight.payment.config;

import com.flight.common.events.choreography.BookingEventConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class PaymentRabbitConfig {

    public static final String QUEUE_SEAT_RESERVED = "payment-service.seat-reserved";
    public static final String QUEUE_TICKET_FAILED = "payment-service.ticket-failed";

    @Bean
    public TopicExchange bookingExchange() {
        return new TopicExchange(BookingEventConstants.EXCHANGE, true, false);
    }

    @Bean
    public Queue paymentSeatReservedQueue() {
        return QueueBuilder.durable(QUEUE_SEAT_RESERVED).build();
    }

    @Bean
    public Queue paymentTicketFailedQueue() {
        return QueueBuilder.durable(QUEUE_TICKET_FAILED).build();
    }

    @Bean
    public Binding paymentSeatReservedBinding(@Qualifier("paymentSeatReservedQueue") Queue queue,
                                              TopicExchange bookingExchange) {
        return BindingBuilder.bind(queue)
                .to(bookingExchange)
                .with(BookingEventConstants.ROUTING_SEAT_RESERVED);
    }

    @Bean
    public Binding paymentTicketFailedBinding(@Qualifier("paymentTicketFailedQueue") Queue queue,
                                              TopicExchange bookingExchange) {
        return BindingBuilder.bind(queue)
                .to(bookingExchange)
                .with(BookingEventConstants.ROUTING_TICKET_FAILED);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}


