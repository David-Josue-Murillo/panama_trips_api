package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.AuditLog;
import com.app.panama_trips.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IAuditLogService {
    // CRUD operations
    Page<AuditLog> getAllAuditLogs(Pageable pageable);
    AuditLog getAuditLogById(Integer id);
    AuditLog saveAuditLog(AuditLog auditLog);
    AuditLog updateAuditLog(Integer id, AuditLog auditLog);
    void deleteAuditLog(Integer id);

    // Find operations by entity relationships
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, Integer entityId);
    List<AuditLog> findByUser(UserEntity user);
    List<AuditLog> findByUserId(Integer userId);
    List<AuditLog> findByAction(String action);
    List<AuditLog> findByActionTimestampBetween(LocalDateTime start, LocalDateTime end);
    List<AuditLog> findByIpAddress(String ipAddress);

    // Specialized queries from repository
    List<AuditLog> findRecentActivityByEntityType(String entityType, LocalDateTime since);
    List<AuditLog> findByEntityType(String entityType);
    List<AuditLog> findByEntityId(Integer entityId);
    List<AuditLog> findByUserAndAction(UserEntity user, String action);
    List<AuditLog> findByUserAndEntityType(UserEntity user, String entityType);
    List<AuditLog> findByActionAndEntityType(String action, String entityType);
    List<AuditLog> findByTimestampAfter(LocalDateTime timestamp);
    List<AuditLog> findByTimestampBefore(LocalDateTime timestamp);

    // Business logic operations
    List<AuditLog> getRecentActivity(int limit);
    List<AuditLog> getActivityByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<AuditLog> getActivityByUser(Integer userId);
    List<AuditLog> getActivityByEntityType(String entityType);
    List<AuditLog> getActivityByAction(String action);
    List<AuditLog> getActivityByIpAddress(String ipAddress);
    List<AuditLog> getActivityByUserAndDateRange(Integer userId, LocalDateTime startDate, LocalDateTime endDate);
    List<AuditLog> getActivityByEntityAndDateRange(String entityType, Integer entityId, LocalDateTime startDate, LocalDateTime endDate);

    // Advanced queries
    List<AuditLog> getRecentActivityByEntityType(String entityType, int limit);
    List<AuditLog> getActivityByUserAgent(String userAgent);
    List<AuditLog> getActivityByUserAgentContaining(String userAgentPattern);
    List<AuditLog> getActivityByIpAddressAndDateRange(String ipAddress, LocalDateTime startDate, LocalDateTime endDate);
    List<AuditLog> getActivityByActionAndDateRange(String action, LocalDateTime startDate, LocalDateTime endDate);
    List<AuditLog> getActivityByEntityTypeAndAction(String entityType, String action);
    List<AuditLog> getActivityByUserAndAction(Integer userId, String action);
    List<AuditLog> getActivityByUserAndEntityType(Integer userId, String entityType);

    // Bulk operations
    void bulkCreateAuditLogs(List<AuditLog> auditLogs);
    void bulkDeleteAuditLogs(List<Integer> auditLogIds);
    void bulkDeleteAuditLogsByEntityType(String entityType);
    void bulkDeleteAuditLogsByUser(Integer userId);
    void bulkDeleteAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    void bulkDeleteAuditLogsByAction(String action);

    // Check operations
    boolean existsById(Integer id);
    boolean existsByEntityTypeAndEntityId(String entityType, Integer entityId);
    boolean existsByUser(Integer userId);
    boolean existsByAction(String action);
    boolean existsByIpAddress(String ipAddress);
    long countByEntityType(String entityType);
    long countByUser(Integer userId);
    long countByAction(String action);
    long countByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    long countByIpAddress(String ipAddress);

    // Audit trail operations
    List<AuditLog> getAuditTrailForEntity(String entityType, Integer entityId);
    List<AuditLog> getAuditTrailForUser(Integer userId);
    List<AuditLog> getAuditTrailForEntityAndUser(String entityType, Integer entityId, Integer userId);
    List<AuditLog> getAuditTrailForEntityAndAction(String entityType, Integer entityId, String action);
    List<AuditLog> getAuditTrailForUserAndAction(Integer userId, String action);
    List<AuditLog> getAuditTrailForEntityAndDateRange(String entityType, Integer entityId, LocalDateTime startDate, LocalDateTime endDate);
    List<AuditLog> getAuditTrailForUserAndDateRange(Integer userId, LocalDateTime startDate, LocalDateTime endDate);

    // Statistics and analytics
    long getTotalAuditLogs();
    long getTotalAuditLogsByEntityType(String entityType);
    long getTotalAuditLogsByUser(Integer userId);
    long getTotalAuditLogsByAction(String action);
    long getTotalAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    long getTotalAuditLogsByIpAddress(String ipAddress);
    List<AuditLog> getTopUsersByActivity(int limit);
    List<AuditLog> getTopEntityTypesByActivity(int limit);
    List<AuditLog> getTopActionsByActivity(int limit);
    List<AuditLog> getTopIpAddressesByActivity(int limit);
    List<AuditLog> getActivityByMonth();
    List<AuditLog> getActivityByDayOfWeek();
    List<AuditLog> getActivityByHour();

    // Security and monitoring operations
    List<AuditLog> getSuspiciousActivity(String ipAddress);
    List<AuditLog> getActivityByMultipleIpAddresses(List<String> ipAddresses);
    List<AuditLog> getActivityByUserAgentPattern(String pattern);
    List<AuditLog> getFailedLoginAttempts();
    List<AuditLog> getSuccessfulLoginAttempts();
    List<AuditLog> getDataModificationActivity();
    List<AuditLog> getDataAccessActivity();
    List<AuditLog> getDataDeletionActivity();

    // Data integrity operations
    List<AuditLog> getAuditLogsWithMissingUser();
    List<AuditLog> getAuditLogsWithMissingIpAddress();
    List<AuditLog> getAuditLogsWithInvalidJsonData();
    List<AuditLog> getAuditLogsWithEmptyOldValues();
    List<AuditLog> getAuditLogsWithEmptyNewValues();
    List<AuditLog> getAuditLogsWithBothEmptyValues();

    // Utility operations
    void cleanupOldAuditLogs(int daysToKeep);
    void deleteOldAuditLogs(LocalDateTime beforeDate);
    List<AuditLog> searchAuditLogsByKeyword(String keyword);
    List<AuditLog> searchAuditLogsByUserAgent(String userAgent);
    Optional<AuditLog> findLatestAuditLogByEntity(String entityType, Integer entityId);
    List<AuditLog> getAuditLogsWithJsonData();
    List<AuditLog> getAuditLogsWithoutJsonData();
    List<AuditLog> getAuditLogsByJsonField(String fieldName, String fieldValue);
    List<AuditLog> getAuditLogsByJsonFieldContaining(String fieldName, String fieldValue);
    List<AuditLog> getAuditLogsByJsonFieldExists(String fieldName);
    List<AuditLog> getAuditLogsByJsonFieldNotExists(String fieldName);
}
