package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record StreetRequest(
    @NotBlank(message = "The name of the street cannot be empty")
    @Size(min = 5, max = 50, message = "The name of the street must be between 5 and 50 characters")
    String name,

    @NotNull(message = "The district ID cannot be null")
    @Positive(message = "The district ID must be a positive number")
    Integer districtId
) {}
