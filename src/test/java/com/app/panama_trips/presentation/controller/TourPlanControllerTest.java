package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.presentation.dto.TourPlanRequest;
import com.app.panama_trips.presentation.dto.TourPlanResponse;
import com.app.panama_trips.service.implementation.TourPlanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static com.app.panama_trips.DataProvider.tourPlanResponseListMocks;
import static com.app.panama_trips.DataProvider.tourPlanResponseMock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TourPlanController.class)
public class TourPlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TourPlanService tourPlanService;

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllTourPlan_success() throws Exception {
        Page<TourPlanResponse> page = new PageImpl<>(tourPlanResponseListMocks);
        when(tourPlanService.getAllTourPlan(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/tour-plan")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(tourPlanResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].address").value(tourPlanResponseListMocks.getFirst().address()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findTourPlanById_success() throws Exception {
        when(tourPlanService.getTourPlanById(anyInt())).thenReturn(tourPlanResponseMock);

        mockMvc.perform(get("/api/tour-plan/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tourPlanResponseMock.id()))
                .andExpect(jsonPath("$.address").value(tourPlanResponseMock.address()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findTourPlanById_failed() throws Exception {
        when(tourPlanService.getTourPlanById(anyInt())).thenThrow(new ResourceNotFoundException("Tour Plan not found"));

        mockMvc.perform(get("/api/tour-plan/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Tour Plan not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void saveTourPlan_success() throws Exception {
        when(tourPlanService.saveTourPlan(any(TourPlanRequest.class))).thenReturn(tourPlanResponseMock);

        mockMvc.perform(post("/api/tour-plan")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(DataProvider.tourPlanRequestMock))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(tourPlanResponseMock.id()))
                .andExpect(jsonPath("$.address").value(tourPlanResponseMock.address()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateTourPlan_success() throws Exception {
        when(tourPlanService.updateTourPlan(anyInt(), any(TourPlanRequest.class))).thenReturn(tourPlanResponseMock);

        mockMvc.perform(put("/api/tour-plan/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(DataProvider.tourPlanRequestMock))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tourPlanResponseMock.id()))
                .andExpect(jsonPath("$.address").value(tourPlanResponseMock.address()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateTourPlan_failed() throws Exception {
        when(tourPlanService.updateTourPlan(anyInt(), any(TourPlanRequest.class))).thenThrow(new ResourceNotFoundException("Tour Plan not found"));

        mockMvc.perform(put("/api/tour-plan/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(DataProvider.tourPlanRequestMock))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Tour Plan not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteTourPlan_success() throws Exception {
        mockMvc.perform(delete("/api/tour-plan/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void countTourPlan_success() throws Exception {
        when(tourPlanService.countTourPlan()).thenReturn(1L);

        mockMvc.perform(get("/api/tour-plan/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllTourPlanTitle_success() throws Exception {
        when(tourPlanService.getTourPlanByTitle(any(String.class))).thenReturn(tourPlanResponseMock);

        mockMvc.perform(get("/api/tour-plan/title")
                        .param("q", "title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tourPlanResponseMock.id()))
                .andExpect(jsonPath("$.address").value(tourPlanResponseMock.address()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllTourPlanTitle_failed() throws Exception {
        when(tourPlanService.getTourPlanByTitle(any(String.class))).thenThrow(new ResourceNotFoundException("Tour Plan not found"));

        mockMvc.perform(get("/api/tour-plan/title")
                        .param("q", "title"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Tour Plan not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllTourPlanByPrice_success() throws Exception {
        when(tourPlanService.getTourPlanByPrice(any(BigDecimal.class))).thenReturn(tourPlanResponseListMocks);

        mockMvc.perform(get("/api/tour-plan/price")
                        .param("q", "100.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(tourPlanResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$[0].address").value(tourPlanResponseListMocks.getFirst().address()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllTourPlanByPrices_success() throws Exception {
        Page<TourPlanResponse> page = new PageImpl<>(tourPlanResponseListMocks);
        when(tourPlanService.getTourPlanByPriceBetween(any(BigDecimal.class), any(BigDecimal.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/tour-plan/prices")
                        .param("min", "50.00")
                        .param("max", "150.00")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(tourPlanResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].address").value(tourPlanResponseListMocks.getFirst().address()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllTourPlanByDuration_success() throws Exception {
        when(tourPlanService.getTourPlanByDuration(anyInt())).thenReturn(tourPlanResponseListMocks);

        mockMvc.perform(get("/api/tour-plan/duration")
                        .param("q", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(tourPlanResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$[0].address").value(tourPlanResponseListMocks.getFirst().address()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllTourPlanByDurations_success() throws Exception {
        Page<TourPlanResponse> page = new PageImpl<>(tourPlanResponseListMocks);
        when(tourPlanService.getTourPlanByDurationBetween(anyInt(), anyInt(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/tour-plan/durations")
                        .param("min", "3")
                        .param("max", "7")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(tourPlanResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].address").value(tourPlanResponseListMocks.getFirst().address()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllTourPlanByAvailableSpots_success() throws Exception {
        when(tourPlanService.getTourPlanByAvailableSpots(anyInt())).thenReturn(tourPlanResponseListMocks);

        mockMvc.perform(get("/api/tour-plan/available-spots")
                        .param("q", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(tourPlanResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$[0].address").value(tourPlanResponseListMocks.getFirst().address()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllTourPlanByAvailableSpotsRange_success() throws Exception {
        Page<TourPlanResponse> page = new PageImpl<>(tourPlanResponseListMocks);
        when(tourPlanService.getTourPlanByAvailableSpotsBetween(anyInt(), anyInt(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/tour-plan/available-spots-range")
                        .param("min", "5")
                        .param("max", "15")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(tourPlanResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].address").value(tourPlanResponseListMocks.getFirst().address()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllTourPlanByProviderId_success() throws Exception {
        when(tourPlanService.getTourPlanByProviderId(anyInt())).thenReturn(tourPlanResponseListMocks);

        mockMvc.perform(get("/api/tour-plan/provider/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(tourPlanResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$[0].address").value(tourPlanResponseListMocks.getFirst().address()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllTourPlanByTitleAndPrice_success() throws Exception {
        when(tourPlanService.getTourPlanByTitleAndPrice(any(String.class), any(BigDecimal.class))).thenReturn(tourPlanResponseListMocks);

        mockMvc.perform(get("/api/tour-plan/title-price")
                        .param("t", "title")
                        .param("p", "100.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(tourPlanResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$[0].address").value(tourPlanResponseListMocks.getFirst().address()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllTourPlanByTitleAndPriceRange_success() throws Exception {
        Page<TourPlanResponse> page = new PageImpl<>(tourPlanResponseListMocks);
        when(tourPlanService.getTourPlanByTitleAndPriceBetween(any(String.class), any(BigDecimal.class), any(BigDecimal.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/tour-plan/title-price-range")
                        .param("t", "title")
                        .param("min", "50.00")
                        .param("max", "150.00")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(tourPlanResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].address").value(tourPlanResponseListMocks.getFirst().address()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllTourPlanByTitleAndPriceBetweenAndDurationBetween_success() throws Exception {
        Page<TourPlanResponse> page = new PageImpl<>(tourPlanResponseListMocks);
        when(tourPlanService.getTourPlanByTitleAndPriceBetweenAndDurationBetween(any(String.class), any(BigDecimal.class), any(BigDecimal.class), anyInt(), anyInt(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/tour-plan/title-price-duration")
                        .param("t", "title")
                        .param("minP", "50.00")
                        .param("maxP", "150.00")
                        .param("minD", "3")
                        .param("maxD", "7")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(tourPlanResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].address").value(tourPlanResponseListMocks.getFirst().address()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void searchTourPlans_success() throws Exception {
        when(tourPlanService.getTop10TourPlanByTitleContaining(any(String.class), any(Pageable.class))).thenReturn(tourPlanResponseListMocks);

        mockMvc.perform(get("/api/tour-plan/search")
                        .param("q", "title")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(tourPlanResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$[0].address").value(tourPlanResponseListMocks.getFirst().address()));
    }
}
