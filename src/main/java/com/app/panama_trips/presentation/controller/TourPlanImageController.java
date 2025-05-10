package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.TourPlanImageRequest;
import com.app.panama_trips.presentation.dto.TourPlanImageResponse;
import com.app.panama_trips.service.implementation.TourPlanImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tour-plan-image")
@RequiredArgsConstructor
public class TourPlanImageController {

    private final TourPlanImageService tourPlanImageService;

    @GetMapping()
    public ResponseEntity<Page<TourPlanImageResponse>> findAllTourPlanImages(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.tourPlanImageService.getAllTourPlanImages(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourPlanImageResponse> findTourPlanImageById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.tourPlanImageService.getTourPlanImageById(id));
    }

    @PostMapping
    public ResponseEntity<TourPlanImageResponse> createTourPlanImage(@RequestBody TourPlanImageRequest request) {
        return new ResponseEntity<>(this.tourPlanImageService.saveTourPlanImage(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TourPlanImageResponse> updateTourPlanImage(
            @PathVariable Integer id,
            @RequestBody TourPlanImageRequest request) {
        return ResponseEntity.ok(this.tourPlanImageService.updateTourPlanImage(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTourPlanImage(@PathVariable Integer id) {
        this.tourPlanImageService.deleteTourPlanImage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tour-plan/{tourPlanId}")
    public ResponseEntity<TourPlanImageResponse> findImageByTourPlanId(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(this.tourPlanImageService.getTourPlanImageById(tourPlanId));
    }

    @GetMapping("/tour-plan/all/{tourPlanId}")
    public ResponseEntity<List<TourPlanImageResponse>> findImagesByTourPlanId(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(this.tourPlanImageService.getTourPlanImagesByTourPlanId(tourPlanId));
    }

    @GetMapping("/tour-plan/{tourPlanId}/ordered")
    public ResponseEntity<List<TourPlanImageResponse>> findImagesByTourPlanIdOrdered(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(this.tourPlanImageService.getTourPlanImagesByTourPlanIdOrderByDisplayOrder(tourPlanId));
    }

    @GetMapping("/tour-plan/{tourPlanId}/main")
    public ResponseEntity<TourPlanImageResponse> findMainImageByTourPlanId(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(this.tourPlanImageService.getMainImageByTourPlanId(tourPlanId));
    }

    @GetMapping("/tour-plan/{tourPlanId}/non-main")
    public ResponseEntity<List<TourPlanImageResponse>> findNonMainImagesByTourPlanId(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(this.tourPlanImageService.getNonMainImagesByTourPlanIdOrdered(tourPlanId));
    }

    @GetMapping("/tour-plan/{tourPlanId}/count")
    public ResponseEntity<Long> countImagesByTourPlanId(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(this.tourPlanImageService.countImagesByTourPlanId(tourPlanId));
    }

    @DeleteMapping("/tour-plan/{tourPlanId}/all")
    public ResponseEntity<Void> deleteAllImagesByTourPlanId(@PathVariable Integer tourPlanId) {
        this.tourPlanImageService.deleteAllImagesByTourPlanId(tourPlanId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tour-plan/{tourPlanId}/max-order")
    public ResponseEntity<Integer> getMaxDisplayOrderForTourPlan(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(this.tourPlanImageService.getMaxDisplayOrderForTourPlan(tourPlanId));
    }
}
