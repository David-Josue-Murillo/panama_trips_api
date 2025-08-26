package com.app.panama_trips.presentation.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.panama_trips.presentation.dto.TourFaqRequest;
import com.app.panama_trips.presentation.dto.TourFaqResponse;
import com.app.panama_trips.service.implementation.TourFaqService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tour-faq")
@RequiredArgsConstructor
public class TourFaqController {

    private final TourFaqService service;

    // CRUD operations
    @GetMapping
    public ResponseEntity<Page<TourFaqResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.getAllFaqs(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourFaqResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getFaqById(id));
    }

    @PostMapping
    public ResponseEntity<TourFaqResponse> create(@RequestBody TourFaqRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.saveFaq(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TourFaqResponse> update(@PathVariable Integer id,
            @RequestBody TourFaqRequest request) {
        return ResponseEntity.ok(service.updateFaq(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteFaq(id);
        return ResponseEntity.noContent().build();
    }

    // Find operations by tour plan
    @GetMapping("/tour-plan/{tourPlanId}")
    public ResponseEntity<List<TourFaqResponse>> findByTourPlanId(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(service.findByTourPlanId(tourPlanId));
    }

    @GetMapping("/tour-plan/{tourPlanId}/ordered")
    public ResponseEntity<List<TourFaqResponse>> findByTourPlanIdOrdered(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(service.findByTourPlanIdOrderByDisplayOrderAsc(tourPlanId));
    }

    @GetMapping("/tour-plan/{tourPlanId}/top/{limit}")
    public ResponseEntity<List<TourFaqResponse>> getTopFaqsByTourPlan(
            @PathVariable Integer tourPlanId, @PathVariable int limit) {
        return ResponseEntity.ok(service.getTopFaqsByTourPlan(tourPlanId, limit));
    }

    // Search operations
    @GetMapping("/search")
    public ResponseEntity<List<TourFaqResponse>> searchByKeyword(@RequestParam String keyword) {
        return ResponseEntity.ok(service.searchByQuestionOrAnswer(keyword));
    }

    @GetMapping("/tour-plan/{tourPlanId}/question")
    public ResponseEntity<TourFaqResponse> findByTourPlanIdAndQuestion(
            @PathVariable Integer tourPlanId, @RequestParam String question) {
        Optional<TourFaqResponse> result = service.findByTourPlanIdAndQuestion(tourPlanId, question);
        return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Bulk operations
    @PostMapping("/bulk")
    public ResponseEntity<Void> bulkCreate(@RequestBody List<TourFaqRequest> requests) {
        service.bulkCreateFaqs(requests);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/bulk")
    public ResponseEntity<Void> bulkUpdate(@RequestBody List<TourFaqRequest> requests) {
        service.bulkUpdateFaqs(requests);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> bulkDelete(@RequestBody List<Integer> faqIds) {
        service.bulkDeleteFaqs(faqIds);
        return ResponseEntity.noContent().build();
    }
}
