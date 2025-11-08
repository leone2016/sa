package com.flight.saga.orchestrator;

import com.flight.common.dto.BookingRequest;
import com.flight.common.dto.BookingResponse;
import com.flight.common.events.SagaStatus;
import com.flight.saga.service.BookingSagaOrchestrator;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingController {

    private final BookingSagaOrchestrator bookingSagaOrchestrator;

    public BookingController(BookingSagaOrchestrator bookingSagaOrchestrator) {
        this.bookingSagaOrchestrator = bookingSagaOrchestrator;
    }

    @PostMapping("/api/bookings")
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request) {
        BookingResponse response = bookingSagaOrchestrator.orchestrate(request);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler
    public ResponseEntity<BookingResponse> handleSagaError(Exception exception) {
        return ResponseEntity.internalServerError()
                .body(new BookingResponse(null, SagaStatus.FAILED, exception.getMessage()));
    }
}

