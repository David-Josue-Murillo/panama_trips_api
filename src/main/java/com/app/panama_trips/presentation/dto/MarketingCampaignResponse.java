package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.MarketingCampaign;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.enums.CampaignStatus;
import com.app.panama_trips.persistence.entity.enums.CampaignType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record MarketingCampaignResponse(
        Integer id,
        String name,
        String description,
        CampaignType type,
        CampaignStatus status,
        BigDecimal budget,
        Long targetClicks,
        Long actualClicks,
        String targetAudience,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String couponCode,
        Long createdById,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<Integer> tourIds) {
    public MarketingCampaignResponse(MarketingCampaign entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getType(),
                entity.getStatus(),
                entity.getBudget(),
                entity.getTargetClicks(),
                entity.getActualClicks(),
                entity.getTargetAudience(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getCoupon() != null ? entity.getCoupon().getCode() : null,
                entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getTours().stream().map(TourPlan::getId).toList());
    }
}