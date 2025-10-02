package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.AuditLog;
import com.app.panama_trips.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

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
}
