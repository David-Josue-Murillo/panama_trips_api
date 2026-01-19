package com.app.panama_trips.service;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.panama_trips.persistence.entity.Language;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourTranslation;
import com.app.panama_trips.persistence.repository.LanguageRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.persistence.repository.TourTranslationRepository;
import com.app.panama_trips.presentation.dto.TourTranslationRequest;
import com.app.panama_trips.service.implementation.TourTranslationService;

import static com.app.panama_trips.DataProvider.*;

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
}