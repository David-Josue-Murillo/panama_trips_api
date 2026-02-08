package com.app.panama_trips.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.AuditLog;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.repository.AuditLogRepository;
import com.app.panama_trips.service.implementation.AuditLogService;

import static com.app.panama_trips.DataProvider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuditLogServiceTest {
    
    @Mock
    private AuditLogRepository repository;

    @InjectMocks
    private AuditLogService service;

    @Captor
    private ArgumentCaptor<AuditLog> auditLogCaptor;

    @Captor
    private ArgumentCaptor<List<AuditLog>> auditLogsCaptor;

    @Captor
    private ArgumentCaptor<List<Integer>> idsCaptor;

    private AuditLog auditLog;
    private List<AuditLog> auditLogs;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        auditLog = auditLogOneMock();
        auditLogs = auditLogListMock();
        user = userAdmin();
    }

    // CRUD Operations Tests
    @Test
    @DisplayName("Should return all audit logs when getAllAuditLogs is called with pagination")
    void getAllAuditLogs_shouldReturnAllData() {
        // Given
        Page<AuditLog> page = new PageImpl<>(auditLogs);
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(page);

        // When
        Page<AuditLog> result = service.getAllAuditLogs(pageable);

        // Then
        assertNotNull(result);
        assertEquals(auditLogs.size(), result.getTotalElements());
        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("Should return audit log by id when exists")
    void getAuditLogById_whenExists_shouldReturnAuditLog() {
        // Given
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.of(auditLog));

        // When
        AuditLog result = service.getAuditLogById(id);

        // Then
        assertNotNull(result);
        assertEquals(auditLog.getId(), result.getId());
        assertEquals(auditLog.getEntityType(), result.getEntityType());
        assertEquals(auditLog.getAction(), result.getAction());
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should throw exception when getting audit log by id that doesn't exist")
    void getAuditLogById_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getAuditLogById(id));
        assertEquals("Audit log not found with id: " + id, exception.getMessage());
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should save audit log successfully")
    void saveAuditLog_success() {
        // Given
        when(repository.save(any(AuditLog.class))).thenReturn(auditLog);

        // When
        AuditLog result = service.saveAuditLog(auditLog);

        // Then
        assertNotNull(result);
        assertEquals(auditLog.getId(), result.getId());
        verify(repository).save(auditLogCaptor.capture());
        AuditLog savedAuditLog = auditLogCaptor.getValue();
        assertEquals(auditLog.getEntityType(), savedAuditLog.getEntityType());
        assertEquals(auditLog.getEntityId(), savedAuditLog.getEntityId());
        assertEquals(auditLog.getAction(), savedAuditLog.getAction());
    }

    @Test
    @DisplayName("Should throw exception when saving audit log with null entity type")
    void saveAuditLog_withNullEntityType_shouldThrowException() {
        // Given
        auditLog.setEntityType(null);

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.saveAuditLog(auditLog));
        assertEquals("Entity type cannot be null or empty", exception.getMessage());
        verify(repository, never()).save(any(AuditLog.class));
    }

    @Test
    @DisplayName("Should throw exception when saving audit log with empty entity type")
    void saveAuditLog_withEmptyEntityType_shouldThrowException() {
        // Given
        auditLog.setEntityType("");

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.saveAuditLog(auditLog));
        assertEquals("Entity type cannot be null or empty", exception.getMessage());
        verify(repository, never()).save(any(AuditLog.class));
    }

    @Test
    @DisplayName("Should throw exception when saving audit log with null entity id")
    void saveAuditLog_withNullEntityId_shouldThrowException() {
        // Given
        auditLog.setEntityId(null);

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.saveAuditLog(auditLog));
        assertEquals("Entity ID cannot be null", exception.getMessage());
        verify(repository, never()).save(any(AuditLog.class));
    }

    @Test
    @DisplayName("Should throw exception when saving audit log with null action")
    void saveAuditLog_withNullAction_shouldThrowException() {
        // Given
        auditLog.setAction(null);

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.saveAuditLog(auditLog));
        assertEquals("Action cannot be null or empty", exception.getMessage());
        verify(repository, never()).save(any(AuditLog.class));
    }

    @Test
    @DisplayName("Should set current timestamp when saving audit log with null timestamp")
    void saveAuditLog_withNullTimestamp_shouldSetCurrentTimestamp() {
        // Given
        auditLog.setActionTimestamp(null);
        when(repository.save(any(AuditLog.class))).thenReturn(auditLog);

        // When
        service.saveAuditLog(auditLog);

        // Then
        verify(repository).save(auditLogCaptor.capture());
        AuditLog savedAuditLog = auditLogCaptor.getValue();
        assertNotNull(savedAuditLog.getActionTimestamp());
    }

    @Test
    @DisplayName("Should update audit log successfully")
    void updateAuditLog_success() {
        // Given
        Integer id = 1;
        AuditLog updateAuditLog = auditLogTwoMock();
        updateAuditLog.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(auditLog));
        when(repository.save(any(AuditLog.class))).thenReturn(updateAuditLog);

        // When
        AuditLog result = service.updateAuditLog(id, updateAuditLog);

        // Then
        assertNotNull(result);
        verify(repository).findById(id);
        verify(repository).save(auditLogCaptor.capture());
        AuditLog updatedAuditLog = auditLogCaptor.getValue();
        assertEquals(updateAuditLog.getEntityType(), updatedAuditLog.getEntityType());
        assertEquals(updateAuditLog.getAction(), updatedAuditLog.getAction());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent audit log")
    void updateAuditLog_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateAuditLog(id, auditLog));
        assertEquals("Audit log not found with id: " + id, exception.getMessage());
        verify(repository).findById(id);
        verify(repository, never()).save(any(AuditLog.class));
    }

    @Test
    @DisplayName("Should delete audit log successfully when exists")
    void deleteAuditLog_whenExists_success() {
        // Given
        Integer id = 1;
        when(repository.existsById(id)).thenReturn(true);

        // When
        service.deleteAuditLog(id);

        // Then
        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent audit log")
    void deleteAuditLog_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(repository.existsById(id)).thenReturn(false);

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteAuditLog(id));
        assertEquals("Audit log not found with id: " + id, exception.getMessage());
        verify(repository).existsById(id);
        verify(repository, never()).deleteById(anyInt());
    }

    // Find Operations by Entity Relationships Tests
    @Test
    @DisplayName("Should find audit logs by entity type and entity id")
    void findByEntityTypeAndEntityId_shouldReturnMatchingLogs() {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        when(repository.findByEntityTypeAndEntityId(entityType, entityId)).thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.findByEntityTypeAndEntityId(entityType, entityId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Test
    @DisplayName("Should find audit logs by user")
    void findByUser_shouldReturnMatchingLogs() {
        // Given
        when(repository.findByUser(user)).thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.findByUser(user);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findByUser(user);
    }

    @Test
    @DisplayName("Should find audit logs by user id")
    void findByUserId_shouldReturnMatchingLogs() {
        // Given
        Integer userId = 1;
        when(repository.findByUser(any(UserEntity.class))).thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.findByUserId(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findByUser(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should find audit logs by action")
    void findByAction_shouldReturnMatchingLogs() {
        // Given
        String action = "CREATE";
        when(repository.findByAction(action)).thenReturn(auditLogListByActionMock());

        // When
        List<AuditLog> result = service.findByAction(action);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findByAction(action);
    }

    @Test
    @DisplayName("Should find audit logs by action timestamp between")
    void findByActionTimestampBetween_shouldReturnMatchingLogs() {
        // Given
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        when(repository.findByActionTimestampBetween(start, end)).thenReturn(auditLogListByDateRangeMock());

        // When
        List<AuditLog> result = service.findByActionTimestampBetween(start, end);

        // Then
        assertNotNull(result);
        assertEquals(5, result.size());
        verify(repository).findByActionTimestampBetween(start, end);
    }

    @Test
    @DisplayName("Should find audit logs by ip address")
    void findByIpAddress_shouldReturnMatchingLogs() {
        // Given
        String ipAddress = "192.168.1.1";
        when(repository.findByIpAddress(ipAddress)).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.findByIpAddress(ipAddress);

        // Then
        assertNotNull(result);
        assertEquals(auditLogs.size(), result.size());
        verify(repository).findByIpAddress(ipAddress);
    }

    // Specialized Queries Tests
    @Test
    @DisplayName("Should find recent activity by entity type")
    void findRecentActivityByEntityType_shouldReturnRecentActivity() {
        // Given
        String entityType = "User";
        LocalDateTime since = LocalDateTime.now().minusHours(24);
        when(repository.findRecentActivityByEntityType(entityType, since)).thenReturn(auditLogListRecentMock());

        // When
        List<AuditLog> result = service.findRecentActivityByEntityType(entityType, since);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(repository).findRecentActivityByEntityType(entityType, since);
    }

    @Test
    @DisplayName("Should find audit logs by entity type using stream filtering")
    void findByEntityType_shouldReturnMatchingLogs() {
        // Given
        String entityType = "Reservation";
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.findByEntityType(entityType);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> entityType.equals(log.getEntityType())));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should find audit logs by entity id using stream filtering")
    void findByEntityId_shouldReturnMatchingLogs() {
        // Given
        Integer entityId = 1;
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.findByEntityId(entityId);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> entityId.equals(log.getEntityId())));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should find audit logs by user and action")
    void findByUserAndAction_shouldReturnMatchingLogs() {
        // Given
        String action = "CREATE";
        when(repository.findByUser(user)).thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.findByUserAndAction(user, action);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> action.equals(log.getAction())));
        verify(repository).findByUser(user);
    }

    @Test
    @DisplayName("Should find audit logs by user and entity type")
    void findByUserAndEntityType_shouldReturnMatchingLogs() {
        // Given
        String entityType = "User";
        when(repository.findByUser(user)).thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.findByUserAndEntityType(user, entityType);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> entityType.equals(log.getEntityType())));
        verify(repository).findByUser(user);
    }

    @Test
    @DisplayName("Should find audit logs by action and entity type")
    void findByActionAndEntityType_shouldReturnMatchingLogs() {
        // Given
        String action = "CREATE";
        String entityType = "User";
        when(repository.findByAction(action)).thenReturn(auditLogListByActionMock());

        // When
        List<AuditLog> result = service.findByActionAndEntityType(action, entityType);

        // Then
        assertNotNull(result);
        verify(repository).findByAction(action);
    }

    @Test
    @DisplayName("Should find audit logs by timestamp after")
    void findByTimestampAfter_shouldReturnMatchingLogs() {
        // Given
        LocalDateTime timestamp = LocalDateTime.now().minusDays(1);
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.findByTimestampAfter(timestamp);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> log.getActionTimestamp().isAfter(timestamp)));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should find audit logs by timestamp before")
    void findByTimestampBefore_shouldReturnMatchingLogs() {
        // Given
        LocalDateTime timestamp = LocalDateTime.now().plusDays(1);
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.findByTimestampBefore(timestamp);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> log.getActionTimestamp().isBefore(timestamp)));
        verify(repository).findAll();
    }

    // Business Logic Operations Tests
    @Test
    @DisplayName("Should get recent activity with limit")
    void getRecentActivity_shouldReturnRecentActivityWithLimit() {
        // Given
        int limit = 3;
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getRecentActivity(limit);

        // Then
        assertNotNull(result);
        assertTrue(result.size() <= limit);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get activity by date range")
    void getActivityByDateRange_shouldReturnActivityInRange() {
        // Given
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(repository.findByActionTimestampBetween(startDate, endDate)).thenReturn(auditLogListByDateRangeMock());

        // When
        List<AuditLog> result = service.getActivityByDateRange(startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(5, result.size());
        verify(repository).findByActionTimestampBetween(startDate, endDate);
    }

    @Test
    @DisplayName("Should get activity by user id")
    void getActivityByUser_shouldReturnUserActivity() {
        // Given
        Integer userId = 1;
        when(repository.findByUser(any(UserEntity.class))).thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getActivityByUser(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findByUser(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should get activity by entity type")
    void getActivityByEntityType_shouldReturnEntityTypeActivity() {
        // Given
        String entityType = "User";
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getActivityByEntityType(entityType);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> entityType.equals(log.getEntityType())));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get activity by action")
    void getActivityByAction_shouldReturnActionActivity() {
        // Given
        String action = "CREATE";
        when(repository.findByAction(action)).thenReturn(auditLogListByActionMock());

        // When
        List<AuditLog> result = service.getActivityByAction(action);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findByAction(action);
    }

    @Test
    @DisplayName("Should get activity by ip address")
    void getActivityByIpAddress_shouldReturnIpAddressActivity() {
        // Given
        String ipAddress = "192.168.1.1";
        when(repository.findByIpAddress(ipAddress)).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getActivityByIpAddress(ipAddress);

        // Then
        assertNotNull(result);
        assertEquals(auditLogs.size(), result.size());
        verify(repository).findByIpAddress(ipAddress);
    }

    @Test
    @DisplayName("Should get activity by user and date range")
    void getActivityByUserAndDateRange_shouldReturnFilteredActivity() {
        // Given
        Integer userId = 1;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(repository.findByUser(any(UserEntity.class))).thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getActivityByUserAndDateRange(userId, startDate, endDate);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> log.getActionTimestamp().isAfter(startDate) &&
                log.getActionTimestamp().isBefore(endDate)));
        verify(repository).findByUser(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should get activity by entity and date range")
    void getActivityByEntityAndDateRange_shouldReturnFilteredActivity() {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(repository.findByEntityTypeAndEntityId(entityType, entityId)).thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getActivityByEntityAndDateRange(entityType, entityId, startDate, endDate);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> log.getActionTimestamp().isAfter(startDate) &&
                log.getActionTimestamp().isBefore(endDate)));
        verify(repository).findByEntityTypeAndEntityId(entityType, entityId);
    }

    // Advanced Queries Tests
    @Test
    @DisplayName("Should get recent activity by entity type with limit")
    void getRecentActivityByEntityType_shouldReturnRecentActivityWithLimit() {
        // Given
        String entityType = "User";
        int limit = 2;
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getRecentActivityByEntityType(entityType, limit);

        // Then
        assertNotNull(result);
        assertTrue(result.size() <= limit);
        assertTrue(result.stream().allMatch(log -> entityType.equals(log.getEntityType())));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get activity by user agent")
    void getActivityByUserAgent_shouldReturnMatchingActivity() {
        // Given
        String userAgent = "Mozilla/5.0";
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getActivityByUserAgent(userAgent);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> userAgent.equals(log.getUserAgent())));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get activity by user agent containing pattern")
    void getActivityByUserAgentContaining_shouldReturnMatchingActivity() {
        // Given
        String userAgentPattern = "Mozilla";
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getActivityByUserAgentContaining(userAgentPattern);

        // Then
        assertNotNull(result);
        assertTrue(result.stream()
                .allMatch(log -> log.getUserAgent() != null && log.getUserAgent().contains(userAgentPattern)));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get activity by ip address and date range")
    void getActivityByIpAddressAndDateRange_shouldReturnFilteredActivity() {
        // Given
        String ipAddress = "192.168.1.1";
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(repository.findByIpAddress(ipAddress)).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getActivityByIpAddressAndDateRange(ipAddress, startDate, endDate);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> log.getActionTimestamp().isAfter(startDate) &&
                log.getActionTimestamp().isBefore(endDate)));
        verify(repository).findByIpAddress(ipAddress);
    }

    @Test
    @DisplayName("Should get activity by action and date range")
    void getActivityByActionAndDateRange_shouldReturnFilteredActivity() {
        // Given
        String action = "CREATE";
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(repository.findByAction(action)).thenReturn(auditLogListByActionMock());

        // When
        List<AuditLog> result = service.getActivityByActionAndDateRange(action, startDate, endDate);

        // Then
        assertNotNull(result);
        verify(repository).findByAction(action);
    }

    @Test
    @DisplayName("Should get activity by entity type and action")
    void getActivityByEntityTypeAndAction_shouldReturnFilteredActivity() {
        // Given
        String entityType = "User";
        String action = "CREATE";
        when(repository.findByAction(action)).thenReturn(auditLogListByActionMock());

        // When
        List<AuditLog> result = service.getActivityByEntityTypeAndAction(entityType, action);

        // Then
        assertNotNull(result);
        verify(repository).findByAction(action);
    }

    @Test
    @DisplayName("Should get activity by user and action")
    void getActivityByUserAndAction_shouldReturnFilteredActivity() {
        // Given
        Integer userId = 1;
        String action = "CREATE";
        when(repository.findByUser(any(UserEntity.class))).thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getActivityByUserAndAction(userId, action);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> action.equals(log.getAction())));
        verify(repository).findByUser(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should get activity by user and entity type")
    void getActivityByUserAndEntityType_shouldReturnFilteredActivity() {
        // Given
        Integer userId = 1;
        String entityType = "User";
        when(repository.findByUser(any(UserEntity.class))).thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getActivityByUserAndEntityType(userId, entityType);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> entityType.equals(log.getEntityType())));
        verify(repository).findByUser(any(UserEntity.class));
    }

    // Bulk Operations Tests
    @Test
    @DisplayName("Should bulk create audit logs")
    void bulkCreateAuditLogs_success() {
        // Given
        List<AuditLog> auditLogsToCreate = auditLogListForBulkCreateMock();

        // When
        service.bulkCreateAuditLogs(auditLogsToCreate);

        // Then
        verify(repository).saveAll(auditLogsCaptor.capture());
        List<AuditLog> savedAuditLogs = auditLogsCaptor.getValue();
        assertEquals(auditLogsToCreate.size(), savedAuditLogs.size());
    }

    @Test
    @DisplayName("Should bulk delete audit logs by ids")
    void bulkDeleteAuditLogs_success() {
        // Given
        List<Integer> auditLogIds = auditLogIdsForBulkDeleteMock();

        // When
        service.bulkDeleteAuditLogs(auditLogIds);

        // Then
        verify(repository).deleteAllById(idsCaptor.capture());
        List<Integer> deletedIds = idsCaptor.getValue();
        assertEquals(auditLogIds.size(), deletedIds.size());
    }

    @Test
    @DisplayName("Should bulk delete audit logs by entity type")
    void bulkDeleteAuditLogsByEntityType_success() {
        // Given
        String entityType = "User";
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        service.bulkDeleteAuditLogsByEntityType(entityType);

        // Then
        verify(repository).findAll();
        verify(repository).deleteAll(auditLogsCaptor.capture());
        List<AuditLog> deletedLogs = auditLogsCaptor.getValue();
        assertTrue(deletedLogs.stream().allMatch(log -> entityType.equals(log.getEntityType())));
    }

    @Test
    @DisplayName("Should bulk delete audit logs by user")
    void bulkDeleteAuditLogsByUser_success() {
        // Given
        Integer userId = 1;
        when(repository.findByUser(any(UserEntity.class))).thenReturn(auditLogListByUserMock());

        // When
        service.bulkDeleteAuditLogsByUser(userId);

        // Then
        verify(repository).findByUser(any(UserEntity.class));
        verify(repository).deleteAll(auditLogsCaptor.capture());
        List<AuditLog> deletedLogs = auditLogsCaptor.getValue();
        assertEquals(2, deletedLogs.size());
    }

    @Test
    @DisplayName("Should bulk delete audit logs by date range")
    void bulkDeleteAuditLogsByDateRange_success() {
        // Given
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(repository.findByActionTimestampBetween(startDate, endDate)).thenReturn(auditLogListByDateRangeMock());

        // When
        service.bulkDeleteAuditLogsByDateRange(startDate, endDate);

        // Then
        verify(repository).findByActionTimestampBetween(startDate, endDate);
        verify(repository).deleteAll(auditLogsCaptor.capture());
        List<AuditLog> deletedLogs = auditLogsCaptor.getValue();
        assertEquals(5, deletedLogs.size());
    }

    @Test
    @DisplayName("Should bulk delete audit logs by action")
    void bulkDeleteAuditLogsByAction_success() {
        // Given
        String action = "CREATE";
        when(repository.findByAction(action)).thenReturn(auditLogListByActionMock());

        // When
        service.bulkDeleteAuditLogsByAction(action);

        // Then
        verify(repository).findByAction(action);
        verify(repository).deleteAll(auditLogsCaptor.capture());
        List<AuditLog> deletedLogs = auditLogsCaptor.getValue();
        assertEquals(2, deletedLogs.size());
    }

    // Check Operations Tests
    @Test
    @DisplayName("Should check if audit log exists by id")
    void existsById_whenExists_returnsTrue() {
        // Given
        Integer id = 1;
        when(repository.existsById(id)).thenReturn(true);

        // When
        boolean result = service.existsById(id);

        // Then
        assertTrue(result);
        verify(repository).existsById(id);
    }

    @Test
    @DisplayName("Should check if audit log does not exist by id")
    void existsById_whenNotExists_returnsFalse() {
        // Given
        Integer id = 999;
        when(repository.existsById(id)).thenReturn(false);

        // When
        boolean result = service.existsById(id);

        // Then
        assertFalse(result);
        verify(repository).existsById(id);
    }

    @Test
    @DisplayName("Should check if audit log exists by entity type and entity id")
    void existsByEntityTypeAndEntityId_whenExists_returnsTrue() {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        when(repository.findByEntityTypeAndEntityId(entityType, entityId)).thenReturn(auditLogListByUserMock());

        // When
        boolean result = service.existsByEntityTypeAndEntityId(entityType, entityId);

        // Then
        assertTrue(result);
        verify(repository).findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Test
    @DisplayName("Should check if audit log does not exist by entity type and entity id")
    void existsByEntityTypeAndEntityId_whenNotExists_returnsFalse() {
        // Given
        String entityType = "NonExistent";
        Integer entityId = 999;
        when(repository.findByEntityTypeAndEntityId(entityType, entityId)).thenReturn(List.of());

        // When
        boolean result = service.existsByEntityTypeAndEntityId(entityType, entityId);

        // Then
        assertFalse(result);
        verify(repository).findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Test
    @DisplayName("Should check if audit log exists by user")
    void existsByUser_whenExists_returnsTrue() {
        // Given
        Integer userId = 1;
        when(repository.findByUser(any(UserEntity.class))).thenReturn(auditLogListByUserMock());

        // When
        boolean result = service.existsByUser(userId);

        // Then
        assertTrue(result);
        verify(repository).findByUser(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should check if audit log does not exist by user")
    void existsByUser_whenNotExists_returnsFalse() {
        // Given
        Integer userId = 999;
        when(repository.findByUser(any(UserEntity.class))).thenReturn(List.of());

        // When
        boolean result = service.existsByUser(userId);

        // Then
        assertFalse(result);
        verify(repository).findByUser(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should check if audit log exists by action")
    void existsByAction_whenExists_returnsTrue() {
        // Given
        String action = "CREATE";
        when(repository.findByAction(action)).thenReturn(auditLogListByActionMock());

        // When
        boolean result = service.existsByAction(action);

        // Then
        assertTrue(result);
        verify(repository).findByAction(action);
    }

    @Test
    @DisplayName("Should check if audit log does not exist by action")
    void existsByAction_whenNotExists_returnsFalse() {
        // Given
        String action = "NON_EXISTENT";
        when(repository.findByAction(action)).thenReturn(List.of());

        // When
        boolean result = service.existsByAction(action);

        // Then
        assertFalse(result);
        verify(repository).findByAction(action);
    }

    @Test
    @DisplayName("Should check if audit log exists by ip address")
    void existsByIpAddress_whenExists_returnsTrue() {
        // Given
        String ipAddress = "192.168.1.1";
        when(repository.findByIpAddress(ipAddress)).thenReturn(auditLogs);

        // When
        boolean result = service.existsByIpAddress(ipAddress);

        // Then
        assertTrue(result);
        verify(repository).findByIpAddress(ipAddress);
    }

    @Test
    @DisplayName("Should check if audit log does not exist by ip address")
    void existsByIpAddress_whenNotExists_returnsFalse() {
        // Given
        String ipAddress = "999.999.999.999";
        when(repository.findByIpAddress(ipAddress)).thenReturn(List.of());

        // When
        boolean result = service.existsByIpAddress(ipAddress);

        // Then
        assertFalse(result);
        verify(repository).findByIpAddress(ipAddress);
    }

    // Count Operations Tests
    @Test
    @DisplayName("Should count audit logs by entity type")
    void countByEntityType_shouldReturnCorrectCount() {
        // Given
        String entityType = "User";
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        long result = service.countByEntityType(entityType);

        // Then
        assertTrue(result >= 0);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should count audit logs by user")
    void countByUser_shouldReturnCorrectCount() {
        // Given
        Integer userId = 1;
        when(repository.findByUser(any(UserEntity.class))).thenReturn(auditLogListByUserMock());

        // When
        long result = service.countByUser(userId);

        // Then
        assertEquals(2, result);
        verify(repository).findByUser(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should count audit logs by action")
    void countByAction_shouldReturnCorrectCount() {
        // Given
        String action = "CREATE";
        when(repository.findByAction(action)).thenReturn(auditLogListByActionMock());

        // When
        long result = service.countByAction(action);

        // Then
        assertEquals(2, result);
        verify(repository).findByAction(action);
    }

    @Test
    @DisplayName("Should count audit logs by date range")
    void countByDateRange_shouldReturnCorrectCount() {
        // Given
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(repository.findByActionTimestampBetween(startDate, endDate)).thenReturn(auditLogListByDateRangeMock());

        // When
        long result = service.countByDateRange(startDate, endDate);

        // Then
        assertEquals(5, result);
        verify(repository).findByActionTimestampBetween(startDate, endDate);
    }

    @Test
    @DisplayName("Should count audit logs by ip address")
    void countByIpAddress_shouldReturnCorrectCount() {
        // Given
        String ipAddress = "192.168.1.1";
        when(repository.findByIpAddress(ipAddress)).thenReturn(auditLogs);

        // When
        long result = service.countByIpAddress(ipAddress);

        // Then
        assertEquals(auditLogs.size(), result);
        verify(repository).findByIpAddress(ipAddress);
    }

    // Audit Trail Operations Tests
    @Test
    @DisplayName("Should get audit trail for entity")
    void getAuditTrailForEntity_shouldReturnSortedTrail() {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        when(repository.findByEntityTypeAndEntityId(entityType, entityId)).thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getAuditTrailForEntity(entityType, entityId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        // Verify that the trail is sorted by timestamp (ascending)
        for (int i = 1; i < result.size(); i++) {
            assertTrue(result.get(i - 1).getActionTimestamp().compareTo(result.get(i).getActionTimestamp()) <= 0);
        }
        verify(repository).findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Test
    @DisplayName("Should get audit trail for user")
    void getAuditTrailForUser_shouldReturnSortedTrail() {
        // Given
        Integer userId = 1;
        when(repository.findByUser(any(UserEntity.class))).thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getAuditTrailForUser(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        // Verify that the trail is sorted by timestamp (ascending)
        for (int i = 1; i < result.size(); i++) {
            assertTrue(result.get(i - 1).getActionTimestamp().compareTo(result.get(i).getActionTimestamp()) <= 0);
        }
        verify(repository).findByUser(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should get audit trail for entity and user")
    void getAuditTrailForEntityAndUser_shouldReturnFilteredTrail() {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        Integer userId = 1;
        when(repository.findByEntityTypeAndEntityId(entityType, entityId)).thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getAuditTrailForEntityAndUser(entityType, entityId, userId);

        // Then
        assertNotNull(result);
        assertTrue(
                result.stream().allMatch(log -> log.getUser() != null && userId.longValue() == log.getUser().getId()));
        verify(repository).findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Test
    @DisplayName("Should get audit trail for entity and action")
    void getAuditTrailForEntityAndAction_shouldReturnFilteredTrail() {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        String action = "CREATE";
        when(repository.findByEntityTypeAndEntityId(entityType, entityId)).thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getAuditTrailForEntityAndAction(entityType, entityId, action);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> action.equals(log.getAction())));
        verify(repository).findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Test
    @DisplayName("Should get audit trail for user and action")
    void getAuditTrailForUserAndAction_shouldReturnFilteredTrail() {
        // Given
        Integer userId = 1;
        String action = "CREATE";
        when(repository.findByUser(any(UserEntity.class))).thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getAuditTrailForUserAndAction(userId, action);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> action.equals(log.getAction())));
        verify(repository).findByUser(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should get audit trail for entity and date range")
    void getAuditTrailForEntityAndDateRange_shouldReturnFilteredTrail() {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(repository.findByEntityTypeAndEntityId(entityType, entityId)).thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getAuditTrailForEntityAndDateRange(entityType, entityId, startDate, endDate);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> log.getActionTimestamp().isAfter(startDate) &&
                log.getActionTimestamp().isBefore(endDate)));
        verify(repository).findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Test
    @DisplayName("Should get audit trail for user and date range")
    void getAuditTrailForUserAndDateRange_shouldReturnFilteredTrail() {
        // Given
        Integer userId = 1;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(repository.findByUser(any(UserEntity.class))).thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getAuditTrailForUserAndDateRange(userId, startDate, endDate);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> log.getActionTimestamp().isAfter(startDate) &&
                log.getActionTimestamp().isBefore(endDate)));
        verify(repository).findByUser(any(UserEntity.class));
    }

    // Statistics and Analytics Tests
    @Test
    @DisplayName("Should get total audit logs count")
    void getTotalAuditLogs_shouldReturnCorrectCount() {
        // Given
        when(repository.count()).thenReturn(5L);

        // When
        long result = service.getTotalAuditLogs();

        // Then
        assertEquals(5L, result);
        verify(repository).count();
    }

    @Test
    @DisplayName("Should get total audit logs by entity type")
    void getTotalAuditLogsByEntityType_shouldReturnCorrectCount() {
        // Given
        String entityType = "User";
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        long result = service.getTotalAuditLogsByEntityType(entityType);

        // Then
        assertTrue(result >= 0);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get total audit logs by user")
    void getTotalAuditLogsByUser_shouldReturnCorrectCount() {
        // Given
        Integer userId = 1;
        when(repository.findByUser(any(UserEntity.class))).thenReturn(auditLogListByUserMock());

        // When
        long result = service.getTotalAuditLogsByUser(userId);

        // Then
        assertEquals(2, result);
        verify(repository).findByUser(any(UserEntity.class));
    }

    @Test
    @DisplayName("Should get total audit logs by action")
    void getTotalAuditLogsByAction_shouldReturnCorrectCount() {
        // Given
        String action = "CREATE";
        when(repository.findByAction(action)).thenReturn(auditLogListByActionMock());

        // When
        long result = service.getTotalAuditLogsByAction(action);

        // Then
        assertEquals(2, result);
        verify(repository).findByAction(action);
    }

    @Test
    @DisplayName("Should get total audit logs by date range")
    void getTotalAuditLogsByDateRange_shouldReturnCorrectCount() {
        // Given
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(repository.findByActionTimestampBetween(startDate, endDate)).thenReturn(auditLogListByDateRangeMock());

        // When
        long result = service.getTotalAuditLogsByDateRange(startDate, endDate);

        // Then
        assertEquals(5, result);
        verify(repository).findByActionTimestampBetween(startDate, endDate);
    }

    @Test
    @DisplayName("Should get total audit logs by ip address")
    void getTotalAuditLogsByIpAddress_shouldReturnCorrectCount() {
        // Given
        String ipAddress = "192.168.1.1";
        when(repository.findByIpAddress(ipAddress)).thenReturn(auditLogs);

        // When
        long result = service.getTotalAuditLogsByIpAddress(ipAddress);

        // Then
        assertEquals(auditLogs.size(), result);
        verify(repository).findByIpAddress(ipAddress);
    }

    @Test
    @DisplayName("Should get top users by activity")
    void getTopUsersByActivity_shouldReturnTopUsers() {
        // Given
        int limit = 2;
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getTopUsersByActivity(limit);

        // Then
        assertNotNull(result);
        assertTrue(result.size() <= limit);
        assertTrue(result.stream().allMatch(log -> log.getUser() != null));
        verify(repository, atLeastOnce()).findAll();
    }

    @Test
    @DisplayName("Should get top entity types by activity")
    void getTopEntityTypesByActivity_shouldReturnTopEntityTypes() {
        // Given
        int limit = 3;
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getTopEntityTypesByActivity(limit);

        // Then
        assertNotNull(result);
        assertTrue(result.size() <= limit);
        verify(repository, atLeastOnce()).findAll();
    }

    @Test
    @DisplayName("Should get top actions by activity")
    void getTopActionsByActivity_shouldReturnTopActions() {
        // Given
        int limit = 2;
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getTopActionsByActivity(limit);

        // Then
        assertNotNull(result);
        assertTrue(result.size() <= limit);
        verify(repository, atLeastOnce()).findAll();
    }

    @Test
    @DisplayName("Should get top ip addresses by activity")
    void getTopIpAddressesByActivity_shouldReturnTopIpAddresses() {
        // Given
        int limit = 2;
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getTopIpAddressesByActivity(limit);

        // Then
        assertNotNull(result);
        assertTrue(result.size() <= limit);
        assertTrue(result.stream().allMatch(log -> log.getIpAddress() != null));
        verify(repository, atLeastOnce()).findAll();
    }

    @Test
    @DisplayName("Should get activity by month")
    void getActivityByMonth_shouldReturnMonthlyActivity() {
        // Given
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getActivityByMonth();

        // Then
        assertNotNull(result);
        assertEquals(auditLogs.size(), result.size());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get activity by day of week")
    void getActivityByDayOfWeek_shouldReturnWeeklyActivity() {
        // Given
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getActivityByDayOfWeek();

        // Then
        assertNotNull(result);
        assertEquals(auditLogs.size(), result.size());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get activity by hour")
    void getActivityByHour_shouldReturnHourlyActivity() {
        // Given
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getActivityByHour();

        // Then
        assertNotNull(result);
        assertEquals(auditLogs.size(), result.size());
        verify(repository).findAll();
    }

    // Security and Monitoring Operations Tests
    @Test
    @DisplayName("Should get suspicious activity")
    void getSuspiciousActivity_shouldReturnSuspiciousLogs() {
        // Given
        String ipAddress = "192.168.1.100";
        when(repository.findByIpAddress(ipAddress)).thenReturn(auditLogListSecurityMock());

        // When
        List<AuditLog> result = service.getSuspiciousActivity(ipAddress);

        // Then
        assertNotNull(result);
        verify(repository).findByIpAddress(ipAddress);
    }

    @Test
    @DisplayName("Should get activity by multiple ip addresses")
    void getActivityByMultipleIpAddresses_shouldReturnMatchingActivity() {
        // Given
        List<String> ipAddresses = List.of("192.168.1.1", "192.168.1.2");
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getActivityByMultipleIpAddresses(ipAddresses);

        // Then
        assertNotNull(result);
        assertTrue(result.stream()
                .allMatch(log -> log.getIpAddress() != null && ipAddresses.contains(log.getIpAddress())));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get activity by user agent pattern")
    void getActivityByUserAgentPattern_shouldReturnMatchingActivity() {
        // Given
        String pattern = "Mozilla";
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getActivityByUserAgentPattern(pattern);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> log.getUserAgent() != null && log.getUserAgent().contains(pattern)));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get failed login attempts")
    void getFailedLoginAttempts_shouldReturnFailedLogins() {
        // Given
        when(repository.findAll()).thenReturn(auditLogListSecurityMock());

        // When
        List<AuditLog> result = service.getFailedLoginAttempts();

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> log.getAction() != null &&
                (log.getAction().contains("LOGIN_FAILED") ||
                        log.getAction().contains("AUTHENTICATION_FAILED") ||
                        log.getAction().contains("FAILED_LOGIN"))));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get successful login attempts")
    void getSuccessfulLoginAttempts_shouldReturnSuccessfulLogins() {
        // Given
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getSuccessfulLoginAttempts();

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> log.getAction() == null ||
                (!log.getAction().contains("LOGIN_FAILED") &&
                        !log.getAction().contains("AUTHENTICATION_FAILED") &&
                        !log.getAction().contains("FAILED_LOGIN"))));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get data modification activity")
    void getDataModificationActivity_shouldReturnModificationLogs() {
        // Given
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getDataModificationActivity();

        // Then
        assertNotNull(result);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get data access activity")
    void getDataAccessActivity_shouldReturnAccessLogs() {
        // Given
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getDataAccessActivity();

        // Then
        assertNotNull(result);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get data deletion activity")
    void getDataDeletionActivity_shouldReturnDeletionLogs() {
        // Given
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getDataDeletionActivity();

        // Then
        assertNotNull(result);
        verify(repository).findAll();
    }

    // Data Integrity Operations Tests
    @Test
    @DisplayName("Should get audit logs with missing user")
    void getAuditLogsWithMissingUser_shouldReturnLogsWithoutUser() {
        // Given
        when(repository.findAll()).thenReturn(auditLogListWithNullsMock());

        // When
        List<AuditLog> result = service.getAuditLogsWithMissingUser();

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> log.getUser() == null));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get audit logs with missing ip address")
    void getAuditLogsWithMissingIpAddress_shouldReturnLogsWithoutIp() {
        // Given
        when(repository.findAll()).thenReturn(auditLogListWithNullsMock());

        // When
        List<AuditLog> result = service.getAuditLogsWithMissingIpAddress();

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> log.getIpAddress() == null || log.getIpAddress().trim().isEmpty()));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get audit logs with invalid json data")
    void getAuditLogsWithInvalidJsonData_shouldReturnLogsWithInvalidJson() {
        // Given
        when(repository.findAll()).thenReturn(auditLogListWithNullsMock());

        // When
        List<AuditLog> result = service.getAuditLogsWithInvalidJsonData();

        // Then
        assertNotNull(result);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get audit logs with empty old values")
    void getAuditLogsWithEmptyOldValues_shouldReturnLogsWithEmptyOldValues() {
        // Given
        when(repository.findAll()).thenReturn(auditLogListWithNullsMock());

        // When
        List<AuditLog> result = service.getAuditLogsWithEmptyOldValues();

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> log.getOldValues() == null || log.getOldValues().trim().isEmpty()));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get audit logs with empty new values")
    void getAuditLogsWithEmptyNewValues_shouldReturnLogsWithEmptyNewValues() {
        // Given
        when(repository.findAll()).thenReturn(auditLogListWithNullsMock());

        // When
        List<AuditLog> result = service.getAuditLogsWithEmptyNewValues();

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> log.getNewValues() == null || log.getNewValues().trim().isEmpty()));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get audit logs with both empty values")
    void getAuditLogsWithBothEmptyValues_shouldReturnLogsWithBothEmptyValues() {
        // Given
        when(repository.findAll()).thenReturn(auditLogListWithNullsMock());

        // When
        List<AuditLog> result = service.getAuditLogsWithBothEmptyValues();

        // Then
        assertNotNull(result);
        assertTrue(
                result.stream().allMatch(log -> (log.getOldValues() == null || log.getOldValues().trim().isEmpty()) &&
                        (log.getNewValues() == null || log.getNewValues().trim().isEmpty())));
        verify(repository).findAll();
    }

    // Utility Operations Tests
    @Test
    @DisplayName("Should cleanup old audit logs")
    void cleanupOldAuditLogs_shouldDeleteOldLogs() {
        // Given
        int daysToKeep = 30;
        when(repository.findAll()).thenReturn(auditLogListOldMock());

        // When
        service.cleanupOldAuditLogs(daysToKeep);

        // Then
        verify(repository).findAll();
        verify(repository).deleteAll(auditLogsCaptor.capture());
        List<AuditLog> deletedLogs = auditLogsCaptor.getValue();
        assertTrue(deletedLogs.stream()
                .allMatch(log -> log.getActionTimestamp().isBefore(LocalDateTime.now().minusDays(daysToKeep))));
    }

    @Test
    @DisplayName("Should archive audit logs")
    void archiveAuditLogs_shouldArchiveOldLogs() {
        // Given
        LocalDateTime beforeDate = LocalDateTime.now().minusDays(30);
        when(repository.findAll()).thenReturn(auditLogListOldMock());

        // When
        service.archiveAuditLogs(beforeDate);

        // Then
        verify(repository).findAll();
        verify(repository).deleteAll(auditLogsCaptor.capture());
        List<AuditLog> archivedLogs = auditLogsCaptor.getValue();
        assertTrue(archivedLogs.stream().allMatch(log -> log.getActionTimestamp().isBefore(beforeDate)));
    }

    @Test
    @DisplayName("Should search audit logs by keyword")
    void searchAuditLogsByKeyword_shouldReturnMatchingLogs() {
        // Given
        String keyword = "User";
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.searchAuditLogsByKeyword(keyword);

        // Then
        assertNotNull(result);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should search audit logs by user agent")
    void searchAuditLogsByUserAgent_shouldReturnMatchingLogs() {
        // Given
        String userAgent = "Mozilla/5.0";
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.searchAuditLogsByUserAgent(userAgent);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> userAgent.equals(log.getUserAgent())));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should find latest audit log by entity")
    void findLatestAuditLogByEntity_shouldReturnLatestLog() {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        when(repository.findByEntityTypeAndEntityId(entityType, entityId)).thenReturn(auditLogListByUserMock());

        // When
        Optional<AuditLog> result = service.findLatestAuditLogByEntity(entityType, entityId);

        // Then
        assertTrue(result.isPresent());
        verify(repository).findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Test
    @DisplayName("Should get audit logs with json data")
    void getAuditLogsWithJsonData_shouldReturnLogsWithJson() {
        // Given
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getAuditLogsWithJsonData();

        // Then
        assertNotNull(result);
        assertTrue(
                result.stream().allMatch(log -> (log.getOldValues() != null && !log.getOldValues().trim().isEmpty()) ||
                        (log.getNewValues() != null && !log.getNewValues().trim().isEmpty())));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get audit logs without json data")
    void getAuditLogsWithoutJsonData_shouldReturnLogsWithoutJson() {
        // Given
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getAuditLogsWithoutJsonData();

        // Then
        assertNotNull(result);
        assertTrue(
                result.stream().allMatch(log -> (log.getOldValues() == null || log.getOldValues().trim().isEmpty()) &&
                        (log.getNewValues() == null || log.getNewValues().trim().isEmpty())));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get audit logs by json field")
    void getAuditLogsByJsonField_shouldReturnMatchingLogs() {
        // Given
        String fieldName = "name";
        String fieldValue = "John";
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getAuditLogsByJsonField(fieldName, fieldValue);

        // Then
        assertNotNull(result);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get audit logs by json field containing")
    void getAuditLogsByJsonFieldContaining_shouldReturnMatchingLogs() {
        // Given
        String fieldName = "description";
        String fieldValue = "test";
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getAuditLogsByJsonFieldContaining(fieldName, fieldValue);

        // Then
        assertNotNull(result);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get audit logs by json field exists")
    void getAuditLogsByJsonFieldExists_shouldReturnMatchingLogs() {
        // Given
        String fieldName = "status";
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getAuditLogsByJsonFieldExists(fieldName);

        // Then
        assertNotNull(result);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get audit logs by json field not exists")
    void getAuditLogsByJsonFieldNotExists_shouldReturnMatchingLogs() {
        // Given
        String fieldName = "nonExistentField";
        when(repository.findAll()).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getAuditLogsByJsonFieldNotExists(fieldName);

        // Then
        assertNotNull(result);
        verify(repository).findAll();
    }
}