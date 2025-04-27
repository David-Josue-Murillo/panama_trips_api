package com.app.panama_trips.presentation.dto;

import java.io.Serializable;

public record TourPlanImageResponse(
        Integer id,
        Integer tourPlanId,
        String imageUrl,
        String altText,
        Boolean isMain,
        Integer displayOrder
) implements Serializable {}