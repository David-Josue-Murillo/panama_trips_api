package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Coupon;
import com.app.panama_trips.persistence.entity.MarketingCampaign;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.entity.enums.CampaignStatus;
import com.app.panama_trips.persistence.entity.enums.CampaignType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MarketingCampaignRepository extends JpaRepository<MarketingCampaign, Integer> {

    // Existence and basic finds
    boolean existsByNameIgnoreCase(String name);
    Optional<MarketingCampaign> findByNameIgnoreCase(String name);
    Optional<MarketingCampaign> findByName(String name);

    // By creator
    List<MarketingCampaign> findByCreatedBy(UserEntity user);
    Page<MarketingCampaign> findByCreatedByOrderByCreatedAtDesc(UserEntity user, Pageable pageable);
    List<MarketingCampaign> findByCreatedByAndStatus(UserEntity user, CampaignStatus status);
    boolean existsByCreatedBy(UserEntity user);
    long countByCreatedBy(UserEntity user);

    // Search
    @Query("SELECT mc FROM MarketingCampaign mc WHERE " +
           "LOWER(TRIM(mc.name)) LIKE LOWER(CONCAT('%', TRIM(:keyword), '%')) OR " +
           "LOWER(TRIM(mc.description)) LIKE LOWER(CONCAT('%', TRIM(:keyword), '%'))")
    Page<MarketingCampaign> searchByNameOrDescription(@Param("keyword") String keyword, Pageable pageable);

    // Active campaigns (status ACTIVE and within dates)
    @Query("SELECT mc FROM MarketingCampaign mc WHERE " +
           "mc.status = 'ACTIVE' AND " +
           "CURRENT_TIMESTAMP BETWEEN mc.startDate AND mc.endDate")
    List<MarketingCampaign> findActiveMarketingCampaigns();

    @Query("SELECT mc FROM MarketingCampaign mc WHERE " +
           "mc.status = 'ACTIVE' AND " +
           "CURRENT_TIMESTAMP BETWEEN mc.startDate AND mc.endDate")
    Page<MarketingCampaign> findActiveMarketingCampaigns(Pageable pageable);

    // By target audience (active)
    @Query("SELECT mc FROM MarketingCampaign mc WHERE " +
           "mc.targetAudience = :audience AND " +
           "mc.status = 'ACTIVE' AND " +
           "CURRENT_TIMESTAMP BETWEEN mc.startDate AND mc.endDate")
    List<MarketingCampaign> findActiveByTargetAudience(@Param("audience") String audience);

    // Relations
    List<MarketingCampaign> findByCoupon(Coupon coupon);

    // Top performers
    @Query(value = "SELECT mc.* FROM marketing_campaigns mc ORDER BY mc.actual_clicks DESC LIMIT :limit", nativeQuery = true)
    List<MarketingCampaign> findTopByActualClicksDescLimit(@Param("limit") int limit);

    // Derived queries for filtering
    List<MarketingCampaign> findByStatus(CampaignStatus status);
    List<MarketingCampaign> findByType(CampaignType type);
    List<MarketingCampaign> findByStartDateAfter(LocalDateTime date);
    List<MarketingCampaign> findByEndDateBefore(LocalDateTime date);
    List<MarketingCampaign> findByStartDateAfterOrderByStartDateAsc(LocalDateTime date);
    List<MarketingCampaign> findByEndDateBeforeOrderByEndDateDesc(LocalDateTime date);
    List<MarketingCampaign> findByBudgetBetween(BigDecimal minBudget, BigDecimal maxBudget);
    List<MarketingCampaign> findByStatusInAndEndDateBefore(List<CampaignStatus> statuses, LocalDateTime date);
    List<MarketingCampaign> findByStatusAndEndDateBefore(CampaignStatus status, LocalDateTime date);
    List<MarketingCampaign> findByStatusIn(List<CampaignStatus> statuses);
    List<MarketingCampaign> findByCreatedAtAfter(LocalDateTime date);

    // Derived queries for counting
    long countByStatus(CampaignStatus status);
    long countByStartDateAfter(LocalDateTime date);

    // Aggregation queries
    @Query("SELECT COALESCE(SUM(mc.budget), 0) FROM MarketingCampaign mc WHERE mc.status = :status")
    BigDecimal sumBudgetByStatus(@Param("status") CampaignStatus status);

    @Query("SELECT COALESCE(SUM(mc.budget), 0) FROM MarketingCampaign mc")
    BigDecimal sumTotalBudget();

    @Query("SELECT COALESCE(AVG(mc.budget), 0) FROM MarketingCampaign mc")
    BigDecimal avgBudget();

    // Date range query
    @Query("SELECT mc FROM MarketingCampaign mc WHERE mc.startDate >= :startDate AND mc.endDate <= :endDate")
    List<MarketingCampaign> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Top campaigns by month (by clicks, within date range)
    @Query("SELECT mc FROM MarketingCampaign mc WHERE mc.createdAt >= :since ORDER BY mc.actualClicks DESC")
    List<MarketingCampaign> findByCreatedAtAfterOrderByActualClicksDesc(@Param("since") LocalDateTime since, Pageable pageable);
}
