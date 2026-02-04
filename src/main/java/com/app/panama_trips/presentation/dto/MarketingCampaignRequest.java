package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.enums.CampaignStatus;
import com.app.panama_trips.persistence.entity.enums.CampaignType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record MarketingCampaignRequest(
        @NotBlank(message = "Name is required") @Size(min = 3, max = 255) String name,
        @Size(max = 1000) String description,
        @NotBlank @Size(max = 50) String targetAudience,
        @NotNull CampaignType type,
        @NotNull CampaignStatus status,
        @NotNull @DecimalMin("0.0") BigDecimal budget,
        @NotNull @FutureOrPresent LocalDateTime startDate,
        @NotNull @Future LocalDateTime endDate,
        @Min(0) Long targetClicks,
        List<Integer> tourIds) {
}