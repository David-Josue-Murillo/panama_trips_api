package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.TourPlanSpecialPrice;
import java.math.BigDecimal;
import java.time.LocalDate;

public record TourPlanSpecialPriceResponse(
        Integer id,
        Integer tourPlanId,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal price,
        String description
) {
    public TourPlanSpecialPriceResponse(TourPlanSpecialPrice specialPrice) {
        this(
                specialPrice.getId(),
                specialPrice.getTourPlan().getId(),
                specialPrice.getStartDate(),
                specialPrice.getEndDate(),
                specialPrice.getPrice(),
                specialPrice.getDescription()
        );
    }
}