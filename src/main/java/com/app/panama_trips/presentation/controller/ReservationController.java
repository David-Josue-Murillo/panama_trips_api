package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.persistence.entity.ReservationStatus;
import com.app.panama_trips.presentation.dto.ReservationRequest;
import com.app.panama_trips.presentation.dto.ReservationResponse;
import com.app.panama_trips.service.implementation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Integer id) {
        this.reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ReservationResponse>> getReservationsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.reservationService.getReservationByUserId(userId, pageable));
    }

    @GetMapping("/tourPlan/{tourPlanId}")
    public ResponseEntity<Page<ReservationResponse>> getReservationsByTourPlanId(
            @PathVariable Integer tourPlanId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.reservationService.getReservationByTourPlanId(tourPlanId, pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ReservationResponse>> getReservationsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.reservationService.getReservationByReservationStatus(status, pageable));
    }

    @GetMapping("/reservation-date/{reservationDate}")
    public ResponseEntity<Page<ReservationResponse>> getReservationsByReservationDate(
            @PathVariable String reservationDate,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.reservationService.getReservationByReservationDate(reservationDate, pageable));
    }

    @GetMapping("/user-status/{userId}/{status}")
    public ResponseEntity<Page<ReservationResponse>> getReservationsByUserIdAndStatus(
            @PathVariable Long userId,
            @PathVariable String status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.reservationService.getReservationsByUserAndStatus(userId, status, pageable));
    }

    @GetMapping("/tour-plan-status/{tourPlanId}/{status}")
    public ResponseEntity<Page<ReservationResponse>> getReservationsByTourPlanIdAndStatus(
            @PathVariable Integer tourPlanId,
            @PathVariable String status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.reservationService.getReservationsByTourPlanAndStatus(tourPlanId, status, pageable));
    }

    @GetMapping("/date-range")
    public ResponseEntity<Page<ReservationResponse>> getReservationsBetweenDates(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.reservationService.getReservationsBetweenDates(LocalDate.parse(startDate), LocalDate.parse(endDate), pageable));
    }

    @GetMapping("/month/{month}")
    public ResponseEntity<Page<ReservationResponse>> getReservationsByMonth(
            @PathVariable short month,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.reservationService.getReservationsByMonth(month, pageable));
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<Page<ReservationResponse>> getReservationsByYear(
            @PathVariable int year,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.reservationService.getReservationsByYear(year, pageable));
    }

    @GetMapping("/price-greater-than/{price}")
    public ResponseEntity<Page<ReservationResponse>> getReservationsWithPriceGreaterThan(
            @PathVariable BigDecimal price,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.reservationService.getReservationsWithPriceGreaterThan(price, pageable));
    }

    @GetMapping("/price-range")
    public ResponseEntity<Page<ReservationResponse>> getReservationsByPriceRange(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.reservationService.getReservationsByPriceRange(min, max, pageable));
    }

    @GetMapping("/recent/{userId}")
    public ResponseEntity<Page<ReservationResponse>> getRecentReservationsByUser(
            @PathVariable Long userId,
            @RequestParam String date,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.reservationService.getRecentReservationsByUser(userId, LocalDate.parse(date), pageable));
    }

    @GetMapping("/day-of-week/{dayOfWeek}")
    public ResponseEntity<Page<ReservationResponse>> getReservationsByDayOfWeek(
            @PathVariable int dayOfWeek,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.reservationService.getReservationsByDayOfWeek(dayOfWeek, pageable));
    }

    @GetMapping("/province/{provinceId}")
    public ResponseEntity<Page<ReservationResponse>> getReservationsByProvince(
            @PathVariable Integer provinceId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.reservationService.getReservationsByProvince(provinceId, pageable));
    }

    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countReservationsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(this.reservationService.countReservationsByStatus(ReservationStatus.valueOf(status.toUpperCase())));
    }

    @GetMapping("/count/tour-plan/{tourPlanId}")
    public ResponseEntity<Long> countReservationsByTourPlan(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(this.reservationService.countReservationsByTourPlan(tourPlanId));
    }

    @GetMapping("/statistics")
    public ResponseEntity<Object[]> getReservationStatistics() {
        return ResponseEntity.ok(this.reservationService.getReservationStatistics());
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<ReservationResponse> cancelReservation(@PathVariable Integer id) {
        return ResponseEntity.ok(this.reservationService.cancelReservation(id));
    }

    @PutMapping("/confirm/{id}")
    public ResponseEntity<ReservationResponse> confirmReservation(@PathVariable Integer id) {
        return ResponseEntity.ok(this.reservationService.confirmReservation(id));
    }
}