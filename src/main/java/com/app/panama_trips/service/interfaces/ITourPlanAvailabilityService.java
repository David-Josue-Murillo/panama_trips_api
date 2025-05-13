package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.TourPlanAvailabilityRequest;
import com.app.panama_trips.presentation.dto.TourPlanAvailabilityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ITourPlanAvailabilityService {
    // CRUD operations
    Page<TourPlanAvailabilityResponse> getAllTourPlanAvailabilities(Pageable pageable);
    TourPlanAvailabilityResponse getTourPlanAvailabilityById(Integer id);
    TourPlanAvailabilityResponse saveTourPlanAvailability(TourPlanAvailabilityRequest request);
    TourPlanAvailabilityResponse updateTourPlanAvailability(Integer id, TourPlanAvailabilityRequest request);
    void deleteTourPlanAvailability(Integer id);

    // Find operations
    List<TourPlanAvailabilityResponse> getTourPlanAvailabilitiesByTourPlanId(Integer tourPlanId);
    List<TourPlanAvailabilityResponse> getAvailableDatesByTourPlanId(Integer tourPlanId);
    List<TourPlanAvailabilityResponse> getAvailabilitiesByDateRange(LocalDate startDate, LocalDate endDate);
    List<TourPlanAvailabilityResponse> getAvailabilitiesByTourPlanIdAndDateRange(Integer tourPlanId, LocalDate startDate, LocalDate endDate);
    TourPlanAvailabilityResponse getAvailabilityByTourPlanIdAndDate(Integer tourPlanId, LocalDate date);

    // Specialized queries
    List<TourPlanAvailabilityResponse> getUpcomingAvailableDatesByTourPlanId(Integer tourPlanId);
    List<TourPlanAvailabilityResponse> getAvailableDatesWithSufficientSpots(Integer tourPlanId, Integer requiredSpots);
    Long countUpcomingAvailableDatesByTourPlanId(Integer tourPlanId);
    List<TourPlanAvailabilityResponse> getAvailabilitiesWithPriceOverride(Integer tourPlanId);
    List<TourPlanAvailabilityResponse> getAvailabilitiesWithPriceAbove(Integer tourPlanId, BigDecimal price);

    // Bulk operations
    void deleteAllAvailabilitiesByTourPlanId(Integer tourPlanId);

    // Check operations
    boolean existsAvailabilityForTourPlanAndDate(Integer tourPlanId, LocalDate date);
}
