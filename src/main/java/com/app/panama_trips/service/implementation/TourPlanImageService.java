package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourPlanImage;
import com.app.panama_trips.persistence.repository.TourPlanImageRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
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
    private final TourPlanRepository tourPlanRepository;

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
        validateTourPlanImage(tourPlanImageRequest);
        TourPlanImage tourPlanImage = buildTourPlanImageFromRequest(tourPlanImageRequest);
        return new TourPlanImageResponse(this.tourPlanImageRepository.save(tourPlanImage));
    }

    @Override
    public TourPlanImageResponse updateTourPlanImage(Integer id, TourPlanImageRequest tourPlanImageRequest) {
        TourPlanImage existingImage = this.tourPlanImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TourPlanImage with " + id + " not found"));

        updateTourPlanImageFields(existingImage, tourPlanImageRequest);
        return new TourPlanImageResponse(this.tourPlanImageRepository.save(existingImage));
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

    // Private methods
    private void validateTourPlanImage(TourPlanImageRequest request) {
        // Validate required fields
        if (request == null) {
            throw new IllegalArgumentException("TourPlanImage request cannot be null");
        }

        // Check if image URL already exists for the tour plan
        if (existsImageWithUrlForTourPlan(request.getTourPlanId(), request.getImageUrl())) {
            throw new IllegalArgumentException("An image with this URL already exists for this tour plan");
        }
    }

    private TourPlanImage buildTourPlanImageFromRequest(TourPlanImageRequest request) {
        return TourPlanImage.builder()
                .tourPlan(findTourPlanOrFail(request.getTourPlanId()))
                .imageUrl(request.getImageUrl())
                .altText(request.getAltText())
                .isMain(request.getIsMain())
                .displayOrder(request.getDisplayOrder())
                .build();
    }

    private TourPlan findTourPlanOrFail(Integer tourPlanId) {
        return this.tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("TourPlan with id " + tourPlanId + " not found"));
    }

    private void updateTourPlanImageFields(TourPlanImage existingImage, TourPlanImageRequest request) {
        existingImage.setAltText(request.getAltText());
        existingImage.setImageUrl(request.getImageUrl());
        existingImage.setIsMain(request.getIsMain());
        existingImage.setDisplayOrder(request.getDisplayOrder());
        existingImage.setTourPlan(findTourPlanOrFail(request.getTourPlanId()));
        if (request.getDisplayOrder() == null) {
            existingImage.setDisplayOrder(getMaxDisplayOrderForTourPlan(request.getTourPlanId()) + 1);
        }
    }
}
