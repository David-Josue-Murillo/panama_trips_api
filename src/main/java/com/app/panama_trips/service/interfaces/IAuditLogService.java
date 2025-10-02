package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAuditLogService {
    // CRUD operations
    Page<AuditLog> getAllAuditLogs(Pageable pageable);
    AuditLog getAuditLogById(Integer id);
    AuditLog saveAuditLog(AuditLog auditLog);
    AuditLog updateAuditLog(Integer id, AuditLog auditLog);
    void deleteAuditLog(Integer id);
}
