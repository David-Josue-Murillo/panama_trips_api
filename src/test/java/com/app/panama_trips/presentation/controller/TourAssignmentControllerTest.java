package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.persistence.entity.Guide;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.presentation.controller.TourAssignmentController;
import com.app.panama_trips.presentation.dto.TourAssignmentRequest;
import com.app.panama_trips.presentation.dto.TourAssignmentResponse;
import com.app.panama_trips.service.implementation.TourAssignmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.app.panama_trips.DataProvider.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TourAssignmentController.class)
public class TourAssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TourAssignmentService service;

    private TourAssignmentRequest request;
    private TourAssignmentResponse response;
    private List<TourAssignmentResponse> responsesList;

    @BeforeEach
    void setUp() {
        request = tourAssignmentRequestMock;
        response = tourAssignmentResponseMock;
        responsesList = tourAssignmentResponseListMock;
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
    @DisplayName("Should return all tour assignments with pagination when getAllAssignments is called")
    void getAllAssignments_success() throws Exception {
        // Given
        Page<TourAssignmentResponse> page = new PageImpl<>(responsesList);
        when(service.getAllAssignments(any(Pageable.class))).thenReturn(page);

        // When/Then
        mockMvc.perform(get("/api/tour-assignments")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(response.id()))
                .andExpect(jsonPath("$.content[0].status").value(response.status()));

        verify(service).getAllAssignments(any(Pageable.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return tour assignment by id when exists")
    void getAssignmentById_whenExists_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.getAssignmentById(id)).thenReturn(Optional.of(response));

        // When/Then
        mockMvc.perform(get("/api/tour-assignments/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.status").value(response.status()));

        verify(service).getAssignmentById(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return 404 when tour assignment not found")
    void getAssignmentById_whenNotExists_returnsNotFound() throws Exception {
        // Given
        Integer id = 999;
        when(service.getAssignmentById(id)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/tour-assignments/{id}", id))
                .andExpect(status().isNotFound());

        verify(service).getAssignmentById(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should create tour assignment successfully")
    void createAssignment_success() throws Exception {
        // Given
        when(service.createAssignment(any(TourAssignmentRequest.class))).thenReturn(response);

        // When/Then
        mockMvc.perform(post("/api/tour-assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.status").value(response.status()));

        verify(service).createAssignment(any(TourAssignmentRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should update tour assignment successfully")
    void updateAssignment_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.updateAssignment(eq(id), any(TourAssignmentRequest.class))).thenReturn(response);

        // When/Then
        mockMvc.perform(put("/api/tour-assignments/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.status").value(response.status()));

        verify(service).updateAssignment(eq(id), any(TourAssignmentRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should delete tour assignment successfully")
    void deleteAssignment_success() throws Exception {
        // Given
        Integer id = 1;
        doNothing().when(service).deleteAssignment(id);

        // When/Then
        mockMvc.perform(delete("/api/tour-assignments/{id}", id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service).deleteAssignment(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get assignments by guide")
    void getAssignmentsByGuide_success() throws Exception {
        // Given
        Integer guideId = 1;
        when(service.getAssignmentsByGuide(any(Guide.class))).thenReturn(responsesList);

        // When/Then
        mockMvc.perform(get("/api/tour-assignments/guide/{guideId}", guideId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getAssignmentsByGuide(any(Guide.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get assignments by tour plan")
    void getAssignmentsByTourPlan_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        when(service.getAssignmentsByTourPlan(any(TourPlan.class))).thenReturn(responsesList);

        // When/Then
        mockMvc.perform(get("/api/tour-assignments/tour-plan/{tourPlanId}", tourPlanId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getAssignmentsByTourPlan(any(TourPlan.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get assignments by status")
    void getAssignmentsByStatus_success() throws Exception {
        // Given
        String status = "ASSIGNED";
        when(service.getAssignmentsByStatus(status)).thenReturn(responsesList);

        // When/Then
        mockMvc.perform(get("/api/tour-assignments/status/{status}", status))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getAssignmentsByStatus(status);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get assignments by date")
    void getAssignmentsByDate_success() throws Exception {
        // Given
        LocalDate date = LocalDate.now();
        when(service.getAssignmentsByDate(date)).thenReturn(responsesList);

        // When/Then
        mockMvc.perform(get("/api/tour-assignments/date/{date}", date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getAssignmentsByDate(date);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get assignments by date range")
    void getAssignmentsByDateRange_success() throws Exception {
        // Given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        when(service.getAssignmentsByDateRange(startDate, endDate)).thenReturn(responsesList);

        // When/Then
        mockMvc.perform(get("/api/tour-assignments/date-range")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getAssignmentsByDateRange(startDate, endDate);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get assignments by guide and status")
    void getAssignmentsByGuideAndStatus_success() throws Exception {
        // Given
        Integer guideId = 1;
        String status = "ASSIGNED";
        when(service.getAssignmentsByGuideAndStatus(any(Guide.class), eq(status))).thenReturn(responsesList);

        // When/Then
        mockMvc.perform(get("/api/tour-assignments/guide/{guideId}/status/{status}", guideId, status))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getAssignmentsByGuideAndStatus(any(Guide.class), eq(status));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get assignments by tour plan and date range")
    void getAssignmentsByTourPlanAndDateRange_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        when(service.getAssignmentsByTourPlanAndDateRange(any(TourPlan.class), eq(startDate), eq(endDate)))
                .thenReturn(responsesList);

        // When/Then
        mockMvc.perform(get("/api/tour-assignments/tour-plan/{tourPlanId}/date-range", tourPlanId)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getAssignmentsByTourPlanAndDateRange(any(TourPlan.class), eq(startDate), eq(endDate));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get assignment by guide, tour plan and date when exists")
    void getAssignmentByGuideTourPlanAndDate_whenExists_success() throws Exception {
        // Given
        Integer guideId = 1;
        Integer tourPlanId = 1;
        LocalDate date = LocalDate.now();
        when(service.getAssignmentByGuideTourPlanAndDate(any(Guide.class), any(TourPlan.class), eq(date)))
                .thenReturn(Optional.of(response));

        // When/Then
        mockMvc.perform(get("/api/tour-assignments/guide-tour-plan-date")
                        .param("guideId", guideId.toString())
                        .param("tourPlanId", tourPlanId.toString())
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()));

        verify(service).getAssignmentByGuideTourPlanAndDate(any(Guide.class), any(TourPlan.class), eq(date));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return 404 when assignment by guide, tour plan and date not found")
    void getAssignmentByGuideTourPlanAndDate_whenNotExists_returnsNotFound() throws Exception {
        // Given
        Integer guideId = 1;
        Integer tourPlanId = 1;
        LocalDate date = LocalDate.now();
        when(service.getAssignmentByGuideTourPlanAndDate(any(Guide.class), any(TourPlan.class), eq(date)))
                .thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/tour-assignments/guide-tour-plan-date")
                        .param("guideId", guideId.toString())
                        .param("tourPlanId", tourPlanId.toString())
                        .param("date", date.toString()))
                .andExpect(status().isNotFound());

        verify(service).getAssignmentByGuideTourPlanAndDate(any(Guide.class), any(TourPlan.class), eq(date));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get upcoming assignments by guide")
    void getUpcomingAssignmentsByGuide_success() throws Exception {
        // Given
        Integer guideId = 1;
        LocalDate startDate = LocalDate.now();
        when(service.getUpcomingAssignmentsByGuide(eq(guideId), eq(startDate))).thenReturn(responsesList);

        // When/Then
        mockMvc.perform(get("/api/tour-assignments/guide/{guideId}/upcoming", guideId)
                        .param("startDate", startDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getUpcomingAssignmentsByGuide(eq(guideId), eq(startDate));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should count assignments by guide and status")
    void countAssignmentsByGuideAndStatus_success() throws Exception {
        // Given
        Integer guideId = 1;
        String status = "ASSIGNED";
        Long expectedCount = 5L;
        when(service.countAssignmentsByGuideAndStatus(eq(guideId), eq(status))).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/tour-assignments/guide/{guideId}/status/{status}/count", guideId, status))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).countAssignmentsByGuideAndStatus(eq(guideId), eq(status));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should check guide availability for date")
    void isGuideAvailableForDate_success() throws Exception {
        // Given
        Integer guideId = 1;
        LocalDate date = LocalDate.now();
        when(service.isGuideAvailableForDate(any(Guide.class), eq(date))).thenReturn(true);

        // When/Then
        mockMvc.perform(get("/api/tour-assignments/guide/{guideId}/availability", guideId)
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).isGuideAvailableForDate(any(Guide.class), eq(date));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should update assignment status")
    void updateAssignmentStatus_success() throws Exception {
        // Given
        Integer id = 1;
        String newStatus = "COMPLETED";
        when(service.updateAssignmentStatus(eq(id), eq(newStatus))).thenReturn(response);

        // When/Then
        mockMvc.perform(patch("/api/tour-assignments/{id}/status", id)
                        .param("newStatus", newStatus)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.status").value(response.status()));

        verify(service).updateAssignmentStatus(eq(id), eq(newStatus));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should add notes to assignment")
    void addNotesToAssignment_success() throws Exception {
        // Given
        Integer id = 1;
        String notes = "New notes";
        when(service.addNotesToAssignment(eq(id), eq(notes))).thenReturn(response);

        // When/Then
        mockMvc.perform(patch("/api/tour-assignments/{id}/notes", id)
                        .param("notes", notes)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.notes").value(response.notes()));

        verify(service).addNotesToAssignment(eq(id), eq(notes));
    }
}
