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

    // Reorder operations
    @PutMapping("/tour-plan/{tourPlanId}/reorder")
    public ResponseEntity<Void> reorderFaqs(@PathVariable Integer tourPlanId,
            @RequestBody List<Integer> faqIdsInOrder) {
        service.reorderFaqs(tourPlanId, faqIdsInOrder);
        return ResponseEntity.ok().build();
    }

    // Check operations
    @GetMapping("/exists/tour-plan/{tourPlanId}/question")
    public ResponseEntity<Boolean> existsByTourPlanIdAndQuestion(
            @PathVariable Integer tourPlanId, @RequestParam String question) {
        return ResponseEntity.ok(service.existsByTourPlanIdAndQuestion(tourPlanId, question));
    }

    @GetMapping("/exists/tour-plan/{tourPlanId}/display-order/{displayOrder}")
    public ResponseEntity<Boolean> isDisplayOrderUniqueWithinTourPlan(
            @PathVariable Integer tourPlanId, @PathVariable Integer displayOrder) {
        return ResponseEntity.ok(service.isDisplayOrderUniqueWithinTourPlan(tourPlanId, displayOrder));
    }

    @GetMapping("/count/tour-plan/{tourPlanId}")
    public ResponseEntity<Long> countByTourPlanId(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(service.countByTourPlanId(tourPlanId));
    }

    // Statistics and analytics
    @GetMapping("/stats/total")
    public ResponseEntity<Long> getTotalFaqs() {
        // This would need to be implemented in the service if needed
        return ResponseEntity.ok(0L);
    }

    @GetMapping("/stats/tour-plan/{tourPlanId}")
    public ResponseEntity<Long> getTotalFaqsByTourPlan(@PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(service.countByTourPlanId(tourPlanId));
    }

    // Validation endpoints
    @GetMapping("/validate/tour-plan/{tourPlanId}/question")
    public ResponseEntity<Boolean> validateQuestionUniqueness(
            @PathVariable Integer tourPlanId, @RequestParam String question) {
        return ResponseEntity.ok(!service.existsByTourPlanIdAndQuestion(tourPlanId, question));
    }

    @GetMapping("/validate/tour-plan/{tourPlanId}/display-order/{displayOrder}")
    public ResponseEntity<Boolean> validateDisplayOrderUniqueness(
            @PathVariable Integer tourPlanId, @PathVariable Integer displayOrder) {
        return ResponseEntity.ok(service.isDisplayOrderUniqueWithinTourPlan(tourPlanId, displayOrder));
    }

    // Utility operations
    @GetMapping("/search/advanced")
    public ResponseEntity<List<TourFaqResponse>> advancedSearch(
            @RequestParam(required = false) Integer tourPlanId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer limit) {

        if (tourPlanId != null && keyword != null) {
            // Search within specific tour plan
            List<TourFaqResponse> tourPlanFaqs = service.findByTourPlanId(tourPlanId);
            return ResponseEntity.ok(tourPlanFaqs.stream()
                    .filter(faq -> faq.question().toLowerCase().contains(keyword.toLowerCase()) ||
                            faq.answer().toLowerCase().contains(keyword.toLowerCase()))
                    .limit(limit != null ? limit : tourPlanFaqs.size())
                    .toList());
        } else if (tourPlanId != null) {
            // Get all FAQs for tour plan
            List<TourFaqResponse> result = service.findByTourPlanId(tourPlanId);
            if (limit != null) {
                result = result.stream().limit(limit).toList();
            }
            return ResponseEntity.ok(result);
        } else if (keyword != null) {
            // Search across all FAQs
            List<TourFaqResponse> result = service.searchByQuestionOrAnswer(keyword);
            if (limit != null) {
                result = result.stream().limit(limit).toList();
            }
            return ResponseEntity.ok(result);
        } else {
            // Return empty list if no search criteria provided
            return ResponseEntity.ok(List.of());
        }
    }

    @GetMapping("/tour-plan/{tourPlanId}/ordered/limit/{limit}")
    public ResponseEntity<List<TourFaqResponse>> getOrderedFaqsWithLimit(
            @PathVariable Integer tourPlanId, @PathVariable int limit) {
        return ResponseEntity.ok(service.getTopFaqsByTourPlan(tourPlanId, limit));
    }

    // Health check and info endpoints
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("TourFaq service is running");
    }

    @GetMapping("/info")
    public ResponseEntity<String> info() {
        return ResponseEntity.ok("TourFaq API - Manage frequently asked questions for tour plans");
    }
}
