package com.app.panama_trips.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.app.panama_trips.persistence.entity.TourAssignment;

public record TourAssignmentResponse(
    Integer id,
    String guideName,
    String tourPlanName,
    BigDecimal tourPlanPrice,
    String duration,
    LocalDate reservationDate,
    String status,
    String notes,
    LocalDate createdAt
) {
    public TourAssignmentResponse(TourAssignment tourAssignment) {
        this(
            tourAssignment.getId(),
            tourAssignment.getGuide().getUser().getName() + " " + tourAssignment.getGuide().getUser().getLastname(),
            tourAssignment.getTourPlan().getTitle(),
            tourAssignment.getTourPlan().getPrice(),
            tourAssignment.getTourPlan().getDuration() + " days",
            tourAssignment.getReservationDate(),
            tourAssignment.getStatus(),
            tourAssignment.getNotes(),
            tourAssignment.getCreatedAt()
        );
    }
}
