package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.AuditLog;
import com.app.panama_trips.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {

    List<AuditLog> findByEntityTypeAndEntityId(String entityType, Integer entityId);

    List<AuditLog> findByUser(UserEntity user);

    List<AuditLog> findByActionTimestampBetween(LocalDateTime start, LocalDateTime end);

    List<AuditLog> findByAction(String action);

    @Query("SELECT a FROM AuditLog a WHERE a.entityType = :entityType AND a.actionTimestamp >= :since ORDER BY a.actionTimestamp DESC")
    List<AuditLog> findRecentActivityByEntityType(@Param("entityType") String entityType, @Param("since") LocalDateTime since);

    @Query("SELECT a FROM AuditLog a WHERE a.ipAddress = :ipAddress ORDER BY a.actionTimestamp DESC")
    List<AuditLog> findByIpAddress(@Param("ipAddress") String ipAddress);
}