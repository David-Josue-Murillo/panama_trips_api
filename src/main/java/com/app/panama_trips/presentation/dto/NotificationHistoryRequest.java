package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record NotificationHistoryRequest(
    
    @NotNull(message = "Template ID is required")
    Integer templateId,
    
    @NotNull(message = "User ID is required")
    Long userId,
    
    Integer reservationId,
    
    @NotBlank(message = "Delivery status is required")
    @Pattern(regexp = "^(SENT|DELIVERED|FAILED|PENDING)$", 
            message = "Delivery status must be one of: SENT, DELIVERED, FAILED, PENDING")
    String deliveryStatus,
    
    @NotBlank(message = "Content is required")
    @Size(min = 10, max = 4000, message = "Content must be between 10 and 4000 characters")
    String content,
    
    @NotBlank(message = "Channel is required")
    @Pattern(regexp = "^(EMAIL|SMS|PUSH)$", 
            message = "Channel must be one of: EMAIL, SMS, PUSH")
    String channel
) {}
