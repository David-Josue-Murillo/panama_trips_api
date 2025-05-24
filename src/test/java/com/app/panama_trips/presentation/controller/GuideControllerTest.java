package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.GuideRequest;
import com.app.panama_trips.presentation.dto.GuideResponse;
import com.app.panama_trips.service.implementation.GuideService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import java.util.List;
import java.util.Optional;

import static com.app.panama_trips.DataProvider.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GuideController.class)
public class GuideControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GuideService service;

    private GuideRequest request;
    private GuideResponse response;
    private List<GuideResponse> responseList;

    @BeforeEach
    void setUp() {
        request = guideRequestMock;
        response = guideResponseMock;
        responseList = guideResponseListMock;
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
    @DisplayName("Should return all guides with pagination when getAllGuides is called")
    void getAllGuides_success() throws Exception {
        // Given
        Page<GuideResponse> page = new PageImpl<>(responseList);
        when(service.findAll(any(Pageable.class))).thenReturn(page);

        // When/Then
        mockMvc.perform(get("/api/guides")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(responseList.getFirst().id()))
                .andExpect(jsonPath("$.content[0].biografy").value(response.biografy()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return guide by id when getGuideById is called")
    void getGuideById_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.findById(id)).thenReturn(Optional.of(response));

        // When/Then
        mockMvc.perform(get("/api/guides/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.biografy").value(response.biografy()));

        verify(service).findById(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return 404 when guide not found")
    void getGuideById_whenNotFound_returns404() throws Exception {
        // Given
        Integer id = 999;
        when(service.findById(id)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/guides/{id}", id))
                .andExpect(status().isNotFound());

        verify(service).findById(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should create guide when createGuide is called")
    void createGuide_success() throws Exception {
        // Given
        when(service.createGuide(any(GuideRequest.class))).thenReturn(response);

        // When/Then
        mockMvc.perform(post("/api/guides")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.biografy").value(response.biografy()));

        verify(service).createGuide(any(GuideRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should update guide when updateGuide is called")
    void updateGuide_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.updateGuide(eq(id), any(GuideRequest.class))).thenReturn(response);

        // When/Then
        mockMvc.perform(put("/api/guides/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.biografy").value(response.biografy()));

        verify(service).updateGuide(eq(id), any(GuideRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should delete guide when deleteGuide is called")
    void deleteGuide_success() throws Exception {
        // Given
        Integer id = 1;
        doNothing().when(service).deleteGuide(id);

        // When/Then
        mockMvc.perform(delete("/api/guides/{id}", id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service).deleteGuide(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get all active guides")
    void getActiveGuides_success() throws Exception {
        // Given
        when(service.findAllActive()).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/guides/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findAllActive();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get guides by provider")
    void getGuidesByProvider_success() throws Exception {
        // Given
        Integer providerId = 1;
        when(service.findByProvider(any())).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/guides/provider/{providerId}", providerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findByProvider(any());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get guide by user when exists")
    void getGuideByUser_whenExists_success() throws Exception {
        // Given
        Long userId = 1L;
        when(service.findByUser(any())).thenReturn(Optional.of(response));

        // When/Then
        mockMvc.perform(get("/api/guides/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()));

        verify(service).findByUser(any());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return 404 when guide by user not found")
    void getGuideByUser_whenNotFound_returns404() throws Exception {
        // Given
        Long userId = 999L;
        when(service.findByUser(any())).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/guides/user/{userId}", userId))
                .andExpect(status().isNotFound());

        verify(service).findByUser(any());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get guides by experience")
    void getGuidesByExperience_success() throws Exception {
        // Given
        Integer minYears = 5;
        when(service.findByYearsExperienceGreaterThanEqual(minYears)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/guides/experience")
                        .param("minYears", minYears.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findByYearsExperienceGreaterThanEqual(minYears);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get active guides by provider")
    void getActiveGuidesByProvider_success() throws Exception {
        // Given
        Integer providerId = 1;
        when(service.findActiveGuidesByProvider(providerId)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/guides/provider/{providerId}/active", providerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findActiveGuidesByProvider(providerId);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get guides by language")
    void getGuidesByLanguage_success() throws Exception {
        // Given
        String language = "English";
        when(service.findByLanguageAndActive(language)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/guides/language")
                        .param("language", language))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findByLanguageAndActive(language);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get guides by specialty")
    void getGuidesBySpecialty_success() throws Exception {
        // Given
        String specialty = "Hiking";
        when(service.findBySpecialtyAndActive(specialty)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/guides/specialty")
                        .param("specialty", specialty))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findBySpecialtyAndActive(specialty);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should activate guide")
    void activateGuide_success() throws Exception {
        // Given
        Integer id = 1;
        doNothing().when(service).activateGuide(id);

        // When/Then
        mockMvc.perform(post("/api/guides/{id}/activate", id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(service).activateGuide(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should deactivate guide")
    void deactivateGuide_success() throws Exception {
        // Given
        Integer id = 1;
        doNothing().when(service).deactivateGuide(id);

        // When/Then
        mockMvc.perform(post("/api/guides/{id}/deactivate", id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(service).deactivateGuide(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should check if guide exists")
    void existsGuide_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.existsById(id)).thenReturn(true);

        // When/Then
        mockMvc.perform(get("/api/guides/exists/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).existsById(id);
    }
}
