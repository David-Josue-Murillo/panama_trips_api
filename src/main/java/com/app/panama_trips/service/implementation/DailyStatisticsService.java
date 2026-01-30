package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.DailyStatistics;
import com.app.panama_trips.persistence.repository.DailyStatisticsRepository;
import com.app.panama_trips.presentation.dto.DailyStatisticsRequest;
import com.app.panama_trips.presentation.dto.DailyStatisticsResponse;
import com.app.panama_trips.service.interfaces.IDailyStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyStatisticsService implements IDailyStatisticsService {

    private final DailyStatisticsRepository dailyStatisticsRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DailyStatisticsResponse> getAllDailyStatistics() {
        return this.dailyStatisticsRepository.findAll().stream()
                .map(DailyStatisticsResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DailyStatisticsResponse getDailyStatisticsById(Long id) {
        return this.dailyStatisticsRepository.findById(id)
                .map(DailyStatisticsResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("DailyStatistics not found with id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DailyStatisticsResponse> getDailyStatisticsByDate(LocalDate dateA, LocalDate dateB) {
        return this.dailyStatisticsRepository.findByDateBetween(dateA, dateB)
                .stream()
                .map(DailyStatisticsResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public DailyStatisticsResponse saveDailyStatistics(DailyStatisticsRequest dailyStatisticsRequest) {
        validateDailyStatistics(dailyStatisticsRequest.date());
        DailyStatistics dailyStatistics = builderDailyStatisticsFromRequest(dailyStatisticsRequest);
        return DailyStatisticsResponse.from(this.dailyStatisticsRepository.save(dailyStatistics));
    }

    @Override
    @Transactional
    public DailyStatisticsResponse updateDailyStatistics(Long id, DailyStatisticsRequest dailyStatisticsRequest) {
        DailyStatistics dailyStatisticsExisting = this.dailyStatisticsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DailyStatistics not found with id " + id));
        updateDailyStatisticsFields(dailyStatisticsExisting, dailyStatisticsRequest);
        return DailyStatisticsResponse.from(this.dailyStatisticsRepository.save(dailyStatisticsExisting));
    }

    @Override
    @Transactional
    public void deleteDailyStatistics(Long id) {
        if (this.dailyStatisticsRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("DailyStatistics not found with id " + id);
        }
        this.dailyStatisticsRepository.deleteById(id);
    }

    // Private methods
    private void validateDailyStatistics(LocalDate date) {
        if (dailyStatisticsRepository.findByDate(date).isPresent()) {
            throw new IllegalArgumentException("DailyStatistics with date " + date + " already exists");
        }
    }

    private DailyStatistics builderDailyStatisticsFromRequest(DailyStatisticsRequest request) {
        return DailyStatistics.builder()
                .date(request.date())
                .totalReservations(request.totalReservations().intValue())
                .cancelledReservations(request.totalReservationsCancelled().intValue())
                .totalRevenue(request.totalRevenue())
                .newUsers(request.newUsers().intValue())
                .build();
    }

    private void updateDailyStatisticsFields(DailyStatistics dailyStatistics, DailyStatisticsRequest request) {
        validateDailyStatistics(request.date());  // Re-validate date if changed
        dailyStatistics.setDate(request.date());
        dailyStatistics.setTotalReservations(request.totalReservations().intValue());
        dailyStatistics.setCancelledReservations(request.totalReservationsCancelled().intValue());
        dailyStatistics.setTotalRevenue(request.totalRevenue());
        dailyStatistics.setNewUsers(request.newUsers().intValue());
    }
}
