package com.app.panama_trips.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = true)
    public Page<NotificationHistoryResponse> getAllNotificationHistory(Pageable pageable) {
        return repository.findAll(pageable).map(NotificationHistoryResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationHistoryResponse getNotificationHistoryById(Integer id) {
        return repository.findById(id)
                .map(NotificationHistoryResponse::new)
                .orElseThrow(() -> new RuntimeException("Notification history not found"));
    }

    @Override
    @Transactional
    public NotificationHistoryResponse saveNotificationHistory(NotificationHistoryRequest request) {
        validateRequest(request);
        NotificationHistory notification = builderFromRequest(request);
        return new NotificationHistoryResponse(repository.save(notification));
    }

    @Override
    @Transactional
    public NotificationHistoryResponse updateNotificationHistory(Integer id, NotificationHistoryRequest request) {
        validateRequest(request);
        NotificationHistory existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification history not found"));

        updateFromRequest(existing, request);
        return new NotificationHistoryResponse(repository.save(existing));
    }

    @Override
    @Transactional
    public void deleteNotificationHistory(Integer id) {
        if(!repository.existsById(id)) {
            throw new ResourceNotFoundException("Notification history not found with: " + id);
        }
        repository.deleteById(id);
    }

    // Find operations
    @Override
    @Transactional(readOnly = true)
    public List<NotificationHistoryResponse> findByUserId(Long userId) {
        UserEntity user = findUserOrFail(userId);
        return repository.findByUser(user).stream()
            .map(NotificationHistoryResponse::new)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationHistoryResponse> findByTemplateId(Integer templateId) {
        NotificationTemplate template = findTemplateOrFail(templateId);
        return repository.findByTemplate(template).stream()
            .map(NotificationHistoryResponse::new)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationHistoryResponse> findByReservationId(Integer reservationId) {
        Reservation reservation = findReservationOrFail(reservationId);
        return repository.findByReservation(reservation).stream()
            .map(NotificationHistoryResponse::new)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationHistoryResponse> findByDeliveryStatus(String deliveryStatus) {
        validateDeliveryStatus(deliveryStatus);
        return repository.findByDeliveryStatus(deliveryStatus).stream()
            .map(NotificationHistoryResponse::new)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationHistoryResponse> findByChannel(String channel) {
        return repository.findByChannel(channel).stream()
                .map(NotificationHistoryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationHistoryResponse> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findBySentAtBetween(startDate, endDate).stream()
                .map(NotificationHistoryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationHistoryResponse> findByDateRangeAndUser(LocalDateTime startDate, LocalDateTime endDate,
            Long userId) {
        return null;
    }

    // Specialized queries
    @Override
    @Transactional(readOnly = true)
    public List<NotificationHistoryResponse> getFailedNotifications() {
        return findByDeliveryStatus("FAILED");
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationHistoryResponse> getPendingNotifications() {
        return findByDeliveryStatus("PENDING");
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationHistoryResponse> getDeliveredNotifications() {
        return findByDeliveryStatus("DELIVERED");
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationHistoryResponse> getNotificationsByUserAndDateRange(Long userId, LocalDate startDate,
            LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        return findByDateRangeAndUser(startDateTime, endDateTime, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationHistoryResponse> getNotificationsByReservationAndChannel(Integer reservationId,
            String channel) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationHistoryResponse> getRecentNotifications(int limit) {
        // Suponiendo que quieres los más recientes de todos los usuarios
        List<NotificationHistory> all = repository.findAll();
        return all.stream()
            .sorted((a, b) -> b.getSentAt().compareTo(a.getSentAt()))
            .limit(limit)
            .map(NotificationHistoryResponse::new)
            .toList();
    }

    // Bulk operations
    @Override
    @Transactional
    public void bulkCreateNotificationHistory(List<NotificationHistoryRequest> requests) {
        
    }

    @Override
    @Transactional
    public void bulkUpdateNotificationHistory(List<NotificationHistoryRequest> requests) {
        // Implementation would depend on how to identify which records to update
        // For now, this is a placeholder
    }

    @Override
    @Transactional
    public void bulkDeleteNotificationHistory(List<Integer> notificationIds) {
        repository.deleteAllById(notificationIds);
    }

    @Override
    @Transactional
    public void bulkUpdateDeliveryStatus(List<Integer> notificationIds, String newStatus) {

    }

    // Check operations
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUserIdAndTemplateId(Long userId, Integer templateId) {
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public long countByUserId(Long userId) {
        UserEntity user = findUserOrFail(userId);
        return repository.findByUser(user).size();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByDeliveryStatus(String deliveryStatus) {
        validateDeliveryStatus(deliveryStatus);
        return repository.findByDeliveryStatus(deliveryStatus).size();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByChannel(String channel) {
        validateChannel(channel);
        return repository.findByChannel(channel).size();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return 0;
    }

    // Statistics and analytics
    @Override
    @Transactional(readOnly = true)
    public long getTotalNotificationsSent() {
        return repository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalNotificationsDelivered() {
        return countByDeliveryStatus("DELIVERED");
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalNotificationsFailed() {
        return countByDeliveryStatus("FAILED");
    }

    @Override
    @Transactional(readOnly = true)
    public double getDeliverySuccessRate() {
        long total = getTotalNotificationsSent();
        if (total == 0)
            return 0.0;
        long delivered = getTotalNotificationsDelivered();
        return (double) delivered / total * 100;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationHistoryResponse> getTopUsersByNotificationCount(int limit) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationHistoryResponse> getNotificationsByHourOfDay() {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationHistoryResponse> getNotificationsByDayOfWeek() {
        return null;
    }

    // Utility operations
    @Override
    @Transactional
    public void markAsDelivered(Integer notificationId) {

    }

    @Override
    @Transactional
    public void markAsFailed(Integer notificationId, String failureReason) {

        // Note: failureReason would need to be stored in a separate field
    }

    @Override
    @Transactional
    public void retryFailedNotifications() {
        List<NotificationHistory> failedNotifications = repository.findByDeliveryStatus("FAILED");
        failedNotifications.forEach(notification -> {
            notification.setDeliveryStatus("PENDING");
            repository.save(notification);
        });
    }

    @Override
    @Transactional
    public void cleanupOldNotifications(int daysToKeep) {

    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationHistoryResponse> searchNotificationsByContent(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Keyword must not be empty");
        }
        return repository.searchByContent(keyword).stream()
            .map(NotificationHistoryResponse::new)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationHistoryResponse> findLatestNotificationByUser(Long userId) {
        UserEntity user = findUserOrFail(userId);
        return repository.findByUser(user).stream()
            .max((a, b) -> a.getSentAt().compareTo(b.getSentAt()))
            .map(NotificationHistoryResponse::new);
    }

    // Helper method
    private void validateRequest(NotificationHistoryRequest request) {
        if (!notificationTemplateRepository.existsById(request.templateId())) {
            throw new ResourceNotFoundException("Template not found");
        }
        if (!userEntityRepository.existsById(request.userId())) {
            throw new ResourceNotFoundException("User not found");
        }
        // Si hay reservationId, validar existencia
        if (request.reservationId() != null && !reservationRepository.existsById(request.reservationId())) {
            throw new ResourceNotFoundException("Reservation not found");
        }
    }

    private NotificationHistory builderFromRequest(NotificationHistoryRequest request) {
        NotificationTemplate template = findTemplateOrFail(request.templateId());
        UserEntity user = findUserOrFail(request.userId());
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

    private NotificationTemplate findTemplateOrFail(Integer templateId) {
        return notificationTemplateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));
    }

    private UserEntity findUserOrFail(Long userId) {
        return userEntityRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Reservation findReservationOrFail(Integer reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
    }

    private void updateFromRequest(NotificationHistory existingNotification, NotificationHistoryRequest request) {
        existingNotification.setTemplate(findTemplateOrFail(request.templateId()));
        existingNotification.setUser(findUserOrFail(request.userId()));
        existingNotification.setReservation(findReservationOrFail(request.reservationId()));
        existingNotification.setDeliveryStatus(request.deliveryStatus());
        existingNotification.setContent(request.content());
        existingNotification.setChannel(request.channel());
    }

     // Métodos de validación auxiliares (si no existen)
     private void validateDeliveryStatus(String status) {
        List<String> validStatuses = List.of("PENDING", "DELIVERED", "FAILED");
        if (status == null || !validStatuses.contains(status)) {
            throw new IllegalArgumentException("Invalid delivery status: " + status);
        }
    }

    private void validateChannel(String channel) {
        List<String> validChannels = List.of("EMAIL", "SMS", "PUSH");
        if (channel == null || !validChannels.contains(channel)) {
            throw new IllegalArgumentException("Invalid channel: " + channel);
        }
    }
}