package com.flight.seat.listener;

import com.flight.common.events.StepStatus;
import com.flight.common.events.choreography.BookingCancelledEvent;
import com.flight.common.events.choreography.PaymentFailedEvent;
import com.flight.common.events.choreography.TicketIssueFailedEvent;
import com.flight.seat.command.CancelSeatCommand;
import com.flight.seat.config.SeatRabbitConfig;
import com.flight.seat.event.BookingCancelledEventPublisher;
import com.flight.seat.service.SeatReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CompensationEventListener {

    private static final Logger log = LoggerFactory.getLogger(CompensationEventListener.class);

    private final SeatReservationService seatReservationService;
    private final BookingCancelledEventPublisher bookingCancelledEventPublisher;

    public CompensationEventListener(SeatReservationService seatReservationService,
                                     BookingCancelledEventPublisher bookingCancelledEventPublisher) {
        this.seatReservationService = seatReservationService;
        this.bookingCancelledEventPublisher = bookingCancelledEventPublisher;
    }

    @RabbitListener(queues = SeatRabbitConfig.QUEUE_PAYMENT_FAILED)
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.info("Received PaymentFailedEvent for booking {}. Triggering seat compensation.", event.bookingId());
        compensate(event.bookingId(), event.reason());
    }

    @RabbitListener(queues = SeatRabbitConfig.QUEUE_TICKET_FAILED)
    public void handleTicketIssueFailed(TicketIssueFailedEvent event) {
        log.info("Received TicketIssueFailedEvent for booking {}. Triggering seat compensation.", event.bookingId());
        compensate(event.bookingId(), event.reason());
    }

    private void compensate(String bookingId, String reason) {
        var response = seatReservationService.cancelReservation(new CancelSeatCommand(bookingId, reason));
        if (response.status() == StepStatus.COMPENSATED) {
            bookingCancelledEventPublisher.publish(new BookingCancelledEvent(bookingId, reason));
        } else {
            log.warn("Seat compensation for booking {} returned status {}: {}", bookingId, response.status(), response.message());
        }
    }
}


