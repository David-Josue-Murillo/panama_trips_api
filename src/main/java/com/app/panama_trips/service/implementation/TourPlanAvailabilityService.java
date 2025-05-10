package com.app.panama_trips.service.implementation;

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
}
