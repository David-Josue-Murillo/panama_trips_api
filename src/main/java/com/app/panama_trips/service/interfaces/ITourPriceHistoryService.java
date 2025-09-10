package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.TourPriceHistoryRequest;
import com.app.panama_trips.presentation.dto.TourPriceHistoryResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
}
