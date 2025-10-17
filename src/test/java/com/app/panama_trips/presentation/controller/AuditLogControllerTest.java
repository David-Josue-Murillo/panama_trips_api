package com.app.panama_trips.presentation.controller;

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
}