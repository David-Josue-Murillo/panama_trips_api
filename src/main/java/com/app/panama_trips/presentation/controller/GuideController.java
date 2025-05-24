package com.app.panama_trips.presentation.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.RequiredArgsConstructor;
import com.app.panama_trips.service.implementation.GuideService;
import com.app.panama_trips.presentation.dto.GuideRequest;
import com.app.panama_trips.presentation.dto.GuideResponse;
import com.app.panama_trips.persistence.entity.Provider;
import com.app.panama_trips.persistence.entity.UserEntity;
import java.util.List;

@RestController
@RequestMapping("/api/guides")
@RequiredArgsConstructor
public class GuideController {

    private final GuideService guideService;

    @GetMapping
    public ResponseEntity<Page<GuideResponse>> getAllGuides(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(guideService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuideResponse> getGuideById(@PathVariable Integer id) {
        return guideService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GuideResponse> createGuide(@RequestBody GuideRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(guideService.createGuide(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuideResponse> updateGuide(
            @PathVariable Integer id,
            @RequestBody GuideRequest request
    ) {
        return ResponseEntity.ok(guideService.updateGuide(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuide(@PathVariable Integer id) {
        guideService.deleteGuide(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<GuideResponse>> getActiveGuides() {
        return ResponseEntity.ok(guideService.findAllActive());
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<GuideResponse>> getGuidesByProvider(@PathVariable Integer providerId) {
        Provider provider = new Provider();
        provider.setId(providerId);
        return ResponseEntity.ok(guideService.findByProvider(provider));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<GuideResponse> getGuideByUser(@PathVariable Long userId) {
        UserEntity user = new UserEntity();
        user.setId(userId);
        return guideService.findByUser(user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/experience")
    public ResponseEntity<List<GuideResponse>> getGuidesByExperience(@RequestParam Integer minYears) {
        return ResponseEntity.ok(guideService.findByYearsExperienceGreaterThanEqual(minYears));
    }

    @GetMapping("/provider/{providerId}/active")
    public ResponseEntity<List<GuideResponse>> getActiveGuidesByProvider(@PathVariable Integer providerId) {
        return ResponseEntity.ok(guideService.findActiveGuidesByProvider(providerId));
    }

    @GetMapping("/language")
    public ResponseEntity<List<GuideResponse>> getGuidesByLanguage(@RequestParam String language) {
        return ResponseEntity.ok(guideService.findByLanguageAndActive(language));
    }

    @GetMapping("/specialty")
    public ResponseEntity<List<GuideResponse>> getGuidesBySpecialty(@RequestParam String specialty) {
        return ResponseEntity.ok(guideService.findBySpecialtyAndActive(specialty));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activateGuide(@PathVariable Integer id) {
        guideService.activateGuide(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateGuide(@PathVariable Integer id) {
        guideService.deactivateGuide(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsGuide(@PathVariable Integer id) {
        return ResponseEntity.ok(guideService.existsById(id));
    }
}
