package com.app.panama_trips.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.panama_trips.presentation.dto.TourPriceHistoryRequest;
import com.app.panama_trips.presentation.dto.TourPriceHistoryResponse;
import com.app.panama_trips.service.implementation.TourPriceHistoryService;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tour-price-history")
@RequiredArgsConstructor
public class TourPriceHistoryController {

    private final TourPriceHistoryService service;

    // CRUD operations
    @GetMapping
    public ResponseEntity<Page<TourPriceHistoryResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.getAllTourPriceHistories(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourPriceHistoryResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getTourPriceHistoryById(id));
    }

    @PostMapping
    public ResponseEntity<TourPriceHistoryResponse> create(@RequestBody TourPriceHistoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveTourPriceHistory(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TourPriceHistoryResponse> update(@PathVariable Integer id,
            @RequestBody TourPriceHistoryRequest request) {
        return ResponseEntity.ok(service.updateTourPriceHistory(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteTourPriceHistory(id);
        return ResponseEntity.noContent().build();
    }

    // Find operations by entity relationships
    @GetMapping("/tour-plan/{tourPlanId}")
    public ResponseEntity<List<TourPriceHistoryResponse>> findByTourPlanId(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(service.findByTourPlanId(tourPlanId));
    }

    @GetMapping("/tour-plan/{tourPlanId}/paginated")
    public ResponseEntity<Page<TourPriceHistoryResponse>> findByTourPlanIdOrderByChangedAtDesc(
            @PathVariable Integer tourPlanId, Pageable pageable) {
        return ResponseEntity.ok(service.findByTourPlanIdOrderByChangedAtDesc(tourPlanId, pageable));
    }

    @GetMapping("/tour-plan/{tourPlanId}/date-range")
    public ResponseEntity<List<TourPriceHistoryResponse>> findByTourPlanIdAndChangedAtBetween(
            @PathVariable Integer tourPlanId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(service.findByTourPlanIdAndChangedAtBetween(tourPlanId, startDate, endDate));
    }

    @GetMapping("/changed-by/{userId}")
    public ResponseEntity<List<TourPriceHistoryResponse>> findByChangedById(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findByChangedById(userId));
    }

    // Specialized queries
    @GetMapping("/tour-plan/{tourPlanId}/average-change-percentage")
    public ResponseEntity<Double> calculateAveragePriceChangePercentageByTourPlanId(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(service.calculateAveragePriceChangePercentageByTourPlanId(tourPlanId));
    }

    @GetMapping("/new-price-greater-than/{price}")
    public ResponseEntity<List<TourPriceHistoryResponse>> findByNewPriceGreaterThan(@PathVariable BigDecimal price) {
        return ResponseEntity.ok(service.findByNewPriceGreaterThan(price));
    }

    @GetMapping("/tour-plan/{tourPlanId}/count")
    public ResponseEntity<Long> countPriceChangesByTourPlanId(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(service.countPriceChangesByTourPlanId(tourPlanId));
    }

    // Business logic operations
    @GetMapping("/recent/{limit}")
    public ResponseEntity<List<TourPriceHistoryResponse>> getRecentChanges(@PathVariable int limit) {
        return ResponseEntity.ok(service.getRecentChanges(limit));
    }

    @GetMapping("/tour-plan/{tourPlanId}/latest")
    public ResponseEntity<TourPriceHistoryResponse> getLatestChangeForTourPlan(@PathVariable Integer tourPlanId) {
        Optional<TourPriceHistoryResponse> result = service.getLatestChangeForTourPlan(tourPlanId);
        return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tour-plan/{tourPlanId}/current-price")
    public ResponseEntity<BigDecimal> getCurrentPriceForTourPlan(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(service.getCurrentPriceForTourPlan(tourPlanId));
    }

    @GetMapping("/tour-plan/{tourPlanId}/previous-price")
    public ResponseEntity<BigDecimal> getPreviousPriceForTourPlan(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(service.getPreviousPriceForTourPlan(tourPlanId));
    }

    @GetMapping("/tour-plan/{tourPlanId}/max-price")
    public ResponseEntity<BigDecimal> getMaxPriceForTourPlan(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(service.getMaxPriceForTourPlan(tourPlanId));
    }

    @GetMapping("/tour-plan/{tourPlanId}/min-price")
    public ResponseEntity<BigDecimal> getMinPriceForTourPlan(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(service.getMinPriceForTourPlan(tourPlanId));
    }
}
