package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.persistence.entity.ReservationStatus;
import com.app.panama_trips.presentation.dto.ReservationRequest;
import com.app.panama_trips.presentation.dto.ReservationResponse;
import com.app.panama_trips.service.implementation.ReservationService;
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

import static com.app.panama_trips.DataProvider.reservationRequestMock;
import static com.app.panama_trips.DataProvider.reservationResponseListMocks;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(obj);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllReservations_success() throws Exception {
        Page<ReservationResponse> page = new PageImpl<>(reservationResponseListMocks);
        when(reservationService.getAllReservations(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/reservations")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(reservationResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].nameTourPlan").value(reservationResponseListMocks.getFirst().nameTourPlan()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getReservationById_success() throws Exception {
        ReservationResponse reservationResponse = reservationResponseListMocks.getFirst();
        when(reservationService.getReservationById(anyInt())).thenReturn(reservationResponse);

        mockMvc.perform(get("/api/reservations/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reservationResponse.id()))
                .andExpect(jsonPath("$.nameTourPlan").value(reservationResponse.nameTourPlan()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createReservation_success() throws Exception {
        ReservationResponse reservationResponse = reservationResponseListMocks.getFirst();
        when(reservationService.saveReservation(any(ReservationRequest.class))).thenReturn(reservationResponse);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(reservationRequestMock))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(reservationResponse.id()))
                .andExpect(jsonPath("$.nameTourPlan").value(reservationResponse.nameTourPlan()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateStatusReservation_success() throws Exception {
        ReservationResponse reservationResponse = reservationResponseListMocks.getFirst();
        when(reservationService.updateStatusReservation(anyInt(), anyString(), anyString())).thenReturn(reservationResponse);

        mockMvc.perform(put("/api/reservations/{id}", 1)
                        .param("user", "admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"CONFIRMED\"")
                    .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reservationResponse.id()))
                .andExpect(jsonPath("$.username").value(reservationResponse.username()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteReservation_success() throws Exception {
        doNothing().when(reservationService).deleteReservation(anyInt());

        mockMvc.perform(delete("/api/reservations/{id}", 1)
                    .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getReservationsByUserId_success() throws Exception {
        Page<ReservationResponse> page = new PageImpl<>(reservationResponseListMocks);
        when(reservationService.getReservationByUserId(anyLong(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/reservations/user/{userId}", 1)
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(reservationResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].nameTourPlan").value(reservationResponseListMocks.getFirst().nameTourPlan()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getReservationsByTourPlanId_success() throws Exception {
        Page<ReservationResponse> page = new PageImpl<>(reservationResponseListMocks);
        when(reservationService.getReservationByTourPlanId(anyInt(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/reservations/tourPlan/{tourPlanId}", 1)
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(reservationResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].nameTourPlan").value(reservationResponseListMocks.getFirst().nameTourPlan()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getReservationsByStatus_success() throws Exception {
        Page<ReservationResponse> page = new PageImpl<>(reservationResponseListMocks);
        when(reservationService.getReservationByReservationStatus(anyString(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/reservations/status/{status}", "CONFIRMED")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(reservationResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].nameTourPlan").value(reservationResponseListMocks.getFirst().nameTourPlan()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getReservationsByReservationDate_success() throws Exception {
        Page<ReservationResponse> page = new PageImpl<>(reservationResponseListMocks);
        when(reservationService.getReservationByReservationDate(anyString(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/reservations/reservation-date/{reservationDate}", "2023-10-01")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                    .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(reservationResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].nameTourPlan").value(reservationResponseListMocks.getFirst().nameTourPlan()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getReservationsByUserIdAndStatus_success() throws Exception {
        Page<ReservationResponse> page = new PageImpl<>(reservationResponseListMocks);
        when(reservationService.getReservationsByUserAndStatus(anyLong(), anyString(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/reservations/user-status/{userId}/{status}", 1, "CONFIRMED")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(reservationResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].nameTourPlan").value(reservationResponseListMocks.getFirst().nameTourPlan()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getReservationsBetweenDates_success() throws Exception {
        Page<ReservationResponse> page = new PageImpl<>(reservationResponseListMocks);
        when(reservationService.getReservationsBetweenDates(any(LocalDate.class), any(LocalDate.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/reservations/date-range")
                        .param("startDate", "2023-10-01")
                        .param("endDate", "2023-10-31")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(reservationResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].nameTourPlan").value(reservationResponseListMocks.getFirst().nameTourPlan()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getReservationByMonth_success() throws Exception{
        Page<ReservationResponse> page = new PageImpl<>(reservationResponseListMocks);
        when(reservationService.getReservationsByMonth(anyShort(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/reservations/month/{month}", 10)
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(reservationResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].nameTourPlan").value(reservationResponseListMocks.getFirst().nameTourPlan()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getReservationByYear_success() throws Exception{
        Page<ReservationResponse> page = new PageImpl<>(reservationResponseListMocks);
        when(reservationService.getReservationsByYear(anyInt(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/reservations/year/{year}", 10)
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(reservationResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].nameTourPlan").value(reservationResponseListMocks.getFirst().nameTourPlan()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getReservationsWithPriceGreaterThan_success() throws Exception {
        Page<ReservationResponse> page = new PageImpl<>(reservationResponseListMocks);
        when(reservationService.getReservationsWithPriceGreaterThan(any(BigDecimal.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/reservations/price-greater-than/{price}", 10)
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(reservationResponseListMocks.getFirst().id()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getReservationsByPriceRange_success() throws Exception {
        Page<ReservationResponse> page = new PageImpl<>(reservationResponseListMocks);
        when(reservationService.getReservationsByPriceRange(any(BigDecimal.class), any(BigDecimal.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/reservations/price-range")
                        .param("min", "10")
                        .param("max", "100")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(reservationResponseListMocks.getFirst().id()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getRecentReservationsByUser_success() throws Exception {
        Page<ReservationResponse> page = new PageImpl<>(reservationResponseListMocks);
        when(reservationService.getRecentReservationsByUser(anyLong(), any(LocalDate.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/reservations/recent/{userId}", 1)
                        .param("date", "2023-10-01")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(reservationResponseListMocks.getFirst().id()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getReservationsByDayOfWeek_success() throws Exception {
        Page<ReservationResponse> page = new PageImpl<>(reservationResponseListMocks);
        when(reservationService.getReservationsByDayOfWeek(anyInt(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/reservations/day-of-week/{dayOfWeek}", 1)
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(reservationResponseListMocks.getFirst().id()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void countReservationsByStatus_success() throws Exception {
        when(reservationService.countReservationsByStatus(any(ReservationStatus.class))).thenReturn(5L);

        mockMvc.perform(get("/api/reservations/count/status/{status}", "confirmed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void countReservationsByTourPlan_success() throws Exception {
        when(reservationService.countReservationsByTourPlan(anyInt())).thenReturn(5L);

        mockMvc.perform(get("/api/reservations/count/tour-plan/{tourPlanId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5));
    }

    /*
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getReservationStatistics_success() throws Exception {
        when(reservationService.getReservationStatistics()).thenReturn(reservationResponseListMocks);

        mockMvc.perform(get("/api/reservations/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reservationResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$[0].nameTourPlan").value(reservationResponseListMocks.getFirst().nameTourPlan()));
    }
     */

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void confirmReservation_success() throws Exception {
        ReservationResponse reservationResponse = reservationResponseListMocks.getFirst();
        when(reservationService.confirmReservation(anyInt())).thenReturn(reservationResponse);

        mockMvc.perform(put("/api/reservations/confirm/{id}", 1)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reservationResponse.id()))
                .andExpect(jsonPath("$.nameTourPlan").value(reservationResponse.nameTourPlan()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void cancelReservation_success() throws Exception {
        ReservationResponse reservationResponse = reservationResponseListMocks.getFirst();
        when(reservationService.cancelReservation(anyInt())).thenReturn(reservationResponse);

        mockMvc.perform(put("/api/reservations/cancel/{id}", 1)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reservationResponse.id()))
                .andExpect(jsonPath("$.nameTourPlan").value(reservationResponse.nameTourPlan()));
    }
}
