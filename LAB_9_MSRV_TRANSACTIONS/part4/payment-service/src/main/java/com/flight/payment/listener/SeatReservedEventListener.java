package com.flight.payment.listener;

import com.flight.common.dto.PaymentRequest;
import com.flight.common.dto.PaymentResponse;
import com.flight.common.events.StepStatus;
import com.flight.common.events.choreography.PaymentCompletedEvent;
import com.flight.common.events.choreography.PaymentFailedEvent;
import com.flight.common.events.choreography.SeatReservedEvent;
import com.flight.common.events.choreography.TicketIssueFailedEvent;
import com.flight.payment.command.ProcessPaymentCommand;
import com.flight.payment.command.RefundPaymentCommand;
import com.flight.payment.config.PaymentRabbitConfig;
import com.flight.payment.event.PaymentCompletedEventPublisher;
import com.flight.payment.event.PaymentFailedEventPublisher;
import com.flight.payment.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SeatReservedEventListener {

    private static final Logger log = LoggerFactory.getLogger(SeatReservedEventListener.class);

    private final PaymentService paymentService;
    private final PaymentCompletedEventPublisher paymentCompletedEventPublisher;
    private final PaymentFailedEventPublisher paymentFailedEventPublisher;

    public SeatReservedEventListener(PaymentService paymentService,
                                     PaymentCompletedEventPublisher paymentCompletedEventPublisher,
                                     PaymentFailedEventPublisher paymentFailedEventPublisher) {
        this.paymentService = paymentService;
        this.paymentCompletedEventPublisher = paymentCompletedEventPublisher;
        this.paymentFailedEventPublisher = paymentFailedEventPublisher;
    }

    @RabbitListener(queues = PaymentRabbitConfig.QUEUE_SEAT_RESERVED)
    public void handleSeatReserved(SeatReservedEvent event) {
        log.info("Processing SeatReservedEvent for booking {}", event.bookingId());
        PaymentRequest request = new PaymentRequest(
                event.bookingId(),
                event.customerId(),
                event.amount(),
                event.currency(),
                event.paymentMethod()
        );
        PaymentResponse response = paymentService.processPayment(new ProcessPaymentCommand(request));
        if (response.status() == StepStatus.SUCCESS) {
            paymentCompletedEventPublisher.publish(new PaymentCompletedEvent(
                    response.bookingId(),
                    response.paymentId(),
                    event.customerId(),
                    response.amount(),
                    event.currency(),
                    event.flightNumber(),
                    event.seatNumber()
            ));
        } else {
            paymentFailedEventPublisher.publish(new PaymentFailedEvent(
                    response.bookingId(),
                    response.message()
            ));
        }
    }

    @RabbitListener(queues = PaymentRabbitConfig.QUEUE_TICKET_FAILED)
    public void handleTicketIssueFailed(TicketIssueFailedEvent event) {
        log.info("Received TicketIssueFailedEvent for booking {}. Initiating refund.", event.bookingId());
        var response = paymentService.refundPayment(new RefundPaymentCommand(event.bookingId(), event.reason()));
        if (response.status() == StepStatus.COMPENSATED) {
            log.info("Refund completed for booking {}", event.bookingId());
        } else {
            log.warn("Refund attempt for booking {} returned status {}: {}", event.bookingId(), response.status(), response.message());
        }
    }
}


