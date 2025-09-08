package com.app.panama_trips.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TourPriceHistoryResponse(
    Integer id,
    Integer tourPlanId,
    BigDecimal previousPrice,
    BigDecimal newPrice,
    LocalDateTime changedAt,
    Long changedById,
    String reason) 
{}
