package com.flight.seat.config;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.annotation.Qualifier;

@Configuration
@EnableRabbit
public class SeatRabbitConfig {

    public static final String QUEUE_PAYMENT_FAILED = "seat-service.payment-failed";
    public static final String QUEUE_TICKET_FAILED = "seat-service.ticket-failed";

    @Bean
    public TopicExchange bookingExchange() {
        return new TopicExchange(BookingEventConstants.EXCHANGE, true, false);
    }

    @Bean
    public Queue seatPaymentFailedQueue() {
        return QueueBuilder.durable(QUEUE_PAYMENT_FAILED).build();
    }

    @Bean
    public Queue seatTicketFailedQueue() {
        return QueueBuilder.durable(QUEUE_TICKET_FAILED).build();
    }

    @Bean
    public Binding seatPaymentFailedBinding(@Qualifier("seatPaymentFailedQueue") Queue seatPaymentFailedQueue,
                                            TopicExchange bookingExchange) {
        return BindingBuilder
                .bind(seatPaymentFailedQueue)
                .to(bookingExchange)
                .with(BookingEventConstants.ROUTING_PAYMENT_FAILED);
    }

    @Bean
    public Binding seatTicketFailedBinding(@Qualifier("seatTicketFailedQueue") Queue seatTicketFailedQueue,
                                           TopicExchange bookingExchange) {
        return BindingBuilder
                .bind(seatTicketFailedQueue)
                .to(bookingExchange)
                .with(BookingEventConstants.ROUTING_TICKET_FAILED);
    }

    @Bean
    @Primary
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


