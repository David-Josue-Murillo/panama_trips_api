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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MarketingCampaignService implements IMarketingCampaignService {

    private final MarketingCampaignRepository repository;
    private final TourPlanRepository tourPlanRepository;
    private final UserEntityRepository userEntityRepository;

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

        // Check name uniqueness if changed
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
        UserEntity user = userEntityRepository.findById(createdById.longValue())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + createdById));
        return repository.findByCreatedBy(user).stream()
                .map(MarketingCampaignResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketingCampaignResponse> findByStatus(CampaignStatus status) {
        return repository.findAll().stream()
                .filter(c -> c.getStatus() == status)
                .map(MarketingCampaignResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketingCampaignResponse> findByType(CampaignType type) {
        return repository.findAll().stream()
                .filter(c -> c.getType() == type)
                .map(MarketingCampaignResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketingCampaignResponse> findByStartDateAfter(LocalDateTime date) {
        return repository.findAll().stream()
                .filter(c -> c.getStartDate().isAfter(date))
                .map(MarketingCampaignResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketingCampaignResponse> findByEndDateBefore(LocalDateTime date) {
        return repository.findAll().stream()
                .filter(c -> c.getEndDate().isBefore(date))
                .map(MarketingCampaignResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketingCampaignResponse> findActiveCampaigns() {
        return repository.findActiveMarketingCampaigns().stream()
                .map(MarketingCampaignResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketingCampaignResponse> findUpcomingCampaigns() {
        LocalDateTime now = LocalDateTime.now();
        return repository.findAll().stream()
                .filter(c -> c.getStartDate().isAfter(now))
                .sorted((a, b) -> a.getStartDate().compareTo(b.getStartDate()))
                .map(MarketingCampaignResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketingCampaignResponse> findExpiredCampaigns() {
        LocalDateTime now = LocalDateTime.now();
        return repository.findAll().stream()
                .filter(c -> c.getEndDate().isBefore(now))
                .sorted((a, b) -> b.getEndDate().compareTo(a.getEndDate()))
                .map(MarketingCampaignResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal sumBudgetByStatus(CampaignStatus status) {
        return repository.findAll().stream()
                .filter(c -> c.getStatus() == status)
                .map(MarketingCampaign::getBudget)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
        return repository.findAll().stream()
                .filter(c -> !c.getStartDate().isBefore(startDate) && !c.getEndDate().isAfter(endDate))
                .map(MarketingCampaignResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketingCampaignResponse> getCampaignsByCreatedByAndStatus(Integer createdById, CampaignStatus status) {
        UserEntity user = userEntityRepository.findById(createdById.longValue())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + createdById));
        return repository.findByCreatedBy(user).stream()
                .filter(c -> c.getStatus() == status)
                .map(MarketingCampaignResponse::new)
                .collect(Collectors.toList());
    }

    // ==================== Advanced Queries ====================

    @Override
    public List<MarketingCampaignResponse> getRecentCampaigns(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        return repository.findAll(pageable).stream()
                .map(MarketingCampaignResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketingCampaignResponse> getCampaignsByBudgetRange(BigDecimal minBudget, BigDecimal maxBudget) {
        return repository.findAll().stream()
                .filter(c -> c.getBudget().compareTo(minBudget) >= 0 && c.getBudget().compareTo(maxBudget) <= 0)
                .map(MarketingCampaignResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketingCampaignResponse> getCampaignsByTargetAudience(String targetAudience) {
        return repository.findActiveByTargetAudience(targetAudience).stream()
                .map(MarketingCampaignResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketingCampaignResponse> getTopCampaignsByClicks(int limit) {
        return repository.findTopByActualClicksDescLimit(limit).stream()
                .map(MarketingCampaignResponse::new)
                .collect(Collectors.toList());
    }

    // ==================== Bulk Operations ====================

    @Override
    @Transactional
    public void bulkCreateCampaigns(List<MarketingCampaignRequest> requests) {
        List<MarketingCampaign> campaigns = requests.stream()
                .peek(this::validateCampaignRequest)
                .map(this::buildFromRequest)
                .collect(Collectors.toList());
        repository.saveAll(campaigns);
    }

    @Override
    @Transactional
    public void bulkUpdateCampaigns(List<MarketingCampaignRequest> requests) {
        throw new UnsupportedOperationException(
                "bulkUpdateCampaigns requires campaign IDs. Use individual updateMarketingCampaign instead.");
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
        List<MarketingCampaign> campaigns = campaignIds.stream()
                .map(this::findCampaignOrThrow)
                .peek(c -> {
                    if (!isValidStatusTransition(c.getStatus(), newStatus)) {
                        throw new IllegalStateException(
                                "Invalid status transition from " + c.getStatus() + " to " + newStatus +
                                " for campaign id: " + c.getId());
                    }
                    c.setStatus(newStatus);
                })
                .collect(Collectors.toList());
        repository.saveAll(campaigns);
    }

    @Override
    @Transactional
    public void bulkIncrementClicks(List<Integer> campaignIds) {
        List<MarketingCampaign> campaigns = campaignIds.stream()
                .map(this::findCampaignOrThrow)
                .peek(c -> c.setActualClicks(c.getActualClicks() + 1))
                .collect(Collectors.toList());
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
        return user != null && !repository.findByCreatedBy(user).isEmpty();
    }

    @Override
    public long countByCreatedById(Integer createdById) {
        UserEntity user = userEntityRepository.findById(createdById.longValue()).orElse(null);
        return user != null ? repository.findByCreatedBy(user).size() : 0;
    }

    @Override
    public long countByStatus(CampaignStatus status) {
        return repository.findAll().stream()
                .filter(c -> c.getStatus() == status)
                .count();
    }

    @Override
    public long countByStartDateAfter(LocalDateTime date) {
        return repository.findAll().stream()
                .filter(c -> c.getStartDate().isAfter(date))
                .count();
    }

    // ==================== Financial Operations ====================

    @Override
    public BigDecimal calculateTotalBudget() {
        return repository.findAll().stream()
                .map(MarketingCampaign::getBudget)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calculateTotalBudgetByStatus(CampaignStatus status) {
        return sumBudgetByStatus(status);
    }

    @Override
    public BigDecimal calculateRemainingBudgetByStatus(CampaignStatus status) {
        // Assuming remaining = total budget - (some spent calculation based on clicks ratio)
        return repository.findAll().stream()
                .filter(c -> c.getStatus() == status)
                .map(c -> {
                    if (c.getTargetClicks() == null || c.getTargetClicks() == 0) {
                        return c.getBudget();
                    }
                    double clickRatio = (double) c.getActualClicks() / c.getTargetClicks();
                    BigDecimal spent = c.getBudget().multiply(BigDecimal.valueOf(Math.min(clickRatio, 1.0)));
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
        return countByStatus(CampaignStatus.ACTIVE);
    }

    @Override
    public long getTotalCompletedCampaigns() {
        return countByStatus(CampaignStatus.COMPLETED);
    }

    @Override
    public BigDecimal getTotalBudgetSpent() {
        return repository.findAll().stream()
                .filter(c -> c.getStatus() == CampaignStatus.COMPLETED || c.getStatus() == CampaignStatus.ACTIVE)
                .map(c -> {
                    if (c.getTargetClicks() == null || c.getTargetClicks() == 0) {
                        return c.getStatus() == CampaignStatus.COMPLETED ? c.getBudget() : BigDecimal.ZERO;
                    }
                    double clickRatio = (double) c.getActualClicks() / c.getTargetClicks();
                    return c.getBudget().multiply(BigDecimal.valueOf(Math.min(clickRatio, 1.0)));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public double getCampaignSuccessRate() {
        long total = getTotalCampaigns();
        if (total == 0) return 0.0;

        long successful = repository.findAll().stream()
                .filter(c -> c.getStatus() == CampaignStatus.COMPLETED)
                .filter(c -> c.getTargetClicks() != null && c.getTargetClicks() > 0)
                .filter(c -> c.getActualClicks() >= c.getTargetClicks())
                .count();

        return (double) successful / total * 100;
    }

    @Override
    public List<MarketingCampaignResponse> getTopCampaignsByMonth(int limit) {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        return repository.findAll().stream()
                .filter(c -> c.getCreatedAt() != null && c.getCreatedAt().isAfter(startOfMonth))
                .sorted((a, b) -> Long.compare(b.getActualClicks(), a.getActualClicks()))
                .limit(limit)
                .map(MarketingCampaignResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketingCampaignResponse> getCampaignsByMonth() {
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        return repository.findAll().stream()
                .filter(c -> c.getCreatedAt() != null && c.getCreatedAt().isAfter(startOfMonth))
                .map(MarketingCampaignResponse::new)
                .collect(Collectors.toList());
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
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateClicksForActiveCampaigns() {
        // This method could be used by a scheduler to update clicks from external sources
        // For now, it's a no-op placeholder
    }

    // ==================== Utility Operations ====================

    @Override
    @Transactional
    public void recalculateCampaignStatus() {
        LocalDateTime now = LocalDateTime.now();
        List<MarketingCampaign> campaigns = repository.findAll();

        campaigns.forEach(campaign -> {
            if (campaign.getStatus() == CampaignStatus.ACTIVE && campaign.getEndDate().isBefore(now)) {
                campaign.setStatus(CampaignStatus.COMPLETED);
            } else if (campaign.getStatus() == CampaignStatus.DRAFT &&
                       campaign.getStartDate().isBefore(now) &&
                       campaign.getEndDate().isAfter(now)) {
                // Optionally auto-activate drafts that are within date range
                // campaign.setStatus(CampaignStatus.ACTIVE);
            }
        });

        repository.saveAll(campaigns);
    }

    @Override
    @Transactional
    public void cleanupExpiredCampaigns(int daysToKeep) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
        List<MarketingCampaign> expiredCampaigns = repository.findAll().stream()
                .filter(c -> c.getStatus() == CampaignStatus.COMPLETED || c.getStatus() == CampaignStatus.CANCELLED)
                .filter(c -> c.getEndDate().isBefore(cutoffDate))
                .collect(Collectors.toList());

        repository.deleteAll(expiredCampaigns);
    }

    @Override
    public List<MarketingCampaignResponse> searchCampaignsByBudget(BigDecimal budget) {
        BigDecimal tolerance = budget.multiply(BigDecimal.valueOf(0.1)); // 10% tolerance
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
        BigDecimal avgBudget = calculateAverageBudget();
        BigDecimal threshold = avgBudget.multiply(BigDecimal.valueOf(1.5)); // 50% above average

        return repository.findAll().stream()
                .filter(c -> c.getBudget().compareTo(threshold) > 0)
                .map(MarketingCampaignResponse::new)
                .collect(Collectors.toList());
    }

    // ==================== Private Helper Methods ====================

    private MarketingCampaign findCampaignOrThrow(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marketing campaign not found with id: " + id));
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

    private BigDecimal calculateAverageBudget() {
        List<MarketingCampaign> campaigns = repository.findAll();
        if (campaigns.isEmpty()) return BigDecimal.ZERO;

        BigDecimal total = campaigns.stream()
                .map(MarketingCampaign::getBudget)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(BigDecimal.valueOf(campaigns.size()), 2, RoundingMode.HALF_UP);
    }

    private UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userEntityRepository.findUserEntitiesByEmail(username).orElse(null);
    }

    private MarketingCampaign buildFromRequest(MarketingCampaignRequest request) {
        UserEntity createdBy = getCurrentUser();

        List<TourPlan> tours = (request.tourIds() != null && !request.tourIds().isEmpty())
                ? request.tourIds().stream()
                        .map(tourPlanRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList()
                : List.of();

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
                .tours(tours)
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

        List<TourPlan> tours = (request.tourIds() != null && !request.tourIds().isEmpty())
                ? request.tourIds().stream()
                        .map(tourPlanRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList()
                : List.of();
        existing.setTours(tours);
    }
}
