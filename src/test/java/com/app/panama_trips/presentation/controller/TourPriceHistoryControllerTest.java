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

import com.app.panama_trips.presentation.dto.TourPriceHistoryRequest;
import com.app.panama_trips.presentation.dto.TourPriceHistoryResponse;
import com.app.panama_trips.service.implementation.TourPriceHistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static com.app.panama_trips.DataProvider.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TourPriceHistoryController.class)
public class TourPriceHistoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TourPriceHistoryService service;

    private TourPriceHistoryRequest request;
    private TourPriceHistoryResponse response;
    private List<TourPriceHistoryResponse> responseList;

    @BeforeEach
    void setUp() {
        request = tourPriceHistoryRequestMock;
        response = tourPriceHistoryResponseMock;
        responseList = tourPriceHistoryResponseListMock;
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
    @DisplayName("Should return all tour price histories with pagination when getAll is called")
    void getAll_success() throws Exception {
        // Given
        Page<TourPriceHistoryResponse> page = new PageImpl<>(responseList);
        when(service.getAllTourPriceHistories(any(Pageable.class))).thenReturn(page);

        // When/Then
        mockMvc.perform(get("/api/tour-price-history")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(responseList.getFirst().id()));

        verify(service).getAllTourPriceHistories(any(Pageable.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should return tour price history by id when getById is called")
    void getById_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.getTourPriceHistoryById(id)).thenReturn(response);

        // When/Then
        mockMvc.perform(get("/api/tour-price-history/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()));

        verify(service).getTourPriceHistoryById(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should create tour price history when create is called")
    void create_success() throws Exception {
        // Given
        when(service.saveTourPriceHistory(any(TourPriceHistoryRequest.class))).thenReturn(response);

        // When/Then
        mockMvc.perform(post("/api/tour-price-history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()));

        verify(service).saveTourPriceHistory(any(TourPriceHistoryRequest.class));
    }
}