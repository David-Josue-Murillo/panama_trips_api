package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.presentation.dto.TourPlanSpecialPriceRequest;
import com.app.panama_trips.presentation.dto.TourPlanSpecialPriceResponse;
import com.app.panama_trips.service.implementation.TourPlanSpecialPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tour-plan-special-price")
@RequiredArgsConstructor
public class TourPlanSpecialPriceController {

    private final TourPlanSpecialPriceService service;

    // Define your endpoints here, for example:
    @GetMapping
    public ResponseEntity<Page<TourPlanSpecialPriceResponse>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean enablePagination
    ) {
        Pageable pageable = enablePagination
                ? PageRequest.of(page, size)
                : Pageable.unpaged();

        return ResponseEntity.ok(this.service.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourPlanSpecialPriceResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.service.findById(id));
    }

    @PostMapping
    public ResponseEntity<TourPlanSpecialPriceResponse> create(@RequestBody TourPlanSpecialPriceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TourPlanSpecialPriceResponse> update(@PathVariable Integer id, @RequestBody TourPlanSpecialPriceRequest request) {
        return ResponseEntity.ok(this.service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        this.service.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/tour-plan/{tourPlanId}")
    public ResponseEntity<List<TourPlanSpecialPriceResponse>> getByTourPlanId(@PathVariable Integer tourPlanId) {
        TourPlan tourPlan = new TourPlan();
        tourPlan.setId(tourPlanId);
        return ResponseEntity.ok(this.service.findByTourPlan(tourPlan));
    }

    @GetMapping("/tour-plan/{tourPlanId}/ordered")
    public ResponseEntity<List<TourPlanSpecialPriceResponse>> getByTourPlanIdOrdered(@PathVariable Integer tourPlanId) {
        TourPlan tourPlan = new TourPlan();
        tourPlan.setId(tourPlanId);
        return ResponseEntity.ok(this.service.findByTourPlanOrderByStartDateAsc(tourPlan));
    }

    @GetMapping("/start-date-between")
    public ResponseEntity<List<TourPlanSpecialPriceResponse>> getByStartDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(this.service.findByStartDateBetween(startDate, endDate));
    }

    @GetMapping("/end-date-between")
    public ResponseEntity<List<TourPlanSpecialPriceResponse>> getByEndDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(this.service.findByEndDateBetween(startDate, endDate));
    }

    @GetMapping("/tour-plan/{tourPlanId}/date")
    public ResponseEntity<TourPlanSpecialPriceResponse> getByTourPlanIdAndDate(
            @PathVariable Integer tourPlanId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return this.service.findByTourPlanIdAndDate(tourPlanId, date)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tour-plan/{tourPlanId}/overlapping")
    public ResponseEntity<List<TourPlanSpecialPriceResponse>> getOverlappingPrices(
            @PathVariable Integer tourPlanId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(
                this.service.findOverlappingPricePeriodsForTourPlan(tourPlanId, startDate, endDate)
        );
    }

    @GetMapping("/tour-plan/{tourPlanId}/price-greater-than")
    public ResponseEntity<List<TourPlanSpecialPriceResponse>> getByTourPlanAndPriceGreaterThan(
            @PathVariable Integer tourPlanId,
            @RequestParam BigDecimal price) {
        TourPlan tourPlan = new TourPlan();
        tourPlan.setId(tourPlanId);
        return ResponseEntity.ok(this.service.findByTourPlanAndPriceGreaterThan(tourPlan, price));
    }

    @GetMapping("/tour-plan/{tourPlanId}/start-date-after")
    public ResponseEntity<List<TourPlanSpecialPriceResponse>> getByTourPlanAndStartDateAfter(
            @PathVariable Integer tourPlanId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        TourPlan tourPlan = new TourPlan();
        tourPlan.setId(tourPlanId);
        return ResponseEntity.ok(this.service.findByTourPlanAndStartDateGreaterThanEqual(tourPlan, date));
    }

    @DeleteMapping("/tour-plan/{tourPlanId}")
    public ResponseEntity<Void> deleteByTourPlanId(@PathVariable Integer tourPlanId) {
        TourPlan tourPlan = new TourPlan();
        tourPlan.setId(tourPlanId);
        this.service.deleteByTourPlan(tourPlan);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkExistsByTourPlanAndDates(
            @RequestParam Integer tourPlanId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        TourPlan tourPlan = new TourPlan();
        tourPlan.setId(tourPlanId);
        return ResponseEntity.ok(this.service.existsByTourPlanAndStartDateAndEndDate(tourPlan, startDate, endDate));
    }
}
