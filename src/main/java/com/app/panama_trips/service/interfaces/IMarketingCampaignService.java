package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.enums.CampaignStatus;
import com.app.panama_trips.persistence.entity.enums.CampaignType;
import com.app.panama_trips.presentation.dto.MarketingCampaignRequest;
import com.app.panama_trips.presentation.dto.MarketingCampaignResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IMarketingCampaignService {
    // CRUD operations
    Page<MarketingCampaignResponse> getAllMarketingCampaigns(Pageable pageable);
    MarketingCampaignResponse getMarketingCampaignById(Integer id);
    MarketingCampaignResponse saveMarketingCampaign(MarketingCampaignRequest request);
    MarketingCampaignResponse updateMarketingCampaign(Integer id, MarketingCampaignRequest request);
    void deleteMarketingCampaign(Integer id);

    // Find operations by entity relationships
    List<MarketingCampaignResponse> findByCreatedById(Integer createdById);
    List<MarketingCampaignResponse> findByStatus(CampaignStatus status);
    List<MarketingCampaignResponse> findByType(CampaignType type);
    List<MarketingCampaignResponse> findByStartDateAfter(LocalDateTime date);
    List<MarketingCampaignResponse> findByEndDateBefore(LocalDateTime date);
    List<MarketingCampaignResponse> findActiveCampaigns();

    // Specialized queries from repository
    List<MarketingCampaignResponse> findUpcomingCampaigns();
    List<MarketingCampaignResponse> findExpiredCampaigns();
    BigDecimal sumBudgetByStatus(CampaignStatus status);
    Long countActiveCampaigns();

    // Business logic operations
    List<MarketingCampaignResponse> getActiveCampaigns();
    List<MarketingCampaignResponse> getDraftCampaigns();
    List<MarketingCampaignResponse> getPausedCampaigns();
    List<MarketingCampaignResponse> getCompletedCampaigns();
    List<MarketingCampaignResponse> getCampaignsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<MarketingCampaignResponse> getCampaignsByCreatedByAndStatus(Integer createdById, CampaignStatus status);

    // Advanced queries
    List<MarketingCampaignResponse> getRecentCampaigns(int limit);
    List<MarketingCampaignResponse> getCampaignsByBudgetRange(BigDecimal minBudget, BigDecimal maxBudget);
    List<MarketingCampaignResponse> getCampaignsByTargetAudience(String targetAudience);
    List<MarketingCampaignResponse> getTopCampaignsByClicks(int limit);

    // Bulk operations
    void bulkCreateCampaigns(List<MarketingCampaignRequest> requests);
    void bulkDeleteCampaigns(List<Integer> campaignIds);
    void bulkUpdateStatus(List<Integer> campaignIds, CampaignStatus newStatus);
    void bulkIncrementClicks(List<Integer> campaignIds);

    // Check operations
    boolean existsById(Integer id);
    boolean existsByName(String name);
    boolean existsByCreatedById(Integer createdById);
    long countByCreatedById(Integer createdById);
    long countByStatus(CampaignStatus status);
    long countByStartDateAfter(LocalDateTime date);

    // Financial operations
    BigDecimal calculateTotalBudget();
    BigDecimal calculateTotalBudgetByStatus(CampaignStatus status);
    BigDecimal calculateRemainingBudgetByStatus(CampaignStatus status);

    // Statistics and analytics
    long getTotalCampaigns();
    long getTotalActiveCampaigns();
    long getTotalCompletedCampaigns();
    BigDecimal getTotalBudgetSpent();
    double getCampaignSuccessRate();
    List<MarketingCampaignResponse> getTopCampaignsByMonth(int limit);
    List<MarketingCampaignResponse> getCampaignsByMonth();

    // Status management operations
    MarketingCampaignResponse activateCampaign(Integer campaignId);
    MarketingCampaignResponse pauseCampaign(Integer campaignId);
    MarketingCampaignResponse completeCampaign(Integer campaignId);
    MarketingCampaignResponse cancelCampaign(Integer campaignId);
    boolean isValidStatusTransition(Integer campaignId, CampaignStatus newStatus);
    List<CampaignStatus> getValidStatusTransitions(Integer campaignId);

    // Clicks operations (adapted from reminders)
    MarketingCampaignResponse incrementClicks(Integer campaignId);
    List<MarketingCampaignResponse> getCampaignsNeedingClicksUpdate();
    void updateClicksForActiveCampaigns();

    // Utility operations
    void recalculateCampaignStatus();
    void cleanupExpiredCampaigns(int daysToKeep);
    List<MarketingCampaignResponse> searchCampaignsByBudget(BigDecimal budget);
    Optional<MarketingCampaignResponse> findLatestCampaignByCreatedBy(Integer createdById);
    List<MarketingCampaignResponse> getHighBudgetCampaigns();
}