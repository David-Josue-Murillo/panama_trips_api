package com.app.panama_trips.presentation.controller;

import java.util.List;

import static com.app.panama_trips.DataProvider.tourTranslationRequestMock;
import static com.app.panama_trips.DataProvider.tourTranslationResponseListMock;
import static com.app.panama_trips.DataProvider.tourTranslationResponseMock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.app.panama_trips.presentation.dto.TourTranslationRequest;
import com.app.panama_trips.presentation.dto.TourTranslationResponse;
import com.app.panama_trips.service.interfaces.ITourTranslationService;
import com.app.panama_trips.utility.ParseJson;

@WebMvcTest(TourTranslationController.class)
public class TourTranslationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ITourTranslationService tourTranslationService;

    private TourTranslationRequest request;
    private TourTranslationResponse response;
    private List<TourTranslationResponse> responseList;

    @BeforeEach
    void setUp() {
        request = tourTranslationRequestMock();
        response = tourTranslationResponseMock();
        responseList = tourTranslationResponseListMock();
    }

    // CRUD Operations Tests

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return all tour translations with pagination")
    void getAllTourTranslations_success() throws Exception {
        // Given
        Page<TourTranslationResponse> page = new PageImpl<>(responseList);
        when(tourTranslationService.getAllTourTranslations(any(Pageable.class))).thenReturn(page);

        // When/Then
        mockMvc.perform(get("/api/tour-translations")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].tourPlanId").value(response.tourPlanId()))
                .andExpect(jsonPath("$.content[0].languageCode").value(response.languageCode()))
                .andExpect(jsonPath("$.content[0].title").value(response.title()));

        verify(tourTranslationService).getAllTourTranslations(any(Pageable.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return tour translation by tourPlanId and languageCode")
    void getTourTranslationByTourPlanIdAndLanguageCode_success() throws Exception {
        // Given
        Integer tourPlanId = response.tourPlanId();
        String languageCode = response.languageCode();
        when(tourTranslationService.getTourTranslationByTourPlanIdAndLanguageCode(tourPlanId, languageCode))
                .thenReturn(response);

        // When/Then
        mockMvc.perform(get("/api/tour-translations/{tourPlanId}/{languageCode}", tourPlanId, languageCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tourPlanId").value(response.tourPlanId()))
                .andExpect(jsonPath("$.languageCode").value(response.languageCode()))
                .andExpect(jsonPath("$.title").value(response.title()))
                .andExpect(jsonPath("$.description").value(response.description()));

        verify(tourTranslationService).getTourTranslationByTourPlanIdAndLanguageCode(tourPlanId, languageCode);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should save tour translation successfully")
    void saveTourTranslation_success() throws Exception {
        // Given
        when(tourTranslationService.saveTourTranslation(any(TourTranslationRequest.class)))
                .thenReturn(response);

        // When/Then
        mockMvc.perform(post("/api/tour-translations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ParseJson.asJsonString(request))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tourPlanId").value(response.tourPlanId()))
                .andExpect(jsonPath("$.languageCode").value(response.languageCode()))
                .andExpect(jsonPath("$.title").value(response.title()));

        verify(tourTranslationService).saveTourTranslation(any(TourTranslationRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should update tour translation successfully")
    void updateTourTranslation_success() throws Exception {
        // Given
        Integer tourPlanId = request.tourPlanId();
        String languageCode = request.languageCode();
        when(tourTranslationService.updateTourTranslation(eq(tourPlanId), eq(languageCode), any(TourTranslationRequest.class)))
                .thenReturn(response);

        // When/Then
        mockMvc.perform(put("/api/tour-translations/{tourPlanId}/{languageCode}", tourPlanId, languageCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ParseJson.asJsonString(request))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tourPlanId").value(response.tourPlanId()))
                .andExpect(jsonPath("$.languageCode").value(response.languageCode()))
                .andExpect(jsonPath("$.title").value(response.title()));

        verify(tourTranslationService).updateTourTranslation(eq(tourPlanId), eq(languageCode), any(TourTranslationRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should delete tour translation successfully")
    void deleteTourTranslation_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        String languageCode = "ES";
        doNothing().when(tourTranslationService).deleteTourTranslation(tourPlanId, languageCode);

        // When/Then
        mockMvc.perform(delete("/api/tour-translations/{tourPlanId}/{languageCode}", tourPlanId, languageCode)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(tourTranslationService).deleteTourTranslation(tourPlanId, languageCode);
    }

    // Business Operations Tests
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return tour translations by tourPlanId")
    void getTourTranslationsByTourPlanId_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        when(tourTranslationService.getTourTranslationsByTourPlanId(tourPlanId)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/tour-translations/tour-plan/{tourPlanId}", tourPlanId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tourPlanId").value(response.tourPlanId()))
                .andExpect(jsonPath("$[0].languageCode").value(response.languageCode()))
                .andExpect(jsonPath("$[0].title").value(response.title()));

        verify(tourTranslationService).getTourTranslationsByTourPlanId(tourPlanId);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return tour translations by languageCode")
    void getTourTranslationsByLanguageCode_success() throws Exception {
        // Given
        String languageCode = "ES";
        when(tourTranslationService.getTourTranslationsByLanguageCode(languageCode)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/tour-translations/language/{languageCode}", languageCode))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tourPlanId").value(response.tourPlanId()))
                .andExpect(jsonPath("$[0].languageCode").value(response.languageCode()))
                .andExpect(jsonPath("$[0].title").value(response.title()));

        verify(tourTranslationService).getTourTranslationsByLanguageCode(languageCode);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should check if tour translation exists")
    void existsByTourPlanIdAndLanguageCode_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        String languageCode = "ES";
        when(tourTranslationService.existsByTourPlanIdAndLanguageCode(tourPlanId, languageCode)).thenReturn(true);

        // When/Then
        mockMvc.perform(get("/api/tour-translations/exists")
                        .param("tourPlanId", tourPlanId.toString())
                        .param("languageCode", languageCode))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(tourTranslationService).existsByTourPlanIdAndLanguageCode(tourPlanId, languageCode);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return false when tour translation does not exist")
    void existsByTourPlanIdAndLanguageCode_notExists() throws Exception {
        // Given
        Integer tourPlanId = 999;
        String languageCode = "XX";
        when(tourTranslationService.existsByTourPlanIdAndLanguageCode(tourPlanId, languageCode)).thenReturn(false);

        // When/Then
        mockMvc.perform(get("/api/tour-translations/exists")
                        .param("tourPlanId", tourPlanId.toString())
                        .param("languageCode", languageCode))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(tourTranslationService).existsByTourPlanIdAndLanguageCode(tourPlanId, languageCode);
    }
}
