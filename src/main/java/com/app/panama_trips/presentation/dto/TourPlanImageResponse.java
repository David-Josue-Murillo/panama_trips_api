package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.TourPlanImage;


public record TourPlanImageResponse(
        Integer id,
        Integer tourPlanId,
        String imageUrl,
        String altText,
        Boolean isMain,
        Integer displayOrder
) {
    public TourPlanImageResponse(TourPlanImage tourPlanImage) {
        this(
                tourPlanImage.getId(),
                tourPlanImage.getTourPlan().getId(),
                tourPlanImage.getImageUrl(),
                tourPlanImage.getAltText(),
                tourPlanImage.getIsMain(),
                tourPlanImage.getDisplayOrder()
        );
    }
}