package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.NotificationHistory;
import com.app.panama_trips.persistence.entity.NotificationTemplate;
import com.app.panama_trips.persistence.entity.Reservation;
import com.app.panama_trips.persistence.entity.UserEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public record NotificationHistoryResponse(
    Integer id,
    Integer templateId,
    String templateName,
    Long userId,
    String userName,
    String userEmail,
    Integer reservationId,
    LocalDate reservationDate,
    LocalDateTime sentAt,
    String deliveryStatus,
    String content,
    String channel
) {
    public NotificationHistoryResponse(NotificationHistory notification) {
        this(
            notification.getId(),
            extractTemplateId(notification.getTemplate()),
            extractTemplateName(notification.getTemplate()),
            extractUserId(notification.getUser()),
            extractUserName(notification.getUser()),
            extractUserEmail(notification.getUser()),
            extractReservationId(notification.getReservation()),
            extractReservationDate(notification.getReservation()),
            notification.getSentAt(),
            notification.getDeliveryStatus(),
            notification.getContent(),
            notification.getChannel()
        );
    }

    // Métodos helper con mejor manejo de Optional y nombres más descriptivos
    private static Integer extractTemplateId(NotificationTemplate template) {
        return Optional.ofNullable(template)
                .map(NotificationTemplate::getId)
                .orElse(null);
    }

    private static String extractTemplateName(NotificationTemplate template) {
        return Optional.ofNullable(template)
                .map(NotificationTemplate::getName)
                .orElse(null);
    }

    private static Long extractUserId(UserEntity user) {
        return Optional.ofNullable(user)
                .map(UserEntity::getId)
                .orElse(null);
    }

    private static String extractUserName(UserEntity user) {
        return Optional.ofNullable(user)
                .map(UserEntity::getName)
                .orElse(null);
    }

    private static String extractUserEmail(UserEntity user) {
        return Optional.ofNullable(user)
                .map(UserEntity::getEmail)
                .orElse(null);
    }

    private static Integer extractReservationId(Reservation reservation) {
        return Optional.ofNullable(reservation)
                .map(Reservation::getId)
                .orElse(null);
    }

    /**
     * Extrae la fecha de reservación manteniendo el tipo LocalDate
     * para consistencia con la entidad Reservation.
     */
    private static LocalDate extractReservationDate(Reservation reservation) {
        return Optional.ofNullable(reservation)
                .map(Reservation::getReservationDate)
                .orElse(null);
    }
}
