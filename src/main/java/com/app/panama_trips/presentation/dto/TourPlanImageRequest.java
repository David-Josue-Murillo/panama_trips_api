package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TourPlanImageRequest (

    @NotNull(message = "Tour plan ID is required")
    Integer tourPlanId,

    @NotBlank(message = "Image URL is required")
    @Size(max = 255, message = "Image URL cannot exceed 255 characters")
    String imageUrl,

    @Size(max = 100, message = "Alt text cannot exceed 100 characters")
    String altText,

    Boolean isMain,

    Integer displayOrder
) {}