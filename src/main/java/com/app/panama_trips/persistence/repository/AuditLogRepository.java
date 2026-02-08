package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.AuditLog;
import com.app.panama_trips.persistence.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {

    // --- Existing methods ---
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, Integer entityId);

    List<AuditLog> findByUser(UserEntity user);

    List<AuditLog> findByActionTimestampBetween(LocalDateTime start, LocalDateTime end);

    List<AuditLog> findByAction(String action);

    @Query("SELECT a FROM AuditLog a WHERE a.ipAddress = :ipAddress ORDER BY a.actionTimestamp DESC")
    List<AuditLog> findByIpAddress(@Param("ipAddress") String ipAddress);

    @Query("SELECT a FROM AuditLog a WHERE a.entityType = :entityType AND a.actionTimestamp >= :since ORDER BY a.actionTimestamp DESC")
    List<AuditLog> findRecentActivityByEntityType(@Param("entityType") String entityType, @Param("since") LocalDateTime since);

    // --- Single field queries ---
    List<AuditLog> findByEntityType(String entityType);

    List<AuditLog> findByEntityId(Integer entityId);

    List<AuditLog> findByUser_Id(Long userId);

    List<AuditLog> findByUserAgent(String userAgent);

    List<AuditLog> findByUserAgentContaining(String userAgent);

    List<AuditLog> findByActionTimestampAfter(LocalDateTime timestamp);

    List<AuditLog> findByActionTimestampBefore(LocalDateTime timestamp);

    // --- Combined queries ---
    List<AuditLog> findByUser_IdAndAction(Long userId, String action);

    List<AuditLog> findByUser_IdAndEntityType(Long userId, String entityType);

    List<AuditLog> findByActionAndEntityType(String action, String entityType);

    List<AuditLog> findByUser_IdAndActionTimestampBetween(Long userId, LocalDateTime start, LocalDateTime end);

    List<AuditLog> findByEntityTypeAndEntityIdAndActionTimestampBetween(String entityType, Integer entityId, LocalDateTime start, LocalDateTime end);

    List<AuditLog> findByIpAddressAndActionTimestampBetween(String ipAddress, LocalDateTime start, LocalDateTime end);

    List<AuditLog> findByActionAndActionTimestampBetween(String action, LocalDateTime start, LocalDateTime end);

    // --- Collection queries ---
    List<AuditLog> findByActionIn(List<String> actions);

    List<AuditLog> findByIpAddressIn(List<String> ipAddresses);

    // --- Sorted queries ---
    List<AuditLog> findByEntityTypeAndEntityIdOrderByActionTimestampAsc(String entityType, Integer entityId);

    List<AuditLog> findByUser_IdOrderByActionTimestampAsc(Long userId);

    @Query("SELECT a FROM AuditLog a ORDER BY a.actionTimestamp DESC")
    List<AuditLog> findRecentActivity(Pageable pageable);

    // --- Null/empty checks ---
    List<AuditLog> findByUserIsNull();

    @Query("SELECT a FROM AuditLog a WHERE a.ipAddress IS NULL OR a.ipAddress = ''")
    List<AuditLog> findByIpAddressIsNullOrEmpty();

    @Query("SELECT a FROM AuditLog a WHERE a.oldValues IS NULL OR a.oldValues = ''")
    List<AuditLog> findByOldValuesIsNullOrEmpty();

    @Query("SELECT a FROM AuditLog a WHERE a.newValues IS NULL OR a.newValues = ''")
    List<AuditLog> findByNewValuesIsNullOrEmpty();

    @Query("SELECT a FROM AuditLog a WHERE (a.oldValues IS NULL OR a.oldValues = '') AND (a.newValues IS NULL OR a.newValues = '')")
    List<AuditLog> findByBothValuesNullOrEmpty();

    @Query("SELECT a FROM AuditLog a WHERE a.oldValues IS NOT NULL AND a.oldValues <> '' OR a.newValues IS NOT NULL AND a.newValues <> ''")
    List<AuditLog> findByOldValuesOrNewValuesNotEmpty();

    @Query("SELECT a FROM AuditLog a WHERE (a.oldValues IS NULL OR a.oldValues = '') AND (a.newValues IS NULL OR a.newValues = '')")
    List<AuditLog> findByOldValuesAndNewValuesNullOrEmpty();

    // --- Count queries ---
    long countByEntityType(String entityType);

    long countByUser_Id(Long userId);

    long countByAction(String action);

    long countByIpAddress(String ipAddress);

    long countByActionTimestampBetween(LocalDateTime start, LocalDateTime end);

    // --- Exists queries ---
    boolean existsByUser_Id(Long userId);

    boolean existsByEntityTypeAndEntityId(String entityType, Integer entityId);

    boolean existsByAction(String action);

    boolean existsByIpAddress(String ipAddress);

    // --- Delete queries ---
    void deleteByEntityType(String entityType);

    void deleteByUser_Id(Long userId);

    void deleteByAction(String action);

    void deleteByActionTimestampBefore(LocalDateTime timestamp);

    // --- Keyword search ---
    @Query("SELECT a FROM AuditLog a WHERE LOWER(a.entityType) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(a.action) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(a.ipAddress) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(a.userAgent) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<AuditLog> searchByKeyword(@Param("keyword") String keyword);
}
