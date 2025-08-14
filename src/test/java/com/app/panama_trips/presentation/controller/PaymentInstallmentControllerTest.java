package com.app.panama_trips.presentation.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
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

import com.app.panama_trips.presentation.dto.PaymentInstallmentRequest;
import com.app.panama_trips.presentation.dto.PaymentInstallmentResponse;
import com.app.panama_trips.service.implementation.PaymentInstallmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static com.app.panama_trips.DataProvider.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentInstallmentController.class)
public class PaymentInstallmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentInstallmentService service;

    private PaymentInstallmentRequest request;
    private PaymentInstallmentResponse response;
    private List<PaymentInstallmentResponse> responseList;

    @BeforeEach
    void setUp() {
        request = paymentInstallmentRequestMock;
        response = paymentInstallmentResponseMock;
        responseList = paymentInstallmentResponseListMock;
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
    @DisplayName("Should return all payment installments with pagination when getAll is called")
    void getAll_success() throws Exception {
        // Given
        Page<PaymentInstallmentResponse> page = new PageImpl<>(responseList);
        when(service.getAllPaymentInstallments(any(Pageable.class))).thenReturn(page);

        // When/Then
        mockMvc.perform(get("/api/payment-installments")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(responseList.getFirst().id()));

        verify(service).getAllPaymentInstallments(any(Pageable.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should return payment installment by id when getById is called")
    void getById_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.getPaymentInstallmentById(id)).thenReturn(response);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()));

        verify(service).getPaymentInstallmentById(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should create payment installment when create is called")
    void create_success() throws Exception {
        // Given
        when(service.savePaymentInstallment(any(PaymentInstallmentRequest.class))).thenReturn(response);

        // When/Then
        mockMvc.perform(post("/api/payment-installments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()));

        verify(service).savePaymentInstallment(any(PaymentInstallmentRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should update payment installment when update is called")
    void update_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.updatePaymentInstallment(eq(id), any(PaymentInstallmentRequest.class))).thenReturn(response);

        // When/Then
        mockMvc.perform(put("/api/payment-installments/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()));

        verify(service).updatePaymentInstallment(eq(id), any(PaymentInstallmentRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should delete payment installment when delete is called")
    void delete_success() throws Exception {
        // Given
        Integer id = 1;
        doNothing().when(service).deletePaymentInstallment(id);

        // When/Then
        mockMvc.perform(delete("/api/payment-installments/{id}", id)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service).deletePaymentInstallment(id);
    }

    // Find operations by entity relationships
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find payment installments by reservation id when findByReservationId is called")
    void findByReservationId_success() throws Exception {
        // Given
        Integer reservationId = 1;
        when(service.findByReservationId(reservationId)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/reservation/{reservationId}", reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findByReservationId(reservationId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find payment installments by payment id when findByPaymentId is called")
    void findByPaymentId_success() throws Exception {
        // Given
        Integer paymentId = 1;
        when(service.findByPaymentId(paymentId)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/payment/{paymentId}", paymentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findByPaymentId(paymentId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find payment installments by status when findByStatus is called")
    void findByStatus_success() throws Exception {
        // Given
        String status = "PENDING";
        when(service.findByStatus(status)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/status/{status}", status))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findByStatus(status);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find payment installments by due date before when findByDueDateBefore is called")
    void findByDueDateBefore_success() throws Exception {
        // Given
        LocalDate date = LocalDate.now();
        when(service.findByDueDateBefore(date)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/due-before/{date}", date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findByDueDateBefore(date);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find payment installments by due date between when findByDueDateBetween is called")
    void findByDueDateBetween_success() throws Exception {
        // Given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(30);
        when(service.findByDueDateBetween(startDate, endDate)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/due-between")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findByDueDateBetween(startDate, endDate);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find payment installments by reminder sent when findByReminderSent is called")
    void findByReminderSent_success() throws Exception {
        // Given
        Boolean reminderSent = true;
        when(service.findByReminderSent(reminderSent)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/reminder-sent/{reminderSent}", reminderSent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findByReminderSent(reminderSent);
    }

}
