package com.app.panama_trips.service;

import java.util.List;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.panama_trips.persistence.entity.Language;
import com.app.panama_trips.persistence.repository.LanguageRepository;
import com.app.panama_trips.presentation.dto.LanguageRequest;
import com.app.panama_trips.service.implementation.LanguageService;

@ExtendWith(MockitoExtension.class)
public class LanguageServiceTest {
    
    @Mock
    private LanguageRepository repository;

    @InjectMocks
    private LanguageService service;

    @Captor
    private ArgumentCaptor<Language> languageCaptor;

    private Language language;
    private List<Language> languages;
    private LanguageRequest languageRequest;

}