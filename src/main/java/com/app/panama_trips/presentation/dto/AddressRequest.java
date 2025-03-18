package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AddressRequest (
        @NotBlank(message = "Street is required")
        @Size(min = 5, max = 255, message = "Street must be between 5 and 255 characters")
        String street,

        @NotNull(message = "Postal code is required")
        @Positive(message = "Postal code must be a positive number")
        String postalCode,

        @NotNull(message = "District is required")
        @Positive(message = "District ID must be a positive number")
        Integer districtId,

        @Size(max = 500, message = "Additional info cannot exceed 500 characters")
        String additionalInfo
) { }
