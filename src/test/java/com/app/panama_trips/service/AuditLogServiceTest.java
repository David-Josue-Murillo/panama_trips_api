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
}