package com.flight.seat.controller;

import com.flight.common.dto.BookingRequest;
import com.flight.common.dto.SeatReservationResponse;
import com.flight.seat.command.CancelSeatCommand;
import com.flight.seat.command.ReserveSeatCommand;
import com.flight.seat.service.SeatReservationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeatReservationController {

    private final SeatReservationService seatReservationService;

    public SeatReservationController(SeatReservationService seatReservationService) {
        this.seatReservationService = seatReservationService;
    }

    @PostMapping("/api/seats/reserve")
    public ResponseEntity<SeatReservationResponse> reserveSeat(@Valid @RequestBody BookingRequest request) {
        SeatReservationResponse response = seatReservationService.reserveSeat(new ReserveSeatCommand(request));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/seats/cancel/{bookingId}")
    public ResponseEntity<SeatReservationResponse> cancelReservation(
            @PathVariable String bookingId,
            @RequestParam(required = false) String reason
    ) {
        SeatReservationResponse response = seatReservationService.cancelReservation(
                new CancelSeatCommand(bookingId, reason)
        );
        return ResponseEntity.ok(response);
    }
}

