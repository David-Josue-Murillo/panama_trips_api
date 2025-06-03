package com.app.panama_trips.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.panama_trips.presentation.dto.NotificationTemplateRequest;
import com.app.panama_trips.presentation.dto.NotificationTemplateResponse;
import com.app.panama_trips.service.implementation.NotificationTemplateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notification-templates")
@RequiredArgsConstructor
public class NotificationTemplateController {

    private final NotificationTemplateService service;

    @GetMapping
    public ResponseEntity<Page<NotificationTemplateResponse>> getAllTemplates(Pageable pageable) {
        return ResponseEntity.ok(service.getAllTemplates(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationTemplateResponse> getTemplateById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getTemplateById(id));
    }

    @PostMapping
    public ResponseEntity<NotificationTemplateResponse> createTemplate(@RequestBody NotificationTemplateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.saveTemplate(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationTemplateResponse> updateTemplate(@PathVariable Integer id, @RequestBody NotificationTemplateRequest request) {
        return ResponseEntity.ok(service.updateTemplate(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Integer id) {
        service.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }
}
