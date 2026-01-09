package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.TourTranslation;

public record TourTranslationResponse(
        Integer tourPlanId,
        String languageCode,
        String title,
        String shortDescription,
        String description,
        String includedServices,
        String excludedServices,
        String whatToBring,
        String meetingPoint) {
    public TourTranslationResponse(TourTranslation tourTranslation) {
        this(
                tourTranslation.getId().getTourPlanId(),
                tourTranslation.getId().getLanguageCode(),
                tourTranslation.getTitle(),
                tourTranslation.getShortDescription(),
                tourTranslation.getDescription(),
                tourTranslation.getIncludedServices(),
                tourTranslation.getExcludedServices(),
                tourTranslation.getWhatToBring(),
                tourTranslation.getMeetingPoint());
    }
}
