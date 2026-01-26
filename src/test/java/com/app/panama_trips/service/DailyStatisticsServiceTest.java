package com.app.panama_trips.service;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.DailyStatistics;
import com.app.panama_trips.persistence.repository.DailyStatisticsRepository;
import com.app.panama_trips.presentation.dto.DailyStatisticsRequest;
import com.app.panama_trips.presentation.dto.DailyStatisticsResponse;
import com.app.panama_trips.service.implementation.DailyStatisticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailyStatisticsServiceTest {

    @Mock
    private DailyStatisticsRepository dailyStatisticsRepository;

    @InjectMocks
    private DailyStatisticsService dailyStatisticsService;

    private final LocalDate testDate = LocalDate.of(2024, 10, 1);
    private final Long testId = 1L;
    private final BigDecimal testRevenue = BigDecimal.valueOf(1000.00);

    private DailyStatistics createMockEntity() {
        return DailyStatistics.builder()
                .id(testId)
                .date(testDate)
                .totalReservations(10)
                .cancelledReservations(1)
                .totalRevenue(testRevenue)
                .newUsers(5)
                .build();
    }

    private DailyStatisticsRequest createMockRequest() {
        return new DailyStatisticsRequest(
                testDate,
                10L,
                1L,
                testRevenue,
                5L,
                2L,
                100L,
                BigDecimal.valueOf(4.5)
        );
    }

    @Test
    void getAllDailyStatistics_shouldReturnListOfResponses() {
        // Given
        DailyStatistics entity = createMockEntity();
        List<DailyStatistics> entities = List.of(entity);
        when(dailyStatisticsRepository.findAll()).thenReturn(entities);

        // When
        List<DailyStatisticsResponse> result = dailyStatisticsService.getAllDailyStatistics();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testDate, result.get(0).date());
        verify(dailyStatisticsRepository, times(1)).findAll();
    }

    @Test
    void getDailyStatisticsById_shouldReturnResponse() {
        // Given
        DailyStatistics entity = createMockEntity();
        when(dailyStatisticsRepository.findById(testId)).thenReturn(Optional.of(entity));

        // When
        DailyStatisticsResponse result = dailyStatisticsService.getDailyStatisticsById(testId);

        // Then
        assertNotNull(result);
        assertEquals(testDate, result.date());
        verify(dailyStatisticsRepository, times(1)).findById(testId);
    }

    @Test
    void getDailyStatisticsByDate_shouldReturnListOfResponses() {
        // Given
        DailyStatistics entity = createMockEntity();
        LocalDate dateB = testDate.plusDays(1);
        when(dailyStatisticsRepository.findByDateBetween(testDate, dateB)).thenReturn(List.of(entity));

        // When
        List<DailyStatisticsResponse> result = dailyStatisticsService.getDailyStatisticsByDate(testDate, dateB);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testDate, result.get(0).date());
        verify(dailyStatisticsRepository, times(1)).findByDateBetween(testDate, dateB);
    }

    @Test
    void saveDailyStatistics_shouldSaveAndReturnResponse() {
        // Given
        DailyStatistics savedEntity = createMockEntity();
        when(dailyStatisticsRepository.save(any(DailyStatistics.class))).thenReturn(savedEntity);

        // When
        DailyStatisticsResponse result = dailyStatisticsService.saveDailyStatistics(createMockRequest());

        // Then
        assertNotNull(result);
        assertEquals(testDate, result.date());
        verify(dailyStatisticsRepository, times(1)).save(any(DailyStatistics.class));
    }

    @Test
    void updateDailyStatistics_shouldUpdateAndReturnResponse() {
        // Given
        DailyStatistics original = createMockEntity();
        DailyStatistics updated = createMockEntity();
        updated.setTotalReservations(20);  // Simulate update
        when(dailyStatisticsRepository.findById(testId)).thenReturn(Optional.of(original));
        when(dailyStatisticsRepository.save(eq(original))).thenReturn(updated);

        // When
        DailyStatisticsResponse result = dailyStatisticsService.updateDailyStatistics(testId, createMockRequest());

        // Then
        assertNotNull(result);
        assertEquals(20, result.totalReservations());
        verify(dailyStatisticsRepository, times(1)).findById(testId);
        verify(dailyStatisticsRepository, times(1)).save(original);
    }

    @Test
    void deleteDailyStatistics_shouldDeleteEntity() {
        // Given
        when(dailyStatisticsRepository.findById(testId)).thenReturn(Optional.of(createMockEntity()));

        // When
        dailyStatisticsService.deleteDailyStatistics(testId);

        // Then
        verify(dailyStatisticsRepository, times(1)).findById(testId);
        verify(dailyStatisticsRepository, times(1)).deleteById(testId);
    }
}
