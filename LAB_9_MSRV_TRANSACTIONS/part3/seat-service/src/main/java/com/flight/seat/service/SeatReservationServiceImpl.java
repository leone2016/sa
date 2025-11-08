package com.flight.seat.service;

import com.flight.common.dto.SeatReservationResponse;
import com.flight.common.events.StepStatus;
import com.flight.seat.command.CancelSeatCommand;
import com.flight.seat.command.ReserveSeatCommand;
import com.flight.seat.domain.SeatReservation;
import com.flight.seat.domain.SeatReservationStatus;
import com.flight.seat.repository.SeatReservationRepository;
import org.springframework.stereotype.Service;

@Service
public class SeatReservationServiceImpl implements SeatReservationService {

    private final SeatReservationRepository repository;

    public SeatReservationServiceImpl(SeatReservationRepository repository) {
        this.repository = repository;
    }

    @Override
    public SeatReservationResponse reserveSeat(ReserveSeatCommand command) {
        var request = command.request();
        if (repository.existsByFlightNumberAndSeatNumber(request.flightNumber(), request.seatNumber())) {
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
        return new SeatReservationResponse(
                saved.getBookingId(),
                saved.getId(),
                StepStatus.SUCCESS,
                "Seat reserved successfully"
        );
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

