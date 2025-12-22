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

    @Test
    @DisplayName("Should set isActive to true by default when saving language with null isActive")
    void saveLanguage_withNullIsActive_shouldSetDefaultTrue() {
        // Given
        LanguageRequest requestWithNullIsActive = new LanguageRequest("DE", "Deutsch", null);
        Language savedLanguage = Language.builder()
                .code("DE")
                .name("Deutsch")
                .isActive(true)
                .build();

        when(repository.existsById("DE")).thenReturn(false);
        when(repository.existsByNameIgnoreCase("Deutsch")).thenReturn(false);
        when(repository.save(any(Language.class))).thenReturn(savedLanguage);

        // When
        LanguageResponse result = service.saveLanguage(requestWithNullIsActive);

        // Then
        assertNotNull(result);
        assertTrue(result.isActive());
        verify(repository).save(languageCaptor.capture());
        Language capturedLanguage = languageCaptor.getValue();
        assertTrue(capturedLanguage.getIsActive());
    }

    @Test
    @DisplayName("Should update language successfully")
    void updateLanguage_success() {
        // Given
        String code = "ES";
        LanguageRequest updateRequest = new LanguageRequest("ES", "Español Actualizado", false);
        Language updatedLanguage = Language.builder()
                .code("ES")
                .name("Español Actualizado")
                .isActive(false)
                .build();

        when(repository.findById(code)).thenReturn(Optional.of(language));
        when(repository.existsByNameIgnoreCase(updateRequest.name())).thenReturn(false);
        when(repository.save(any(Language.class))).thenReturn(updatedLanguage);

        // When
        LanguageResponse result = service.updateLanguage(code, updateRequest);

        // Then
        assertNotNull(result);
        assertEquals(updateRequest.name(), result.name());
        assertEquals(updateRequest.isActive(), result.isActive());
        verify(repository).findById(code);
        verify(repository).save(languageCaptor.capture());
        Language capturedLanguage = languageCaptor.getValue();
        assertEquals(updateRequest.name(), capturedLanguage.getName());
        assertEquals(updateRequest.isActive(), capturedLanguage.getIsActive());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent language")
    void updateLanguage_whenNotExists_shouldThrowException() {
        // Given
        String code = "XX";
        when(repository.findById(code)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateLanguage(code, languageRequest));
        assertEquals("Language not found with code " + code, exception.getMessage());
        verify(repository).findById(code);
        verify(repository, never()).save(any(Language.class));
    }

    @Test
    @DisplayName("Should throw exception when updating language with duplicate name")
    void updateLanguage_withDuplicateName_shouldThrowException() {
        // Given
        String code = "ES";
        LanguageRequest updateRequest = new LanguageRequest("ES", "English", true);
        Language existingLanguage = languageSpanishMock();

        when(repository.findById(code)).thenReturn(Optional.of(existingLanguage));
        when(repository.existsByNameIgnoreCase(updateRequest.name())).thenReturn(true);

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.updateLanguage(code, updateRequest));
        assertEquals("Language with name " + updateRequest.name() + " already exists", exception.getMessage());
        verify(repository).findById(code);
        verify(repository).existsByNameIgnoreCase(updateRequest.name());
        verify(repository, never()).save(any(Language.class));
    }

    @Test
    @DisplayName("Should not throw exception when updating language with same name")
    void updateLanguage_withSameName_shouldNotThrowException() {
        // Given
        String code = "ES";
        LanguageRequest updateRequest = new LanguageRequest("ES", "Español", false);
        Language existingLanguage = languageSpanishMock();

        when(repository.findById(code)).thenReturn(Optional.of(existingLanguage));
        when(repository.save(any(Language.class))).thenReturn(existingLanguage);

        // When
        LanguageResponse result = service.updateLanguage(code, updateRequest);

        // Then
        assertNotNull(result);
        verify(repository).findById(code);
        verify(repository).save(any(Language.class));
    }

    @Test
    @DisplayName("Should preserve isActive when updating language with null isActive")
    void updateLanguage_withNullIsActive_shouldPreserveCurrentValue() {
        // Given
        String code = "ES";
        LanguageRequest updateRequest = new LanguageRequest("ES", "Español Actualizado", null);
        Language existingLanguage = languageSpanishMock();
        existingLanguage.setIsActive(true);
        Language updatedLanguage = Language.builder()
                .code("ES")
                .name("Español Actualizado")
                .isActive(true)
                .build();

        when(repository.findById(code)).thenReturn(Optional.of(existingLanguage));
        when(repository.save(any(Language.class))).thenReturn(updatedLanguage);

        // When
        LanguageResponse result = service.updateLanguage(code, updateRequest);

        // Then
        assertNotNull(result);
        assertTrue(result.isActive());
        verify(repository).save(languageCaptor.capture());
        Language capturedLanguage = languageCaptor.getValue();
        assertTrue(capturedLanguage.getIsActive());
    }

    @Test
    @DisplayName("Should delete language successfully when exists")
    void deleteLanguage_whenExists_success() {
        // Given
        String code = "ES";
        when(repository.existsById(code)).thenReturn(true);

        // When
        service.deleteLanguage(code);

        // Then
        verify(repository).existsById(code);
        verify(repository).deleteById(code);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent language")
    void deleteLanguage_whenNotExists_shouldThrowException() {
        // Given
        String code = "XX";
        when(repository.existsById(code)).thenReturn(false);

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteLanguage(code));
        assertEquals("Language not found with code " + code, exception.getMessage());
        verify(repository).existsById(code);
        verify(repository, never()).deleteById(anyString());
    }

    // Business Operations Tests
    @Test
    @DisplayName("Should return all active languages")
    void getAllActiveLanguages_shouldReturnActiveLanguages() {
        // Given
        List<Language> activeLanguages = languages.stream()
                .filter(l -> l.getIsActive())
                .toList();
        when(repository.findByIsActiveTrue()).thenReturn(activeLanguages);

        // When
        List<LanguageResponse> result = service.getAllActiveLanguages();

        // Then
        assertNotNull(result);
        assertEquals(activeLanguages.size(), result.size());
        assertTrue(result.stream().allMatch(l -> l.isActive()));
        verify(repository).findByIsActiveTrue();
    }

    @Test
    @DisplayName("Should return language by name when exists")
    void getLanguageByName_whenExists_shouldReturnLanguage() {
        // Given
        String name = "Español";
        when(repository.findByNameIgnoreCase(name)).thenReturn(Optional.of(language));

        // When
        LanguageResponse result = service.getLanguageByName(name);

        // Then
        assertNotNull(result);
        assertEquals(language.getCode(), result.code());
        assertEquals(language.getName(), result.name());
        verify(repository).findByNameIgnoreCase(name);
    }

    @Test
    @DisplayName("Should throw exception when getting language by name that doesn't exist")
    void getLanguageByName_whenNotExists_shouldThrowException() {
        // Given
        String name = "NonExistent";
        when(repository.findByNameIgnoreCase(name)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getLanguageByName(name));
        assertEquals("Language not found with name " + name, exception.getMessage());
        verify(repository).findByNameIgnoreCase(name);
    }
}