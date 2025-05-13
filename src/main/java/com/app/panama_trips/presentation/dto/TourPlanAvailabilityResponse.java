package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.TourPlanAvailability;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TourPlanAvailabilityResponse(
        Integer id,
        Integer tourPlanId,
        LocalDate availableDate,
        Integer availableSpots,
        Boolean isAvailable,
        BigDecimal priceOverride
) {
    public TourPlanAvailabilityResponse(TourPlanAvailability tourPlanAvailability) {
        this(
                tourPlanAvailability.getId(),
                tourPlanAvailability.getTourPlan().getId(),
                tourPlanAvailability.getAvailableDate(),
                tourPlanAvailability.getAvailableSpots(),
                tourPlanAvailability.getIsAvailable(),
                tourPlanAvailability.getPriceOverride()
        );
    }
}