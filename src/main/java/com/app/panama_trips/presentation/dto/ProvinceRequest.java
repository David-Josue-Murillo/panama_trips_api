package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProvinceRequest(
        @NotBlank(message = "Province name is required")
        @Size(min = 5, max = 50, message = "Province name must be between 5 and 50 characters")
        String name
) { }
