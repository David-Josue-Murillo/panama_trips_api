package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record DistrictRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 75, message = "Name must be between 3 and 75 characters")
        String name,

        @NotNull(message = "Province is required")
        @Positive(message = "Province must be a positive number")
        Integer provinceId
) { }
