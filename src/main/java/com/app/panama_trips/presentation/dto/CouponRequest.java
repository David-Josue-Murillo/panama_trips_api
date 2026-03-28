package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record CouponRequest(
    @NotBlank(message = "Coupon code is required") 
    @Size(min = 3, max = 50, message = "Coupon code must be between 3 and 50 characters") 
    String code,

    @NotNull(message = "Discount percentage is required") 
    @Min(value = 1, message = "Discount percentage must be at least 1") 
    @Max(value = 100, message = "Discount percentage cannot exceed 100") 
    Integer discountPercentage,

    @NotNull(message = "Expiration date is required") 
    @FutureOrPresent(message = "Expiration date must be in the present or future") 
    LocalDate expirationDate) {
}
