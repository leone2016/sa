package com.flight.ticket.config;

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
public class TicketRabbitConfig {

    public static final String QUEUE_PAYMENT_COMPLETED = "ticket-service.payment-completed";

    @Bean
    public TopicExchange bookingExchange() {
        return new TopicExchange(BookingEventConstants.EXCHANGE, true, false);
    }

    @Bean
    public Queue ticketPaymentCompletedQueue() {
        return QueueBuilder.durable(QUEUE_PAYMENT_COMPLETED).build();
    }

    @Bean
    public Binding ticketPaymentCompletedBinding(@Qualifier("ticketPaymentCompletedQueue") Queue queue,
                                                 TopicExchange bookingExchange) {
        return BindingBuilder.bind(queue)
                .to(bookingExchange)
                .with(BookingEventConstants.ROUTING_PAYMENT_COMPLETED);
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


