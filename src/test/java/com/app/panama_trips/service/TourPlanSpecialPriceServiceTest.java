package com.app.panama_trips.service;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourPlanSpecialPrice;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.persistence.repository.TourPlanSpecialPriceRepository;
import com.app.panama_trips.presentation.dto.TourPlanSpecialPriceRequest;
import com.app.panama_trips.presentation.dto.TourPlanSpecialPriceResponse;
import com.app.panama_trips.service.implementation.TourPlanSpecialPriceService;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.app.panama_trips.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourPlanSpecialPriceServiceTest {

    @Mock
    private TourPlanSpecialPriceRepository tourPlanSpecialPriceRepository;

    @Mock
    private TourPlanRepository tourPlanRepository;

    @InjectMocks
    private TourPlanSpecialPriceService service;

    private TourPlan tourPlan;
    private TourPlanSpecialPrice specialPrice1, specialPrice2;
    private TourPlanSpecialPriceRequest validRequest;
    private LocalDate today, tomorrow, nextWeek;

    @BeforeEach
    void setUp() {
        // Set up dates for testing
        today = LocalDate.now();
        tomorrow = today.plusDays(1);
        nextWeek = today.plusDays(7);

        // Set up tour plan
        tourPlan = tourPlanOneMock;

        // Set up special prices
        specialPrice1 = tourPlanSpecialPriceOneMock;
        specialPrice2 = tourPlanSpecialPriceTwoMock;

        // Set up valid request
        validRequest = tourPlanSpecialPriceRequestMock;
    }

    @Test
    void getAll_shouldReturnPageOfSpecialPrices() {
        // Given
        List<TourPlanSpecialPrice> specialPrices = Arrays.asList(specialPrice1, specialPrice2);
        Page<TourPlanSpecialPrice> specialPricePage = new PageImpl<>(specialPrices);
        Pageable pageable = PageRequest.of(0, 10);

        when(tourPlanSpecialPriceRepository.findAll(pageable)).thenReturn(specialPricePage);

        // When
        Page<TourPlanSpecialPriceResponse> result = service.getAll(pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(tourPlanSpecialPriceRepository).findAll(pageable);
    }

    @Test
    void findById_whenSpecialPriceExists_shouldReturnSpecialPrice() {
        // Given
        Integer id = 1;
        when(tourPlanSpecialPriceRepository.findById(id)).thenReturn(Optional.of(specialPrice1));

        // When
        TourPlanSpecialPriceResponse result = service.findById(id);

        // Then
        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals(specialPrice1.getPrice(), result.price());
        verify(tourPlanSpecialPriceRepository).findById(id);
    }

    @Test
    void findById_whenSpecialPriceDoesNotExist_shouldThrowException() {
        // Given
        Integer id = 999;
        when(tourPlanSpecialPriceRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> service.findById(id));
        verify(tourPlanSpecialPriceRepository).findById(id);
    }

    @Test
    void save_withValidRequest_shouldSaveAndReturnSpecialPrice() {
        // Given
        when(tourPlanRepository.findById(tourPlan.getId())).thenReturn(Optional.of(tourPlan));
        when(tourPlanSpecialPriceRepository.findOverlappingPricePeriodsForTourPlan(
                tourPlan.getId(), validRequest.startDate(), validRequest.endDate()))
                .thenReturn(Collections.emptyList());
        when(tourPlanSpecialPriceRepository.save(any(TourPlanSpecialPrice.class))).thenReturn(specialPrice1);

        // When
        TourPlanSpecialPriceResponse result = service.save(validRequest);

        // Then
        assertNotNull(result);
        assertEquals(specialPrice1.getId(), result.id());
        assertEquals(specialPrice1.getPrice(), result.price());
        verify(tourPlanSpecialPriceRepository).findOverlappingPricePeriodsForTourPlan(
                tourPlan.getId(), validRequest.startDate(), validRequest.endDate());
        verify(tourPlanSpecialPriceRepository).save(any(TourPlanSpecialPrice.class));
    }

    @Test
    void save_whenTourPlanDoesNotExist_shouldThrowException() {
        // Given
        when(tourPlanRepository.findById(tourPlan.getId())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> service.save(validRequest));
        verify(tourPlanRepository).findById(tourPlan.getId());
        verify(tourPlanSpecialPriceRepository, never()).save(any(TourPlanSpecialPrice.class));
    }

    @Test
    void save_withOverlappingDateRange_shouldThrowException() {
        // Given
        when(tourPlanRepository.findById(tourPlan.getId())).thenReturn(Optional.of(tourPlan));
        when(tourPlanSpecialPriceRepository.findOverlappingPricePeriodsForTourPlan(
                tourPlan.getId(), validRequest.startDate(), validRequest.endDate()))
                .thenReturn(Collections.singletonList(specialPrice2));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> service.save(validRequest));
        verify(tourPlanRepository).findById(tourPlan.getId());
        verify(tourPlanSpecialPriceRepository).findOverlappingPricePeriodsForTourPlan(
                tourPlan.getId(), validRequest.startDate(), validRequest.endDate());
        verify(tourPlanSpecialPriceRepository, never()).save(any(TourPlanSpecialPrice.class));
    }

    @Test
    void save_withPriceHigherThanRegular_shouldThrowException() {
        // Given
        TourPlanSpecialPriceRequest invalidRequest = new TourPlanSpecialPriceRequest(
                tourPlan.getId(),
                tomorrow,
                nextWeek,
                new BigDecimal("150.00"), // Higher than regular price (100.00)
                "Premium special"
        );

        when(tourPlanRepository.findById(tourPlan.getId())).thenReturn(Optional.of(tourPlan));
        when(tourPlanSpecialPriceRepository.findOverlappingPricePeriodsForTourPlan(
                tourPlan.getId(), invalidRequest.startDate(), invalidRequest.endDate()))
                .thenReturn(Collections.emptyList());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> service.save(invalidRequest));
        verify(tourPlanRepository).findById(tourPlan.getId());
        verify(tourPlanSpecialPriceRepository).findOverlappingPricePeriodsForTourPlan(
                tourPlan.getId(), invalidRequest.startDate(), invalidRequest.endDate());
        verify(tourPlanSpecialPriceRepository, never()).save(any(TourPlanSpecialPrice.class));
    }

    @Test
    void save_withDateRangeTooLong_shouldThrowException() {
        // Given
        TourPlanSpecialPriceRequest invalidRequest = new TourPlanSpecialPriceRequest(
                tourPlan.getId(),
                tomorrow,
                tomorrow.plusDays(100), // More than 90 days
                new BigDecimal("80.00"),
                "Long term special"
        );

        when(tourPlanRepository.findById(tourPlan.getId())).thenReturn(Optional.of(tourPlan));
        when(tourPlanSpecialPriceRepository.findOverlappingPricePeriodsForTourPlan(
                tourPlan.getId(), invalidRequest.startDate(), invalidRequest.endDate()))
                .thenReturn(Collections.emptyList());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> service.save(invalidRequest));
        verify(tourPlanRepository).findById(tourPlan.getId());
        verify(tourPlanSpecialPriceRepository).findOverlappingPricePeriodsForTourPlan(
                tourPlan.getId(), invalidRequest.startDate(), invalidRequest.endDate());
        verify(tourPlanSpecialPriceRepository, never()).save(any(TourPlanSpecialPrice.class));
    }

    @Test
    void update_withValidRequest_shouldUpdateAndReturnSpecialPrice() {
        // Given
        Integer id = 1;
        TourPlanSpecialPriceRequest updateRequest = new TourPlanSpecialPriceRequest(
                tourPlan.getId(),
                tomorrow,
                nextWeek,
                new BigDecimal("75.00"), // Updated price
                "Updated special"
        );

        when(tourPlanSpecialPriceRepository.findById(id)).thenReturn(Optional.of(specialPrice1));
        when(tourPlanRepository.findById(tourPlan.getId())).thenReturn(Optional.of(tourPlan));
        when(tourPlanSpecialPriceRepository.save(any(TourPlanSpecialPrice.class))).thenAnswer(invocation -> {
            TourPlanSpecialPrice updated = invocation.getArgument(0);
            return updated;
        });

        // When
        TourPlanSpecialPriceResponse result = service.update(id, updateRequest);

        // Then
        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals(updateRequest.price(), result.price());
        assertEquals(updateRequest.description(), result.description());
        verify(tourPlanSpecialPriceRepository).findById(id);
        verify(tourPlanRepository).findById(tourPlan.getId());
        verify(tourPlanSpecialPriceRepository).save(any(TourPlanSpecialPrice.class));
    }

    @Test
    void update_whenSpecialPriceDoesNotExist_shouldThrowException() {
        // Given
        Integer id = 999;
        when(tourPlanSpecialPriceRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> service.update(id, validRequest));
        verify(tourPlanSpecialPriceRepository).findById(id);
        verify(tourPlanSpecialPriceRepository, never()).save(any(TourPlanSpecialPrice.class));
    }

    @Test
    void deleteById_whenSpecialPriceExists_shouldDelete() {
        // Given
        Integer id = 1;
        when(tourPlanSpecialPriceRepository.existsById(id)).thenReturn(true);

        // When
        service.deleteById(id);

        // Then
        verify(tourPlanSpecialPriceRepository).existsById(id);
        verify(tourPlanSpecialPriceRepository).deleteById(id);
    }

    @Test
    void deleteById_whenSpecialPriceDoesNotExist_shouldThrowException() {
        // Given
        Integer id = 999;
        when(tourPlanSpecialPriceRepository.existsById(id)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> service.deleteById(id));
        verify(tourPlanSpecialPriceRepository).existsById(id);
        verify(tourPlanSpecialPriceRepository, never()).deleteById(id);
    }

    @Test
    void findByTourPlan_shouldReturnSpecialPrices() {
        // Given
        List<TourPlanSpecialPrice> specialPrices = Arrays.asList(specialPrice1, specialPrice2);
        when(tourPlanSpecialPriceRepository.findByTourPlan(tourPlan)).thenReturn(specialPrices);

        // When
        List<TourPlanSpecialPriceResponse> result = service.findByTourPlan(tourPlan);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tourPlanSpecialPriceRepository).findByTourPlan(tourPlan);
    }

    @Test
    void findByTourPlanOrderByStartDateAsc_shouldReturnOrderedSpecialPrices() {
        // Given
        List<TourPlanSpecialPrice> specialPrices = Arrays.asList(specialPrice1, specialPrice2);
        when(tourPlanSpecialPriceRepository.findByTourPlanOrderByStartDateAsc(tourPlan)).thenReturn(specialPrices);

        // When
        List<TourPlanSpecialPriceResponse> result = service.findByTourPlanOrderByStartDateAsc(tourPlan);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tourPlanSpecialPriceRepository).findByTourPlanOrderByStartDateAsc(tourPlan);
    }

    @Test
    void findByStartDateBetween_shouldReturnSpecialPricesBetweenDates() {
        // Given
        LocalDate start = today;
        LocalDate end = tomorrow.plusDays(3);
        List<TourPlanSpecialPrice> specialPrices = Collections.singletonList(specialPrice1);

        when(tourPlanSpecialPriceRepository.findByStartDateBetween(start, end)).thenReturn(specialPrices);

        // When
        List<TourPlanSpecialPriceResponse> result = service.findByStartDateBetween(start, end);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tourPlanSpecialPriceRepository).findByStartDateBetween(start, end);
    }

    @Test
    void findByEndDateBetween_shouldReturnSpecialPricesBetweenDates() {
        // Given
        LocalDate start = nextWeek.minusDays(1);
        LocalDate end = nextWeek.plusDays(1);
        List<TourPlanSpecialPrice> specialPrices = Collections.singletonList(specialPrice1);

        when(tourPlanSpecialPriceRepository.findByEndDateBetween(start, end)).thenReturn(specialPrices);

        // When
        List<TourPlanSpecialPriceResponse> result = service.findByEndDateBetween(start, end);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tourPlanSpecialPriceRepository).findByEndDateBetween(start, end);
    }

    @Test
    void findByTourPlanIdAndDate_whenSpecialPriceExists_shouldReturnSpecialPrice() {
        // Given
        Integer tourPlanId = 1;
        LocalDate date = tomorrow.plusDays(1);

        when(tourPlanSpecialPriceRepository.findByTourPlanIdAndDate(tourPlanId, date))
                .thenReturn(Optional.of(specialPrice1));

        // When
        Optional<TourPlanSpecialPriceResponse> result = service.findByTourPlanIdAndDate(tourPlanId, date);

        // Then
        assertTrue(result.isPresent());
        assertEquals(specialPrice1.getId(), result.get().id());
        verify(tourPlanSpecialPriceRepository).findByTourPlanIdAndDate(tourPlanId, date);
    }

    @Test
    void findByTourPlanIdAndDate_whenNoSpecialPrice_shouldReturnEmpty() {
        // Given
        Integer tourPlanId = 1;
        LocalDate date = today.minusDays(1); // Before any special price

        when(tourPlanSpecialPriceRepository.findByTourPlanIdAndDate(tourPlanId, date))
                .thenReturn(Optional.empty());

        // When
        Optional<TourPlanSpecialPriceResponse> result = service.findByTourPlanIdAndDate(tourPlanId, date);

        // Then
        assertFalse(result.isPresent());
        verify(tourPlanSpecialPriceRepository).findByTourPlanIdAndDate(tourPlanId, date);
    }

    @Test
    void findOverlappingPricePeriodsForTourPlan_shouldReturnOverlappingPrices() {
        // Given
        Integer tourPlanId = 1;
        LocalDate start = today;
        LocalDate end = nextWeek.plusDays(3);
        List<TourPlanSpecialPrice> overlappingPrices = Collections.singletonList(specialPrice1);

        when(tourPlanSpecialPriceRepository.findOverlappingPricePeriodsForTourPlan(tourPlanId, start, end))
                .thenReturn(overlappingPrices);

        // When
        List<TourPlanSpecialPriceResponse> result = service.findOverlappingPricePeriodsForTourPlan(tourPlanId, start, end);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tourPlanSpecialPriceRepository).findOverlappingPricePeriodsForTourPlan(tourPlanId, start, end);
    }

    @Test
    void findByTourPlanAndPriceGreaterThan_shouldReturnMatchingPrices() {
        // Given
        BigDecimal price = new BigDecimal("75.00");
        List<TourPlanSpecialPrice> specialPrices = Collections.singletonList(specialPrice1);

        when(tourPlanSpecialPriceRepository.findByTourPlanAndPriceGreaterThan(tourPlan, price))
                .thenReturn(specialPrices);

        // When
        List<TourPlanSpecialPriceResponse> result = service.findByTourPlanAndPriceGreaterThan(tourPlan, price);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(tourPlanSpecialPriceRepository).findByTourPlanAndPriceGreaterThan(tourPlan, price);
    }

    @Test
    void findByTourPlanAndStartDateGreaterThanEqual_shouldReturnFuturePrices() {
        // Given
        LocalDate date = today;
        List<TourPlanSpecialPrice> specialPrices = Arrays.asList(specialPrice1, specialPrice2);

        when(tourPlanSpecialPriceRepository.findByTourPlanAndStartDateGreaterThanEqual(tourPlan, date))
                .thenReturn(specialPrices);

        // When
        List<TourPlanSpecialPriceResponse> result = service.findByTourPlanAndStartDateGreaterThanEqual(tourPlan, date);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tourPlanSpecialPriceRepository).findByTourPlanAndStartDateGreaterThanEqual(tourPlan, date);
    }

    @Test
    void deleteByTourPlan_shouldCallRepositoryMethod() {
        // When
        service.deleteByTourPlan(tourPlan);

        // Then
        verify(tourPlanSpecialPriceRepository).deleteByTourPlan(tourPlan);
    }

    @Test
    void existsByTourPlanAndStartDateAndEndDate_whenExists_shouldReturnTrue() {
        // Given
        when(tourPlanSpecialPriceRepository.existsByTourPlanAndStartDateAndEndDate(
                tourPlan, tomorrow, nextWeek)).thenReturn(true);

        // When
        boolean result = service.existsByTourPlanAndStartDateAndEndDate(tourPlan, tomorrow, nextWeek);

        // Then
        assertTrue(result);
        verify(tourPlanSpecialPriceRepository).existsByTourPlanAndStartDateAndEndDate(tourPlan, tomorrow, nextWeek);
    }

    @Test
    void existsByTourPlanAndStartDateAndEndDate_whenDoesNotExist_shouldReturnFalse() {
        // Given
        LocalDate nonExistingStart = nextWeek.plusDays(10);
        LocalDate nonExistingEnd = nextWeek.plusDays(15);

        when(tourPlanSpecialPriceRepository.existsByTourPlanAndStartDateAndEndDate(
                tourPlan, nonExistingStart, nonExistingEnd)).thenReturn(false);

        // When
        boolean result = service.existsByTourPlanAndStartDateAndEndDate(tourPlan, nonExistingStart, nonExistingEnd);

        // Then
        assertFalse(result);
        verify(tourPlanSpecialPriceRepository).existsByTourPlanAndStartDateAndEndDate(
                tourPlan, nonExistingStart, nonExistingEnd);
    }
}