package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.presentation.dto.TourPlanAvailabilityRequest;
import com.app.panama_trips.presentation.dto.TourPlanAvailabilityResponse;
import com.app.panama_trips.service.implementation.TourPlanAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tour-plan-availability")
@RequiredArgsConstructor
public class TourPlanAvailabilityController {

    private final TourPlanAvailabilityService tourPlanAvailabilityService;

    @GetMapping
    public ResponseEntity<Page<TourPlanAvailabilityResponse>> getAllTourPlanAvailabilities(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(tourPlanAvailabilityService.getAllTourPlanAvailabilities(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourPlanAvailabilityResponse> getTourPlanAvailabilityById(@PathVariable Integer id) {
        return ResponseEntity.ok(tourPlanAvailabilityService.getTourPlanAvailabilityById(id));
    }

    @PostMapping
    public ResponseEntity<TourPlanAvailabilityResponse> createTourPlanAvailability(@RequestBody TourPlanAvailabilityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tourPlanAvailabilityService.saveTourPlanAvailability(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TourPlanAvailabilityResponse> updateTourPlanAvailability(@PathVariable Integer id, @RequestBody TourPlanAvailabilityRequest request) {
        return ResponseEntity.ok(tourPlanAvailabilityService.updateTourPlanAvailability(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTourPlanAvailability(@PathVariable Integer id) {
        tourPlanAvailabilityService.deleteTourPlanAvailability(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tour-plan/{tourPlanId}")
    public ResponseEntity<List<TourPlanAvailabilityResponse>> getTourPlanAvailabilitiesByTourPlanId(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(tourPlanAvailabilityService.getTourPlanAvailabilitiesByTourPlanId(tourPlanId));
    }

    @GetMapping("/tour-plan/{tourPlanId}/available")
    public ResponseEntity<List<TourPlanAvailabilityResponse>> getAvailableDatesByTourPlanId(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(tourPlanAvailabilityService.getAvailableDatesByTourPlanId(tourPlanId));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<TourPlanAvailabilityResponse>> getAvailabilitiesByDateRange(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(tourPlanAvailabilityService.getAvailabilitiesByDateRange(startDate, endDate));
    }

    @GetMapping("/tour-plan/{tourPlanId}/date-range")
    public ResponseEntity<List<TourPlanAvailabilityResponse>> getAvailabilitiesByTourPlanIdAndDateRange(
            @PathVariable Integer tourPlanId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(tourPlanAvailabilityService.getAvailabilitiesByTourPlanIdAndDateRange(
                tourPlanId, startDate, endDate));
    }

    @GetMapping("/tour-plan/{tourPlanId}/date/{date}")
    public ResponseEntity<TourPlanAvailabilityResponse> getAvailabilityByTourPlanIdAndDate( @PathVariable Integer tourPlanId, @PathVariable LocalDate date) {
        return ResponseEntity.ok(tourPlanAvailabilityService.getAvailabilityByTourPlanIdAndDate(tourPlanId, date));
    }

    @GetMapping("/tour-plan/{tourPlanId}/upcoming")
    public ResponseEntity<List<TourPlanAvailabilityResponse>> getUpcomingAvailableDatesByTourPlanId(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(tourPlanAvailabilityService.getUpcomingAvailableDatesByTourPlanId(tourPlanId));
    }

    @GetMapping("/tour-plan/{tourPlanId}/sufficient-spots/{requiredSpots}")
    public ResponseEntity<List<TourPlanAvailabilityResponse>> getAvailableDatesWithSufficientSpots(@PathVariable Integer tourPlanId, @PathVariable Integer requiredSpots) {
        return ResponseEntity.ok(tourPlanAvailabilityService.getAvailableDatesWithSufficientSpots(
                tourPlanId, requiredSpots));
    }

    @GetMapping("/tour-plan/{tourPlanId}/upcoming/count")
    public ResponseEntity<Long> countUpcomingAvailableDatesByTourPlanId(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(tourPlanAvailabilityService.countUpcomingAvailableDatesByTourPlanId(tourPlanId));
    }

    @GetMapping("/tour-plan/{tourPlanId}/price-override")
    public ResponseEntity<List<TourPlanAvailabilityResponse>> getAvailabilitiesWithPriceOverride(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(tourPlanAvailabilityService.getAvailabilitiesWithPriceOverride(tourPlanId));
    }

    @GetMapping("/tour-plan/{tourPlanId}/price-above/{price}")
    public ResponseEntity<List<TourPlanAvailabilityResponse>> getAvailabilitiesWithPriceAbove(@PathVariable Integer tourPlanId, @PathVariable BigDecimal price) {
        return ResponseEntity.ok(tourPlanAvailabilityService.getAvailabilitiesWithPriceAbove(tourPlanId, price));
    }

    @DeleteMapping("/tour-plan/{tourPlanId}")
    public ResponseEntity<Void> deleteAllAvailabilitiesByTourPlanId(@PathVariable Integer tourPlanId) {
        tourPlanAvailabilityService.deleteAllAvailabilitiesByTourPlanId(tourPlanId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tour-plan/{tourPlanId}/exists/{date}")
    public ResponseEntity<Boolean> existsAvailabilityForTourPlanAndDate(@PathVariable Integer tourPlanId, @PathVariable LocalDate date) {
        return ResponseEntity.ok(tourPlanAvailabilityService.existsAvailabilityForTourPlanAndDate(tourPlanId, date));
    }
}
