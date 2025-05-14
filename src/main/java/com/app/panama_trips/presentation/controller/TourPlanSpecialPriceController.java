package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.TourPlanSpecialPriceRequest;
import com.app.panama_trips.presentation.dto.TourPlanSpecialPriceResponse;
import com.app.panama_trips.service.implementation.TourPlanSpecialPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
