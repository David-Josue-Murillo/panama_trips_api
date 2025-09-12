package com.app.panama_trips.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.panama_trips.presentation.dto.TourPriceHistoryRequest;
import com.app.panama_trips.presentation.dto.TourPriceHistoryResponse;
import com.app.panama_trips.service.implementation.TourPriceHistoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;




@RestController
@RequestMapping("/api/tour-price-history")
@RequiredArgsConstructor
public class TourPriceHistoryController {

    private final TourPriceHistoryService service;

    // CRUD operations
    @GetMapping
    public ResponseEntity<Page<TourPriceHistoryResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.getAllTourPriceHistories(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourPriceHistoryResponse> getById (@PathVariable Integer id) {
        return ResponseEntity.ok(service.getTourPriceHistoryById(id));
    }

    @PostMapping
    public ResponseEntity<TourPriceHistoryResponse> create(@RequestBody TourPriceHistoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveTourPriceHistory(request));
    }
    
    
    @PutMapping("/{id}")
    public ResponseEntity<TourPriceHistoryResponse> update(@PathVariable Integer id, @RequestBody TourPriceHistoryRequest request) {
        return ResponseEntity.ok(service.updateTourPriceHistory(id, request));
    }

    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteTourPriceHistory(id);
        return ResponseEntity.noContent().build();
    }
}
