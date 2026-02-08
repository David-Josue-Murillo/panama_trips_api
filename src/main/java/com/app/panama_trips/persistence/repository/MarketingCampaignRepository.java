package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Coupon;
import com.app.panama_trips.persistence.entity.MarketingCampaign;
import com.app.panama_trips.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MarketingCampaignRepository extends JpaRepository<MarketingCampaign, Integer> {

    /**
     * Existence and basic finds
     */
    boolean existsByNameIgnoreCase(String name);
    Optional<MarketingCampaign> findByNameIgnoreCase(String name);
    Optional<MarketingCampaign> findByName(String name);

    /**
     * By creator
     */
    List<MarketingCampaign> findByCreatedBy(UserEntity user);
    Page<MarketingCampaign> findByCreatedByOrderByCreatedAtDesc(UserEntity user, Pageable pageable);

    /**
     * Search
     */
    @Query("SELECT mc FROM MarketingCampaign mc WHERE " +
           "LOWER(TRIM(mc.name)) LIKE LOWER(CONCAT('%', TRIM(:keyword), '%')) OR " +
           "LOWER(TRIM(mc.description)) LIKE LOWER(CONCAT('%', TRIM(:keyword), '%'))")
    Page<MarketingCampaign> searchByNameOrDescription(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Active campaigns (status ACTIVE and within dates)
     */
    @Query("SELECT mc FROM MarketingCampaign mc WHERE " +
           "mc.status = 'ACTIVE' AND " +
           "CURRENT_TIMESTAMP BETWEEN mc.startDate AND mc.endDate")
    List<MarketingCampaign> findActiveMarketingCampaigns();

    @Query("SELECT mc FROM MarketingCampaign mc WHERE " +
           "mc.status = 'ACTIVE' AND " +
           "CURRENT_TIMESTAMP BETWEEN mc.startDate AND mc.endDate")
    Page<MarketingCampaign> findActiveMarketingCampaigns(Pageable pageable);

    /**
     * By target audience (active)
     */
    @Query("SELECT mc FROM MarketingCampaign mc WHERE " +
           "mc.targetAudience = :audience AND " +
           "mc.status = 'ACTIVE' AND " +
           "CURRENT_TIMESTAMP BETWEEN mc.startDate AND mc.endDate")
    List<MarketingCampaign> findActiveByTargetAudience(@Param("audience") String audience);

    /**
     * Date-based
     */
    List<MarketingCampaign> findByStartDateGreaterThanEqualOrderByStartDateAsc(LocalDate date);
    List<MarketingCampaign> findByEndDateLessThanOrderByEndDateAsc(LocalDate date);
    List<MarketingCampaign> findAllByOrderByStartDateAsc();

    /**
     * Relations
     */
    List<MarketingCampaign> findByCoupon(Coupon coupon);

    /**
     * Top performers
     */
    @Query(value = "SELECT mc.* FROM marketing_campaigns mc ORDER BY mc.actual_clicks DESC LIMIT :limit", nativeQuery = true)
    List<MarketingCampaign> findTopByActualClicksDescLimit(@Param("limit") int limit);
}