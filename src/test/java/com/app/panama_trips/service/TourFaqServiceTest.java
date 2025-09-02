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

    
}
