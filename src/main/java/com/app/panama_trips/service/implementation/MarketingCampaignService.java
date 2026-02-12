package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.MarketingCampaign;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.entity.enums.CampaignStatus;
import com.app.panama_trips.persistence.entity.enums.CampaignType;
import com.app.panama_trips.persistence.repository.MarketingCampaignRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.presentation.dto.MarketingCampaignRequest;
import com.app.panama_trips.presentation.dto.MarketingCampaignResponse;
import com.app.panama_trips.service.interfaces.IMarketingCampaignService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MarketingCampaignService implements IMarketingCampaignService {

    private final MarketingCampaignRepository repository;
    private final TourPlanRepository tourPlanRepository;
    private final UserEntityRepository userEntityRepository;

    // Constants for magic numbers
    private static final BigDecimal BUDGET_SEARCH_TOLERANCE = BigDecimal.valueOf(0.1);
    private static final BigDecimal HIGH_BUDGET_MULTIPLIER = BigDecimal.valueOf(1.5);
    private static final double MAX_CLICK_RATIO = 1.0;

    // Valid status transitions map
    private static final Map<CampaignStatus, Set<CampaignStatus>> VALID_TRANSITIONS = Map.of(
            CampaignStatus.DRAFT, Set.of(CampaignStatus.ACTIVE, CampaignStatus.CANCELLED),
            CampaignStatus.ACTIVE, Set.of(CampaignStatus.PAUSED, CampaignStatus.COMPLETED, CampaignStatus.CANCELLED),
            CampaignStatus.PAUSED, Set.of(CampaignStatus.ACTIVE, CampaignStatus.CANCELLED),
            CampaignStatus.COMPLETED, Set.of(),
            CampaignStatus.CANCELLED, Set.of()
    );

    // ==================== CRUD Operations ====================

    @Override
    public Page<MarketingCampaignResponse> getAllMarketingCampaigns(Pageable pageable) {
        return repository.findAll(pageable).map(MarketingCampaignResponse::new);
    }

    @Override
    public MarketingCampaignResponse getMarketingCampaignById(Integer id) {
        return repository.findById(id)
                .map(MarketingCampaignResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("Marketing campaign not found with id: " + id));
    }

    @Override
    @Transactional
    public MarketingCampaignResponse saveMarketingCampaign(MarketingCampaignRequest request) {
        validateCampaignRequest(request);
        if (repository.existsByNameIgnoreCase(request.name())) {
            throw new IllegalArgumentException("Campaign with name '" + request.name() + "' already exists");
        }
        MarketingCampaign campaign = buildFromRequest(request);
        return new MarketingCampaignResponse(repository.save(campaign));
    }

    @Override
    @Transactional
    public MarketingCampaignResponse updateMarketingCampaign(Integer id, MarketingCampaignRequest request) {
        MarketingCampaign existing = findCampaignOrThrow(id);
        validateCampaignRequest(request);

        if (!existing.getName().equalsIgnoreCase(request.name()) &&
            repository.existsByNameIgnoreCase(request.name())) {
            throw new IllegalArgumentException("Campaign with name '" + request.name() + "' already exists");
        }

        updateFromRequest(existing, request);
        return new MarketingCampaignResponse(repository.save(existing));
    }

    @Override
    @Transactional
    public void deleteMarketingCampaign(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Marketing campaign not found with id: " + id);
        }
        repository.deleteById(id);
    }

    // ==================== Find Operations ====================

    @Override
    public List<MarketingCampaignResponse> findByCreatedById(Integer createdById) {
        UserEntity user = findUserOrThrow(createdById);
        return toResponseList(repository.findByCreatedBy(user));
    }

    @Override
    public List<MarketingCampaignResponse> findByStatus(CampaignStatus status) {
        return toResponseList(repository.findByStatus(status));
    }

    @Override
    public List<MarketingCampaignResponse> findByType(CampaignType type) {
        return toResponseList(repository.findByType(type));
    }

    @Override
    public List<MarketingCampaignResponse> findByStartDateAfter(LocalDateTime date) {
        return toResponseList(repository.findByStartDateAfter(date));
    }

    @Override
    public List<MarketingCampaignResponse> findByEndDateBefore(LocalDateTime date) {
        return toResponseList(repository.findByEndDateBefore(date));
    }

    @Override
    public List<MarketingCampaignResponse> findActiveCampaigns() {
        return toResponseList(repository.findActiveMarketingCampaigns());
    }

    @Override
    public List<MarketingCampaignResponse> findUpcomingCampaigns() {
        return toResponseList(repository.findByStartDateAfterOrderByStartDateAsc(LocalDateTime.now()));
    }

    @Override
    public List<MarketingCampaignResponse> findExpiredCampaigns() {
        return toResponseList(repository.findByEndDateBeforeOrderByEndDateDesc(LocalDateTime.now()));
    }

    @Override
    public BigDecimal sumBudgetByStatus(CampaignStatus status) {
        return repository.sumBudgetByStatus(status);
    }

    @Override
    public Long countActiveCampaigns() {
        return repository.findActiveMarketingCampaigns().stream().count();
    }

    // ==================== Business Logic Operations ====================

    @Override
    public List<MarketingCampaignResponse> getActiveCampaigns() {
        return findByStatus(CampaignStatus.ACTIVE);
    }

    @Override
    public List<MarketingCampaignResponse> getDraftCampaigns() {
        return findByStatus(CampaignStatus.DRAFT);
    }

    @Override
    public List<MarketingCampaignResponse> getPausedCampaigns() {
        return findByStatus(CampaignStatus.PAUSED);
    }

    @Override
    public List<MarketingCampaignResponse> getCompletedCampaigns() {
        return findByStatus(CampaignStatus.COMPLETED);
    }

    @Override
    public List<MarketingCampaignResponse> getCampaignsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return toResponseList(repository.findByDateRange(startDate, endDate));
    }

    @Override
    public List<MarketingCampaignResponse> getCampaignsByCreatedByAndStatus(Integer createdById, CampaignStatus status) {
        UserEntity user = findUserOrThrow(createdById);
        return toResponseList(repository.findByCreatedByAndStatus(user, status));
    }

    // ==================== Advanced Queries ====================

    @Override
    public List<MarketingCampaignResponse> getRecentCampaigns(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return toResponseList(repository.findAll(pageable).getContent());
    }

    @Override
    public List<MarketingCampaignResponse> getCampaignsByBudgetRange(BigDecimal minBudget, BigDecimal maxBudget) {
        return toResponseList(repository.findByBudgetBetween(minBudget, maxBudget));
    }

    @Override
    public List<MarketingCampaignResponse> getCampaignsByTargetAudience(String targetAudience) {
        return toResponseList(repository.findActiveByTargetAudience(targetAudience));
    }

    @Override
    public List<MarketingCampaignResponse> getTopCampaignsByClicks(int limit) {
        return toResponseList(repository.findTopByActualClicksDescLimit(limit));
    }

    // ==================== Bulk Operations ====================

    @Override
    @Transactional
    public void bulkCreateCampaigns(List<MarketingCampaignRequest> requests) {
        requests.forEach(this::validateCampaignRequest);
        List<MarketingCampaign> campaigns = requests.stream()
                .map(this::buildFromRequest)
                .toList();
        repository.saveAll(campaigns);
    }

    @Override
    @Transactional
    public void bulkDeleteCampaigns(List<Integer> campaignIds) {
        campaignIds.forEach(id -> {
            if (!repository.existsById(id)) {
                throw new ResourceNotFoundException("Marketing campaign not found with id: " + id);
            }
        });
        repository.deleteAllById(campaignIds);
    }

    @Override
    @Transactional
    public void bulkUpdateStatus(List<Integer> campaignIds, CampaignStatus newStatus) {
        List<MarketingCampaign> campaigns = repository.findAllById(campaignIds);
        campaigns.forEach(c -> {
            if (!isValidStatusTransition(c.getStatus(), newStatus)) {
                throw new IllegalStateException(
                        "Invalid status transition from " + c.getStatus() + " to " + newStatus +
                        " for campaign id: " + c.getId());
            }
            c.setStatus(newStatus);
        });
        repository.saveAll(campaigns);
    }

    @Override
    @Transactional
    public void bulkIncrementClicks(List<Integer> campaignIds) {
        List<MarketingCampaign> campaigns = repository.findAllById(campaignIds);
        campaigns.forEach(c -> c.setActualClicks(c.getActualClicks() + 1));
        repository.saveAll(campaigns);
    }

    // ==================== Check Operations ====================

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsByCreatedById(Integer createdById) {
        UserEntity user = userEntityRepository.findById(createdById.longValue()).orElse(null);
        return user != null && repository.existsByCreatedBy(user);
    }

    @Override
    public long countByCreatedById(Integer createdById) {
        UserEntity user = userEntityRepository.findById(createdById.longValue()).orElse(null);
        return user != null ? repository.countByCreatedBy(user) : 0;
    }

    @Override
    public long countByStatus(CampaignStatus status) {
        return repository.countByStatus(status);
    }

    @Override
    public long countByStartDateAfter(LocalDateTime date) {
        return repository.countByStartDateAfter(date);
    }

    // ==================== Financial Operations ====================

    @Override
    public BigDecimal calculateTotalBudget() {
        return repository.sumTotalBudget();
    }

    @Override
    public BigDecimal calculateTotalBudgetByStatus(CampaignStatus status) {
        return repository.sumBudgetByStatus(status);
    }

    @Override
    public BigDecimal calculateRemainingBudgetByStatus(CampaignStatus status) {
        return repository.findByStatus(status).stream()
                .map(c -> {
                    if (c.getTargetClicks() == null || c.getTargetClicks() == 0) {
                        return c.getBudget();
                    }
                    double clickRatio = (double) c.getActualClicks() / c.getTargetClicks();
                    BigDecimal spent = c.getBudget().multiply(BigDecimal.valueOf(Math.min(clickRatio, MAX_CLICK_RATIO)));
                    return c.getBudget().subtract(spent);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ==================== Statistics and Analytics ====================

    @Override
    public long getTotalCampaigns() {
        return repository.count();
    }

    @Override
    public long getTotalActiveCampaigns() {
        return repository.countByStatus(CampaignStatus.ACTIVE);
    }

    @Override
    public long getTotalCompletedCampaigns() {
        return repository.countByStatus(CampaignStatus.COMPLETED);
    }

    @Override
    public BigDecimal getTotalBudgetSpent() {
        List<MarketingCampaign> campaigns = repository.findByStatusIn(
                List.of(CampaignStatus.COMPLETED, CampaignStatus.ACTIVE));
        return campaigns.stream()
                .map(c -> {
                    if (c.getTargetClicks() == null || c.getTargetClicks() == 0) {
                        return c.getStatus() == CampaignStatus.COMPLETED ? c.getBudget() : BigDecimal.ZERO;
                    }
                    double clickRatio = (double) c.getActualClicks() / c.getTargetClicks();
                    return c.getBudget().multiply(BigDecimal.valueOf(Math.min(clickRatio, MAX_CLICK_RATIO)));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public double getCampaignSuccessRate() {
        long total = getTotalCampaigns();
        if (total == 0) return 0.0;

        long successful = repository.findByStatus(CampaignStatus.COMPLETED).stream()
                .filter(c -> c.getTargetClicks() != null && c.getTargetClicks() > 0)
                .filter(c -> c.getActualClicks() >= c.getTargetClicks())
                .count();

        return (double) successful / total * 100;
    }

    @Override
    public List<MarketingCampaignResponse> getTopCampaignsByMonth(int limit) {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        return toResponseList(repository.findByCreatedAtAfterOrderByActualClicksDesc(
                startOfMonth, PageRequest.of(0, limit)));
    }

    @Override
    public List<MarketingCampaignResponse> getCampaignsByMonth() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        return toResponseList(repository.findByCreatedAtAfter(startOfMonth));
    }

    // ==================== Status Management Operations ====================

    @Override
    @Transactional
    public MarketingCampaignResponse activateCampaign(Integer campaignId) {
        return updateCampaignStatus(campaignId, CampaignStatus.ACTIVE);
    }

    @Override
    @Transactional
    public MarketingCampaignResponse pauseCampaign(Integer campaignId) {
        return updateCampaignStatus(campaignId, CampaignStatus.PAUSED);
    }

    @Override
    @Transactional
    public MarketingCampaignResponse completeCampaign(Integer campaignId) {
        return updateCampaignStatus(campaignId, CampaignStatus.COMPLETED);
    }

    @Override
    @Transactional
    public MarketingCampaignResponse cancelCampaign(Integer campaignId) {
        return updateCampaignStatus(campaignId, CampaignStatus.CANCELLED);
    }

    @Override
    public boolean isValidStatusTransition(Integer campaignId, CampaignStatus newStatus) {
        MarketingCampaign campaign = findCampaignOrThrow(campaignId);
        return isValidStatusTransition(campaign.getStatus(), newStatus);
    }

    @Override
    public List<CampaignStatus> getValidStatusTransitions(Integer campaignId) {
        MarketingCampaign campaign = findCampaignOrThrow(campaignId);
        Set<CampaignStatus> validTransitions = VALID_TRANSITIONS.get(campaign.getStatus());
        return validTransitions != null ? List.copyOf(validTransitions) : List.of();
    }

    // ==================== Clicks Operations ====================

    @Override
    @Transactional
    public MarketingCampaignResponse incrementClicks(Integer campaignId) {
        MarketingCampaign campaign = findCampaignOrThrow(campaignId);
        campaign.setActualClicks(campaign.getActualClicks() + 1);
        return new MarketingCampaignResponse(repository.save(campaign));
    }

    @Override
    public List<MarketingCampaignResponse> getCampaignsNeedingClicksUpdate() {
        return repository.findActiveMarketingCampaigns().stream()
                .filter(c -> c.getTargetClicks() != null && c.getTargetClicks() > 0)
                .filter(c -> c.getActualClicks() < c.getTargetClicks())
                .map(MarketingCampaignResponse::new)
                .toList();
    }

    @Override
    @Transactional
    public void updateClicksForActiveCampaigns() {
        log.info("updateClicksForActiveCampaigns: not yet implemented");
    }

    // ==================== Utility Operations ====================

    @Override
    @Transactional
    public void recalculateCampaignStatus() {
        LocalDateTime now = LocalDateTime.now();
        List<MarketingCampaign> expiredActive = repository.findByStatusAndEndDateBefore(CampaignStatus.ACTIVE, now);
        expiredActive.forEach(campaign -> campaign.setStatus(CampaignStatus.COMPLETED));
        repository.saveAll(expiredActive);
    }

    @Override
    @Transactional
    public void cleanupExpiredCampaigns(int daysToKeep) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        List<MarketingCampaign> expiredCampaigns = repository.findByStatusInAndEndDateBefore(
                List.of(CampaignStatus.COMPLETED, CampaignStatus.CANCELLED), cutoffDate);
        repository.deleteAll(expiredCampaigns);
    }

    @Override
    public List<MarketingCampaignResponse> searchCampaignsByBudget(BigDecimal budget) {
        BigDecimal tolerance = budget.multiply(BUDGET_SEARCH_TOLERANCE);
        BigDecimal minBudget = budget.subtract(tolerance);
        BigDecimal maxBudget = budget.add(tolerance);
        return getCampaignsByBudgetRange(minBudget, maxBudget);
    }

    @Override
    public Optional<MarketingCampaignResponse> findLatestCampaignByCreatedBy(Integer createdById) {
        UserEntity user = userEntityRepository.findById(createdById.longValue()).orElse(null);
        if (user == null) return Optional.empty();

        return repository.findByCreatedBy(user).stream()
                .max((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()))
                .map(MarketingCampaignResponse::new);
    }

    @Override
    public List<MarketingCampaignResponse> getHighBudgetCampaigns() {
        BigDecimal avgBudget = repository.avgBudget();
        BigDecimal threshold = avgBudget.multiply(HIGH_BUDGET_MULTIPLIER);
        return toResponseList(repository.findByBudgetBetween(threshold, BigDecimal.valueOf(Long.MAX_VALUE)));
    }

    // ==================== Private Helper Methods ====================

    private MarketingCampaign findCampaignOrThrow(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marketing campaign not found with id: " + id));
    }

    private UserEntity findUserOrThrow(Integer userId) {
        return userEntityRepository.findById(userId.longValue())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    private void validateCampaignRequest(MarketingCampaignRequest request) {
        if (request.endDate().isBefore(request.startDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }

    private boolean isValidStatusTransition(CampaignStatus currentStatus, CampaignStatus newStatus) {
        Set<CampaignStatus> validTransitions = VALID_TRANSITIONS.get(currentStatus);
        return validTransitions != null && validTransitions.contains(newStatus);
    }

    private MarketingCampaignResponse updateCampaignStatus(Integer campaignId, CampaignStatus newStatus) {
        MarketingCampaign campaign = findCampaignOrThrow(campaignId);

        if (!isValidStatusTransition(campaign.getStatus(), newStatus)) {
            throw new IllegalStateException(
                    "Invalid status transition from " + campaign.getStatus() + " to " + newStatus);
        }

        campaign.setStatus(newStatus);
        return new MarketingCampaignResponse(repository.save(campaign));
    }

    private List<MarketingCampaignResponse> toResponseList(List<MarketingCampaign> campaigns) {
        return campaigns.stream().map(MarketingCampaignResponse::new).toList();
    }

    private List<TourPlan> resolveTours(List<Integer> tourIds) {
        if (tourIds == null || tourIds.isEmpty()) return List.of();
        return tourIds.stream()
                .map(tourPlanRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userEntityRepository.findUserEntitiesByEmail(username).orElse(null);
    }

    private MarketingCampaign buildFromRequest(MarketingCampaignRequest request) {
        UserEntity createdBy = getCurrentUser();
        return MarketingCampaign.builder()
                .name(request.name())
                .description(request.description())
                .targetAudience(request.targetAudience())
                .type(request.type())
                .status(request.status())
                .budget(request.budget())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .targetClicks(request.targetClicks() != null ? request.targetClicks() : 0L)
                .tours(resolveTours(request.tourIds()))
                .createdBy(createdBy)
                .build();
    }

    private void updateFromRequest(MarketingCampaign existing, MarketingCampaignRequest request) {
        existing.setName(request.name());
        existing.setDescription(request.description());
        existing.setTargetAudience(request.targetAudience());
        existing.setType(request.type());
        existing.setStatus(request.status());
        existing.setBudget(request.budget());
        existing.setStartDate(request.startDate());
        existing.setEndDate(request.endDate());
        existing.setTargetClicks(request.targetClicks() != null ? request.targetClicks() : 0L);
        existing.setTours(resolveTours(request.tourIds()));
    }
}
