package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.enums.DifficultyLevel;
import com.app.panama_trips.persistence.entity.enums.TourPlanStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record TourPlanResponse(
        Integer id,
        String title,
        String description,
        BigDecimal price,
        Integer duration,
        Integer availableSpots,
        String providerName,
        String providerEmail,
        String providerPhone,
        String address,
        TourPlanStatus status,
        DifficultyLevel difficultyLevel,
        LocalTime startTime,
        LocalTime endTime,
        LocalDateTime createdAt,
        List<String> includedServices,
        List<String> excludedServices,
        List<String> whatToBring,
        List<String> tags,
        List<String> languageOptions,
        List<String> availableDays,
        List<String> imageGallery) {
    public TourPlanResponse(TourPlan tourPlan) {
        this(
                tourPlan.getId(),
                tourPlan.getTitle(),
                tourPlan.getDescription(),
                tourPlan.getPricing() != null ? tourPlan.getPricing().getPrice() : null,
                tourPlan.getDuration(),
                tourPlan.getAvailableSpots(),
                tourPlan.getProvider().getName(),
                tourPlan.getProvider().getEmail(),
                tourPlan.getProvider().getPhone(),
                tourPlan.getProvider().getAddress().getStreet(),
                tourPlan.getStatus(),
                tourPlan.getDifficultyLevel(),
                tourPlan.getSchedule() != null ? tourPlan.getSchedule().getStartTime() : null,
                tourPlan.getSchedule() != null ? tourPlan.getSchedule().getEndTime() : null,
                tourPlan.getCreatedAt(),
                tourPlan.getIncludedServices(),
                tourPlan.getExcludedServices(),
                tourPlan.getWhatToBring(),
                tourPlan.getTags(),
                tourPlan.getLanguageOptions(),
                tourPlan.getSchedule() != null ? tourPlan.getSchedule().getAvailableDays() : null,
                tourPlan.getMedia() != null ? tourPlan.getMedia().getImageGallery() : null);
    }
}
