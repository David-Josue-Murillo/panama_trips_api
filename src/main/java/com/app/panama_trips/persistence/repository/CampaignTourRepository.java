package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.CampaignTour;
import com.app.panama_trips.persistence.entity.CampaignTourId;
import com.app.panama_trips.persistence.entity.MarketingCampaign;
import com.app.panama_trips.persistence.entity.TourPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CampaignTourRepository extends JpaRepository<CampaignTour, CampaignTourId> {

    List<CampaignTour> findByCampaign(MarketingCampaign campaign);

    List<CampaignTour> findByTourPlan(TourPlan tourPlan);

    List<CampaignTour> findBySpecialPriceLessThan(BigDecimal price);

    @Query("SELECT ct FROM CampaignTour ct WHERE ct.campaign.id = :campaignId ORDER BY ct.featuredOrder ASC")
    List<CampaignTour> findByCampaignIdOrderByFeaturedOrder(@Param("campaignId") Integer campaignId);

    @Query("SELECT COUNT(ct) FROM CampaignTour ct WHERE ct.campaign.id = :campaignId")
    Long countToursByCampaign(@Param("campaignId") Integer campaignId);
}