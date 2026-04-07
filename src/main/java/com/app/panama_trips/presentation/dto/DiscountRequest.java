package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record DiscountRequest(
    @NotNull(message = "Reservation ID is required") 
    Integer reservationId,

    Long couponId,

    @NotNull(message = "Discount amount is required") 
    @Positive(message = "Discount amount must be positive") 
    BigDecimal discountAmount) {
}
