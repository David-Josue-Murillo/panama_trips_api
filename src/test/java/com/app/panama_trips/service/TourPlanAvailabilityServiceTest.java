package com.app.panama_trips.service;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.TourPlanAvailability;
import com.app.panama_trips.persistence.repository.TourPlanAvailabilityRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.presentation.dto.TourPlanAvailabilityRequest;
import com.app.panama_trips.presentation.dto.TourPlanAvailabilityResponse;
import com.app.panama_trips.service.implementation.TourPlanAvailabilityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.app.panama_trips.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TourPlanAvailabilityServiceTest {

    @Mock
    private TourPlanAvailabilityRepository tourPlanAvailabilityRepository;

    @Mock
    private TourPlanRepository tourPlanRepository;

    @InjectMocks
    private TourPlanAvailabilityService tourPlanAvailabilityService;

    @Test
    void getAllTourPlanAvailabilities_shouldReturnPagedAvailabilities() {
        // Given
        List<TourPlanAvailability> availabilities = tourPlanAvailabilityListsMock;
        Page<TourPlanAvailability> availabilityPage = new PageImpl<>(availabilities);
        Pageable pageable = PageRequest.of(0, 10);

        when(tourPlanAvailabilityRepository.findAll(pageable)).thenReturn(availabilityPage);

        // When
        Page<TourPlanAvailabilityResponse> result = tourPlanAvailabilityService.getAllTourPlanAvailabilities(pageable);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        verify(tourPlanAvailabilityRepository).findAll(pageable);
    }

    @Test
    void getTourPlanAvailabilityById_shouldReturnAvailability_whenExists() {
        // Given
        Integer id = 1;
        when(tourPlanAvailabilityRepository.findById(id)).thenReturn(Optional.of(tourPlanAvailabilityOneMock));

        // When
        TourPlanAvailabilityResponse result = tourPlanAvailabilityService.getTourPlanAvailabilityById(id);

        // Then
        assertNotNull(result);
        assertEquals(id, result.id());
        verify(tourPlanAvailabilityRepository).findById(id);
    }

    @Test
    void getTourPlanAvailabilityById_shouldThrowException_whenNotExists() {
        // Given
        Integer id = 999;
        when(tourPlanAvailabilityRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> tourPlanAvailabilityService.getTourPlanAvailabilityById(id));
    }

    @Test
    void saveTourPlanAvailability_shouldSaveAndReturnAvailability() {
        // Given
        when(tourPlanRepository.findById(1)).thenReturn(Optional.of(tourPlanOneMock));
        when(tourPlanAvailabilityRepository.save(any(TourPlanAvailability.class))).thenReturn(tourPlanAvailabilityOneMock);

        // When
        TourPlanAvailabilityResponse result = tourPlanAvailabilityService.saveTourPlanAvailability(tourPlanAvailabilityRequest);

        // Then
        assertNotNull(result);
        assertEquals(1, result.id());
        verify(tourPlanRepository).findById(1);
        verify(tourPlanAvailabilityRepository).save(any(TourPlanAvailability.class));
    }

    @Test
    void saveTourPlanAvailability_shouldThrowException_whenRequestIsNull() {
        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> tourPlanAvailabilityService.saveTourPlanAvailability(null));
    }

    @Test
    void saveTourPlanAvailability_shouldThrowException_whenTourPlanIdIsNull() {
        // Given
        TourPlanAvailabilityRequest request = new TourPlanAvailabilityRequest(
                null, 1, LocalDate.now(), 10, true, null
        );

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> tourPlanAvailabilityService.saveTourPlanAvailability(request));
    }

    @Test
    void saveTourPlanAvailability_shouldThrowException_whenDateIsNull() {
        // Given
        TourPlanAvailabilityRequest request = new TourPlanAvailabilityRequest(
                1, 1, null, 10, true, null
        );

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> tourPlanAvailabilityService.saveTourPlanAvailability(request));
    }

    @Test
    void updateTourPlanAvailability_shouldUpdateAndReturnAvailability() {
        // Given
        Integer id = 1;
        TourPlanAvailabilityRequest updateRequest = new TourPlanAvailabilityRequest(
                1, 1, LocalDate.now().plusWeeks(1), 15, true, new BigDecimal("110.00")
        );

        when(tourPlanAvailabilityRepository.findById(id)).thenReturn(Optional.of(tourPlanAvailabilityOneMock));
        when(tourPlanAvailabilityRepository.save(any(TourPlanAvailability.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        TourPlanAvailabilityResponse result = tourPlanAvailabilityService.updateTourPlanAvailability(id, updateRequest);

        // Then
        assertNotNull(result);
        verify(tourPlanAvailabilityRepository).findById(id);
        verify(tourPlanAvailabilityRepository).save(any(TourPlanAvailability.class));
    }

    @Test
    void updateTourPlanAvailability_shouldThrowException_whenAvailabilityNotFound() {
        // Given
        Integer id = 999;
        when(tourPlanAvailabilityRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> tourPlanAvailabilityService.updateTourPlanAvailability(id, tourPlanAvailabilityRequest));
    }

    @Test
    void deleteTourPlanAvailability_shouldDeleteAvailability_whenExists() {
        // Given
        Integer id = 1;
        when(tourPlanAvailabilityRepository.existsById(id)).thenReturn(true);

        // When
        tourPlanAvailabilityService.deleteTourPlanAvailability(id);

        // Then
        verify(tourPlanAvailabilityRepository).deleteById(id);
    }

    @Test
    void deleteTourPlanAvailability_shouldThrowException_whenNotExists() {
        // Given
        Integer id = 999;
        when(tourPlanAvailabilityRepository.existsById(id)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> tourPlanAvailabilityService.deleteTourPlanAvailability(id));
    }

    @Test
    void getTourPlanAvailabilitiesByTourPlanId_shouldReturnAvailabilities() {
        // Given
        Integer tourPlanId = 1;
        List<TourPlanAvailability> availabilities = tourPlanAvailabilityListsMock;

        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlanOneMock));
        when(tourPlanAvailabilityRepository.findByTourPlan(tourPlanOneMock)).thenReturn(availabilities);

        // When
        List<TourPlanAvailabilityResponse> result = tourPlanAvailabilityService.getTourPlanAvailabilitiesByTourPlanId(tourPlanId);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(tourPlanRepository).findById(tourPlanId);
        verify(tourPlanAvailabilityRepository).findByTourPlan(tourPlanOneMock);
    }

    @Test
    void getAvailableDatesByTourPlanId_shouldReturnAvailableDates() {
        // Given
        Integer tourPlanId = 1;
        List<TourPlanAvailability> availabilities = tourPlanAvailabilityListsMock;

        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlanOneMock));
        when(tourPlanAvailabilityRepository.findByTourPlanAndIsAvailableTrue(tourPlanOneMock)).thenReturn(availabilities);

        // When
        List<TourPlanAvailabilityResponse> result = tourPlanAvailabilityService.getAvailableDatesByTourPlanId(tourPlanId);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(tourPlanRepository).findById(tourPlanId);
        verify(tourPlanAvailabilityRepository).findByTourPlanAndIsAvailableTrue(tourPlanOneMock);
    }

    @Test
    void getAvailabilitiesByDateRange_shouldReturnAvailabilitiesInRange() {
        // Given
        List<TourPlanAvailability> availabilities = tourPlanAvailabilityListsMock;

        when(tourPlanAvailabilityRepository.findByAvailableDateBetween(LocalDate.now(), LocalDate.now().plusWeeks(1))).thenReturn(availabilities);

        // When
        List<TourPlanAvailabilityResponse> result = tourPlanAvailabilityService.getAvailabilitiesByDateRange(LocalDate.now(), LocalDate.now().plusWeeks(1));

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(tourPlanAvailabilityRepository).findByAvailableDateBetween(LocalDate.now(), LocalDate.now().plusWeeks(1));
    }

    @Test
    void getAvailabilitiesByTourPlanIdAndDateRange_shouldReturnAvailabilities() {
        // Given
        Integer tourPlanId = 1;
        List<TourPlanAvailability> availabilities = tourPlanAvailabilityListsMock;

        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlanOneMock));
        when(tourPlanAvailabilityRepository.findByTourPlanAndAvailableDateBetween(tourPlanOneMock, LocalDate.now(), LocalDate.now().plusWeeks(1)))
                .thenReturn(availabilities);

        // When
        List<TourPlanAvailabilityResponse> result =
                tourPlanAvailabilityService.getAvailabilitiesByTourPlanIdAndDateRange(tourPlanId, LocalDate.now(), LocalDate.now().plusWeeks(1));

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(tourPlanRepository).findById(tourPlanId);
        verify(tourPlanAvailabilityRepository).findByTourPlanAndAvailableDateBetween(tourPlanOneMock, LocalDate.now(), LocalDate.now().plusWeeks(1));
    }

    @Test
    void getAvailabilityByTourPlanIdAndDate_shouldReturnAvailability_whenExists() {
        // Given
        Integer tourPlanId = 1;

        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlanOneMock));
        when(tourPlanAvailabilityRepository.findByTourPlanAndAvailableDate(tourPlanOneMock, LocalDate.now().plusDays(1)))
                .thenReturn(Optional.of(tourPlanAvailabilityOneMock));

        // When
        TourPlanAvailabilityResponse result = tourPlanAvailabilityService.getAvailabilityByTourPlanIdAndDate(tourPlanId, LocalDate.now().plusDays(1));

        // Then
        assertNotNull(result);
        assertEquals(1, result.id());
        verify(tourPlanRepository).findById(tourPlanId);
        verify(tourPlanAvailabilityRepository).findByTourPlanAndAvailableDate(tourPlanOneMock, LocalDate.now().plusDays(1));
    }

    @Test
    void getAvailabilityByTourPlanIdAndDate_shouldThrowException_whenNotExists() {
        // Given
        Integer tourPlanId = 1;
        LocalDate date = LocalDate.now().plusDays(1).plusMonths(1);

        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlanOneMock));
        when(tourPlanAvailabilityRepository.findByTourPlanAndAvailableDate(tourPlanOneMock, date))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class,
                () -> tourPlanAvailabilityService.getAvailabilityByTourPlanIdAndDate(tourPlanId, date));
    }

    @Test
    void getUpcomingAvailableDatesByTourPlanId_shouldReturnUpcomingDates() {
        // Given
        Integer tourPlanId = 1;
        List<TourPlanAvailability> availabilities = tourPlanAvailabilityListsMock;

        when(tourPlanAvailabilityRepository.findAvailableDatesByTourPlanId(eq(tourPlanId), any(LocalDate.class)))
                .thenReturn(availabilities);

        // When
        List<TourPlanAvailabilityResponse> result = tourPlanAvailabilityService.getUpcomingAvailableDatesByTourPlanId(tourPlanId);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(tourPlanAvailabilityRepository).findAvailableDatesByTourPlanId(eq(tourPlanId), any(LocalDate.class));
    }

    @Test
    void getAvailableDatesWithSufficientSpots_shouldReturnAvailabilities() {
        // Given
        Integer tourPlanId = 1;
        Integer requiredSpots = 5;
        List<TourPlanAvailability> availabilities = tourPlanAvailabilityListsMock;

        when(tourPlanAvailabilityRepository.findDatesByTourPlanWithSufficientSpots(
                eq(tourPlanId), eq(requiredSpots), any(LocalDate.class)))
                .thenReturn(availabilities);

        // When
        List<TourPlanAvailabilityResponse> result =
                tourPlanAvailabilityService.getAvailableDatesWithSufficientSpots(tourPlanId, requiredSpots);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(tourPlanAvailabilityRepository).findDatesByTourPlanWithSufficientSpots(
                eq(tourPlanId), eq(requiredSpots), any(LocalDate.class));
    }

    @Test
    void countUpcomingAvailableDatesByTourPlanId_shouldReturnCount() {
        // Given
        Integer tourPlanId = 1;
        Long count = 2L;

        when(tourPlanAvailabilityRepository.countAvailableDatesByTourPlan(eq(tourPlanId), any(LocalDate.class)))
                .thenReturn(count);

        // When
        Long result = tourPlanAvailabilityService.countUpcomingAvailableDatesByTourPlanId(tourPlanId);

        // Then
        assertEquals(count, result);
        verify(tourPlanAvailabilityRepository).countAvailableDatesByTourPlan(eq(tourPlanId), any(LocalDate.class));
    }

    @Test
    void getAvailabilitiesWithPriceOverride_shouldReturnAvailabilitiesWithOverride() {
        // Given
        Integer tourPlanId = 1;
        List<TourPlanAvailability> availabilities = tourPlanAvailabilityListsMock;

        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlanOneMock));
        when(tourPlanAvailabilityRepository.findByTourPlanAndPriceOverrideIsNotNull(tourPlanOneMock))
                .thenReturn(availabilities);

        // When
        List<TourPlanAvailabilityResponse> result = tourPlanAvailabilityService.getAvailabilitiesWithPriceOverride(tourPlanId);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(tourPlanRepository).findById(tourPlanId);
        verify(tourPlanAvailabilityRepository).findByTourPlanAndPriceOverrideIsNotNull(tourPlanOneMock);
    }

    @Test
    void getAvailabilitiesWithPriceAbove_shouldReturnAvailabilitiesAbovePrice() {
        // Given
        Integer tourPlanId = 1;
        BigDecimal price = new BigDecimal("110.00");
        List<TourPlanAvailability> availabilities = tourPlanAvailabilityListsMock;

        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlanOneMock));
        when(tourPlanAvailabilityRepository.findByTourPlanAndPriceOverrideGreaterThan(tourPlanOneMock, price))
                .thenReturn(availabilities);

        // When
        List<TourPlanAvailabilityResponse> result = tourPlanAvailabilityService.getAvailabilitiesWithPriceAbove(tourPlanId, price);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(tourPlanRepository).findById(tourPlanId);
        verify(tourPlanAvailabilityRepository).findByTourPlanAndPriceOverrideGreaterThan(tourPlanOneMock, price);
    }

    @Test
    void deleteAllAvailabilitiesByTourPlanId_shouldDeleteAllAvailabilities() {
        // Given
        Integer tourPlanId = 1;

        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlanOneMock));

        // When
        tourPlanAvailabilityService.deleteAllAvailabilitiesByTourPlanId(tourPlanId);

        // Then
        verify(tourPlanRepository).findById(tourPlanId);
        verify(tourPlanAvailabilityRepository).deleteByTourPlan(tourPlanOneMock);
    }

    @Test
    void existsAvailabilityForTourPlanAndDate_shouldReturnTrue_whenExists() {
        // Given
        Integer tourPlanId = 1;

        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlanOneMock));
        when(tourPlanAvailabilityRepository.existsByTourPlanAndAvailableDate(tourPlanOneMock, LocalDate.now().plusDays(1)))
                .thenReturn(true);

        // When
        boolean result = tourPlanAvailabilityService.existsAvailabilityForTourPlanAndDate(tourPlanId, LocalDate.now().plusDays(1));

        // Then
        assertTrue(result);
        verify(tourPlanRepository).findById(tourPlanId);
        verify(tourPlanAvailabilityRepository).existsByTourPlanAndAvailableDate(tourPlanOneMock, LocalDate.now().plusDays(1));
    }

    @Test
    void existsAvailabilityForTourPlanAndDate_shouldReturnFalse_whenNotExists() {
        // Given
        Integer tourPlanId = 1;
        LocalDate date = LocalDate.now().plusMonths(1);

        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlanOneMock));
        when(tourPlanAvailabilityRepository.existsByTourPlanAndAvailableDate(tourPlanOneMock, date))
                .thenReturn(false);

        // When
        boolean result = tourPlanAvailabilityService.existsAvailabilityForTourPlanAndDate(tourPlanId, date);

        // Then
        assertFalse(result);
        verify(tourPlanRepository).findById(tourPlanId);
        verify(tourPlanAvailabilityRepository).existsByTourPlanAndAvailableDate(tourPlanOneMock, date);
    }

    @Test
    void findTourPlanOrFail_shouldThrowException_whenTourPlanNotFound() {
        // Given
        Integer tourPlanId = 999;
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> tourPlanAvailabilityService.getTourPlanAvailabilitiesByTourPlanId(tourPlanId));
    }
}
