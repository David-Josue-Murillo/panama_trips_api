package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.presentation.dto.ReservationRequest;
import com.app.panama_trips.presentation.dto.ReservationResponse;
import com.app.panama_trips.presentation.dto.TourPlanResponse;
import com.app.panama_trips.service.implementation.ReservationService;
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
        return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
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
}
