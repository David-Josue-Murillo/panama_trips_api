package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.NotificationHistoryRequest;
import com.app.panama_trips.presentation.dto.NotificationHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface INotificationHistoryService {
    // CRUD operations
    Page<NotificationHistoryResponse> getAllNotificationHistory(Pageable pageable);
    NotificationHistoryResponse getNotificationHistoryById(Integer id);
    NotificationHistoryResponse saveNotificationHistory(NotificationHistoryRequest request);
    NotificationHistoryResponse updateNotificationHistory(Integer id, NotificationHistoryRequest request);
    void deleteNotificationHistory(Integer id);

    // Find operations
    List<NotificationHistoryResponse> findByUserId(Long userId);
    List<NotificationHistoryResponse> findByTemplateId(Integer templateId);
    List<NotificationHistoryResponse> findByReservationId(Integer reservationId);
    List<NotificationHistoryResponse> findByDeliveryStatus(String deliveryStatus);
    List<NotificationHistoryResponse> findByChannel(String channel);
    List<NotificationHistoryResponse> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<NotificationHistoryResponse> findByDateRangeAndUser(LocalDateTime startDate, LocalDateTime endDate, Long userId);

    // Specialized queries
    List<NotificationHistoryResponse> getFailedNotifications();
    List<NotificationHistoryResponse> getPendingNotifications();
    List<NotificationHistoryResponse> getDeliveredNotifications();
    List<NotificationHistoryResponse> getNotificationsByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);
    List<NotificationHistoryResponse> getNotificationsByReservationAndChannel(Integer reservationId, String channel);
    List<NotificationHistoryResponse> getRecentNotifications(int limit);

    // Bulk operations
    void bulkCreateNotificationHistory(List<NotificationHistoryRequest> requests);
    void bulkDeleteNotificationHistory(List<Integer> notificationIds);
    void bulkUpdateDeliveryStatus(List<Integer> notificationIds, String newStatus);

    // Check operations
    boolean existsById(Integer id);
    boolean existsByUserIdAndTemplateId(Long userId, Integer templateId);
    long countByUserId(Long userId);
    long countByDeliveryStatus(String deliveryStatus);
    long countByChannel(String channel);
    long countByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    // Statistics and analytics
    long getTotalNotificationsSent();
    long getTotalNotificationsDelivered();
    long getTotalNotificationsFailed();
    double getDeliverySuccessRate();

    // Utility operations
    void markAsDelivered(Integer notificationId);
    void markAsFailed(Integer notificationId, String failureReason);
    void retryFailedNotifications();
    void cleanupOldNotifications(int daysToKeep);
    List<NotificationHistoryResponse> searchNotificationsByContent(String keyword);
    Optional<NotificationHistoryResponse> findLatestNotificationByUser(Long userId);
}
