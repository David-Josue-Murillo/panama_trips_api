package com.app.panama_trips.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.AuditLog;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.entity.enums.AuditAction;
import com.app.panama_trips.persistence.repository.AuditLogRepository;
import com.app.panama_trips.service.interfaces.IAuditLogService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogService implements IAuditLogService {

    private final AuditLogRepository repository;
    private final ObjectMapper objectMapper;

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
        return repository.findByUser_Id(userId.longValue());
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
        return repository.findByEntityType(entityType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByEntityId(Integer entityId) {
        return repository.findByEntityId(entityId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByUserAndAction(UserEntity user, String action) {
        return repository.findByUser_IdAndAction(user.getId(), action);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByUserAndEntityType(UserEntity user, String entityType) {
        return repository.findByUser_IdAndEntityType(user.getId(), entityType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByActionAndEntityType(String action, String entityType) {
        return repository.findByActionAndEntityType(action, entityType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByTimestampAfter(LocalDateTime timestamp) {
        return repository.findByActionTimestampAfter(timestamp);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findByTimestampBefore(LocalDateTime timestamp) {
        return repository.findByActionTimestampBefore(timestamp);
    }

    // Business logic operations
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getRecentActivity(int limit) {
        return repository.findRecentActivity(PageRequest.of(0, limit));
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
    public List<AuditLog> getActivityByUserAndDateRange(Integer userId, LocalDateTime startDate,
            LocalDateTime endDate) {
        return repository.findByUser_IdAndActionTimestampBetween(userId.longValue(), startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByEntityAndDateRange(String entityType, Integer entityId, LocalDateTime startDate,
            LocalDateTime endDate) {
        return repository.findByEntityTypeAndEntityIdAndActionTimestampBetween(entityType, entityId, startDate, endDate);
    }

    // Advanced queries
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getRecentActivityByEntityType(String entityType, int limit) {
        return repository.findRecentActivityByEntityType(entityType, LocalDateTime.MIN).stream()
                .limit(limit)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByUserAgent(String userAgent) {
        return repository.findByUserAgent(userAgent);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByUserAgentContaining(String userAgentPattern) {
        return repository.findByUserAgentContaining(userAgentPattern);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByIpAddressAndDateRange(String ipAddress, LocalDateTime startDate,
            LocalDateTime endDate) {
        return repository.findByIpAddressAndActionTimestampBetween(ipAddress, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByActionAndDateRange(String action, LocalDateTime startDate,
            LocalDateTime endDate) {
        return repository.findByActionAndActionTimestampBetween(action, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByEntityTypeAndAction(String entityType, String action) {
        return repository.findByActionAndEntityType(action, entityType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByUserAndAction(Integer userId, String action) {
        return repository.findByUser_IdAndAction(userId.longValue(), action);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByUserAndEntityType(Integer userId, String entityType) {
        return repository.findByUser_IdAndEntityType(userId.longValue(), entityType);
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
        repository.deleteByEntityType(entityType);
    }

    @Override
    @Transactional
    public void bulkDeleteAuditLogsByUser(Integer userId) {
        repository.deleteByUser_Id(userId.longValue());
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
        repository.deleteByAction(action);
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
        return repository.existsByEntityTypeAndEntityId(entityType, entityId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUser(Integer userId) {
        return repository.existsByUser_Id(userId.longValue());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByAction(String action) {
        return repository.existsByAction(action);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIpAddress(String ipAddress) {
        return repository.existsByIpAddress(ipAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByEntityType(String entityType) {
        return repository.countByEntityType(entityType);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByUser(Integer userId) {
        return repository.countByUser_Id(userId.longValue());
    }

    @Override
    @Transactional(readOnly = true)
    public long countByAction(String action) {
        return repository.countByAction(action);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.countByActionTimestampBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByIpAddress(String ipAddress) {
        return repository.countByIpAddress(ipAddress);
    }

    // Audit trail operations
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditTrailForEntity(String entityType, Integer entityId) {
        return repository.findByEntityTypeAndEntityIdOrderByActionTimestampAsc(entityType, entityId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditTrailForUser(Integer userId) {
        return repository.findByUser_IdOrderByActionTimestampAsc(userId.longValue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditTrailForEntityAndUser(String entityType, Integer entityId, Integer userId) {
        return getAuditTrailForEntity(entityType, entityId).stream()
                .filter(log -> log.getUser() != null && userId.longValue() == log.getUser().getId())
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
        return repository.findByUser_IdAndAction(userId.longValue(), action);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditTrailForEntityAndDateRange(String entityType, Integer entityId,
            LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByEntityTypeAndEntityIdAndActionTimestampBetween(entityType, entityId, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditTrailForUserAndDateRange(Integer userId, LocalDateTime startDate,
            LocalDateTime endDate) {
        return repository.findByUser_IdAndActionTimestampBetween(userId.longValue(), startDate, endDate);
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
        return repository.countByEntityType(entityType);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalAuditLogsByUser(Integer userId) {
        return repository.countByUser_Id(userId.longValue());
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalAuditLogsByAction(String action) {
        return repository.countByAction(action);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.countByActionTimestampBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalAuditLogsByIpAddress(String ipAddress) {
        return repository.countByIpAddress(ipAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getTopUsersByActivity(int limit) {
        List<AuditLog> allLogs = repository.findAll();
        return allLogs.stream()
                .filter(log -> log.getUser() != null)
                .collect(Collectors.groupingBy(log -> log.getUser().getId(), Collectors.counting()))
                .entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .map(entry -> allLogs.stream()
                        .filter(log -> log.getUser() != null && entry.getKey().equals(log.getUser().getId()))
                        .findFirst().orElse(null))
                .filter(log -> log != null)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getTopEntityTypesByActivity(int limit) {
        List<AuditLog> allLogs = repository.findAll();
        return allLogs.stream()
                .collect(Collectors.groupingBy(AuditLog::getEntityType, Collectors.counting()))
                .entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .map(entry -> allLogs.stream()
                        .filter(log -> entry.getKey().equals(log.getEntityType()))
                        .findFirst().orElse(null))
                .filter(log -> log != null)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getTopActionsByActivity(int limit) {
        List<AuditLog> allLogs = repository.findAll();
        return allLogs.stream()
                .collect(Collectors.groupingBy(AuditLog::getAction, Collectors.counting()))
                .entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .map(entry -> allLogs.stream()
                        .filter(log -> entry.getKey().equals(log.getAction()))
                        .findFirst().orElse(null))
                .filter(log -> log != null)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getTopIpAddressesByActivity(int limit) {
        List<AuditLog> allLogs = repository.findAll();
        return allLogs.stream()
                .filter(log -> log.getIpAddress() != null)
                .collect(Collectors.groupingBy(AuditLog::getIpAddress, Collectors.counting()))
                .entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .map(entry -> allLogs.stream()
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
                    LocalDateTime now = LocalDateTime.now();
                    return log.getActionTimestamp().isAfter(now.minusHours(24)) &&
                            (AuditAction.getSuspiciousActions().stream()
                                    .anyMatch(action -> log.getAction().contains(action)) ||
                                    log.getActionTimestamp().getHour() < 6 ||
                                    log.getActionTimestamp().getHour() > 22);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByMultipleIpAddresses(List<String> ipAddresses) {
        return repository.findByIpAddressIn(ipAddresses);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getActivityByUserAgentPattern(String pattern) {
        return repository.findByUserAgentContaining(pattern);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getFailedLoginAttempts() {
        return repository.findByActionIn(AuditAction.getFailedLoginActions());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getSuccessfulLoginAttempts() {
        return repository.findByActionIn(AuditAction.getSuccessfulLoginActions());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getDataModificationActivity() {
        return repository.findByActionIn(AuditAction.getDataModificationActions());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getDataAccessActivity() {
        return repository.findByActionIn(AuditAction.getDataAccessActions());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getDataDeletionActivity() {
        return repository.findByActionIn(AuditAction.getDataDeletionActions());
    }

    // Data integrity operations
    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsWithMissingUser() {
        return repository.findByUserIsNull();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsWithMissingIpAddress() {
        return repository.findByIpAddressIsNullOrEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsWithInvalidJsonData() {
        return repository.findAll().stream()
                .filter(auditLog -> {
                    try {
                        if (auditLog.getOldValues() != null && !auditLog.getOldValues().trim().isEmpty()) {
                            objectMapper.readTree(auditLog.getOldValues());
                        }
                        if (auditLog.getNewValues() != null && !auditLog.getNewValues().trim().isEmpty()) {
                            objectMapper.readTree(auditLog.getNewValues());
                        }
                        return false;
                    } catch (Exception e) {
                        log.warn("Invalid JSON data in audit log id {}: {}", auditLog.getId(), e.getMessage());
                        return true;
                    }
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsWithEmptyOldValues() {
        return repository.findByOldValuesIsNullOrEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsWithEmptyNewValues() {
        return repository.findByNewValuesIsNullOrEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsWithBothEmptyValues() {
        return repository.findByBothValuesNullOrEmpty();
    }

    // Utility operations
    @Override
    @Transactional
    public void cleanupOldAuditLogs(int daysToKeep) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        repository.deleteByActionTimestampBefore(cutoffDate);
    }

    @Override
    @Transactional
    public void deleteOldAuditLogs(LocalDateTime beforeDate) {
        repository.deleteByActionTimestampBefore(beforeDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> searchAuditLogsByKeyword(String keyword) {
        return repository.searchByKeyword(keyword);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> searchAuditLogsByUserAgent(String userAgent) {
        return repository.findByUserAgent(userAgent);
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
        return repository.findByOldValuesOrNewValuesNotEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsWithoutJsonData() {
        return repository.findByOldValuesAndNewValuesNullOrEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByJsonField(String fieldName, String fieldValue) {
        return repository.findAll().stream()
                .filter(log -> hasJsonFieldWithValue(log, fieldName, fieldValue, false))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByJsonFieldContaining(String fieldName, String fieldValue) {
        return repository.findAll().stream()
                .filter(log -> hasJsonFieldWithValue(log, fieldName, fieldValue, true))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByJsonFieldExists(String fieldName) {
        return repository.findAll().stream()
                .filter(log -> hasJsonField(log, fieldName))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByJsonFieldNotExists(String fieldName) {
        return repository.findAll().stream()
                .filter(log -> !hasJsonField(log, fieldName))
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

    private boolean hasJsonField(AuditLog auditLog, String fieldName) {
        try {
            if (auditLog.getOldValues() != null && !auditLog.getOldValues().trim().isEmpty()) {
                JsonNode oldNode = objectMapper.readTree(auditLog.getOldValues());
                if (oldNode.has(fieldName)) {
                    return true;
                }
            }
            if (auditLog.getNewValues() != null && !auditLog.getNewValues().trim().isEmpty()) {
                JsonNode newNode = objectMapper.readTree(auditLog.getNewValues());
                if (newNode.has(fieldName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.warn("Invalid JSON in audit log id {}: {}", auditLog.getId(), e.getMessage());
        }
        return false;
    }

    private boolean hasJsonFieldWithValue(AuditLog auditLog, String fieldName, String fieldValue, boolean contains) {
        try {
            if (auditLog.getOldValues() != null && !auditLog.getOldValues().trim().isEmpty()) {
                JsonNode oldNode = objectMapper.readTree(auditLog.getOldValues());
                if (oldNode.has(fieldName) && matchesValue(oldNode.get(fieldName).asText(), fieldValue, contains)) {
                    return true;
                }
            }
            if (auditLog.getNewValues() != null && !auditLog.getNewValues().trim().isEmpty()) {
                JsonNode newNode = objectMapper.readTree(auditLog.getNewValues());
                if (newNode.has(fieldName) && matchesValue(newNode.get(fieldName).asText(), fieldValue, contains)) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.warn("Invalid JSON in audit log id {}: {}", auditLog.getId(), e.getMessage());
        }
        return false;
    }

    private boolean matchesValue(String actual, String expected, boolean contains) {
        return contains ? actual.contains(expected) : expected.equals(actual);
    }
}
