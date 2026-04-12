package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.PaymentStatus;
import com.app.panama_trips.presentation.dto.PaymentRequest;
import com.app.panama_trips.presentation.dto.PaymentResponse;
import com.app.panama_trips.service.interfaces.IPaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IPaymentService paymentService;

    private PaymentResponse paymentResponseMock;
    private PaymentRequest paymentRequestMock;

    @BeforeEach
    void setUp() {
        paymentResponseMock = new PaymentResponse(
                1L, 1, "TX123", BigDecimal.valueOf(100.00), PaymentStatus.PENDING,
                LocalDateTime.now(), null, null, null, "CREDIT_CARD", null
        );

        paymentRequestMock = new PaymentRequest(
                1, "TX123", BigDecimal.valueOf(100.00), PaymentStatus.PENDING,
                null, null, null, "CREDIT_CARD", null
        );
    }

    private String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllPayments_success() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(List.of(paymentResponseMock));

        mockMvc.perform(get("/api/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(paymentResponseMock.id()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void savePayment_success() throws Exception {
        when(paymentService.savePayment(any(PaymentRequest.class))).thenReturn(paymentResponseMock);

        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(paymentRequestMock))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(paymentResponseMock.id()))
                .andExpect(jsonPath("$.transactionId").value(paymentResponseMock.transactionId()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void savePayment_validationFailed() throws Exception {
        // Missing required connection string fields to simulate validation error
        PaymentRequest invalidRequest = new PaymentRequest(
                null, null, null, null, null, null, null, null, null
        );

        mockMvc.perform(post("/api/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(invalidRequest))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findPaymentById_success() throws Exception {
        when(paymentService.getPaymentById(anyLong())).thenReturn(paymentResponseMock);

        mockMvc.perform(get("/api/payments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(paymentResponseMock.id()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findPaymentById_failed() throws Exception {
        when(paymentService.getPaymentById(anyLong())).thenThrow(new ResourceNotFoundException("Payment not found"));

        mockMvc.perform(get("/api/payments/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Payment not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updatePayment_success() throws Exception {
        when(paymentService.updatePayment(anyLong(), any(PaymentRequest.class))).thenReturn(paymentResponseMock);

        mockMvc.perform(put("/api/payments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(paymentRequestMock))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(paymentResponseMock.id()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deletePayment_success() throws Exception {
        mockMvc.perform(delete("/api/payments/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }
}
