package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.TourPlanAvailabilityRequest;
import com.app.panama_trips.presentation.dto.TourPlanAvailabilityResponse;
import com.app.panama_trips.service.implementation.TourPlanAvailabilityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import static com.app.panama_trips.DataProvider.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TourPlanAvailabilityController.class)
public class TourPlanAvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TourPlanAvailabilityService tourPlanAvailabilityService;

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(obj);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllTourPlanAvailabilities_success() throws Exception {
        Page<TourPlanAvailabilityResponse> page = new PageImpl<>(tourPlanAvailabilityResponseListMocks);
        when(tourPlanAvailabilityService.getAllTourPlanAvailabilities(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/tour-plan-availability")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(tourPlanAvailabilityResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].isAvailable").value(tourPlanAvailabilityResponseListMocks.getFirst().isAvailable()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getTourPlanAvailabilityById_success() throws Exception {
        TourPlanAvailabilityResponse response = tourPlanAvailabilityResponseMock;
        when(tourPlanAvailabilityService.getTourPlanAvailabilityById(any(Integer.class))).thenReturn(response);

        mockMvc.perform(get("/api/tour-plan-availability/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.tourPlanId").value(response.tourPlanId()))
                .andExpect(jsonPath("$.availableDate").value(response.availableDate().toString()))
                .andExpect(jsonPath("$.isAvailable").value(response.isAvailable()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createTourPlanAvailability_success() throws Exception {
        TourPlanAvailabilityRequest request = tourPlanAvailabilityRequest;
        TourPlanAvailabilityResponse response = tourPlanAvailabilityResponseMock;

        when(tourPlanAvailabilityService.saveTourPlanAvailability(any(TourPlanAvailabilityRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/tour-plan-availability")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.tourPlanId").value(response.tourPlanId()))
                .andExpect(jsonPath("$.availableDate").value(response.availableDate().toString()))
                .andExpect(jsonPath("$.isAvailable").value(response.isAvailable()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateTourPlanAvailability_success() throws Exception {
        TourPlanAvailabilityRequest request = tourPlanAvailabilityRequest;
        TourPlanAvailabilityResponse response = new TourPlanAvailabilityResponse(
                1, 1, LocalDate.now(), 15, true, BigDecimal.valueOf(95.00));

        when(tourPlanAvailabilityService.updateTourPlanAvailability(any(Integer.class), any(TourPlanAvailabilityRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/tour-plan-availability/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.availableSpots").value(response.availableSpots()))
                .andExpect(jsonPath("$.priceOverride").value(response.priceOverride()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteTourPlanAvailability_success() throws Exception {
        mockMvc.perform(delete("/api/tour-plan-availability/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getTourPlanAvailabilitiesByTourPlanId_success() throws Exception {
        when(tourPlanAvailabilityService.getTourPlanAvailabilitiesByTourPlanId(any(Integer.class)))
                .thenReturn(tourPlanAvailabilityResponseListMocks);

        mockMvc.perform(get("/api/tour-plan-availability/tour-plan/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(tourPlanAvailabilityResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$[0].tourPlanId").value(tourPlanAvailabilityResponseListMocks.getFirst().tourPlanId()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAvailableDatesByTourPlanId_success() throws Exception {
        when(tourPlanAvailabilityService.getAvailableDatesByTourPlanId(any(Integer.class)))
                .thenReturn(tourPlanAvailabilityResponseListMocks);

        mockMvc.perform(get("/api/tour-plan-availability/tour-plan/1/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(tourPlanAvailabilityResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$[0].isAvailable").value(tourPlanAvailabilityResponseListMocks.getFirst().isAvailable()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAvailabilitiesByDateRange_success() throws Exception {
        when(tourPlanAvailabilityService.getAvailabilitiesByDateRange(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(tourPlanAvailabilityResponseListMocks);

        mockMvc.perform(get("/api/tour-plan-availability/date-range")
                        .param("startDate", LocalDate.now().toString())
                        .param("endDate", LocalDate.now().plusDays(7).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(tourPlanAvailabilityResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$[0].availableDate").value(tourPlanAvailabilityResponseListMocks.getFirst().availableDate().toString()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAvailabilitiesByTourPlanIdAndDateRange_success() throws Exception {
        when(tourPlanAvailabilityService.getAvailabilitiesByTourPlanIdAndDateRange(
                any(Integer.class), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(tourPlanAvailabilityResponseListMocks);

        mockMvc.perform(get("/api/tour-plan-availability/tour-plan/1/date-range")
                        .param("startDate", LocalDate.now().toString())
                        .param("endDate", LocalDate.now().plusDays(7).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(tourPlanAvailabilityResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$[0].tourPlanId").value(tourPlanAvailabilityResponseListMocks.getFirst().tourPlanId()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAvailabilityByTourPlanIdAndDate_success() throws Exception {
        TourPlanAvailabilityResponse response = tourPlanAvailabilityResponseMock;
        when(tourPlanAvailabilityService.getAvailabilityByTourPlanIdAndDate(
                any(Integer.class), any(LocalDate.class)))
                .thenReturn(response);

        mockMvc.perform(get("/api/tour-plan-availability/tour-plan/1/date/" + LocalDate.now()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.availableDate").value(response.availableDate().toString()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getUpcomingAvailableDatesByTourPlanId_success() throws Exception {
        when(tourPlanAvailabilityService.getUpcomingAvailableDatesByTourPlanId(any(Integer.class)))
                .thenReturn(tourPlanAvailabilityResponseListMocks);

        mockMvc.perform(get("/api/tour-plan-availability/tour-plan/1/upcoming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(tourPlanAvailabilityResponseListMocks.getFirst().id()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAvailableDatesWithSufficientSpots_success() throws Exception {
        when(tourPlanAvailabilityService.getAvailableDatesWithSufficientSpots(any(Integer.class), any(Integer.class)))
                .thenReturn(tourPlanAvailabilityResponseListMocks);

        mockMvc.perform(get("/api/tour-plan-availability/tour-plan/1/sufficient-spots/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(tourPlanAvailabilityResponseListMocks.getFirst().id()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void countUpcomingAvailableDatesByTourPlanId_success() throws Exception {
        when(tourPlanAvailabilityService.countUpcomingAvailableDatesByTourPlanId(any(Integer.class)))
                .thenReturn(3L);

        mockMvc.perform(get("/api/tour-plan-availability/tour-plan/1/upcoming/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(3));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAvailabilitiesWithPriceOverride_success() throws Exception {
        when(tourPlanAvailabilityService.getAvailabilitiesWithPriceOverride(any(Integer.class)))
                .thenReturn(tourPlanAvailabilityResponseListMocks);

        mockMvc.perform(get("/api/tour-plan-availability/tour-plan/1/price-override"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(tourPlanAvailabilityResponseListMocks.getFirst().id()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAvailabilitiesWithPriceAbove_success() throws Exception {
        when(tourPlanAvailabilityService.getAvailabilitiesWithPriceAbove(any(Integer.class), any(BigDecimal.class)))
                .thenReturn(tourPlanAvailabilityResponseListMocks);

        mockMvc.perform(get("/api/tour-plan-availability/tour-plan/1/price-above/50.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(tourPlanAvailabilityResponseListMocks.getFirst().id()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteAllAvailabilitiesByTourPlanId_success() throws Exception {
        mockMvc.perform(delete("/api/tour-plan-availability/tour-plan/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void existsAvailabilityForTourPlanAndDate_success() throws Exception {
        when(tourPlanAvailabilityService.existsAvailabilityForTourPlanAndDate(any(Integer.class), any(LocalDate.class)))
                .thenReturn(true);

        mockMvc.perform(get("/api/tour-plan-availability/tour-plan/1/exists/" + LocalDate.now()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }
}
