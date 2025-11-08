package com.flight.saga.service;

import com.flight.common.dto.BookingRequest;
import com.flight.common.dto.BookingResponse;
import com.flight.common.dto.PaymentRequest;
import com.flight.common.dto.PaymentResponse;
import com.flight.common.dto.SeatReservationRequest;
import com.flight.common.dto.SeatReservationResponse;
import com.flight.common.dto.TicketIssueRequest;
import com.flight.common.dto.TicketIssueResponse;
import com.flight.common.events.SagaStatus;
import com.flight.common.events.StepStatus;
import com.flight.saga.exception.SagaOrchestrationException;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BookingSagaOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(BookingSagaOrchestrator.class);

    private final SeatServiceClient seatServiceClient;
    private final PaymentServiceClient paymentServiceClient;
    private final TicketServiceClient ticketServiceClient;

    public BookingSagaOrchestrator(SeatServiceClient seatServiceClient,
                                   PaymentServiceClient paymentServiceClient,
                                   TicketServiceClient ticketServiceClient) {
        this.seatServiceClient = seatServiceClient;
        this.paymentServiceClient = paymentServiceClient;
        this.ticketServiceClient = ticketServiceClient;
    }

    public BookingResponse orchestrate(BookingRequest request) {
        SeatReservationResponse seatResponse = null;
        PaymentResponse paymentResponse = null;

        try {
            seatResponse = seatServiceClient.reserveSeat(toSeatRequest(request));
            if (!isSuccess(seatResponse.status())) {
                log.warn("Seat reservation failed for booking {}: {}", request.bookingId(), seatResponse.message());
                return new BookingResponse(request.bookingId(), SagaStatus.FAILED, seatResponse.message());
            }

            paymentResponse = paymentServiceClient.processPayment(toPaymentRequest(request));
            if (!isSuccess(paymentResponse.status())) {
                log.warn("Payment failed for booking {}: {}", request.bookingId(), paymentResponse.message());
                compensateSeat(request, "Payment failed");
                return new BookingResponse(request.bookingId(), SagaStatus.FAILED, paymentResponse.message());
            }

            TicketIssueResponse ticketResponse = ticketServiceClient.issueTicket(toTicketRequest(request));
            if (!isSuccess(ticketResponse.status())) {
                log.warn("Ticket issuance failed for booking {}: {}", request.bookingId(), ticketResponse.message());
                compensateAfterTicketFailure(request, paymentResponse);
                return new BookingResponse(request.bookingId(), SagaStatus.FAILED, ticketResponse.message());
            }

            log.info("Booking {} completed successfully", request.bookingId());
            return new BookingResponse(request.bookingId(), SagaStatus.COMPLETED, "Booking completed successfully");
        } catch (FeignException feignException) {
            log.error("Feign exception during saga for booking {}: {}", request.bookingId(), feignException.getMessage());
            performCompensationsOnError(request, seatResponse, paymentResponse, "Remote service error");
            throw new SagaOrchestrationException("Saga failed due to remote service error", feignException);
        } catch (Exception exception) {
            log.error("Unexpected exception during saga for booking {}", request.bookingId(), exception);
            performCompensationsOnError(request, seatResponse, paymentResponse, "Unexpected error");
            throw new SagaOrchestrationException("Saga failed due to an unexpected error", exception);
        }
    }

    private void compensateAfterTicketFailure(BookingRequest request, PaymentResponse paymentResponse) {
        if (paymentResponse != null && isSuccess(paymentResponse.status())) {
            paymentServiceClient.refundPayment(request.bookingId(), "Ticket issuance failed");
        }
        compensateSeat(request, "Ticket issuance failed");
    }

    private void performCompensationsOnError(BookingRequest request,
                                             SeatReservationResponse seatResponse,
                                             PaymentResponse paymentResponse,
                                             String reason) {
        if (paymentResponse != null && isSuccess(paymentResponse.status())) {
            paymentServiceClient.refundPayment(request.bookingId(), reason);
        }
        if (seatResponse != null && isSuccess(seatResponse.status())) {
            compensateSeat(request, reason);
        }
    }

    private void compensateSeat(BookingRequest request, String reason) {
        try {
            seatServiceClient.cancelReservation(request.bookingId(), reason);
        } catch (Exception e) {
            log.error("Failed to compensate seat for booking {}: {}", request.bookingId(), e.getMessage());
        }
    }

    private boolean isSuccess(StepStatus status) {
        return StepStatus.SUCCESS.equals(status);
    }

    private SeatReservationRequest toSeatRequest(BookingRequest request) {
        return new SeatReservationRequest(request.bookingId(), request.flightNumber(), request.seatNumber(), request.customerId());
    }

    private PaymentRequest toPaymentRequest(BookingRequest request) {
        return new PaymentRequest(request.bookingId(), request.customerId(), request.amount(), request.currency(), request.paymentMethod());
    }

    private TicketIssueRequest toTicketRequest(BookingRequest request) {
        return new TicketIssueRequest(request.bookingId(), request.customerId(), request.flightNumber(), request.seatNumber());
    }
}

