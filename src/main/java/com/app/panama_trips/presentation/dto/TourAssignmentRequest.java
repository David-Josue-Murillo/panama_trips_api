package com.app.panama_trips.presentation.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TourAssignmentRequest(
    @NotNull(message = "Guide ID is required")
    Integer guideId,

    @NotNull(message = "Tour Plan ID is required")
    Integer tourPlanId,

    @NotNull(message = "Reservation date is required")
    @Future(message = "Reservation date must be in the future")
    LocalDate reservationDate,

    @NotNull(message = "Status is required")
    @Size(max = 20, message = "Status must not exceed 20 characters")
    @Pattern(regexp = "^(ASSIGNED|CANCELLED|COMPLETED)$", message = "Status must be one of: ASSIGNED, CANCELLED, COMPLETED")
    String status,

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    String notes
) {

}
