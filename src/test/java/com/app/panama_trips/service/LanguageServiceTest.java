package com.app.panama_trips.service;

import java.util.List;

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

import com.app.panama_trips.persistence.entity.Language;
import com.app.panama_trips.persistence.repository.LanguageRepository;
import com.app.panama_trips.presentation.dto.LanguageRequest;
import com.app.panama_trips.presentation.dto.LanguageResponse;
import com.app.panama_trips.service.implementation.LanguageService;

import static com.app.panama_trips.DataProvider.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() {
        language = languageSpanishMock();
        languages = languageListMock();
        languageRequest = new LanguageRequest("ES", "Español", true);
    }

    // CRUD Operations Tests
    @Test
    @DisplayName("Should return all languages when getAllLanguages is called with pagination")
    void getAllLanguages_shouldReturnAllData() {
        // Given
        Page<Language> page = new PageImpl<>(languages);
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(page);

        // When
        Page<LanguageResponse> result = service.getAllLanguages(pageable);

        // Then
        assertNotNull(result);
        assertEquals(languages.size(), result.getTotalElements());
        verify(repository).findAll(pageable);
    }
}