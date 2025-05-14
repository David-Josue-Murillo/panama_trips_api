package com.app.panama_trips.service.implementation;

import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourPlanSpecialPrice;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.persistence.repository.TourPlanSpecialPriceRepository;
import com.app.panama_trips.presentation.dto.TourPlanSpecialPriceRequest;
import com.app.panama_trips.presentation.dto.TourPlanSpecialPriceResponse;
import com.app.panama_trips.service.interfaces.ITourPlanSpecialPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TourPlanSpecialPriceService implements ITourPlanSpecialPriceService {

    private final TourPlanSpecialPriceRepository tourPlanSpecialPriceRepository;
    private final TourPlanRepository tourPlanRepository;

    @Override
    public Page<TourPlanSpecialPriceResponse> getAll() {
        return null;
    }

    @Override
    public Optional<TourPlanSpecialPriceResponse> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public TourPlanSpecialPriceResponse save(TourPlanSpecialPriceRequest request) {
        return null;
    }

    @Override
    public TourPlanSpecialPriceResponse update(Integer id, TourPlanSpecialPrice tourPlanSpecialPrice) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public List<TourPlanSpecialPriceResponse> findByTourPlan(TourPlan tourPlan) {
        return List.of();
    }

    @Override
    public List<TourPlanSpecialPriceResponse> findByTourPlanOrderByStartDateAsc(TourPlan tourPlan) {
        return List.of();
    }

    @Override
    public List<TourPlanSpecialPriceResponse> findByStartDateBetween(LocalDate startDate, LocalDate endDate) {
        return List.of();
    }

    @Override
    public List<TourPlanSpecialPriceResponse> findOverlappingPricePeriodsForTourPlan(Integer tourPlanId, LocalDate startDate, LocalDate endDate) {
        return List.of();
    }

    @Override
    public List<TourPlanSpecialPriceResponse> findByTourPlanAndPriceGreaterThan(TourPlan tourPlan, BigDecimal price) {
        return List.of();
    }

    @Override
    public List<TourPlanSpecialPriceResponse> findByTourPlanAndStartDateGreaterThanEqual(TourPlan tourPlan, LocalDate date) {
        return List.of();
    }

    @Override
    public List<TourPlanSpecialPriceResponse> findByEndDateBetween(LocalDate startDate, LocalDate endDate) {
        return List.of();
    }

    @Override
    public Optional<TourPlanSpecialPriceResponse> findByTourPlanIdAndDate(Integer tourPlanId, LocalDate date) {
        return Optional.empty();
    }

    @Override
    public void deleteByTourPlan(TourPlan tourPlan) {

    }

    @Override
    public boolean existsByTourPlanAndStartDateAndEndDate(TourPlan tourPlan, LocalDate startDate, LocalDate endDate) {
        return false;
    }
}
