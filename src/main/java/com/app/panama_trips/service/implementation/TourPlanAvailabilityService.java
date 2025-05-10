package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourPlanAvailability;
import com.app.panama_trips.persistence.repository.TourPlanAvailabilityRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.presentation.dto.TourPlanAvailabilityRequest;
import com.app.panama_trips.presentation.dto.TourPlanAvailabilityResponse;
import com.app.panama_trips.service.interfaces.ITourPlanAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TourPlanAvailabilityService implements ITourPlanAvailabilityService {

    private final TourPlanAvailabilityRepository availabilityRepository;
    private final TourPlanRepository tourPlanRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<TourPlanAvailabilityResponse> getAllTourPlanAvailabilities(Pageable pageable) {
        return null;
    }

    @Override
    public TourPlanAvailabilityResponse getTourPlanAvailabilityById(Integer id) {
        return null;
    }

    @Override
    public TourPlanAvailabilityResponse saveTourPlanAvailability(TourPlanAvailabilityRequest request) {
        return null;
    }

    @Override
    public TourPlanAvailabilityResponse updateTourPlanAvailability(Integer id, TourPlanAvailabilityRequest request) {
        return null;
    }

    @Override
    public void deleteTourPlanAvailability(Integer id) {

    }

    @Override
    public List<TourPlanAvailabilityResponse> getTourPlanAvailabilitiesByTourPlanId(Integer tourPlanId) {
        return List.of();
    }

    @Override
    public List<TourPlanAvailabilityResponse> getAvailableDatesByTourPlanId(Integer tourPlanId) {
        return List.of();
    }

    @Override
    public List<TourPlanAvailabilityResponse> getAvailabilitiesByDateRange(LocalDate startDate, LocalDate endDate) {
        return List.of();
    }

    @Override
    public List<TourPlanAvailabilityResponse> getAvailabilitiesByTourPlanIdAndDateRange(Integer tourPlanId, LocalDate startDate, LocalDate endDate) {
        return List.of();
    }

    @Override
    public TourPlanAvailabilityResponse getAvailabilityByTourPlanIdAndDate(Integer tourPlanId, LocalDate date) {
        return null;
    }

    @Override
    public List<TourPlanAvailabilityResponse> getUpcomingAvailableDatesByTourPlanId(Integer tourPlanId) {
        return List.of();
    }

    @Override
    public List<TourPlanAvailabilityResponse> getAvailableDatesWithSufficientSpots(Integer tourPlanId, Integer requiredSpots) {
        return List.of();
    }

    @Override
    public Long countUpcomingAvailableDatesByTourPlanId(Integer tourPlanId) {
        return 0L;
    }

    @Override
    public List<TourPlanAvailabilityResponse> getAvailabilitiesWithPriceOverride(Integer tourPlanId) {
        return List.of();
    }

    @Override
    public List<TourPlanAvailabilityResponse> getAvailabilitiesWithPriceAbove(Integer tourPlanId, BigDecimal price) {
        return List.of();
    }

    @Override
    public void deleteAllAvailabilitiesByTourPlanId(Integer tourPlanId) {

    }

    @Override
    public boolean existsAvailabilityForTourPlanAndDate(Integer tourPlanId, LocalDate date) {
        return false;
    }

    // Private methods
    private void validateTourPlanAvailability(TourPlanAvailabilityRequest request) {
        // Validate required fields
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (request.tourPlanId() == null) {
            throw new IllegalArgumentException("TourPlan ID cannot be null");
        }
        if (request.availableDate() == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
    }

    private TourPlanAvailability buildTourPlanAvailabilityFromRequest(TourPlanAvailabilityRequest request) {
        return TourPlanAvailability.builder()
                .tourPlan(findTourPlanOrFail(request.tourPlanId()))
                .availableDate(request.availableDate())
                .availableSpots(request.availableSpots())
                .isAvailable(request.isAvailable())
                .priceOverride(request.priceOverride())
                .build();
    }

    private TourPlan findTourPlanOrFail(Integer tourPlanId) {
        return this.tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("TourPlan with id " + tourPlanId + " not found"));
    }

    private void updateTourPlanAvailability(TourPlanAvailability availability, TourPlanAvailabilityRequest request) {
        availability.setAvailableDate(request.availableDate());
        availability.setAvailableSpots(request.availableSpots());
        availability.setIsAvailable(request.isAvailable());
        availability.setPriceOverride(request.priceOverride());
    }
}
