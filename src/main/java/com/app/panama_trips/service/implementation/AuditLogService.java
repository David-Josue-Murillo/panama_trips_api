package com.app.panama_trips.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.AuditLog;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.repository.AuditLogRepository;
import com.app.panama_trips.service.interfaces.IAuditLogService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogService implements IAuditLogService {

    private final AuditLogRepository repository;

    // CRUD operations
    @Override
    @Transactional(readOnly = true)
    public Page<AuditLog> getAllAuditLogs(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public AuditLog getAuditLogById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Audit log not found with id: " + id));
    }

    @Override
    @Transactional
    public AuditLog saveAuditLog(AuditLog auditLog) {
        validateAuditLog(auditLog);
        return repository.save(auditLog);
    }

    @Override
    @Transactional
    public AuditLog updateAuditLog(Integer id, AuditLog auditLog) {
        AuditLog existingAuditLog = findAuditLogOrFail(id);
        updateAuditLogFields(existingAuditLog, auditLog);
        return repository.save(existingAuditLog);
    }

    @Override
    @Transactional
    public void deleteAuditLog(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Audit log not found with id: " + id);
        }
        repository.deleteById(id);
    }

    // Find operations by entity relationships
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByEntityTypeAndEntityId(String entityType, Integer entityId) {
        return repository.findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByUser(UserEntity user) {
        return repository.findByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByUserId(Integer userId) {
        UserEntity user = new UserEntity();
        user.setId(userId.longValue());
        return repository.findByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByAction(String action) {
        return repository.findByAction(action);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByActionTimestampBetween(LocalDateTime start, LocalDateTime end) {
        return repository.findByActionTimestampBetween(start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByIpAddress(String ipAddress) {
        return repository.findByIpAddress(ipAddress);
    }

    // Specialized queries from repository
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findRecentActivityByEntityType(String entityType, LocalDateTime since) {
        return repository.findRecentActivityByEntityType(entityType, since);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByEntityType(String entityType) {
        return repository.findAll().stream()
                .filter(log -> entityType.equals(log.getEntityType()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByEntityId(Integer entityId) {
        return repository.findAll().stream()
                .filter(log -> entityId.equals(log.getEntityId()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByUserAndAction(UserEntity user, String action) {
        return repository.findByUser(user).stream()
                .filter(log -> action.equals(log.getAction()))
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByUserAndEntityType(UserEntity user, String entityType) {
        return repository.findByUser(user).stream()
                .filter(log -> entityType.equals(log.getEntityType()))
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByActionAndEntityType(String action, String entityType) {
        return repository.findByAction(action).stream()
                .filter(log -> entityType.equals(log.getEntityType()))
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByTimestampAfter(LocalDateTime timestamp) {
        return repository.findAll().stream()
                .filter(log -> log.getActionTimestamp().isAfter(timestamp))
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByTimestampBefore(LocalDateTime timestamp) {
        return repository.findAll().stream()
                .filter(log -> log.getActionTimestamp().isBefore(timestamp))
                .toList();

    }

    // Business logic operations
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getRecentActivity(int limit) {
        return repository.findAll().stream()
                .sorted((a, b) -> b.getActionTimestamp().compareTo(a.getActionTimestamp()))
                .limit(limit)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByActionTimestampBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByUser(Integer userId) {
        return findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByEntityType(String entityType) {
        return findByEntityType(entityType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByAction(String action) {
        return repository.findByAction(action);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByIpAddress(String ipAddress) {
        return repository.findByIpAddress(ipAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByUserAndDateRange(Integer userId, LocalDateTime startDate, LocalDateTime endDate) {
        return findByUserId(userId).stream()
                .filter(log -> log.getActionTimestamp().isAfter(startDate) &&
                        log.getActionTimestamp().isBefore(endDate))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByEntityAndDateRange(String entityType, Integer entityId, LocalDateTime startDate, LocalDateTime endDate) {
        return findByEntityTypeAndEntityId(entityType, entityId).stream()
                .filter(log -> log.getActionTimestamp().isAfter(startDate) &&
                        log.getActionTimestamp().isBefore(endDate))
                .toList();
    }

    // Advanced queries
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getRecentActivityByEntityType(String entityType, int limit) {
        return findByEntityType(entityType).stream()
                .sorted((a, b) -> b.getActionTimestamp().compareTo(a.getActionTimestamp()))
                .limit(limit)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByUserAgent(String userAgent) {
        return repository.findAll().stream()
                .filter(log -> userAgent.equals(log.getUserAgent()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByUserAgentContaining(String userAgentPattern) {
        return repository.findAll().stream()
                .filter(log -> log.getUserAgent() != null &&
                        log.getUserAgent().contains(userAgentPattern))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByIpAddressAndDateRange(String ipAddress, LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByIpAddress(ipAddress).stream()
                .filter(log -> log.getActionTimestamp().isAfter(startDate) &&
                        log.getActionTimestamp().isBefore(endDate))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByActionAndDateRange(String action, LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByAction(action).stream()
                .filter(log -> log.getActionTimestamp().isAfter(startDate) &&
                        log.getActionTimestamp().isBefore(endDate))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByEntityTypeAndAction(String entityType, String action) {
        return findByActionAndEntityType(action, entityType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByUserAndAction(Integer userId, String action) {
        return findByUserId(userId).stream()
                .filter(log -> action.equals(log.getAction()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByUserAndEntityType(Integer userId, String entityType) {
        return findByUserId(userId).stream()
                .filter(log -> entityType.equals(log.getEntityType()))
                .toList();
    }

    // Bulk operations
    @Override
    @Transactional
    public void bulkCreateAuditLogs(List<AuditLog> auditLogs) {
        auditLogs.forEach(this::validateAuditLog);
        repository.saveAll(auditLogs);
    }

    @Override
    @Transactional
    public void bulkDeleteAuditLogs(List<Integer> auditLogIds) {
        repository.deleteAllById(auditLogIds);
    }

    @Override
    @Transactional
    public void bulkDeleteAuditLogsByEntityType(String entityType) {
        List<AuditLog> logsToDelete = findByEntityType(entityType);
        repository.deleteAll(logsToDelete);
    }

    @Override
    @Transactional
    public void bulkDeleteAuditLogsByUser(Integer userId) {
        List<AuditLog> logsToDelete = findByUserId(userId);
        repository.deleteAll(logsToDelete);
    }

    @Override
    @Transactional
    public void bulkDeleteAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<AuditLog> logsToDelete = repository.findByActionTimestampBetween(startDate, endDate);
        repository.deleteAll(logsToDelete);
    }

    @Override
    @Transactional
    public void bulkDeleteAuditLogsByAction(String action) {
        List<AuditLog> logsToDelete = repository.findByAction(action);
        repository.deleteAll(logsToDelete);
    }

    // Check operations
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEntityTypeAndEntityId(String entityType, Integer entityId) {
        return !repository.findByEntityTypeAndEntityId(entityType, entityId).isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUser(Integer userId) {
        return !findByUserId(userId).isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByAction(String action) {
        return !repository.findByAction(action).isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIpAddress(String ipAddress) {
        return !repository.findByIpAddress(ipAddress).isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByEntityType(String entityType) {
        return findByEntityType(entityType).size();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByUser(Integer userId) {
        return findByUserId(userId).size();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByAction(String action) {
        return repository.findByAction(action).size();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByActionTimestampBetween(startDate, endDate).size();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByIpAddress(String ipAddress) {
        return repository.findByIpAddress(ipAddress).size();
    }

    // Audit trail operations
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditTrailForEntity(String entityType, Integer entityId) {
        return repository.findByEntityTypeAndEntityId(entityType, entityId).stream()
                .sorted((a, b) -> a.getActionTimestamp().compareTo(b.getActionTimestamp()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditTrailForUser(Integer userId) {
        return findByUserId(userId).stream()
                .sorted((a, b) -> a.getActionTimestamp().compareTo(b.getActionTimestamp()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditTrailForEntityAndUser(String entityType, Integer entityId, Integer userId) {
        return getAuditTrailForEntity(entityType, entityId).stream()
                .filter(log -> log.getUser() != null && userId.equals(log.getUser().getId()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditTrailForEntityAndAction(String entityType, Integer entityId, String action) {
        return getAuditTrailForEntity(entityType, entityId).stream()
                .filter(log -> action.equals(log.getAction()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditTrailForUserAndAction(Integer userId, String action) {
        return getAuditTrailForUser(userId).stream()
                .filter(log -> action.equals(log.getAction()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditTrailForEntityAndDateRange(String entityType, Integer entityId,
            LocalDateTime startDate, LocalDateTime endDate) {
        return getAuditTrailForEntity(entityType, entityId).stream()
                .filter(log -> log.getActionTimestamp().isAfter(startDate) && log.getActionTimestamp().isBefore(endDate))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditTrailForUserAndDateRange(Integer userId, LocalDateTime startDate,
            LocalDateTime endDate) {
        return getAuditTrailForUser(userId).stream()
                .filter(log -> log.getActionTimestamp().isAfter(startDate) && log.getActionTimestamp().isBefore(endDate))
                .toList();
    }

    // Statistics and analytics
    @Override
    @Transactional(readOnly = true)
    public long getTotalAuditLogs() {
        return repository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalAuditLogsByEntityType(String entityType) {
        return countByEntityType(entityType);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalAuditLogsByUser(Integer userId) {
        return countByUser(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalAuditLogsByAction(String action) {
        return countByAction(action);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return countByDateRange(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalAuditLogsByIpAddress(String ipAddress) {
        return countByIpAddress(ipAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getTopUsersByActivity(int limit) {
        return repository.findAll().stream()
                .filter(log -> log.getUser() != null)
                .collect(Collectors.groupingBy(log -> log.getUser().getId(), Collectors.counting()))
                .entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .map(entry -> repository.findAll().stream()
                        .filter(log -> log.getUser() != null && entry.getKey().equals(log.getUser().getId()))
                        .findFirst().orElse(null))
                .filter(log -> log != null)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getTopEntityTypesByActivity(int limit) {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(AuditLog::getEntityType, Collectors.counting()))
                .entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .map(entry -> repository.findAll().stream()
                        .filter(log -> entry.getKey().equals(log.getEntityType()))
                        .findFirst().orElse(null))
                .filter(log -> log != null)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getTopActionsByActivity(int limit) {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(AuditLog::getAction, Collectors.counting()))
                .entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .map(entry -> repository.findAll().stream()
                        .filter(log -> entry.getKey().equals(log.getAction()))
                        .findFirst().orElse(null))
                .filter(log -> log != null)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getTopIpAddressesByActivity(int limit) {
        return repository.findAll().stream()
                .filter(log -> log.getIpAddress() != null)
                .collect(Collectors.groupingBy(AuditLog::getIpAddress, Collectors.counting()))
                .entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .map(entry -> repository.findAll().stream()
                        .filter(log -> entry.getKey().equals(log.getIpAddress()))
                        .findFirst().orElse(null))
                .filter(log -> log != null)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByMonth() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(log -> log.getActionTimestamp().getMonth(), Collectors.toList()))
                .values().stream()
                .flatMap(List::stream)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByDayOfWeek() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(log -> log.getActionTimestamp().getDayOfWeek(), Collectors.toList()))
                .values().stream()
                .flatMap(List::stream)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByHour() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(log -> log.getActionTimestamp().getHour(), Collectors.toList()))
                .values().stream()
                .flatMap(List::stream)
                .toList();
    }

    // Security and monitoring operations
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getSuspiciousActivity(String ipAddress) {
        return repository.findByIpAddress(ipAddress).stream()
                .filter(log -> {
                    // Consider suspicious: multiple failed attempts, unusual hours, etc.
                    LocalDateTime now = LocalDateTime.now();
                    return log.getActionTimestamp().isAfter(now.minusHours(24)) &&
                            (log.getAction().contains("FAILED") ||
                                    log.getAction().contains("ERROR") ||
                                    log.getActionTimestamp().getHour() < 6 ||
                                    log.getActionTimestamp().getHour() > 22);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByMultipleIpAddresses(List<String> ipAddresses) {
        return repository.findAll().stream()
                .filter(log -> log.getIpAddress() != null && ipAddresses.contains(log.getIpAddress()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByUserAgentPattern(String pattern) {
        return getActivityByUserAgentContaining(pattern);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getFailedLoginAttempts() {
        return repository.findAll().stream()
                .filter(log -> log.getAction() != null &&
                        (log.getAction().contains("LOGIN_FAILED") ||
                                log.getAction().contains("AUTHENTICATION_FAILED") ||
                                log.getAction().contains("FAILED_LOGIN")))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getSuccessfulLoginAttempts() {
        return repository.findAll().stream()
                .filter(log -> log.getAction() != null &&
                        (log.getAction().contains("LOGIN_SUCCESS") ||
                                log.getAction().contains("AUTHENTICATION_SUCCESS") ||
                                log.getAction().contains("SUCCESSFUL_LOGIN")))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getDataModificationActivity() {
        return repository.findAll().stream()
                .filter(log -> log.getAction() != null &&
                        (log.getAction().contains("UPDATE") ||
                                log.getAction().contains("MODIFY") ||
                                log.getAction().contains("EDIT")))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getDataAccessActivity() {
        return repository.findAll().stream()
                .filter(log -> log.getAction() != null &&
                        (log.getAction().contains("READ") ||
                                log.getAction().contains("VIEW") ||
                                log.getAction().contains("ACCESS")))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getDataDeletionActivity() {
        return repository.findAll().stream()
                .filter(log -> log.getAction() != null &&
                        (log.getAction().contains("DELETE") ||
                                log.getAction().contains("REMOVE") ||
                                log.getAction().contains("DESTROY")))
                .toList();
    }

    // Data integrity operations
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsWithMissingUser() {
        return repository.findAll().stream()
                .filter(log -> log.getUser() == null)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsWithMissingIpAddress() {
        return repository.findAll().stream()
                .filter(log -> log.getIpAddress() == null || log.getIpAddress().trim().isEmpty())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsWithInvalidJsonData() {
        return repository.findAll().stream()
                .filter(log -> {
                    try {
                        if (log.getOldValues() != null && !log.getOldValues().trim().isEmpty()) {
                            // Try to parse as JSON
                            new com.fasterxml.jackson.databind.ObjectMapper().readTree(log.getOldValues());
                        }
                        if (log.getNewValues() != null && !log.getNewValues().trim().isEmpty()) {
                            // Try to parse as JSON
                            new com.fasterxml.jackson.databind.ObjectMapper().readTree(log.getNewValues());
                        }
                        return false; // Valid JSON
                    } catch (Exception e) {
                        return true; // Invalid JSON
                    }
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsWithEmptyOldValues() {
        return repository.findAll().stream()
                .filter(log -> log.getOldValues() == null || log.getOldValues().trim().isEmpty())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsWithEmptyNewValues() {
        return repository.findAll().stream()
                .filter(log -> log.getNewValues() == null || log.getNewValues().trim().isEmpty())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsWithBothEmptyValues() {
        return repository.findAll().stream()
                .filter(log -> (log.getOldValues() == null || log.getOldValues().trim().isEmpty()) &&
                        (log.getNewValues() == null || log.getNewValues().trim().isEmpty()))
                .toList();
    }

    // Utility operations
    @Override
    @Transactional
    public void cleanupOldAuditLogs(int daysToKeep) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        List<AuditLog> oldLogs = repository.findAll().stream()
                .filter(log -> log.getActionTimestamp().isBefore(cutoffDate))
                .toList();
        repository.deleteAll(oldLogs);
    }

    @Override
    @Transactional
    public void archiveAuditLogs(LocalDateTime beforeDate) {
        List<AuditLog> logsToArchive = repository.findAll().stream()
                .filter(log -> log.getActionTimestamp().isBefore(beforeDate))
                .toList();
        // In a real implementation, you would move these to an archive table
        // For now, we'll just delete them
        repository.deleteAll(logsToArchive);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> searchAuditLogsByKeyword(String keyword) {
        return repository.findAll().stream()
                .filter(log -> {
                    String searchText = (log.getEntityType() + " " +
                            log.getAction() + " " +
                            (log.getOldValues() != null ? log.getOldValues() : "") + " " +
                            (log.getNewValues() != null ? log.getNewValues() : "") + " " +
                            (log.getUserAgent() != null ? log.getUserAgent() : "")).toLowerCase();
                    return searchText.contains(keyword.toLowerCase());
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> searchAuditLogsByUserAgent(String userAgent) {
        return getActivityByUserAgent(userAgent);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuditLog> findLatestAuditLogByEntity(String entityType, Integer entityId) {
        return repository.findByEntityTypeAndEntityId(entityType, entityId).stream()
                .max((a, b) -> a.getActionTimestamp().compareTo(b.getActionTimestamp()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsWithJsonData() {
        return repository.findAll().stream()
                .filter(log -> (log.getOldValues() != null && !log.getOldValues().trim().isEmpty()) ||
                        (log.getNewValues() != null && !log.getNewValues().trim().isEmpty()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsWithoutJsonData() {
        return repository.findAll().stream()
                .filter(log -> (log.getOldValues() == null || log.getOldValues().trim().isEmpty()) &&
                        (log.getNewValues() == null || log.getNewValues().trim().isEmpty()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByJsonField(String fieldName, String fieldValue) {
        return repository.findAll().stream()
                .filter(log -> {
                    try {
                        if (log.getOldValues() != null && !log.getOldValues().trim().isEmpty()) {
                            com.fasterxml.jackson.databind.JsonNode oldNode = new com.fasterxml.jackson.databind.ObjectMapper()
                                    .readTree(log.getOldValues());
                            if (oldNode.has(fieldName) && fieldValue.equals(oldNode.get(fieldName).asText())) {
                                return true;
                            }
                        }
                        if (log.getNewValues() != null && !log.getNewValues().trim().isEmpty()) {
                            com.fasterxml.jackson.databind.JsonNode newNode = new com.fasterxml.jackson.databind.ObjectMapper()
                                    .readTree(log.getNewValues());
                            if (newNode.has(fieldName) && fieldValue.equals(newNode.get(fieldName).asText())) {
                                return true;
                            }
                        }
                    } catch (Exception e) {
                        // Invalid JSON, skip
                    }
                    return false;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByJsonFieldContaining(String fieldName, String fieldValue) {
        return repository.findAll().stream()
                .filter(log -> {
                    try {
                        if (log.getOldValues() != null && !log.getOldValues().trim().isEmpty()) {
                            com.fasterxml.jackson.databind.JsonNode oldNode = new com.fasterxml.jackson.databind.ObjectMapper()
                                    .readTree(log.getOldValues());
                            if (oldNode.has(fieldName) && oldNode.get(fieldName).asText().contains(fieldValue)) {
                                return true;
                            }
                        }
                        if (log.getNewValues() != null && !log.getNewValues().trim().isEmpty()) {
                            com.fasterxml.jackson.databind.JsonNode newNode = new com.fasterxml.jackson.databind.ObjectMapper()
                                    .readTree(log.getNewValues());
                            if (newNode.has(fieldName) && newNode.get(fieldName).asText().contains(fieldValue)) {
                                return true;
                            }
                        }
                    } catch (Exception e) {
                        // Invalid JSON, skip
                    }
                    return false;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByJsonFieldExists(String fieldName) {
        return repository.findAll().stream()
                .filter(log -> {
                    try {
                        if (log.getOldValues() != null && !log.getOldValues().trim().isEmpty()) {
                            com.fasterxml.jackson.databind.JsonNode oldNode = new com.fasterxml.jackson.databind.ObjectMapper()
                                    .readTree(log.getOldValues());
                            if (oldNode.has(fieldName)) {
                                return true;
                            }
                        }
                        if (log.getNewValues() != null && !log.getNewValues().trim().isEmpty()) {
                            com.fasterxml.jackson.databind.JsonNode newNode = new com.fasterxml.jackson.databind.ObjectMapper()
                                    .readTree(log.getNewValues());
                            if (newNode.has(fieldName)) {
                                return true;
                            }
                        }
                    } catch (Exception e) {
                        // Invalid JSON, skip
                    }
                    return false;
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByJsonFieldNotExists(String fieldName) {
        return repository.findAll().stream()
                .filter(log -> {
                    try {
                        boolean hasFieldInOld = false;
                        boolean hasFieldInNew = false;

                        if (log.getOldValues() != null && !log.getOldValues().trim().isEmpty()) {
                            com.fasterxml.jackson.databind.JsonNode oldNode = new com.fasterxml.jackson.databind.ObjectMapper()
                                    .readTree(log.getOldValues());
                            hasFieldInOld = oldNode.has(fieldName);
                        }

                        if (log.getNewValues() != null && !log.getNewValues().trim().isEmpty()) {
                            com.fasterxml.jackson.databind.JsonNode newNode = new com.fasterxml.jackson.databind.ObjectMapper()
                                    .readTree(log.getNewValues());
                            hasFieldInNew = newNode.has(fieldName);
                        }

                        return !hasFieldInOld && !hasFieldInNew;
                    } catch (Exception e) {
                        // Invalid JSON, skip
                        return false;
                    }
                })
                .toList();
    }

    // Private helper methods
    private AuditLog findAuditLogOrFail(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Audit log not found with id: " + id));
    }

    private void validateAuditLog(AuditLog auditLog) {
        if (auditLog.getEntityType() == null || auditLog.getEntityType().trim().isEmpty()) {
            throw new IllegalArgumentException("Entity type cannot be null or empty");
        }
        if (auditLog.getEntityId() == null) {
            throw new IllegalArgumentException("Entity ID cannot be null");
        }
        if (auditLog.getAction() == null || auditLog.getAction().trim().isEmpty()) {
            throw new IllegalArgumentException("Action cannot be null or empty");
        }
        if (auditLog.getActionTimestamp() == null) {
            auditLog.setActionTimestamp(LocalDateTime.now());
        }
    }

    private void updateAuditLogFields(AuditLog existingAuditLog, AuditLog auditLog) {
        if (auditLog.getEntityType() != null) {
            existingAuditLog.setEntityType(auditLog.getEntityType());
        }
        if (auditLog.getEntityId() != null) {
            existingAuditLog.setEntityId(auditLog.getEntityId());
        }
        if (auditLog.getAction() != null) {
            existingAuditLog.setAction(auditLog.getAction());
        }
        if (auditLog.getUser() != null) {
            existingAuditLog.setUser(auditLog.getUser());
        }
        if (auditLog.getActionTimestamp() != null) {
            existingAuditLog.setActionTimestamp(auditLog.getActionTimestamp());
        }
        if (auditLog.getOldValues() != null) {
            existingAuditLog.setOldValues(auditLog.getOldValues());
        }
        if (auditLog.getNewValues() != null) {
            existingAuditLog.setNewValues(auditLog.getNewValues());
        }
        if (auditLog.getIpAddress() != null) {
            existingAuditLog.setIpAddress(auditLog.getIpAddress());
        }
        if (auditLog.getUserAgent() != null) {
            existingAuditLog.setUserAgent(auditLog.getUserAgent());
        }
    }
}
