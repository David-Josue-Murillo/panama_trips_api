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
}
