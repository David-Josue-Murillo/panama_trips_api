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

    @Test
    @DisplayName("Should return language by code when exists")
    void getLanguageByCode_whenExists_shouldReturnLanguage() {
        // Given
        String code = "ES";
        when(repository.findById(code)).thenReturn(Optional.of(language));

        // When
        LanguageResponse result = service.getLanguageByCode(code);

        // Then
        assertNotNull(result);
        assertEquals(language.getCode(), result.code());
        assertEquals(language.getName(), result.name());
        assertEquals(language.getIsActive(), result.isActive());
        verify(repository).findById(code);
    }

    @Test
    @DisplayName("Should throw exception when getting language by code that doesn't exist")
    void getLanguageByCode_whenNotExists_shouldThrowException() {
        // Given
        String code = "XX";
        when(repository.findById(code)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getLanguageByCode(code));
        assertEquals("Language not found with code " + code, exception.getMessage());
        verify(repository).findById(code);
    }

    @Test
    @DisplayName("Should save language successfully")
    void saveLanguage_success() {
        // Given
        when(repository.existsById(languageRequest.code())).thenReturn(false);
        when(repository.existsByNameIgnoreCase(languageRequest.name())).thenReturn(false);
        when(repository.save(any(Language.class))).thenReturn(language);

        // When
        LanguageResponse result = service.saveLanguage(languageRequest);

        // Then
        assertNotNull(result);
        assertEquals(language.getCode(), result.code());
        assertEquals(language.getName(), result.name());
        verify(repository).existsById(languageRequest.code());
        verify(repository).existsByNameIgnoreCase(languageRequest.name());
        verify(repository).save(languageCaptor.capture());
        Language savedLanguage = languageCaptor.getValue();
        assertEquals(languageRequest.code(), savedLanguage.getCode());
        assertEquals(languageRequest.name(), savedLanguage.getName());
    }

    @Test
    @DisplayName("Should throw exception when saving language with duplicate code")
    void saveLanguage_withDuplicateCode_shouldThrowException() {
        // Given
        when(repository.existsById(languageRequest.code())).thenReturn(true);

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.saveLanguage(languageRequest));
        assertEquals("Language with code " + languageRequest.code() + " already exists", exception.getMessage());
        verify(repository).existsById(languageRequest.code());
        verify(repository, never()).existsByNameIgnoreCase(anyString());
        verify(repository, never()).save(any(Language.class));
    }

    @Test
    @DisplayName("Should throw exception when saving language with duplicate name")
    void saveLanguage_withDuplicateName_shouldThrowException() {
        // Given
        when(repository.existsById(languageRequest.code())).thenReturn(false);
        when(repository.existsByNameIgnoreCase(languageRequest.name())).thenReturn(true);

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.saveLanguage(languageRequest));
        assertEquals("Language with name " + languageRequest.name() + " already exists", exception.getMessage());
        verify(repository).existsById(languageRequest.code());
        verify(repository).existsByNameIgnoreCase(languageRequest.name());
        verify(repository, never()).save(any(Language.class));
    }
}