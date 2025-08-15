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

    // Specialized queries
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find payment installments by reservation id and status when findByReservationIdAndStatus is called")
    void findByReservationIdAndStatus_success() throws Exception {
        // Given
        Integer reservationId = 1;
        String status = "PENDING";
        when(service.findByReservationIdAndStatus(reservationId, status)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(
                get("/api/payment-installments/reservation/{reservationId}/status/{status}", reservationId, status))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findByReservationIdAndStatus(reservationId, status);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find pending installments without reminder when findPendingInstallmentsWithoutReminder is called")
    void findPendingInstallmentsWithoutReminder_success() throws Exception {
        // Given
        LocalDate date = LocalDate.now();
        when(service.findPendingInstallmentsWithoutReminder(date)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/pending-without-reminder/{date}", date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findPendingInstallmentsWithoutReminder(date);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should sum pending amount by reservation when sumPendingAmountByReservation is called")
    void sumPendingAmountByReservation_success() throws Exception {
        // Given
        Integer reservationId = 1;
        BigDecimal expectedAmount = new BigDecimal("1000.00");
        when(service.sumPendingAmountByReservation(reservationId)).thenReturn(expectedAmount);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/sum-pending/{reservationId}", reservationId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAmount.toString()));

        verify(service).sumPendingAmountByReservation(reservationId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should count overdue installments when countOverdueInstallments is called")
    void countOverdueInstallments_success() throws Exception {
        // Given
        String status = "OVERDUE";
        LocalDate date = LocalDate.now();
        Long expectedCount = 5L;
        when(service.countOverdueInstallments(status, date)).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/count-overdue/{status}/{date}", status, date))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).countOverdueInstallments(status, date);
    }

    // Business logic operations
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get overdue installments when getOverdueInstallments is called")
    void getOverdueInstallments_success() throws Exception {
        // Given
        when(service.getOverdueInstallments()).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/overdue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getOverdueInstallments();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get pending installments when getPendingInstallments is called")
    void getPendingInstallments_success() throws Exception {
        // Given
        when(service.getPendingInstallments()).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getPendingInstallments();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get paid installments when getPaidInstallments is called")
    void getPaidInstallments_success() throws Exception {
        // Given
        when(service.getPaidInstallments()).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/paid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getPaidInstallments();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get cancelled installments when getCancelledInstallments is called")
    void getCancelledInstallments_success() throws Exception {
        // Given
        when(service.getCancelledInstallments()).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/cancelled"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getCancelledInstallments();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get installments requiring reminder when getInstallmentsRequiringReminder is called")
    void getInstallmentsRequiringReminder_success() throws Exception {
        // Given
        when(service.getInstallmentsRequiringReminder()).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/requiring-reminder"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getInstallmentsRequiringReminder();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get installments by date range when getInstallmentsByDateRange is called")
    void getInstallmentsByDateRange_success() throws Exception {
        // Given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(30);
        when(service.getInstallmentsByDateRange(startDate, endDate)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/date-range")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getInstallmentsByDateRange(startDate, endDate);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get installments by reservation and status when getInstallmentsByReservationAndStatus is called")
    void getInstallmentsByReservationAndStatus_success() throws Exception {
        // Given
        Integer reservationId = 1;
        String status = "PENDING";
        when(service.getInstallmentsByReservationAndStatus(reservationId, status)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/reservation/{reservationId}/status/{status}/installments",
                reservationId, status))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getInstallmentsByReservationAndStatus(reservationId, status);
    }

    // Advanced queries
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get recent installments when getRecentInstallments is called")
    void getRecentInstallments_success() throws Exception {
        // Given
        int limit = 10;
        when(service.getRecentInstallments(limit)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/recent/{limit}", limit))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getRecentInstallments(limit);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get installments by amount range when getInstallmentsByAmountRange is called")
    void getInstallmentsByAmountRange_success() throws Exception {
        // Given
        BigDecimal minAmount = new BigDecimal("100.00");
        BigDecimal maxAmount = new BigDecimal("1000.00");
        when(service.getInstallmentsByAmountRange(minAmount, maxAmount)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/amount-range")
                .param("minAmount", minAmount.toString())
                .param("maxAmount", maxAmount.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getInstallmentsByAmountRange(minAmount, maxAmount);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get installments by user when getInstallmentsByUser is called")
    void getInstallmentsByUser_success() throws Exception {
        // Given
        Long userId = 1L;
        when(service.getInstallmentsByUser(userId)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getInstallmentsByUser(userId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get installments by tour plan when getInstallmentsByTourPlan is called")
    void getInstallmentsByTourPlan_success() throws Exception {
        // Given
        Integer tourPlanId = 1;
        when(service.getInstallmentsByTourPlan(tourPlanId)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/tour-plan/{tourPlanId}", tourPlanId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getInstallmentsByTourPlan(tourPlanId);
    }

    // Bulk operations
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should create multiple payment installments in bulk when bulkCreate is called")
    void bulkCreate_success() throws Exception {
        // Given
        List<PaymentInstallmentRequest> requests = Collections.singletonList(request);
        doNothing().when(service).bulkCreatePaymentInstallments(anyList());

        // When/Then
        mockMvc.perform(post("/api/payment-installments/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requests))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated());

        verify(service).bulkCreatePaymentInstallments(anyList());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should update multiple payment installments in bulk when bulkUpdate is called")
    void bulkUpdate_success() throws Exception {
        // Given
        List<PaymentInstallmentRequest> requests = Collections.singletonList(request);
        doNothing().when(service).bulkUpdatePaymentInstallments(anyList());

        // When/Then
        mockMvc.perform(put("/api/payment-installments/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requests))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(service).bulkUpdatePaymentInstallments(anyList());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should delete multiple payment installments in bulk when bulkDelete is called")
    void bulkDelete_success() throws Exception {
        // Given
        List<Integer> installmentIds = Collections.singletonList(1);
        doNothing().when(service).bulkDeletePaymentInstallments(anyList());

        // When/Then
        mockMvc.perform(delete("/api/payment-installments/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(installmentIds))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service).bulkDeletePaymentInstallments(anyList());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should update status of multiple payment installments in bulk when bulkUpdateStatus is called")
    void bulkUpdateStatus_success() throws Exception {
        // Given
        List<Integer> installmentIds = Collections.singletonList(1);
        String newStatus = "PAID";
        doNothing().when(service).bulkUpdateStatus(anyList(), eq(newStatus));

        // When/Then
        mockMvc.perform(put("/api/payment-installments/bulk/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(installmentIds))
                .param("newStatus", newStatus)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(service).bulkUpdateStatus(anyList(), eq(newStatus));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should mark multiple payment installments as reminder sent in bulk when bulkMarkAsReminderSent is called")
    void bulkMarkAsReminderSent_success() throws Exception {
        // Given
        List<Integer> installmentIds = Collections.singletonList(1);
        doNothing().when(service).bulkMarkAsReminderSent(anyList());

        // When/Then
        mockMvc.perform(put("/api/payment-installments/bulk/reminder-sent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(installmentIds))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(service).bulkMarkAsReminderSent(anyList());
    }    

    // Check operations
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should check if payment installment exists by id when existsById is called")
    void existsById_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.existsById(id)).thenReturn(true);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/exists/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).existsById(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should check if payment installment exists by reservation id when existsByReservationId is called")
    void existsByReservationId_success() throws Exception {
        // Given
        Integer reservationId = 1;
        when(service.existsByReservationId(reservationId)).thenReturn(true);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/exists/reservation/{reservationId}", reservationId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).existsByReservationId(reservationId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should check if payment installment exists by payment id when existsByPaymentId is called")
    void existsByPaymentId_success() throws Exception {
        // Given
        Integer paymentId = 1;
        when(service.existsByPaymentId(paymentId)).thenReturn(true);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/exists/payment/{paymentId}", paymentId))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).existsByPaymentId(paymentId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should count payment installments by reservation id when countByReservationId is called")
    void countByReservationId_success() throws Exception {
        // Given
        Integer reservationId = 1;
        Long expectedCount = 5L;
        when(service.countByReservationId(reservationId)).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/count/reservation/{reservationId}", reservationId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).countByReservationId(reservationId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should count payment installments by status when countByStatus is called")
    void countByStatus_success() throws Exception {
        // Given
        String status = "PENDING";
        Long expectedCount = 10L;
        when(service.countByStatus(status)).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/count/status/{status}", status))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).countByStatus(status);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should count payment installments by due date before when countByDueDateBefore is called")
    void countByDueDateBefore_success() throws Exception {
        // Given
        LocalDate date = LocalDate.now();
        Long expectedCount = 3L;
        when(service.countByDueDateBefore(date)).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/count/due-before/{date}", date))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).countByDueDateBefore(date);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should count payment installments by reminder sent when countByReminderSent is called")
    void countByReminderSent_success() throws Exception {
        // Given
        Boolean reminderSent = true;
        Long expectedCount = 7L;
        when(service.countByReminderSent(reminderSent)).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/count/reminder-sent/{reminderSent}", reminderSent))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).countByReminderSent(reminderSent);
    }

    // Financial operations
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should calculate total amount for reservation when calculateTotalAmountForReservation is called")
    void calculateTotalAmountForReservation_success() throws Exception {
        // Given
        Integer reservationId = 1;
        BigDecimal expectedAmount = new BigDecimal("2500.00");
        when(service.calculateTotalAmountForReservation(reservationId)).thenReturn(expectedAmount);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/total-amount/reservation/{reservationId}", reservationId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAmount.toString()));

        verify(service).calculateTotalAmountForReservation(reservationId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should calculate total pending amount for reservation when calculateTotalPendingAmountForReservation is called")
    void calculateTotalPendingAmountForReservation_success() throws Exception {
        // Given
        Integer reservationId = 1;
        BigDecimal expectedAmount = new BigDecimal("1500.00");
        when(service.calculateTotalPendingAmountForReservation(reservationId)).thenReturn(expectedAmount);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/total-pending/reservation/{reservationId}", reservationId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAmount.toString()));

        verify(service).calculateTotalPendingAmountForReservation(reservationId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should calculate total overdue amount for reservation when calculateTotalOverdueAmountForReservation is called")
    void calculateTotalOverdueAmountForReservation_success() throws Exception {
        // Given
        Integer reservationId = 1;
        BigDecimal expectedAmount = new BigDecimal("500.00");
        when(service.calculateTotalOverdueAmountForReservation(reservationId)).thenReturn(expectedAmount);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/total-overdue/reservation/{reservationId}", reservationId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAmount.toString()));

        verify(service).calculateTotalOverdueAmountForReservation(reservationId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should calculate total late fees for reservation when calculateTotalLateFeesForReservation is called")
    void calculateTotalLateFeesForReservation_success() throws Exception {
        // Given
        Integer reservationId = 1;
        BigDecimal expectedAmount = new BigDecimal("75.00");
        when(service.calculateTotalLateFeesForReservation(reservationId)).thenReturn(expectedAmount);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/total-late-fees/reservation/{reservationId}", reservationId))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAmount.toString()));

        verify(service).calculateTotalLateFeesForReservation(reservationId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should calculate total amount by date range when calculateTotalAmountByDateRange is called")
    void calculateTotalAmountByDateRange_success() throws Exception {
        // Given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(30);
        BigDecimal expectedAmount = new BigDecimal("5000.00");
        when(service.calculateTotalAmountByDateRange(startDate, endDate)).thenReturn(expectedAmount);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/total-amount/date-range")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAmount.toString()));

        verify(service).calculateTotalAmountByDateRange(startDate, endDate);
    }

    // Statistics and analytics
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get total installments when getTotalInstallments is called")
    void getTotalInstallments_success() throws Exception {
        // Given
        Long expectedCount = 100L;
        when(service.getTotalInstallments()).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/stats/total"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).getTotalInstallments();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get total pending installments when getTotalPendingInstallments is called")
    void getTotalPendingInstallments_success() throws Exception {
        // Given
        Long expectedCount = 30L;
        when(service.getTotalPendingInstallments()).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/stats/pending"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).getTotalPendingInstallments();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get total paid installments when getTotalPaidInstallments is called")
    void getTotalPaidInstallments_success() throws Exception {
        // Given
        Long expectedCount = 60L;
        when(service.getTotalPaidInstallments()).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/stats/paid"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).getTotalPaidInstallments();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get total overdue installments when getTotalOverdueInstallments is called")
    void getTotalOverdueInstallments_success() throws Exception {
        // Given
        Long expectedCount = 8L;
        when(service.getTotalOverdueInstallments()).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/stats/overdue"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).getTotalOverdueInstallments();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get total cancelled installments when getTotalCancelledInstallments is called")
    void getTotalCancelledInstallments_success() throws Exception {
        // Given
        Long expectedCount = 2L;
        when(service.getTotalCancelledInstallments()).thenReturn(expectedCount);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/stats/cancelled"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedCount.toString()));

        verify(service).getTotalCancelledInstallments();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get total amount pending when getTotalAmountPending is called")
    void getTotalAmountPending_success() throws Exception {
        // Given
        BigDecimal expectedAmount = new BigDecimal("15000.00");
        when(service.getTotalAmountPending()).thenReturn(expectedAmount);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/stats/amount-pending"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAmount.toString()));

        verify(service).getTotalAmountPending();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get total amount paid when getTotalAmountPaid is called")
    void getTotalAmountPaid_success() throws Exception {
        // Given
        BigDecimal expectedAmount = new BigDecimal("45000.00");
        when(service.getTotalAmountPaid()).thenReturn(expectedAmount);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/stats/amount-paid"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAmount.toString()));

        verify(service).getTotalAmountPaid();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get total amount overdue when getTotalAmountOverdue is called")
    void getTotalAmountOverdue_success() throws Exception {
        // Given
        BigDecimal expectedAmount = new BigDecimal("3000.00");
        when(service.getTotalAmountOverdue()).thenReturn(expectedAmount);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/stats/amount-overdue"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAmount.toString()));

        verify(service).getTotalAmountOverdue();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get total late fees when getTotalLateFees is called")
    void getTotalLateFees_success() throws Exception {
        // Given
        BigDecimal expectedAmount = new BigDecimal("450.00");
        when(service.getTotalLateFees()).thenReturn(expectedAmount);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/stats/late-fees"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedAmount.toString()));

        verify(service).getTotalLateFees();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get payment success rate when getPaymentSuccessRate is called")
    void getPaymentSuccessRate_success() throws Exception {
        // Given
        Double expectedRate = 60.0;
        when(service.getPaymentSuccessRate()).thenReturn(expectedRate);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/stats/success-rate"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedRate.toString()));

        verify(service).getPaymentSuccessRate();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get top reservations by installment count when getTopReservationsByInstallmentCount is called")
    void getTopReservationsByInstallmentCount_success() throws Exception {
        // Given
        int limit = 10;
        when(service.getTopReservationsByInstallmentCount(limit)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/stats/top-reservations/{limit}", limit))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getTopReservationsByInstallmentCount(limit);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get installments by month when getInstallmentsByMonth is called")
    void getInstallmentsByMonth_success() throws Exception {
        // Given
        when(service.getInstallmentsByMonth()).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/stats/by-month"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getInstallmentsByMonth();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get installments by day of week when getInstallmentsByDayOfWeek is called")
    void getInstallmentsByDayOfWeek_success() throws Exception {
        // Given
        when(service.getInstallmentsByDayOfWeek()).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/stats/by-day-of-week"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getInstallmentsByDayOfWeek();
    }

    // Status management operations
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should mark payment installment as paid when markAsPaid is called")
    void markAsPaid_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.markAsPaid(id)).thenReturn(response);

        // When/Then
        mockMvc.perform(put("/api/payment-installments/{id}/mark-as-paid", id)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()));

        verify(service).markAsPaid(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should mark payment installment as overdue when markAsOverdue is called")
    void markAsOverdue_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.markAsOverdue(id)).thenReturn(response);

        // When/Then
        mockMvc.perform(put("/api/payment-installments/{id}/mark-as-overdue", id)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()));

        verify(service).markAsOverdue(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should mark payment installment as cancelled when markAsCancelled is called")
    void markAsCancelled_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.markAsCancelled(id)).thenReturn(response);

        // When/Then
        mockMvc.perform(put("/api/payment-installments/{id}/mark-as-cancelled", id)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()));

        verify(service).markAsCancelled(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should mark payment installment as pending when markAsPending is called")
    void markAsPending_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.markAsPending(id)).thenReturn(response);

        // When/Then
        mockMvc.perform(put("/api/payment-installments/{id}/mark-as-pending", id)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()));

        verify(service).markAsPending(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get valid status transitions when getValidStatusTransitions is called")
    void getValidStatusTransitions_success() throws Exception {
        // Given
        Integer id = 1;
        List<String> expectedTransitions = List.of("PAID", "OVERDUE", "CANCELLED");
        when(service.getValidStatusTransitions(id)).thenReturn(expectedTransitions);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/{id}/valid-transitions", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("PAID"))
                .andExpect(jsonPath("$[1]").value("OVERDUE"))
                .andExpect(jsonPath("$[2]").value("CANCELLED"));

        verify(service).getValidStatusTransitions(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should check if status transition is valid when isValidStatusTransition is called")
    void isValidStatusTransition_success() throws Exception {
        // Given
        Integer id = 1;
        String newStatus = "PAID";
        when(service.isValidStatusTransition(id, newStatus)).thenReturn(true);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/{id}/is-valid-transition", id)
                .param("newStatus", newStatus))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(service).isValidStatusTransition(id, newStatus);
    }

    // Reminder operations
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should mark reminder as sent when markReminderAsSent is called")
    void markReminderAsSent_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.markReminderAsSent(id)).thenReturn(response);

        // When/Then
        mockMvc.perform(put("/api/payment-installments/{id}/mark-reminder-sent", id)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()));

        verify(service).markReminderAsSent(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get installments needing reminder when getInstallmentsNeedingReminder is called")
    void getInstallmentsNeedingReminder_success() throws Exception {
        // Given
        when(service.getInstallmentsNeedingReminder()).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/needing-reminder"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getInstallmentsNeedingReminder();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should send reminders for due installments when sendRemindersForDueInstallments is called")
    void sendRemindersForDueInstallments_success() throws Exception {
        // Given
        doNothing().when(service).sendRemindersForDueInstallments();

        // When/Then
        mockMvc.perform(post("/api/payment-installments/send-reminders-due")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(service).sendRemindersForDueInstallments();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should send reminders for overdue installments when sendRemindersForOverdueInstallments is called")
    void sendRemindersForOverdueInstallments_success() throws Exception {
        // Given
        doNothing().when(service).sendRemindersForOverdueInstallments();

        // When/Then
        mockMvc.perform(post("/api/payment-installments/send-reminders-overdue")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(service).sendRemindersForOverdueInstallments();
    }

    // Utility operations
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should recalculate overdue status when recalculateOverdueStatus is called")
    void recalculateOverdueStatus_success() throws Exception {
        // Given
        doNothing().when(service).recalculateOverdueStatus();

        // When/Then
        mockMvc.perform(post("/api/payment-installments/recalculate-overdue")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(service).recalculateOverdueStatus();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should cleanup old installments when cleanupOldInstallments is called")
    void cleanupOldInstallments_success() throws Exception {
        // Given
        int daysToKeep = 30;
        doNothing().when(service).cleanupOldInstallments(daysToKeep);

        // When/Then
        mockMvc.perform(delete("/api/payment-installments/cleanup/{daysToKeep}", daysToKeep)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(service).cleanupOldInstallments(daysToKeep);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should search installments by amount when searchInstallmentsByAmount is called")
    void searchInstallmentsByAmount_success() throws Exception {
        // Given
        BigDecimal amount = new BigDecimal("500.00");
        when(service.searchInstallmentsByAmount(amount)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/search/amount/{amount}", amount))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).searchInstallmentsByAmount(amount);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find latest installment by reservation when found")
    void findLatestInstallmentByReservation_whenFound_success() throws Exception {
        // Given
        Integer reservationId = 1;
        when(service.findLatestInstallmentByReservation(reservationId)).thenReturn(Optional.of(response));

        // When/Then
        mockMvc.perform(get("/api/payment-installments/latest/reservation/{reservationId}", reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()));

        verify(service).findLatestInstallmentByReservation(reservationId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should return 404 when latest installment by reservation not found")
    void findLatestInstallmentByReservation_whenNotFound_returns404() throws Exception {
        // Given
        Integer reservationId = 1;
        when(service.findLatestInstallmentByReservation(reservationId)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/payment-installments/latest/reservation/{reservationId}", reservationId))
                .andExpect(status().isNotFound());

        verify(service).findLatestInstallmentByReservation(reservationId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get installments with late fees when getInstallmentsWithLateFees is called")
    void getInstallmentsWithLateFees_success() throws Exception {
        // Given
        when(service.getInstallmentsWithLateFees()).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/with-late-fees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getInstallmentsWithLateFees();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should calculate late fee for installment when calculateLateFeeForInstallment is called")
    void calculateLateFeeForInstallment_success() throws Exception {
        // Given
        Integer id = 1;
        BigDecimal expectedLateFee = new BigDecimal("25.00");
        when(service.calculateLateFeeForInstallment(id)).thenReturn(expectedLateFee);

        // When/Then
        mockMvc.perform(get("/api/payment-installments/{id}/late-fee", id))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedLateFee.toString()));

        verify(service).calculateLateFeeForInstallment(id);
    }
}
