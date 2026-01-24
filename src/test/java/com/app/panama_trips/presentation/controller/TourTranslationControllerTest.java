package com.app.panama_trips.presentation.controller;

import java.util.List;

import static com.app.panama_trips.DataProvider.tourTranslationRequestMock;
import static com.app.panama_trips.DataProvider.tourTranslationResponseListMock;
import static com.app.panama_trips.DataProvider.tourTranslationResponseMock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.app.panama_trips.presentation.dto.TourTranslationRequest;
import com.app.panama_trips.presentation.dto.TourTranslationResponse;
import com.app.panama_trips.service.interfaces.ITourTranslationService;

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
}
