package com.app.panama_trips.presentation.dto;

public record TourTranslationResponse(
        Integer tourPlanId,
        String languageCode,
        String title,
        String shortDescription,
        String description,
        String includedServices,
        String excludedServices,
        String whatToBring,
        String meetingPoint) {}
