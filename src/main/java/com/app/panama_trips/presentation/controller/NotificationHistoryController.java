package com.app.panama_trips.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.panama_trips.presentation.dto.NotificationHistoryRequest;
import com.app.panama_trips.presentation.dto.NotificationHistoryResponse;
import com.app.panama_trips.service.implementation.NotificationHistoryService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notification-history")
@RequiredArgsConstructor
public class NotificationHistoryController {
    private final NotificationHistoryService service;

    @GetMapping
    public ResponseEntity<Page<NotificationHistoryResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.getAllNotificationHistory(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationHistoryResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getNotificationHistoryById(id));
    }

    @PostMapping
    public ResponseEntity<NotificationHistoryResponse> create(@RequestBody NotificationHistoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.saveNotificationHistory(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationHistoryResponse> update(@PathVariable Integer id, @RequestBody NotificationHistoryRequest request) {
        return ResponseEntity.ok(service.updateNotificationHistory(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteNotificationHistory(id);
        return ResponseEntity.noContent().build();
    }

    // Búsquedas por campos
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationHistoryResponse>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }

    @GetMapping("/template/{templateId}")
    public ResponseEntity<List<NotificationHistoryResponse>> findByTemplateId(@PathVariable Integer templateId) {
        return ResponseEntity.ok(service.findByTemplateId(templateId));
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<List<NotificationHistoryResponse>> findByReservationId(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(service.findByReservationId(reservationId));
    }

    @GetMapping("/status/{deliveryStatus}")
    public ResponseEntity<List<NotificationHistoryResponse>> findByDeliveryStatus(@PathVariable String deliveryStatus) {
        return ResponseEntity.ok(service.findByDeliveryStatus(deliveryStatus));
    }

    @GetMapping("/channel/{channel}")
    public ResponseEntity<List<NotificationHistoryResponse>> findByChannel(@PathVariable String channel) {
        return ResponseEntity.ok(service.findByChannel(channel));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<NotificationHistoryResponse>> findByDateRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(service.findByDateRange(start, end));
    }

    @GetMapping("/date-range/user")
    public ResponseEntity<List<NotificationHistoryResponse>> findByDateRangeAndUser(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            @RequestParam Long userId) {
        return ResponseEntity.ok(service.findByDateRangeAndUser(start, end, userId));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<NotificationHistoryResponse>> getRecentNotifications(@RequestParam int limit) {
        return ResponseEntity.ok(service.getRecentNotifications(limit));
    }

    @GetMapping("/search")
    public ResponseEntity<List<NotificationHistoryResponse>> searchByContent(@RequestParam String keyword) {
        return ResponseEntity.ok(service.searchNotificationsByContent(keyword));
    }

    @GetMapping("/latest/user/{userId}")
    public ResponseEntity<NotificationHistoryResponse> findLatestByUser(@PathVariable Long userId) {
        Optional<NotificationHistoryResponse> result = service.findLatestNotificationByUser(userId);
        return result.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Conteos
    @GetMapping("/count/user/{userId}")
    public ResponseEntity<Long> countByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(service.countByUserId(userId));
    }

    @GetMapping("/count/status/{deliveryStatus}")
    public ResponseEntity<Long> countByDeliveryStatus(@PathVariable String deliveryStatus) {
        return ResponseEntity.ok(service.countByDeliveryStatus(deliveryStatus));
    }

    @GetMapping("/count/channel/{channel}")
    public ResponseEntity<Long> countByChannel(@PathVariable String channel) {
        return ResponseEntity.ok(service.countByChannel(channel));
    }

    @GetMapping("/failed")
    public ResponseEntity<List<NotificationHistoryResponse>> getFailedNotifications() {
        return ResponseEntity.ok(service.getFailedNotifications());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<NotificationHistoryResponse>> getPendingNotifications() {
        return ResponseEntity.ok(service.getPendingNotifications());
    }

    @GetMapping("/delivered")
    public ResponseEntity<List<NotificationHistoryResponse>> getDeliveredNotifications() {
        return ResponseEntity.ok(service.getDeliveredNotifications());
    }

    @GetMapping("/user/{userId}/date-range")
    public ResponseEntity<List<NotificationHistoryResponse>> getByUserAndDateRange(
            @PathVariable Long userId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return ResponseEntity.ok(service.getNotificationsByUserAndDateRange(userId, start, end));
    }

    @GetMapping("/reservation/{reservationId}/channel/{channel}")
    public ResponseEntity<List<NotificationHistoryResponse>> getByReservationAndChannel(
            @PathVariable Integer reservationId,
            @PathVariable String channel) {
        return ResponseEntity.ok(service.getNotificationsByReservationAndChannel(reservationId, channel));
    }

    @GetMapping("/stats/total")
    public ResponseEntity<Long> getTotalNotificationsSent() {
        return ResponseEntity.ok(service.getTotalNotificationsSent());
    }

    @GetMapping("/stats/delivered")
    public ResponseEntity<Long> getTotalNotificationsDelivered() {
        return ResponseEntity.ok(service.getTotalNotificationsDelivered());
    }

    @GetMapping("/stats/failed")
    public ResponseEntity<Long> getTotalNotificationsFailed() {
        return ResponseEntity.ok(service.getTotalNotificationsFailed());
    }

    @GetMapping("/stats/success-rate")
    public ResponseEntity<Double> getDeliverySuccessRate() {
        return ResponseEntity.ok(service.getDeliverySuccessRate());
    }

    @PostMapping("/bulk")
    public ResponseEntity<Void> bulkCreate(@RequestBody List<NotificationHistoryRequest> requests) {
        service.bulkCreateNotificationHistory(requests);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> bulkDelete(@RequestBody List<Integer> notificationIds) {
        service.bulkDeleteNotificationHistory(notificationIds);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/retry-failed")
    public ResponseEntity<Void> retryFailedNotifications() {
        service.retryFailedNotifications();
        return ResponseEntity.ok().build();
    }
}
