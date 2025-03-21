package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.TourPlanRequest;
import com.app.panama_trips.presentation.dto.TourPlanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITourPlan {
    // CRUD operations
    Page<TourPlanResponse> getAllTourPlan(Pageable pageable);
    TourPlanResponse getTourPlanById(Integer id);
    TourPlanResponse saveTourPlan(TourPlanRequest tourPlanRequest);
    TourPlanResponse updateTourPlan(Integer id, TourPlanRequest tourPlanRequest);
    void deleteTourPlan(Integer id);

    // Additional service methods
}
