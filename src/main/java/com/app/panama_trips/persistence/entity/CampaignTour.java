package com.app.panama_trips.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "campaign_tours")
public class CampaignTour {

    @EmbeddedId
    private CampaignTourId id;

    @MapsId("campaignId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private MarketingCampaign campaign;

    @MapsId("tourPlanId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_plan_id")
    private TourPlan tourPlan;

    @Column(name = "featured_order")
    private Integer featuredOrder = 0;

    @Column(name = "special_price", precision = 10, scale = 2)
    private BigDecimal specialPrice;
}