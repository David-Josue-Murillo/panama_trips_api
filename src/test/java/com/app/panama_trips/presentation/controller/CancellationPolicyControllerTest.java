package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.CancellationPolicyRequest;
import com.app.panama_trips.presentation.dto.CancellationPolicyResponse;
import com.app.panama_trips.service.implementation.CancellationPolicyService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static com.app.panama_trips.DataProvider.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(CancellationPolicyController.class)
public class CancellationPolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CancellationPolicyService service;

    private CancellationPolicyRequest request;
    private CancellationPolicyResponse response;
    private List<CancellationPolicyResponse> responseList;

    @BeforeEach
    void setUp() {
        request = cancellationPolicyRequestMock;
        response = cancellationPolicyResponseMock;
        responseList = cancellationPolicyResponseListMock;
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
    void getAllCancellationPolicies_success() throws Exception{
        // Given
        Page<CancellationPolicyResponse> page = new PageImpl<>(responseList);
        when(service.getAllCancellationPolicies(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/cancellation-policies")
                    .param("page", "0")
                    .param("size", "10")
                    .param("enablePagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(responseList.getFirst().id()))
                .andExpect(jsonPath("$.content[0].name").value(response.name()));
    }
}
