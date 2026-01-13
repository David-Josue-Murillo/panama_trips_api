package com.app.panama_trips.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.panama_trips.presentation.dto.TourTranslationResponse;
import com.app.panama_trips.service.implementation.TourTranslationService;

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
}
