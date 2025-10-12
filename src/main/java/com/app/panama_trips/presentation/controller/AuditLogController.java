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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.panama_trips.persistence.entity.AuditLog;
import com.app.panama_trips.service.implementation.AuditLogService;

import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    // CRUD endpoints
    @GetMapping
    public ResponseEntity<Page<AuditLog>> getAll(Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getAllAuditLogs(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLog> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(auditLogService.getAuditLogById(id));
    }

    @PostMapping
    public ResponseEntity<AuditLog> create(@RequestBody AuditLog request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(auditLogService.saveAuditLog(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuditLog> update(@PathVariable Integer id, @RequestBody AuditLog request) {
        return ResponseEntity.ok(auditLogService.updateAuditLog(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        auditLogService.deleteAuditLog(id);
        return ResponseEntity.noContent().build();
    }

    // Search endpoints
    @GetMapping("/by-entity")
    public ResponseEntity<List<AuditLog>> findByEntity(
            @RequestParam String entityType,
            @RequestParam Integer entityId) {
        return ResponseEntity.ok(auditLogService.findByEntityTypeAndEntityId(entityType, entityId));
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<AuditLog>> findByUser(@RequestParam Integer userId) {
        return ResponseEntity.ok(auditLogService.findByUserId(userId));
    }

    @GetMapping("/by-action")
    public ResponseEntity<List<AuditLog>> findByAction(@RequestParam String action) {
        return ResponseEntity.ok(auditLogService.findByAction(action));
    }

    @GetMapping("/by-ip")
    public ResponseEntity<List<AuditLog>> findByIp(@RequestParam String ip) {
        return ResponseEntity.ok(auditLogService.findByIpAddress(ip));
    }

    @GetMapping("/by-date-range")
    public ResponseEntity<List<AuditLog>> findByDateRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(auditLogService.findByActionTimestampBetween(start, end));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<AuditLog>> getRecent(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(auditLogService.getRecentActivity(limit));
    }

    // Find operations by entity relationships
    @GetMapping("/entity-type/{entityType}")
    public ResponseEntity<List<AuditLog>> findByEntityType(@PathVariable String entityType) {
        return ResponseEntity.ok(auditLogService.findByEntityType(entityType));
    }

    @GetMapping("/entity-id/{entityId}")
    public ResponseEntity<List<AuditLog>> findByEntityId(@PathVariable Integer entityId) {
        return ResponseEntity.ok(auditLogService.findByEntityId(entityId));
    }

    @GetMapping("/timestamp-after/{timestamp}")
    public ResponseEntity<List<AuditLog>> findByTimestampAfter(@PathVariable LocalDateTime timestamp) {
        return ResponseEntity.ok(auditLogService.findByTimestampAfter(timestamp));
    }

    @GetMapping("/timestamp-before/{timestamp}")
    public ResponseEntity<List<AuditLog>> findByTimestampBefore(@PathVariable LocalDateTime timestamp) {
        return ResponseEntity.ok(auditLogService.findByTimestampBefore(timestamp));
    }

    @GetMapping("/user-agent/{userAgent}")
    public ResponseEntity<List<AuditLog>> findByUserAgent(@PathVariable String userAgent) {
        return ResponseEntity.ok(auditLogService.getActivityByUserAgent(userAgent));
    }

    // Specialized queries
    @GetMapping("/user/{userId}/action/{action}")
    public ResponseEntity<List<AuditLog>> findByUserAndAction(@PathVariable Integer userId,
            @PathVariable String action) {
        return ResponseEntity.ok(auditLogService.getActivityByUserAndAction(userId, action));
    }

    @GetMapping("/user/{userId}/entity-type/{entityType}")
    public ResponseEntity<List<AuditLog>> findByUserAndEntityType(@PathVariable Integer userId,
            @PathVariable String entityType) {
        return ResponseEntity.ok(auditLogService.getActivityByUserAndEntityType(userId, entityType));
    }

    @GetMapping("/action/{action}/entity-type/{entityType}")
    public ResponseEntity<List<AuditLog>> findByActionAndEntityType(@PathVariable String action,
            @PathVariable String entityType) {
        return ResponseEntity.ok(auditLogService.getActivityByEntityTypeAndAction(entityType, action));
    }

    @GetMapping("/recent-activity/{entityType}")
    public ResponseEntity<List<AuditLog>> findRecentActivityByEntityType(@PathVariable String entityType,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(auditLogService.getRecentActivityByEntityType(entityType, limit));
    }

    // Business logic operations
    @GetMapping("/activity/date-range")
    public ResponseEntity<List<AuditLog>> getActivityByDateRange(
            @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(auditLogService.getActivityByDateRange(startDate, endDate));
    }

    @GetMapping("/activity/user/{userId}")
    public ResponseEntity<List<AuditLog>> getActivityByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(auditLogService.getActivityByUser(userId));
    }

    @GetMapping("/activity/entity-type/{entityType}")
    public ResponseEntity<List<AuditLog>> getActivityByEntityType(@PathVariable String entityType) {
        return ResponseEntity.ok(auditLogService.getActivityByEntityType(entityType));
    }

    @GetMapping("/activity/action/{action}")
    public ResponseEntity<List<AuditLog>> getActivityByAction(@PathVariable String action) {
        return ResponseEntity.ok(auditLogService.getActivityByAction(action));
    }

    @GetMapping("/activity/ip/{ipAddress}")
    public ResponseEntity<List<AuditLog>> getActivityByIpAddress(@PathVariable String ipAddress) {
        return ResponseEntity.ok(auditLogService.getActivityByIpAddress(ipAddress));
    }

    @GetMapping("/activity/user/{userId}/date-range")
    public ResponseEntity<List<AuditLog>> getActivityByUserAndDateRange(
            @PathVariable Integer userId, @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(auditLogService.getActivityByUserAndDateRange(userId, startDate, endDate));
    }

    @GetMapping("/activity/entity/{entityType}/{entityId}/date-range")
    public ResponseEntity<List<AuditLog>> getActivityByEntityAndDateRange(
            @PathVariable String entityType, @PathVariable Integer entityId,
            @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return ResponseEntity
                .ok(auditLogService.getActivityByEntityAndDateRange(entityType, entityId, startDate, endDate));
    }

    // Advanced queries
    @GetMapping("/user-agent/containing/{pattern}")
    public ResponseEntity<List<AuditLog>> getActivityByUserAgentContaining(@PathVariable String pattern) {
        return ResponseEntity.ok(auditLogService.getActivityByUserAgentContaining(pattern));
    }

    @GetMapping("/ip/{ipAddress}/date-range")
    public ResponseEntity<List<AuditLog>> getActivityByIpAddressAndDateRange(
            @PathVariable String ipAddress, @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(auditLogService.getActivityByIpAddressAndDateRange(ipAddress, startDate, endDate));
    }

    @GetMapping("/action/{action}/date-range")
    public ResponseEntity<List<AuditLog>> getActivityByActionAndDateRange(
            @PathVariable String action, @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(auditLogService.getActivityByActionAndDateRange(action, startDate, endDate));
    }

    // Bulk operations
    @PostMapping("/bulk")
    public ResponseEntity<Void> bulkCreate(@RequestBody List<AuditLog> auditLogs) {
        auditLogService.bulkCreateAuditLogs(auditLogs);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> bulkDelete(@RequestBody List<Integer> auditLogIds) {
        auditLogService.bulkDeleteAuditLogs(auditLogIds);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bulk/entity-type/{entityType}")
    public ResponseEntity<Void> bulkDeleteByEntityType(@PathVariable String entityType) {
        auditLogService.bulkDeleteAuditLogsByEntityType(entityType);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bulk/user/{userId}")
    public ResponseEntity<Void> bulkDeleteByUser(@PathVariable Integer userId) {
        auditLogService.bulkDeleteAuditLogsByUser(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bulk/date-range")
    public ResponseEntity<Void> bulkDeleteByDateRange(
            @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        auditLogService.bulkDeleteAuditLogsByDateRange(startDate, endDate);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/bulk/action/{action}")
    public ResponseEntity<Void> bulkDeleteByAction(@PathVariable String action) {
        auditLogService.bulkDeleteAuditLogsByAction(action);
        return ResponseEntity.noContent().build();
    }

    // Check operations
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Integer id) {
        return ResponseEntity.ok(auditLogService.existsById(id));
    }

    @GetMapping("/exists/entity/{entityType}/{entityId}")
    public ResponseEntity<Boolean> existsByEntityTypeAndEntityId(
            @PathVariable String entityType, @PathVariable Integer entityId) {
        return ResponseEntity.ok(auditLogService.existsByEntityTypeAndEntityId(entityType, entityId));
    }

    @GetMapping("/exists/user/{userId}")
    public ResponseEntity<Boolean> existsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(auditLogService.existsByUser(userId));
    }

    @GetMapping("/exists/action/{action}")
    public ResponseEntity<Boolean> existsByAction(@PathVariable String action) {
        return ResponseEntity.ok(auditLogService.existsByAction(action));
    }

    @GetMapping("/exists/ip/{ipAddress}")
    public ResponseEntity<Boolean> existsByIpAddress(@PathVariable String ipAddress) {
        return ResponseEntity.ok(auditLogService.existsByIpAddress(ipAddress));
    }

    @GetMapping("/count/entity-type/{entityType}")
    public ResponseEntity<Long> countByEntityType(@PathVariable String entityType) {
        return ResponseEntity.ok(auditLogService.countByEntityType(entityType));
    }

    @GetMapping("/count/user/{userId}")
    public ResponseEntity<Long> countByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(auditLogService.countByUser(userId));
    }

    @GetMapping("/count/action/{action}")
    public ResponseEntity<Long> countByAction(@PathVariable String action) {
        return ResponseEntity.ok(auditLogService.countByAction(action));
    }

    @GetMapping("/count/date-range")
    public ResponseEntity<Long> countByDateRange(
            @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(auditLogService.countByDateRange(startDate, endDate));
    }

    @GetMapping("/count/ip/{ipAddress}")
    public ResponseEntity<Long> countByIpAddress(@PathVariable String ipAddress) {
        return ResponseEntity.ok(auditLogService.countByIpAddress(ipAddress));
    }

    // Audit trail operations
    @GetMapping("/audit-trail/entity/{entityType}/{entityId}")
    public ResponseEntity<List<AuditLog>> getAuditTrailForEntity(
            @PathVariable String entityType, @PathVariable Integer entityId) {
        return ResponseEntity.ok(auditLogService.getAuditTrailForEntity(entityType, entityId));
    }

    @GetMapping("/audit-trail/user/{userId}")
    public ResponseEntity<List<AuditLog>> getAuditTrailForUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(auditLogService.getAuditTrailForUser(userId));
    }

    @GetMapping("/audit-trail/entity/{entityType}/{entityId}/user/{userId}")
    public ResponseEntity<List<AuditLog>> getAuditTrailForEntityAndUser(
            @PathVariable String entityType, @PathVariable Integer entityId, @PathVariable Integer userId) {
        return ResponseEntity.ok(auditLogService.getAuditTrailForEntityAndUser(entityType, entityId, userId));
    }

    @GetMapping("/audit-trail/entity/{entityType}/{entityId}/action/{action}")
    public ResponseEntity<List<AuditLog>> getAuditTrailForEntityAndAction(
            @PathVariable String entityType, @PathVariable Integer entityId, @PathVariable String action) {
        return ResponseEntity.ok(auditLogService.getAuditTrailForEntityAndAction(entityType, entityId, action));
    }

    @GetMapping("/audit-trail/user/{userId}/action/{action}")
    public ResponseEntity<List<AuditLog>> getAuditTrailForUserAndAction(
            @PathVariable Integer userId, @PathVariable String action) {
        return ResponseEntity.ok(auditLogService.getAuditTrailForUserAndAction(userId, action));
    }

    @GetMapping("/audit-trail/entity/{entityType}/{entityId}/date-range")
    public ResponseEntity<List<AuditLog>> getAuditTrailForEntityAndDateRange(
            @PathVariable String entityType, @PathVariable Integer entityId,
            @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return ResponseEntity
                .ok(auditLogService.getAuditTrailForEntityAndDateRange(entityType, entityId, startDate, endDate));
    }

    @GetMapping("/audit-trail/user/{userId}/date-range")
    public ResponseEntity<List<AuditLog>> getAuditTrailForUserAndDateRange(
            @PathVariable Integer userId, @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(auditLogService.getAuditTrailForUserAndDateRange(userId, startDate, endDate));
    }

    // Statistics and analytics
    @GetMapping("/stats/total")
    public ResponseEntity<Long> getTotalAuditLogs() {
        return ResponseEntity.ok(auditLogService.getTotalAuditLogs());
    }

    @GetMapping("/stats/entity-type/{entityType}")
    public ResponseEntity<Long> getTotalAuditLogsByEntityType(@PathVariable String entityType) {
        return ResponseEntity.ok(auditLogService.getTotalAuditLogsByEntityType(entityType));
    }

    @GetMapping("/stats/user/{userId}")
    public ResponseEntity<Long> getTotalAuditLogsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(auditLogService.getTotalAuditLogsByUser(userId));
    }

    @GetMapping("/stats/action/{action}")
    public ResponseEntity<Long> getTotalAuditLogsByAction(@PathVariable String action) {
        return ResponseEntity.ok(auditLogService.getTotalAuditLogsByAction(action));
    }

    @GetMapping("/stats/date-range")
    public ResponseEntity<Long> getTotalAuditLogsByDateRange(
            @RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(auditLogService.getTotalAuditLogsByDateRange(startDate, endDate));
    }

    @GetMapping("/stats/ip/{ipAddress}")
    public ResponseEntity<Long> getTotalAuditLogsByIpAddress(@PathVariable String ipAddress) {
        return ResponseEntity.ok(auditLogService.getTotalAuditLogsByIpAddress(ipAddress));
    }

    @GetMapping("/stats/top-users/{limit}")
    public ResponseEntity<List<AuditLog>> getTopUsersByActivity(@PathVariable int limit) {
        return ResponseEntity.ok(auditLogService.getTopUsersByActivity(limit));
    }

    @GetMapping("/stats/top-entity-types/{limit}")
    public ResponseEntity<List<AuditLog>> getTopEntityTypesByActivity(@PathVariable int limit) {
        return ResponseEntity.ok(auditLogService.getTopEntityTypesByActivity(limit));
    }

    @GetMapping("/stats/top-actions/{limit}")
    public ResponseEntity<List<AuditLog>> getTopActionsByActivity(@PathVariable int limit) {
        return ResponseEntity.ok(auditLogService.getTopActionsByActivity(limit));
    }

    @GetMapping("/stats/top-ips/{limit}")
    public ResponseEntity<List<AuditLog>> getTopIpAddressesByActivity(@PathVariable int limit) {
        return ResponseEntity.ok(auditLogService.getTopIpAddressesByActivity(limit));
    }

    @GetMapping("/stats/by-month")
    public ResponseEntity<List<AuditLog>> getActivityByMonth() {
        return ResponseEntity.ok(auditLogService.getActivityByMonth());
    }

    @GetMapping("/stats/by-day-of-week")
    public ResponseEntity<List<AuditLog>> getActivityByDayOfWeek() {
        return ResponseEntity.ok(auditLogService.getActivityByDayOfWeek());
    }

    @GetMapping("/stats/by-hour")
    public ResponseEntity<List<AuditLog>> getActivityByHour() {
        return ResponseEntity.ok(auditLogService.getActivityByHour());
    }

    // Security and monitoring operations
    @GetMapping("/security/suspicious/{ipAddress}")
    public ResponseEntity<List<AuditLog>> getSuspiciousActivity(@PathVariable String ipAddress) {
        return ResponseEntity.ok(auditLogService.getSuspiciousActivity(ipAddress));
    }

    @PostMapping("/security/multiple-ips")
    public ResponseEntity<List<AuditLog>> getActivityByMultipleIpAddresses(@RequestBody List<String> ipAddresses) {
        return ResponseEntity.ok(auditLogService.getActivityByMultipleIpAddresses(ipAddresses));
    }

    @GetMapping("/security/user-agent-pattern/{pattern}")
    public ResponseEntity<List<AuditLog>> getActivityByUserAgentPattern(@PathVariable String pattern) {
        return ResponseEntity.ok(auditLogService.getActivityByUserAgentPattern(pattern));
    }

    @GetMapping("/security/failed-logins")
    public ResponseEntity<List<AuditLog>> getFailedLoginAttempts() {
        return ResponseEntity.ok(auditLogService.getFailedLoginAttempts());
    }

    @GetMapping("/security/successful-logins")
    public ResponseEntity<List<AuditLog>> getSuccessfulLoginAttempts() {
        return ResponseEntity.ok(auditLogService.getSuccessfulLoginAttempts());
    }

    @GetMapping("/security/data-modification")
    public ResponseEntity<List<AuditLog>> getDataModificationActivity() {
        return ResponseEntity.ok(auditLogService.getDataModificationActivity());
    }

    @GetMapping("/security/data-access")
    public ResponseEntity<List<AuditLog>> getDataAccessActivity() {
        return ResponseEntity.ok(auditLogService.getDataAccessActivity());
    }

    @GetMapping("/security/data-deletion")
    public ResponseEntity<List<AuditLog>> getDataDeletionActivity() {
        return ResponseEntity.ok(auditLogService.getDataDeletionActivity());
    }

    // Data integrity operations
    @GetMapping("/integrity/missing-user")
    public ResponseEntity<List<AuditLog>> getAuditLogsWithMissingUser() {
        return ResponseEntity.ok(auditLogService.getAuditLogsWithMissingUser());
    }

    @GetMapping("/integrity/missing-ip")
    public ResponseEntity<List<AuditLog>> getAuditLogsWithMissingIpAddress() {
        return ResponseEntity.ok(auditLogService.getAuditLogsWithMissingIpAddress());
    }

    @GetMapping("/integrity/invalid-json")
    public ResponseEntity<List<AuditLog>> getAuditLogsWithInvalidJsonData() {
        return ResponseEntity.ok(auditLogService.getAuditLogsWithInvalidJsonData());
    }

    @GetMapping("/integrity/empty-old-values")
    public ResponseEntity<List<AuditLog>> getAuditLogsWithEmptyOldValues() {
        return ResponseEntity.ok(auditLogService.getAuditLogsWithEmptyOldValues());
    }

    @GetMapping("/integrity/empty-new-values")
    public ResponseEntity<List<AuditLog>> getAuditLogsWithEmptyNewValues() {
        return ResponseEntity.ok(auditLogService.getAuditLogsWithEmptyNewValues());
    }

    @GetMapping("/integrity/empty-both-values")
    public ResponseEntity<List<AuditLog>> getAuditLogsWithBothEmptyValues() {
        return ResponseEntity.ok(auditLogService.getAuditLogsWithBothEmptyValues());
    }

    // Utility operations
    @PostMapping("/cleanup/{daysToKeep}")
    public ResponseEntity<Void> cleanupOldAuditLogs(@PathVariable int daysToKeep) {
        auditLogService.cleanupOldAuditLogs(daysToKeep);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/archive/{beforeDate}")
    public ResponseEntity<Void> archiveAuditLogs(@PathVariable LocalDateTime beforeDate) {
        auditLogService.archiveAuditLogs(beforeDate);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search/keyword/{keyword}")
    public ResponseEntity<List<AuditLog>> searchAuditLogsByKeyword(@PathVariable String keyword) {
        return ResponseEntity.ok(auditLogService.searchAuditLogsByKeyword(keyword));
    }

    @GetMapping("/search/user-agent/{userAgent}")
    public ResponseEntity<List<AuditLog>> searchAuditLogsByUserAgent(@PathVariable String userAgent) {
        return ResponseEntity.ok(auditLogService.searchAuditLogsByUserAgent(userAgent));
    }

    @GetMapping("/latest/entity/{entityType}/{entityId}")
    public ResponseEntity<AuditLog> findLatestAuditLogByEntity(@PathVariable String entityType,
            @PathVariable Integer entityId) {
        return auditLogService.findLatestAuditLogByEntity(entityType, entityId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/with-json-data")
    public ResponseEntity<List<AuditLog>> getAuditLogsWithJsonData() {
        return ResponseEntity.ok(auditLogService.getAuditLogsWithJsonData());
    }

    @GetMapping("/without-json-data")
    public ResponseEntity<List<AuditLog>> getAuditLogsWithoutJsonData() {
        return ResponseEntity.ok(auditLogService.getAuditLogsWithoutJsonData());
    }

    @GetMapping("/json-field/{fieldName}/{fieldValue}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByJsonField(
            @PathVariable String fieldName, @PathVariable String fieldValue) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByJsonField(fieldName, fieldValue));
    }

    @GetMapping("/json-field/containing/{fieldName}/{fieldValue}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByJsonFieldContaining(
            @PathVariable String fieldName, @PathVariable String fieldValue) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByJsonFieldContaining(fieldName, fieldValue));
    }

    @GetMapping("/json-field/exists/{fieldName}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByJsonFieldExists(@PathVariable String fieldName) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByJsonFieldExists(fieldName));
    }

    @GetMapping("/json-field/not-exists/{fieldName}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByJsonFieldNotExists(@PathVariable String fieldName) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByJsonFieldNotExists(fieldName));
    }
}
