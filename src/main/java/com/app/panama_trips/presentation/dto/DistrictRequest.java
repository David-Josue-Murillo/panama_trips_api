package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DistrictRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Province is required")
        @Positive(message = "Province must be a positive number")
        Integer province
) { }
