package com.app.panama_trips.service.implementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Override
    public void bulkCreateAuditLogs(List<AuditLog> auditLogs) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bulkCreateAuditLogs'");
    }

    @Override
    public void bulkDeleteAuditLogs(List<Integer> auditLogIds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bulkDeleteAuditLogs'");
    }

    @Override
    public void bulkDeleteAuditLogsByEntityType(String entityType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bulkDeleteAuditLogsByEntityType'");
    }

    @Override
    public void bulkDeleteAuditLogsByUser(Integer userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bulkDeleteAuditLogsByUser'");
    }

    @Override
    public void bulkDeleteAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bulkDeleteAuditLogsByDateRange'");
    }

    @Override
    public void bulkDeleteAuditLogsByAction(String action) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bulkDeleteAuditLogsByAction'");
    }

    @Override
    public boolean existsById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsById'");
    }

    @Override
    public boolean existsByEntityTypeAndEntityId(String entityType, Integer entityId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsByEntityTypeAndEntityId'");
    }

    @Override
    public boolean existsByUser(Integer userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsByUser'");
    }

    @Override
    public boolean existsByAction(String action) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsByAction'");
    }

    @Override
    public boolean existsByIpAddress(String ipAddress) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsByIpAddress'");
    }

    @Override
    public long countByEntityType(String entityType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'countByEntityType'");
    }

    @Override
    public long countByUser(Integer userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'countByUser'");
    }

    @Override
    public long countByAction(String action) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'countByAction'");
    }

    @Override
    public long countByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'countByDateRange'");
    }

    @Override
    public long countByIpAddress(String ipAddress) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'countByIpAddress'");
    }

    @Override
    public List<AuditLog> getAuditTrailForEntity(String entityType, Integer entityId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuditTrailForEntity'");
    }

    @Override
    public List<AuditLog> getAuditTrailForUser(Integer userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuditTrailForUser'");
    }

    @Override
    public List<AuditLog> getAuditTrailForEntityAndUser(String entityType, Integer entityId, Integer userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuditTrailForEntityAndUser'");
    }

    @Override
    public List<AuditLog> getAuditTrailForEntityAndAction(String entityType, Integer entityId, String action) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuditTrailForEntityAndAction'");
    }

    @Override
    public List<AuditLog> getAuditTrailForUserAndAction(Integer userId, String action) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuditTrailForUserAndAction'");
    }

    @Override
    public List<AuditLog> getAuditTrailForEntityAndDateRange(String entityType, Integer entityId,
            LocalDateTime startDate, LocalDateTime endDate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuditTrailForEntityAndDateRange'");
    }

    @Override
    public List<AuditLog> getAuditTrailForUserAndDateRange(Integer userId, LocalDateTime startDate,
            LocalDateTime endDate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuditTrailForUserAndDateRange'");
    }

    @Override
    public long getTotalAuditLogs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalAuditLogs'");
    }

    @Override
    public long getTotalAuditLogsByEntityType(String entityType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalAuditLogsByEntityType'");
    }

    @Override
    public long getTotalAuditLogsByUser(Integer userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalAuditLogsByUser'");
    }

    @Override
    public long getTotalAuditLogsByAction(String action) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalAuditLogsByAction'");
    }

    @Override
    public long getTotalAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalAuditLogsByDateRange'");
    }

    @Override
    public long getTotalAuditLogsByIpAddress(String ipAddress) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalAuditLogsByIpAddress'");
    }

    @Override
    public List<AuditLog> getTopUsersByActivity(int limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTopUsersByActivity'");
    }

    @Override
    public List<AuditLog> getTopEntityTypesByActivity(int limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTopEntityTypesByActivity'");
    }

    @Override
    public List<AuditLog> getTopActionsByActivity(int limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTopActionsByActivity'");
    }

    @Override
    public List<AuditLog> getTopIpAddressesByActivity(int limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTopIpAddressesByActivity'");
    }

    @Override
    public List<AuditLog> getActivityByMonth() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getActivityByMonth'");
    }

    @Override
    public List<AuditLog> getActivityByDayOfWeek() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getActivityByDayOfWeek'");
    }

    @Override
    public List<AuditLog> getActivityByHour() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getActivityByHour'");
    }

    @Override
    public List<AuditLog> getSuspiciousActivity(String ipAddress) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSuspiciousActivity'");
    }

    @Override
    public List<AuditLog> getActivityByMultipleIpAddresses(List<String> ipAddresses) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getActivityByMultipleIpAddresses'");
    }

    @Override
    public List<AuditLog> getActivityByUserAgentPattern(String pattern) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getActivityByUserAgentPattern'");
    }

    @Override
    public List<AuditLog> getFailedLoginAttempts() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFailedLoginAttempts'");
    }

    @Override
    public List<AuditLog> getSuccessfulLoginAttempts() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSuccessfulLoginAttempts'");
    }

    @Override
    public List<AuditLog> getDataModificationActivity() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDataModificationActivity'");
    }

    @Override
    public List<AuditLog> getDataAccessActivity() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDataAccessActivity'");
    }

    @Override
    public List<AuditLog> getDataDeletionActivity() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDataDeletionActivity'");
    }

    @Override
    public List<AuditLog> getAuditLogsWithMissingUser() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuditLogsWithMissingUser'");
    }

    @Override
    public List<AuditLog> getAuditLogsWithMissingIpAddress() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuditLogsWithMissingIpAddress'");
    }

    @Override
    public List<AuditLog> getAuditLogsWithInvalidJsonData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuditLogsWithInvalidJsonData'");
    }

    @Override
    public List<AuditLog> getAuditLogsWithEmptyOldValues() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuditLogsWithEmptyOldValues'");
    }

    @Override
    public List<AuditLog> getAuditLogsWithEmptyNewValues() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuditLogsWithEmptyNewValues'");
    }

    @Override
    public List<AuditLog> getAuditLogsWithBothEmptyValues() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuditLogsWithBothEmptyValues'");
    }

    @Override
    public void cleanupOldAuditLogs(int daysToKeep) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cleanupOldAuditLogs'");
    }

    @Override
    public void archiveAuditLogs(LocalDateTime beforeDate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'archiveAuditLogs'");
    }

    @Override
    public List<AuditLog> searchAuditLogsByKeyword(String keyword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchAuditLogsByKeyword'");
    }

    @Override
    public List<AuditLog> searchAuditLogsByUserAgent(String userAgent) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchAuditLogsByUserAgent'");
    }

    @Override
    public Optional<AuditLog> findLatestAuditLogByEntity(String entityType, Integer entityId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findLatestAuditLogByEntity'");
    }

    @Override
    public List<AuditLog> getAuditLogsWithJsonData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuditLogsWithJsonData'");
    }

    @Override
    public List<AuditLog> getAuditLogsWithoutJsonData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuditLogsWithoutJsonData'");
    }

    @Override
    public List<AuditLog> getAuditLogsByJsonField(String fieldName, String fieldValue) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuditLogsByJsonField'");
    }

    @Override
    public List<AuditLog> getAuditLogsByJsonFieldContaining(String fieldName, String fieldValue) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuditLogsByJsonFieldContaining'");
    }

    @Override
    public List<AuditLog> getAuditLogsByJsonFieldExists(String fieldName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuditLogsByJsonFieldExists'");
    }

    @Override
    public List<AuditLog> getAuditLogsByJsonFieldNotExists(String fieldName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAuditLogsByJsonFieldNotExists'");
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
