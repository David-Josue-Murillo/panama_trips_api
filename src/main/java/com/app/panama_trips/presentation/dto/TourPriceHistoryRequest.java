package com.app.panama_trips.presentation.dto;

import java.math.BigDecimal;

public record TourPriceHistoryRequest(
        Integer tourPlanId,
        BigDecimal previousPrice,
        BigDecimal newPrice,
        Long changedById,
        String reason) {
}
