package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record TourPriceHistoryRequest(
    @NotNull(message = "Tour plan ID is required") 
    @Min(value = 1, message = "Tour plan ID must be a positive number") 
    Integer tourPlanId,

    @NotNull(message = "Previous price is required") 
    @DecimalMin(value = "0.00", inclusive = true, message = "Previous price must be >= 0.00") 
    @Digits(integer = 8, fraction = 2, message = "Previous price must have up to 8 integer digits and 2 decimals") 
    BigDecimal previousPrice,

    @NotNull(message = "New price is required") 
    @DecimalMin(value = "0.00", inclusive = true, message = "New price must be >= 0.00") 
    @Digits(integer = 8, fraction = 2, message = "New price must have up to 8 integer digits and 2 decimals") 
    BigDecimal newPrice,

    @Min(value = 1, message = "Changed by ID must be a positive number") 
    Long changedById,

    @Size(max = 1000, message = "Reason must be at most 1000 characters") 
    String reason) 
{}
