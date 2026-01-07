package com.app.panama_trips.presentation.controller;

import java.util.List;

import static com.app.panama_trips.DataProvider.languageRequestMock;
import static com.app.panama_trips.DataProvider.languageResponseListMock;
import static com.app.panama_trips.DataProvider.languageResponseMock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
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

import com.app.panama_trips.presentation.dto.LanguageRequest;
import com.app.panama_trips.presentation.dto.LanguageResponse;
import com.app.panama_trips.service.implementation.LanguageService;

@WebMvcTest(LanguageController.class)
public class LanguageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LanguageService languageService;

    private LanguageRequest request;
    private LanguageResponse response;
    private List<LanguageResponse> responseList;

    @BeforeEach
    void setUp() {
        request = languageRequestMock();
        response = languageResponseMock();
        responseList = languageResponseListMock();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should return all languages with pagination when getAllLanguages is called")
    void getAllLanguages_success() throws Exception {
        // Given
        Page<LanguageResponse> page = new PageImpl<>(responseList);
        when(languageService.getAllLanguages(any(Pageable.class))).thenReturn(page);

        // When/Then
        mockMvc.perform(get("/api/languages")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].code").value(response.code()))
                .andExpect(jsonPath("$.content[0].name").value(response.name()))
                .andExpect(jsonPath("$.content[0].isActive").value(response.isActive()));

        verify(languageService).getAllLanguages(any(Pageable.class));
    }
}
