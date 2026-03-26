package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CampaignTourRequest(
    @NotNull(message = "Campaign ID is required") 
    Integer campaignId,
    
    @NotNull(message = "Tour Plan ID is required") 
    Integer tourPlanId,
    
    @Min(0) 
    Integer featuredOrder,
    
    @DecimalMin("0.0") 
    BigDecimal specialPrice
  ) {
}
