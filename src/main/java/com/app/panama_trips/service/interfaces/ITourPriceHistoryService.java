package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.TourPriceHistoryRequest;
import com.app.panama_trips.presentation.dto.TourPriceHistoryResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITourPriceHistoryService {

    // CRUD operations
    Page<TourPriceHistoryResponse> getAllTourPriceHistories(Pageable pageable);
    TourPriceHistoryResponse getTourPriceHistoryById(Integer id);
    TourPriceHistoryResponse saveTourPriceHistory(TourPriceHistoryRequest request);
    TourPriceHistoryResponse updateTourPriceHistory(Integer id, TourPriceHistoryRequest request);
    void deleteTourPriceHistory(Integer id);

    // Find operations by entity relationships
    List<TourPriceHistoryResponse> findByTourPlanId(Integer tourPlanId);
    Page<TourPriceHistoryResponse> findByTourPlanIdOrderByChangedAtDesc(Integer tourPlanId, Pageable pageable);
    List<TourPriceHistoryResponse> findByTourPlanIdAndChangedAtBetween(Integer tourPlanId, LocalDateTime startDate,LocalDateTime endDate);
    List<TourPriceHistoryResponse> findByChangedById(Long userId);

    // Specialized queries from repository
    Double calculateAveragePriceChangePercentageByTourPlanId(Integer tourPlanId);
    List<TourPriceHistoryResponse> findByNewPriceGreaterThan(BigDecimal price);
    Long countPriceChangesByTourPlanId(Integer tourPlanId);

    // Business logic operations
    List<TourPriceHistoryResponse> getRecentChanges(int limit);
    Optional<TourPriceHistoryResponse> getLatestChangeForTourPlan(Integer tourPlanId);
    BigDecimal getCurrentPriceForTourPlan(Integer tourPlanId);
    BigDecimal getPreviousPriceForTourPlan(Integer tourPlanId);
    BigDecimal getMaxPriceForTourPlan(Integer tourPlanId);
    BigDecimal getMinPriceForTourPlan(Integer tourPlanId);
    BigDecimal getAveragePriceForTourPlan(Integer tourPlanId);
    List<TourPriceHistoryResponse> getPriceChangesOnDate(Integer tourPlanId, LocalDate date);
    List<TourPriceHistoryResponse> getPriceChangesByUserAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
