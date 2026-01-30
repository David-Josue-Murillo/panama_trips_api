package com.app.panama_trips.service;

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

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Language;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourTranslation;
import com.app.panama_trips.persistence.entity.TourTranslationId;
import com.app.panama_trips.persistence.repository.LanguageRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.persistence.repository.TourTranslationRepository;
import com.app.panama_trips.presentation.dto.TourTranslationRequest;
import com.app.panama_trips.presentation.dto.TourTranslationResponse;
import com.app.panama_trips.service.implementation.TourTranslationService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TourTranslationServiceTest {

    @Mock
    private TourTranslationRepository tourTranslationRepository;

    @Mock
    private TourPlanRepository tourPlanRepository;

    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private TourTranslationService tourTranslationService;

    private TourTranslation tourTranslation;
    private TourPlan tourPlan;
    private Language language;
    private TourTranslationRequest tourTranslationRequest;
    private TourTranslationId tourTranslationId;
    private Pageable pageable;
    private final Integer tourPlanId = 1;
    private final String languageCode = "EN";

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        tourPlan = new TourPlan();
        tourPlan.setId(tourPlanId);

        language = new Language();
        language.setCode(languageCode);

        tourTranslationId = TourTranslationId.builder()
                .tourPlanId(tourPlanId)
                .languageCode(languageCode)
                .build();

        tourTranslation = TourTranslation.builder()
                .id(tourTranslationId)
                .tourPlan(tourPlan)
                .language(language)
                .title("Test Title")
                .shortDescription("Short Description")
                .description("Test Description")
                .includedServices("Included Services")
                .excludedServices("Excluded Services")
                .whatToBring("What to Bring")
                .meetingPoint("Meeting Point")
                .build();

        tourTranslationRequest = new TourTranslationRequest(
                tourPlanId,
                languageCode,
                "New Title",
                "New Short Description",
                "New Description",
                "New Included Services",
                "New Excluded Services",
                "New What to Bring",
                "New Meeting Point"
        );
    }

    // 1. getAllTourTranslations
    @Test
    void getAllTourTranslations_Success() {
        Page<TourTranslation> mockPage = new PageImpl<>(List.of(tourTranslation));
        when(tourTranslationRepository.findAll(pageable)).thenReturn(mockPage);

        Page<TourTranslationResponse> result = tourTranslationService.getAllTourTranslations(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("Test Title", result.getContent().get(0).title());
        verify(tourTranslationRepository).findAll(pageable);
    }

    @Test
    void getAllTourTranslations_EmptyPage() {
        Page<TourTranslation> mockPage = new PageImpl<>(List.of());
        when(tourTranslationRepository.findAll(pageable)).thenReturn(mockPage);

        Page<TourTranslationResponse> result = tourTranslationService.getAllTourTranslations(pageable);

        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        verify(tourTranslationRepository).findAll(pageable);
    }

    // 2. getTourTranslationByTourPlanIdAndLanguageCode
    @Test
    void getTourTranslationByTourPlanIdAndLanguageCode_Success() {
        when(tourTranslationRepository.findByTourPlanIdAndLanguageCode(tourPlanId, languageCode))
                .thenReturn(Optional.of(tourTranslation));

        TourTranslationResponse result = tourTranslationService.getTourTranslationByTourPlanIdAndLanguageCode(tourPlanId, languageCode);

        assertNotNull(result);
        assertEquals("Test Title", result.title());
        assertEquals(tourPlanId, result.tourPlanId());
        assertEquals(languageCode, result.languageCode());
        verify(tourTranslationRepository).findByTourPlanIdAndLanguageCode(tourPlanId, languageCode);
    }

    @Test
    void getTourTranslationByTourPlanIdAndLanguageCode_NotFound() {
        when(tourTranslationRepository.findByTourPlanIdAndLanguageCode(tourPlanId, languageCode))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> tourTranslationService.getTourTranslationByTourPlanIdAndLanguageCode(tourPlanId, languageCode));
    }

    // 3. saveTourTranslation
    @Test
    void saveTourTranslation_Success() {
        TourTranslation savedTranslation = TourTranslation.builder()
                .id(tourTranslationId)
                .tourPlan(tourPlan)
                .language(language)
                .title("New Title")
                .shortDescription("New Short Description")
                .description("New Description")
                .includedServices("New Included Services")
                .excludedServices("New Excluded Services")
                .whatToBring("New What to Bring")
                .meetingPoint("New Meeting Point")
                .build();

        when(tourPlanRepository.existsById(tourPlanId)).thenReturn(true);
        when(languageRepository.existsById(languageCode)).thenReturn(true);
        when(tourTranslationRepository.findByTourPlanIdAndLanguageCode(tourPlanId, languageCode))
                .thenReturn(Optional.empty());
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlan));
        when(languageRepository.findById(languageCode)).thenReturn(Optional.of(language));
        when(tourTranslationRepository.save(any(TourTranslation.class))).thenReturn(savedTranslation);

        TourTranslationResponse result = tourTranslationService.saveTourTranslation(tourTranslationRequest);

        assertNotNull(result);
        assertEquals("New Title", result.title());
        verify(tourPlanRepository).existsById(tourPlanId);
        verify(languageRepository).existsById(languageCode);
        verify(tourTranslationRepository).save(any(TourTranslation.class));
    }

    @Test
    void saveTourTranslation_TourPlanNotFound() {
        when(tourPlanRepository.existsById(tourPlanId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> tourTranslationService.saveTourTranslation(tourTranslationRequest));
    }

    @Test
    void saveTourTranslation_LanguageNotFound() {
        when(tourPlanRepository.existsById(tourPlanId)).thenReturn(true);
        when(languageRepository.existsById(languageCode)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> tourTranslationService.saveTourTranslation(tourTranslationRequest));
    }

    @Test
    void saveTourTranslation_AlreadyExists() {
        when(tourPlanRepository.existsById(tourPlanId)).thenReturn(true);
        when(languageRepository.existsById(languageCode)).thenReturn(true);
        when(tourTranslationRepository.findByTourPlanIdAndLanguageCode(tourPlanId, languageCode))
                .thenReturn(Optional.of(tourTranslation));

        assertThrows(IllegalArgumentException.class,
                () -> tourTranslationService.saveTourTranslation(tourTranslationRequest));
    }

    // 4. updateTourTranslation
    @Test
    void updateTourTranslation_Success() {
        TourTranslation updatedTranslation = TourTranslation.builder()
                .id(tourTranslationId)
                .tourPlan(tourPlan)
                .language(language)
                .title("New Title")
                .shortDescription("New Short Description")
                .description("New Description")
                .includedServices("New Included Services")
                .excludedServices("New Excluded Services")
                .whatToBring("New What to Bring")
                .meetingPoint("New Meeting Point")
                .build();

        when(tourTranslationRepository.findByTourPlanIdAndLanguageCode(tourPlanId, languageCode))
                .thenReturn(Optional.of(tourTranslation));
        when(tourTranslationRepository.save(any(TourTranslation.class))).thenReturn(updatedTranslation);

        TourTranslationResponse result = tourTranslationService.updateTourTranslation(tourPlanId, languageCode, tourTranslationRequest);

        assertNotNull(result);
        assertEquals("New Title", result.title());
        verify(tourTranslationRepository).findByTourPlanIdAndLanguageCode(tourPlanId, languageCode);
        verify(tourTranslationRepository).save(any(TourTranslation.class));
    }

    @Test
    void updateTourTranslation_NotFound() {
        when(tourTranslationRepository.findByTourPlanIdAndLanguageCode(tourPlanId, languageCode))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> tourTranslationService.updateTourTranslation(tourPlanId, languageCode, tourTranslationRequest));
    }

    @Test
    void updateTourTranslation_MismatchedIds() {
        TourTranslationRequest mismatchedRequest = new TourTranslationRequest(
                999,
                languageCode,
                "Title",
                "Short",
                "Description",
                "Included",
                "Excluded",
                "Bring",
                "Meeting"
        );

        assertThrows(IllegalArgumentException.class,
                () -> tourTranslationService.updateTourTranslation(tourPlanId, languageCode, mismatchedRequest));
    }

    // 5. deleteTourTranslation
    @Test
    void deleteTourTranslation_Success() {
        when(tourTranslationRepository.existsById(tourTranslationId)).thenReturn(true);

        tourTranslationService.deleteTourTranslation(tourPlanId, languageCode);

        verify(tourTranslationRepository).existsById(tourTranslationId);
        verify(tourTranslationRepository).deleteById(tourTranslationId);
    }

    @Test
    void deleteTourTranslation_NotFound() {
        when(tourTranslationRepository.existsById(tourTranslationId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> tourTranslationService.deleteTourTranslation(tourPlanId, languageCode));
    }

    // 6. getTourTranslationsByTourPlanId
    @Test
    void getTourTranslationsByTourPlanId_Success() {
        List<TourTranslation> translations = List.of(tourTranslation);
        when(tourTranslationRepository.findByTourPlanId(tourPlanId)).thenReturn(translations);

        List<TourTranslationResponse> result = tourTranslationService.getTourTranslationsByTourPlanId(tourPlanId);

        assertEquals(1, result.size());
        assertEquals("Test Title", result.get(0).title());
        verify(tourTranslationRepository).findByTourPlanId(tourPlanId);
    }

    @Test
    void getTourTranslationsByTourPlanId_Empty() {
        when(tourTranslationRepository.findByTourPlanId(tourPlanId)).thenReturn(List.of());

        List<TourTranslationResponse> result = tourTranslationService.getTourTranslationsByTourPlanId(tourPlanId);

        assertTrue(result.isEmpty());
        verify(tourTranslationRepository).findByTourPlanId(tourPlanId);
    }

    // 7. getTourTranslationsByLanguageCode
    @Test
    void getTourTranslationsByLanguageCode_Success() {
        List<TourTranslation> translations = List.of(tourTranslation);
        when(tourTranslationRepository.findByLanguageCode(languageCode)).thenReturn(translations);

        List<TourTranslationResponse> result = tourTranslationService.getTourTranslationsByLanguageCode(languageCode);

        assertEquals(1, result.size());
        assertEquals("Test Title", result.get(0).title());
        verify(tourTranslationRepository).findByLanguageCode(languageCode);
    }

    @Test
    void getTourTranslationsByLanguageCode_Empty() {
        when(tourTranslationRepository.findByLanguageCode(languageCode)).thenReturn(List.of());

        List<TourTranslationResponse> result = tourTranslationService.getTourTranslationsByLanguageCode(languageCode);

        assertTrue(result.isEmpty());
        verify(tourTranslationRepository).findByLanguageCode(languageCode);
    }

    // 8. existsByTourPlanIdAndLanguageCode
    @Test
    void existsByTourPlanIdAndLanguageCode_ReturnsTrue() {
        when(tourTranslationRepository.findByTourPlanIdAndLanguageCode(tourPlanId, languageCode))
                .thenReturn(Optional.of(tourTranslation));

        boolean result = tourTranslationService.existsByTourPlanIdAndLanguageCode(tourPlanId, languageCode);

        assertTrue(result);
        verify(tourTranslationRepository).findByTourPlanIdAndLanguageCode(tourPlanId, languageCode);
    }

    @Test
    void existsByTourPlanIdAndLanguageCode_ReturnsFalse() {
        when(tourTranslationRepository.findByTourPlanIdAndLanguageCode(tourPlanId, languageCode))
                .thenReturn(Optional.empty());

        boolean result = tourTranslationService.existsByTourPlanIdAndLanguageCode(tourPlanId, languageCode);

        assertFalse(result);
        verify(tourTranslationRepository).findByTourPlanIdAndLanguageCode(tourPlanId, languageCode);
    }

    // 9. deleteAllTranslationsByTourPlanId
    @Test
    void deleteAllTranslationsByTourPlanId_WhenExists() {
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlan));

        tourTranslationService.deleteAllTranslationsByTourPlanId(tourPlanId);

        verify(tourPlanRepository).findById(tourPlanId);
        verify(tourTranslationRepository).deleteByTourPlan(tourPlan);
    }

    @Test
    void deleteAllTranslationsByTourPlanId_WhenTourPlanNotFound() {
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> tourTranslationService.deleteAllTranslationsByTourPlanId(tourPlanId));
    }

    // 10. deleteAllTranslationsByLanguageCode
    @Test
    void deleteAllTranslationsByLanguageCode_WhenExists() {
        when(languageRepository.findById(languageCode)).thenReturn(Optional.of(language));

        tourTranslationService.deleteAllTranslationsByLanguageCode(languageCode);

        verify(languageRepository).findById(languageCode);
        verify(tourTranslationRepository).deleteByLanguage(language);
    }

    @Test
    void deleteAllTranslationsByLanguageCode_WhenLanguageNotFound() {
        when(languageRepository.findById(languageCode)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> tourTranslationService.deleteAllTranslationsByLanguageCode(languageCode));
    }

    // 11. countTranslationsByTourPlanId
    @Test
    void countTranslationsByTourPlanId_ReturnsCount() {
        when(tourTranslationRepository.countTranslationsByTourPlanId(tourPlanId)).thenReturn(5L);

        Long result = tourTranslationService.countTranslationsByTourPlanId(tourPlanId);

        assertEquals(5L, result);
        verify(tourTranslationRepository).countTranslationsByTourPlanId(tourPlanId);
    }

    @Test
    void countTranslationsByTourPlanId_ReturnsZero() {
        when(tourTranslationRepository.countTranslationsByTourPlanId(tourPlanId)).thenReturn(0L);

        Long result = tourTranslationService.countTranslationsByTourPlanId(tourPlanId);

        assertEquals(0L, result);
        verify(tourTranslationRepository).countTranslationsByTourPlanId(tourPlanId);
    }

    // 12. countAllTourTranslations
    @Test
    void countAllTourTranslations_ReturnsCount() {
        when(tourTranslationRepository.count()).thenReturn(10L);

        long result = tourTranslationService.countAllTourTranslations();

        assertEquals(10L, result);
        verify(tourTranslationRepository).count();
    }

    @Test
    void countAllTourTranslations_ReturnsZero() {
        when(tourTranslationRepository.count()).thenReturn(0L);

        long result = tourTranslationService.countAllTourTranslations();

        assertEquals(0L, result);
        verify(tourTranslationRepository).count();
    }
}

