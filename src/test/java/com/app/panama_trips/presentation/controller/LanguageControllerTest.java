package com.app.panama_trips.presentation.controller;

import java.util.List;

import static com.app.panama_trips.DataProvider.languageRequestMock;
import static com.app.panama_trips.DataProvider.languageResponseListMock;
import static com.app.panama_trips.DataProvider.languageResponseMock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import com.app.panama_trips.presentation.dto.LanguageRequest;
import com.app.panama_trips.presentation.dto.LanguageResponse;
import com.app.panama_trips.service.implementation.LanguageService;
import com.app.panama_trips.utility.ParseJson;

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

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should return language by code when exists")
    void getLanguageByCode_success() throws Exception {
        // Given
        String code = response.code();
        when(languageService.getLanguageByCode(code)).thenReturn(response);

        // When/Then
        mockMvc.perform(get("/api/languages/{code}", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(response.code()))
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.isActive").value(response.isActive()));

        verify(languageService).getLanguageByCode(code);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should save language successfully")
    void saveLanguage_success() throws Exception {
        // Given
        when(languageService.saveLanguage(any(LanguageRequest.class)))
                .thenReturn(response);

        // When/Then
        mockMvc.perform(post("/api/languages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ParseJson.asJsonString(request))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(response.code()))
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.isActive").value(response.isActive()));

        verify(languageService).saveLanguage(any(LanguageRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should update language successfully")
    void updateLanguage_success() throws Exception {
        // Given
        String code = request.code();
        when(languageService.updateLanguage(eq(code), any(LanguageRequest.class))).thenReturn(response);

        // When/Then
        mockMvc.perform(put("/api/languages/{code}", code)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ParseJson.asJsonString(request))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(response.code()))
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.isActive").value(response.isActive()));

        verify(languageService).updateLanguage(eq(code), any(LanguageRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should delete language successfully")
    void deleteLanguage_success() throws Exception {
        // Given
        String code = request.code();

        // When/Then
        mockMvc.perform(delete("/api/languages/{code}", code)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(languageService).deleteLanguage(code);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should return all active languages successfully")
    void getAllActiveLanguages_success() throws Exception {
        // Given
        when(languageService.getAllActiveLanguages()).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/languages/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value(response.code()))
                .andExpect(jsonPath("$[0].name").value(response.name()))
                .andExpect(jsonPath("$[0].isActive").value(response.isActive()));

        verify(languageService).getAllActiveLanguages();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should return language by name successfully")
    void getLanguageByName_success() throws Exception {
        // Given
        String name = response.name();
        when(languageService.getLanguageByName(name)).thenReturn(response);

        // When/Then
        mockMvc.perform(get("/api/languages/name/{name}", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(response.code()))
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.isActive").value(response.isActive()));

        verify(languageService).getLanguageByName(name);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should search active languages by keyword successfully")
    void searchActiveLanguages_success() throws Exception {
        // Given
        String keyword = "es";
        when(languageService.searchActiveLanguages(keyword)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/languages/search")
                .param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value(response.code()))
                .andExpect(jsonPath("$[0].name").value(response.name()))
                .andExpect(jsonPath("$[0].isActive").value(response.isActive()));

        verify(languageService).searchActiveLanguages(keyword);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should count active languages successfully")
    void countActiveLanguages_success() throws Exception {
        // Given
        Long count = 3L;
        when(languageService.countActiveLanguages()).thenReturn(count);

        // When/Then
        mockMvc.perform(get("/api/languages/active/count"))
                .andExpect(status().isOk())
                .andExpect(content().string(count.toString()));

        verify(languageService).countActiveLanguages();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should check if language exists by code")
    void existsByCode_success() throws Exception {
        // Given
        String code = response.code();
        when(languageService.existsByCode(code)).thenReturn(true);

        // When/Then
        mockMvc.perform(get("/api/languages/exists/code")
                .param("code", code))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(languageService).existsByCode(code);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should check if language exists by name")
    void existsByName_success() throws Exception {
        // Given
        String name = response.name();
        when(languageService.existsByName(name)).thenReturn(true);

        // When/Then
        mockMvc.perform(get("/api/languages/exists/name")
                .param("name", name))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(languageService).existsByName(name);
    }
}
