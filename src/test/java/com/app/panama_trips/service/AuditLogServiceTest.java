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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.AuditLog;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.entity.enums.AuditAction;
import com.app.panama_trips.persistence.repository.AuditLogRepository;
import com.app.panama_trips.service.implementation.AuditLogService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static com.app.panama_trips.DataProvider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuditLogServiceTest {

    @Mock
    private AuditLogRepository repository;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

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
        when(repository.findByUser_Id(1L)).thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.findByUserId(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findByUser_Id(1L);
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
    @DisplayName("Should find audit logs by entity type")
    void findByEntityType_shouldReturnMatchingLogs() {
        // Given
        String entityType = "Reservation";
        List<AuditLog> expectedLogs = auditLogs.stream()
                .filter(log -> entityType.equals(log.getEntityType()))
                .toList();
        when(repository.findByEntityType(entityType)).thenReturn(expectedLogs);

        // When
        List<AuditLog> result = service.findByEntityType(entityType);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> entityType.equals(log.getEntityType())));
        verify(repository).findByEntityType(entityType);
    }

    @Test
    @DisplayName("Should find audit logs by entity id")
    void findByEntityId_shouldReturnMatchingLogs() {
        // Given
        Integer entityId = 1;
        List<AuditLog> expectedLogs = auditLogs.stream()
                .filter(log -> entityId.equals(log.getEntityId()))
                .toList();
        when(repository.findByEntityId(entityId)).thenReturn(expectedLogs);

        // When
        List<AuditLog> result = service.findByEntityId(entityId);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> entityId.equals(log.getEntityId())));
        verify(repository).findByEntityId(entityId);
    }

    @Test
    @DisplayName("Should find audit logs by user and action")
    void findByUserAndAction_shouldReturnMatchingLogs() {
        // Given
        String action = "CREATE";
        List<AuditLog> expectedLogs = auditLogListByUserMock().stream()
                .filter(log -> action.equals(log.getAction()))
                .toList();
        when(repository.findByUser_IdAndAction(user.getId(), action)).thenReturn(expectedLogs);

        // When
        List<AuditLog> result = service.findByUserAndAction(user, action);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> action.equals(log.getAction())));
        verify(repository).findByUser_IdAndAction(user.getId(), action);
    }

    @Test
    @DisplayName("Should find audit logs by user and entity type")
    void findByUserAndEntityType_shouldReturnMatchingLogs() {
        // Given
        String entityType = "User";
        List<AuditLog> expectedLogs = auditLogListByUserMock().stream()
                .filter(log -> entityType.equals(log.getEntityType()))
                .toList();
        when(repository.findByUser_IdAndEntityType(user.getId(), entityType)).thenReturn(expectedLogs);

        // When
        List<AuditLog> result = service.findByUserAndEntityType(user, entityType);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> entityType.equals(log.getEntityType())));
        verify(repository).findByUser_IdAndEntityType(user.getId(), entityType);
    }

    @Test
    @DisplayName("Should find audit logs by action and entity type")
    void findByActionAndEntityType_shouldReturnMatchingLogs() {
        // Given
        String action = "CREATE";
        String entityType = "User";
        when(repository.findByActionAndEntityType(action, entityType)).thenReturn(auditLogListByActionMock());

        // When
        List<AuditLog> result = service.findByActionAndEntityType(action, entityType);

        // Then
        assertNotNull(result);
        verify(repository).findByActionAndEntityType(action, entityType);
    }

    @Test
    @DisplayName("Should find audit logs by timestamp after")
    void findByTimestampAfter_shouldReturnMatchingLogs() {
        // Given
        LocalDateTime timestamp = LocalDateTime.now().minusDays(1);
        List<AuditLog> expectedLogs = auditLogs.stream()
                .filter(log -> log.getActionTimestamp().isAfter(timestamp))
                .toList();
        when(repository.findByActionTimestampAfter(timestamp)).thenReturn(expectedLogs);

        // When
        List<AuditLog> result = service.findByTimestampAfter(timestamp);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> log.getActionTimestamp().isAfter(timestamp)));
        verify(repository).findByActionTimestampAfter(timestamp);
    }

    @Test
    @DisplayName("Should find audit logs by timestamp before")
    void findByTimestampBefore_shouldReturnMatchingLogs() {
        // Given
        LocalDateTime timestamp = LocalDateTime.now().plusDays(1);
        List<AuditLog> expectedLogs = auditLogs.stream()
                .filter(log -> log.getActionTimestamp().isBefore(timestamp))
                .toList();
        when(repository.findByActionTimestampBefore(timestamp)).thenReturn(expectedLogs);

        // When
        List<AuditLog> result = service.findByTimestampBefore(timestamp);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> log.getActionTimestamp().isBefore(timestamp)));
        verify(repository).findByActionTimestampBefore(timestamp);
    }

    // Business Logic Operations Tests
    @Test
    @DisplayName("Should get recent activity with limit")
    void getRecentActivity_shouldReturnRecentActivityWithLimit() {
        // Given
        int limit = 3;
        when(repository.findRecentActivity(PageRequest.of(0, limit))).thenReturn(auditLogListRecentMock());

        // When
        List<AuditLog> result = service.getRecentActivity(limit);

        // Then
        assertNotNull(result);
        assertTrue(result.size() <= limit);
        verify(repository).findRecentActivity(PageRequest.of(0, limit));
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
        when(repository.findByUser_Id(1L)).thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getActivityByUser(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findByUser_Id(1L);
    }

    @Test
    @DisplayName("Should get activity by entity type")
    void getActivityByEntityType_shouldReturnEntityTypeActivity() {
        // Given
        String entityType = "User";
        List<AuditLog> expectedLogs = auditLogs.stream()
                .filter(log -> entityType.equals(log.getEntityType()))
                .toList();
        when(repository.findByEntityType(entityType)).thenReturn(expectedLogs);

        // When
        List<AuditLog> result = service.getActivityByEntityType(entityType);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> entityType.equals(log.getEntityType())));
        verify(repository).findByEntityType(entityType);
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
        when(repository.findByUser_IdAndActionTimestampBetween(1L, startDate, endDate))
                .thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getActivityByUserAndDateRange(userId, startDate, endDate);

        // Then
        assertNotNull(result);
        verify(repository).findByUser_IdAndActionTimestampBetween(1L, startDate, endDate);
    }

    @Test
    @DisplayName("Should get activity by entity and date range")
    void getActivityByEntityAndDateRange_shouldReturnFilteredActivity() {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(repository.findByEntityTypeAndEntityIdAndActionTimestampBetween(entityType, entityId, startDate, endDate))
                .thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getActivityByEntityAndDateRange(entityType, entityId, startDate, endDate);

        // Then
        assertNotNull(result);
        verify(repository).findByEntityTypeAndEntityIdAndActionTimestampBetween(entityType, entityId, startDate, endDate);
    }

    // Advanced Queries Tests
    @Test
    @DisplayName("Should get recent activity by entity type with limit")
    void getRecentActivityByEntityType_shouldReturnRecentActivityWithLimit() {
        // Given
        String entityType = "User";
        int limit = 2;
        List<AuditLog> expectedLogs = auditLogs.stream()
                .filter(log -> entityType.equals(log.getEntityType()))
                .toList();
        when(repository.findRecentActivityByEntityType(entityType, LocalDateTime.MIN)).thenReturn(expectedLogs);

        // When
        List<AuditLog> result = service.getRecentActivityByEntityType(entityType, limit);

        // Then
        assertNotNull(result);
        assertTrue(result.size() <= limit);
        verify(repository).findRecentActivityByEntityType(entityType, LocalDateTime.MIN);
    }

    @Test
    @DisplayName("Should get activity by user agent")
    void getActivityByUserAgent_shouldReturnMatchingActivity() {
        // Given
        String userAgent = "Mozilla/5.0";
        List<AuditLog> expectedLogs = auditLogs.stream()
                .filter(log -> userAgent.equals(log.getUserAgent()))
                .toList();
        when(repository.findByUserAgent(userAgent)).thenReturn(expectedLogs);

        // When
        List<AuditLog> result = service.getActivityByUserAgent(userAgent);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> userAgent.equals(log.getUserAgent())));
        verify(repository).findByUserAgent(userAgent);
    }

    @Test
    @DisplayName("Should get activity by user agent containing pattern")
    void getActivityByUserAgentContaining_shouldReturnMatchingActivity() {
        // Given
        String userAgentPattern = "Mozilla";
        List<AuditLog> expectedLogs = auditLogs.stream()
                .filter(log -> log.getUserAgent() != null && log.getUserAgent().contains(userAgentPattern))
                .toList();
        when(repository.findByUserAgentContaining(userAgentPattern)).thenReturn(expectedLogs);

        // When
        List<AuditLog> result = service.getActivityByUserAgentContaining(userAgentPattern);

        // Then
        assertNotNull(result);
        verify(repository).findByUserAgentContaining(userAgentPattern);
    }

    @Test
    @DisplayName("Should get activity by ip address and date range")
    void getActivityByIpAddressAndDateRange_shouldReturnFilteredActivity() {
        // Given
        String ipAddress = "192.168.1.1";
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(repository.findByIpAddressAndActionTimestampBetween(ipAddress, startDate, endDate))
                .thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getActivityByIpAddressAndDateRange(ipAddress, startDate, endDate);

        // Then
        assertNotNull(result);
        verify(repository).findByIpAddressAndActionTimestampBetween(ipAddress, startDate, endDate);
    }

    @Test
    @DisplayName("Should get activity by action and date range")
    void getActivityByActionAndDateRange_shouldReturnFilteredActivity() {
        // Given
        String action = "CREATE";
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(repository.findByActionAndActionTimestampBetween(action, startDate, endDate))
                .thenReturn(auditLogListByActionMock());

        // When
        List<AuditLog> result = service.getActivityByActionAndDateRange(action, startDate, endDate);

        // Then
        assertNotNull(result);
        verify(repository).findByActionAndActionTimestampBetween(action, startDate, endDate);
    }

    @Test
    @DisplayName("Should get activity by entity type and action")
    void getActivityByEntityTypeAndAction_shouldReturnFilteredActivity() {
        // Given
        String entityType = "User";
        String action = "CREATE";
        when(repository.findByActionAndEntityType(action, entityType)).thenReturn(auditLogListByActionMock());

        // When
        List<AuditLog> result = service.getActivityByEntityTypeAndAction(entityType, action);

        // Then
        assertNotNull(result);
        verify(repository).findByActionAndEntityType(action, entityType);
    }

    @Test
    @DisplayName("Should get activity by user and action")
    void getActivityByUserAndAction_shouldReturnFilteredActivity() {
        // Given
        Integer userId = 1;
        String action = "CREATE";
        List<AuditLog> expectedLogs = auditLogListByUserMock().stream()
                .filter(log -> action.equals(log.getAction()))
                .toList();
        when(repository.findByUser_IdAndAction(1L, action)).thenReturn(expectedLogs);

        // When
        List<AuditLog> result = service.getActivityByUserAndAction(userId, action);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> action.equals(log.getAction())));
        verify(repository).findByUser_IdAndAction(1L, action);
    }

    @Test
    @DisplayName("Should get activity by user and entity type")
    void getActivityByUserAndEntityType_shouldReturnFilteredActivity() {
        // Given
        Integer userId = 1;
        String entityType = "User";
        List<AuditLog> expectedLogs = auditLogListByUserMock().stream()
                .filter(log -> entityType.equals(log.getEntityType()))
                .toList();
        when(repository.findByUser_IdAndEntityType(1L, entityType)).thenReturn(expectedLogs);

        // When
        List<AuditLog> result = service.getActivityByUserAndEntityType(userId, entityType);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> entityType.equals(log.getEntityType())));
        verify(repository).findByUser_IdAndEntityType(1L, entityType);
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

        // When
        service.bulkDeleteAuditLogsByEntityType(entityType);

        // Then
        verify(repository).deleteByEntityType(entityType);
    }

    @Test
    @DisplayName("Should bulk delete audit logs by user")
    void bulkDeleteAuditLogsByUser_success() {
        // Given
        Integer userId = 1;

        // When
        service.bulkDeleteAuditLogsByUser(userId);

        // Then
        verify(repository).deleteByUser_Id(1L);
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

        // When
        service.bulkDeleteAuditLogsByAction(action);

        // Then
        verify(repository).deleteByAction(action);
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
        when(repository.existsByEntityTypeAndEntityId(entityType, entityId)).thenReturn(true);

        // When
        boolean result = service.existsByEntityTypeAndEntityId(entityType, entityId);

        // Then
        assertTrue(result);
        verify(repository).existsByEntityTypeAndEntityId(entityType, entityId);
    }

    @Test
    @DisplayName("Should check if audit log does not exist by entity type and entity id")
    void existsByEntityTypeAndEntityId_whenNotExists_returnsFalse() {
        // Given
        String entityType = "NonExistent";
        Integer entityId = 999;
        when(repository.existsByEntityTypeAndEntityId(entityType, entityId)).thenReturn(false);

        // When
        boolean result = service.existsByEntityTypeAndEntityId(entityType, entityId);

        // Then
        assertFalse(result);
        verify(repository).existsByEntityTypeAndEntityId(entityType, entityId);
    }

    @Test
    @DisplayName("Should check if audit log exists by user")
    void existsByUser_whenExists_returnsTrue() {
        // Given
        Integer userId = 1;
        when(repository.existsByUser_Id(1L)).thenReturn(true);

        // When
        boolean result = service.existsByUser(userId);

        // Then
        assertTrue(result);
        verify(repository).existsByUser_Id(1L);
    }

    @Test
    @DisplayName("Should check if audit log does not exist by user")
    void existsByUser_whenNotExists_returnsFalse() {
        // Given
        Integer userId = 999;
        when(repository.existsByUser_Id(999L)).thenReturn(false);

        // When
        boolean result = service.existsByUser(userId);

        // Then
        assertFalse(result);
        verify(repository).existsByUser_Id(999L);
    }

    @Test
    @DisplayName("Should check if audit log exists by action")
    void existsByAction_whenExists_returnsTrue() {
        // Given
        String action = "CREATE";
        when(repository.existsByAction(action)).thenReturn(true);

        // When
        boolean result = service.existsByAction(action);

        // Then
        assertTrue(result);
        verify(repository).existsByAction(action);
    }

    @Test
    @DisplayName("Should check if audit log does not exist by action")
    void existsByAction_whenNotExists_returnsFalse() {
        // Given
        String action = "NON_EXISTENT";
        when(repository.existsByAction(action)).thenReturn(false);

        // When
        boolean result = service.existsByAction(action);

        // Then
        assertFalse(result);
        verify(repository).existsByAction(action);
    }

    @Test
    @DisplayName("Should check if audit log exists by ip address")
    void existsByIpAddress_whenExists_returnsTrue() {
        // Given
        String ipAddress = "192.168.1.1";
        when(repository.existsByIpAddress(ipAddress)).thenReturn(true);

        // When
        boolean result = service.existsByIpAddress(ipAddress);

        // Then
        assertTrue(result);
        verify(repository).existsByIpAddress(ipAddress);
    }

    @Test
    @DisplayName("Should check if audit log does not exist by ip address")
    void existsByIpAddress_whenNotExists_returnsFalse() {
        // Given
        String ipAddress = "999.999.999.999";
        when(repository.existsByIpAddress(ipAddress)).thenReturn(false);

        // When
        boolean result = service.existsByIpAddress(ipAddress);

        // Then
        assertFalse(result);
        verify(repository).existsByIpAddress(ipAddress);
    }

    // Count Operations Tests
    @Test
    @DisplayName("Should count audit logs by entity type")
    void countByEntityType_shouldReturnCorrectCount() {
        // Given
        String entityType = "User";
        when(repository.countByEntityType(entityType)).thenReturn(2L);

        // When
        long result = service.countByEntityType(entityType);

        // Then
        assertEquals(2L, result);
        verify(repository).countByEntityType(entityType);
    }

    @Test
    @DisplayName("Should count audit logs by user")
    void countByUser_shouldReturnCorrectCount() {
        // Given
        Integer userId = 1;
        when(repository.countByUser_Id(1L)).thenReturn(2L);

        // When
        long result = service.countByUser(userId);

        // Then
        assertEquals(2, result);
        verify(repository).countByUser_Id(1L);
    }

    @Test
    @DisplayName("Should count audit logs by action")
    void countByAction_shouldReturnCorrectCount() {
        // Given
        String action = "CREATE";
        when(repository.countByAction(action)).thenReturn(2L);

        // When
        long result = service.countByAction(action);

        // Then
        assertEquals(2, result);
        verify(repository).countByAction(action);
    }

    @Test
    @DisplayName("Should count audit logs by date range")
    void countByDateRange_shouldReturnCorrectCount() {
        // Given
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(repository.countByActionTimestampBetween(startDate, endDate)).thenReturn(5L);

        // When
        long result = service.countByDateRange(startDate, endDate);

        // Then
        assertEquals(5, result);
        verify(repository).countByActionTimestampBetween(startDate, endDate);
    }

    @Test
    @DisplayName("Should count audit logs by ip address")
    void countByIpAddress_shouldReturnCorrectCount() {
        // Given
        String ipAddress = "192.168.1.1";
        when(repository.countByIpAddress(ipAddress)).thenReturn(5L);

        // When
        long result = service.countByIpAddress(ipAddress);

        // Then
        assertEquals(5L, result);
        verify(repository).countByIpAddress(ipAddress);
    }

    // Audit Trail Operations Tests
    @Test
    @DisplayName("Should get audit trail for entity")
    void getAuditTrailForEntity_shouldReturnSortedTrail() {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        when(repository.findByEntityTypeAndEntityIdOrderByActionTimestampAsc(entityType, entityId))
                .thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getAuditTrailForEntity(entityType, entityId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findByEntityTypeAndEntityIdOrderByActionTimestampAsc(entityType, entityId);
    }

    @Test
    @DisplayName("Should get audit trail for user")
    void getAuditTrailForUser_shouldReturnSortedTrail() {
        // Given
        Integer userId = 1;
        when(repository.findByUser_IdOrderByActionTimestampAsc(1L)).thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getAuditTrailForUser(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findByUser_IdOrderByActionTimestampAsc(1L);
    }

    @Test
    @DisplayName("Should get audit trail for entity and user")
    void getAuditTrailForEntityAndUser_shouldReturnFilteredTrail() {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        Integer userId = 1;
        when(repository.findByEntityTypeAndEntityIdOrderByActionTimestampAsc(entityType, entityId))
                .thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getAuditTrailForEntityAndUser(entityType, entityId, userId);

        // Then
        assertNotNull(result);
        assertTrue(
                result.stream().allMatch(log -> log.getUser() != null && userId.longValue() == log.getUser().getId()));
        verify(repository).findByEntityTypeAndEntityIdOrderByActionTimestampAsc(entityType, entityId);
    }

    @Test
    @DisplayName("Should get audit trail for entity and action")
    void getAuditTrailForEntityAndAction_shouldReturnFilteredTrail() {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        String action = "CREATE";
        when(repository.findByEntityTypeAndEntityIdOrderByActionTimestampAsc(entityType, entityId))
                .thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getAuditTrailForEntityAndAction(entityType, entityId, action);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> action.equals(log.getAction())));
        verify(repository).findByEntityTypeAndEntityIdOrderByActionTimestampAsc(entityType, entityId);
    }

    @Test
    @DisplayName("Should get audit trail for user and action")
    void getAuditTrailForUserAndAction_shouldReturnFilteredTrail() {
        // Given
        Integer userId = 1;
        String action = "CREATE";
        List<AuditLog> expectedLogs = auditLogListByUserMock().stream()
                .filter(log -> action.equals(log.getAction()))
                .toList();
        when(repository.findByUser_IdAndAction(1L, action)).thenReturn(expectedLogs);

        // When
        List<AuditLog> result = service.getAuditTrailForUserAndAction(userId, action);

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> action.equals(log.getAction())));
        verify(repository).findByUser_IdAndAction(1L, action);
    }

    @Test
    @DisplayName("Should get audit trail for entity and date range")
    void getAuditTrailForEntityAndDateRange_shouldReturnFilteredTrail() {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(repository.findByEntityTypeAndEntityIdAndActionTimestampBetween(entityType, entityId, startDate, endDate))
                .thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getAuditTrailForEntityAndDateRange(entityType, entityId, startDate, endDate);

        // Then
        assertNotNull(result);
        verify(repository).findByEntityTypeAndEntityIdAndActionTimestampBetween(entityType, entityId, startDate, endDate);
    }

    @Test
    @DisplayName("Should get audit trail for user and date range")
    void getAuditTrailForUserAndDateRange_shouldReturnFilteredTrail() {
        // Given
        Integer userId = 1;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(repository.findByUser_IdAndActionTimestampBetween(1L, startDate, endDate))
                .thenReturn(auditLogListByUserMock());

        // When
        List<AuditLog> result = service.getAuditTrailForUserAndDateRange(userId, startDate, endDate);

        // Then
        assertNotNull(result);
        verify(repository).findByUser_IdAndActionTimestampBetween(1L, startDate, endDate);
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
        when(repository.countByEntityType(entityType)).thenReturn(2L);

        // When
        long result = service.getTotalAuditLogsByEntityType(entityType);

        // Then
        assertEquals(2L, result);
        verify(repository).countByEntityType(entityType);
    }

    @Test
    @DisplayName("Should get total audit logs by user")
    void getTotalAuditLogsByUser_shouldReturnCorrectCount() {
        // Given
        Integer userId = 1;
        when(repository.countByUser_Id(1L)).thenReturn(2L);

        // When
        long result = service.getTotalAuditLogsByUser(userId);

        // Then
        assertEquals(2, result);
        verify(repository).countByUser_Id(1L);
    }

    @Test
    @DisplayName("Should get total audit logs by action")
    void getTotalAuditLogsByAction_shouldReturnCorrectCount() {
        // Given
        String action = "CREATE";
        when(repository.countByAction(action)).thenReturn(2L);

        // When
        long result = service.getTotalAuditLogsByAction(action);

        // Then
        assertEquals(2, result);
        verify(repository).countByAction(action);
    }

    @Test
    @DisplayName("Should get total audit logs by date range")
    void getTotalAuditLogsByDateRange_shouldReturnCorrectCount() {
        // Given
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(repository.countByActionTimestampBetween(startDate, endDate)).thenReturn(5L);

        // When
        long result = service.getTotalAuditLogsByDateRange(startDate, endDate);

        // Then
        assertEquals(5, result);
        verify(repository).countByActionTimestampBetween(startDate, endDate);
    }

    @Test
    @DisplayName("Should get total audit logs by ip address")
    void getTotalAuditLogsByIpAddress_shouldReturnCorrectCount() {
        // Given
        String ipAddress = "192.168.1.1";
        when(repository.countByIpAddress(ipAddress)).thenReturn(5L);

        // When
        long result = service.getTotalAuditLogsByIpAddress(ipAddress);

        // Then
        assertEquals(5L, result);
        verify(repository).countByIpAddress(ipAddress);
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
        verify(repository).findAll();
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
        verify(repository).findAll();
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
        verify(repository).findAll();
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
        verify(repository).findAll();
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
        List<AuditLog> expectedLogs = auditLogs.stream()
                .filter(log -> log.getIpAddress() != null && ipAddresses.contains(log.getIpAddress()))
                .toList();
        when(repository.findByIpAddressIn(ipAddresses)).thenReturn(expectedLogs);

        // When
        List<AuditLog> result = service.getActivityByMultipleIpAddresses(ipAddresses);

        // Then
        assertNotNull(result);
        verify(repository).findByIpAddressIn(ipAddresses);
    }

    @Test
    @DisplayName("Should get activity by user agent pattern")
    void getActivityByUserAgentPattern_shouldReturnMatchingActivity() {
        // Given
        String pattern = "Mozilla";
        List<AuditLog> expectedLogs = auditLogs.stream()
                .filter(log -> log.getUserAgent() != null && log.getUserAgent().contains(pattern))
                .toList();
        when(repository.findByUserAgentContaining(pattern)).thenReturn(expectedLogs);

        // When
        List<AuditLog> result = service.getActivityByUserAgentPattern(pattern);

        // Then
        assertNotNull(result);
        verify(repository).findByUserAgentContaining(pattern);
    }

    @Test
    @DisplayName("Should get failed login attempts")
    void getFailedLoginAttempts_shouldReturnFailedLogins() {
        // Given
        List<AuditLog> failedLogs = auditLogListSecurityMock().stream()
                .filter(log -> AuditAction.getFailedLoginActions().contains(log.getAction()))
                .toList();
        when(repository.findByActionIn(AuditAction.getFailedLoginActions())).thenReturn(failedLogs);

        // When
        List<AuditLog> result = service.getFailedLoginAttempts();

        // Then
        assertNotNull(result);
        verify(repository).findByActionIn(AuditAction.getFailedLoginActions());
    }

    @Test
    @DisplayName("Should get successful login attempts")
    void getSuccessfulLoginAttempts_shouldReturnSuccessfulLogins() {
        // Given
        List<AuditLog> successLogs = auditLogs.stream()
                .filter(log -> AuditAction.getSuccessfulLoginActions().contains(log.getAction()))
                .toList();
        when(repository.findByActionIn(AuditAction.getSuccessfulLoginActions())).thenReturn(successLogs);

        // When
        List<AuditLog> result = service.getSuccessfulLoginAttempts();

        // Then
        assertNotNull(result);
        verify(repository).findByActionIn(AuditAction.getSuccessfulLoginActions());
    }

    @Test
    @DisplayName("Should get data modification activity")
    void getDataModificationActivity_shouldReturnModificationLogs() {
        // Given
        when(repository.findByActionIn(AuditAction.getDataModificationActions())).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getDataModificationActivity();

        // Then
        assertNotNull(result);
        verify(repository).findByActionIn(AuditAction.getDataModificationActions());
    }

    @Test
    @DisplayName("Should get data access activity")
    void getDataAccessActivity_shouldReturnAccessLogs() {
        // Given
        when(repository.findByActionIn(AuditAction.getDataAccessActions())).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getDataAccessActivity();

        // Then
        assertNotNull(result);
        verify(repository).findByActionIn(AuditAction.getDataAccessActions());
    }

    @Test
    @DisplayName("Should get data deletion activity")
    void getDataDeletionActivity_shouldReturnDeletionLogs() {
        // Given
        when(repository.findByActionIn(AuditAction.getDataDeletionActions())).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.getDataDeletionActivity();

        // Then
        assertNotNull(result);
        verify(repository).findByActionIn(AuditAction.getDataDeletionActions());
    }

    // Data Integrity Operations Tests
    @Test
    @DisplayName("Should get audit logs with missing user")
    void getAuditLogsWithMissingUser_shouldReturnLogsWithoutUser() {
        // Given
        List<AuditLog> logsWithNullUser = auditLogListWithNullsMock().stream()
                .filter(log -> log.getUser() == null)
                .toList();
        when(repository.findByUserIsNull()).thenReturn(logsWithNullUser);

        // When
        List<AuditLog> result = service.getAuditLogsWithMissingUser();

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> log.getUser() == null));
        verify(repository).findByUserIsNull();
    }

    @Test
    @DisplayName("Should get audit logs with missing ip address")
    void getAuditLogsWithMissingIpAddress_shouldReturnLogsWithoutIp() {
        // Given
        List<AuditLog> logsWithNullIp = auditLogListWithNullsMock().stream()
                .filter(log -> log.getIpAddress() == null || log.getIpAddress().trim().isEmpty())
                .toList();
        when(repository.findByIpAddressIsNullOrEmpty()).thenReturn(logsWithNullIp);

        // When
        List<AuditLog> result = service.getAuditLogsWithMissingIpAddress();

        // Then
        assertNotNull(result);
        assertTrue(result.stream().allMatch(log -> log.getIpAddress() == null || log.getIpAddress().trim().isEmpty()));
        verify(repository).findByIpAddressIsNullOrEmpty();
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
        List<AuditLog> logsWithEmptyOld = auditLogListWithNullsMock().stream()
                .filter(log -> log.getOldValues() == null || log.getOldValues().trim().isEmpty())
                .toList();
        when(repository.findByOldValuesIsNullOrEmpty()).thenReturn(logsWithEmptyOld);

        // When
        List<AuditLog> result = service.getAuditLogsWithEmptyOldValues();

        // Then
        assertNotNull(result);
        verify(repository).findByOldValuesIsNullOrEmpty();
    }

    @Test
    @DisplayName("Should get audit logs with empty new values")
    void getAuditLogsWithEmptyNewValues_shouldReturnLogsWithEmptyNewValues() {
        // Given
        List<AuditLog> logsWithEmptyNew = auditLogListWithNullsMock().stream()
                .filter(log -> log.getNewValues() == null || log.getNewValues().trim().isEmpty())
                .toList();
        when(repository.findByNewValuesIsNullOrEmpty()).thenReturn(logsWithEmptyNew);

        // When
        List<AuditLog> result = service.getAuditLogsWithEmptyNewValues();

        // Then
        assertNotNull(result);
        verify(repository).findByNewValuesIsNullOrEmpty();
    }

    @Test
    @DisplayName("Should get audit logs with both empty values")
    void getAuditLogsWithBothEmptyValues_shouldReturnLogsWithBothEmptyValues() {
        // Given
        List<AuditLog> logsWithBothEmpty = auditLogListWithNullsMock().stream()
                .filter(log -> (log.getOldValues() == null || log.getOldValues().trim().isEmpty()) &&
                        (log.getNewValues() == null || log.getNewValues().trim().isEmpty()))
                .toList();
        when(repository.findByBothValuesNullOrEmpty()).thenReturn(logsWithBothEmpty);

        // When
        List<AuditLog> result = service.getAuditLogsWithBothEmptyValues();

        // Then
        assertNotNull(result);
        verify(repository).findByBothValuesNullOrEmpty();
    }

    // Utility Operations Tests
    @Test
    @DisplayName("Should cleanup old audit logs")
    void cleanupOldAuditLogs_shouldDeleteOldLogs() {
        // Given
        int daysToKeep = 30;

        // When
        service.cleanupOldAuditLogs(daysToKeep);

        // Then
        verify(repository).deleteByActionTimestampBefore(any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Should delete old audit logs")
    void deleteOldAuditLogs_shouldDeleteOldLogs() {
        // Given
        LocalDateTime beforeDate = LocalDateTime.now().minusDays(30);

        // When
        service.deleteOldAuditLogs(beforeDate);

        // Then
        verify(repository).deleteByActionTimestampBefore(beforeDate);
    }

    @Test
    @DisplayName("Should search audit logs by keyword")
    void searchAuditLogsByKeyword_shouldReturnMatchingLogs() {
        // Given
        String keyword = "User";
        when(repository.searchByKeyword(keyword)).thenReturn(auditLogs);

        // When
        List<AuditLog> result = service.searchAuditLogsByKeyword(keyword);

        // Then
        assertNotNull(result);
        verify(repository).searchByKeyword(keyword);
    }

    @Test
    @DisplayName("Should search audit logs by user agent")
    void searchAuditLogsByUserAgent_shouldReturnMatchingLogs() {
        // Given
        String userAgent = "Mozilla/5.0";
        List<AuditLog> expectedLogs = auditLogs.stream()
                .filter(log -> userAgent.equals(log.getUserAgent()))
                .toList();
        when(repository.findByUserAgent(userAgent)).thenReturn(expectedLogs);

        // When
        List<AuditLog> result = service.searchAuditLogsByUserAgent(userAgent);

        // Then
        assertNotNull(result);
        verify(repository).findByUserAgent(userAgent);
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
        List<AuditLog> logsWithJson = auditLogs.stream()
                .filter(log -> (log.getOldValues() != null && !log.getOldValues().trim().isEmpty()) ||
                        (log.getNewValues() != null && !log.getNewValues().trim().isEmpty()))
                .toList();
        when(repository.findByOldValuesOrNewValuesNotEmpty()).thenReturn(logsWithJson);

        // When
        List<AuditLog> result = service.getAuditLogsWithJsonData();

        // Then
        assertNotNull(result);
        verify(repository).findByOldValuesOrNewValuesNotEmpty();
    }

    @Test
    @DisplayName("Should get audit logs without json data")
    void getAuditLogsWithoutJsonData_shouldReturnLogsWithoutJson() {
        // Given
        List<AuditLog> logsWithoutJson = auditLogs.stream()
                .filter(log -> (log.getOldValues() == null || log.getOldValues().trim().isEmpty()) &&
                        (log.getNewValues() == null || log.getNewValues().trim().isEmpty()))
                .toList();
        when(repository.findByOldValuesAndNewValuesNullOrEmpty()).thenReturn(logsWithoutJson);

        // When
        List<AuditLog> result = service.getAuditLogsWithoutJsonData();

        // Then
        assertNotNull(result);
        verify(repository).findByOldValuesAndNewValuesNullOrEmpty();
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
