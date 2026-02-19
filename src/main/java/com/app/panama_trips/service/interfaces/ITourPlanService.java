package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.TourPlanRequest;
import com.app.panama_trips.presentation.dto.TourPlanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ITourPlanService {
    // CRUD operations
    Page<TourPlanResponse> getAllTourPlan(Pageable pageable);
    TourPlanResponse getTourPlanById(Integer id);
    TourPlanResponse saveTourPlan(TourPlanRequest tourPlanRequest);
    TourPlanResponse updateTourPlan(Integer id, TourPlanRequest tourPlanRequest);
    void deleteTourPlan(Integer id);

    // Additional service methods
    TourPlanResponse getTourPlanByTitle(String title);
    List<TourPlanResponse> getTourPlanByPrice(BigDecimal price);
    Page<TourPlanResponse> getTourPlanByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    List<TourPlanResponse> getTourPlanByDuration(Integer duration);
    Page<TourPlanResponse> getTourPlanByDurationBetween(Integer minDuration, Integer maxDuration, Pageable pageable);
    List<TourPlanResponse> getTourPlanByAvailableSpots(Integer availableSpots);
    Page<TourPlanResponse> getTourPlanByAvailableSpotsBetween(Integer minSpots, Integer maxSpots, Pageable pageable);
    List<TourPlanResponse> getTourPlanByProviderId(Integer providerId);
    List<TourPlanResponse> getTourPlanByTitleAndPrice(String title, BigDecimal price);
    Page<TourPlanResponse> getTourPlanByTitleAndPriceBetween(String title, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    Page<TourPlanResponse> getTourPlanByTitleAndPriceBetweenAndDurationBetween(String title, BigDecimal minPrice, BigDecimal maxPrice, Integer minDuration, Integer maxDuration, Pageable pageable);
    List<TourPlanResponse> getTop10TourPlanByTitleContaining(String keyword, Pageable pageable);
    boolean existsTourPlanByTitle(String title);
    long countTourPlan();
}
