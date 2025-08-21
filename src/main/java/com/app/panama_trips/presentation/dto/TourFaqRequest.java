package com.app.panama_trips.presentation.dto;

public record TourFaqRequest(
    Integer tourPlanId,

    String question,

    String answer,

    Integer displayOrder
) {}
