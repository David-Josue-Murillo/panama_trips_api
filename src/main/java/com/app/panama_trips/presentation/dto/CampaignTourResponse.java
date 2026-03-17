package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.CampaignTour;
import java.math.BigDecimal;

public record CampaignTourResponse(
    Integer campaignId,
    Integer tourPlanId,
    Integer featuredOrder,
    BigDecimal specialPrice) {
  public CampaignTourResponse(CampaignTour entity) {
    this(
        entity.getId() != null ? entity.getId().getCampaignId() : null,
        entity.getId() != null ? entity.getId().getTourPlanId() : null,
        entity.getFeaturedOrder(),
        entity.getSpecialPrice());
  }
}
