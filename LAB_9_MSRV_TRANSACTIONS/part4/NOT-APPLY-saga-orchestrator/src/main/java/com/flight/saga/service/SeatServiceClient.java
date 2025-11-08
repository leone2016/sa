package com.flight.saga.service;

import com.flight.common.dto.SeatReservationRequest;
import com.flight.common.dto.SeatReservationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "seat-service", url = "${services.seat}")
public interface SeatServiceClient {

    @PostMapping("/api/seats/reserve")
    SeatReservationResponse reserveSeat(@RequestBody SeatReservationRequest request);

    @PostMapping("/api/seats/cancel/{bookingId}")
    SeatReservationResponse cancelReservation(@PathVariable String bookingId, @RequestParam(required = false) String reason);
}

