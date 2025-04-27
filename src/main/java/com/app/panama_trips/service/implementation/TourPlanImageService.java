package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.repository.TourPlanImageRepository;
import com.app.panama_trips.presentation.dto.TourPlanImageRequest;
import com.app.panama_trips.presentation.dto.TourPlanImageResponse;
import com.app.panama_trips.service.interfaces.ITourPlanImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TourPlanImageService implements ITourPlanImageService {

    private final TourPlanImageRepository tourPlanImageRepository;

    @Override
    public Page<TourPlanImageResponse> getAllTourPlanImages(Pageable pageable) {
        return this.tourPlanImageRepository.findAll(pageable)
                .map(TourPlanImageResponse::new);
    }

    @Override
    public TourPlanImageResponse getTourPlanImageById(Integer id) {
        return this.tourPlanImageRepository.findById(id)
                .map(TourPlanImageResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("TourPlanImage with " + id + " not found"));
    }

    @Override
    public TourPlanImageResponse saveTourPlanImage(TourPlanImageRequest tourPlanImageRequest) {
        return null;
    }

    @Override
    public TourPlanImageResponse updateTourPlanImage(Integer id, TourPlanImageRequest tourPlanImageRequest) {
        return null;
    }

    @Override
    public void deleteTourPlanImage(Integer id) {

    }

    @Override
    public List<TourPlanImageResponse> getTourPlanImagesByTourPlanId(Integer tourPlanId) {
        return List.of();
    }

    @Override
    public List<TourPlanImageResponse> getTourPlanImagesByTourPlanIdOrderByDisplayOrder(Integer tourPlanId) {
        return List.of();
    }

    @Override
    public TourPlanImageResponse getMainImageByTourPlanId(Integer tourPlanId) {
        return null;
    }

    @Override
    public List<TourPlanImageResponse> getNonMainImagesByTourPlanIdOrdered(Integer tourPlanId) {
        return List.of();
    }

    @Override
    public Integer getMaxDisplayOrderForTourPlan(Integer tourPlanId) {
        return 0;
    }

    @Override
    public Long countImagesByTourPlanId(Integer tourPlanId) {
        return 0L;
    }

    @Override
    public void deleteAllImagesByTourPlanId(Integer tourPlanId) {

    }

    @Override
    public boolean existsImageWithUrlForTourPlan(Integer tourPlanId, String imageUrl) {
        return false;
    }

    @Override
    public void reorderTourPlanImages(Integer tourPlanId, List<Integer> imageIds) {

    }

    @Override
    public void setMainImage(Integer tourPlanId, Integer imageId) {

    }
}
