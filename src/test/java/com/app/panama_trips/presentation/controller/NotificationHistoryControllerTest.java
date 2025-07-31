package com.app.panama_trips.presentation.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

import com.app.panama_trips.presentation.dto.NotificationHistoryRequest;
import com.app.panama_trips.presentation.dto.NotificationHistoryResponse;
import com.app.panama_trips.service.implementation.NotificationHistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static com.app.panama_trips.DataProvider.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationHistoryController.class)
public class NotificationHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationHistoryService service;

    private NotificationHistoryRequest request;
    private NotificationHistoryResponse response;
    private List<NotificationHistoryResponse> responseList;

    @BeforeEach
    void setUp() {
        request = notificationHistoryRequest;
        response = notificationHistoryResponse;
        responseList = notificationHistoryListResponse;
    }

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(obj);
    }

    // ========== CRUD OPERATIONS TESTS ==========

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should return all notification history with pagination when getAll is called")
    void getAll_success() throws Exception {
        // Given
        Page<NotificationHistoryResponse> page = new PageImpl<>(responseList);
        when(service.getAllNotificationHistory(any(Pageable.class))).thenReturn(page);

        // When/Then
        mockMvc.perform(get("/api/notification-history")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(responseList.getFirst().id()))
                .andExpect(jsonPath("$.content[0].content").value(response.content()));

        verify(service).getAllNotificationHistory(any(Pageable.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should return notification history by id when getById is called")
    void getById_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.getNotificationHistoryById(id)).thenReturn(response);

        // When/Then
        mockMvc.perform(get("/api/notification-history/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.content").value(response.content()));

        verify(service).getNotificationHistoryById(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should create notification history when create is called")
    void create_success() throws Exception {
        // Given
        when(service.saveNotificationHistory(any(NotificationHistoryRequest.class))).thenReturn(response);

        // When/Then
        mockMvc.perform(post("/api/notification-history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.content").value(response.content()));

        verify(service).saveNotificationHistory(any(NotificationHistoryRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should update notification history when update is called")
    void update_success() throws Exception {
        // Given
        Integer id = 1;
        when(service.updateNotificationHistory(eq(id), any(NotificationHistoryRequest.class))).thenReturn(response);

        // When/Then
        mockMvc.perform(put("/api/notification-history/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.content").value(response.content()));

        verify(service).updateNotificationHistory(eq(id), any(NotificationHistoryRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should delete notification history when delete is called")
    void delete_success() throws Exception {
        // Given
        Integer id = 1;
        doNothing().when(service).deleteNotificationHistory(id);

        // When/Then
        mockMvc.perform(delete("/api/notification-history/{id}", id)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service).deleteNotificationHistory(id);
    }

    // ========== FIND OPERATIONS TESTS ==========

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find notification history by user id when findByUserId is called")
    void findByUserId_success() throws Exception {
        // Given
        Long userId = 1L;
        when(service.findByUserId(userId)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/notification-history/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()))
                .andExpect(jsonPath("$[0].userId").value(response.userId()));

        verify(service).findByUserId(userId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find notification history by template id when findByTemplateId is called")
    void findByTemplateId_success() throws Exception {
        // Given
        Integer templateId = 1;
        when(service.findByTemplateId(templateId)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/notification-history/template/{templateId}", templateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()))
                .andExpect(jsonPath("$[0].templateId").value(response.templateId()));

        verify(service).findByTemplateId(templateId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find notification history by reservation id when findByReservationId is called")
    void findByReservationId_success() throws Exception {
        // Given
        Integer reservationId = 1;
        when(service.findByReservationId(reservationId)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/notification-history/reservation/{reservationId}", reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()))
                .andExpect(jsonPath("$[0].reservationId").value(response.reservationId()));

        verify(service).findByReservationId(reservationId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find notification history by delivery status when findByDeliveryStatus is called")
    void findByDeliveryStatus_success() throws Exception {
        // Given
        String deliveryStatus = "DELIVERED";
        when(service.findByDeliveryStatus(deliveryStatus)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/notification-history/status/{deliveryStatus}", deliveryStatus))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()))
                .andExpect(jsonPath("$[0].deliveryStatus").value(response.deliveryStatus()));

        verify(service).findByDeliveryStatus(deliveryStatus);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find notification history by channel when findByChannel is called")
    void findByChannel_success() throws Exception {
        // Given
        String channel = "EMAIL";
        when(service.findByChannel(channel)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/notification-history/channel/{channel}", channel))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()))
                .andExpect(jsonPath("$[0].channel").value(response.channel()));

        verify(service).findByChannel(channel);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find notification history by date range when findByDateRange is called")
    void findByDateRange_success() throws Exception {
        // Given
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        when(service.findByDateRange(start, end)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/notification-history/date-range")
                .param("start", start.toString())
                .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findByDateRange(start, end);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find notification history by date range and user when findByDateRangeAndUser is called")
    void findByDateRangeAndUser_success() throws Exception {
        // Given
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        Long userId = 1L;
        when(service.findByDateRangeAndUser(start, end, userId)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/notification-history/date-range/user")
                .param("start", start.toString())
                .param("end", end.toString())
                .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).findByDateRangeAndUser(start, end, userId);
    }

    // ========== SPECIALIZED QUERIES TESTS ==========

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get recent notifications when getRecentNotifications is called")
    void getRecentNotifications_success() throws Exception {
        // Given
        int limit = 5;
        when(service.getRecentNotifications(limit)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/notification-history/recent")
                .param("limit", String.valueOf(limit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getRecentNotifications(limit);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should search notification history by content when searchByContent is called")
    void searchByContent_success() throws Exception {
        // Given
        String keyword = "test";
        when(service.searchNotificationsByContent(keyword)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/notification-history/search")
                .param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).searchNotificationsByContent(keyword);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should find latest notification by user when found")
    void findLatestByUser_whenFound_success() throws Exception {
        // Given
        Long userId = 1L;
        when(service.findLatestNotificationByUser(userId)).thenReturn(Optional.of(response));

        // When/Then
        mockMvc.perform(get("/api/notification-history/latest/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()));

        verify(service).findLatestNotificationByUser(userId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should return 404 when latest notification by user not found")
    void findLatestByUser_whenNotFound_returns404() throws Exception {
        // Given
        Long userId = 1L;
        when(service.findLatestNotificationByUser(userId)).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/notification-history/latest/user/{userId}", userId))
                .andExpect(status().isNotFound());

        verify(service).findLatestNotificationByUser(userId);
    }

    // ========== COUNT OPERATIONS TESTS ==========

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should count notification history by user id when countByUserId is called")
    void countByUserId_success() throws Exception {
        // Given
        Long userId = 1L;
        when(service.countByUserId(userId)).thenReturn(5L);

        // When/Then
        mockMvc.perform(get("/api/notification-history/count/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(service).countByUserId(userId);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should count notification history by delivery status when countByDeliveryStatus is called")
    void countByDeliveryStatus_success() throws Exception {
        // Given
        String deliveryStatus = "DELIVERED";
        when(service.countByDeliveryStatus(deliveryStatus)).thenReturn(10L);

        // When/Then
        mockMvc.perform(get("/api/notification-history/count/status/{deliveryStatus}", deliveryStatus))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));

        verify(service).countByDeliveryStatus(deliveryStatus);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should count notification history by channel when countByChannel is called")
    void countByChannel_success() throws Exception {
        // Given
        String channel = "EMAIL";
        when(service.countByChannel(channel)).thenReturn(15L);

        // When/Then
        mockMvc.perform(get("/api/notification-history/count/channel/{channel}", channel))
                .andExpect(status().isOk())
                .andExpect(content().string("15"));

        verify(service).countByChannel(channel);
    }

    // ========== SPECIALIZED STATUS TESTS ==========

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get failed notifications when getFailedNotifications is called")
    void getFailedNotifications_success() throws Exception {
        // Given
        when(service.getFailedNotifications()).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/notification-history/failed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getFailedNotifications();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get pending notifications when getPendingNotifications is called")
    void getPendingNotifications_success() throws Exception {
        // Given
        when(service.getPendingNotifications()).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/notification-history/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getPendingNotifications();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get delivered notifications when getDeliveredNotifications is called")
    void getDeliveredNotifications_success() throws Exception {
        // Given
        when(service.getDeliveredNotifications()).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/notification-history/delivered"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getDeliveredNotifications();
    }

    // ========== COMBINED QUERIES TESTS ==========

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get notifications by user and date range when getByUserAndDateRange is called")
    void getByUserAndDateRange_success() throws Exception {
        // Given
        Long userId = 1L;
        LocalDate start = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now();
        when(service.getNotificationsByUserAndDateRange(userId, start, end)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/notification-history/user/{userId}/date-range", userId)
                .param("start", start.toString())
                .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getNotificationsByUserAndDateRange(userId, start, end);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get notifications by reservation and channel when getByReservationAndChannel is called")
    void getByReservationAndChannel_success() throws Exception {
        // Given
        Integer reservationId = 1;
        String channel = "EMAIL";
        when(service.getNotificationsByReservationAndChannel(reservationId, channel)).thenReturn(responseList);

        // When/Then
        mockMvc.perform(get("/api/notification-history/reservation/{reservationId}/channel/{channel}",
                reservationId, channel))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()));

        verify(service).getNotificationsByReservationAndChannel(reservationId, channel);
    }

    // ========== STATISTICS TESTS ==========

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get total notifications sent when getTotalNotificationsSent is called")
    void getTotalNotificationsSent_success() throws Exception {
        // Given
        when(service.getTotalNotificationsSent()).thenReturn(100L);

        // When/Then
        mockMvc.perform(get("/api/notification-history/stats/total"))
                .andExpect(status().isOk())
                .andExpect(content().string("100"));

        verify(service).getTotalNotificationsSent();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get total notifications delivered when getTotalNotificationsDelivered is called")
    void getTotalNotificationsDelivered_success() throws Exception {
        // Given
        when(service.getTotalNotificationsDelivered()).thenReturn(85L);

        // When/Then
        mockMvc.perform(get("/api/notification-history/stats/delivered"))
                .andExpect(status().isOk())
                .andExpect(content().string("85"));

        verify(service).getTotalNotificationsDelivered();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get total notifications failed when getTotalNotificationsFailed is called")
    void getTotalNotificationsFailed_success() throws Exception {
        // Given
        when(service.getTotalNotificationsFailed()).thenReturn(15L);

        // When/Then
        mockMvc.perform(get("/api/notification-history/stats/failed"))
                .andExpect(status().isOk())
                .andExpect(content().string("15"));

        verify(service).getTotalNotificationsFailed();
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should get delivery success rate when getDeliverySuccessRate is called")
    void getDeliverySuccessRate_success() throws Exception {
        // Given
        when(service.getDeliverySuccessRate()).thenReturn(85.5);

        // When/Then
        mockMvc.perform(get("/api/notification-history/stats/success-rate"))
                .andExpect(status().isOk())
                .andExpect(content().string("85.5"));

        verify(service).getDeliverySuccessRate();
    }

    // ========== BULK OPERATIONS TESTS ==========

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should create multiple notification history in bulk when bulkCreate is called")
    void bulkCreate_success() throws Exception {
        // Given
        List<NotificationHistoryRequest> requests = Collections.singletonList(request);
        doNothing().when(service).bulkCreateNotificationHistory(anyList());

        // When/Then
        mockMvc.perform(post("/api/notification-history/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requests))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated());

        verify(service).bulkCreateNotificationHistory(anyList());
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should delete multiple notification history in bulk when bulkDelete is called")
    void bulkDelete_success() throws Exception {
        // Given
        List<Integer> notificationIds = Collections.singletonList(1);
        doNothing().when(service).bulkDeleteNotificationHistory(anyList());

        // When/Then
        mockMvc.perform(delete("/api/notification-history/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(notificationIds))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());

        verify(service).bulkDeleteNotificationHistory(anyList());
    }

    // ========== UTILITY OPERATIONS TESTS ==========

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should retry failed notifications when retryFailedNotifications is called")
    void retryFailedNotifications_success() throws Exception {
        // Given
        doNothing().when(service).retryFailedNotifications();

        // When/Then
        mockMvc.perform(post("/api/notification-history/retry-failed")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(service).retryFailedNotifications();
    }

    // ========== ERROR HANDLING TESTS ==========

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should handle service exception when creating notification history")
    void create_whenServiceThrowsException_returns500() throws Exception {
        // Given
        when(service.saveNotificationHistory(any(NotificationHistoryRequest.class)))
                .thenThrow(new RuntimeException("Service error"));

        // When/Then
        mockMvc.perform(post("/api/notification-history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isInternalServerError());

        verify(service).saveNotificationHistory(any(NotificationHistoryRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should handle service exception when updating notification history")
    void update_whenServiceThrowsException_returns500() throws Exception {
        // Given
        Integer id = 1;
        when(service.updateNotificationHistory(eq(id), any(NotificationHistoryRequest.class)))
                .thenThrow(new RuntimeException("Service error"));

        // When/Then
        mockMvc.perform(put("/api/notification-history/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isInternalServerError());

        verify(service).updateNotificationHistory(eq(id), any(NotificationHistoryRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should handle service exception when searching by content")
    void searchByContent_whenServiceThrowsException_returns400() throws Exception {
        // Given
        String keyword = "test";
        when(service.searchNotificationsByContent(keyword))
                .thenThrow(new IllegalArgumentException("Invalid keyword"));

        // When/Then
        mockMvc.perform(get("/api/notification-history/search")
                .param("keyword", keyword))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid keyword"));

        verify(service).searchNotificationsByContent(keyword);
    }

    // ========== SECURITY TESTS ==========

    @Test
    @DisplayName("Should return 401 when accessing without authentication")
    void accessWithoutAuthentication_returns401() throws Exception {
        // When/Then
        mockMvc.perform(get("/api/notification-history"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = { "USER" })
    @DisplayName("Should allow access with user role")
    void accessWithUserRole_success() throws Exception {
        // Given
        Page<NotificationHistoryResponse> page = new PageImpl<>(responseList);
        when(service.getAllNotificationHistory(any(Pageable.class))).thenReturn(page);

        // When/Then
        mockMvc.perform(get("/api/notification-history"))
                .andExpect(status().isOk());

        verify(service).getAllNotificationHistory(any(Pageable.class));
    }

    // ========== EDGE CASES TESTS ==========

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should handle empty list response gracefully")
    void handleEmptyListResponse_success() throws Exception {
        // Given
        when(service.findByUserId(anyLong())).thenReturn(Collections.emptyList());

        // When/Then
        mockMvc.perform(get("/api/notification-history/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(service).findByUserId(1L);
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @DisplayName("Should handle null reservation id gracefully")
    void handleNullReservationId_success() throws Exception {
        // Given
        NotificationHistoryRequest requestWithNullReservation = new NotificationHistoryRequest(
                1, 1L, null, "PENDING", "Test content", "EMAIL");
        when(service.saveNotificationHistory(any())).thenReturn(response);

        // When/Then
        mockMvc.perform(post("/api/notification-history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestWithNullReservation))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated());

        verify(service).saveNotificationHistory(any());
    }
}
