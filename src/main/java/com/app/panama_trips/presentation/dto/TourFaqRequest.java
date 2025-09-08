package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

public record TourFaqRequest(
    @NotNull(message = "Tour plan ID is required") 
    @Min(value = 1, message = "Tour plan ID must be a positive number") 
    Integer tourPlanId,

    @NotBlank(message = "Question is required") 
    @Size(min = 10, max = 500, message = "Question must be between 10 and 500 characters") 
    String question,

    @NotBlank(message = "Answer is required") 
    @Size(min = 10, max = 2000, message = "Answer must be between 10 and 2000 characters") 
    String answer,

    @NotNull(message = "Display order is required") 
    @Min(value = 0, message = "Display order must be a non-negative number") 
    Integer displayOrder
) {}