package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.CancellationPolicyRequest;
import com.app.panama_trips.presentation.dto.CancellationPolicyResponse;
import com.app.panama_trips.service.implementation.CancellationPolicyService;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cancellation-policies")
@RequiredArgsConstructor
public class CancellationPolicyController {

    private final CancellationPolicyService service;

    @GetMapping
    public ResponseEntity<Page<CancellationPolicyResponse>> getAllCancellationPolicies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.service.getAllCancellationPolicies(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CancellationPolicyResponse> getCancellationPolicyById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.service.getCancellationPolicyById(id));
    }

    @PostMapping
    public ResponseEntity<CancellationPolicyResponse> saveCancellationPolicy(@RequestBody CancellationPolicyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.saveCancellationPolicy(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CancellationPolicyResponse> updateCancellationPolicy(@PathVariable Integer id, @RequestBody CancellationPolicyRequest request) {
        return ResponseEntity.ok(this.service.updateCancellationPolicy(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCancellationPolicy(@PathVariable Integer id) {
        this.service.deleteCancellationPolicy(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/refund")
    public ResponseEntity<List<CancellationPolicyResponse>> findByRefundPercentageGreaterThanEqual(@RequestParam Integer percentage) {
        return ResponseEntity.ok(this.service.findByRefundPercentageGreaterThanEqual(percentage));
    }

    @GetMapping("/min-day")
    public ResponseEntity<List<CancellationPolicyResponse>> findByDaysBeforeTourGreaterThanEqual(@RequestParam Integer minDay){
        return ResponseEntity.ok(this.service.findByDaysBeforeTourGreaterThanEqual(minDay));
    }

    @GetMapping("/find")
    public ResponseEntity<Optional<CancellationPolicyResponse>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(this.service.findByName(name));
    }

    @GetMapping("/find-policies")
    public ResponseEntity<List<CancellationPolicyResponse>> findEligiblePolicies(@RequestParam Integer minPercentage, Integer maxDays) {
        return ResponseEntity.ok(this.service.findEligiblePolicies(minPercentage, maxDays));
    }

    @GetMapping("/recommended")
    public ResponseEntity<CancellationPolicyResponse> getRecommendedPolicy(@RequestParam Integer daysBeforeTrip) {
        return ResponseEntity.ok(this.service.getRecommendedPolicy(daysBeforeTrip));
    }

    @GetMapping("/{policyId}/eligible")
    public ResponseEntity<Boolean> isPolicyEligibleForRefund(
            @PathVariable Integer policyId,
            @RequestParam Integer daysRemaining) {
        return ResponseEntity.ok(this.service.isPolicyEligibleForRefund(policyId, daysRemaining));
    }

    @GetMapping("/{policyId}/calculate-refund")
    public ResponseEntity<Integer> calculateRefundAmount(
            @PathVariable Integer policyId,
            @RequestParam Integer totalAmount,
            @RequestParam Integer daysRemaining) {
        return ResponseEntity.ok(this.service.calculateRefundAmount(policyId, totalAmount, daysRemaining));
    }

    @GetMapping("/active")
    public ResponseEntity<List<CancellationPolicyResponse>> getActivePolicies() {
        return ResponseEntity.ok(this.service.getActivePolicies());
    }

    @PostMapping("/bulk")
    public ResponseEntity<Void> bulkCreatePolicies(@RequestBody List<CancellationPolicyRequest> requests) {
        this.service.bulkCreatePolicies(requests);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/bulk")
    public ResponseEntity<Void> bulkUpdatePolicies(@RequestBody List<CancellationPolicyRequest> requests) {
        this.service.bulkUpdatePolicies(requests);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsPolicyWithName(@RequestParam String name) {
        return ResponseEntity.ok(this.service.existsPolicyWithName(name));
    }

    @GetMapping("/{policyId}/in-use")
    public ResponseEntity<Boolean> isPolicyUsedByAnyTour(@PathVariable Integer policyId) {
        return ResponseEntity.ok(this.service.isPolicyUsedByAnyTour(policyId));
    }
}
