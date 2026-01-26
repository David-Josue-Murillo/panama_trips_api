package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyStatisticsRequest(
        @NotNull(message = "Date is required")
        LocalDate date,

        @NotNull(message = "Total reservations is required")
        @Min(value = 0, message = "Total reservations must be non-negative")
        Long totalReservations,

        @NotNull(message = "Total cancelled reservations is required")
        @Min(value = 0, message = "Total cancelled reservations must be non-negative")
        Long totalReservationsCancelled,

        @NotNull(message = "Total revenue is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Total revenue must be non-negative")
        BigDecimal totalRevenue,

        @NotNull(message = "New users is required")
        @Min(value = 0, message = "New users must be non-negative")
        Long newUsers,

        @NotNull(message = "New providers is required")
        @Min(value = 0, message = "New providers must be non-negative")
        Long newProviders,

        @NotNull(message = "Total views is required")
        @Min(value = 0, message = "Total views must be non-negative")
        Long totalViews,

        @DecimalMin(value = "0.0", inclusive = true, message = "Average rating must be at least 0.0")
        @DecimalMax(value = "5.0", inclusive = true, message = "Average rating must be at most 5.0")
        BigDecimal averageRating
) {}