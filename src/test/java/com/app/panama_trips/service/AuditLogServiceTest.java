package com.app.panama_trips.service;

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
}