package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.TourPlan;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TourPlanResponse(
        Integer id,
        String title,
        String description,
        BigDecimal price,
        Integer duration,
        Integer availableSpots,
        LocalDateTime createdAt,
        ProviderResponse provider
) {
    public TourPlanResponse(TourPlan tourPlan) {
        this(
                tourPlan.getId(),
                tourPlan.getTitle(),
                tourPlan.getDescription(),
                tourPlan.getPrice(),
                tourPlan.getDuration(),
                tourPlan.getAvailableSpots(),
                tourPlan.getCreatedAt(),
                new ProviderResponse(tourPlan.getProviderId())
        );
    }
}
