package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourPlanSpecialPrice;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.persistence.repository.TourPlanSpecialPriceRepository;
import com.app.panama_trips.presentation.dto.TourPlanSpecialPriceRequest;
import com.app.panama_trips.presentation.dto.TourPlanSpecialPriceResponse;
import com.app.panama_trips.service.interfaces.ITourPlanSpecialPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = true)
    public Page<TourPlanSpecialPriceResponse> getAll(Pageable pageable) {
        return this.tourPlanSpecialPriceRepository.findAll(pageable)
                .map(TourPlanSpecialPriceResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public TourPlanSpecialPriceResponse findById(Integer id) {
        return this.tourPlanSpecialPriceRepository.findById(id)
                .map(TourPlanSpecialPriceResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("TourPlanSpecialPrice with id " + id + " not found"));
    }

    @Override
    @Transactional
    public TourPlanSpecialPriceResponse save(TourPlanSpecialPriceRequest request) {
        validateTourPlanSpecialService(request);
        TourPlanSpecialPrice tourPlanSpecialPrice = builderFromRequest(request);
        return new TourPlanSpecialPriceResponse(this.tourPlanSpecialPriceRepository.save(tourPlanSpecialPrice));
    }

    @Override
    @Transactional
    public TourPlanSpecialPriceResponse update(Integer id, TourPlanSpecialPriceRequest tourPlanSpecialPrice) {
        TourPlanSpecialPrice existingTour = this.tourPlanSpecialPriceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TourPlanSpecialPrice with id " + id + " not found"));
        updateTourPlanSpecialPrice(existingTour, tourPlanSpecialPrice);
        return new TourPlanSpecialPriceResponse(this.tourPlanSpecialPriceRepository.save(existingTour));
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        if(!this.tourPlanSpecialPriceRepository.existsById(id)) {
            throw new ResourceNotFoundException("TourPlanSpecialPrice with id " + id + " not found");
        }
        this.tourPlanSpecialPriceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPlanSpecialPriceResponse> findByTourPlan(TourPlan tourPlan) {
        return this.tourPlanSpecialPriceRepository.findByTourPlan(tourPlan)
                .stream()
                .map(TourPlanSpecialPriceResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPlanSpecialPriceResponse> findByTourPlanOrderByStartDateAsc(TourPlan tourPlan) {
        return this.tourPlanSpecialPriceRepository.findByTourPlanOrderByStartDateAsc(tourPlan)
                .stream()
                .map(TourPlanSpecialPriceResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPlanSpecialPriceResponse> findByStartDateBetween(LocalDate startDate, LocalDate endDate) {
        return this.tourPlanSpecialPriceRepository.findByStartDateBetween(startDate, endDate)
                .stream()
                .map(TourPlanSpecialPriceResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPlanSpecialPriceResponse> findByEndDateBetween(LocalDate startDate, LocalDate endDate) {
        return this.tourPlanSpecialPriceRepository.findByEndDateBetween(startDate, endDate)
                .stream()
                .map(TourPlanSpecialPriceResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TourPlanSpecialPriceResponse> findByTourPlanIdAndDate(Integer tourPlanId, LocalDate date) {
        return this.tourPlanSpecialPriceRepository.findByTourPlanIdAndDate(tourPlanId, date)
                .map(TourPlanSpecialPriceResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPlanSpecialPriceResponse> findOverlappingPricePeriodsForTourPlan(Integer tourPlanId, LocalDate startDate, LocalDate endDate) {
        return this.tourPlanSpecialPriceRepository.findOverlappingPricePeriodsForTourPlan(tourPlanId, startDate, endDate)
                .stream()
                .map(TourPlanSpecialPriceResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPlanSpecialPriceResponse> findByTourPlanAndPriceGreaterThan(TourPlan tourPlan, BigDecimal price) {
        return this.tourPlanSpecialPriceRepository.findByTourPlanAndPriceGreaterThan(tourPlan, price)
                .stream()
                .map(TourPlanSpecialPriceResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPlanSpecialPriceResponse> findByTourPlanAndStartDateGreaterThanEqual(TourPlan tourPlan, LocalDate date) {
        return this.tourPlanSpecialPriceRepository.findByTourPlanAndStartDateGreaterThanEqual(tourPlan, date)
                .stream()
                .map(TourPlanSpecialPriceResponse::new)
                .toList();
    }

    @Override
    @Transactional
    public void deleteByTourPlan(TourPlan tourPlan) {
        this.tourPlanSpecialPriceRepository.deleteByTourPlan(tourPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByTourPlanAndStartDateAndEndDate(TourPlan tourPlan, LocalDate startDate, LocalDate endDate) {
        return this.tourPlanSpecialPriceRepository.existsByTourPlanAndStartDateAndEndDate(tourPlan, startDate, endDate);
    }

    // Private methods
    private void validateTourPlanSpecialService(TourPlanSpecialPriceRequest request) {
        // Validate if tour plan exists
        TourPlan tourPlan = findTourPlanOrFail(request.tourPlanId());

        // Check for overlapping date ranges for the same tour plan
        List<TourPlanSpecialPrice> overlappingPrices = tourPlanSpecialPriceRepository
                .findOverlappingPricePeriodsForTourPlan(
                        request.tourPlanId(),
                        request.startDate(),
                        request.endDate());

        if (!overlappingPrices.isEmpty()) {
            throw new IllegalArgumentException("There is already a special price defined for this tour plan during the specified date range");
        }

        // Check if the special price is lower than the regular tour price
        if (tourPlan.getPrice() != null && request.price().compareTo(tourPlan.getPrice()) >= 0) {
            throw new IllegalArgumentException("Special price must be lower than the regular tour price");
        }

        // Check if the date range is not too long (e.g., maximum 90 days)
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(request.startDate(), request.endDate());
        if (daysBetween > 90) {
            throw new IllegalArgumentException("Special price period cannot exceed 90 days");
        }
    }

    private TourPlanSpecialPrice builderFromRequest(TourPlanSpecialPriceRequest request) {
        return TourPlanSpecialPrice.builder()
                .tourPlan(findTourPlanOrFail(request.tourPlanId()))
                .startDate(request.startDate())
                .endDate(request.endDate())
                .price(request.price())
                .description(request.description())
                .build();
    }

    private TourPlan findTourPlanOrFail(Integer tourPlanId) {
        return this.tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("TourPlan with id " + tourPlanId + " not found"));
    }

    private void updateTourPlanSpecialPrice(TourPlanSpecialPrice existingTour, TourPlanSpecialPriceRequest request) {
        existingTour.setTourPlan(findTourPlanOrFail(request.tourPlanId()));
        existingTour.setStartDate(request.startDate());
        existingTour.setEndDate(request.endDate());
        existingTour.setPrice(request.price());
        existingTour.setDescription(request.description());
    }
}
