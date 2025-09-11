package com.app.panama_trips.service.implementation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourPriceHistory;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.persistence.repository.TourPriceHistoryRepository;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.presentation.dto.TourPriceHistoryRequest;
import com.app.panama_trips.presentation.dto.TourPriceHistoryResponse;
import com.app.panama_trips.service.interfaces.ITourPriceHistoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TourPriceHistoryService implements ITourPriceHistoryService {

    private final TourPriceHistoryRepository repository;
    private final TourPlanRepository tourPlanRepository;
    private final UserEntityRepository userRepository;
    
    @Override
    @Transactional(readOnly = true)
    public Page<TourPriceHistoryResponse> getAllTourPriceHistories(Pageable pageable) {
        return repository.findAll(pageable).map(TourPriceHistoryResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public TourPriceHistoryResponse getTourPriceHistoryById(Integer id) {
        return repository.findById(id)
                .map(TourPriceHistoryResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("Tour price history not found"));
    }

    @Override
    @Transactional
    public TourPriceHistoryResponse saveTourPriceHistory(TourPriceHistoryRequest request) {
        TourPriceHistory entity = buildFromRequest(request);
        return new TourPriceHistoryResponse(repository.save(entity));
    }

    @Override
    @Transactional
    public TourPriceHistoryResponse updateTourPriceHistory(Integer id, TourPriceHistoryRequest request) {
        TourPriceHistory existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tour price history not found"));
        updateFromRequest(existing, request);
        return new TourPriceHistoryResponse(repository.save(existing));
    }

    @Override
    @Transactional
    public void deleteTourPriceHistory(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Tour price history not found");
        }
        repository.deleteById(id);
    }

    @Override
    public List<TourPriceHistoryResponse> findByTourPlanId(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByTourPlanId'");
    }

    @Override
    public Page<TourPriceHistoryResponse> findByTourPlanIdOrderByChangedAtDesc(Integer tourPlanId, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByTourPlanIdOrderByChangedAtDesc'");
    }

    @Override
    public List<TourPriceHistoryResponse> findByTourPlanIdAndChangedAtBetween(Integer tourPlanId,
            LocalDateTime startDate, LocalDateTime endDate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByTourPlanIdAndChangedAtBetween'");
    }

    @Override
    public List<TourPriceHistoryResponse> findByChangedById(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByChangedById'");
    }

    @Override
    public Double calculateAveragePriceChangePercentageByTourPlanId(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
                "Unimplemented method 'calculateAveragePriceChangePercentageByTourPlanId'");
    }

    @Override
    public List<TourPriceHistoryResponse> findByNewPriceGreaterThan(BigDecimal price) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByNewPriceGreaterThan'");
    }

    @Override
    public Long countPriceChangesByTourPlanId(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'countPriceChangesByTourPlanId'");
    }

    @Override
    public List<TourPriceHistoryResponse> getRecentChanges(int limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRecentChanges'");
    }

    @Override
    public Optional<TourPriceHistoryResponse> getLatestChangeForTourPlan(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLatestChangeForTourPlan'");
    }

    @Override
    public BigDecimal getCurrentPriceForTourPlan(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentPriceForTourPlan'");
    }

    @Override
    public BigDecimal getPreviousPriceForTourPlan(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPreviousPriceForTourPlan'");
    }

    @Override
    public BigDecimal getMaxPriceForTourPlan(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMaxPriceForTourPlan'");
    }

    @Override
    public BigDecimal getMinPriceForTourPlan(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMinPriceForTourPlan'");
    }

    @Override
    public BigDecimal getAveragePriceForTourPlan(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAveragePriceForTourPlan'");
    }

    @Override
    public List<TourPriceHistoryResponse> getPriceChangesOnDate(Integer tourPlanId, LocalDate date) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPriceChangesOnDate'");
    }

    @Override
    public List<TourPriceHistoryResponse> getPriceChangesByUserAndDateRange(Long userId, LocalDateTime startDate,
            LocalDateTime endDate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPriceChangesByUserAndDateRange'");
    }

    @Override
    public void bulkCreateTourPriceHistories(List<TourPriceHistoryRequest> requests) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bulkCreateTourPriceHistories'");
    }

    @Override
    public void bulkDeleteTourPriceHistories(List<Integer> tourPriceHistoryIds) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bulkDeleteTourPriceHistories'");
    }

    @Override
    public boolean existsById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsById'");
    }

    @Override
    public long countByTourPlanId(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'countByTourPlanId'");
    }

    @Override
    public BigDecimal getTotalPriceIncreaseForTourPlan(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalPriceIncreaseForTourPlan'");
    }

    @Override
    public BigDecimal getTotalPriceDecreaseForTourPlan(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTotalPriceDecreaseForTourPlan'");
    }

    @Override
    public double getAverageChangePercentageForTourPlan(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAverageChangePercentageForTourPlan'");
    }

    @Override
    public List<TourPriceHistoryResponse> getTopTourPlansByChangeCount(int limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTopTourPlansByChangeCount'");
    }

    @Override
    public List<TourPriceHistoryResponse> getChangesByMonth(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getChangesByMonth'");
    }

    @Override
    public List<TourPriceHistoryResponse> getChangesByDayOfWeek(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getChangesByDayOfWeek'");
    }

    @Override
    public List<TourPriceHistoryResponse> searchChangesByPrice(BigDecimal price) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchChangesByPrice'");
    }

    private TourPriceHistory buildFromRequest(TourPriceHistoryRequest request) {
        TourPlan tourPlan = tourPlanRepository.findById(request.tourPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Tour plan not found"));

        UserEntity changedBy = null;
        if (request.changedById() != null) {
            changedBy = userRepository.findById(request.changedById())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }

        return TourPriceHistory.builder()
                .tourPlan(tourPlan)
                .previousPrice(request.previousPrice())
                .newPrice(request.newPrice())
                .changedBy(changedBy)
                .reason(request.reason())
                .build();
    }

    private void updateFromRequest(TourPriceHistory existing, TourPriceHistoryRequest request) {
        TourPlan tourPlan = tourPlanRepository.findById(request.tourPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Tour plan not found"));

        UserEntity changedBy = null;
        if (request.changedById() != null) {
            changedBy = userRepository.findById(request.changedById())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }

        existing.setTourPlan(tourPlan);
        existing.setPreviousPrice(request.previousPrice());
        existing.setNewPrice(request.newPrice());
        existing.setChangedBy(changedBy);
        existing.setReason(request.reason());
    }
}
