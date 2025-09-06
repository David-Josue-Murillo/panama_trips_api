package com.app.panama_trips.presentation.controller;

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

import com.app.panama_trips.presentation.dto.TourFaqRequest;
import com.app.panama_trips.presentation.dto.TourFaqResponse;
import com.app.panama_trips.service.implementation.TourFaqService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static com.app.panama_trips.DataProvider.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TourFaqController.class)
public class TourFaqControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TourFaqService service;

    private TourFaqRequest request;
    private TourFaqResponse response;
    private List<TourFaqResponse> responsesList;

    @BeforeEach
    void setUp() {
        request = tourFaqRequestMock;
        response = tourFaqResponseMock;
        responsesList = tourFaqResponseListMock;
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
    @DisplayName("Should return all FAQs with pagination when getAll is called")
    void getAll_success() throws Exception {
        // Given
        Page<TourFaqResponse> page = new PageImpl<>(responsesList);
        when(service.getAllFaqs(any(Pageable.class))).thenReturn(page);

        // When/Then
        mockMvc.perform(get("/api/tour-faq")
                .param("page", "0")
                .param("size", "10")
                .param("enabledPagination", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(response.id()))
                .andExpect(jsonPath("$.content[0].question").value(response.question()));

        verify(service).getAllFaqs(any(Pageable.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should return FAQ by id when exists")
    void getById_whenExists_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.getFaqById(id)).thenReturn(response);

        // When/Then
        mockMvc.perform(get("/api/tour-faq/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.question").value(response.question()))
                .andExpect(jsonPath("$.answer").value(response.answer()));

        verify(service).getFaqById(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should create FAQ successfully")
    void create_success() throws Exception {
        // Given
        when(service.saveFaq(any(TourFaqRequest.class))).thenReturn(response);

        // When/Then
        mockMvc.perform(post("/api/tour-faq")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.question").value(response.question()));

        verify(service).saveFaq(any(TourFaqRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should update FAQ successfully")
    void update_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.updateFaq(eq(id), any(TourFaqRequest.class))).thenReturn(response);

        // When/Then
        mockMvc.perform(put("/api/tour-faq/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.question").value(response.question()));

        verify(service).updateFaq(eq(id), any(TourFaqRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should delete FAQ successfully")
    void delete_success() throws Exception {
        // Given
        Integer id = 1;
        doNothing().when(service).deleteFaq(id);

        // When/Then
        mockMvc.perform(delete("/api/tour-faq/{id}", id)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service).deleteFaq(id);
    }

    // Find Operations by Tour Plan Tests
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get FAQs by tour plan ID")
    void findByTourPlanId_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        when(service.findByTourPlanId(tourPlanId)).thenReturn(responsesList);

        // When/Then
        mockMvc.perform(get("/api/tour-faq/tour-plan/{tourPlanId}", tourPlanId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()))
                .andExpect(jsonPath("$[0].tourPlanId").value(response.tourPlanId()));

        verify(service).findByTourPlanId(tourPlanId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get FAQs by tour plan ID ordered by display order")
    void findByTourPlanIdOrdered_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        when(service.findByTourPlanIdOrderByDisplayOrderAsc(tourPlanId)).thenReturn(responsesList);

        // When/Then
        mockMvc.perform(get("/api/tour-faq/tour-plan/{tourPlanId}/ordered", tourPlanId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findByTourPlanIdOrderByDisplayOrderAsc(tourPlanId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get top FAQs by tour plan with limit")
    void getTopFaqsByTourPlan_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        int limit = 3;
        when(service.getTopFaqsByTourPlan(tourPlanId, limit)).thenReturn(responsesList);

        // When/Then
        mockMvc.perform(get("/api/tour-faq/tour-plan/{tourPlanId}/top/{limit}", tourPlanId, limit))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getTopFaqsByTourPlan(tourPlanId, limit);
    }

    // Search Operations Tests
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should search FAQs by keyword")
    void searchByKeyword_success() throws Exception {
        // Given
        String keyword = "duración";
        when(service.searchByQuestionOrAnswer(keyword)).thenReturn(responsesList);

        // When/Then
        mockMvc.perform(get("/api/tour-faq/search")
                .param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).searchByQuestionOrAnswer(keyword);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find FAQ by tour plan ID and question when exists")
    void findByTourPlanIdAndQuestion_whenExists_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        String question = "¿Cuál es la duración del tour?";
        when(service.findByTourPlanIdAndQuestion(tourPlanId, question)).thenReturn(Optional.of(response));

        // When/Then
        mockMvc.perform(get("/api/tour-faq/tour-plan/{tourPlanId}/question", tourPlanId)
                .param("question", question))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.question").value(response.question()));

        verify(service).findByTourPlanIdAndQuestion(tourPlanId, question);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should return 404 when FAQ by tour plan ID and question not found")
    void findByTourPlanIdAndQuestion_whenNotExists_returnsNotFound() throws Exception {
        // Given
        Integer tourPlanId = 1;
        String question = "¿Pregunta que no existe?";
        when(service.findByTourPlanIdAndQuestion(tourPlanId, question)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/tour-faq/tour-plan/{tourPlanId}/question", tourPlanId)
                .param("question", question))
                .andExpect(status().isNotFound());

        verify(service).findByTourPlanIdAndQuestion(tourPlanId, question);
    }

    // Bulk Operations Tests
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should bulk create FAQs successfully")
    void bulkCreate_success() throws Exception {
        // Given
        List<TourFaqRequest> requests = tourFaqRequestListForBulkCreateMock();
        doNothing().when(service).bulkCreateFaqs(requests);

        // When/Then
        mockMvc.perform(post("/api/tour-faq/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requests))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated());

        verify(service).bulkCreateFaqs(requests);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should bulk update FAQs successfully")
    void bulkUpdate_success() throws Exception {
        // Given
        List<TourFaqRequest> requests = tourFaqRequestListForBulkUpdateMock();
        doNothing().when(service).bulkUpdateFaqs(requests);

        // When/Then
        mockMvc.perform(put("/api/tour-faq/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requests))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(service).bulkUpdateFaqs(requests);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should bulk delete FAQs successfully")
    void bulkDelete_success() throws Exception {
        // Given
        List<Integer> faqIds = tourFaqIdsForBulkDeleteMock();
        doNothing().when(service).bulkDeleteFaqs(faqIds);

        // When/Then
        mockMvc.perform(delete("/api/tour-faq/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(faqIds))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service).bulkDeleteFaqs(faqIds);
    }

    // Reorder Operations Tests
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should reorder FAQs successfully")
    void reorderFaqs_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        List<Integer> faqIdsInOrder = tourFaqIdsForReorderMock();
        doNothing().when(service).reorderFaqs(tourPlanId, faqIdsInOrder);

        // When/Then
        mockMvc.perform(put("/api/tour-faq/tour-plan/{tourPlanId}/reorder", tourPlanId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(faqIdsInOrder))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(service).reorderFaqs(tourPlanId, faqIdsInOrder);
    }

}
