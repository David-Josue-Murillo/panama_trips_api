package com.app.panama_trips.service;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.repository.ProviderRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.presentation.dto.TourPlanResponse;
import com.app.panama_trips.service.implementation.TourPlanService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

import static com.app.panama_trips.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourPlanServiceTest {
    @Mock
    private TourPlanRepository tourPlanRepository;

    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private TourPlanService tourPlanService;

    @Test
    void getAllTourPlan_shouldReturnAllTourPlan() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<TourPlan> page = new PageImpl<>(tourPlanListsMock, pageable, tourPlanListsMock.size());
        when(tourPlanRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<TourPlanResponse> result = tourPlanService.getAllTourPlan(pageable);

        // Then
        assertNotNull(result);
        assertEquals(10, result.getSize());
        assertEquals(tourPlanListsMock.size(), result.getTotalElements());
    }

    @Test
    void getTourPlanById_shouldReturnTourPlan() {
        // Given
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.of(tourPlanOneMock));

        // When
        TourPlanResponse result = tourPlanService.getTourPlanById(1);

        // Then
        assertNotNull(result);
        assertEquals(tourPlanOneMock.getId(), result.id());
        assertEquals(tourPlanOneMock.getTitle(), result.title());
        assertEquals(tourPlanOneMock.getDescription(), result.description());
        assertEquals(tourPlanOneMock.getPrice(), result.price());
    }

    @Test
    void getTourPlanById_shouldThrowResourceNotFoundException() {
        // Given
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> tourPlanService.getTourPlanById(1));

        // Then
        assertEquals("Tour Plan with id 1 not found", exception.getMessage());
    }

    @Test
    void saveTourPlan_shouldReturnTourPlan() {
        // Given
        when(tourPlanRepository.existsByTitleIgnoreCase(anyString())).thenReturn(false);
        when(providerRepository.existsById(anyInt())).thenReturn(true);
        when(providerRepository.findById(anyInt())).thenReturn(Optional.of(DataProvider.providerOneMock));
        when(tourPlanRepository.save(any(TourPlan.class))).thenReturn(tourPlanOneMock);

        // When
        TourPlanResponse result = tourPlanService.saveTourPlan(tourPlanRequestMock);

        // Then
        assertNotNull(result);
        assertEquals(tourPlanOneMock.getId(), result.id());
        assertEquals(tourPlanOneMock.getTitle(), result.title());
        assertEquals(tourPlanOneMock.getDescription(), result.description());
        assertEquals(tourPlanOneMock.getPrice(), result.price());
    }

    @Test
    void saveTourPlan_shouldThrowExceptionWhenProviderNotExits() {
        // Given
        when(tourPlanRepository.existsByTitleIgnoreCase(anyString())).thenReturn(false);
        when(providerRepository.existsById(anyInt())).thenReturn(false);

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> tourPlanService.saveTourPlan(tourPlanRequestMock));

        // Then
        assertEquals("Provider with id 1 not found", exception.getMessage());
    }

    @Test
    void updateTourPlan_shouldReturnTourPlan() {
        // Given
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.of(tourPlanOneMock));
        when(providerRepository.findById(anyInt())).thenReturn(Optional.of(DataProvider.providerOneMock));
        when(tourPlanRepository.save(any(TourPlan.class))).thenReturn(tourPlanOneMock);

        // When
        TourPlanResponse result = tourPlanService.updateTourPlan(1, tourPlanRequestMock);

        // Then{
        assertNotNull(result);
        assertEquals(tourPlanOneMock.getId(), result.id());
        assertEquals(tourPlanOneMock.getTitle(), result.title());
        assertEquals(tourPlanOneMock.getDescription(), result.description());
        assertEquals(tourPlanOneMock.getPrice(), result.price());
    }

    @Test
    void updateTourPlan_shouldThrowExceptionWhenTourPlanNotExits() {
        // Given
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> tourPlanService.updateTourPlan(1, tourPlanRequestMock));

        // Then
        assertEquals("Tour Plan with id 1 not found", exception.getMessage());
    }

    @Test
    void deleteTourPlan_shouldDeleteTourPlan() {
        // Given
        when(tourPlanRepository.existsById(anyInt())).thenReturn(true);

        // When
        tourPlanService.deleteTourPlan(1);

        // Then
        verify(tourPlanRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void deleteTourPlan_shouldThrowExceptionWhenProviderNotExits() {
        // Given
        when(tourPlanRepository.existsById(anyInt())).thenReturn(false);

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> tourPlanService.deleteTourPlan(1));

        // Then
        assertEquals("Tour Plan with id 1 not found", exception.getMessage());
    }

    @Test
    void getTourPlanByTitle_shouldReturnTourPlan() {
        // Given
        when(tourPlanRepository.findByTitleIgnoreCase(anyString())).thenReturn(Optional.of(tourPlanOneMock));

        // When
        TourPlanResponse result = tourPlanService.getTourPlanByTitle("title");

        // Then
        assertNotNull(result);
        assertEquals(tourPlanOneMock.getId(), result.id());
        assertEquals(tourPlanOneMock.getTitle(), result.title());
        assertEquals(tourPlanOneMock.getDescription(), result.description());
        assertEquals(tourPlanOneMock.getPrice(), result.price());
    }

    @Test
    void getTourPlanByTitle_shouldThrowResourceNotFoundException() {
        // Given
        when(tourPlanRepository.findByTitleIgnoreCase(anyString())).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> tourPlanService.getTourPlanByTitle("title"));

        // Then
        assertEquals("Tour Plan with title title not found", exception.getMessage());
    }

    @Test
    void getTourPlanByPrice_shouldReturnTourPlan() {
        // Given
        when(tourPlanRepository.findByPrice(any())).thenReturn(tourPlanListsMock);

        // When
        var result = tourPlanService.getTourPlanByPrice(tourPlanOneMock.getPrice());

        // Then
        assertNotNull(result);
        assertEquals(tourPlanListsMock.size(), result.size());
    }

    @Test
    void getTourPlanByPriceBetween_shouldReturnTourPlan() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<TourPlan> page = new PageImpl<>(tourPlanListsMock, pageable, tourPlanListsMock.size());
        when(tourPlanRepository.findByPriceBetween(any(), any(), any())).thenReturn(page);

        // When
        var result = tourPlanService.getTourPlanByPriceBetween(tourPlanOneMock.getPrice(), tourPlanTwoMock.getPrice(), pageable);

        // Then
        assertNotNull(result);
        assertEquals(tourPlanListsMock.size(), result.getTotalElements());
    }

    @Test
    void getTourPlanByDuration_shouldReturnTourPlan() {
        // Given
        when(tourPlanRepository.findByDuration(anyInt())).thenReturn(tourPlanListsMock);

        // When
        var result = tourPlanService.getTourPlanByDuration(1);

        // Then
        assertNotNull(result);
        assertEquals(tourPlanListsMock.size(), result.size());
    }

    @Test
    void getTourPlanByDurationBetween_shouldReturnTourPlan() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<TourPlan> page = new PageImpl<>(tourPlanListsMock, pageable, tourPlanListsMock.size());
        when(tourPlanRepository.findByDurationBetween(anyInt(), anyInt(), any())).thenReturn(page);

        // When
        var result = tourPlanService.getTourPlanByDurationBetween(1, 2, pageable);

        // Then
        assertNotNull(result);
        assertEquals(tourPlanListsMock.size(), result.getTotalElements());
    }

    @Test
    void getTourPlanByAvailableSpots_shouldReturnTourPlan() {
        // Given
        when(tourPlanRepository.findByAvailableSpots(anyInt())).thenReturn(tourPlanListsMock);

        // When
        var result = tourPlanService.getTourPlanByAvailableSpots(1);

        // Then
        assertNotNull(result);
        assertEquals(tourPlanListsMock.size(), result.size());
    }

    @Test
    void getTourPlanByAvailableSpotsBetween_shouldReturnTourPlan() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<TourPlan> page = new PageImpl<>(tourPlanListsMock, pageable, tourPlanListsMock.size());
        when(tourPlanRepository.findByAvailableSpotsBetween(anyInt(), anyInt(), any())).thenReturn(page);

        // When
        var result = tourPlanService.getTourPlanByAvailableSpotsBetween(1, 2, pageable);

        // Then
        assertNotNull(result);
        assertEquals(tourPlanListsMock.size(), result.getTotalElements());
    }

    @Test
    void getTourPlanByProviderId_shouldReturnTourPlan() {
        // Given
        when(tourPlanRepository.findByProvider_Id(anyInt())).thenReturn(tourPlanListsMock);

        // When
        var result = tourPlanService.getTourPlanByProviderId(1);

        // Then
        assertNotNull(result);
        assertEquals(tourPlanListsMock.size(), result.size());
    }

    @Test
    void getTourPlanByTitleAndPrice_shouldReturnTourPlan() {
        // Given
        when(tourPlanRepository.findByTitleContainingIgnoreCaseAndPrice(anyString(), any())).thenReturn(tourPlanListsMock);

        // When
        var result = tourPlanService.getTourPlanByTitleAndPrice("title", tourPlanOneMock.getPrice());

        // Then
        assertNotNull(result);
        assertEquals(tourPlanListsMock.size(), result.size());
    }

    @Test
    void getTourPlanByTitleAndPriceBetween_shouldReturnTourPlan() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<TourPlan> page = new PageImpl<>(tourPlanListsMock, pageable, tourPlanListsMock.size());
        when(tourPlanRepository.findByTitleContainingIgnoreCaseAndPriceBetween(anyString(), any(), any(), any())).thenReturn(page);

        // When
        var result = tourPlanService.getTourPlanByTitleAndPriceBetween("title", tourPlanOneMock.getPrice(), tourPlanTwoMock.getPrice(), pageable);

        // Then
        assertNotNull(result);
        assertEquals(tourPlanListsMock.size(), result.getTotalElements());
    }

    @Test
    void getTourPlanByTitleAndPriceBetweenAndDurationBetween_shouldReturnTourPlan() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<TourPlan> page = new PageImpl<>(tourPlanListsMock, pageable, tourPlanListsMock.size());
        when(tourPlanRepository.findByTitleContainingIgnoreCaseAndPriceBetweenAndDurationBetween(anyString(), any(), any(), anyInt(), anyInt(), any())).thenReturn(page);

        // When
        var result = tourPlanService.getTourPlanByTitleAndPriceBetweenAndDurationBetween("title", tourPlanOneMock.getPrice(), tourPlanTwoMock.getPrice(), 1, 2, pageable);

        // Then
        assertNotNull(result);
        assertEquals(tourPlanListsMock.size(), result.getTotalElements());
    }

    @Test
    void getTop10TourPlanByTitleContaining_shouldReturnTourPlan() {
        // Given
        when(tourPlanRepository.findTop10ByTitleContainingIgnoreCaseOrderByTitleAsc(anyString(), any(Pageable.class))).thenReturn(tourPlanListsMock);

        // When
        var result = tourPlanService.getTop10TourPlanByTitleContaining("title", PageRequest.of(0, 10));

        // Then
        assertNotNull(result);
        assertEquals(tourPlanListsMock.size(), result.size());
    }

    @Test
    void existsTourPlanByTitle_shouldReturnTrue() {
        // Given
        when(tourPlanRepository.existsByTitleIgnoreCase(anyString())).thenReturn(true);

        // When
        var result = tourPlanService.existsTourPlanByTitle("title");

        // Then
        assertTrue(result);
    }

    @Test
    void countTourPlan_shouldReturnCount() {
        // Given
        when(tourPlanRepository.count()).thenReturn(10L);

        // When
        var result = tourPlanService.countTourPlan();

        // Then
        assertEquals(10, result);
    }
}
