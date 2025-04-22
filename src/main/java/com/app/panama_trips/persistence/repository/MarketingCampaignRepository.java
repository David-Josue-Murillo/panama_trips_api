package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Coupon;
import com.app.panama_trips.persistence.entity.MarketingCampaign;
import com.app.panama_trips.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MarketingCampaignRepository extends JpaRepository<MarketingCampaign, Integer> {

    List<MarketingCampaign> findByStartDateGreaterThanEqual(LocalDate date);

    List<MarketingCampaign> findByEndDateGreaterThanEqual(LocalDate date);

    @Query("SELECT mc FROM MarketingCampaign mc WHERE CURRENT_DATE BETWEEN mc.startDate AND mc.endDate")
    List<MarketingCampaign> findActiveMarketingCampaigns();

    List<MarketingCampaign> findByCoupon(Coupon coupon);

    List<MarketingCampaign> findByCreatedBy(UserEntity user);

    Optional<MarketingCampaign> findByName(String name);

    @Query("SELECT mc FROM MarketingCampaign mc WHERE mc.targetAudience = :audience AND CURRENT_DATE BETWEEN mc.startDate AND mc.endDate")
    List<MarketingCampaign> findActiveByTargetAudience(@Param("audience") String audience);

    @Query("SELECT mc FROM MarketingCampaign mc WHERE LOWER(mc.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(mc.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<MarketingCampaign> searchByNameOrDescription(@Param("keyword") String keyword);
}