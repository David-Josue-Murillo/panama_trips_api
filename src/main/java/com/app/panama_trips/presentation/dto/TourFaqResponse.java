package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.TourFaq;

import java.time.LocalDateTime;

public record TourFaqResponse(
        Integer id,
        Integer tourPlanId,
        String question,
        String answer,
        Integer displayOrder,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
