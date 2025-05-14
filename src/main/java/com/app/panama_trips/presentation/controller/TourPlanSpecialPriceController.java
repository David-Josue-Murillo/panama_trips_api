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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tour-plan-special-price")
@RequiredArgsConstructor
public class TourPlanSpecialPriceController {

    private final TourPlanSpecialPriceService service;

    // Define your endpoints here, for example:
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

    public ResponseEntity<TourPlanSpecialPriceResponse> getById(Integer id) {
        return ResponseEntity.ok(this.service.findById(id));
    }

    public ResponseEntity<TourPlanSpecialPriceResponse> create(TourPlanSpecialPriceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(request));
    }

    public ResponseEntity<TourPlanSpecialPriceResponse> update(Integer id, TourPlanSpecialPriceRequest request) {
        return ResponseEntity.ok(this.service.update(id, request));
    }

    public ResponseEntity<Void> delete(Integer id) {
        this.service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
