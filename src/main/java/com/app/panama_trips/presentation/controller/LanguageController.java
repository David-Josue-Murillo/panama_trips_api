package com.app.panama_trips.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import com.app.panama_trips.presentation.dto.LanguageRequest;
import com.app.panama_trips.presentation.dto.LanguageResponse;
import com.app.panama_trips.service.implementation.LanguageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/languages")
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageService service;

    // CRUD operations
    @GetMapping
    public ResponseEntity<Page<LanguageResponse>> getAllLanguages(Pageable pageable) {
        return ResponseEntity.ok(service.getAllLanguages(pageable));
    }

    @GetMapping("/{code}")
    public ResponseEntity<LanguageResponse> getLanguageByCode(@PathVariable String code) {
        return ResponseEntity.ok(service.getLanguageByCode(code));
    }

    @PostMapping
    public ResponseEntity<LanguageResponse> saveLanguage(@RequestBody LanguageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.saveLanguage(request));
    }

    @PutMapping("/{code}")
    public ResponseEntity<LanguageResponse> updateLanguage(
            @PathVariable String code,
            @RequestBody LanguageRequest request) {
        return ResponseEntity.ok(service.updateLanguage(code, request));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable String code) {
        service.deleteLanguage(code);
        return ResponseEntity.noContent().build();
    }
}
