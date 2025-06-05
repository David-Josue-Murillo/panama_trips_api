package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.NotificationTemplate;

public record NotificationTemplateResponse(
    Integer id,
    String name,
    String subject,
    String body,
    String type,
    String variables
) {
    public NotificationTemplateResponse(NotificationTemplate notification) {
        this(
            notification.getId(),
            notification.getName(),
            notification.getSubject(),
            notification.getBody(),
            notification.getType(),
            notification.getVariables()
        );
    } 
}
