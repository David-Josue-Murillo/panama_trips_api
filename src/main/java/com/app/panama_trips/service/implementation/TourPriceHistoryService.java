package com.app.panama_trips.service.implementation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Transactional(readOnly = true)
    public List<TourPriceHistoryResponse> findByTourPlanId(Integer tourPlanId) {
        return repository.findByTourPlanIdOrderByChangedAtDesc(tourPlanId)
                .stream()
                .map(TourPriceHistoryResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TourPriceHistoryResponse> findByTourPlanIdOrderByChangedAtDesc(Integer tourPlanId, Pageable pageable) {
        TourPlan tourPlan = tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour plan not found"));
        return repository.findByTourPlanOrderByChangedAtDesc(tourPlan, pageable)
                .map(TourPriceHistoryResponse::new);
    }

    @Override
    public List<TourPriceHistoryResponse> findByTourPlanIdAndChangedAtBetween(Integer tourPlanId,
            LocalDateTime startDate, LocalDateTime endDate) {
        TourPlan tourPlan = tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour plan not found"));
        return repository.findByTourPlanAndChangedAtBetween(tourPlan, startDate, endDate)
                .stream()
                .map(TourPriceHistoryResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPriceHistoryResponse> findByChangedById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return repository.findByChangedBy(user)
                .stream()
                .map(TourPriceHistoryResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculateAveragePriceChangePercentageByTourPlanId(Integer tourPlanId) {
        return repository.calculateAveragePriceChangePercentageByTourPlanId(tourPlanId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPriceHistoryResponse> findByNewPriceGreaterThan(BigDecimal price) {
        return repository.findByNewPriceGreaterThan(price)
                .stream()
                .map(TourPriceHistoryResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countPriceChangesByTourPlanId(Integer tourPlanId) {
        return repository.countPriceChangesByTourPlanId(tourPlanId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPriceHistoryResponse> getRecentChanges(int limit) {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "changedAt"))
                .stream()
                .limit(limit)
                .map(TourPriceHistoryResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TourPriceHistoryResponse> getLatestChangeForTourPlan(Integer tourPlanId) {
        return repository.findByTourPlanIdOrderByChangedAtDesc(tourPlanId)
                .stream()
                .findFirst()
                .map(TourPriceHistoryResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getCurrentPriceForTourPlan(Integer tourPlanId) {
        Optional<TourPriceHistory> latest = repository.findByTourPlanIdOrderByChangedAtDesc(tourPlanId)
                .stream().findFirst();
        if (latest.isPresent()) {
            return latest.get().getNewPrice();
        }
        // Fallback to current price on TourPlan if no history
        TourPlan plan = tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour plan not found"));
        return plan.getPrice();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getPreviousPriceForTourPlan(Integer tourPlanId) {
        return repository.findByTourPlanIdOrderByChangedAtDesc(tourPlanId)
                .stream()
                .findFirst()
                .map(TourPriceHistory::getPreviousPrice)
                .orElseThrow(() -> new ResourceNotFoundException("No price history found for tour plan"));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getMaxPriceForTourPlan(Integer tourPlanId) {
        return repository.findByTourPlanIdOrderByChangedAtDesc(tourPlanId)
                .stream()
                .map(TourPriceHistory::getNewPrice)
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new ResourceNotFoundException("No price history found for tour plan"));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getMinPriceForTourPlan(Integer tourPlanId) {
        return repository.findByTourPlanIdOrderByChangedAtDesc(tourPlanId)
                .stream()
                .map(TourPriceHistory::getNewPrice)
                .min(Comparator.naturalOrder())
                .orElseThrow(() -> new ResourceNotFoundException("No price history found for tour plan"));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAveragePriceForTourPlan(Integer tourPlanId) {
        List<TourPriceHistory> list = repository.findByTourPlanIdOrderByChangedAtDesc(tourPlanId);
        if (list.isEmpty()) {
            throw new ResourceNotFoundException("No price history found for tour plan");
        }
        return list.stream()
                .map(TourPriceHistory::getNewPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(list.size()), BigDecimal.ROUND_HALF_UP);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPriceHistoryResponse> getPriceChangesOnDate(Integer tourPlanId, LocalDate date) {
        return repository.findByTourPlanIdOrderByChangedAtDesc(tourPlanId)
                .stream()
                .filter(t -> t.getChangedAt() != null && t.getChangedAt().toLocalDate().equals(date))
                .map(TourPriceHistoryResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourPriceHistoryResponse> getPriceChangesByUserAndDateRange(Long userId, LocalDateTime startDate,
            LocalDateTime endDate) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return repository.findByChangedBy(user)
                .stream()
                .filter(t -> t.getChangedAt() != null && !t.getChangedAt().isBefore(startDate)
                        && !t.getChangedAt().isAfter(endDate))
                .map(TourPriceHistoryResponse::new)
                .toList();
    }

    @Override
    @Transactional
    public void bulkCreateTourPriceHistories(List<TourPriceHistoryRequest> requests) {
        List<TourPriceHistory> entities = requests.stream()
                .map(this::buildFromRequest)
                .toList();
        repository.saveAll(entities);
    }

    @Override
    @Transactional
    public void bulkDeleteTourPriceHistories(List<Integer> tourPriceHistoryIds) {
        repository.deleteAllById(tourPriceHistoryIds);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByTourPlanId(Integer tourPlanId) {
        return repository.countPriceChangesByTourPlanId(tourPlanId);
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
