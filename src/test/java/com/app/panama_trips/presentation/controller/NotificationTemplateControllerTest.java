package com.app.panama_trips.presentation.controller;

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

import com.app.panama_trips.presentation.dto.NotificationTemplateRequest;
import com.app.panama_trips.presentation.dto.NotificationTemplateResponse;
import com.app.panama_trips.service.implementation.NotificationTemplateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static com.app.panama_trips.DataProvider.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationTemplateController.class)
public class NotificationTemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationTemplateService service;

    private NotificationTemplateRequest request;
    private NotificationTemplateResponse response;
    private List<NotificationTemplateResponse> responseList;

    @BeforeEach
    void setUp() {
        request = notificationTemplateRequestMock;
        response = notificationTemplateResponseMock;
        responseList = notificationTemplateResponseListMock;
    }

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(obj);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return all templates with pagination when getAllTemplates is called")
    void getAllTemplates_success() throws Exception {
        // Given
        Page<NotificationTemplateResponse> page = new PageImpl<>(responseList);
        when(service.getAllTemplates(any(Pageable.class))).thenReturn(page);

        // When/Then
        mockMvc.perform(get("/api/notification-templates")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(responseList.getFirst().id()))
                .andExpect(jsonPath("$.content[0].name").value(response.name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return template by id when getTemplateById is called")
    void getTemplateById_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.getTemplateById(id)).thenReturn(response);

        // When/Then
        mockMvc.perform(get("/api/notification-templates/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()));

        verify(service).getTemplateById(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should create template when createTemplate is called")
    void createTemplate_success() throws Exception {
        // Given
        when(service.saveTemplate(any(NotificationTemplateRequest.class))).thenReturn(response);

        // When/Then
        mockMvc.perform(post("/api/notification-templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()));

        verify(service).saveTemplate(any(NotificationTemplateRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should update template when updateTemplate is called")
    void updateTemplate_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.updateTemplate(eq(id), any(NotificationTemplateRequest.class))).thenReturn(response);

        // When/Then
        mockMvc.perform(put("/api/notification-templates/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()));

        verify(service).updateTemplate(eq(id), any(NotificationTemplateRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should delete template when deleteTemplate is called")
    void deleteTemplate_success() throws Exception {
        // Given
        Integer id = 1;
        doNothing().when(service).deleteTemplate(id);

        // When/Then
        mockMvc.perform(delete("/api/notification-templates/{id}", id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service).deleteTemplate(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should find templates by type when getTemplatesByType is called")
    void getTemplatesByType_success() throws Exception {
        // Given
        String type = "EMAIL";
        when(service.findByType(type)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/notification-templates/type/{type}", type))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()))
                .andExpect(jsonPath("$[0].type").value(response.type()));

        verify(service).findByType(type);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should find template by name when getTemplateByName is called")
    void getTemplateByName_whenFound_success() throws Exception {
        // Given
        String name = "Welcome Email";
        when(service.findByName(name)).thenReturn(Optional.of(response));

        // When/Then
        mockMvc.perform(get("/api/notification-templates/name/{name}", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()));

        verify(service).findByName(name);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return 404 when template name not found")
    void getTemplateByName_whenNotFound_returns404() throws Exception {
        // Given
        String name = "Non-existent Template";
        when(service.findByName(name)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/notification-templates/name/{name}", name))
                .andExpect(status().isNotFound());

        verify(service).findByName(name);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should search templates by keyword when searchTemplates is called")
    void searchTemplates_success() throws Exception {
        // Given
        String keyword = "welcome";
        when(service.searchBySubjectOrBody(keyword)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/notification-templates/search")
                        .param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).searchBySubjectOrBody(keyword);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should find templates by type and name when getTemplatesByTypeAndName is called")
    void getTemplatesByTypeAndName_success() throws Exception {
        // Given
        String type = "EMAIL";
        String name = "welcome";
        when(service.findByTypeAndNameContaining(type, name)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/notification-templates/type/{type}/name", type)
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findByTypeAndNameContaining(type, name);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should find templates by type and content when getTemplatesByTypeAndContent is called")
    void getTemplatesByTypeAndContent_success() throws Exception {
        // Given
        String type = "EMAIL";
        String content = "welcome";
        when(service.getTemplatesByTypeAndContent(type, content)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/notification-templates/type/{type}/content", type)
                        .param("content", content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getTemplatesByTypeAndContent(type, content);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get active templates when getActiveTemplates is called")
    void getActiveTemplates_success() throws Exception {
        // Given
        when(service.getActiveTemplates()).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/notification-templates/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getActiveTemplates();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should find templates by variable when getTemplatesByVariable is called")
    void getTemplatesByVariable_success() throws Exception {
        // Given
        String variable = "userName";
        when(service.getTemplatesByVariable(variable)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/notification-templates/variable/{variable}", variable))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getTemplatesByVariable(variable);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get template for notification when getTemplateForNotification is called")
    void getTemplateForNotification_success() throws Exception {
        // Given
        String type = "EMAIL";
        String event = "welcome";
        when(service.getTemplateForNotification(type, event)).thenReturn(response);

        // When/Then
        mockMvc.perform(get("/api/notification-templates/notification")
                        .param("type", type)
                        .param("event", event))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()));

        verify(service).getTemplateForNotification(type, event);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should create multiple templates in bulk when bulkCreateTemplates is called")
    void bulkCreateTemplates_success() throws Exception {
        // Given
        List<NotificationTemplateRequest> requests = Collections.singletonList(request);
        doNothing().when(service).bulkCreateTemplates(anyList());

        // When/Then
        mockMvc.perform(post("/api/notification-templates/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requests))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated());

        verify(service).bulkCreateTemplates(anyList());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should update multiple templates in bulk when bulkUpdateTemplates is called")
    void bulkUpdateTemplates_success() throws Exception {
        // Given
        List<NotificationTemplateRequest> requests = Collections.singletonList(request);
        doNothing().when(service).bulkUpdateTemplates(anyList());

        // When/Then
        mockMvc.perform(put("/api/notification-templates/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requests))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(service).bulkUpdateTemplates(anyList());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should delete multiple templates in bulk when bulkDeleteTemplates is called")
    void bulkDeleteTemplates_success() throws Exception {
        // Given
        List<Integer> templateIds = Collections.singletonList(1);
        doNothing().when(service).bulkDeleteTemplates(anyList());

        // When/Then
        mockMvc.perform(delete("/api/notification-templates/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(templateIds))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service).bulkDeleteTemplates(anyList());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should check if template exists by name when checkTemplateExists is called")
    void checkTemplateExists_success() throws Exception {
        // Given
        String name = "Welcome Email";
        when(service.existsByName(name)).thenReturn(true);

        // When/Then
        mockMvc.perform(get("/api/notification-templates/exists/{name}", name))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).existsByName(name);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should count templates by type when countTemplatesByType is called")
    void countTemplatesByType_success() throws Exception {
        // Given
        String type = "EMAIL";
        when(service.countByType(type)).thenReturn(5L);

        // When/Then
        mockMvc.perform(get("/api/notification-templates/count/{type}", type))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(service).countByType(type);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should validate template variables when validateTemplateVariables is called")
    void validateTemplateVariables_success() throws Exception {
        // Given
        Integer id = 1;
        List<String> variables = List.of("userName", "tourName");
        when(service.validateTemplateVariables(id, variables)).thenReturn(true);

        // When/Then
        mockMvc.perform(post("/api/notification-templates/{id}/validate-variables", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(variables))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).validateTemplateVariables(id, variables);
    }
}
