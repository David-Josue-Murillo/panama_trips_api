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
}