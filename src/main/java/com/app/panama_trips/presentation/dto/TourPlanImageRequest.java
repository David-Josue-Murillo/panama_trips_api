package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourPlanImageRequest {

    @NotNull(message = "Tour plan ID is required")
    private Integer tourPlanId;

    @NotBlank(message = "Image URL is required")
    @Size(max = 255, message = "Image URL cannot exceed 255 characters")
    private String imageUrl;

    @Size(max = 100, message = "Alt text cannot exceed 100 characters")
    private String altText;

    private Boolean isMain = false;

    private Integer displayOrder = 0;
}