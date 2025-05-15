package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TourPlanSpecialPriceRequest(
        @NotNull(message = "Tour plan ID is required")
        Integer tourPlanId,

        @NotNull(message = "Start date is required")
        @FutureOrPresent(message = "Start date must be today or in the future")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        @Future(message = "End date must be in the future")
        LocalDate endDate,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
        @Digits(integer = 8, fraction = 2, message = "Price must have at most 8 digits in total and 2 decimal places")
        BigDecimal price,

        @Size(max = 100, message = "Description cannot exceed 100 characters")
        String description
) {
    public TourPlanSpecialPriceRequest {
        if (startDate != null && endDate != null && !endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }
}