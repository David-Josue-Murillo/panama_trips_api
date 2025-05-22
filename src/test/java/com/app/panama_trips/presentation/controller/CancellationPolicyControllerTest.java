package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.CancellationPolicyRequest;
import com.app.panama_trips.presentation.dto.CancellationPolicyResponse;
import com.app.panama_trips.service.implementation.CancellationPolicyService;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.app.panama_trips.DataProvider.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    @DisplayName("Should return all cancellation policies with pagination when getAllCancellationPolicies is called")
    void getAllCancellationPolicies_success() throws Exception{
        // Given
        Page<CancellationPolicyResponse> page = new PageImpl<>(responseList);
        when(service.getAllCancellationPolicies(any(Pageable.class))).thenReturn(page);

        // When/Then
        mockMvc.perform(get("/api/cancellation-policies")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enablePagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(responseList.getFirst().id()))
                .andExpect(jsonPath("$.content[0].name").value(response.name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return cancellation policy by id when getCancellationPolicyById is called")
    void getCancellationPolicyById_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.getCancellationPolicyById(id)).thenReturn(response);

        // When/Then
        mockMvc.perform(get("/api/cancellation-policies/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.refundPercentage").value(response.refundPercentage()));

        verify(service).getCancellationPolicyById(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should save cancellation policy when saveCancellationPolicy is called")
    void saveCancellationPolicy_success() throws Exception {
        // Given
        when(service.saveCancellationPolicy(any(CancellationPolicyRequest.class))).thenReturn(response);

        // When/Then
        mockMvc.perform(post("/api/cancellation-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()));

        verify(service).saveCancellationPolicy(any(CancellationPolicyRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should update cancellation policy when updateCancellationPolicy is called")
    void updateCancellationPolicy_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.updateCancellationPolicy(eq(id), any(CancellationPolicyRequest.class))).thenReturn(response);

        // When/Then
        mockMvc.perform(put("/api/cancellation-policies/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()));

        verify(service).updateCancellationPolicy(eq(id), any(CancellationPolicyRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should delete cancellation policy when deleteCancellationPolicy is called")
    void deleteCancellationPolicy_success() throws Exception {
        // Given
        Integer id = 1;
        doNothing().when(service).deleteCancellationPolicy(id);

        // When/Then
        mockMvc.perform(delete("/api/cancellation-policies/{id}", id)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service).deleteCancellationPolicy(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should find policies by refund percentage when findByRefundPercentageGreaterThanEqual is called")
    void findByRefundPercentageGreaterThanEqual_success() throws Exception {
        // Given
        Integer percentage = 50;
        when(service.findByRefundPercentageGreaterThanEqual(percentage)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/cancellation-policies/refund")
                        .param("percentage", percentage.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()))
                .andExpect(jsonPath("$[0].refundPercentage").value(response.refundPercentage()));

        verify(service).findByRefundPercentageGreaterThanEqual(percentage);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should find policies by days before tour when findByDaysBeforeTourGreaterThanEqual is called")
    void findByDaysBeforeTourGreaterThanEqual_success() throws Exception {
        // Given
        Integer minDay = 5;
        when(service.findByDaysBeforeTourGreaterThanEqual(minDay)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/cancellation-policies/min-day")
                        .param("minDay", minDay.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()))
                .andExpect(jsonPath("$[0].daysBeforeTour").value(response.daysBeforeTour()));

        verify(service).findByDaysBeforeTourGreaterThanEqual(minDay);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should find policy by name when findByName is called")
    void findByName_whenFound_success() throws Exception {
        // Given
        String name = "Standard Policy";
        when(service.findByName(name)).thenReturn(Optional.of(response));

        // When/Then
        mockMvc.perform(get("/api/cancellation-policies/find")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()));

        verify(service).findByName(name);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should return empty when policy name not found")
    void findByName_whenNotFound_returnsEmpty() throws Exception {
        // Given
        String name = "Non-existent Policy";
        when(service.findByName(name)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/cancellation-policies/find")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(content().string("null")); // Empty optional serializes as null

        verify(service).findByName(name);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should find eligible policies based on parameters")
    void findEligiblePolicies_success() throws Exception {
        // Given
        Integer minPercentage = 25;
        Integer maxDays = 10;
        when(service.findEligiblePolicies(eq(minPercentage), eq(maxDays))).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/cancellation-policies/find-policies")
                        .param("minPercentage", minPercentage.toString())
                        .param("maxDays", maxDays.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findEligiblePolicies(eq(minPercentage), eq(maxDays));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get recommended policy based on days before trip")
    void getRecommendedPolicy_success() throws Exception {
        // Given
        Integer daysBeforeTrip = 10;
        when(service.getRecommendedPolicy(daysBeforeTrip)).thenReturn(response);

        // When/Then
        mockMvc.perform(get("/api/cancellation-policies/recommended")
                        .param("daysBeforeTrip", daysBeforeTrip.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.refundPercentage").value(response.refundPercentage()));

        verify(service).getRecommendedPolicy(daysBeforeTrip);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should check if policy is eligible for refund")
    void isPolicyEligibleForRefund_success() throws Exception {
        // Given
        Integer policyId = 1;
        Integer daysRemaining = 10;
        when(service.isPolicyEligibleForRefund(policyId, daysRemaining)).thenReturn(true);

        // When/Then
        mockMvc.perform(get("/api/cancellation-policies/{policyId}/eligible", policyId)
                        .param("daysRemaining", daysRemaining.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).isPolicyEligibleForRefund(policyId, daysRemaining);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should calculate refund amount based on policy and parameters")
    void calculateRefundAmount_success() throws Exception {
        // Given
        Integer policyId = 1;
        Integer totalAmount = 1000;
        Integer daysRemaining = 10;
        Integer refundAmount = 750; // 75% of 1000

        when(service.calculateRefundAmount(policyId, totalAmount, daysRemaining)).thenReturn(refundAmount);

        // When/Then
        mockMvc.perform(get("/api/cancellation-policies/{policyId}/calculate-refund", policyId)
                        .param("totalAmount", totalAmount.toString())
                        .param("daysRemaining", daysRemaining.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(refundAmount.toString()));

        verify(service).calculateRefundAmount(policyId, totalAmount, daysRemaining);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should get all active policies")
    void getActivePolicies_success() throws Exception {
        // Given
        when(service.getActivePolicies()).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/cancellation-policies/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getActivePolicies();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should create multiple policies in bulk")
    void bulkCreatePolicies_success() throws Exception {
        // Given
        List<CancellationPolicyRequest> requests = Collections.singletonList(request);
        doNothing().when(service).bulkCreatePolicies(anyList());

        // When/Then
        mockMvc.perform(post("/api/cancellation-policies/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requests))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated());

        verify(service).bulkCreatePolicies(anyList());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should update multiple policies in bulk")
    void bulkUpdatePolicies_success() throws Exception {
        // Given
        List<CancellationPolicyRequest> requests = Collections.singletonList(request);
        doNothing().when(service).bulkUpdatePolicies(anyList());

        // When/Then
        mockMvc.perform(put("/api/cancellation-policies/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requests))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(service).bulkUpdatePolicies(anyList());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should check if policy exists by name")
    void existsPolicyWithName_success() throws Exception {
        // Given
        String name = "Standard Policy";
        when(service.existsPolicyWithName(name)).thenReturn(true);

        // When/Then
        mockMvc.perform(get("/api/cancellation-policies/exists")
                        .param("name", name))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).existsPolicyWithName(name);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Should check if policy is used by any tour")
    void isPolicyUsedByAnyTour_success() throws Exception {
        // Given
        Integer policyId = 1;
        when(service.isPolicyUsedByAnyTour(policyId)).thenReturn(false);

        // When/Then
        mockMvc.perform(get("/api/cancellation-policies/{policyId}/in-use", policyId))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(service).isPolicyUsedByAnyTour(policyId);
    }
}