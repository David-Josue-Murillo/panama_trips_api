package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.ReservationRequest;
import com.app.panama_trips.presentation.dto.ReservationResponse;
import com.app.panama_trips.service.implementation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<Page<ReservationResponse>> getAllReservations(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.reservationService.getAllReservations(pageable));
    }

    @GetMapping("/id")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.reservationService.getReservationById(id));
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationRequest reservationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.reservationService.saveReservation(reservationRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponse> updateStatusReservation(@PathVariable Integer id, @RequestParam String user, @RequestBody String status) {
        return ResponseEntity.ok(this.reservationService.updateStatusReservation(id, user, status));
    }
}
