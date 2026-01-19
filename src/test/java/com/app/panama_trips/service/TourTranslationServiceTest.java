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
import com.app.panama_trips.persistence.entity.Language;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourTranslation;
import com.app.panama_trips.persistence.repository.LanguageRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.persistence.repository.TourTranslationRepository;
import com.app.panama_trips.presentation.dto.TourTranslationRequest;
import com.app.panama_trips.presentation.dto.TourTranslationResponse;
import com.app.panama_trips.service.implementation.TourTranslationService;

import static com.app.panama_trips.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourTranslationServiceTest {
    @Mock
    private TourTranslationRepository tourTranslationRepository;

    @Mock
    private TourPlanRepository tourPlanRepository;

    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private TourTranslationService service;

    @Captor
    private ArgumentCaptor<TourTranslation> tourTranslationCaptor;

    private TourTranslation tourTranslation;
    private List<TourTranslation> tourTranslations;
    private TourTranslationRequest tourTranslationRequest;
    private TourPlan tourPlan;
    private Language language;

    @BeforeEach
    void setUp() {
        tourTranslation = tourTranslationSpanishMock();
        tourTranslations = tourTranslationListMock();
        tourTranslationRequest = tourTranslationRequestMock();
        tourPlan = tourPlanOneMock;
        language = languageSpanishMock();
    }

    // CRUD Operations Tests
    @Test
    @DisplayName("Should return all tour translations when getAllTourTranslations is called with pagination")
    void getAllTourTranslations_shouldReturnAllData() {
        // Given
        Page<TourTranslation> page = new PageImpl<>(tourTranslations);
        Pageable pageable = PageRequest.of(0, 10);

        when(tourTranslationRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<TourTranslationResponse> result = service.getAllTourTranslations(pageable);

        // Then
        assertNotNull(result);
        assertEquals(tourTranslations.size(), result.getTotalElements());
        verify(tourTranslationRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should return tour translation by tourPlanId and languageCode when exists")
    void getTourTranslationByTourPlanIdAndLanguageCode_whenExists_shouldReturnTranslation() {
        // Given
        Integer tourPlanId = 1;
        String languageCode = "ES";
        when(tourTranslationRepository.findByTourPlanIdAndLanguageCode(tourPlanId, languageCode))
                .thenReturn(Optional.of(tourTranslation));

        // When
        TourTranslationResponse result = service.getTourTranslationByTourPlanIdAndLanguageCode(tourPlanId,
                languageCode);

        // Then
        assertNotNull(result);
        assertEquals(tourTranslation.getId().getTourPlanId(), result.tourPlanId());
        assertEquals(tourTranslation.getId().getLanguageCode(), result.languageCode());
        assertEquals(tourTranslation.getTitle(), result.title());
        verify(tourTranslationRepository).findByTourPlanIdAndLanguageCode(tourPlanId, languageCode);
    }

    @Test
    @DisplayName("Should throw exception when getting tour translation that doesn't exist")
    void getTourTranslationByTourPlanIdAndLanguageCode_whenNotExists_shouldThrowException() {
        // Given
        Integer tourPlanId = 999;
        String languageCode = "XX";
        when(tourTranslationRepository.findByTourPlanIdAndLanguageCode(tourPlanId, languageCode))
                .thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getTourTranslationByTourPlanIdAndLanguageCode(tourPlanId, languageCode));
        assertTrue(exception.getMessage().contains("Tour Translation not found"));
        verify(tourTranslationRepository).findByTourPlanIdAndLanguageCode(tourPlanId, languageCode);
    }

    @Test
    @DisplayName("Should save tour translation successfully")
    void saveTourTranslation_success() {
        // Given
        when(tourPlanRepository.existsById(tourTranslationRequest.tourPlanId())).thenReturn(true);
        when(languageRepository.existsById(tourTranslationRequest.languageCode())).thenReturn(true);
        when(tourTranslationRepository.findByTourPlanIdAndLanguageCode(
                tourTranslationRequest.tourPlanId(), tourTranslationRequest.languageCode()))
                .thenReturn(Optional.empty());
        when(tourPlanRepository.findById(tourTranslationRequest.tourPlanId()))
                .thenReturn(Optional.of(tourPlan));
        when(languageRepository.findById(tourTranslationRequest.languageCode()))
                .thenReturn(Optional.of(language));
        when(tourTranslationRepository.save(any(TourTranslation.class))).thenReturn(tourTranslation);

        // When
        TourTranslationResponse result = service.saveTourTranslation(tourTranslationRequest);

        // Then
        assertNotNull(result);
        assertEquals(tourTranslation.getId().getTourPlanId(), result.tourPlanId());
        assertEquals(tourTranslation.getId().getLanguageCode(), result.languageCode());
        verify(tourPlanRepository).existsById(tourTranslationRequest.tourPlanId());
        verify(languageRepository).existsById(tourTranslationRequest.languageCode());
        verify(tourTranslationRepository).findByTourPlanIdAndLanguageCode(
                tourTranslationRequest.tourPlanId(), tourTranslationRequest.languageCode());
        verify(tourTranslationRepository).save(tourTranslationCaptor.capture());
    }

    @Test
    @DisplayName("Should throw exception when saving tour translation with non-existent tour plan")
    void saveTourTranslation_whenTourPlanNotExists_shouldThrowException() {
        // Given
        when(tourPlanRepository.existsById(tourTranslationRequest.tourPlanId())).thenReturn(false);

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.saveTourTranslation(tourTranslationRequest));
        assertTrue(exception.getMessage().contains("Tour Plan not found"));
        verify(tourPlanRepository).existsById(tourTranslationRequest.tourPlanId());
        verify(languageRepository, never()).existsById(anyString());
        verify(tourTranslationRepository, never()).save(any(TourTranslation.class));
    }

    @Test
    @DisplayName("Should throw exception when saving tour translation with non-existent language")
    void saveTourTranslation_whenLanguageNotExists_shouldThrowException() {
        // Given
        when(tourPlanRepository.existsById(tourTranslationRequest.tourPlanId())).thenReturn(true);
        when(languageRepository.existsById(tourTranslationRequest.languageCode())).thenReturn(false);

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.saveTourTranslation(tourTranslationRequest));
        assertTrue(exception.getMessage().contains("Language not found"));
        verify(tourPlanRepository).existsById(tourTranslationRequest.tourPlanId());
        verify(languageRepository).existsById(tourTranslationRequest.languageCode());
        verify(tourTranslationRepository, never()).save(any(TourTranslation.class));
    }

    @Test
    @DisplayName("Should throw exception when saving tour translation that already exists")
    void saveTourTranslation_whenAlreadyExists_shouldThrowException() {
        // Given
        when(tourPlanRepository.existsById(tourTranslationRequest.tourPlanId())).thenReturn(true);
        when(languageRepository.existsById(tourTranslationRequest.languageCode())).thenReturn(true);
        when(tourTranslationRepository.findByTourPlanIdAndLanguageCode(
                tourTranslationRequest.tourPlanId(), tourTranslationRequest.languageCode()))
                .thenReturn(Optional.of(tourTranslation));

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.saveTourTranslation(tourTranslationRequest));
        assertTrue(exception.getMessage().contains("Tour Translation already exists"));
        verify(tourPlanRepository).existsById(tourTranslationRequest.tourPlanId());
        verify(languageRepository).existsById(tourTranslationRequest.languageCode());
        verify(tourTranslationRepository).findByTourPlanIdAndLanguageCode(
                tourTranslationRequest.tourPlanId(), tourTranslationRequest.languageCode());
        verify(tourTranslationRepository, never()).save(any(TourTranslation.class));
    }

    @Test
    @DisplayName("Should update tour translation successfully")
    void updateTourTranslation_success() {
        // Given
        Integer tourPlanId = 1;
        String languageCode = "ES";
        TourTranslationRequest updateRequest = tourTranslationRequestMock();
        TourTranslation updatedTranslation = tourTranslationSpanishMock();
        updatedTranslation.setTitle("Updated Title");

        when(tourTranslationRepository.findByTourPlanIdAndLanguageCode(tourPlanId, languageCode))
                .thenReturn(Optional.of(tourTranslation));
        when(tourTranslationRepository.save(any(TourTranslation.class))).thenReturn(updatedTranslation);

        // When
        TourTranslationResponse result = service.updateTourTranslation(tourPlanId, languageCode, updateRequest);

        // Then
        assertNotNull(result);
        verify(tourTranslationRepository).findByTourPlanIdAndLanguageCode(tourPlanId, languageCode);
        verify(tourTranslationRepository).save(tourTranslationCaptor.capture());
        TourTranslation capturedTranslation = tourTranslationCaptor.getValue();
        assertEquals(updateRequest.title(), capturedTranslation.getTitle());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent tour translation")
    void updateTourTranslation_whenNotExists_shouldThrowException() {
        // Given
        Integer tourPlanId = 999;
        String languageCode = "XX";
        TourTranslationRequest requestWithMatchingIds = new TourTranslationRequest(
                tourPlanId, // Matching tourPlanId
                languageCode, // Matching languageCode
                "Title",
                "Short Description",
                "Description",
                "Included",
                "Excluded",
                "What to bring",
                "Meeting point");

        when(tourTranslationRepository.findByTourPlanIdAndLanguageCode(tourPlanId, languageCode))
                .thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateTourTranslation(tourPlanId, languageCode, requestWithMatchingIds));
        assertTrue(exception.getMessage().contains("Tour Translation not found"));
        verify(tourTranslationRepository).findByTourPlanIdAndLanguageCode(tourPlanId, languageCode);
        verify(tourTranslationRepository, never()).save(any(TourTranslation.class));
    }

    @Test
    @DisplayName("Should throw exception when tourPlanId in request doesn't match path parameter")
    void updateTourTranslation_whenTourPlanIdMismatch_shouldThrowException() {
        // Given
        Integer tourPlanId = 1;
        String languageCode = "ES";
        TourTranslationRequest requestWithDifferentId = new TourTranslationRequest(
                999, // Different tourPlanId
                languageCode,
                "Title",
                "Short Description",
                "Description",
                "Included",
                "Excluded",
                "What to bring",
                "Meeting point");

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.updateTourTranslation(tourPlanId, languageCode, requestWithDifferentId));
        assertTrue(exception.getMessage().contains("must match the path parameters"));
        verify(tourTranslationRepository, never()).findByTourPlanIdAndLanguageCode(anyInt(), anyString());
        verify(tourTranslationRepository, never()).save(any(TourTranslation.class));
    }

    @Test
    @DisplayName("Should throw exception when languageCode in request doesn't match path parameter")
    void updateTourTranslation_whenLanguageCodeMismatch_shouldThrowException() {
        // Given
        Integer tourPlanId = 1;
        String languageCode = "ES";
        TourTranslationRequest requestWithDifferentCode = new TourTranslationRequest(
                tourPlanId,
                "EN", // Different languageCode
                "Title",
                "Short Description",
                "Description",
                "Included",
                "Excluded",
                "What to bring",
                "Meeting point");

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.updateTourTranslation(tourPlanId, languageCode, requestWithDifferentCode));
        assertTrue(exception.getMessage().contains("must match the path parameters"));
        verify(tourTranslationRepository, never()).findByTourPlanIdAndLanguageCode(anyInt(), anyString());
        verify(tourTranslationRepository, never()).save(any(TourTranslation.class));
    }
}