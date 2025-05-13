package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TourPlanAvailabilityRequest(
        Integer id,

        @NotNull(message = "Tour plan ID is required")
        Integer tourPlanId,

        @NotNull(message = "Available date is required")
        @Future(message = "Available date must be in the future")
        LocalDate availableDate,

        @NotNull(message = "Available spots is required")
        @Min(value = 0, message = "Available spots must be at least 0")
        Integer availableSpots,

        @NotNull(message = "Availability status is required")
        Boolean isAvailable,

        BigDecimal priceOverride
) {}