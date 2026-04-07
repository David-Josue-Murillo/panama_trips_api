package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.Discount;
import java.math.BigDecimal;

public record DiscountResponse(
    Long id,
    Integer reservationId,
    Long couponId,
    BigDecimal discountAmount) {
  public DiscountResponse(Discount discount) {
    this(
        discount.getId(),
        discount.getReservationId().getId(),
        discount.getCouponId() != null ? discount.getCouponId().getId() : null,
        discount.getDiscountAmount());
  }
}
