package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.TourPlanRequest;
import com.app.panama_trips.presentation.dto.TourPlanResponse;
import com.app.panama_trips.service.implementation.TourPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/tour-plan")
@RequiredArgsConstructor
public class TourPlanController {

    private final TourPlanService tourPlanService;

    @GetMapping
    public ResponseEntity<Page<TourPlanResponse>> findAllTourPlan(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.tourPlanService.getAllTourPlan(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourPlanResponse> findTourPlanById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.tourPlanService.getTourPlanById(id));
    }

    @PostMapping
    public ResponseEntity<TourPlanResponse> saveTourPlan(@RequestBody TourPlanRequest tourPlanRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.tourPlanService.saveTourPlan(tourPlanRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TourPlanResponse> updateTourPlan(@PathVariable Integer id, @RequestBody TourPlanRequest tourPlanRequest) {
        return ResponseEntity.ok(this.tourPlanService.updateTourPlan(id, tourPlanRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTourPlan(@PathVariable Integer id) {
        this.tourPlanService.deleteTourPlan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countTourPlan() {
        return ResponseEntity.ok(this.tourPlanService.countTourPlan());
    }

    @GetMapping("/title")
    public ResponseEntity<TourPlanResponse> findAllTourPlanTitle(@RequestParam String q) {
        return ResponseEntity.ok(this.tourPlanService.getTourPlanByTitle(q));
    }

    @GetMapping("/price")
    public ResponseEntity<List<TourPlanResponse>> findAllTourPlanByPrice(@RequestParam BigDecimal q) {
        return ResponseEntity.ok(this.tourPlanService.getTourPlanByPrice(q));
    }

    @GetMapping("/prices")
    public ResponseEntity<Page<TourPlanResponse>> findAllTourPlanByPrices(
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.tourPlanService.getTourPlanByPriceBetween(min, max, pageable));
    }

    @GetMapping("/duration")
    public ResponseEntity<List<TourPlanResponse>> findAllTourPlanByDuration(@RequestParam Integer q) {
        return ResponseEntity.ok(this.tourPlanService.getTourPlanByDuration(q));
    }

    @GetMapping("/durations")
    public ResponseEntity<Page<TourPlanResponse>> findAllTourPlanByDurations(
            @RequestParam Integer min,
            @RequestParam Integer max,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.tourPlanService.getTourPlanByDurationBetween(min, max, pageable));
    }

    @GetMapping("/available-spots")
    public ResponseEntity<List<TourPlanResponse>> findAllTourPlanByAvailableSpots(@RequestParam Integer q) {
        return ResponseEntity.ok(this.tourPlanService.getTourPlanByAvailableSpots(q));
    }

    @GetMapping("/available-spots-range")
    public ResponseEntity<Page<TourPlanResponse>> findAllTourPlanByAvailableSpotsRange(
            @RequestParam Integer min,
            @RequestParam Integer max,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.tourPlanService.getTourPlanByAvailableSpotsBetween(min, max, pageable));
    }

    @GetMapping("/provider/{id}")
    public ResponseEntity<List<TourPlanResponse>> findAllTourPlanByProviderId(@PathVariable Integer id) {
        return ResponseEntity.ok(this.tourPlanService.getTourPlanByProviderId(id));
    }

    @GetMapping("/title-price")
    public ResponseEntity<List<TourPlanResponse>> findAllTourPlanByTitleAndPrice(
            @RequestParam String t,
            @RequestParam BigDecimal p
    ) {
        return ResponseEntity.ok(this.tourPlanService.getTourPlanByTitleAndPrice(t, p));
    }

    @GetMapping("/title-price-range")
    public ResponseEntity<Page<TourPlanResponse>> findAllTourPlanByTitleAndPriceRange(
            @RequestParam String t,
            @RequestParam BigDecimal min,
            @RequestParam BigDecimal max,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.tourPlanService.getTourPlanByTitleAndPriceBetween(t, min, max, pageable));
    }

    @GetMapping("/title-price-duration")
    public ResponseEntity<Page<TourPlanResponse>> findAllTourPlanByTitleAndPriceBetweenAndDurationBetween(
            @RequestParam String t,
            @RequestParam BigDecimal minP,
            @RequestParam BigDecimal maxP,
            @RequestParam Integer minD,
            @RequestParam Integer maxD,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.tourPlanService.getTourPlanByTitleAndPriceBetweenAndDurationBetween(t, minP, maxP, minD, maxD, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TourPlanResponse>> searchTourPlans(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<TourPlanResponse> tourPlans = tourPlanService.getTop10TourPlanByTitleContaining(q, pageable);

        return ResponseEntity.ok(tourPlans);
    }
}
