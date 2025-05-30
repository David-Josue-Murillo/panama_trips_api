package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record NotificationTemplateRequest(
    
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s-_]+$", message = "Name can only contain letters, numbers, spaces, hyphens and underscores")
    String name,
    
    @NotBlank(message = "Subject is required")
    @Size(max = 200, message = "Subject cannot exceed 200 characters")
    String subject,
    
    @NotBlank(message = "Body is required")
    @Size(min = 10, max = 4000, message = "Body must be between 10 and 4000 characters")
    String body,
    
    @NotBlank(message = "Types is required")
    @Pattern(regexp = "^(EMAIL|SMS|PUSH|ALL)(,(EMAIL|SMS|PUSH|ALL))*$", 
            message = "Types must be one or more of: EMAIL, SMS, PUSH, ALL (comma-separated)")
    String types,
    
    @NotNull(message = "Variables cannot be null")
    @Pattern(regexp = "^[a-zA-Z0-9_,]+$", 
            message = "Variables must be comma-separated alphanumeric values")
    String variables
) {}
