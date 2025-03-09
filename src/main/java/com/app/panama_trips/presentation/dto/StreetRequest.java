package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StreetRequest(
    @NotBlank(message = "The name of the street cannot be empty")
    String name,

    @NotNull(message = "The district ID cannot be null")
    Integer districtId
) {}
