package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.persistence.entity.CancellationPolicy;
import com.app.panama_trips.presentation.dto.CancellationPolicyRequest;
import com.app.panama_trips.presentation.dto.CancellationPolicyResponse;
import com.app.panama_trips.service.implementation.CancellationPolicyService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
