package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.Coupon;
import java.time.LocalDate;

public record CouponResponse(
    Long id,
    String code,
    Integer discountPercentage,
    LocalDate expirationDate) {
  public CouponResponse(Coupon coupon) {
    this(
        coupon.getId(),
        coupon.getCode(),
        coupon.getDiscountPercentage(),
        coupon.getExpirationDate());
  }
}
