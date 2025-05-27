package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.persistence.entity.Guide;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.presentation.dto.TourAssignmentRequest;
import com.app.panama_trips.presentation.dto.TourAssignmentResponse;
import com.app.panama_trips.service.implementation.TourAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tour-assignments")
@RequiredArgsConstructor
public class TourAssignmentController {

    private final TourAssignmentService service;

    @GetMapping
    public ResponseEntity<Page<TourAssignmentResponse>> getAllAssignments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "0") int size,
            @RequestParam(defaultValue = "false") boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
            ? Pageable.ofSize(size).withPage(page)
            : Pageable.unpaged();
        return ResponseEntity.ok(service.getAllAssignments(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourAssignmentResponse> getAssignmentById(@PathVariable Integer id) {
        return service.getAssignmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TourAssignmentResponse createAssignment(@RequestBody TourAssignmentRequest request) {
        return service.createAssignment(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TourAssignmentResponse> updateAssignment(
            @PathVariable Integer id,
            @RequestBody TourAssignmentRequest request) {
        return ResponseEntity.ok(service.updateAssignment(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAssignment(@PathVariable Integer id) {
        service.deleteAssignment(id);
    }

    @GetMapping("/guide/{guideId}")
    public ResponseEntity<List<TourAssignmentResponse>> getAssignmentsByGuide(@PathVariable Integer guideId) {
        Guide guide = new Guide();
        guide.setId(guideId);
        return ResponseEntity.ok(service.getAssignmentsByGuide(guide));
    }

    @GetMapping("/tour-plan/{tourPlanId}")
    public ResponseEntity<List<TourAssignmentResponse>> getAssignmentsByTourPlan(@PathVariable Integer tourPlanId) {
        TourPlan tourPlan = new TourPlan();
        tourPlan.setId(tourPlanId);
        return ResponseEntity.ok(service.getAssignmentsByTourPlan(tourPlan));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TourAssignmentResponse>> getAssignmentsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.getAssignmentsByStatus(status));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<TourAssignmentResponse>> getAssignmentsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(service.getAssignmentsByDate(date));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<TourAssignmentResponse>> getAssignmentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(service.getAssignmentsByDateRange(startDate, endDate));
    }

    @GetMapping("/guide/{guideId}/status/{status}")
    public ResponseEntity<List<TourAssignmentResponse>> getAssignmentsByGuideAndStatus(
            @PathVariable Integer guideId,
            @PathVariable String status) {
        Guide guide = new Guide();
        guide.setId(guideId);
        return ResponseEntity.ok(service.getAssignmentsByGuideAndStatus(guide, status));
    }

    @GetMapping("/tour-plan/{tourPlanId}/date-range")
    public ResponseEntity<List<TourAssignmentResponse>> getAssignmentsByTourPlanAndDateRange(
            @PathVariable Integer tourPlanId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        TourPlan tourPlan = new TourPlan();
        tourPlan.setId(tourPlanId);
        return ResponseEntity.ok(service.getAssignmentsByTourPlanAndDateRange(tourPlan, startDate, endDate));
    }

    @GetMapping("/guide-tour-plan-date")
    public ResponseEntity<TourAssignmentResponse> getAssignmentByGuideTourPlanAndDate(
            @RequestParam Integer guideId,
            @RequestParam Integer tourPlanId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Guide guide = new Guide();
        guide.setId(guideId);
        TourPlan tourPlan = new TourPlan();
        tourPlan.setId(tourPlanId);
        return service.getAssignmentByGuideTourPlanAndDate(guide, tourPlan, date)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/guide/{guideId}/upcoming")
    public ResponseEntity<List<TourAssignmentResponse>> getUpcomingAssignmentsByGuide(
            @PathVariable Integer guideId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        return ResponseEntity.ok(service.getUpcomingAssignmentsByGuide(guideId, startDate));
    }

    @GetMapping("/guide/{guideId}/status/{status}/count")
    public ResponseEntity<Long> countAssignmentsByGuideAndStatus(
            @PathVariable Integer guideId,
            @PathVariable String status) {
        return ResponseEntity.ok(service.countAssignmentsByGuideAndStatus(guideId, status));
    }

    @GetMapping("/guide/{guideId}/availability")
    public ResponseEntity<Boolean> isGuideAvailableForDate(
            @PathVariable Integer guideId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Guide guide = new Guide();
        guide.setId(guideId);
        return ResponseEntity.ok(service.isGuideAvailableForDate(guide, date));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TourAssignmentResponse> updateAssignmentStatus(
            @PathVariable Integer id,
            @RequestParam String newStatus) {
        return ResponseEntity.ok(service.updateAssignmentStatus(id, newStatus));
    }

    @PatchMapping("/{id}/notes")
    public ResponseEntity<TourAssignmentResponse> addNotesToAssignment(
            @PathVariable Integer id,
            @RequestParam String notes) {
        return ResponseEntity.ok(service.addNotesToAssignment(id, notes));
    }
}
