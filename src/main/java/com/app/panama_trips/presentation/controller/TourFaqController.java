package com.app.panama_trips.presentation.controller;

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

}
