package com.flight.seat.service;

import com.flight.common.dto.SeatReservationResponse;
import com.flight.common.events.StepStatus;
import com.flight.common.events.choreography.SeatReservedEvent;
import com.flight.seat.command.CancelSeatCommand;
import com.flight.seat.command.ReserveSeatCommand;
import com.flight.seat.domain.SeatReservation;
import com.flight.seat.domain.SeatReservationStatus;
import com.flight.seat.event.SeatReservedEventPublisher;
import com.flight.seat.repository.SeatReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SeatReservationServiceImpl implements SeatReservationService {

    private static final Logger log = LoggerFactory.getLogger(SeatReservationServiceImpl.class);

    private final SeatReservationRepository repository;
    private final SeatReservedEventPublisher seatReservedEventPublisher;

    public SeatReservationServiceImpl(SeatReservationRepository repository,
                                      SeatReservedEventPublisher seatReservedEventPublisher) {
        this.repository = repository;
        this.seatReservedEventPublisher = seatReservedEventPublisher;
    }

    @Override
    public SeatReservationResponse reserveSeat(ReserveSeatCommand command) {
        var request = command.request();
        if (repository.existsByFlightNumberAndSeatNumber(request.flightNumber(), request.seatNumber())) {
            log.warn("Seat {} on flight {} is already reserved for booking {}",
                    request.seatNumber(), request.flightNumber(), request.bookingId());
            return new SeatReservationResponse(
                    request.bookingId(),
                    null,
                    StepStatus.FAILED,
                    "Seat already reserved"
            );
        }

        SeatReservation reservation = new SeatReservation(
                request.bookingId(),
                request.flightNumber(),
                request.seatNumber(),
                request.customerId()
        );
        SeatReservation saved = repository.save(reservation);
        SeatReservationResponse response = new SeatReservationResponse(
                saved.getBookingId(),
                saved.getId(),
                StepStatus.SUCCESS,
                "Seat reserved successfully"
        );

        seatReservedEventPublisher.publish(new SeatReservedEvent(
                request.bookingId(),
                request.flightNumber(),
                request.seatNumber(),
                request.customerId(),
                request.amount(),
                request.currency(),
                request.paymentMethod()
        ));
        return response;
    }

    @Override
    public SeatReservationResponse cancelReservation(CancelSeatCommand command) {
        return repository.findByBookingId(command.bookingId())
                .map(reservation -> {
                    reservation.setStatus(SeatReservationStatus.CANCELLED);
                    repository.save(reservation);
                    return new SeatReservationResponse(
                            reservation.getBookingId(),
                            reservation.getId(),
                            StepStatus.COMPENSATED,
                            command.reason() == null ? "Reservation cancelled" : command.reason()
                    );
                })
                .orElseGet(() -> new SeatReservationResponse(
                        command.bookingId(),
                        null,
                        StepStatus.FAILED,
                        "Reservation not found"
                ));
    }
}

