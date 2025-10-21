package com.app.panama_trips.presentation.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    // Business Logic Operations Tests
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get activity by date range when getActivityByDateRange is called")
    void getActivityByDateRange_success() throws Exception {
        // Given
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(service.getActivityByDateRange(startDate, endDate)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/activity/date-range")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getActivityByDateRange(startDate, endDate);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get activity by user when getActivityByUser is called")
    void getActivityByUser_success() throws Exception {
        // Given
        Integer userId = 1;
        when(service.getActivityByUser(userId)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/activity/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getActivityByUser(userId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get activity by entity type when getActivityByEntityType is called")
    void getActivityByEntityType_success() throws Exception {
        // Given
        String entityType = "User";
        when(service.getActivityByEntityType(entityType)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/activity/entity-type/{entityType}", entityType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getActivityByEntityType(entityType);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get activity by action when getActivityByAction is called")
    void getActivityByAction_success() throws Exception {
        // Given
        String action = "CREATE";
        when(service.getActivityByAction(action)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/activity/action/{action}", action))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getActivityByAction(action);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get activity by ip address when getActivityByIpAddress is called")
    void getActivityByIpAddress_success() throws Exception {
        // Given
        String ipAddress = "192.168.1.1";
        when(service.getActivityByIpAddress(ipAddress)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/activity/ip/{ipAddress}", ipAddress))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getActivityByIpAddress(ipAddress);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get activity by user and date range when getActivityByUserAndDateRange is called")
    void getActivityByUserAndDateRange_success() throws Exception {
        // Given
        Integer userId = 1;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(service.getActivityByUserAndDateRange(userId, startDate, endDate)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/activity/user/{userId}/date-range", userId)
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getActivityByUserAndDateRange(userId, startDate, endDate);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get activity by entity and date range when getActivityByEntityAndDateRange is called")
    void getActivityByEntityAndDateRange_success() throws Exception {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(service.getActivityByEntityAndDateRange(entityType, entityId, startDate, endDate)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/activity/entity/{entityType}/{entityId}/date-range", entityType, entityId)
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getActivityByEntityAndDateRange(entityType, entityId, startDate, endDate);
    }

    // Advanced Queries Tests
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get activity by user agent containing when getActivityByUserAgentContaining is called")
    void getActivityByUserAgentContaining_success() throws Exception {
        // Given
        String pattern = "Mozilla";
        when(service.getActivityByUserAgentContaining(pattern)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/user-agent/containing/{pattern}", pattern))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getActivityByUserAgentContaining(pattern);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get activity by ip address and date range when getActivityByIpAddressAndDateRange is called")
    void getActivityByIpAddressAndDateRange_success() throws Exception {
        // Given
        String ipAddress = "192.168.1.1";
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(service.getActivityByIpAddressAndDateRange(ipAddress, startDate, endDate)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/ip/{ipAddress}/date-range", ipAddress)
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getActivityByIpAddressAndDateRange(ipAddress, startDate, endDate);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get activity by action and date range when getActivityByActionAndDateRange is called")
    void getActivityByActionAndDateRange_success() throws Exception {
        // Given
        String action = "CREATE";
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(service.getActivityByActionAndDateRange(action, startDate, endDate)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/action/{action}/date-range", action)
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getActivityByActionAndDateRange(action, startDate, endDate);
    }

    // Bulk Operations Tests
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should create multiple audit logs in bulk when bulkCreate is called")
    void bulkCreate_success() throws Exception {
        // Given
        List<AuditLog> auditLogsToCreate = Collections.singletonList(auditLog);
        doNothing().when(service).bulkCreateAuditLogs(anyList());

        // When/Then
        mockMvc.perform(post("/api/audit-logs/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(auditLogsToCreate))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated());

        verify(service).bulkCreateAuditLogs(anyList());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should delete multiple audit logs in bulk when bulkDelete is called")
    void bulkDelete_success() throws Exception {
        // Given
        List<Integer> auditLogIds = Collections.singletonList(1);
        doNothing().when(service).bulkDeleteAuditLogs(anyList());

        // When/Then
        mockMvc.perform(delete("/api/audit-logs/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(auditLogIds))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service).bulkDeleteAuditLogs(anyList());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should delete audit logs by entity type in bulk when bulkDeleteByEntityType is called")
    void bulkDeleteByEntityType_success() throws Exception {
        // Given
        String entityType = "User";
        doNothing().when(service).bulkDeleteAuditLogsByEntityType(entityType);

        // When/Then
        mockMvc.perform(delete("/api/audit-logs/bulk/entity-type/{entityType}", entityType)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service).bulkDeleteAuditLogsByEntityType(entityType);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should delete audit logs by user in bulk when bulkDeleteByUser is called")
    void bulkDeleteByUser_success() throws Exception {
        // Given
        Integer userId = 1;
        doNothing().when(service).bulkDeleteAuditLogsByUser(userId);

        // When/Then
        mockMvc.perform(delete("/api/audit-logs/bulk/user/{userId}", userId)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service).bulkDeleteAuditLogsByUser(userId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should delete audit logs by date range in bulk when bulkDeleteByDateRange is called")
    void bulkDeleteByDateRange_success() throws Exception {
        // Given
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        doNothing().when(service).bulkDeleteAuditLogsByDateRange(startDate, endDate);

        // When/Then
        mockMvc.perform(delete("/api/audit-logs/bulk/date-range")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service).bulkDeleteAuditLogsByDateRange(startDate, endDate);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should delete audit logs by action in bulk when bulkDeleteByAction is called")
    void bulkDeleteByAction_success() throws Exception {
        // Given
        String action = "CREATE";
        doNothing().when(service).bulkDeleteAuditLogsByAction(action);

        // When/Then
        mockMvc.perform(delete("/api/audit-logs/bulk/action/{action}", action)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service).bulkDeleteAuditLogsByAction(action);
    }

    // Check Operations Tests
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should check if audit log exists by id when existsById is called")
    void existsById_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.existsById(id)).thenReturn(true);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/exists/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).existsById(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should check if audit log exists by entity type and entity id when existsByEntityTypeAndEntityId is called")
    void existsByEntityTypeAndEntityId_success() throws Exception {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        when(service.existsByEntityTypeAndEntityId(entityType, entityId)).thenReturn(true);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/exists/entity/{entityType}/{entityId}", entityType, entityId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).existsByEntityTypeAndEntityId(entityType, entityId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should check if audit log exists by user when existsByUser is called")
    void existsByUser_success() throws Exception {
        // Given
        Integer userId = 1;
        when(service.existsByUser(userId)).thenReturn(true);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/exists/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).existsByUser(userId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should check if audit log exists by action when existsByAction is called")
    void existsByAction_success() throws Exception {
        // Given
        String action = "CREATE";
        when(service.existsByAction(action)).thenReturn(true);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/exists/action/{action}", action))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).existsByAction(action);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should check if audit log exists by ip address when existsByIpAddress is called")
    void existsByIpAddress_success() throws Exception {
        // Given
        String ipAddress = "192.168.1.1";
        when(service.existsByIpAddress(ipAddress)).thenReturn(true);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/exists/ip/{ipAddress}", ipAddress))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).existsByIpAddress(ipAddress);
    }

    // Count Operations Tests
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should count audit logs by entity type when countByEntityType is called")
    void countByEntityType_success() throws Exception {
        // Given
        String entityType = "User";
        Long expectedCount = 5L;
        when(service.countByEntityType(entityType)).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/count/entity-type/{entityType}", entityType))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).countByEntityType(entityType);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should count audit logs by user when countByUser is called")
    void countByUser_success() throws Exception {
        // Given
        Integer userId = 1;
        Long expectedCount = 10L;
        when(service.countByUser(userId)).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/count/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).countByUser(userId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should count audit logs by action when countByAction is called")
    void countByAction_success() throws Exception {
        // Given
        String action = "CREATE";
        Long expectedCount = 15L;
        when(service.countByAction(action)).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/count/action/{action}", action))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).countByAction(action);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should count audit logs by date range when countByDateRange is called")
    void countByDateRange_success() throws Exception {
        // Given
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        Long expectedCount = 20L;
        when(service.countByDateRange(startDate, endDate)).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/count/date-range")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).countByDateRange(startDate, endDate);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should count audit logs by ip address when countByIpAddress is called")
    void countByIpAddress_success() throws Exception {
        // Given
        String ipAddress = "192.168.1.1";
        Long expectedCount = 8L;
        when(service.countByIpAddress(ipAddress)).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/count/ip/{ipAddress}", ipAddress))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).countByIpAddress(ipAddress);
    }

    // Audit Trail Operations Tests
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get audit trail for entity when getAuditTrailForEntity is called")
    void getAuditTrailForEntity_success() throws Exception {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        when(service.getAuditTrailForEntity(entityType, entityId)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/audit-trail/entity/{entityType}/{entityId}", entityType, entityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getAuditTrailForEntity(entityType, entityId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get audit trail for user when getAuditTrailForUser is called")
    void getAuditTrailForUser_success() throws Exception {
        // Given
        Integer userId = 1;
        when(service.getAuditTrailForUser(userId)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/audit-trail/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getAuditTrailForUser(userId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get audit trail for entity and user when getAuditTrailForEntityAndUser is called")
    void getAuditTrailForEntityAndUser_success() throws Exception {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        Integer userId = 1;
        when(service.getAuditTrailForEntityAndUser(entityType, entityId, userId)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/audit-trail/entity/{entityType}/{entityId}/user/{userId}", entityType,
                entityId, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getAuditTrailForEntityAndUser(entityType, entityId, userId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get audit trail for entity and action when getAuditTrailForEntityAndAction is called")
    void getAuditTrailForEntityAndAction_success() throws Exception {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        String action = "CREATE";
        when(service.getAuditTrailForEntityAndAction(entityType, entityId, action)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/audit-trail/entity/{entityType}/{entityId}/action/{action}", entityType,
                entityId, action))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getAuditTrailForEntityAndAction(entityType, entityId, action);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get audit trail for user and action when getAuditTrailForUserAndAction is called")
    void getAuditTrailForUserAndAction_success() throws Exception {
        // Given
        Integer userId = 1;
        String action = "CREATE";
        when(service.getAuditTrailForUserAndAction(userId, action)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/audit-trail/user/{userId}/action/{action}", userId, action))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getAuditTrailForUserAndAction(userId, action);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get audit trail for entity and date range when getAuditTrailForEntityAndDateRange is called")
    void getAuditTrailForEntityAndDateRange_success() throws Exception {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(service.getAuditTrailForEntityAndDateRange(entityType, entityId, startDate, endDate))
                .thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(
                get("/api/audit-logs/audit-trail/entity/{entityType}/{entityId}/date-range", entityType, entityId)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getAuditTrailForEntityAndDateRange(entityType, entityId, startDate, endDate);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get audit trail for user and date range when getAuditTrailForUserAndDateRange is called")
    void getAuditTrailForUserAndDateRange_success() throws Exception {
        // Given
        Integer userId = 1;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        when(service.getAuditTrailForUserAndDateRange(userId, startDate, endDate)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/audit-trail/user/{userId}/date-range", userId)
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getAuditTrailForUserAndDateRange(userId, startDate, endDate);
    }

    // Statistics and Analytics Tests
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get total audit logs when getTotalAuditLogs is called")
    void getTotalAuditLogs_success() throws Exception {
        // Given
        Long expectedCount = 100L;
        when(service.getTotalAuditLogs()).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/stats/total"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).getTotalAuditLogs();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get total audit logs by entity type when getTotalAuditLogsByEntityType is called")
    void getTotalAuditLogsByEntityType_success() throws Exception {
        // Given
        String entityType = "User";
        Long expectedCount = 30L;
        when(service.getTotalAuditLogsByEntityType(entityType)).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/stats/entity-type/{entityType}", entityType))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).getTotalAuditLogsByEntityType(entityType);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get total audit logs by user when getTotalAuditLogsByUser is called")
    void getTotalAuditLogsByUser_success() throws Exception {
        // Given
        Integer userId = 1;
        Long expectedCount = 25L;
        when(service.getTotalAuditLogsByUser(userId)).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/stats/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).getTotalAuditLogsByUser(userId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get total audit logs by action when getTotalAuditLogsByAction is called")
    void getTotalAuditLogsByAction_success() throws Exception {
        // Given
        String action = "CREATE";
        Long expectedCount = 40L;
        when(service.getTotalAuditLogsByAction(action)).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/stats/action/{action}", action))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).getTotalAuditLogsByAction(action);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get total audit logs by date range when getTotalAuditLogsByDateRange is called")
    void getTotalAuditLogsByDateRange_success() throws Exception {
        // Given
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);
        Long expectedCount = 50L;
        when(service.getTotalAuditLogsByDateRange(startDate, endDate)).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/stats/date-range")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).getTotalAuditLogsByDateRange(startDate, endDate);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get total audit logs by ip address when getTotalAuditLogsByIpAddress is called")
    void getTotalAuditLogsByIpAddress_success() throws Exception {
        // Given
        String ipAddress = "192.168.1.1";
        Long expectedCount = 15L;
        when(service.getTotalAuditLogsByIpAddress(ipAddress)).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/stats/ip/{ipAddress}", ipAddress))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).getTotalAuditLogsByIpAddress(ipAddress);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get top users by activity when getTopUsersByActivity is called")
    void getTopUsersByActivity_success() throws Exception {
        // Given
        int limit = 10;
        when(service.getTopUsersByActivity(limit)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/stats/top-users/{limit}", limit))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getTopUsersByActivity(limit);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get top entity types by activity when getTopEntityTypesByActivity is called")
    void getTopEntityTypesByActivity_success() throws Exception {
        // Given
        int limit = 5;
        when(service.getTopEntityTypesByActivity(limit)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/stats/top-entity-types/{limit}", limit))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getTopEntityTypesByActivity(limit);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get top actions by activity when getTopActionsByActivity is called")
    void getTopActionsByActivity_success() throws Exception {
        // Given
        int limit = 8;
        when(service.getTopActionsByActivity(limit)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/stats/top-actions/{limit}", limit))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getTopActionsByActivity(limit);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get top ip addresses by activity when getTopIpAddressesByActivity is called")
    void getTopIpAddressesByActivity_success() throws Exception {
        // Given
        int limit = 6;
        when(service.getTopIpAddressesByActivity(limit)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/stats/top-ips/{limit}", limit))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getTopIpAddressesByActivity(limit);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get activity by month when getActivityByMonth is called")
    void getActivityByMonth_success() throws Exception {
        // Given
        when(service.getActivityByMonth()).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/stats/by-month"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getActivityByMonth();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get activity by day of week when getActivityByDayOfWeek is called")
    void getActivityByDayOfWeek_success() throws Exception {
        // Given
        when(service.getActivityByDayOfWeek()).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/stats/by-day-of-week"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getActivityByDayOfWeek();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get activity by hour when getActivityByHour is called")
    void getActivityByHour_success() throws Exception {
        // Given
        when(service.getActivityByHour()).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/stats/by-hour"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getActivityByHour();
    }

    // Security and Monitoring Operations Tests
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get suspicious activity when getSuspiciousActivity is called")
    void getSuspiciousActivity_success() throws Exception {
        // Given
        String ipAddress = "192.168.1.100";
        when(service.getSuspiciousActivity(ipAddress)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/security/suspicious/{ipAddress}", ipAddress))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getSuspiciousActivity(ipAddress);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get activity by multiple ip addresses when getActivityByMultipleIpAddresses is called")
    void getActivityByMultipleIpAddresses_success() throws Exception {
        // Given
        List<String> ipAddresses = List.of("192.168.1.1", "192.168.1.2");
        when(service.getActivityByMultipleIpAddresses(ipAddresses)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(post("/api/audit-logs/security/multiple-ips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(ipAddresses))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getActivityByMultipleIpAddresses(ipAddresses);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get activity by user agent pattern when getActivityByUserAgentPattern is called")
    void getActivityByUserAgentPattern_success() throws Exception {
        // Given
        String pattern = "Mozilla";
        when(service.getActivityByUserAgentPattern(pattern)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/security/user-agent-pattern/{pattern}", pattern))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getActivityByUserAgentPattern(pattern);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get failed login attempts when getFailedLoginAttempts is called")
    void getFailedLoginAttempts_success() throws Exception {
        // Given
        when(service.getFailedLoginAttempts()).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/security/failed-logins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getFailedLoginAttempts();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get successful login attempts when getSuccessfulLoginAttempts is called")
    void getSuccessfulLoginAttempts_success() throws Exception {
        // Given
        when(service.getSuccessfulLoginAttempts()).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/security/successful-logins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getSuccessfulLoginAttempts();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get data modification activity when getDataModificationActivity is called")
    void getDataModificationActivity_success() throws Exception {
        // Given
        when(service.getDataModificationActivity()).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/security/data-modification"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getDataModificationActivity();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get data access activity when getDataAccessActivity is called")
    void getDataAccessActivity_success() throws Exception {
        // Given
        when(service.getDataAccessActivity()).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/security/data-access"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getDataAccessActivity();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get data deletion activity when getDataDeletionActivity is called")
    void getDataDeletionActivity_success() throws Exception {
        // Given
        when(service.getDataDeletionActivity()).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/security/data-deletion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getDataDeletionActivity();
    }

    // Data Integrity Operations Tests
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get audit logs with missing user when getAuditLogsWithMissingUser is called")
    void getAuditLogsWithMissingUser_success() throws Exception {
        // Given
        when(service.getAuditLogsWithMissingUser()).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/integrity/missing-user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getAuditLogsWithMissingUser();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get audit logs with missing ip when getAuditLogsWithMissingIpAddress is called")
    void getAuditLogsWithMissingIpAddress_success() throws Exception {
        // Given
        when(service.getAuditLogsWithMissingIpAddress()).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/integrity/missing-ip"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getAuditLogsWithMissingIpAddress();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get audit logs with invalid json when getAuditLogsWithInvalidJsonData is called")
    void getAuditLogsWithInvalidJsonData_success() throws Exception {
        // Given
        when(service.getAuditLogsWithInvalidJsonData()).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/integrity/invalid-json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getAuditLogsWithInvalidJsonData();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get audit logs with empty old values when getAuditLogsWithEmptyOldValues is called")
    void getAuditLogsWithEmptyOldValues_success() throws Exception {
        // Given
        when(service.getAuditLogsWithEmptyOldValues()).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/integrity/empty-old-values"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getAuditLogsWithEmptyOldValues();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get audit logs with empty new values when getAuditLogsWithEmptyNewValues is called")
    void getAuditLogsWithEmptyNewValues_success() throws Exception {
        // Given
        when(service.getAuditLogsWithEmptyNewValues()).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/integrity/empty-new-values"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getAuditLogsWithEmptyNewValues();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get audit logs with both empty values when getAuditLogsWithBothEmptyValues is called")
    void getAuditLogsWithBothEmptyValues_success() throws Exception {
        // Given
        when(service.getAuditLogsWithBothEmptyValues()).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/integrity/empty-both-values"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getAuditLogsWithBothEmptyValues();
    }

    // Utility Operations Tests
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should cleanup old audit logs when cleanupOldAuditLogs is called")
    void cleanupOldAuditLogs_success() throws Exception {
        // Given
        int daysToKeep = 30;
        doNothing().when(service).cleanupOldAuditLogs(daysToKeep);

        // When/Then
        mockMvc.perform(post("/api/audit-logs/cleanup/{daysToKeep}", daysToKeep)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(service).cleanupOldAuditLogs(daysToKeep);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should archive audit logs when archiveAuditLogs is called")
    void archiveAuditLogs_success() throws Exception {
        // Given
        LocalDateTime beforeDate = LocalDateTime.now().minusDays(30);
        doNothing().when(service).archiveAuditLogs(beforeDate);

        // When/Then
        mockMvc.perform(post("/api/audit-logs/archive/{beforeDate}", beforeDate)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(service).archiveAuditLogs(beforeDate);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should search audit logs by keyword when searchAuditLogsByKeyword is called")
    void searchAuditLogsByKeyword_success() throws Exception {
        // Given
        String keyword = "User";
        when(service.searchAuditLogsByKeyword(keyword)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/search/keyword/{keyword}", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).searchAuditLogsByKeyword(keyword);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should search audit logs by user agent when searchAuditLogsByUserAgent is called")
    void searchAuditLogsByUserAgent_success() throws Exception {
        // Given
        String userAgent = "Mozilla";
        when(service.searchAuditLogsByUserAgent(userAgent)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/search/user-agent/{userAgent}", userAgent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).searchAuditLogsByUserAgent(userAgent);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find latest audit log by entity when found")
    void findLatestAuditLogByEntity_whenFound_success() throws Exception {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        when(service.findLatestAuditLogByEntity(entityType, entityId)).thenReturn(Optional.of(auditLog));

        // When/Then
        mockMvc.perform(get("/api/audit-logs/latest/entity/{entityType}/{entityId}", entityType, entityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(auditLog.getId()));

        verify(service).findLatestAuditLogByEntity(entityType, entityId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should return 404 when latest audit log by entity not found")
    void findLatestAuditLogByEntity_whenNotFound_returns404() throws Exception {
        // Given
        String entityType = "User";
        Integer entityId = 1;
        when(service.findLatestAuditLogByEntity(entityType, entityId)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/audit-logs/latest/entity/{entityType}/{entityId}", entityType, entityId))
                .andExpect(status().isNotFound());

        verify(service).findLatestAuditLogByEntity(entityType, entityId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get audit logs with json data when getAuditLogsWithJsonData is called")
    void getAuditLogsWithJsonData_success() throws Exception {
        // Given
        when(service.getAuditLogsWithJsonData()).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/with-json-data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getAuditLogsWithJsonData();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get audit logs without json data when getAuditLogsWithoutJsonData is called")
    void getAuditLogsWithoutJsonData_success() throws Exception {
        // Given
        when(service.getAuditLogsWithoutJsonData()).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/without-json-data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getAuditLogsWithoutJsonData();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get audit logs by json field when getAuditLogsByJsonField is called")
    void getAuditLogsByJsonField_success() throws Exception {
        // Given
        String fieldName = "name";
        String fieldValue = "John";
        when(service.getAuditLogsByJsonField(fieldName, fieldValue)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/json-field/{fieldName}/{fieldValue}", fieldName, fieldValue))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getAuditLogsByJsonField(fieldName, fieldValue);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get audit logs by json field containing when getAuditLogsByJsonFieldContaining is called")
    void getAuditLogsByJsonFieldContaining_success() throws Exception {
        // Given
        String fieldName = "description";
        String fieldValue = "test";
        when(service.getAuditLogsByJsonFieldContaining(fieldName, fieldValue)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/json-field/containing/{fieldName}/{fieldValue}", fieldName, fieldValue))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getAuditLogsByJsonFieldContaining(fieldName, fieldValue);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get audit logs by json field exists when getAuditLogsByJsonFieldExists is called")
    void getAuditLogsByJsonFieldExists_success() throws Exception {
        // Given
        String fieldName = "status";
        when(service.getAuditLogsByJsonFieldExists(fieldName)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/json-field/exists/{fieldName}", fieldName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getAuditLogsByJsonFieldExists(fieldName);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get audit logs by json field not exists when getAuditLogsByJsonFieldNotExists is called")
    void getAuditLogsByJsonFieldNotExists_success() throws Exception {
        // Given
        String fieldName = "nonExistentField";
        when(service.getAuditLogsByJsonFieldNotExists(fieldName)).thenReturn(auditLogs);

        // When/Then
        mockMvc.perform(get("/api/audit-logs/json-field/not-exists/{fieldName}", fieldName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(auditLog.getId()));

        verify(service).getAuditLogsByJsonFieldNotExists(fieldName);
    }
}