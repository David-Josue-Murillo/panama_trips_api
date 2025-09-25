package com.app.panama_trips.presentation.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should update tour price history when update is called")
    void update_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.updateTourPriceHistory(eq(id), any(TourPriceHistoryRequest.class))).thenReturn(response);

        // When/Then
        mockMvc.perform(put("/api/tour-price-history/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()));

        verify(service).updateTourPriceHistory(eq(id), any(TourPriceHistoryRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should delete tour price history when delete is called")
    void delete_success() throws Exception {
        // Given
        Integer id = 1;
        doNothing().when(service).deleteTourPriceHistory(id);

        // When/Then
        mockMvc.perform(delete("/api/tour-price-history/{id}", id)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service).deleteTourPriceHistory(id);
    }

    // Find operations by entity relationships
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find tour price histories by tour plan id when findByTourPlanId is called")
    void findByTourPlanId_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        when(service.findByTourPlanId(tourPlanId)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/tour-price-history/tour-plan/{tourPlanId}", tourPlanId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findByTourPlanId(tourPlanId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find tour price histories by tour plan id with pagination when findByTourPlanIdOrderByChangedAtDesc is called")
    void findByTourPlanIdOrderByChangedAtDesc_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        Page<TourPriceHistoryResponse> page = new PageImpl<>(responseList);
        when(service.findByTourPlanIdOrderByChangedAtDesc(eq(tourPlanId), any(Pageable.class))).thenReturn(page);

        // When/Then
        mockMvc.perform(get("/api/tour-price-history/tour-plan/{tourPlanId}/paginated", tourPlanId)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(response.id()));

        verify(service).findByTourPlanIdOrderByChangedAtDesc(eq(tourPlanId), any(Pageable.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find tour price histories by tour plan id and date range when findByTourPlanIdAndChangedAtBetween is called")
    void findByTourPlanIdAndChangedAtBetween_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        LocalDateTime startDate = LocalDateTime.now().minusDays(10);
        LocalDateTime endDate = LocalDateTime.now();
        when(service.findByTourPlanIdAndChangedAtBetween(eq(tourPlanId), any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/tour-price-history/tour-plan/{tourPlanId}/date-range", tourPlanId)
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findByTourPlanIdAndChangedAtBetween(eq(tourPlanId), any(LocalDateTime.class),
                any(LocalDateTime.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find tour price histories by changed by user id when findByChangedById is called")
    void findByChangedById_success() throws Exception {
        // Given
        Long userId = 1L;
        when(service.findByChangedById(userId)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/tour-price-history/changed-by/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findByChangedById(userId);
    }

    // Specialized queries
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should calculate average price change percentage by tour plan id when calculateAveragePriceChangePercentageByTourPlanId is called")
    void calculateAveragePriceChangePercentageByTourPlanId_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        Double expectedPercentage = 12.5;
        when(service.calculateAveragePriceChangePercentageByTourPlanId(tourPlanId)).thenReturn(expectedPercentage);

        // When/Then
        mockMvc.perform(get("/api/tour-price-history/tour-plan/{tourPlanId}/average-change-percentage", tourPlanId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedPercentage.toString()));

        verify(service).calculateAveragePriceChangePercentageByTourPlanId(tourPlanId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find tour price histories by new price greater than when findByNewPriceGreaterThan is called")
    void findByNewPriceGreaterThan_success() throws Exception {
        // Given
        BigDecimal price = BigDecimal.valueOf(200.00);
        when(service.findByNewPriceGreaterThan(price)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/tour-price-history/new-price-greater-than/{price}", price))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findByNewPriceGreaterThan(price);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should count price changes by tour plan id when countPriceChangesByTourPlanId is called")
    void countPriceChangesByTourPlanId_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        Long expectedCount = 5L;
        when(service.countPriceChangesByTourPlanId(tourPlanId)).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/tour-price-history/tour-plan/{tourPlanId}/count", tourPlanId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).countPriceChangesByTourPlanId(tourPlanId);
    }

    // Business logic operations
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get recent changes when getRecentChanges is called")
    void getRecentChanges_success() throws Exception {
        // Given
        int limit = 10;
        when(service.getRecentChanges(limit)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/tour-price-history/recent/{limit}", limit))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getRecentChanges(limit);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get latest change for tour plan when found")
    void getLatestChangeForTourPlan_whenFound_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        when(service.getLatestChangeForTourPlan(tourPlanId)).thenReturn(Optional.of(response));

        // When/Then
        mockMvc.perform(get("/api/tour-price-history/tour-plan/{tourPlanId}/latest", tourPlanId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()));

        verify(service).getLatestChangeForTourPlan(tourPlanId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should return 404 when latest change for tour plan not found")
    void getLatestChangeForTourPlan_whenNotFound_returns404() throws Exception {
        // Given
        Integer tourPlanId = 1;
        when(service.getLatestChangeForTourPlan(tourPlanId)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/tour-price-history/tour-plan/{tourPlanId}/latest", tourPlanId))
                .andExpect(status().isNotFound());

        verify(service).getLatestChangeForTourPlan(tourPlanId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get current price for tour plan when getCurrentPriceForTourPlan is called")
    void getCurrentPriceForTourPlan_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        BigDecimal expectedPrice = BigDecimal.valueOf(120.00);
        when(service.getCurrentPriceForTourPlan(tourPlanId)).thenReturn(expectedPrice);

        // When/Then
        mockMvc.perform(get("/api/tour-price-history/tour-plan/{tourPlanId}/current-price", tourPlanId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedPrice.toString()));

        verify(service).getCurrentPriceForTourPlan(tourPlanId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get previous price for tour plan when getPreviousPriceForTourPlan is called")
    void getPreviousPriceForTourPlan_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        BigDecimal expectedPrice = BigDecimal.valueOf(100.00);
        when(service.getPreviousPriceForTourPlan(tourPlanId)).thenReturn(expectedPrice);

        // When/Then
        mockMvc.perform(get("/api/tour-price-history/tour-plan/{tourPlanId}/previous-price", tourPlanId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedPrice.toString()));

        verify(service).getPreviousPriceForTourPlan(tourPlanId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get max price for tour plan when getMaxPriceForTourPlan is called")
    void getMaxPriceForTourPlan_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        BigDecimal expectedPrice = BigDecimal.valueOf(150.00);
        when(service.getMaxPriceForTourPlan(tourPlanId)).thenReturn(expectedPrice);

        // When/Then
        mockMvc.perform(get("/api/tour-price-history/tour-plan/{tourPlanId}/max-price", tourPlanId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedPrice.toString()));

        verify(service).getMaxPriceForTourPlan(tourPlanId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get min price for tour plan when getMinPriceForTourPlan is called")
    void getMinPriceForTourPlan_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        BigDecimal expectedPrice = BigDecimal.valueOf(80.00);
        when(service.getMinPriceForTourPlan(tourPlanId)).thenReturn(expectedPrice);

        // When/Then
        mockMvc.perform(get("/api/tour-price-history/tour-plan/{tourPlanId}/min-price", tourPlanId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedPrice.toString()));

        verify(service).getMinPriceForTourPlan(tourPlanId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get average price for tour plan when getAveragePriceForTourPlan is called")
    void getAveragePriceForTourPlan_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        BigDecimal expectedPrice = BigDecimal.valueOf(110.00);
        when(service.getAveragePriceForTourPlan(tourPlanId)).thenReturn(expectedPrice);

        // When/Then
        mockMvc.perform(get("/api/tour-price-history/tour-plan/{tourPlanId}/average-price", tourPlanId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedPrice.toString()));

        verify(service).getAveragePriceForTourPlan(tourPlanId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get price changes on date when getPriceChangesOnDate is called")
    void getPriceChangesOnDate_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        LocalDate date = LocalDate.now();
        when(service.getPriceChangesOnDate(tourPlanId, date)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/tour-price-history/tour-plan/{tourPlanId}/changes-on-date/{date}", tourPlanId, date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getPriceChangesOnDate(tourPlanId, date);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get price changes by user and date range when getPriceChangesByUserAndDateRange is called")
    void getPriceChangesByUserAndDateRange_success() throws Exception {
        // Given
        Long userId = 1L;
        LocalDateTime startDate = LocalDateTime.now().minusDays(10);
        LocalDateTime endDate = LocalDateTime.now();
        when(service.getPriceChangesByUserAndDateRange(eq(userId), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/tour-price-history/user/{userId}/date-range", userId)
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getPriceChangesByUserAndDateRange(eq(userId), any(LocalDateTime.class),
                any(LocalDateTime.class));
    }

    // Bulk operations
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should create multiple tour price histories in bulk when bulkCreate is called")
    void bulkCreate_success() throws Exception {
        // Given
        List<TourPriceHistoryRequest> requests = Collections.singletonList(request);
        doNothing().when(service).bulkCreateTourPriceHistories(anyList());

        // When/Then
        mockMvc.perform(post("/api/tour-price-history/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requests))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated());

        verify(service).bulkCreateTourPriceHistories(anyList());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should delete multiple tour price histories in bulk when bulkDelete is called")
    void bulkDelete_success() throws Exception {
        // Given
        List<Integer> tourPriceHistoryIds = Collections.singletonList(1);
        doNothing().when(service).bulkDeleteTourPriceHistories(anyList());

        // When/Then
        mockMvc.perform(delete("/api/tour-price-history/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(tourPriceHistoryIds))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service).bulkDeleteTourPriceHistories(anyList());
    }

    // Check operations
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should check if tour price history exists by id when existsById is called")
    void existsById_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.existsById(id)).thenReturn(true);

        // When/Then
        mockMvc.perform(get("/api/tour-price-history/exists/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).existsById(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should count tour price histories by tour plan id when countByTourPlanId is called")
    void countByTourPlanId_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        Long expectedCount = 5L;
        when(service.countByTourPlanId(tourPlanId)).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/tour-price-history/count/tour-plan/{tourPlanId}", tourPlanId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).countByTourPlanId(tourPlanId);
    }
}