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
        return this.availabilityRepository.findAll(pageable)
                .map(TourPlanAvailabilityResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public TourPlanAvailabilityResponse getTourPlanAvailabilityById(Integer id) {
        return this.availabilityRepository.findById(id)
                .map(TourPlanAvailabilityResponse::new)
                .orElseThrow(() -> new RuntimeException("TourPlanAvailability not found with id: " + id));
    }

    @Override
    public TourPlanAvailabilityResponse saveTourPlanAvailability(TourPlanAvailabilityRequest request) {
        validateTourPlanAvailability(request);
        TourPlanAvailability availability = buildTourPlanAvailabilityFromRequest(request);
        return new TourPlanAvailabilityResponse(this.availabilityRepository.save(availability));
    }

    @Override
    public TourPlanAvailabilityResponse updateTourPlanAvailability(Integer id, TourPlanAvailabilityRequest request) {
        TourPlanAvailability existingAvailability = this.availabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TourPlanAvailability with id " + id + " not found"));

        updateTourPlanAvailability(existingAvailability, request);
        return new TourPlanAvailabilityResponse(this.availabilityRepository.save(existingAvailability));
    }

    @Override
    public void deleteTourPlanAvailability(Integer id) {
        if(!this.availabilityRepository.existsById(id)) {
            throw new ResourceNotFoundException("TourPlanAvailability with id " + id + " not found");
        }

        this.availabilityRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPlanAvailabilityResponse> getTourPlanAvailabilitiesByTourPlanId(Integer tourPlanId) {
        TourPlan tourPlan = findTourPlanOrFail(tourPlanId);

        return availabilityRepository.findByTourPlan(tourPlan).stream()
                .map(TourPlanAvailabilityResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPlanAvailabilityResponse> getAvailableDatesByTourPlanId(Integer tourPlanId) {
        TourPlan tourPlan = findTourPlanOrFail(tourPlanId);

        return availabilityRepository.findByTourPlanAndIsAvailableTrue(tourPlan).stream()
                .map(TourPlanAvailabilityResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPlanAvailabilityResponse> getAvailabilitiesByDateRange(LocalDate startDate, LocalDate endDate) {
        return availabilityRepository.findByAvailableDateBetween(startDate, endDate).stream()
                .map(TourPlanAvailabilityResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPlanAvailabilityResponse> getAvailabilitiesByTourPlanIdAndDateRange(Integer tourPlanId, LocalDate startDate, LocalDate endDate) {
        TourPlan tourPlan = findTourPlanOrFail(tourPlanId);

        return availabilityRepository.findByTourPlanAndAvailableDateBetween(tourPlan, startDate, endDate).stream()
                .map(TourPlanAvailabilityResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TourPlanAvailabilityResponse getAvailabilityByTourPlanIdAndDate(Integer tourPlanId, LocalDate date) {
        TourPlan tourPlan = findTourPlanOrFail(tourPlanId);

        return availabilityRepository.findByTourPlanAndAvailableDate(tourPlan, date)
                .map(TourPlanAvailabilityResponse::new)
                .orElseThrow(() -> new RuntimeException(
                        "TourPlanAvailability not found for tour plan id: " + tourPlanId + " and date: " + date));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPlanAvailabilityResponse> getUpcomingAvailableDatesByTourPlanId(Integer tourPlanId) {
        LocalDate today = LocalDate.now();

        return this.availabilityRepository.findAvailableDatesByTourPlanId(tourPlanId, today)
                .stream()
                .map(TourPlanAvailabilityResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPlanAvailabilityResponse> getAvailableDatesWithSufficientSpots(Integer tourPlanId, Integer requiredSpots) {
        LocalDate today = LocalDate.now();

        return availabilityRepository.findDatesByTourPlanWithSufficientSpots(tourPlanId, requiredSpots, today)
                .stream()
                .map(TourPlanAvailabilityResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countUpcomingAvailableDatesByTourPlanId(Integer tourPlanId) {
        LocalDate today = LocalDate.now();

        return availabilityRepository.countAvailableDatesByTourPlan(tourPlanId, today);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPlanAvailabilityResponse> getAvailabilitiesWithPriceOverride(Integer tourPlanId) {
        TourPlan tourPlan = findTourPlanOrFail(tourPlanId);

        return availabilityRepository.findByTourPlanAndPriceOverrideIsNotNull(tourPlan).stream()
                .map(TourPlanAvailabilityResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPlanAvailabilityResponse> getAvailabilitiesWithPriceAbove(Integer tourPlanId, BigDecimal price) {
        TourPlan tourPlan = findTourPlanOrFail(tourPlanId);

        return availabilityRepository.findByTourPlanAndPriceOverrideGreaterThan(tourPlan, price).stream()
                .map(TourPlanAvailabilityResponse::new)
                .toList();
    }

    @Override
    public void deleteAllAvailabilitiesByTourPlanId(Integer tourPlanId) {
        TourPlan tourPlan = findTourPlanOrFail(tourPlanId);
        availabilityRepository.deleteByTourPlan(tourPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsAvailabilityForTourPlanAndDate(Integer tourPlanId, LocalDate date) {
        TourPlan tourPlan = findTourPlanOrFail(tourPlanId);
        return availabilityRepository.existsByTourPlanAndAvailableDate(tourPlan, date);
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
