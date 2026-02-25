package com.app.panama_trips.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
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
        var pageable = PageRequest.of(0, 10);
        var page = new PageImpl<>(tourPriceHistoryListMock());

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
        assertEquals("Tour price history not found with id 999", ex.getMessage());
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
        assertEquals("Tour plan not found with id " + request.tourPlanId(), ex.getMessage());
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
        assertEquals("User not found with id " + request.changedById(), ex.getMessage());
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
        assertEquals("Tour price history not found with id 999", ex.getMessage());
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
        assertEquals("Tour plan not found with id " + request.tourPlanId(), ex.getMessage());
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
        assertEquals("User not found with id " + request.changedById(), ex.getMessage());
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
        assertEquals("Tour price history not found with id 999", ex.getMessage());
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
        var pageable = PageRequest.of(0, 5);
        when(tourPlanRepository.existsById(1)).thenReturn(true);
        var page = new PageImpl<>(tourPriceHistoryListForTourPlanOneMock());
        when(repository.findByTourPlan_IdOrderByChangedAtDesc(1, pageable)).thenReturn(page);

        var result = service.findByTourPlanIdOrderByChangedAtDesc(1, pageable);

        assertNotNull(result);
        assertEquals(page.getTotalElements(), result.getTotalElements());
        verify(tourPlanRepository).existsById(1);
        verify(repository).findByTourPlan_IdOrderByChangedAtDesc(1, pageable);
    }

    @Test
    @DisplayName("Should throw when pageable find by tour plan and plan not found")
    void findByTourPlanIdOrderByChangedAtDesc_whenTourPlanNotFound_shouldThrow() {
        var pageable = PageRequest.of(0, 5);
        when(tourPlanRepository.existsById(999)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.findByTourPlanIdOrderByChangedAtDesc(999, pageable));
        assertEquals("Tour plan not found with id 999", ex.getMessage());
        verify(tourPlanRepository).existsById(999);
    }

    @Test
    @DisplayName("Should find by tour plan and date range")
    void findByTourPlanIdAndChangedAtBetween_shouldReturnList() {
        LocalDateTime start = LocalDateTime.now().minusDays(10);
        LocalDateTime end = LocalDateTime.now();
        when(tourPlanRepository.existsById(1)).thenReturn(true);
        when(repository.findByTourPlan_IdAndChangedAtBetween(1, start, end))
                .thenReturn(tourPriceHistoryListForTourPlanOneMock());

        List<TourPriceHistoryResponse> result = service.findByTourPlanIdAndChangedAtBetween(1, start, end);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(repository).findByTourPlan_IdAndChangedAtBetween(1, start, end);
    }

    @Test
    @DisplayName("Should throw when finding by date range and tour plan not found")
    void findByTourPlanIdAndChangedAtBetween_whenTourPlanNotFound_shouldThrow() {
        LocalDateTime start = LocalDateTime.now().minusDays(10);
        LocalDateTime end = LocalDateTime.now();
        when(tourPlanRepository.existsById(999)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.findByTourPlanIdAndChangedAtBetween(999, start, end));
        assertEquals("Tour plan not found with id 999", ex.getMessage());
        verify(tourPlanRepository).existsById(999);
    }

    @Test
    @DisplayName("Should find by changedBy user id")
    void findByChangedById_shouldReturnList() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(repository.findByChangedBy_Id(1L)).thenReturn(List.of(priceHistory1));

        List<TourPriceHistoryResponse> result = service.findByChangedById(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(userRepository).existsById(1L);
        verify(repository).findByChangedBy_Id(1L);
    }

    @Test
    @DisplayName("Should throw when user not found in findByChangedById")
    void findByChangedById_whenUserNotFound_shouldThrow() {
        when(userRepository.existsById(999L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.findByChangedById(999L));
        assertEquals("User not found with id 999", ex.getMessage());
        verify(userRepository).existsById(999L);
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
    @DisplayName("Should get recent changes with limit using paginated query")
    void getRecentChanges_shouldReturnLimitedList() {
        var list = tourPriceHistoryListOrderedByDateMock().subList(0, 3);
        var page = new PageImpl<>(list);
        when(repository.findAllByOrderByChangedAtDesc(PageRequest.of(0, 3))).thenReturn(page);

        List<TourPriceHistoryResponse> result = service.getRecentChanges(3);

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(repository).findAllByOrderByChangedAtDesc(PageRequest.of(0, 3));
    }

    @Test
    @DisplayName("Should get latest change for tour plan when exists")
    void getLatestChangeForTourPlan_whenExists_shouldReturnOptional() {
        when(repository.findFirstByTourPlan_IdOrderByChangedAtDesc(1))
                .thenReturn(Optional.of(priceHistory1));

        var result = service.getLatestChangeForTourPlan(1);

        assertTrue(result.isPresent());
        verify(repository).findFirstByTourPlan_IdOrderByChangedAtDesc(1);
    }

    @Test
    @DisplayName("Should return empty when no changes for tour plan")
    void getLatestChangeForTourPlan_whenEmpty_shouldReturnEmpty() {
        when(repository.findFirstByTourPlan_IdOrderByChangedAtDesc(2)).thenReturn(Optional.empty());

        var result = service.getLatestChangeForTourPlan(2);

        assertTrue(result.isEmpty());
        verify(repository).findFirstByTourPlan_IdOrderByChangedAtDesc(2);
    }

    @Test
    @DisplayName("Should get current price from latest history if exists")
    void getCurrentPriceForTourPlan_whenHistoryExists_shouldReturnNewPrice() {
        when(repository.findFirstByTourPlan_IdOrderByChangedAtDesc(1))
                .thenReturn(Optional.of(priceHistory1));

        BigDecimal result = service.getCurrentPriceForTourPlan(1);

        assertNotNull(result);
        assertEquals(priceHistory1.getNewPrice(), result);
        verify(repository).findFirstByTourPlan_IdOrderByChangedAtDesc(1);
    }

    @Test
    @DisplayName("Should fallback to tour plan base price when no history exists")
    void getCurrentPriceForTourPlan_whenNoHistory_shouldReturnPlanPrice() {
        when(repository.findFirstByTourPlan_IdOrderByChangedAtDesc(1)).thenReturn(Optional.empty());
        when(tourPlanRepository.findById(1)).thenReturn(Optional.of(tourPlan));

        BigDecimal result = service.getCurrentPriceForTourPlan(1);

        assertEquals(tourPlan.getPrice(), result);
        verify(tourPlanRepository).findById(1);
    }

    @Test
    @DisplayName("Should throw when getting current price and plan not found")
    void getCurrentPriceForTourPlan_whenPlanNotFound_shouldThrow() {
        when(repository.findFirstByTourPlan_IdOrderByChangedAtDesc(999)).thenReturn(Optional.empty());
        when(tourPlanRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.getCurrentPriceForTourPlan(999));
        assertEquals("Tour plan not found with id 999", ex.getMessage());
        verify(tourPlanRepository).findById(999);
    }

    @Test
    @DisplayName("Should get previous price for tour plan when history exists")
    void getPreviousPriceForTourPlan_shouldReturnPreviousPrice() {
        when(repository.findFirstByTourPlan_IdOrderByChangedAtDesc(1))
                .thenReturn(Optional.of(priceHistory1));

        BigDecimal result = service.getPreviousPriceForTourPlan(1);

        assertNotNull(result);
        assertEquals(priceHistory1.getPreviousPrice(), result);
        verify(repository).findFirstByTourPlan_IdOrderByChangedAtDesc(1);
    }

    @Test
    @DisplayName("Should throw when getting previous price and no history")
    void getPreviousPriceForTourPlan_whenEmpty_shouldThrow() {
        when(repository.findFirstByTourPlan_IdOrderByChangedAtDesc(1)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.getPreviousPriceForTourPlan(1));
        assertEquals("No price history found for tour plan with id 1", ex.getMessage());
        verify(repository).findFirstByTourPlan_IdOrderByChangedAtDesc(1);
    }

    @Test
    @DisplayName("Should get max price for tour plan using aggregate query")
    void getMaxPriceForTourPlan_shouldReturnMax() {
        BigDecimal expectedMax = BigDecimal.valueOf(350);
        when(repository.findMaxNewPriceByTourPlanId(2)).thenReturn(expectedMax);

        BigDecimal result = service.getMaxPriceForTourPlan(2);

        assertNotNull(result);
        assertEquals(expectedMax, result);
        verify(repository).findMaxNewPriceByTourPlanId(2);
    }

    @Test
    @DisplayName("Should throw when getting max price and no history")
    void getMaxPriceForTourPlan_whenEmpty_shouldThrow() {
        when(repository.findMaxNewPriceByTourPlanId(1)).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.getMaxPriceForTourPlan(1));
        assertEquals("No price history found for tour plan with id 1", ex.getMessage());
        verify(repository).findMaxNewPriceByTourPlanId(1);
    }

    @Test
    @DisplayName("Should get min price for tour plan using aggregate query")
    void getMinPriceForTourPlan_shouldReturnMin() {
        BigDecimal expectedMin = BigDecimal.valueOf(95);
        when(repository.findMinNewPriceByTourPlanId(2)).thenReturn(expectedMin);

        BigDecimal result = service.getMinPriceForTourPlan(2);

        assertNotNull(result);
        assertEquals(expectedMin, result);
        verify(repository).findMinNewPriceByTourPlanId(2);
    }

    @Test
    @DisplayName("Should throw when getting min price and no history")
    void getMinPriceForTourPlan_whenEmpty_shouldThrow() {
        when(repository.findMinNewPriceByTourPlanId(1)).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.getMinPriceForTourPlan(1));
        assertEquals("No price history found for tour plan with id 1", ex.getMessage());
        verify(repository).findMinNewPriceByTourPlanId(1);
    }

    @Test
    @DisplayName("Should get average price for tour plan using aggregate query")
    void getAveragePriceForTourPlan_shouldReturnAverage() {
        when(repository.findAvgNewPriceByTourPlanId(1)).thenReturn(110.50);

        BigDecimal result = service.getAveragePriceForTourPlan(1);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(110.50).setScale(2), result);
        verify(repository).findAvgNewPriceByTourPlanId(1);
    }

    @Test
    @DisplayName("Should throw when getting average price and no history")
    void getAveragePriceForTourPlan_whenEmpty_shouldThrow() {
        when(repository.findAvgNewPriceByTourPlanId(1)).thenReturn(null);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.getAveragePriceForTourPlan(1));
        assertEquals("No price history found for tour plan with id 1", ex.getMessage());
        verify(repository).findAvgNewPriceByTourPlanId(1);
    }

    @Test
    @DisplayName("Should get price changes on specific date using date range query")
    void getPriceChangesOnDate_shouldFilterByDate() {
        LocalDate targetDate = LocalDate.now();
        LocalDateTime startOfDay = targetDate.atStartOfDay();
        LocalDateTime endOfDay = targetDate.atTime(LocalTime.MAX);
        when(repository.findByTourPlan_IdAndChangedAtBetween(1, startOfDay, endOfDay))
                .thenReturn(tourPriceHistoryListRecentMock());

        List<TourPriceHistoryResponse> result = service.getPriceChangesOnDate(1, targetDate);

        assertNotNull(result);
        verify(repository).findByTourPlan_IdAndChangedAtBetween(1, startOfDay, endOfDay);
    }

    @Test
    @DisplayName("Should get price changes by user and date range using optimized query")
    void getPriceChangesByUserAndDateRange_shouldReturnFilteredList() {
        LocalDateTime start = LocalDateTime.now().minusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(repository.findByChangedBy_IdAndChangedAtBetween(1L, start, end))
                .thenReturn(tourPriceHistoryListRecentMock());

        List<TourPriceHistoryResponse> result = service.getPriceChangesByUserAndDateRange(1L, start, end);

        assertNotNull(result);
        verify(userRepository).existsById(1L);
        verify(repository).findByChangedBy_IdAndChangedAtBetween(1L, start, end);
    }

    @Test
    @DisplayName("Should throw when user not found in getPriceChangesByUserAndDateRange")
    void getPriceChangesByUserAndDateRange_whenUserNotFound_shouldThrow() {
        when(userRepository.existsById(999L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.getPriceChangesByUserAndDateRange(999L, LocalDateTime.now().minusDays(1),
                        LocalDateTime.now()));
        assertEquals("User not found with id 999", ex.getMessage());
        verify(userRepository).existsById(999L);
    }

    @Test
    @DisplayName("Should bulk create tour price histories")
    void bulkCreateTourPriceHistories_success() {
        var requests = tourPriceHistoryRequestListForBulkCreateMock();
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.of(tourPlan));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(userAdmin()));

        service.bulkCreateTourPriceHistories(requests);

        verify(tourPlanRepository, times(requests.size())).findById(anyInt());
        verify(repository).saveAll(argThat(iterable -> {
            int count = 0;
            Iterator<?> it = iterable.iterator();
            while (it.hasNext()) {
                it.next();
                count++;
            }
            return count == requests.size();
        }));
    }

    @Test
    @DisplayName("Should bulk delete tour price histories")
    void bulkDeleteTourPriceHistories_success() {
        List<Integer> ids = tourPriceHistoryIdsForBulkDeleteMock();

        service.bulkDeleteTourPriceHistories(ids);

        verify(repository).deleteAllById(ids);
    }

    @Test
    @DisplayName("Should check existence by id")
    void existsById_shouldReturnBoolean() {
        when(repository.existsById(1)).thenReturn(true);
        when(repository.existsById(2)).thenReturn(false);

        assertTrue(service.existsById(1));
        assertFalse(service.existsById(2));
        verify(repository).existsById(1);
        verify(repository).existsById(2);
    }

    @Test
    @DisplayName("Should count changes by tour plan id (alias method)")
    void countByTourPlanId_shouldReturnCount() {
        when(repository.countPriceChangesByTourPlanId(1)).thenReturn(4L);
        assertEquals(4L, service.countByTourPlanId(1));
        verify(repository).countPriceChangesByTourPlanId(1);
    }

    @Test
    @DisplayName("Should get total price increase using aggregate query")
    void getTotalPriceIncreaseForTourPlan_shouldDelegate() {
        BigDecimal expectedIncrease = BigDecimal.valueOf(30);
        when(repository.sumPriceIncreaseByTourPlanId(1)).thenReturn(expectedIncrease);

        BigDecimal result = service.getTotalPriceIncreaseForTourPlan(1);

        assertNotNull(result);
        assertEquals(expectedIncrease, result);
        verify(repository).sumPriceIncreaseByTourPlanId(1);
    }

    @Test
    @DisplayName("Should get total price decrease using aggregate query")
    void getTotalPriceDecreaseForTourPlan_shouldDelegate() {
        BigDecimal expectedDecrease = BigDecimal.valueOf(10);
        when(repository.sumPriceDecreaseByTourPlanId(1)).thenReturn(expectedDecrease);

        BigDecimal result = service.getTotalPriceDecreaseForTourPlan(1);

        assertNotNull(result);
        assertEquals(expectedDecrease, result);
        verify(repository).sumPriceDecreaseByTourPlanId(1);
    }

    @Test
    @DisplayName("Should return average change percentage or zero when null")
    void getAverageChangePercentageForTourPlan_shouldHandleNull() {
        when(repository.calculateAveragePriceChangePercentageByTourPlanId(1)).thenReturn(null);
        double result = service.getAverageChangePercentageForTourPlan(1);
        assertEquals(0.0, result);
        verify(repository).calculateAveragePriceChangePercentageByTourPlanId(1);
    }

    @Test
    @DisplayName("Should get top tour plans by change count using optimized query")
    void getTopTourPlansByChangeCount_shouldReturnLatestOfTopPlans() {
        when(repository.findTopTourPlanIdsByChangeCount(PageRequest.of(0, 2)))
                .thenReturn(List.of(1, 2));
        when(repository.findFirstByTourPlan_IdOrderByChangedAtDesc(1))
                .thenReturn(Optional.of(tourPriceHistoryOneMock()));
        when(repository.findFirstByTourPlan_IdOrderByChangedAtDesc(2))
                .thenReturn(Optional.of(tourPriceHistoryThreeMock()));

        List<TourPriceHistoryResponse> result = service.getTopTourPlansByChangeCount(2);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository).findTopTourPlanIdsByChangeCount(PageRequest.of(0, 2));
    }

    @Test
    @DisplayName("Should search changes by exact price using optimized query")
    void searchChangesByPrice_shouldReturnMatches() {
        BigDecimal price = BigDecimal.valueOf(120.00);
        when(repository.findByNewPriceOrPreviousPrice(price, price))
                .thenReturn(tourPriceHistoryListMock());

        List<TourPriceHistoryResponse> result = service.searchChangesByPrice(price);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(repository).findByNewPriceOrPreviousPrice(price, price);
    }
}
