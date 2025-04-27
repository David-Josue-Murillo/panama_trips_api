package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.TourPlanImageRequest;
import com.app.panama_trips.presentation.dto.TourPlanImageResponse;

import java.util.List;

public interface ITourPlanImageService {
    // CRUD operations
    TourPlanImageResponse getTourPlanImageById(Integer id);
    TourPlanImageResponse saveTourPlanImage(TourPlanImageRequest tourPlanImageRequest);
    TourPlanImageResponse updateTourPlanImage(Integer id, TourPlanImageRequest tourPlanImageRequest);
    void deleteTourPlanImage(Integer id);

    // Additional service methods
    List<TourPlanImageResponse> getTourPlanImagesByTourPlanId(Integer tourPlanId);
    List<TourPlanImageResponse> getMainTourPlanImagesByTourPlanId(Integer tourPlanId);
}
