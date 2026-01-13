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

import com.app.panama_trips.presentation.dto.TourTranslationRequest;
import com.app.panama_trips.presentation.dto.TourTranslationResponse;
import com.app.panama_trips.service.implementation.TourTranslationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tour-translations")
@RequiredArgsConstructor
public class TourTranslationController {
    private final TourTranslationService service;

    // CRUD operations
    @GetMapping
    public ResponseEntity<Page<TourTranslationResponse>> getAllTourTranslations(Pageable pageable) {
        return ResponseEntity.ok(service.getAllTourTranslations(pageable));
    }

    @GetMapping("/{tourPlanId}/{languageCode}")
    public ResponseEntity<TourTranslationResponse> getTourTranslationByTourPlanIdAndLanguageCode(
            @PathVariable Integer tourPlanId,
            @PathVariable String languageCode) {
        return ResponseEntity.ok(service.getTourTranslationByTourPlanIdAndLanguageCode(tourPlanId, languageCode));
    }

    @PostMapping
    public ResponseEntity<TourTranslationResponse> saveTourTranslation(
            @Valid @RequestBody TourTranslationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.saveTourTranslation(request));
    }

    @PutMapping("/{tourPlanId}/{languageCode}")
    public ResponseEntity<TourTranslationResponse> updateTourTranslation(
            @PathVariable Integer tourPlanId,
            @PathVariable String languageCode,
            @Valid @RequestBody TourTranslationRequest request) {
        return ResponseEntity.ok(service.updateTourTranslation(tourPlanId, languageCode, request));
    }

    @DeleteMapping("/{tourPlanId}/{languageCode}")
    public ResponseEntity<Void> deleteTourTranslation(
            @PathVariable Integer tourPlanId,
            @PathVariable String languageCode) {
        service.deleteTourTranslation(tourPlanId, languageCode);
        return ResponseEntity.noContent().build();
    }
}
