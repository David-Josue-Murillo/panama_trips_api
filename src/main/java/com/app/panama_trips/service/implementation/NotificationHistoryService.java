package com.app.panama_trips.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationHistoryService implements INotificationHistoryService {

    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_DELIVERED = "DELIVERED";
    private static final String STATUS_FAILED = "FAILED";
    private static final List<String> VALID_DELIVERY_STATUSES = List.of(STATUS_PENDING, STATUS_DELIVERED, STATUS_FAILED);

    private static final String CHANNEL_EMAIL = "EMAIL";
    private static final String CHANNEL_SMS = "SMS";
    private static final String CHANNEL_PUSH = "PUSH";
    private static final List<String> VALID_CHANNELS = List.of(CHANNEL_EMAIL, CHANNEL_SMS, CHANNEL_PUSH);

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
        return new NotificationHistoryResponse(findNotificationOrThrow(id));
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
        NotificationHistory existing = findNotificationOrThrow(id);
        updateFromRequest(existing, request);
        return new NotificationHistoryResponse(repository.save(existing));
    }

    @Override
    @Transactional
    public void deleteNotificationHistory(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Notification history not found with: " + id);
        }
        repository.deleteById(id);
    }

    // Find operations

    @Override
    public List<NotificationHistoryResponse> findByUserId(Long userId) {
        UserEntity user = findUserOrFail(userId);
        return toResponseList(repository.findByUser(user));
    }

    @Override
    public List<NotificationHistoryResponse> findByTemplateId(Integer templateId) {
        NotificationTemplate template = findTemplateOrFail(templateId);
        return toResponseList(repository.findByTemplate(template));
    }

    @Override
    public List<NotificationHistoryResponse> findByReservationId(Integer reservationId) {
        Reservation reservation = findReservationOrFail(reservationId);
        return toResponseList(repository.findByReservation(reservation));
    }

    @Override
    public List<NotificationHistoryResponse> findByDeliveryStatus(String deliveryStatus) {
        validateDeliveryStatus(deliveryStatus);
        return toResponseList(repository.findByDeliveryStatus(deliveryStatus));
    }

    @Override
    public List<NotificationHistoryResponse> findByChannel(String channel) {
        return toResponseList(repository.findByChannel(channel));
    }

    @Override
    public List<NotificationHistoryResponse> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return toResponseList(repository.findBySentAtBetween(startDate, endDate));
    }

    @Override
    public List<NotificationHistoryResponse> findByDateRangeAndUser(LocalDateTime startDate, LocalDateTime endDate, Long userId) {
        UserEntity user = findUserOrFail(userId);
        return toResponseList(repository.findByUserAndSentAtBetween(user, startDate, endDate));
    }

    // Specialized queries

    @Override
    public List<NotificationHistoryResponse> getFailedNotifications() {
        return findByDeliveryStatus(STATUS_FAILED);
    }

    @Override
    public List<NotificationHistoryResponse> getPendingNotifications() {
        return findByDeliveryStatus(STATUS_PENDING);
    }

    @Override
    public List<NotificationHistoryResponse> getDeliveredNotifications() {
        return findByDeliveryStatus(STATUS_DELIVERED);
    }

    @Override
    public List<NotificationHistoryResponse> getNotificationsByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        return findByDateRangeAndUser(startDateTime, endDateTime, userId);
    }

    @Override
    public List<NotificationHistoryResponse> getNotificationsByReservationAndChannel(Integer reservationId, String channel) {
        Reservation reservation = findReservationOrFail(reservationId);
        return toResponseList(repository.findByReservationAndChannel(reservation, channel));
    }

    @Override
    public List<NotificationHistoryResponse> getRecentNotifications(int limit) {
        return toResponseList(repository.findAllByOrderBySentAtDesc(PageRequest.of(0, limit)));
    }

    // Bulk operations

    @Override
    @Transactional
    public void bulkCreateNotificationHistory(List<NotificationHistoryRequest> requests) {
        requests.forEach(this::validateRequest);
        List<NotificationHistory> notifications = requests.stream()
                .map(this::builderFromRequest)
                .toList();
        repository.saveAll(notifications);
        log.info("Bulk created {} notification history records", notifications.size());
    }

    @Override
    @Transactional
    public void bulkDeleteNotificationHistory(List<Integer> notificationIds) {
        repository.deleteAllById(notificationIds);
    }

    @Override
    @Transactional
    public void bulkUpdateDeliveryStatus(List<Integer> notificationIds, String newStatus) {
        validateDeliveryStatus(newStatus);
        List<NotificationHistory> notifications = repository.findAllById(notificationIds);
        notifications.forEach(n -> n.setDeliveryStatus(newStatus));
        repository.saveAll(notifications);
        log.info("Bulk updated delivery status to '{}' for {} notifications", newStatus, notifications.size());
    }

    // Check operations

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByUserIdAndTemplateId(Long userId, Integer templateId) {
        UserEntity user = findUserOrFail(userId);
        NotificationTemplate template = findTemplateOrFail(templateId);
        return repository.existsByUserAndTemplate(user, template);
    }

    @Override
    public long countByUserId(Long userId) {
        UserEntity user = findUserOrFail(userId);
        return repository.countByUser(user);
    }

    @Override
    public long countByDeliveryStatus(String deliveryStatus) {
        validateDeliveryStatus(deliveryStatus);
        return repository.countByDeliveryStatus(deliveryStatus);
    }

    @Override
    public long countByChannel(String channel) {
        validateChannel(channel);
        return repository.countByChannel(channel);
    }

    @Override
    public long countByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.countBySentAtBetween(startDate, endDate);
    }

    // Statistics and analytics

    @Override
    public long getTotalNotificationsSent() {
        return repository.count();
    }

    @Override
    public long getTotalNotificationsDelivered() {
        return countByDeliveryStatus(STATUS_DELIVERED);
    }

    @Override
    public long getTotalNotificationsFailed() {
        return countByDeliveryStatus(STATUS_FAILED);
    }

    @Override
    public double getDeliverySuccessRate() {
        long total = getTotalNotificationsSent();
        if (total == 0) return 0.0;
        long delivered = getTotalNotificationsDelivered();
        return (double) delivered / total * 100;
    }

    // Utility operations

    @Override
    @Transactional
    public void markAsDelivered(Integer notificationId) {
        NotificationHistory notification = findNotificationOrThrow(notificationId);
        notification.setDeliveryStatus(STATUS_DELIVERED);
        repository.save(notification);
        log.info("Notification {} marked as delivered", notificationId);
    }

    @Override
    @Transactional
    public void markAsFailed(Integer notificationId, String failureReason) {
        NotificationHistory notification = findNotificationOrThrow(notificationId);
        notification.setDeliveryStatus(STATUS_FAILED);
        repository.save(notification);
        log.warn("Notification {} marked as failed. Reason: {}", notificationId, failureReason);
    }

    @Override
    @Transactional
    public void retryFailedNotifications() {
        List<NotificationHistory> failedNotifications = repository.findByDeliveryStatus(STATUS_FAILED);
        failedNotifications.forEach(notification -> notification.setDeliveryStatus(STATUS_PENDING));
        repository.saveAll(failedNotifications);
        log.info("Retried {} failed notifications", failedNotifications.size());
    }

    @Override
    @Transactional
    public void cleanupOldNotifications(int daysToKeep) {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(daysToKeep);
        repository.deleteBySentAtBefore(cutoff);
        log.info("Cleaned up notifications older than {} days", daysToKeep);
    }

    @Override
    public List<NotificationHistoryResponse> searchNotificationsByContent(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Keyword must not be empty");
        }
        return toResponseList(repository.searchByContent(keyword));
    }

    @Override
    public Optional<NotificationHistoryResponse> findLatestNotificationByUser(Long userId) {
        UserEntity user = findUserOrFail(userId);
        return repository.findFirstByUserOrderBySentAtDesc(user)
                .map(NotificationHistoryResponse::new);
    }

    // Helper methods

    private NotificationHistory findNotificationOrThrow(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification history not found with: " + id));
    }

    private void validateRequest(NotificationHistoryRequest request) {
        if (!notificationTemplateRepository.existsById(request.templateId())) {
            throw new ResourceNotFoundException("Template not found");
        }
        if (!userEntityRepository.existsById(request.userId())) {
            throw new ResourceNotFoundException("User not found");
        }
        if (request.reservationId() != null && !reservationRepository.existsById(request.reservationId())) {
            throw new ResourceNotFoundException("Reservation not found");
        }
    }

    private NotificationHistory builderFromRequest(NotificationHistoryRequest request) {
        NotificationTemplate template = findTemplateOrFail(request.templateId());
        UserEntity user = findUserOrFail(request.userId());
        Reservation reservation = null;

        if (request.reservationId() != null) {
            reservation = reservationRepository.findById(request.reservationId()).orElse(null);
        }

        return NotificationHistory.builder()
                .template(template)
                .user(user)
                .reservation(reservation)
                .deliveryStatus(request.deliveryStatus())
                .content(request.content())
                .channel(request.channel())
                .build();
    }

    private NotificationTemplate findTemplateOrFail(Integer templateId) {
        return notificationTemplateRepository.findById(templateId)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));
    }

    private UserEntity findUserOrFail(Long userId) {
        return userEntityRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Reservation findReservationOrFail(Integer reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
    }

    private void updateFromRequest(NotificationHistory existing, NotificationHistoryRequest request) {
        existing.setTemplate(findTemplateOrFail(request.templateId()));
        existing.setUser(findUserOrFail(request.userId()));
        if (request.reservationId() != null) {
            existing.setReservation(findReservationOrFail(request.reservationId()));
        } else {
            existing.setReservation(null);
        }
        existing.setDeliveryStatus(request.deliveryStatus());
        existing.setContent(request.content());
        existing.setChannel(request.channel());
    }

    private void validateDeliveryStatus(String status) {
        if (status == null || !VALID_DELIVERY_STATUSES.contains(status)) {
            throw new IllegalArgumentException("Invalid delivery status: " + status);
        }
    }

    private void validateChannel(String channel) {
        if (channel == null || !VALID_CHANNELS.contains(channel)) {
            throw new IllegalArgumentException("Invalid channel: " + channel);
        }
    }

    private List<NotificationHistoryResponse> toResponseList(List<NotificationHistory> notifications) {
        return notifications.stream()
                .map(NotificationHistoryResponse::new)
                .toList();
    }
}
