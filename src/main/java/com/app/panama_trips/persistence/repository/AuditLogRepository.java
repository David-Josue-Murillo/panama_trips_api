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

    // Additional repository methods for comprehensive functionality
    @Query("SELECT a FROM AuditLog a WHERE a.entityType = :entityType")
    List<AuditLog> findByEntityType(@Param("entityType") String entityType);

    @Query("SELECT a FROM AuditLog a WHERE a.entityId = :entityId")
    List<AuditLog> findByEntityId(@Param("entityId") Integer entityId);

    @Query("SELECT a FROM AuditLog a WHERE a.actionTimestamp >= :timestamp")
    List<AuditLog> findByActionTimestampAfter(@Param("timestamp") LocalDateTime timestamp);

    @Query("SELECT a FROM AuditLog a WHERE a.actionTimestamp <= :timestamp")
    List<AuditLog> findByActionTimestampBeforeQuery(@Param("timestamp") LocalDateTime timestamp);

    @Query("SELECT a FROM AuditLog a WHERE a.user.id = :userId")
    List<AuditLog> findByUserId(@Param("userId") Long userId);

    @Query("SELECT a FROM AuditLog a WHERE a.userAgent = :userAgent")
    List<AuditLog> findByUserAgent(@Param("userAgent") String userAgent);

    @Query("SELECT a FROM AuditLog a WHERE a.userAgent LIKE CONCAT('%', :userAgentPattern, '%')")
    List<AuditLog> findByUserAgentContaining(@Param("userAgentPattern") String userAgentPattern);

    @Query("SELECT a FROM AuditLog a WHERE a.user.id = :userId AND a.action = :action")
    List<AuditLog> findByUserIdAndAction(@Param("userId") Long userId, @Param("action") String action);

    @Query("SELECT a FROM AuditLog a WHERE a.user.id = :userId AND a.entityType = :entityType")
    List<AuditLog> findByUserIdAndEntityType(@Param("userId") Long userId, @Param("entityType") String entityType);

    @Query("SELECT a FROM AuditLog a WHERE a.action = :action AND a.entityType = :entityType")
    List<AuditLog> findByActionAndEntityType(@Param("action") String action, @Param("entityType") String entityType);

    @Query("SELECT a FROM AuditLog a WHERE a.entityType = :entityType AND a.entityId = :entityId AND a.action = :action")
    List<AuditLog> findByEntityTypeAndEntityIdAndAction(@Param("entityType") String entityType,
            @Param("entityId") Integer entityId, @Param("action") String action);

    @Query("SELECT a FROM AuditLog a WHERE a.user.id = :userId AND a.actionTimestamp BETWEEN :startDate AND :endDate")
    List<AuditLog> findByUserIdAndActionTimestampBetween(@Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM AuditLog a WHERE a.ipAddress = :ipAddress AND a.actionTimestamp BETWEEN :startDate AND :endDate")
    List<AuditLog> findByIpAddressAndActionTimestampBetween(@Param("ipAddress") String ipAddress,
            @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM AuditLog a WHERE a.action = :action AND a.actionTimestamp BETWEEN :startDate AND :endDate")
    List<AuditLog> findByActionAndActionTimestampBetween(@Param("action") String action,
            @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM AuditLog a WHERE a.entityType = :entityType AND a.entityId = :entityId AND a.actionTimestamp BETWEEN :startDate AND :endDate")
    List<AuditLog> findByEntityTypeAndEntityIdAndActionTimestampBetween(@Param("entityType") String entityType,
            @Param("entityId") Integer entityId, @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Count queries
    @Query("SELECT COUNT(a) FROM AuditLog a WHERE a.entityType = :entityType")
    Long countByEntityType(@Param("entityType") String entityType);

    @Query("SELECT COUNT(a) FROM AuditLog a WHERE a.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(a) FROM AuditLog a WHERE a.action = :action")
    Long countByAction(@Param("action") String action);

    @Query("SELECT COUNT(a) FROM AuditLog a WHERE a.actionTimestamp BETWEEN :startDate AND :endDate")
    Long countByActionTimestampBetween(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(a) FROM AuditLog a WHERE a.ipAddress = :ipAddress")
    Long countByIpAddress(@Param("ipAddress") String ipAddress);

    @Query("SELECT COUNT(a) FROM AuditLog a WHERE a.entityType = :entityType AND a.entityId = :entityId")
    Long countByEntityTypeAndEntityId(@Param("entityType") String entityType, @Param("entityId") Integer entityId);

    // Ordering queries
    @Query("SELECT a FROM AuditLog a ORDER BY a.actionTimestamp DESC")
    List<AuditLog> findAllOrderByActionTimestampDesc();

    @Query("SELECT a FROM AuditLog a ORDER BY a.actionTimestamp ASC")
    List<AuditLog> findAllOrderByActionTimestampAsc();

    @Query("SELECT a FROM AuditLog a WHERE a.entityType = :entityType AND a.entityId = :entityId ORDER BY a.actionTimestamp ASC")
    List<AuditLog> findByEntityTypeAndEntityIdOrderByActionTimestampAsc(@Param("entityType") String entityType, @Param("entityId") Integer entityId);

    @Query("SELECT a FROM AuditLog a WHERE a.user.id = :userId ORDER BY a.actionTimestamp ASC")
    List<AuditLog> findByUserIdOrderByActionTimestampAsc(@Param("userId") Long userId);

    @Query("SELECT a FROM AuditLog a WHERE a.entityType = :entityType ORDER BY a.actionTimestamp DESC")
    List<AuditLog> findByEntityTypeOrderByActionTimestampDesc(@Param("entityType") String entityType);

    @Query("SELECT a FROM AuditLog a WHERE a.action = :action ORDER BY a.actionTimestamp DESC")
    List<AuditLog> findByActionOrderByActionTimestampDesc(@Param("action") String action);

    // Latest record queries
    @Query("SELECT a FROM AuditLog a WHERE a.entityType = :entityType AND a.entityId = :entityId ORDER BY a.actionTimestamp DESC LIMIT 1")
    AuditLog findLatestByEntityTypeAndEntityId(@Param("entityType") String entityType, @Param("entityId") Integer entityId);

    @Query("SELECT a FROM AuditLog a WHERE a.user.id = :userId ORDER BY a.actionTimestamp DESC LIMIT 1")
    AuditLog findLatestByUserId(@Param("userId") Long userId);

    // Complex filtering queries
    @Query("SELECT a FROM AuditLog a WHERE a.action IN :actions")
    List<AuditLog> findByActionIn(@Param("actions") List<String> actions);

    @Query("SELECT a FROM AuditLog a WHERE a.entityType IN :entityTypes")
    List<AuditLog> findByEntityTypeIn(@Param("entityTypes") List<String> entityTypes);

    @Query("SELECT a FROM AuditLog a WHERE a.ipAddress IN :ipAddresses")
    List<AuditLog> findByIpAddressIn(@Param("ipAddresses") List<String> ipAddresses);

    @Query("SELECT a FROM AuditLog a WHERE a.user.id IN :userIds")
    List<AuditLog> findByUserIdIn(@Param("userIds") List<Long> userIds);

    // JSON field queries (PostgreSQL specific)
    @Query(value = "SELECT * FROM audit_log WHERE old_values::text LIKE '%' || :fieldValue || '%' OR new_values::text LIKE '%' || :fieldValue || '%'", nativeQuery = true)
    List<AuditLog> findByJsonFieldContaining(@Param("fieldValue") String fieldValue);

    @Query(value = "SELECT * FROM audit_log WHERE old_values ? :fieldName OR new_values ? :fieldName", nativeQuery = true)
    List<AuditLog> findByJsonFieldExists(@Param("fieldName") String fieldName);

    @Query(value = "SELECT * FROM audit_log WHERE old_values->>:fieldName = :fieldValue OR new_values->>:fieldName = :fieldValue", nativeQuery = true)
    List<AuditLog> findByJsonFieldValue(@Param("fieldName") String fieldName, @Param("fieldValue") String fieldValue);

    // Null/empty checks
    @Query("SELECT a FROM AuditLog a WHERE a.user IS NULL")
    List<AuditLog> findByUserIsNull();

    @Query("SELECT a FROM AuditLog a WHERE a.ipAddress IS NULL OR a.ipAddress = ''")
    List<AuditLog> findByIpAddressIsNullOrEmpty();

    @Query("SELECT a FROM AuditLog a WHERE a.oldValues IS NULL OR a.oldValues = ''")
    List<AuditLog> findByOldValuesIsNullOrEmpty();

    @Query("SELECT a FROM AuditLog a WHERE a.newValues IS NULL OR a.newValues = ''")
    List<AuditLog> findByNewValuesIsNullOrEmpty();

    @Query("SELECT a FROM AuditLog a WHERE (a.oldValues IS NULL OR a.oldValues = '') AND (a.newValues IS NULL OR a.newValues = '')")
    List<AuditLog> findByBothValuesIsNullOrEmpty();

    @Query("SELECT a FROM AuditLog a WHERE (a.oldValues IS NOT NULL AND a.oldValues != '') OR (a.newValues IS NOT NULL AND a.newValues != '')")
    List<AuditLog> findByAnyValueIsNotNullOrEmpty();

    // Security-related queries
    @Query("SELECT a FROM AuditLog a WHERE a.action LIKE CONCAT('%', :actionPattern, '%')")
    List<AuditLog> findByActionContaining(@Param("actionPattern") String actionPattern);

    @Query("SELECT a FROM AuditLog a WHERE a.action LIKE 'LOGIN_FAILED%' OR a.action LIKE 'AUTHENTICATION_FAILED%' OR a.action LIKE 'FAILED_LOGIN%'")
    List<AuditLog> findFailedLoginAttempts();

    @Query("SELECT a FROM AuditLog a WHERE a.action LIKE 'LOGIN_SUCCESS%' OR a.action LIKE 'AUTHENTICATION_SUCCESS%' OR a.action LIKE 'SUCCESSFUL_LOGIN%'")
    List<AuditLog> findSuccessfulLoginAttempts();

    @Query("SELECT a FROM AuditLog a WHERE a.action LIKE '%UPDATE%' OR a.action LIKE '%MODIFY%' OR a.action LIKE '%EDIT%'")
    List<AuditLog> findDataModificationActivity();

    @Query("SELECT a FROM AuditLog a WHERE a.action LIKE '%READ%' OR a.action LIKE '%VIEW%' OR a.action LIKE '%ACCESS%'")
    List<AuditLog> findDataAccessActivity();

    @Query("SELECT a FROM AuditLog a WHERE a.action LIKE '%DELETE%' OR a.action LIKE '%REMOVE%' OR a.action LIKE '%DESTROY%'")
    List<AuditLog> findDataDeletionActivity();

    // Time-based queries
    @Query("SELECT a FROM AuditLog a WHERE EXTRACT(HOUR FROM a.actionTimestamp) = :hour")
    List<AuditLog> findByHour(@Param("hour") int hour);

    @Query("SELECT a FROM AuditLog a WHERE EXTRACT(DOW FROM a.actionTimestamp) = :dayOfWeek")
    List<AuditLog> findByDayOfWeek(@Param("dayOfWeek") int dayOfWeek);

    @Query("SELECT a FROM AuditLog a WHERE EXTRACT(MONTH FROM a.actionTimestamp) = :month")
    List<AuditLog> findByMonth(@Param("month") int month);

    @Query("SELECT a FROM AuditLog a WHERE EXTRACT(YEAR FROM a.actionTimestamp) = :year")
    List<AuditLog> findByYear(@Param("year") int year);

    // Cleanup queries
    @Query("SELECT a FROM AuditLog a WHERE a.actionTimestamp < :cutoffDate")
    List<AuditLog> findByActionTimestampBefore(@Param("cutoffDate") LocalDateTime cutoffDate);

    // Existence checks
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AuditLog a WHERE a.entityType = :entityType AND a.entityId = :entityId")
    Boolean existsByEntityTypeAndEntityId(@Param("entityType") String entityType, @Param("entityId") Integer entityId);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AuditLog a WHERE a.user.id = :userId")
    Boolean existsByUserId(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AuditLog a WHERE a.action = :action")
    Boolean existsByAction(@Param("action") String action);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AuditLog a WHERE a.ipAddress = :ipAddress")
    Boolean existsByIpAddress(@Param("ipAddress") String ipAddress);
}