package com.app.panama_trips.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.panama_trips.persistence.repository.LanguageRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.persistence.repository.TourTranslationRepository;
import com.app.panama_trips.service.implementation.TourTranslationService;

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
}