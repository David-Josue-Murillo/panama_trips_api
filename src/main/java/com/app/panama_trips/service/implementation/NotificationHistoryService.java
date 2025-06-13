package com.app.panama_trips.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
import com.app.panama_trips.service.interfaces.INotificationHistoryService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationHistoryService implements INotificationHistoryService {

    private final NotificationHistoryRepository repository;
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final UserEntityRepository userEntityRepository;
    private final ReservationRepository reservationRepository;

    // CRUD operations
    @Override
    public Page<NotificationHistoryResponse> getAllNotificationHistory(Pageable pageable) {
        return repository.findAll(pageable).map(NotificationHistoryResponse::new);
    }

    @Override
    public NotificationHistoryResponse getNotificationHistoryById(Integer id) {
        return repository.findById(id)
                .map(NotificationHistoryResponse::new)
                .orElseThrow(() -> new RuntimeException("Notification history not found"));
    }

    @Override
    public NotificationHistoryResponse saveNotificationHistory(NotificationHistoryRequest request) {
        NotificationTemplate template = notificationTemplateRepository.findById(request.templateId())
                .orElseThrow(() -> new RuntimeException("Template not found"));
        UserEntity user = userEntityRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Optional<Reservation> reservation = Optional.empty();
        if (request.reservationId() != null) {
            reservation = reservationRepository.findById(request.reservationId());
        }

        NotificationHistory notification = NotificationHistory.builder()
                .template(template)
                .user(user)
                .reservation(reservation.orElse(null))
                .deliveryStatus(request.deliveryStatus())
                .content(request.content())
                .channel(request.channel())
                .build();

        return new NotificationHistoryResponse(repository.save(notification));
    }

    @Override
    public NotificationHistoryResponse updateNotificationHistory(Integer id, NotificationHistoryRequest request) {
        NotificationHistory existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification history not found"));
        
        existing.setDeliveryStatus(request.deliveryStatus());
        existing.setContent(request.content());
        existing.setChannel(request.channel());
        
        return new NotificationHistoryResponse(repository.save(existing));
    }

    @Override
    public void deleteNotificationHistory(Integer id) {
        repository.deleteById(id);
    }

    // Find operations
    @Override
    public List<NotificationHistoryResponse> findByUserId(Long userId) {
        return null;
    }

    @Override
    public List<NotificationHistoryResponse> findByTemplateId(Integer templateId) {
        return null;
    }

    @Override
    public List<NotificationHistoryResponse> findByReservationId(Integer reservationId) {
        return null;
    }

    @Override
    public List<NotificationHistoryResponse> findByDeliveryStatus(String deliveryStatus) {
        return null;
    }

    @Override
    public List<NotificationHistoryResponse> findByChannel(String channel) {
        return repository.findByChannel(channel).stream()
                .map(NotificationHistoryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationHistoryResponse> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findBySentAtBetween(startDate, endDate).stream()
                .map(NotificationHistoryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationHistoryResponse> findByDateRangeAndUser(LocalDateTime startDate, LocalDateTime endDate, Long userId) {
        return null;
    }

    // Specialized queries
    @Override
    public List<NotificationHistoryResponse> getFailedNotifications() {
        return findByDeliveryStatus("FAILED");
    }

    @Override
    public List<NotificationHistoryResponse> getPendingNotifications() {
        return findByDeliveryStatus("PENDING");
    }

    @Override
    public List<NotificationHistoryResponse> getDeliveredNotifications() {
        return findByDeliveryStatus("DELIVERED");
    }

    @Override
    public List<NotificationHistoryResponse> getNotificationsByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        return findByDateRangeAndUser(startDateTime, endDateTime, userId);
    }

    @Override
    public List<NotificationHistoryResponse> getNotificationsByReservationAndChannel(Integer reservationId, String channel) {
        return null;
    }

    @Override
    public List<NotificationHistoryResponse> getRecentNotifications(int limit) {
        return null;
    }

    // Bulk operations
    @Override
    public void bulkCreateNotificationHistory(List<NotificationHistoryRequest> requests) {
        List<NotificationHistory> notifications = requests.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
        repository.saveAll(notifications);
    }

    @Override
    public void bulkUpdateNotificationHistory(List<NotificationHistoryRequest> requests) {
        // Implementation would depend on how to identify which records to update
        // For now, this is a placeholder
    }

    @Override
    public void bulkDeleteNotificationHistory(List<Integer> notificationIds) {
        repository.deleteAllById(notificationIds);
    }

    @Override
    public void bulkUpdateDeliveryStatus(List<Integer> notificationIds, String newStatus) {
        
    }

    // Check operations
    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByUserIdAndTemplateId(Long userId, Integer templateId) {
        return false;
    }

    @Override
    public long countByUserId(Long userId) {
        return 0;
    }

    @Override
    public long countByDeliveryStatus(String deliveryStatus) {
        return 0;
    }

    @Override
    public long countByChannel(String channel) {
        return 0;
    }

    @Override
    public long countByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return 0;
    }

    // Statistics and analytics
    @Override
    public long getTotalNotificationsSent() {
        return repository.count();
    }

    @Override
    public long getTotalNotificationsDelivered() {
        return countByDeliveryStatus("DELIVERED");
    }

    @Override
    public long getTotalNotificationsFailed() {
        return countByDeliveryStatus("FAILED");
    }

    @Override
    public double getDeliverySuccessRate() {
        long total = getTotalNotificationsSent();
        if (total == 0) return 0.0;
        long delivered = getTotalNotificationsDelivered();
        return (double) delivered / total * 100;
    }

    @Override
    public List<NotificationHistoryResponse> getTopUsersByNotificationCount(int limit) {
        return null;
    }

    @Override
    public List<NotificationHistoryResponse> getNotificationsByHourOfDay() {
        return null;
    }

    @Override
    public List<NotificationHistoryResponse> getNotificationsByDayOfWeek() {
        return null;
    }

    // Utility operations
    @Override
    public void markAsDelivered(Integer notificationId) {
        
    }

    @Override
    public void markAsFailed(Integer notificationId, String failureReason) {
        
        // Note: failureReason would need to be stored in a separate field
    }

    @Override
    public void retryFailedNotifications() {
        List<NotificationHistory> failedNotifications = repository.findByDeliveryStatus("FAILED");
        failedNotifications.forEach(notification -> {
            notification.setDeliveryStatus("PENDING");
            repository.save(notification);
        });
    }

    @Override
    public void cleanupOldNotifications(int daysToKeep) {
        
    }

    @Override
    public List<NotificationHistoryResponse> searchNotificationsByContent(String keyword) {
        return null;
    }

    @Override
    public Optional<NotificationHistoryResponse> findLatestNotificationByUser(Long userId) {
        return null;
    }

    // Helper method
    private NotificationHistory convertToEntity(NotificationHistoryRequest request) {
        NotificationTemplate template = notificationTemplateRepository.findById(request.templateId())
                .orElseThrow(() -> new RuntimeException("Template not found"));
        UserEntity user = userEntityRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Optional<Reservation> reservation = Optional.empty();
        if (request.reservationId() != null) {
            reservation = reservationRepository.findById(request.reservationId());
        }

        return NotificationHistory.builder()
                .template(template)
                .user(user)
                .reservation(reservation.orElse(null))
                .deliveryStatus(request.deliveryStatus())
                .content(request.content())
                .channel(request.channel())
                .build();
    }
} 