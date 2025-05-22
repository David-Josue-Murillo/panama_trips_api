package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.*;

public record CancellationPolicyRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String name,

        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        String description,

        @NotNull(message = "Refund percentage is required")
        @Min(value = 0, message = "Refund percentage must be between 0 and 100")
        @Max(value = 100, message = "Refund percentage must be between 0 and 100")
        Integer refundPercentage,

        @NotNull(message = "Days before tour is required")
        @Min(value = 0, message = "Days before tour must be positive")
        Integer daysBeforeTour
) {}