package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.TourPlanImageRequest;
import com.app.panama_trips.presentation.dto.TourPlanImageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITourPlanImageService {
    // CRUD operations
    Page<TourPlanImageResponse> getAllTourPlanImages(Pageable pageable);
    TourPlanImageResponse getTourPlanImageById(Integer id);
    TourPlanImageResponse saveTourPlanImage(TourPlanImageRequest tourPlanImageRequest);
    TourPlanImageResponse updateTourPlanImage(Integer id, TourPlanImageRequest tourPlanImageRequest);
    void deleteTourPlanImage(Integer id);

    // Additional service methods
    List<TourPlanImageResponse> getTourPlanImagesByTourPlanId(Integer tourPlanId);
    List<TourPlanImageResponse> getTourPlanImagesByTourPlanIdOrderByDisplayOrder(Integer tourPlanId);
    TourPlanImageResponse getMainImageByTourPlanId(Integer tourPlanId);
    List<TourPlanImageResponse> getNonMainImagesByTourPlanIdOrdered(Integer tourPlanId);
    Integer getMaxDisplayOrderForTourPlan(Integer tourPlanId);
    Long countImagesByTourPlanId(Integer tourPlanId);
    void deleteAllImagesByTourPlanId(Integer tourPlanId);
    boolean existsImageWithUrlForTourPlan(Integer tourPlanId, String imageUrl);
}
