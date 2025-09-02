package com.app.panama_trips.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.TourFaq;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.repository.TourFaqRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.presentation.dto.TourFaqRequest;
import com.app.panama_trips.presentation.dto.TourFaqResponse;
import com.app.panama_trips.service.implementation.TourFaqService;

import static com.app.panama_trips.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourFaqServiceTest {

    @Mock
    private TourFaqRepository repository;

    @Mock
    private TourPlanRepository tourPlanRepository;

    @InjectMocks
    private TourFaqService service;

    @Captor
    private ArgumentCaptor<TourFaq> tourFaqCaptor;

    private TourFaq tourFaq;
    private TourFaqRequest tourFaqRequest;
    private List<TourFaq> tourFaqList;
    private TourPlan tourPlan;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        tourPlan = tourPlanOneMock;
        tourFaq = tourFaqOneMock();
        tourFaqRequest = tourFaqRequestMock;
        tourFaqList = tourFaqListMock();
        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("Should return all FAQs when getAllFaqs is called with pagination")
    void getAllFaqs_shouldReturnAllData() {
        // Given
        Page<TourFaq> page = new PageImpl<>(tourFaqList);
        when(repository.findAll(pageable)).thenReturn(page);

        // When
        Page<TourFaqResponse> response = service.getAllFaqs(pageable);

        // Then
        assertNotNull(response);
        assertEquals(tourFaqList.size(), response.getTotalElements());
        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("Should return FAQ by id when exists")
    void getFaqById_whenExists_shouldReturnFaq() {
        // Given
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.of(tourFaq));

        // When
        TourFaqResponse result = service.getFaqById(id);

        // Then
        assertNotNull(result);
        assertEquals(tourFaq.getId(), result.id());
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should throw exception when FAQ not found")
    void getFaqById_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getFaqById(id));
        assertEquals("TourFaq not found with id: " + id, exception.getMessage());
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should create FAQ successfully")
    void saveFaq_success() {
        // Given
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.of(tourPlan));
        when(repository.findByTourPlan(any(TourPlan.class))).thenReturn(List.of());
        when(repository.save(any(TourFaq.class))).thenReturn(tourFaqOneMock());

        // When
        TourFaqResponse result = service.saveFaq(tourFaqRequest);

        // Then
        assertNotNull(result);
        assertEquals(tourFaq.getId(), result.id());
        verify(repository).save(tourFaqCaptor.capture());

        TourFaq savedFaq = tourFaqCaptor.getValue();
        assertEquals(tourFaqRequest.tourPlanId(), savedFaq.getTourPlan().getId());
        assertEquals(tourFaqRequest.question(), savedFaq.getQuestion());
        assertEquals(tourFaqRequest.answer(), savedFaq.getAnswer());
        assertEquals(tourFaqRequest.displayOrder(), savedFaq.getDisplayOrder());
    }

    @Test
    @DisplayName("Should throw exception when tour plan is not active")
    void saveFaq_whenTourPlanNotActive_shouldThrowException() {
        // Given
        TourPlan inactiveTourPlan = tourPlanTwoMock;
        inactiveTourPlan.setStatus("INACTIVE");
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.of(inactiveTourPlan));

        // When/Then
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.saveFaq(tourFaqRequest));
        assertEquals("Tour plan is not active", exception.getMessage());
        verify(repository, never()).save(any(TourFaq.class));
    }

    @Test
    @DisplayName("Should throw exception when display order is not unique")
    void saveFaq_whenDisplayOrderNotUnique_shouldThrowException() {
        // Given
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.of(tourPlan));
        when(repository.findByTourPlan(any(TourPlan.class))).thenReturn(List.of(tourFaqOneMock()));

        // When/Then
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.saveFaq(tourFaqRequest));
        assertEquals("Display order 1 is not unique within tour plan 1", exception.getMessage());
        verify(repository, never()).save(any(TourFaq.class));
    }

    @Test
    @DisplayName("Should throw exception when question already exists")
    void saveFaq_whenQuestionAlreadyExists_shouldThrowException() {
        // Given
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.of(tourPlan));
        when(repository.findByTourPlan(any(TourPlan.class))).thenReturn(List.of(tourFaqOneMock()));

        // When/Then
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.saveFaq(tourFaqRequest));
        assertEquals("Display order 1 is not unique within tour plan 1", exception.getMessage());
        verify(repository, never()).save(any(TourFaq.class));
    }

    @Test
    @DisplayName("Should update FAQ successfully")
    void updateFaq_success() {
        // Given
        Integer id = 1;
        TourFaqRequest updateRequest = new TourFaqRequest(
                2, // different tour plan
                "¿Pregunta actualizada?",
                "Respuesta actualizada",
                2);

        when(repository.findById(anyInt())).thenReturn(Optional.of(tourFaq));
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.of(tourPlanTwoMock));
        when(repository.save(any(TourFaq.class))).thenReturn(tourFaq);

        // When
        TourFaqResponse result = service.updateFaq(id, updateRequest);

        // Then
        assertNotNull(result);
        verify(repository).findById(id);
        verify(repository).save(tourFaqCaptor.capture());

        TourFaq updatedFaq = tourFaqCaptor.getValue();
        assertEquals(updateRequest.tourPlanId(), updatedFaq.getTourPlan().getId());
        assertEquals(updateRequest.question(), updatedFaq.getQuestion());
        assertEquals(updateRequest.answer(), updatedFaq.getAnswer());
        assertEquals(updateRequest.displayOrder(), updatedFaq.getDisplayOrder());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent FAQ")
    void updateFaq_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateFaq(id, tourFaqRequest));
        assertEquals("TourFaq not found with id: " + id, exception.getMessage());
        verify(repository, never()).save(any(TourFaq.class));
    }

    @Test
    @DisplayName("Should delete FAQ successfully")
    void deleteFaq_success() {
        // Given
        Integer id = 1;
        when(repository.existsById(id)).thenReturn(true);

        // When
        service.deleteFaq(id);

        // Then
        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent FAQ")
    void deleteFaq_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(repository.existsById(id)).thenReturn(false);

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteFaq(id));
        assertEquals("TourFaq not found with id: " + id, exception.getMessage());
        verify(repository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Should get FAQs by tour plan ID")
    void findByTourPlanId_shouldReturnFaqs() {
        // Given
        Integer tourPlanId = 1;
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlan));
        when(repository.findByTourPlan(any(TourPlan.class))).thenReturn(tourFaqListForTourPlanOneMock());

        // When
        List<TourFaqResponse> result = service.findByTourPlanId(tourPlanId);

        // Then
        assertNotNull(result);
        assertEquals(tourFaqListForTourPlanOneMock().size(), result.size());
        verify(tourPlanRepository).findById(tourPlanId);
        verify(repository).findByTourPlan(tourPlan);
    }

    @Test
    @DisplayName("Should throw exception when tour plan not found for findByTourPlanId")
    void findByTourPlanId_whenTourPlanNotFound_shouldThrowException() {
        // Given
        Integer tourPlanId = 999;
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.findByTourPlanId(tourPlanId));
        assertEquals("TourPlan not found with id: " + tourPlanId, exception.getMessage());
        verify(repository, never()).findByTourPlan(any(TourPlan.class));
    }

    @Test
    @DisplayName("Should get FAQs by tour plan ID ordered by display order")
    void findByTourPlanIdOrderByDisplayOrderAsc_shouldReturnOrderedFaqs() {
        // Given
        Integer tourPlanId = 1;
        when(repository.findByTourPlanIdOrderByDisplayOrder(tourPlanId.longValue()))
                .thenReturn(tourFaqListOrderedByDisplayOrderMock());

        // When
        List<TourFaqResponse> result = service.findByTourPlanIdOrderByDisplayOrderAsc(tourPlanId);

        // Then
        assertNotNull(result);
        assertEquals(tourFaqListOrderedByDisplayOrderMock().size(), result.size());
        verify(repository).findByTourPlanIdOrderByDisplayOrder(tourPlanId.longValue());
    }

    @Test
    @DisplayName("Should search FAQs by keyword")
    void searchByQuestionOrAnswer_shouldReturnMatchingFaqs() {
        // Given
        String keyword = "duración";
        when(repository.searchByKeyword(keyword)).thenReturn(tourFaqList);

        // When
        List<TourFaqResponse> result = service.searchByQuestionOrAnswer(keyword);

        // Then
        assertNotNull(result);
        assertEquals(tourFaqList.size(), result.size());
        verify(repository).searchByKeyword(keyword);
    }

    @Test
    @DisplayName("Should find FAQ by tour plan ID and question")
    void findByTourPlanIdAndQuestion_shouldReturnFaq() {
        // Given
        Integer tourPlanId = 1;
        String question = "¿Cuál es la duración del tour?";
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlan));
        when(repository.findByTourPlan(any(TourPlan.class))).thenReturn(List.of(tourFaqOneMock()));

        // When
        Optional<TourFaqResponse> result = service.findByTourPlanIdAndQuestion(tourPlanId, question);

        // Then
        assertTrue(result.isPresent());
        assertEquals(tourFaqOneMock().getId(), result.get().id());
        verify(tourPlanRepository).findById(tourPlanId);
        verify(repository).findByTourPlan(tourPlan);
    }

    @Test
    @DisplayName("Should return empty when FAQ not found by tour plan ID and question")
    void findByTourPlanIdAndQuestion_whenNotFound_shouldReturnEmpty() {
        // Given
        Integer tourPlanId = 1;
        String question = "¿Pregunta que no existe?";
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlan));
        when(repository.findByTourPlan(any(TourPlan.class))).thenReturn(List.of());

        // When
        Optional<TourFaqResponse> result = service.findByTourPlanIdAndQuestion(tourPlanId, question);

        // Then
        assertTrue(result.isEmpty());
        verify(tourPlanRepository).findById(tourPlanId);
        verify(repository).findByTourPlan(tourPlan);
    }

    @Test
    @DisplayName("Should get top FAQs by tour plan")
    void getTopFaqsByTourPlan_shouldReturnLimitedFaqs() {
        // Given
        Integer tourPlanId = 1;
        int limit = 2;
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlan));
        when(repository.findByTourPlanOrderByDisplayOrderAsc(any(TourPlan.class)))
                .thenReturn(tourFaqListForTourPlanOneMock());

        // When
        List<TourFaqResponse> result = service.getTopFaqsByTourPlan(tourPlanId, limit);

        // Then
        assertNotNull(result);
        assertEquals(limit, result.size());
        verify(tourPlanRepository).findById(tourPlanId);
        verify(repository).findByTourPlanOrderByDisplayOrderAsc(tourPlan);
    }

    @Test
    @DisplayName("Should reorder FAQs successfully")
    void reorderFaqs_success() {
        // Given
        Integer tourPlanId = 1;
        List<Integer> faqIdsInOrder = List.of(3, 1, 2);
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlan));
        when(repository.findById(3)).thenReturn(Optional.of(tourFaqThreeMock()));
        when(repository.findById(1)).thenReturn(Optional.of(tourFaqOneMock()));
        when(repository.findById(2)).thenReturn(Optional.of(tourFaqTwoMock()));

        // When
        service.reorderFaqs(tourPlanId, faqIdsInOrder);

        // Then
        verify(repository, times(3)).save(any(TourFaq.class));
        verify(repository).save(tourFaqCaptor.capture());
    }

    @Test
    @DisplayName("Should throw exception when FAQ not found during reorder")
    void reorderFaqs_whenFaqNotFound_shouldThrowException() {
        // Given
        Integer tourPlanId = 1;
        List<Integer> faqIdsInOrder = List.of(999);
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlan));
        when(repository.findById(999)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.reorderFaqs(tourPlanId, faqIdsInOrder));
        assertEquals("TourFaq not found with id: 999", exception.getMessage());
        verify(repository, never()).save(any(TourFaq.class));
    }

    @Test
    @DisplayName("Should throw exception when FAQ belongs to different tour plan during reorder")
    void reorderFaqs_whenFaqBelongsToDifferentTourPlan_shouldThrowException() {
        // Given
        Integer tourPlanId = 1;
        List<Integer> faqIdsInOrder = List.of(3);
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlan));
        when(repository.findById(3)).thenReturn(Optional.of(tourFaqThreeMock()));

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.reorderFaqs(tourPlanId, faqIdsInOrder));
        assertEquals("FAQ with id 3 does not belong to tour plan 1", exception.getMessage());
        verify(repository, never()).save(any(TourFaq.class));
    }

    @Test
    @DisplayName("Should bulk create FAQs successfully")
    void bulkCreateFaqs_success() {
        // Given
        List<TourFaqRequest> requests = tourFaqRequestListForBulkCreateMock();
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.of(tourPlan));
        when(repository.findByTourPlan(any(TourPlan.class))).thenReturn(List.of());

        // When
        service.bulkCreateFaqs(requests);

        // Then
        verify(repository).saveAll(anyList());
        verify(tourPlanRepository, times(3)).findById(anyInt());
    }

    @Test
    @DisplayName("Should bulk delete FAQs successfully")
    void bulkDeleteFaqs_success() {
        // Given
        List<Integer> faqIds = tourFaqIdsForBulkDeleteMock();
        when(repository.existsById(anyInt())).thenReturn(true);

        // When
        service.bulkDeleteFaqs(faqIds);

        // Then
        verify(repository, times(5)).existsById(anyInt());
        verify(repository).deleteAllById(faqIds);
    }

    @Test
    @DisplayName("Should throw exception when FAQ not found during bulk delete")
    void bulkDeleteFaqs_whenFaqNotFound_shouldThrowException() {
        // Given
        List<Integer> faqIds = List.of(1, 999);
        when(repository.existsById(1)).thenReturn(true);
        when(repository.existsById(999)).thenReturn(false);

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.bulkDeleteFaqs(faqIds));
        assertEquals("TourFaq not found with id: 999", exception.getMessage());
        verify(repository, never()).deleteAllById(anyList());
    }

    @Test
    @DisplayName("Should check if FAQ exists by tour plan ID and question")
    void existsByTourPlanIdAndQuestion_shouldReturnTrue() {
        // Given
        Integer tourPlanId = 1;
        String question = "¿Cuál es la duración del tour?";
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlan));
        when(repository.findByTourPlan(any(TourPlan.class))).thenReturn(List.of(tourFaqOneMock()));

        // When
        boolean result = service.existsByTourPlanIdAndQuestion(tourPlanId, question);

        // Then
        assertTrue(result);
        verify(tourPlanRepository).findById(tourPlanId);
        verify(repository).findByTourPlan(tourPlan);
    }

    @Test
    @DisplayName("Should check if display order is unique within tour plan")
    void isDisplayOrderUniqueWithinTourPlan_shouldReturnTrue() {
        // Given
        Integer tourPlanId = 1;
        Integer displayOrder = 5;
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlan));
        when(repository.findByTourPlan(any(TourPlan.class))).thenReturn(List.of(tourFaqOneMock()));

        // When
        boolean result = service.isDisplayOrderUniqueWithinTourPlan(tourPlanId, displayOrder);

        // Then
        assertTrue(result);
        verify(tourPlanRepository).findById(tourPlanId);
        verify(repository).findByTourPlan(tourPlan);
    }
}
