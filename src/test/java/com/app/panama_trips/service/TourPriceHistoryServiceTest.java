package com.app.panama_trips.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourPriceHistory;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.persistence.repository.TourPriceHistoryRepository;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.presentation.dto.TourPriceHistoryRequest;
import com.app.panama_trips.presentation.dto.TourPriceHistoryResponse;
import com.app.panama_trips.service.implementation.TourPriceHistoryService;

import static com.app.panama_trips.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TourPriceHistoryServiceTest {

    @Mock
    private TourPriceHistoryRepository repository;

    @Mock
    private TourPlanRepository tourPlanRepository;

    @Mock
    private UserEntityRepository userRepository;

    @InjectMocks
    private TourPriceHistoryService service;

    private TourPlan tourPlan;
    private TourPriceHistory priceHistory1;
    private TourPriceHistoryRequest request;

    private ArgumentCaptor<TourPriceHistory> entityCaptor;

    @BeforeEach
    void setUp() {
        tourPlan = tourPlanOneMock;
        priceHistory1 = tourPriceHistoryOneMock();
        request = tourPriceHistoryRequestMock;
        entityCaptor = ArgumentCaptor.forClass(TourPriceHistory.class);
    }

    // CRUD
    @Test
    @DisplayName("Should return all tour price histories with pagination")
    void getAllTourPriceHistories_shouldReturnPage() {
        var pageable = org.springframework.data.domain.PageRequest.of(0, 10);
        var page = new org.springframework.data.domain.PageImpl<>(tourPriceHistoryListMock());

        when(repository.findAll(pageable)).thenReturn(page);

        var result = service.getAllTourPriceHistories(pageable);

        assertNotNull(result);
        assertEquals(page.getTotalElements(), result.getTotalElements());
        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("Should get tour price history by id when exists")
    void getTourPriceHistoryById_whenExists_shouldReturn() {
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.of(priceHistory1));

        TourPriceHistoryResponse response = service.getTourPriceHistoryById(id);

        assertNotNull(response);
        assertEquals(priceHistory1.getId(), response.id());
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should throw when getting tour price history by id that does not exist")
    void getTourPriceHistoryById_whenNotExists_shouldThrow() {
        Integer id = 999;
        when(repository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.getTourPriceHistoryById(id));
        assertEquals("Tour price history not found", ex.getMessage());
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should save tour price history successfully")
    void saveTourPriceHistory_success() {
        when(tourPlanRepository.findById(request.tourPlanId())).thenReturn(Optional.of(tourPlan));
        when(userRepository.findById(request.changedById())).thenReturn(Optional.of(userAdmin()));
        when(repository.save(any(TourPriceHistory.class))).thenReturn(priceHistory1);

        TourPriceHistoryResponse response = service.saveTourPriceHistory(request);

        assertNotNull(response);
        verify(repository).save(entityCaptor.capture());
        TourPriceHistory saved = entityCaptor.getValue();
        assertEquals(request.tourPlanId(), saved.getTourPlan().getId());
        assertEquals(request.previousPrice(), saved.getPreviousPrice());
        assertEquals(request.newPrice(), saved.getNewPrice());
        assertEquals(request.changedById(), saved.getChangedBy().getId());
        assertEquals(request.reason(), saved.getReason());
    }

    @Test
    @DisplayName("Should throw when saving with non-existent tour plan")
    void saveTourPriceHistory_whenTourPlanNotFound_shouldThrow() {
        when(tourPlanRepository.findById(request.tourPlanId())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.saveTourPriceHistory(request));
        assertEquals("Tour plan not found", ex.getMessage());
        verify(tourPlanRepository).findById(request.tourPlanId());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw when saving with non-existent user")
    void saveTourPriceHistory_whenUserNotFound_shouldThrow() {
        when(tourPlanRepository.findById(request.tourPlanId())).thenReturn(Optional.of(tourPlan));
        when(userRepository.findById(request.changedById())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.saveTourPriceHistory(request));
        assertEquals("User not found", ex.getMessage());
        verify(userRepository).findById(request.changedById());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should update tour price history successfully")
    void updateTourPriceHistory_success() {
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.of(priceHistory1));
        when(tourPlanRepository.findById(request.tourPlanId())).thenReturn(Optional.of(tourPlan));
        when(userRepository.findById(request.changedById())).thenReturn(Optional.of(userOperator()));
        when(repository.save(any(TourPriceHistory.class))).thenReturn(priceHistory1);

        TourPriceHistoryResponse response = service.updateTourPriceHistory(id, request);

        assertNotNull(response);
        verify(repository).save(entityCaptor.capture());
        TourPriceHistory updated = entityCaptor.getValue();
        assertEquals(request.tourPlanId(), updated.getTourPlan().getId());
        assertEquals(request.previousPrice(), updated.getPreviousPrice());
        assertEquals(request.newPrice(), updated.getNewPrice());
        assertEquals(request.reason(), updated.getReason());
    }

    @Test
    @DisplayName("Should throw when updating non-existent tour price history")
    void updateTourPriceHistory_whenNotExists_shouldThrow() {
        Integer id = 999;
        when(repository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.updateTourPriceHistory(id, request));
        assertEquals("Tour price history not found", ex.getMessage());
        verify(repository).findById(id);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw when updating with non-existent tour plan")
    void updateTourPriceHistory_whenTourPlanNotFound_shouldThrow() {
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.of(priceHistory1));
        when(tourPlanRepository.findById(request.tourPlanId())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.updateTourPriceHistory(id, request));
        assertEquals("Tour plan not found", ex.getMessage());
        verify(tourPlanRepository).findById(request.tourPlanId());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw when updating with non-existent user")
    void updateTourPriceHistory_whenUserNotFound_shouldThrow() {
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.of(priceHistory1));
        when(tourPlanRepository.findById(request.tourPlanId())).thenReturn(Optional.of(tourPlan));
        when(userRepository.findById(request.changedById())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.updateTourPriceHistory(id, request));
        assertEquals("User not found", ex.getMessage());
        verify(userRepository).findById(request.changedById());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete tour price history when exists")
    void deleteTourPriceHistory_whenExists_success() {
        Integer id = 1;
        when(repository.existsById(id)).thenReturn(true);

        service.deleteTourPriceHistory(id);

        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw when deleting non-existent tour price history")
    void deleteTourPriceHistory_whenNotExists_shouldThrow() {
        Integer id = 999;
        when(repository.existsById(id)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.deleteTourPriceHistory(id));
        assertEquals("Tour price history not found", ex.getMessage());
        verify(repository).existsById(id);
        verify(repository, never()).deleteById(anyInt());
    }

    // Finders and calculations
    @Test
    @DisplayName("Should find by tour plan id ordered desc")
    void findByTourPlanId_shouldReturnList() {
        when(repository.findByTourPlanIdOrderByChangedAtDesc(1)).thenReturn(tourPriceHistoryListForTourPlanOneMock());

        List<TourPriceHistoryResponse> result = service.findByTourPlanId(1);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(repository).findByTourPlanIdOrderByChangedAtDesc(1);
    }

    @Test
    @DisplayName("Should return pageable list by tour plan ordered desc")
    void findByTourPlanIdOrderByChangedAtDesc_shouldReturnPage() {
        var pageable = org.springframework.data.domain.PageRequest.of(0, 5);
        when(tourPlanRepository.findById(1)).thenReturn(Optional.of(tourPlan));
        var page = new org.springframework.data.domain.PageImpl<>(tourPriceHistoryListForTourPlanOneMock());
        when(repository.findByTourPlanOrderByChangedAtDesc(tourPlan, pageable)).thenReturn(page);

        var result = service.findByTourPlanIdOrderByChangedAtDesc(1, pageable);

        assertNotNull(result);
        assertEquals(page.getTotalElements(), result.getTotalElements());
        verify(tourPlanRepository).findById(1);
        verify(repository).findByTourPlanOrderByChangedAtDesc(tourPlan, pageable);
    }

    @Test
    @DisplayName("Should throw when pageable find by tour plan and plan not found")
    void findByTourPlanIdOrderByChangedAtDesc_whenTourPlanNotFound_shouldThrow() {
        var pageable = org.springframework.data.domain.PageRequest.of(0, 5);
        when(tourPlanRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.findByTourPlanIdOrderByChangedAtDesc(999, pageable));
        assertEquals("Tour plan not found", ex.getMessage());
        verify(tourPlanRepository).findById(999);
    }

    @Test
    @DisplayName("Should find by tour plan and date range")
    void findByTourPlanIdAndChangedAtBetween_shouldReturnList() {
        LocalDateTime start = LocalDateTime.now().minusDays(10);
        LocalDateTime end = LocalDateTime.now();
        when(tourPlanRepository.findById(1)).thenReturn(Optional.of(tourPlan));
        when(repository.findByTourPlanAndChangedAtBetween(tourPlan, start, end))
                .thenReturn(tourPriceHistoryListForTourPlanOneMock());

        List<TourPriceHistoryResponse> result = service.findByTourPlanIdAndChangedAtBetween(1, start, end);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(repository).findByTourPlanAndChangedAtBetween(tourPlan, start, end);
    }

    @Test
    @DisplayName("Should throw when finding by date range and tour plan not found")
    void findByTourPlanIdAndChangedAtBetween_whenTourPlanNotFound_shouldThrow() {
        LocalDateTime start = LocalDateTime.now().minusDays(10);
        LocalDateTime end = LocalDateTime.now();
        when(tourPlanRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.findByTourPlanIdAndChangedAtBetween(999, start, end));
        assertEquals("Tour plan not found", ex.getMessage());
        verify(tourPlanRepository).findById(999);
    }

    @Test
    @DisplayName("Should find by changedBy user id")
    void findByChangedById_shouldReturnList() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userAdmin()));

        List<TourPriceHistoryResponse> result = service.findByChangedById(1L);

        assertNotNull(result);
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw when user not found in findByChangedById")
    void findByChangedById_whenUserNotFound_shouldThrow() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.findByChangedById(999L));
        assertEquals("User not found", ex.getMessage());
        verify(userRepository).findById(999L);
    }

    @Test
    @DisplayName("Should delegate average price change percentage calculation")
    void calculateAveragePriceChangePercentageByTourPlanId_shouldDelegate() {
        when(repository.calculateAveragePriceChangePercentageByTourPlanId(1)).thenReturn(12.5);
        Double result = service.calculateAveragePriceChangePercentageByTourPlanId(1);
        assertEquals(12.5, result);
        verify(repository).calculateAveragePriceChangePercentageByTourPlanId(1);
    }

    @Test
    @DisplayName("Should find by new price greater than")
    void findByNewPriceGreaterThan_shouldReturnList() {
        when(repository.findByNewPriceGreaterThan(BigDecimal.valueOf(200)))
                .thenReturn(tourPriceHistoryListForTourPlanTwoMock());

        List<TourPriceHistoryResponse> result = service.findByNewPriceGreaterThan(BigDecimal.valueOf(200));

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(repository).findByNewPriceGreaterThan(BigDecimal.valueOf(200));
    }

    @Test
    @DisplayName("Should count price changes by tour plan id")
    void countPriceChangesByTourPlanId_shouldReturnCount() {
        when(repository.countPriceChangesByTourPlanId(1)).thenReturn(5L);
        Long result = service.countPriceChangesByTourPlanId(1);
        assertEquals(5L, result);
        verify(repository).countPriceChangesByTourPlanId(1);
    }

    @Test
    @DisplayName("Should get recent changes with limit")
    void getRecentChanges_shouldReturnLimitedList() {
        when(repository.findAll(any(org.springframework.data.domain.Sort.class)))
                .thenReturn(tourPriceHistoryListOrderedByDateMock());

        List<TourPriceHistoryResponse> result = service.getRecentChanges(3);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(repository).findAll(any(org.springframework.data.domain.Sort.class));
    }

    @Test
    @DisplayName("Should get latest change for tour plan when exists")
    void getLatestChangeForTourPlan_whenExists_shouldReturnOptional() {
        when(repository.findByTourPlanIdOrderByChangedAtDesc(1))
                .thenReturn(tourPriceHistoryListForTourPlanOneMock());

        var result = service.getLatestChangeForTourPlan(1);

        assertTrue(result.isPresent());
        verify(repository).findByTourPlanIdOrderByChangedAtDesc(1);
    }
}
