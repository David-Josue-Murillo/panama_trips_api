package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Provider;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.repository.ProviderRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.presentation.dto.TourPlanRequest;
import com.app.panama_trips.presentation.dto.TourPlanResponse;
import com.app.panama_trips.service.interfaces.ITourPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TourPlanService implements ITourPlanService {

    private final TourPlanRepository tourPlanRepository;
    private final ProviderRepository providerRepository;

    @Override
    public Page<TourPlanResponse> getAllTourPlan(Pageable pageable) {
        return this.tourPlanRepository.findAll(pageable)
                .map(TourPlanResponse::new);
    }

    @Override
    public TourPlanResponse getTourPlanById(Integer id) {
        return this.tourPlanRepository.findById(id)
                .map(TourPlanResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("Tour Plan with id " + id + " not found"));
    }

    @Override
    public TourPlanResponse saveTourPlan(TourPlanRequest tourPlanRequest) {
        validateProvider(tourPlanRequest);
        TourPlan newTourPlan = builderTourPlanFromRequest(tourPlanRequest);
        return new TourPlanResponse(this.tourPlanRepository.save(newTourPlan));
    }

    @Override
    public TourPlanResponse updateTourPlan(Integer id, TourPlanRequest tourPlanRequest) {
        TourPlan tourPlan = this.tourPlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tour Plan with id " + id + " not found"));
        updateTourPlanEntity(tourPlan, tourPlanRequest);
        return new TourPlanResponse(this.tourPlanRepository.save(tourPlan));
    }

    @Override
    public void deleteTourPlan(Integer id) {
        if(!this.tourPlanRepository.existsById(id)){
            throw new ResourceNotFoundException("Tour Plan with id " + id + " not found");
        }

        this.tourPlanRepository.deleteById(id);
    }

    @Override
    public TourPlanResponse getTourPlanByTitle(String title) {
        return this.tourPlanRepository.findByTitleIgnoreCase(title)
                .map(TourPlanResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("Tour Plan with title " + title + " not found"));
    }

    @Override
    public List<TourPlanResponse> getTourPlanByPrice(BigDecimal price) {
        return this.tourPlanRepository.findByPrice(price)
                .stream()
                .map(TourPlanResponse::new)
                .toList();
    }

    @Override
    public Page<TourPlanResponse> getTourPlanByPriceBetween(BigDecimal priceAfter, BigDecimal priceBefore, Pageable pageable) {
        return this.tourPlanRepository.findByPriceBetween(priceAfter, priceBefore, pageable)
                .map(TourPlanResponse::new);
    }

    @Override
    public List<TourPlanResponse> getTourPlanByDuration(Integer duration) {
        return this.tourPlanRepository.findByDuration(duration)
                .stream()
                .map(TourPlanResponse::new)
                .toList();
    }

    @Override
    public Page<TourPlanResponse> getTourPlanByDurationBetween(Integer durationAfter, Integer durationBefore, Pageable pageable) {
        return this.tourPlanRepository.findByDurationBetween(durationAfter, durationBefore, pageable)
                .map(TourPlanResponse::new);
    }

    @Override
    public List<TourPlanResponse> getTourPlanByAvailableSpots(Integer availableSpots) {
        return this.tourPlanRepository.findByAvailableSpots(availableSpots)
                .stream()
                .map(TourPlanResponse::new)
                .toList();
    }

    @Override
    public Page<TourPlanResponse> getTourPlanByAvailableSpotsBetween(Integer availableSpotsAfter, Integer availableSpotsBefore, Pageable pageable) {
        return this.tourPlanRepository.findByAvailableSpotsBetween(availableSpotsAfter, availableSpotsBefore, pageable)
                .map(TourPlanResponse::new);
    }

    @Override
    public List<TourPlanResponse> getTourPlanByProviderId(Integer providerId) {
        return this.tourPlanRepository.findByProvider_Id(providerId)
                .stream()
                .map(TourPlanResponse::new)
                .toList();
    }

    @Override
    public List<TourPlanResponse> getTourPlanByTitleAndPrice(String title, BigDecimal price) {
        return this.tourPlanRepository.findByTitleContainingIgnoreCaseAndPrice(title, price)
                .stream()
                .map(TourPlanResponse::new)
                .toList();
    }

    @Override
    public Page<TourPlanResponse> getTourPlanByTitleAndPriceBetween(String title, BigDecimal priceAfter, BigDecimal priceBefore, Pageable pageable) {
        return this.tourPlanRepository.findByTitleContainingIgnoreCaseAndPriceBetween(title, priceAfter, priceBefore, pageable)
                .map(TourPlanResponse::new);
    }

    @Override
    public Page<TourPlanResponse> getTourPlanByTitleAndPriceBetweenAndDurationBetween(String title, BigDecimal priceAfter, BigDecimal priceBefore, Integer durationAfter, Integer durationBefore, Pageable pageable) {
        return this.tourPlanRepository.findByTitleContainingIgnoreCaseAndPriceBetweenAndDurationBetween(title, priceAfter, priceBefore, durationAfter, durationBefore, pageable)
                .map(TourPlanResponse::new);
    }

    @Override
    public List<TourPlanResponse> getTop10TourPlanByTitleContaining(String keyword, Pageable pageable) {
        return this.tourPlanRepository.findTop10ByTitleContainingIgnoreCaseOrderByTitleAsc(keyword, pageable)
                .stream()
                .map(TourPlanResponse::new)
                .toList();
    }

    @Override
    public boolean existsTourPlanByTitle(String title) {
        return this.tourPlanRepository.existsByTitleIgnoreCase(title);
    }

    @Override
    public long countTourPlan() {
        return this.tourPlanRepository.count();
    }

    // Methods private
    private void validateProvider(TourPlanRequest tourPlanRequest) {
        if(this.tourPlanRepository.existsByTitleIgnoreCase(tourPlanRequest.title())) {
            throw new IllegalArgumentException("Tour Plan with title " + tourPlanRequest.title() + " already exists");
        }

        if(!this.providerRepository.existsById(tourPlanRequest.providerId())) {
            throw new ResourceNotFoundException("Provider with id " + tourPlanRequest.providerId() + " not found");
        }
    }

    private TourPlan builderTourPlanFromRequest(TourPlanRequest tourPlanRequest) {
        return TourPlan.builder()
                .title(tourPlanRequest.title())
                .description(tourPlanRequest.description())
                .price(tourPlanRequest.price())
                .duration(tourPlanRequest.duration())
                .availableSpots(tourPlanRequest.availableSpots())
                .provider(findProviderOrFail(tourPlanRequest.providerId()))
                .build();
    }

    private Provider findProviderOrFail(Integer providerId) {
        return this.providerRepository.findById(providerId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider with id " + providerId + " not found"));
    }

    private void updateTourPlanEntity(TourPlan tourPlan, TourPlanRequest tourPlanRequest) {
        tourPlan.setTitle(tourPlanRequest.title());
        tourPlan.setDescription(tourPlanRequest.description());
        tourPlan.setPrice(tourPlanRequest.price());
        tourPlan.setDuration(tourPlanRequest.duration());
        tourPlan.setAvailableSpots(tourPlanRequest.availableSpots());
        tourPlan.setProvider(findProviderOrFail(tourPlanRequest.providerId()));
    }
}
