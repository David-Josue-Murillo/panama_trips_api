package com.app.panama_trips.service;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Guide;
import com.app.panama_trips.persistence.entity.Provider;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.repository.GuideRepository;
import com.app.panama_trips.persistence.repository.ProviderRepository;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.presentation.dto.GuideRequest;
import com.app.panama_trips.presentation.dto.GuideResponse;
import com.app.panama_trips.service.implementation.GuideService;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.app.panama_trips.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GuideServiceTest {

    @Mock
    private GuideRepository guideRepository;

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private UserEntityRepository userRepository;

    @InjectMocks
    private GuideService service;

    @Captor
    private ArgumentCaptor<Guide> guideCaptor;

    private Guide guide;
    private GuideRequest request;
    private List<Guide> guides;
    private UserEntity user;
    private Provider provider;

    @BeforeEach
    void setUp() {
        guide = guideOneMock;
        guides = guideListMock;
        request = guideRequestMock;
        user = userAdmin();
        provider = providerOneMock;
    }

    @Test
    @DisplayName("Should return all guides when findAll is called with pagination")
    void findAll_shouldReturnAllGuides() {
        // Given
        Page<Guide> page = new PageImpl<>(guides);
        Pageable pageable = PageRequest.of(0, 10);

        when(guideRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<GuideResponse> response = service.findAll(pageable);

        // Then
        assertNotNull(response);
        assertEquals(guides.size(), response.getTotalElements());
        verify(guideRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should create guide successfully")
    void createGuide_success() {
        // Given
        when(userRepository.findById(request.userId())).thenReturn(Optional.of(user));
        when(providerRepository.findById(request.providerId())).thenReturn(Optional.of(provider));
        when(guideRepository.save(any(Guide.class))).thenReturn(guide);

        // When
        GuideResponse result = service.createGuide(request);

        // Then
        assertNotNull(result);
        verify(userRepository).findById(request.userId());
        verify(providerRepository).findById(request.providerId());
        verify(guideRepository).save(guideCaptor.capture());

        Guide savedGuide = guideCaptor.getValue();
        assertEquals(request.bio(), savedGuide.getBio());
        assertEquals(request.specialties().toString(), savedGuide.getSpecialties());
        assertEquals(request.languages().toString(), savedGuide.getLanguages());
        assertEquals(request.yearsExperience(), savedGuide.getYearsExperience());
    }

    @Test
    @DisplayName("Should throw exception when creating guide with non-existent user")
    void createGuide_withNonExistentUser_shouldThrowException() {
        // Given
        when(userRepository.findById(request.userId())).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.createGuide(request)
        );
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(request.userId());
        verify(guideRepository, never()).save(any(Guide.class));
    }

    @Test
    @DisplayName("Should throw exception when creating guide with non-existent provider")
    void createGuide_withNonExistentProvider_shouldThrowException() {
        // Given
        when(userRepository.findById(request.userId())).thenReturn(Optional.of(user));
        when(providerRepository.findById(request.providerId())).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.createGuide(request)
        );
        assertEquals("Provider not found", exception.getMessage());
        verify(userRepository).findById(request.userId());
        verify(providerRepository).findById(request.providerId());
        verify(guideRepository, never()).save(any(Guide.class));
    }

    @Test
    @DisplayName("Should update guide successfully")
    void updateGuide_success() {
        // Given
        Integer id = 1;
        when(guideRepository.findById(id)).thenReturn(Optional.of(guide));
        when(userRepository.findById(request.userId())).thenReturn(Optional.of(user));
        when(providerRepository.findById(request.providerId())).thenReturn(Optional.of(provider));
        when(guideRepository.save(any(Guide.class))).thenReturn(guide);

        // When
        GuideResponse result = service.updateGuide(id, request);

        // Then
        assertNotNull(result);
        verify(guideRepository).findById(id);
        verify(userRepository).findById(request.userId());
        verify(providerRepository).findById(request.providerId());
        verify(guideRepository).save(guideCaptor.capture());

        Guide updatedGuide = guideCaptor.getValue();
        assertEquals(request.bio(), updatedGuide.getBio());
        assertEquals(request.specialties().toString(), updatedGuide.getSpecialties());
        assertEquals(request.languages().toString(), updatedGuide.getLanguages());
        assertEquals(request.yearsExperience(), updatedGuide.getYearsExperience());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent guide")
    void updateGuide_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(guideRepository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateGuide(id, request)
        );
        assertEquals("Guide not found", exception.getMessage());
        verify(guideRepository).findById(id);
        verify(guideRepository, never()).save(any(Guide.class));
    }

    @Test
    @DisplayName("Should delete guide successfully")
    void deleteGuide_success() {
        // Given
        Integer id = 1;
        when(guideRepository.existsById(id)).thenReturn(true);

        // When
        service.deleteGuide(id);

        // Then
        verify(guideRepository).existsById(id);
        verify(guideRepository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent guide")
    void deleteGuide_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(guideRepository.existsById(id)).thenReturn(false);

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteGuide(id)
        );
        assertEquals("Guide not found", exception.getMessage());
        verify(guideRepository).existsById(id);
        verify(guideRepository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Should find guide by id when exists")
    void findById_whenExists_shouldReturnGuide() {
        // Given
        Integer id = 1;
        when(guideRepository.findById(id)).thenReturn(Optional.of(guide));

        // When
        Optional<GuideResponse> result = service.findById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(guide.getId(), result.get().id());
        verify(guideRepository).findById(id);
    }

    @Test
    @DisplayName("Should return empty optional when guide id doesn't exist")
    void findById_whenNotExists_shouldReturnEmpty() {
        // Given
        Integer id = 999;
        when(guideRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<GuideResponse> result = service.findById(id);

        // Then
        assertFalse(result.isPresent());
        verify(guideRepository).findById(id);
    }

    @Test
    @DisplayName("Should find all active guides")
    void findAllActive_shouldReturnActiveGuides() {
        // Given
        when(guideRepository.findByIsActiveTrue()).thenReturn(guides);

        // When
        List<GuideResponse> result = service.findAllActive();

        // Then
        assertNotNull(result);
        assertEquals(guides.size(), result.size());
        verify(guideRepository).findByIsActiveTrue();
    }

    @Test
    @DisplayName("Should find guides by provider")
    void findByProvider_shouldReturnProviderGuides() {
        // Given
        when(guideRepository.findByProvider(provider)).thenReturn(guides);

        // When
        List<GuideResponse> result = service.findByProvider(provider);

        // Then
        assertNotNull(result);
        assertEquals(guides.size(), result.size());
        verify(guideRepository).findByProvider(provider);
    }

    @Test
    @DisplayName("Should find guide by user when exists")
    void findByUser_whenExists_shouldReturnGuide() {
        // Given
        when(guideRepository.findByUser(user)).thenReturn(Optional.of(guide));

        // When
        Optional<GuideResponse> result = service.findByUser(user);

        // Then
        assertTrue(result.isPresent());
        assertEquals(guide.getId(), result.get().id());
        verify(guideRepository).findByUser(user);
    }

    @Test
    @DisplayName("Should find guides by years of experience")
    void findByYearsExperienceGreaterThanEqual_shouldReturnMatchingGuides() {
        // Given
        Integer years = 5;
        when(guideRepository.findByYearsExperienceGreaterThanEqual(years)).thenReturn(guides);

        // When
        List<GuideResponse> result = service.findByYearsExperienceGreaterThanEqual(years);

        // Then
        assertNotNull(result);
        assertEquals(guides.size(), result.size());
        verify(guideRepository).findByYearsExperienceGreaterThanEqual(years);
    }

    @Test
    @DisplayName("Should find active guides by provider")
    void findActiveGuidesByProvider_shouldReturnActiveGuides() {
        // Given
        Integer providerId = 1;
        when(guideRepository.findActiveGuidesByProvider(providerId)).thenReturn(guides);

        // When
        List<GuideResponse> result = service.findActiveGuidesByProvider(providerId);

        // Then
        assertNotNull(result);
        assertEquals(guides.size(), result.size());
        verify(guideRepository).findActiveGuidesByProvider(providerId);
    }

    @Test
    @DisplayName("Should find guides by language and active status")
    void findByLanguageAndActive_shouldReturnMatchingGuides() {
        // Given
        String language = "English";
        when(guideRepository.findByLanguageAndActive(language)).thenReturn(guides);

        // When
        List<GuideResponse> result = service.findByLanguageAndActive(language);

        // Then
        assertNotNull(result);
        assertEquals(guides.size(), result.size());
        verify(guideRepository).findByLanguageAndActive(language);
    }

    @Test
    @DisplayName("Should find guides by specialty and active status")
    void findBySpecialtyAndActive_shouldReturnMatchingGuides() {
        // Given
        String specialty = "Hiking";
        when(guideRepository.findBySpecialtyAndActive(specialty)).thenReturn(guides);

        // When
        List<GuideResponse> result = service.findBySpecialtyAndActive(specialty);

        // Then
        assertNotNull(result);
        assertEquals(guides.size(), result.size());
        verify(guideRepository).findBySpecialtyAndActive(specialty);
    }

    @Test
    @DisplayName("Should activate guide successfully")
    void activateGuide_success() {
        // Given
        Integer id = 1;
        when(guideRepository.findById(id)).thenReturn(Optional.of(guide));

        // When
        service.activateGuide(id);

        // Then
        verify(guideRepository).findById(id);
        verify(guideRepository).save(guideCaptor.capture());
        assertTrue(guideCaptor.getValue().getIsActive());
    }

    @Test
    @DisplayName("Should deactivate guide successfully")
    void deactivateGuide_success() {
        // Given
        Integer id = 1;
        when(guideRepository.findById(id)).thenReturn(Optional.of(guide));

        // When
        service.deactivateGuide(id);

        // Then
        verify(guideRepository).findById(id);
        verify(guideRepository).save(guideCaptor.capture());
        assertFalse(guideCaptor.getValue().getIsActive());
    }

    @Test
    @DisplayName("Should check if guide exists by id")
    void existsById_whenExists_returnsTrue() {
        // Given
        Integer id = 1;
        when(guideRepository.existsById(id)).thenReturn(true);

        // When
        boolean result = service.existsById(id);

        // Then
        assertTrue(result);
        verify(guideRepository).existsById(id);
    }

    @Test
    @DisplayName("Should check if guide exists by user")
    void existsByUser_returnsFalse() {
        // Given/When
        boolean result = service.existsByUser(user);

        // Then - currently implemented to always return false
        assertFalse(result);
    }
}
