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

import java.util.List;

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

    @GetMapping("/type/{type}")
    public ResponseEntity<List<NotificationTemplateResponse>> getTemplatesByType(@PathVariable String type) {
        return ResponseEntity.ok(service.findByType(type));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<NotificationTemplateResponse> getTemplateByName(@PathVariable String name) {
        return service.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<NotificationTemplateResponse>> searchTemplates(@RequestParam String keyword) {
        return ResponseEntity.ok(service.searchBySubjectOrBody(keyword));
    }

    @GetMapping("/type/{type}/name")
    public ResponseEntity<List<NotificationTemplateResponse>> getTemplatesByTypeAndName(
            @PathVariable String type,
            @RequestParam String name) {
        return ResponseEntity.ok(service.findByTypeAndNameContaining(type, name));
    }

    @GetMapping("/type/{type}/content")
    public ResponseEntity<List<NotificationTemplateResponse>> getTemplatesByTypeAndContent(
            @PathVariable String type,
            @RequestParam String content) {
        return ResponseEntity.ok(service.getTemplatesByTypeAndContent(type, content));
    }

    @GetMapping("/active")
    public ResponseEntity<List<NotificationTemplateResponse>> getActiveTemplates() {
        return ResponseEntity.ok(service.getActiveTemplates());
    }

    @GetMapping("/variable/{variable}")
    public ResponseEntity<List<NotificationTemplateResponse>> getTemplatesByVariable(@PathVariable String variable) {
        return ResponseEntity.ok(service.getTemplatesByVariable(variable));
    }

    @GetMapping("/notification")
    public ResponseEntity<NotificationTemplateResponse> getTemplateForNotification(
            @RequestParam String type,
            @RequestParam String event) {
        return ResponseEntity.ok(service.getTemplateForNotification(type, event));
    }

    @PostMapping("/bulk")
    public ResponseEntity<Void> bulkCreateTemplates(@RequestBody List<NotificationTemplateRequest> requests) {
        service.bulkCreateTemplates(requests);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/bulk")
    public ResponseEntity<Void> bulkUpdateTemplates(@RequestBody List<NotificationTemplateRequest> requests) {
        service.bulkUpdateTemplates(requests);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> bulkDeleteTemplates(@RequestBody List<Integer> templateIds) {
        service.bulkDeleteTemplates(templateIds);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{name}")
    public ResponseEntity<Boolean> checkTemplateExists(@PathVariable String name) {
        return ResponseEntity.ok(service.existsByName(name));
    }

    @GetMapping("/count/{type}")
    public ResponseEntity<Long> countTemplatesByType(@PathVariable String type) {
        return ResponseEntity.ok(service.countByType(type));
    }

    @PostMapping("/{id}/validate-variables")
    public ResponseEntity<Boolean> validateTemplateVariables(
            @PathVariable Integer id,
            @RequestBody List<String> variables) {
        return ResponseEntity.ok(service.validateTemplateVariables(id, variables));
    }
}
