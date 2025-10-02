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
}
