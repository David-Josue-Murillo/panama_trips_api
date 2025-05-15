package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.presentation.dto.TourPlanSpecialPriceRequest;
import com.app.panama_trips.presentation.dto.TourPlanSpecialPriceResponse;
import com.app.panama_trips.service.implementation.TourPlanSpecialPriceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.app.panama_trips.DataProvider.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TourPlanSpecialPriceController.class)
public class TourPlanSpecialPriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TourPlanSpecialPriceService service;

    private TourPlanSpecialPriceRequest specialPriceRequest;
    private TourPlanSpecialPriceResponse specialPriceResponse;
    private List<TourPlanSpecialPriceResponse> specialPriceResponseList;
    private LocalDate today, tomorrow, nextWeek;

    @BeforeEach
    void setUp() {
        // Setup common test dates
        today = LocalDate.now();
        tomorrow = today.plusDays(1);
        nextWeek = today.plusDays(7);

        // Setup request object
        specialPriceRequest = tourPlanSpecialPriceRequestMock;

        // Setup response object
        specialPriceResponse = tourPlanSpecialPriceResponseMock;

        // Setup response list
        specialPriceResponseList = tourPlanSpecialPriceResponseListMocks;
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
    void getAll_success() throws Exception {
        // Given
        Page<TourPlanSpecialPriceResponse> page = new PageImpl<>(specialPriceResponseList);
        when(service.getAll(any(Pageable.class))).thenReturn(page);

        // When/Then
        mockMvc.perform(get("/api/tour-plan-special-price")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enablePagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(specialPriceResponse.id()))
                .andExpect(jsonPath("$.content[0].price").value(specialPriceResponse.price().doubleValue()))
                .andExpect(jsonPath("$.content[1].id").value(specialPriceResponseList.get(1).id()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getById_success() throws Exception {
        // Given
        when(service.findById(anyInt())).thenReturn(specialPriceResponse);

        // When/Then
        mockMvc.perform(get("/api/tour-plan-special-price/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(specialPriceResponse.id()))
                .andExpect(jsonPath("$.tourPlanId").value(specialPriceResponse.tourPlanId()))
                .andExpect(jsonPath("$.startDate").value(specialPriceResponse.startDate().toString()))
                .andExpect(jsonPath("$.price").value(specialPriceResponse.price().doubleValue()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void create_success() throws Exception {
        // Given
        when(service.save(any(TourPlanSpecialPriceRequest.class))).thenReturn(specialPriceResponse);

        // When/Then
        mockMvc.perform(post("/api/tour-plan-special-price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(specialPriceRequest))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(specialPriceResponse.id()))
                .andExpect(jsonPath("$.description").value(specialPriceResponse.description()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void update_success() throws Exception {
        // Given
        when(service.update(anyInt(), any(TourPlanSpecialPriceRequest.class))).thenReturn(specialPriceResponse);

        // When/Then
        mockMvc.perform(put("/api/tour-plan-special-price/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(specialPriceRequest))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(specialPriceResponse.id()))
                .andExpect(jsonPath("$.price").value(specialPriceResponse.price().doubleValue()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void delete_success() throws Exception {
        // Given
        doNothing().when(service).deleteById(anyInt());

        // When/Then
        mockMvc.perform(delete("/api/tour-plan-special-price/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByTourPlanId_success() throws Exception {
        // Given
        when(service.findByTourPlan(any(TourPlan.class))).thenReturn(specialPriceResponseList);

        // When/Then
        mockMvc.perform(get("/api/tour-plan-special-price/tour-plan/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(specialPriceResponse.id()))
                .andExpect(jsonPath("$[1].id").value(specialPriceResponseList.get(1).id()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByTourPlanIdOrdered_success() throws Exception {
        // Given
        when(service.findByTourPlanOrderByStartDateAsc(any(TourPlan.class))).thenReturn(specialPriceResponseList);

        // When/Then
        mockMvc.perform(get("/api/tour-plan-special-price/tour-plan/1/ordered"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(specialPriceResponse.id()))
                .andExpect(jsonPath("$[0].startDate").value(specialPriceResponse.startDate().toString()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByStartDateBetween_success() throws Exception {
        // Given
        when(service.findByStartDateBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(specialPriceResponseList);

        // When/Then
        mockMvc.perform(get("/api/tour-plan-special-price/start-date-between")
                        .param("startDate", today.toString())
                        .param("endDate", nextWeek.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(specialPriceResponse.id()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByEndDateBetween_success() throws Exception {
        // Given
        when(service.findByEndDateBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(specialPriceResponseList);

        // When/Then
        mockMvc.perform(get("/api/tour-plan-special-price/end-date-between")
                        .param("startDate", today.toString())
                        .param("endDate", nextWeek.plusDays(10).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(specialPriceResponse.id()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByTourPlanIdAndDate_whenFound_success() throws Exception {
        // Given
        when(service.findByTourPlanIdAndDate(anyInt(), any(LocalDate.class)))
                .thenReturn(Optional.of(specialPriceResponse));

        // When/Then
        mockMvc.perform(get("/api/tour-plan-special-price/tour-plan/1/date")
                        .param("date", tomorrow.plusDays(1).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(specialPriceResponse.id()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByTourPlanIdAndDate_whenNotFound_returnsNotFound() throws Exception {
        // Given
        when(service.findByTourPlanIdAndDate(anyInt(), any(LocalDate.class)))
                .thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/tour-plan-special-price/tour-plan/1/date")
                        .param("date", today.minusDays(1).toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getOverlappingPrices_success() throws Exception {
        // Given
        when(service.findOverlappingPricePeriodsForTourPlan(anyInt(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(specialPriceResponse));

        // When/Then
        mockMvc.perform(get("/api/tour-plan-special-price/tour-plan/1/overlapping")
                        .param("startDate", today.toString())
                        .param("endDate", nextWeek.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(specialPriceResponse.id()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByTourPlanAndPriceGreaterThan_success() throws Exception {
        // Given
        when(service.findByTourPlanAndPriceGreaterThan(any(TourPlan.class), any(BigDecimal.class)))
                .thenReturn(Collections.singletonList(specialPriceResponse));

        // When/Then
        mockMvc.perform(get("/api/tour-plan-special-price/tour-plan/1/price-greater-than")
                        .param("price", "75.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(specialPriceResponse.id()))
                .andExpect(jsonPath("$[0].price").value(specialPriceResponse.price().doubleValue()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getByTourPlanAndStartDateAfter_success() throws Exception {
        // Given
        when(service.findByTourPlanAndStartDateGreaterThanEqual(any(TourPlan.class), any(LocalDate.class)))
                .thenReturn(specialPriceResponseList);

        // When/Then
        mockMvc.perform(get("/api/tour-plan-special-price/tour-plan/1/start-date-after")
                        .param("date", today.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(specialPriceResponse.id()))
                .andExpect(jsonPath("$[1].id").value(specialPriceResponseList.get(1).id()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteByTourPlanId_success() throws Exception {
        // Given
        doNothing().when(service).deleteByTourPlan(any(TourPlan.class));

        // When/Then
        mockMvc.perform(delete("/api/tour-plan-special-price/tour-plan/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void checkExistsByTourPlanAndDates_whenExists_returnsTrue() throws Exception {
        // Given
        when(service.existsByTourPlanAndStartDateAndEndDate(any(TourPlan.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(true);

        // When/Then
        mockMvc.perform(get("/api/tour-plan-special-price/exists")
                        .param("tourPlanId", "1")
                        .param("startDate", tomorrow.toString())
                        .param("endDate", nextWeek.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void checkExistsByTourPlanAndDates_whenNotExists_returnsFalse() throws Exception {
        // Given
        when(service.existsByTourPlanAndStartDateAndEndDate(any(TourPlan.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(false);

        // When/Then
        mockMvc.perform(get("/api/tour-plan-special-price/exists")
                        .param("tourPlanId", "1")
                        .param("startDate", nextWeek.plusDays(10).toString())
                        .param("endDate", nextWeek.plusDays(17).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }
}