package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.DailyStatisticsRequest;
import com.app.panama_trips.presentation.dto.DailyStatisticsResponse;
import java.time.LocalDate;
import java.util.List;

public interface IDailyStatisticsService {
    List<DailyStatisticsResponse> getAllDailyStatistics();
    DailyStatisticsResponse getDailyStatisticsById(Long id);
    List<DailyStatisticsResponse> getDailyStatisticsByDate(LocalDate dateA, LocalDate dateB);
    DailyStatisticsResponse saveDailyStatistics(DailyStatisticsRequest dailyStatisticsRequest);
    DailyStatisticsResponse updateDailyStatistics(Long id, DailyStatisticsRequest dailyStatisticsRequest);
    void deleteDailyStatistics(Long id);
}