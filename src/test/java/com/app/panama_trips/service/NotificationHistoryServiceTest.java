package com.app.panama_trips.service;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.NotificationHistory;
import com.app.panama_trips.persistence.entity.NotificationTemplate;
import com.app.panama_trips.persistence.entity.Reservation;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.repository.NotificationHistoryRepository;
import com.app.panama_trips.persistence.repository.NotificationTemplateRepository;
import com.app.panama_trips.persistence.repository.ReservationRepository;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.presentation.dto.NotificationHistoryRequest;
import com.app.panama_trips.presentation.dto.NotificationHistoryResponse;
import com.app.panama_trips.service.implementation.NotificationHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.app.panama_trips.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationHistoryServiceTest {

    @Mock
    private NotificationHistoryRepository repository;

    @Mock
    private NotificationTemplateRepository notificationTemplateRepository;

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private NotificationHistoryService service;

    @Captor
    private ArgumentCaptor<NotificationHistory> notificationCaptor;

    @Captor
    private ArgumentCaptor<List<NotificationHistory>> notificationsCaptor;

    private NotificationHistory notification;
    private NotificationHistoryRequest request;
    private List<NotificationHistory> notifications;
    private NotificationTemplate template;
    private UserEntity user;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        notification = notificationHistoryOneMock();
        notifications = notificationHistoriesListMock();
        request = notificationHistoryRequest;
        template = notificationTemplateOneMock();
        user = userAdmin();
        reservation = reservationOneMock;
    }

    // ========== CRUD OPERATIONS TESTS ==========

    @Test
    @DisplayName("Should return all notification history when getAllNotificationHistory is called with pagination")
    void getAllNotificationHistory_shouldReturnAllData() {
        // Given
        Page<NotificationHistory> page = new PageImpl<>(notifications);
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(page);

        // When
        Page<NotificationHistoryResponse> response = service.getAllNotificationHistory(pageable);

        // Then
        assertNotNull(response);
        assertEquals(notifications.size(), response.getTotalElements());
        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("Should return notification history by id when exists")
    void getNotificationHistoryById_whenExists_shouldReturnNotification() {
        // Given
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.of(notification));

        // When
        NotificationHistoryResponse result = service.getNotificationHistoryById(id);

        // Then
        assertNotNull(result);
        assertEquals(notification.getId(), result.id());
        assertEquals(notification.getContent(), result.content());
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when getting notification history by id that doesn't exist")
    void getNotificationHistoryById_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getNotificationHistoryById(id)
        );
        assertEquals("Notification history not found with: " + id, exception.getMessage());
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should save notification history successfully")
    void saveNotificationHistory_success() {
        // Given
        when(notificationTemplateRepository.existsById(request.templateId())).thenReturn(true);
        when(userEntityRepository.existsById(request.userId())).thenReturn(true);
        when(reservationRepository.existsById(request.reservationId())).thenReturn(true);
        when(notificationTemplateRepository.findById(request.templateId())).thenReturn(Optional.of(template));
        when(userEntityRepository.findById(request.userId())).thenReturn(Optional.of(user));
        when(reservationRepository.findById(request.reservationId())).thenReturn(Optional.of(reservation));
        when(repository.save(any(NotificationHistory.class))).thenReturn(notification);

        // When
        NotificationHistoryResponse result = service.saveNotificationHistory(request);

        // Then
        assertNotNull(result);
        assertEquals(notification.getId(), result.id());
        assertEquals(notification.getContent(), result.content());

        verify(repository).save(notificationCaptor.capture());
        NotificationHistory savedNotification = notificationCaptor.getValue();
        assertEquals(request.content(), savedNotification.getContent());
        assertEquals(request.deliveryStatus(), savedNotification.getDeliveryStatus());
        assertEquals(request.channel(), savedNotification.getChannel());
    }

    @Test
    @DisplayName("Should throw exception when saving notification history with non-existent template")
    void saveNotificationHistory_withNonExistentTemplate_shouldThrowException() {
        // Given
        when(notificationTemplateRepository.existsById(request.templateId())).thenReturn(false);

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.saveNotificationHistory(request)
        );
        assertEquals("Template not found", exception.getMessage());
        verify(repository, never()).save(any(NotificationHistory.class));
    }

    @Test
    @DisplayName("Should throw exception when saving notification history with non-existent user")
    void saveNotificationHistory_withNonExistentUser_shouldThrowException() {
        // Given
        when(notificationTemplateRepository.existsById(request.templateId())).thenReturn(true);
        when(userEntityRepository.existsById(request.userId())).thenReturn(false);

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.saveNotificationHistory(request)
        );
        assertEquals("User not found", exception.getMessage());
        verify(repository, never()).save(any(NotificationHistory.class));
    }

    @Test
    @DisplayName("Should update notification history successfully")
    void updateNotificationHistory_success() {
        // Given
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.of(notification));
        when(notificationTemplateRepository.existsById(request.templateId())).thenReturn(true);
        when(userEntityRepository.existsById(request.userId())).thenReturn(true);
        when(reservationRepository.existsById(request.reservationId())).thenReturn(true);
        when(notificationTemplateRepository.findById(request.templateId())).thenReturn(Optional.of(template));
        when(userEntityRepository.findById(request.userId())).thenReturn(Optional.of(user));
        when(reservationRepository.findById(request.reservationId())).thenReturn(Optional.of(reservation));
        when(repository.save(any(NotificationHistory.class))).thenReturn(notification);

        // When
        NotificationHistoryResponse result = service.updateNotificationHistory(id, request);

        // Then
        assertNotNull(result);
        verify(repository).findById(id);
        verify(repository).save(notificationCaptor.capture());

        NotificationHistory updatedNotification = notificationCaptor.getValue();
        assertEquals(request.content(), updatedNotification.getContent());
        assertEquals(request.deliveryStatus(), updatedNotification.getDeliveryStatus());
        assertEquals(request.channel(), updatedNotification.getChannel());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent notification history")
    void updateNotificationHistory_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(notificationTemplateRepository.existsById(request.templateId())).thenReturn(true);
        when(userEntityRepository.existsById(request.userId())).thenReturn(true);
        when(reservationRepository.existsById(request.reservationId())).thenReturn(true);
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateNotificationHistory(id, request)
        );
        assertEquals("Notification history not found with: " + id, exception.getMessage());
        verify(repository).findById(id);
        verify(repository, never()).save(any(NotificationHistory.class));
    }

    @Test
    @DisplayName("Should delete notification history successfully when exists")
    void deleteNotificationHistory_whenExists_success() {
        // Given
        Integer id = 1;
        when(repository.existsById(id)).thenReturn(true);

        // When
        service.deleteNotificationHistory(id);

        // Then
        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent notification history")
    void deleteNotificationHistory_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(repository.existsById(id)).thenReturn(false);

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteNotificationHistory(id)
        );
        assertEquals("Notification history not found with: " + id, exception.getMessage());
        verify(repository).existsById(id);
        verify(repository, never()).deleteById(anyInt());
    }

    // ========== FIND OPERATIONS TESTS ==========

    @Test
    @DisplayName("Should find notification history by user id")
    void findByUserId_shouldReturnMatchingNotifications() {
        // Given
        Long userId = 1L;
        when(userEntityRepository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.findByUser(user)).thenReturn(notifications);

        // When
        List<NotificationHistoryResponse> result = service.findByUserId(userId);

        // Then
        assertNotNull(result);
        assertEquals(notifications.size(), result.size());
        verify(userEntityRepository).findById(userId);
        verify(repository).findByUser(user);
    }

    @Test
    @DisplayName("Should find notification history by template id")
    void findByTemplateId_shouldReturnMatchingNotifications() {
        // Given
        Integer templateId = 1;
        when(notificationTemplateRepository.findById(templateId)).thenReturn(Optional.of(template));
        when(repository.findByTemplate(template)).thenReturn(notifications);

        // When
        List<NotificationHistoryResponse> result = service.findByTemplateId(templateId);

        // Then
        assertNotNull(result);
        assertEquals(notifications.size(), result.size());
        verify(notificationTemplateRepository).findById(templateId);
        verify(repository).findByTemplate(template);
    }

    @Test
    @DisplayName("Should find notification history by reservation id")
    void findByReservationId_shouldReturnMatchingNotifications() {
        // Given
        Integer reservationId = 1;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(repository.findByReservation(reservation)).thenReturn(notifications);

        // When
        List<NotificationHistoryResponse> result = service.findByReservationId(reservationId);

        // Then
        assertNotNull(result);
        assertEquals(notifications.size(), result.size());
        verify(reservationRepository).findById(reservationId);
        verify(repository).findByReservation(reservation);
    }

    @Test
    @DisplayName("Should find notification history by delivery status")
    void findByDeliveryStatus_shouldReturnMatchingNotifications() {
        // Given
        String deliveryStatus = "DELIVERED";
        when(repository.findByDeliveryStatus(deliveryStatus)).thenReturn(notifications);

        // When
        List<NotificationHistoryResponse> result = service.findByDeliveryStatus(deliveryStatus);

        // Then
        assertNotNull(result);
        assertEquals(notifications.size(), result.size());
        verify(repository).findByDeliveryStatus(deliveryStatus);
    }

    @Test
    @DisplayName("Should throw exception when finding by invalid delivery status")
    void findByDeliveryStatus_withInvalidStatus_shouldThrowException() {
        // Given
        String invalidStatus = "INVALID_STATUS";

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.findByDeliveryStatus(invalidStatus)
        );
        assertEquals("Invalid delivery status: " + invalidStatus, exception.getMessage());
        verify(repository, never()).findByDeliveryStatus(anyString());
    }

    @Test
    @DisplayName("Should find notification history by channel")
    void findByChannel_shouldReturnMatchingNotifications() {
        // Given
        String channel = "EMAIL";
        when(repository.findByChannel(channel)).thenReturn(notifications);

        // When
        List<NotificationHistoryResponse> result = service.findByChannel(channel);

        // Then
        assertNotNull(result);
        assertEquals(notifications.size(), result.size());
        verify(repository).findByChannel(channel);
    }

    @Test
    @DisplayName("Should find notification history by date range")
    void findByDateRange_shouldReturnMatchingNotifications() {
        // Given
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        when(repository.findBySentAtBetween(startDate, endDate)).thenReturn(notifications);

        // When
        List<NotificationHistoryResponse> result = service.findByDateRange(startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(notifications.size(), result.size());
        verify(repository).findBySentAtBetween(startDate, endDate);
    }

    @Test
    @DisplayName("Should find notification history by date range and user")
    void findByDateRangeAndUser_shouldReturnMatchingNotifications() {
        // Given
        Long userId = 1L;
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        when(userEntityRepository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.findByUserAndSentAtBetween(user, startDate, endDate)).thenReturn(notifications);

        // When
        List<NotificationHistoryResponse> result = service.findByDateRangeAndUser(startDate, endDate, userId);

        // Then
        assertNotNull(result);
        assertEquals(notifications.size(), result.size());
        verify(userEntityRepository).findById(userId);
        verify(repository).findByUserAndSentAtBetween(user, startDate, endDate);
    }

    // ========== SPECIALIZED QUERIES TESTS ==========

    @Test
    @DisplayName("Should get failed notifications")
    void getFailedNotifications_shouldReturnFailedNotifications() {
        // Given
        when(repository.findByDeliveryStatus("FAILED")).thenReturn(notifications);

        // When
        List<NotificationHistoryResponse> result = service.getFailedNotifications();

        // Then
        assertNotNull(result);
        assertEquals(notifications.size(), result.size());
        verify(repository).findByDeliveryStatus("FAILED");
    }

    @Test
    @DisplayName("Should get pending notifications")
    void getPendingNotifications_shouldReturnPendingNotifications() {
        // Given
        when(repository.findByDeliveryStatus("PENDING")).thenReturn(notifications);

        // When
        List<NotificationHistoryResponse> result = service.getPendingNotifications();

        // Then
        assertNotNull(result);
        assertEquals(notifications.size(), result.size());
        verify(repository).findByDeliveryStatus("PENDING");
    }

    @Test
    @DisplayName("Should get delivered notifications")
    void getDeliveredNotifications_shouldReturnDeliveredNotifications() {
        // Given
        when(repository.findByDeliveryStatus("DELIVERED")).thenReturn(notifications);

        // When
        List<NotificationHistoryResponse> result = service.getDeliveredNotifications();

        // Then
        assertNotNull(result);
        assertEquals(notifications.size(), result.size());
        verify(repository).findByDeliveryStatus("DELIVERED");
    }

    @Test
    @DisplayName("Should get recent notifications with limit using paginated query")
    void getRecentNotifications_shouldReturnRecentNotifications() {
        // Given
        int limit = 5;
        when(repository.findAllByOrderBySentAtDesc(any(Pageable.class))).thenReturn(notifications);

        // When
        List<NotificationHistoryResponse> result = service.getRecentNotifications(limit);

        // Then
        assertNotNull(result);
        assertEquals(notifications.size(), result.size());
        verify(repository).findAllByOrderBySentAtDesc(any(Pageable.class));
    }

    @Test
    @DisplayName("Should get notifications by reservation and channel")
    void getNotificationsByReservationAndChannel_shouldReturnMatchingNotifications() {
        // Given
        Integer reservationId = 1;
        String channel = "EMAIL";
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(repository.findByReservationAndChannel(reservation, channel)).thenReturn(notifications);

        // When
        List<NotificationHistoryResponse> result = service.getNotificationsByReservationAndChannel(reservationId, channel);

        // Then
        assertNotNull(result);
        assertEquals(notifications.size(), result.size());
        verify(reservationRepository).findById(reservationId);
        verify(repository).findByReservationAndChannel(reservation, channel);
    }

    // ========== BULK OPERATIONS TESTS ==========

    @Test
    @DisplayName("Should bulk create notification history")
    void bulkCreateNotificationHistory_success() {
        // Given
        List<NotificationHistoryRequest> requests = List.of(request);
        when(notificationTemplateRepository.existsById(request.templateId())).thenReturn(true);
        when(userEntityRepository.existsById(request.userId())).thenReturn(true);
        when(reservationRepository.existsById(request.reservationId())).thenReturn(true);
        when(notificationTemplateRepository.findById(request.templateId())).thenReturn(Optional.of(template));
        when(userEntityRepository.findById(request.userId())).thenReturn(Optional.of(user));
        when(reservationRepository.findById(request.reservationId())).thenReturn(Optional.of(reservation));
        when(repository.saveAll(anyList())).thenReturn(List.of(notification));

        // When
        service.bulkCreateNotificationHistory(requests);

        // Then
        verify(repository).saveAll(notificationsCaptor.capture());
        List<NotificationHistory> savedNotifications = notificationsCaptor.getValue();
        assertEquals(1, savedNotifications.size());
        assertEquals(request.content(), savedNotifications.getFirst().getContent());
    }

    @Test
    @DisplayName("Should bulk delete notification history")
    void bulkDeleteNotificationHistory_success() {
        // Given
        List<Integer> notificationIds = List.of(1, 2, 3);

        // When
        service.bulkDeleteNotificationHistory(notificationIds);

        // Then
        verify(repository).deleteAllById(notificationIds);
    }

    @Test
    @DisplayName("Should bulk update delivery status")
    void bulkUpdateDeliveryStatus_success() {
        // Given
        List<Integer> notificationIds = List.of(1, 2);
        String newStatus = "DELIVERED";
        when(repository.findAllById(notificationIds)).thenReturn(notifications.subList(0, 2));
        when(repository.saveAll(anyList())).thenReturn(notifications.subList(0, 2));

        // When
        service.bulkUpdateDeliveryStatus(notificationIds, newStatus);

        // Then
        verify(repository).findAllById(notificationIds);
        verify(repository).saveAll(anyList());
    }

    @Test
    @DisplayName("Should throw exception when bulk updating with invalid status")
    void bulkUpdateDeliveryStatus_withInvalidStatus_shouldThrowException() {
        // Given
        List<Integer> notificationIds = List.of(1, 2);
        String invalidStatus = "INVALID";

        // When/Then
        assertThrows(IllegalArgumentException.class,
                () -> service.bulkUpdateDeliveryStatus(notificationIds, invalidStatus));
        verify(repository, never()).findAllById(anyList());
    }

    // ========== CHECK OPERATIONS TESTS ==========

    @Test
    @DisplayName("Should check if notification history exists by id")
    void existsById_whenExists_returnsTrue() {
        // Given
        Integer id = 1;
        when(repository.existsById(id)).thenReturn(true);

        // When
        boolean result = service.existsById(id);

        // Then
        assertTrue(result);
        verify(repository).existsById(id);
    }

    @Test
    @DisplayName("Should check if notification history does not exist by id")
    void existsById_whenNotExists_returnsFalse() {
        // Given
        Integer id = 999;
        when(repository.existsById(id)).thenReturn(false);

        // When
        boolean result = service.existsById(id);

        // Then
        assertFalse(result);
        verify(repository).existsById(id);
    }

    @Test
    @DisplayName("Should check existence by user id and template id")
    void existsByUserIdAndTemplateId_shouldReturnCorrectResult() {
        // Given
        Long userId = 1L;
        Integer templateId = 1;
        when(userEntityRepository.findById(userId)).thenReturn(Optional.of(user));
        when(notificationTemplateRepository.findById(templateId)).thenReturn(Optional.of(template));
        when(repository.existsByUserAndTemplate(user, template)).thenReturn(true);

        // When
        boolean result = service.existsByUserIdAndTemplateId(userId, templateId);

        // Then
        assertTrue(result);
        verify(repository).existsByUserAndTemplate(user, template);
    }

    @Test
    @DisplayName("Should count notification history by user id using repository count query")
    void countByUserId_shouldReturnCorrectCount() {
        // Given
        Long userId = 1L;
        when(userEntityRepository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.countByUser(user)).thenReturn(3L);

        // When
        long result = service.countByUserId(userId);

        // Then
        assertEquals(3L, result);
        verify(userEntityRepository).findById(userId);
        verify(repository).countByUser(user);
    }

    @Test
    @DisplayName("Should count notification history by delivery status using repository count query")
    void countByDeliveryStatus_shouldReturnCorrectCount() {
        // Given
        String deliveryStatus = "DELIVERED";
        when(repository.countByDeliveryStatus(deliveryStatus)).thenReturn(5L);

        // When
        long result = service.countByDeliveryStatus(deliveryStatus);

        // Then
        assertEquals(5L, result);
        verify(repository).countByDeliveryStatus(deliveryStatus);
    }

    @Test
    @DisplayName("Should count notification history by channel using repository count query")
    void countByChannel_shouldReturnCorrectCount() {
        // Given
        String channel = "EMAIL";
        when(repository.countByChannel(channel)).thenReturn(7L);

        // When
        long result = service.countByChannel(channel);

        // Then
        assertEquals(7L, result);
        verify(repository).countByChannel(channel);
    }

    @Test
    @DisplayName("Should count notification history by date range")
    void countByDateRange_shouldReturnCorrectCount() {
        // Given
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();
        when(repository.countBySentAtBetween(startDate, endDate)).thenReturn(10L);

        // When
        long result = service.countByDateRange(startDate, endDate);

        // Then
        assertEquals(10L, result);
        verify(repository).countBySentAtBetween(startDate, endDate);
    }

    // ========== STATISTICS AND ANALYTICS TESTS ==========

    @Test
    @DisplayName("Should get total notifications sent")
    void getTotalNotificationsSent_shouldReturnCorrectCount() {
        // Given
        when(repository.count()).thenReturn(10L);

        // When
        long result = service.getTotalNotificationsSent();

        // Then
        assertEquals(10L, result);
        verify(repository).count();
    }

    @Test
    @DisplayName("Should get total notifications delivered")
    void getTotalNotificationsDelivered_shouldReturnCorrectCount() {
        // Given
        when(repository.countByDeliveryStatus("DELIVERED")).thenReturn(8L);

        // When
        long result = service.getTotalNotificationsDelivered();

        // Then
        assertEquals(8L, result);
        verify(repository).countByDeliveryStatus("DELIVERED");
    }

    @Test
    @DisplayName("Should get total notifications failed")
    void getTotalNotificationsFailed_shouldReturnCorrectCount() {
        // Given
        when(repository.countByDeliveryStatus("FAILED")).thenReturn(2L);

        // When
        long result = service.getTotalNotificationsFailed();

        // Then
        assertEquals(2L, result);
        verify(repository).countByDeliveryStatus("FAILED");
    }

    @Test
    @DisplayName("Should calculate delivery success rate correctly")
    void getDeliverySuccessRate_shouldReturnCorrectRate() {
        // Given
        when(repository.count()).thenReturn(100L);
        when(repository.countByDeliveryStatus("DELIVERED")).thenReturn(85L);

        // When
        double result = service.getDeliverySuccessRate();

        // Then
        assertEquals(85.0, result);
        verify(repository).count();
        verify(repository).countByDeliveryStatus("DELIVERED");
    }

    @Test
    @DisplayName("Should return zero delivery success rate when no notifications sent")
    void getDeliverySuccessRate_whenNoNotifications_shouldReturnZero() {
        // Given
        when(repository.count()).thenReturn(0L);

        // When
        double result = service.getDeliverySuccessRate();

        // Then
        assertEquals(0.0, result);
        verify(repository).count();
        verify(repository, never()).countByDeliveryStatus(anyString());
    }

    // ========== UTILITY OPERATIONS TESTS ==========

    @Test
    @DisplayName("Should mark notification as delivered")
    void markAsDelivered_success() {
        // Given
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.of(notification));
        when(repository.save(any(NotificationHistory.class))).thenReturn(notification);

        // When
        service.markAsDelivered(id);

        // Then
        verify(repository).findById(id);
        verify(repository).save(notificationCaptor.capture());
        assertEquals("DELIVERED", notificationCaptor.getValue().getDeliveryStatus());
    }

    @Test
    @DisplayName("Should throw exception when marking non-existent notification as delivered")
    void markAsDelivered_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> service.markAsDelivered(id));
        verify(repository, never()).save(any(NotificationHistory.class));
    }

    @Test
    @DisplayName("Should mark notification as failed with reason")
    void markAsFailed_success() {
        // Given
        Integer id = 1;
        String reason = "Connection timeout";
        when(repository.findById(id)).thenReturn(Optional.of(notification));
        when(repository.save(any(NotificationHistory.class))).thenReturn(notification);

        // When
        service.markAsFailed(id, reason);

        // Then
        verify(repository).findById(id);
        verify(repository).save(notificationCaptor.capture());
        assertEquals("FAILED", notificationCaptor.getValue().getDeliveryStatus());
    }

    @Test
    @DisplayName("Should retry failed notifications using saveAll batch")
    void retryFailedNotifications_success() {
        // Given
        when(repository.findByDeliveryStatus("FAILED")).thenReturn(notifications);
        when(repository.saveAll(anyList())).thenReturn(notifications);

        // When
        service.retryFailedNotifications();

        // Then
        verify(repository).findByDeliveryStatus("FAILED");
        verify(repository).saveAll(notificationsCaptor.capture());
        List<NotificationHistory> retried = notificationsCaptor.getValue();
        retried.forEach(n -> assertEquals("PENDING", n.getDeliveryStatus()));
    }

    @Test
    @DisplayName("Should cleanup old notifications")
    void cleanupOldNotifications_success() {
        // Given
        int daysToKeep = 30;

        // When
        service.cleanupOldNotifications(daysToKeep);

        // Then
        verify(repository).deleteBySentAtBefore(any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Should search notifications by content")
    void searchNotificationsByContent_shouldReturnMatchingNotifications() {
        // Given
        String keyword = "test";
        when(repository.searchByContent(keyword)).thenReturn(notifications);

        // When
        List<NotificationHistoryResponse> result = service.searchNotificationsByContent(keyword);

        // Then
        assertNotNull(result);
        assertEquals(notifications.size(), result.size());
        verify(repository).searchByContent(keyword);
    }

    @Test
    @DisplayName("Should throw exception when searching with empty keyword")
    void searchNotificationsByContent_withEmptyKeyword_shouldThrowException() {
        // Given
        String emptyKeyword = "";

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.searchNotificationsByContent(emptyKeyword)
        );
        assertEquals("Keyword must not be empty", exception.getMessage());
        verify(repository, never()).searchByContent(anyString());
    }

    @Test
    @DisplayName("Should find latest notification by user using optimized query")
    void findLatestNotificationByUser_whenExists_shouldReturnNotification() {
        // Given
        Long userId = 1L;
        when(userEntityRepository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.findFirstByUserOrderBySentAtDesc(user)).thenReturn(Optional.of(notification));

        // When
        Optional<NotificationHistoryResponse> result = service.findLatestNotificationByUser(userId);

        // Then
        assertTrue(result.isPresent());
        verify(userEntityRepository).findById(userId);
        verify(repository).findFirstByUserOrderBySentAtDesc(user);
    }

    @Test
    @DisplayName("Should return empty optional when no notifications found for user")
    void findLatestNotificationByUser_whenNoNotifications_shouldReturnEmpty() {
        // Given
        Long userId = 1L;
        when(userEntityRepository.findById(userId)).thenReturn(Optional.of(user));
        when(repository.findFirstByUserOrderBySentAtDesc(user)).thenReturn(Optional.empty());

        // When
        Optional<NotificationHistoryResponse> result = service.findLatestNotificationByUser(userId);

        // Then
        assertFalse(result.isPresent());
        verify(userEntityRepository).findById(userId);
        verify(repository).findFirstByUserOrderBySentAtDesc(user);
    }

    // ========== HELPER METHOD TESTS ==========

    @Test
    @DisplayName("Should validate delivery status correctly")
    void validateDeliveryStatus_shouldAcceptValidStatuses() {
        // Test that valid statuses don't throw exceptions
        assertDoesNotThrow(() -> service.findByDeliveryStatus("PENDING"));
        assertDoesNotThrow(() -> service.findByDeliveryStatus("DELIVERED"));
        assertDoesNotThrow(() -> service.findByDeliveryStatus("FAILED"));
    }

    @Test
    @DisplayName("Should validate channel correctly")
    void validateChannel_shouldAcceptValidChannels() {
        // Test that valid channels don't throw exceptions
        assertDoesNotThrow(() -> service.findByChannel("EMAIL"));
        assertDoesNotThrow(() -> service.findByChannel("SMS"));
        assertDoesNotThrow(() -> service.findByChannel("PUSH"));
    }

    @Test
    @DisplayName("Should throw exception when counting by invalid channel")
    void countByChannel_withInvalidChannel_shouldThrowException() {
        // Given
        String invalidChannel = "INVALID";

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.countByChannel(invalidChannel)
        );
        assertEquals("Invalid channel: " + invalidChannel, exception.getMessage());
        verify(repository, never()).countByChannel(anyString());
    }
}
