package com.app.panama_trips.service.implementation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourPriceHistory;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.persistence.repository.TourPriceHistoryRepository;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.presentation.dto.TourPriceHistoryRequest;
import com.app.panama_trips.presentation.dto.TourPriceHistoryResponse;
import com.app.panama_trips.service.interfaces.ITourPriceHistoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TourPriceHistoryService implements ITourPriceHistoryService {

    private final TourPriceHistoryRepository repository;
    private final TourPlanRepository tourPlanRepository;
    private final UserEntityRepository userRepository;

    // CRUD operations

    @Override
    public Page<TourPriceHistoryResponse> getAllTourPriceHistories(Pageable pageable) {
        return repository.findAll(pageable).map(TourPriceHistoryResponse::new);
    }

    @Override
    public TourPriceHistoryResponse getTourPriceHistoryById(Integer id) {
        return new TourPriceHistoryResponse(findPriceHistoryOrThrow(id));
    }

    @Override
    @Transactional
    public TourPriceHistoryResponse saveTourPriceHistory(TourPriceHistoryRequest request) {
        return new TourPriceHistoryResponse(repository.save(buildFromRequest(request)));
    }

    @Override
    @Transactional
    public TourPriceHistoryResponse updateTourPriceHistory(Integer id, TourPriceHistoryRequest request) {
        TourPriceHistory existing = findPriceHistoryOrThrow(id);
        updateFromRequest(existing, request);
        return new TourPriceHistoryResponse(repository.save(existing));
    }

    @Override
    @Transactional
    public void deleteTourPriceHistory(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Tour price history not found with id " + id);
        }
        repository.deleteById(id);
    }

    // Find operations by entity relationships

    @Override
    public List<TourPriceHistoryResponse> findByTourPlanId(Integer tourPlanId) {
        return toResponseList(repository.findByTourPlanIdOrderByChangedAtDesc(tourPlanId));
    }

    @Override
    public Page<TourPriceHistoryResponse> findByTourPlanIdOrderByChangedAtDesc(Integer tourPlanId, Pageable pageable) {
        validateTourPlanExists(tourPlanId);
        return repository.findByTourPlan_IdOrderByChangedAtDesc(tourPlanId, pageable)
                .map(TourPriceHistoryResponse::new);
    }

    @Override
    public List<TourPriceHistoryResponse> findByTourPlanIdAndChangedAtBetween(Integer tourPlanId,
            LocalDateTime startDate, LocalDateTime endDate) {
        validateTourPlanExists(tourPlanId);
        return toResponseList(repository.findByTourPlan_IdAndChangedAtBetween(tourPlanId, startDate, endDate));
    }

    @Override
    public List<TourPriceHistoryResponse> findByChangedById(Long userId) {
        validateUserExists(userId);
        return toResponseList(repository.findByChangedBy_Id(userId));
    }

    // Specialized queries

    @Override
    public Double calculateAveragePriceChangePercentageByTourPlanId(Integer tourPlanId) {
        return repository.calculateAveragePriceChangePercentageByTourPlanId(tourPlanId);
    }

    @Override
    public List<TourPriceHistoryResponse> findByNewPriceGreaterThan(BigDecimal price) {
        return toResponseList(repository.findByNewPriceGreaterThan(price));
    }

    @Override
    public Long countPriceChangesByTourPlanId(Integer tourPlanId) {
        return repository.countPriceChangesByTourPlanId(tourPlanId);
    }

    // Business logic operations

    @Override
    public List<TourPriceHistoryResponse> getRecentChanges(int limit) {
        return repository.findAllByOrderByChangedAtDesc(PageRequest.of(0, limit))
                .map(TourPriceHistoryResponse::new)
                .getContent();
    }

    @Override
    public Optional<TourPriceHistoryResponse> getLatestChangeForTourPlan(Integer tourPlanId) {
        return repository.findFirstByTourPlan_IdOrderByChangedAtDesc(tourPlanId)
                .map(TourPriceHistoryResponse::new);
    }

    @Override
    public BigDecimal getCurrentPriceForTourPlan(Integer tourPlanId) {
        return repository.findFirstByTourPlan_IdOrderByChangedAtDesc(tourPlanId)
                .map(TourPriceHistory::getNewPrice)
                .orElseGet(() -> {
                    TourPlan tourPlan = findTourPlanOrThrow(tourPlanId);
                    return tourPlan.getPricing() != null ? tourPlan.getPricing().getPrice() : null;
                });
    }

    @Override
    public BigDecimal getPreviousPriceForTourPlan(Integer tourPlanId) {
        return repository.findFirstByTourPlan_IdOrderByChangedAtDesc(tourPlanId)
                .map(TourPriceHistory::getPreviousPrice)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No price history found for tour plan with id " + tourPlanId));
    }

    @Override
    public BigDecimal getMaxPriceForTourPlan(Integer tourPlanId) {
        BigDecimal max = repository.findMaxNewPriceByTourPlanId(tourPlanId);
        if (max == null) {
            throw new ResourceNotFoundException("No price history found for tour plan with id " + tourPlanId);
        }
        return max;
    }

    @Override
    public BigDecimal getMinPriceForTourPlan(Integer tourPlanId) {
        BigDecimal min = repository.findMinNewPriceByTourPlanId(tourPlanId);
        if (min == null) {
            throw new ResourceNotFoundException("No price history found for tour plan with id " + tourPlanId);
        }
        return min;
    }

    @Override
    public BigDecimal getAveragePriceForTourPlan(Integer tourPlanId) {
        Double avg = repository.findAvgNewPriceByTourPlanId(tourPlanId);
        if (avg == null) {
            throw new ResourceNotFoundException("No price history found for tour plan with id " + tourPlanId);
        }
        return BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public List<TourPriceHistoryResponse> getPriceChangesOnDate(Integer tourPlanId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return toResponseList(repository.findByTourPlan_IdAndChangedAtBetween(tourPlanId, startOfDay, endOfDay));
    }

    @Override
    public List<TourPriceHistoryResponse> getPriceChangesByUserAndDateRange(Long userId, LocalDateTime startDate,
            LocalDateTime endDate) {
        validateUserExists(userId);
        return toResponseList(repository.findByChangedBy_IdAndChangedAtBetween(userId, startDate, endDate));
    }

    // Bulk operations

    @Override
    @Transactional
    public void bulkCreateTourPriceHistories(List<TourPriceHistoryRequest> requests) {
        List<TourPriceHistory> entities = requests.stream()
                .map(this::buildFromRequest)
                .toList();
        repository.saveAll(entities);
    }

    @Override
    @Transactional
    public void bulkDeleteTourPriceHistories(List<Integer> tourPriceHistoryIds) {
        repository.deleteAllById(tourPriceHistoryIds);
    }

    // Check operations

    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public long countByTourPlanId(Integer tourPlanId) {
        return repository.countPriceChangesByTourPlanId(tourPlanId);
    }

    // Analytics and statistics

    @Override
    public BigDecimal getTotalPriceIncreaseForTourPlan(Integer tourPlanId) {
        return repository.sumPriceIncreaseByTourPlanId(tourPlanId);
    }

    @Override
    public BigDecimal getTotalPriceDecreaseForTourPlan(Integer tourPlanId) {
        return repository.sumPriceDecreaseByTourPlanId(tourPlanId);
    }

    @Override
    public double getAverageChangePercentageForTourPlan(Integer tourPlanId) {
        Double value = repository.calculateAveragePriceChangePercentageByTourPlanId(tourPlanId);
        return value != null ? value : 0.0;
    }

    @Override
    public List<TourPriceHistoryResponse> getTopTourPlansByChangeCount(int limit) {
        List<Integer> topTourPlanIds = repository.findTopTourPlanIdsByChangeCount(PageRequest.of(0, limit));
        return topTourPlanIds.stream()
                .map(tourPlanId -> repository.findFirstByTourPlan_IdOrderByChangedAtDesc(tourPlanId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(TourPriceHistoryResponse::new)
                .toList();
    }

    // Utility operations

    @Override
    public List<TourPriceHistoryResponse> searchChangesByPrice(BigDecimal price) {
        return toResponseList(repository.findByNewPriceOrPreviousPrice(price, price));
    }

    // Private helpers

    private TourPriceHistory findPriceHistoryOrThrow(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tour price history not found with id " + id));
    }

    private TourPlan findTourPlanOrThrow(Integer tourPlanId) {
        return tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour plan not found with id " + tourPlanId));
    }

    private void validateTourPlanExists(Integer tourPlanId) {
        if (!tourPlanRepository.existsById(tourPlanId)) {
            throw new ResourceNotFoundException("Tour plan not found with id " + tourPlanId);
        }
    }

    private void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
    }

    private List<TourPriceHistoryResponse> toResponseList(List<TourPriceHistory> entities) {
        return entities.stream()
                .map(TourPriceHistoryResponse::new)
                .toList();
    }

    private TourPriceHistory buildFromRequest(TourPriceHistoryRequest request) {
        TourPlan tourPlan = findTourPlanOrThrow(request.tourPlanId());

        UserEntity changedBy = null;
        if (request.changedById() != null) {
            changedBy = userRepository.findById(request.changedById())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("User not found with id " + request.changedById()));
        }

        return TourPriceHistory.builder()
                .tourPlan(tourPlan)
                .previousPrice(request.previousPrice())
                .newPrice(request.newPrice())
                .changedBy(changedBy)
                .reason(request.reason())
                .build();
    }

    private void updateFromRequest(TourPriceHistory existing, TourPriceHistoryRequest request) {
        TourPlan tourPlan = findTourPlanOrThrow(request.tourPlanId());

        UserEntity changedBy = null;
        if (request.changedById() != null) {
            changedBy = userRepository.findById(request.changedById())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("User not found with id " + request.changedById()));
        }

        existing.setTourPlan(tourPlan);
        existing.setPreviousPrice(request.previousPrice());
        existing.setNewPrice(request.newPrice());
        existing.setChangedBy(changedBy);
        existing.setReason(request.reason());
    }
}
