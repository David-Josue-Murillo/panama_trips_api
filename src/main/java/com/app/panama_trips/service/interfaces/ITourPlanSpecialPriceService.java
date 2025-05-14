package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourPlanSpecialPrice;
import com.app.panama_trips.presentation.dto.TourPlanSpecialPriceRequest;
import com.app.panama_trips.presentation.dto.TourPlanSpecialPriceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ITourPlanSpecialPriceService {
    // CRUD operations
    Page<TourPlanSpecialPriceResponse> getAll(Pageable pageable);
    TourPlanSpecialPriceResponse findById(Integer id);
    TourPlanSpecialPriceResponse save(TourPlanSpecialPriceRequest request);
    TourPlanSpecialPriceResponse update(Integer id, TourPlanSpecialPrice tourPlanSpecialPrice);
    void deleteById(Integer id);

    // Find operations
    List<TourPlanSpecialPriceResponse> findByTourPlan(TourPlan tourPlan);
    List<TourPlanSpecialPriceResponse> findByTourPlanOrderByStartDateAsc(TourPlan tourPlan);
    List<TourPlanSpecialPriceResponse> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
    List<TourPlanSpecialPriceResponse> findOverlappingPricePeriodsForTourPlan(Integer tourPlanId, LocalDate startDate, LocalDate endDate);
    List<TourPlanSpecialPriceResponse> findByTourPlanAndPriceGreaterThan(TourPlan tourPlan, BigDecimal price);
    List<TourPlanSpecialPriceResponse> findByTourPlanAndStartDateGreaterThanEqual(TourPlan tourPlan, LocalDate date);

    // Specialized queries
    List<TourPlanSpecialPriceResponse> findByEndDateBetween(LocalDate startDate, LocalDate endDate);
    Optional<TourPlanSpecialPriceResponse> findByTourPlanIdAndDate(Integer tourPlanId, LocalDate date);

    // Bulk operations
    void deleteByTourPlan(TourPlan tourPlan);

    // Check operations
    boolean existsByTourPlanAndStartDateAndEndDate(TourPlan tourPlan, LocalDate startDate, LocalDate endDate);
}