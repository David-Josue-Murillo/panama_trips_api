package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.TourPriceHistory;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TourPriceHistoryResponse(
    Integer id,
    Integer tourPlanId,
    BigDecimal previousPrice,
    BigDecimal newPrice,
    LocalDateTime changedAt,
    Long changedById,
    String reason) {
    public TourPriceHistoryResponse(TourPriceHistory entity) {
        this(
            entity.getId(),
            entity.getTourPlan() != null ? entity.getTourPlan().getId() : null,
            entity.getPreviousPrice(),
            entity.getNewPrice(),
            entity.getChangedAt(),
            entity.getChangedBy() != null ? entity.getChangedBy().getId() : null,
            entity.getReason()
        );
    }
}
