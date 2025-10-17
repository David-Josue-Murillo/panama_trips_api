package com.app.panama_trips.presentation.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.app.panama_trips.persistence.entity.AuditLog;
import com.app.panama_trips.service.implementation.AuditLogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static com.app.panama_trips.DataProvider.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuditLogController.class)
public class AuditLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuditLogService service;

    private AuditLog auditLog;
    private List<AuditLog> auditLogs;

    @BeforeEach
    void setUp() {
        auditLog = auditLogOneMock();
        auditLogs = auditLogListMock();
    }

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(obj);
    }

    // CRUD Operations Tests
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should return all audit logs with pagination when getAll is called")
    void getAll_success() throws Exception {
        // Given
        Page<AuditLog> page = new PageImpl<>(auditLogs);
        when(service.getAllAuditLogs(any(Pageable.class))).thenReturn(page);

        // When/Then
        mockMvc.perform(get("/api/audit-logs")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(auditLogs.getFirst().getId()));

        verify(service).getAllAuditLogs(any(Pageable.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should return audit log by id when getById is called")
    void getById_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.getAuditLogById(id)).thenReturn(auditLog);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(auditLog.getId()));

        verify(service).getAuditLogById(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should create audit log when create is called")
    void create_success() throws Exception {
        // Given
        when(service.saveAuditLog(any(AuditLog.class))).thenReturn(auditLog);

        // When/Then
        mockMvc.perform(post("/api/audit-logs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(auditLog))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(auditLog.getId()));

        verify(service).saveAuditLog(any(AuditLog.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should update audit log when update is called")
    void update_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.updateAuditLog(eq(id), any(AuditLog.class))).thenReturn(auditLog);

        // When/Then
        mockMvc.perform(put("/api/audit-logs/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(auditLog))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(auditLog.getId()));

        verify(service).updateAuditLog(eq(id), any(AuditLog.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should delete audit log when delete is called")
    void delete_success() throws Exception {
        // Given
        Integer id = 1;
        doNothing().when(service).deleteAuditLog(id);

        // When/Then
        mockMvc.perform(delete("/api/audit-logs/{id}", id)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service).deleteAuditLog(id);
    }

    // Search Operations Tests
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find audit logs by entity when findByEntity is called")
    void findByEntity_success() throws Exception {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        when(service.findByEntityTypeAndEntityId(entityType, entityId)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/by-entity")
                .param("entityType", entityType)
                .param("entityId", entityId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).findByEntityTypeAndEntityId(entityType, entityId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find audit logs by user when findByUser is called")
    void findByUser_success() throws Exception {
        // Given
        Integer userId = 1;
        when(service.findByUserId(userId)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/by-user")
                .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).findByUserId(userId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find audit logs by action when findByAction is called")
    void findByAction_success() throws Exception {
        // Given
        String action = "CREATE";
        when(service.findByAction(action)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/by-action")
                .param("action", action))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).findByAction(action);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find audit logs by ip when findByIp is called")
    void findByIp_success() throws Exception {
        // Given
        String ip = "192.168.1.1";
        when(service.findByIpAddress(ip)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/by-ip")
                .param("ip", ip))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).findByIpAddress(ip);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find audit logs by date range when findByDateRange is called")
    void findByDateRange_success() throws Exception {
        // Given
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        when(service.findByActionTimestampBetween(start, end)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/by-date-range")
                .param("start", start.toString())
                .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).findByActionTimestampBetween(start, end);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get recent audit logs when getRecent is called")
    void getRecent_success() throws Exception {
        // Given
        int limit = 10;
        when(service.getRecentActivity(limit)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/recent")
                .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getRecentActivity(limit);
    }

    // Entity Type and Entity ID Operations Tests
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find audit logs by entity type when findByEntityType is called")
    void findByEntityType_success() throws Exception {
        // Given
        String entityType = "User";
        when(service.findByEntityType(entityType)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/entity-type/{entityType}", entityType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).findByEntityType(entityType);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find audit logs by entity id when findByEntityId is called")
    void findByEntityId_success() throws Exception {
        // Given
        Integer entityId = 1;
        when(service.findByEntityId(entityId)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/entity-id/{entityId}", entityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).findByEntityId(entityId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find audit logs by timestamp after when findByTimestampAfter is called")
    void findByTimestampAfter_success() throws Exception {
        // Given
        LocalDateTime timestamp = LocalDateTime.now().minusDays(1);
        when(service.findByTimestampAfter(timestamp)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/timestamp-after/{timestamp}", timestamp))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).findByTimestampAfter(timestamp);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find audit logs by timestamp before when findByTimestampBefore is called")
    void findByTimestampBefore_success() throws Exception {
        // Given
        LocalDateTime timestamp = LocalDateTime.now().plusDays(1);
        when(service.findByTimestampBefore(timestamp)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/timestamp-before/{timestamp}", timestamp))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).findByTimestampBefore(timestamp);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find audit logs by user agent when findByUserAgent is called")
    void findByUserAgent_success() throws Exception {
        // Given
        String userAgent = "Mozilla";
        when(service.getActivityByUserAgent(userAgent)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/user-agent/{userAgent}", userAgent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getActivityByUserAgent(userAgent);
    }

    // Specialized Queries Tests
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find audit logs by user and action when findByUserAndAction is called")
    void findByUserAndAction_success() throws Exception {
        // Given
        Integer userId = 1;
        String action = "CREATE";
        when(service.getActivityByUserAndAction(userId, action)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/user/{userId}/action/{action}", userId, action))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getActivityByUserAndAction(userId, action);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find audit logs by user and entity type when findByUserAndEntityType is called")
    void findByUserAndEntityType_success() throws Exception {
        // Given
        Integer userId = 1;
        String entityType = "User";
        when(service.getActivityByUserAndEntityType(userId, entityType)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/user/{userId}/entity-type/{entityType}", userId, entityType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getActivityByUserAndEntityType(userId, entityType);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find audit logs by action and entity type when findByActionAndEntityType is called")
    void findByActionAndEntityType_success() throws Exception {
        // Given
        String action = "CREATE";
        String entityType = "User";
        when(service.getActivityByEntityTypeAndAction(entityType, action)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/action/{action}/entity-type/{entityType}", action, entityType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getActivityByEntityTypeAndAction(entityType, action);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find recent activity by entity type when findRecentActivityByEntityType is called")
    void findRecentActivityByEntityType_success() throws Exception {
        // Given
        String entityType = "User";
        int limit = 10;
        when(service.getRecentActivityByEntityType(entityType, limit)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/recent-activity/{entityType}", entityType)
                .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getRecentActivityByEntityType(entityType, limit);
    }
}