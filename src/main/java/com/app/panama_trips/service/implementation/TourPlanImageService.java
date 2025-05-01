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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TourPlanImageService implements ITourPlanImageService {

    private final TourPlanImageRepository tourPlanImageRepository;
    private final TourPlanRepository tourPlanRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<TourPlanImageResponse> getAllTourPlanImages(Pageable pageable) {
        return this.tourPlanImageRepository.findAll(pageable)
                .map(TourPlanImageResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public TourPlanImageResponse getTourPlanImageById(Integer id) {
        return this.tourPlanImageRepository.findById(id)
                .map(TourPlanImageResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("TourPlanImage with " + id + " not found"));
    }

    @Override
    @Transactional
    public TourPlanImageResponse saveTourPlanImage(TourPlanImageRequest tourPlanImageRequest) {
        validateTourPlanImage(tourPlanImageRequest);
        TourPlanImage tourPlanImage = buildTourPlanImageFromRequest(tourPlanImageRequest);
        return new TourPlanImageResponse(this.tourPlanImageRepository.save(tourPlanImage));
    }

    @Override
    @Transactional
    public TourPlanImageResponse updateTourPlanImage(Integer id, TourPlanImageRequest tourPlanImageRequest) {
        TourPlanImage existingImage = this.tourPlanImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TourPlanImage with " + id + " not found"));

        updateTourPlanImageFields(existingImage, tourPlanImageRequest);
        return new TourPlanImageResponse(this.tourPlanImageRepository.save(existingImage));
    }

    @Override
    @Transactional
    public void deleteTourPlanImage(Integer id) {
        if(!this.tourPlanImageRepository.existsById(id)) {
            throw new ResourceNotFoundException("TourPlanImage with " + id + " not found");
        }
        this.tourPlanImageRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPlanImageResponse> getTourPlanImagesByTourPlanId(Integer tourPlanId) {
        TourPlan tourPlanExists = this.tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("TourPlan with id " + tourPlanId + " not found"));
        return this.tourPlanImageRepository.findByTourPlan(tourPlanExists)
                .stream()
                .map(TourPlanImageResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPlanImageResponse> getTourPlanImagesByTourPlanIdOrderByDisplayOrder(Integer tourPlanId) {
        return this.tourPlanImageRepository.findByTourPlanIdOrderByDisplayOrder(tourPlanId)
                .stream()
                .map(TourPlanImageResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TourPlanImageResponse getMainImageByTourPlanId(Integer tourPlanId) {
        TourPlan tourPlan = this.tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("TourPlan with id " + tourPlanId + " not found"));
        Optional<TourPlanImage> mainImage = this.tourPlanImageRepository.findByTourPlanAndIsMainTrue(tourPlan);
        return mainImage.map(TourPlanImageResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("Main image not found for TourPlan with id " + tourPlanId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPlanImageResponse> getNonMainImagesByTourPlanIdOrdered(Integer tourPlanId) {
        TourPlan tourPlan = this.tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("TourPlan with id " + tourPlanId + " not found"));
        return this.tourPlanImageRepository.findByTourPlanAndIsMainFalseOrderByDisplayOrderAsc(tourPlan)
                .stream()
                .map(TourPlanImageResponse::new)
                .toList();
    }

    @Override
    public Integer getMaxDisplayOrderForTourPlan(Integer tourPlanId) {
        return this.tourPlanImageRepository.findMaxDisplayOrderByTourPlanId(tourPlanId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countImagesByTourPlanId(Integer tourPlanId) {
        return this.tourPlanImageRepository.countByTourPlanId(tourPlanId);
    }

    @Override
    @Transactional
    public void deleteAllImagesByTourPlanId(Integer tourPlanId) {
        TourPlan tourPlan = this.tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("TourPlan with id " + tourPlanId + " not found"));
        this.tourPlanImageRepository.deleteByTourPlan(tourPlan);
    }

    @Override
    public boolean existsImageWithUrlForTourPlan(Integer tourPlanId, String imageUrl) {
        TourPlan tourPlan = this.tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("TourPlan with id " + tourPlanId + " not found"));
        return this.tourPlanImageRepository.existsByTourPlanAndImageUrl(tourPlan, imageUrl);
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
