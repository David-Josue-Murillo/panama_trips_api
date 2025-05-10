package com.app.panama_trips.service;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourPlanImage;
import com.app.panama_trips.persistence.repository.TourPlanImageRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.presentation.dto.TourPlanImageRequest;
import com.app.panama_trips.presentation.dto.TourPlanImageResponse;
import com.app.panama_trips.service.implementation.TourPlanImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.app.panama_trips.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourPlanImageServiceTest {

    @Mock
    private TourPlanImageRepository tourPlanImageRepository;

    @Mock
    private TourPlanRepository tourPlanRepository;

    @InjectMocks
    private TourPlanImageService tourPlanImageService;

    @Test
    void getAllTourPlanImages_shouldReturnAllTourPlanImages() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<TourPlanImage> page = new PageImpl<>(tourPlanImageListsMock, pageable, tourPlanImageListsMock.size());
        when(tourPlanImageRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<TourPlanImageResponse> responses = tourPlanImageService.getAllTourPlanImages(pageable);

        // Then
        assertNotNull(responses);
        assertEquals(tourPlanImageListsMock.size(), responses.getTotalElements());
        assertEquals(tourPlanImageListsMock.size(), responses.getContent().size());
    }

    @Test
    void getTourPlanImageById_shouldReturnTourPlanImage() {
        // Given
        TourPlanImage tourPlanImage = tourPlanImageOneMock;
        when(tourPlanImageRepository.findById(anyInt())).thenReturn(Optional.of(tourPlanImage));

        // When
        TourPlanImageResponse response = tourPlanImageService.getTourPlanImageById(1);

        // Then
        assertNotNull(response);
        assertEquals(tourPlanImage.getId(), response.id());
        assertEquals(tourPlanImage.getImageUrl(), response.imageUrl());
        assertEquals(tourPlanImage.getDisplayOrder(), response.displayOrder());
        assertEquals(tourPlanImage.getIsMain(), response.isMain());
    }

    @Test
    void getTourPlanImageById_shouldThrowResourceNotFoundException() {
        // Given
        when(tourPlanImageRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> tourPlanImageService.getTourPlanImageById(1));
    }

    @Test
    void saveTourPlanImage_shouldCreateAndReturnNewTourPlanImage() {
        // Given
        TourPlanImageRequest request = tourPlanImageRequestMock;
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.of(tourPlanOneMock));
        when(tourPlanImageRepository.existsByTourPlanAndImageUrl(any(TourPlan.class), anyString())).thenReturn(false);
        when(tourPlanImageRepository.save(any(TourPlanImage.class))).thenReturn(tourPlanImageOneMock);

        // When
        TourPlanImageResponse response = tourPlanImageService.saveTourPlanImage(request);

        // Then
        assertNotNull(response);
        assertEquals(tourPlanImageOneMock.getId(), response.id());
        assertEquals(tourPlanImageOneMock.getImageUrl(), response.imageUrl());
        assertEquals(tourPlanImageOneMock.getDisplayOrder(), response.displayOrder());
        assertEquals(tourPlanImageOneMock.getIsMain(), response.isMain());

        verify(tourPlanImageRepository).save(any(TourPlanImage.class));
    }

    @Test
    void updateTourPlanImage_shouldUpdateAndReturnTourPlanImage() {
        // Given
        int imageId = 1;
        TourPlanImageRequest request = tourPlanImageRequestMock;

        TourPlanImage existingImage = tourPlanImageListsMock.getFirst();
        TourPlanImage updatedImage = new TourPlanImage();
        updatedImage.setId(imageId);
        updatedImage.setTourPlan(DataProvider.tourPlanOneMock);
        updatedImage.setImageUrl(request.getImageUrl());
        updatedImage.setDisplayOrder(request.getDisplayOrder());
        updatedImage.setIsMain(request.getIsMain());

        when(tourPlanImageRepository.findById(imageId)).thenReturn(Optional.of(existingImage));
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.of(tourPlanOneMock));
        when(tourPlanImageRepository.save(any(TourPlanImage.class))).thenReturn(updatedImage);

        // When
        TourPlanImageResponse response = tourPlanImageService.updateTourPlanImage(imageId, request);

        // Then
        assertNotNull(response);
        assertEquals(updatedImage.getId(), response.id());
        assertEquals(updatedImage.getImageUrl(), response.imageUrl());
        assertEquals(updatedImage.getDisplayOrder(), response.displayOrder());
        assertEquals(updatedImage.getIsMain(), response.isMain());

        verify(tourPlanImageRepository).findById(imageId);
        verify(tourPlanImageRepository).save(any(TourPlanImage.class));
    }

    @Test
    void updateTourPlanImage_shouldThrowResourceNotFoundException() {
        // Given
        int imageId = 999;
        TourPlanImageRequest request = tourPlanImageRequestMock;

        when(tourPlanImageRepository.findById(imageId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> tourPlanImageService.updateTourPlanImage(imageId, request));
    }

    @Test
    void deleteTourPlanImage_shouldDeleteTourPlanImage() {
        // Given
        int imageId = 1;
        when(tourPlanImageRepository.existsById(anyInt())).thenReturn(true);

        // When
        tourPlanImageService.deleteTourPlanImage(imageId);

        // Then
        verify(tourPlanImageRepository, times(1)).deleteById(imageId);
    }

    @Test
    void deleteTourPlanImage_shouldThrowResourceNotFoundException() {
        // Given
        int imageId = 999;
        when(tourPlanImageRepository.existsById(imageId)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> tourPlanImageService.deleteTourPlanImage(imageId));
    }

    @Test
    void getTourPlanImagesByTourPlanId_shouldReturnImages() {
        // Given
        int tourPlanId = 1;
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlanOneMock));
        when(tourPlanImageRepository.findByTourPlan(tourPlanOneMock)).thenReturn(tourPlanImageListsMock);

        // When
        List<TourPlanImageResponse> responses = tourPlanImageService.getTourPlanImagesByTourPlanId(tourPlanId);

        // Then
        assertNotNull(responses);
        assertEquals(tourPlanImageListsMock.size(), responses.size());
        verify(tourPlanImageRepository).findByTourPlan(tourPlanOneMock);
    }

    @Test
    void getTourPlanImagesByTourPlanIdOrderByDisplayOrder_shouldReturnImages() {
        // Given
        int tourPlanId = 1;
        when(tourPlanImageRepository.findByTourPlanIdOrderByDisplayOrder(tourPlanId)).thenReturn(tourPlanImageListsMock);

        // When
        List<TourPlanImageResponse> responses = tourPlanImageService.getTourPlanImagesByTourPlanIdOrderByDisplayOrder(tourPlanId);

        // Then
        assertNotNull(responses);
        assertEquals(tourPlanImageListsMock.size(), responses.size());
        verify(tourPlanImageRepository).findByTourPlanIdOrderByDisplayOrder(tourPlanId);
    }

    @Test
    void getMainImageByTourPlanId_shouldReturnImages() {
        // Given
        int tourPlanId = 1;
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlanOneMock));
        when(tourPlanImageRepository.findByTourPlanAndIsMainTrue(tourPlanOneMock)).thenReturn(Optional.of(tourPlanImageOneMock));

        // When
        TourPlanImageResponse responses = tourPlanImageService.getMainImageByTourPlanId(tourPlanId);

        // Then
        assertNotNull(responses);
        verify(tourPlanImageRepository).findByTourPlanAndIsMainTrue(tourPlanOneMock);
    }

    @Test
    void getMainImageByTourPlanId_shouldThrowExceptionWhenTourPlanNotFound() {
        // Given
        int tourPlanId = 999;
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> tourPlanImageService.getMainImageByTourPlanId(tourPlanId));
    }

    @Test
    void getMainImageByTourPlanId_shouldThrowExceptionWhenMainImageNotFound() {
        // Given
        int tourPlanId = 1;
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlanOneMock));
        when(tourPlanImageRepository.findByTourPlanAndIsMainTrue(tourPlanOneMock)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> tourPlanImageService.getMainImageByTourPlanId(tourPlanId));
    }

    @Test
    void getNonMainImagesByTourPlanIdOrdered_shouldReturnNonMainImages() {
        // Given
        int tourPlanId = 1;
        List<TourPlanImage> nonMainImages = tourPlanImageListsMock.subList(1, 2);

        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlanOneMock));
        when(tourPlanImageRepository.findByTourPlanAndIsMainFalseOrderByDisplayOrderAsc(tourPlanOneMock))
                .thenReturn(nonMainImages);

        // When
        List<TourPlanImageResponse> responses = tourPlanImageService.getNonMainImagesByTourPlanIdOrdered(tourPlanId);

        // Then
        assertNotNull(responses);
        assertEquals(nonMainImages.size(), responses.size());
        verify(tourPlanImageRepository).findByTourPlanAndIsMainFalseOrderByDisplayOrderAsc(tourPlanOneMock);
    }

    @Test
    void getNonMainImagesByTourPlanIdOrdered_shouldThrowExceptionWhenTourPlanNotFound() {
        // Given
        int tourPlanId = 999;
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> tourPlanImageService.getNonMainImagesByTourPlanIdOrdered(tourPlanId));
    }

    @Test
    void getMaxDisplayOrderForTourPlan_shouldReturnMaxDisplayOrder() {
        // Given
        int tourPlanId = 1;
        Integer maxOrder = 5;
        when(tourPlanImageRepository.findMaxDisplayOrderByTourPlanId(tourPlanId)).thenReturn(maxOrder);

        // When
        Integer result = tourPlanImageService.getMaxDisplayOrderForTourPlan(tourPlanId);

        // Then
        assertEquals(maxOrder, result);
        verify(tourPlanImageRepository).findMaxDisplayOrderByTourPlanId(tourPlanId);
    }

    @Test
    void getMaxDisplayOrderForTourPlan_shouldReturnNullWhenNoImagesExist() {
        // Given
        int tourPlanId = 1;
        when(tourPlanImageRepository.findMaxDisplayOrderByTourPlanId(tourPlanId)).thenReturn(null);

        // When
        Integer result = tourPlanImageService.getMaxDisplayOrderForTourPlan(tourPlanId);

        // Then
        assertNull(result);
        verify(tourPlanImageRepository).findMaxDisplayOrderByTourPlanId(tourPlanId);
    }

    @Test
    void countImagesByTourPlanId_shouldReturnCorrectCount() {
        // Given
        int tourPlanId = 1;
        long expectedCount = 3L;
        when(tourPlanImageRepository.countByTourPlanId(tourPlanId)).thenReturn(expectedCount);

        // When
        Long result = tourPlanImageService.countImagesByTourPlanId(tourPlanId);

        // Then
        assertEquals(expectedCount, result);
        verify(tourPlanImageRepository).countByTourPlanId(tourPlanId);
    }

    @Test
    void deleteAllImagesByTourPlanId_shouldDeleteAllImages() {
        // Given
        int tourPlanId = 1;
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlanOneMock));

        // When
        tourPlanImageService.deleteAllImagesByTourPlanId(tourPlanId);

        // Then
        verify(tourPlanRepository).findById(tourPlanId);
        verify(tourPlanImageRepository).deleteByTourPlan(tourPlanOneMock);
    }

    @Test
    void deleteAllImagesByTourPlanId_shouldThrowExceptionWhenTourPlanNotFound() {
        // Given
        int tourPlanId = 999;
        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> tourPlanImageService.deleteAllImagesByTourPlanId(tourPlanId));
    }

    @Test
    void existsImageWithUrlForTourPlan_shouldReturnTrueWhenImageExists() {
        // Given
        int tourPlanId = 1;
        String imageUrl = "http://example.com/image.jpg";

        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlanOneMock));
        when(tourPlanImageRepository.existsByTourPlanAndImageUrl(tourPlanOneMock, imageUrl)).thenReturn(true);

        // When
        boolean result = tourPlanImageService.existsImageWithUrlForTourPlan(tourPlanId, imageUrl);

        // Then
        assertTrue(result);
        verify(tourPlanRepository).findById(tourPlanId);
        verify(tourPlanImageRepository).existsByTourPlanAndImageUrl(tourPlanOneMock, imageUrl);
    }

    @Test
    void existsImageWithUrlForTourPlan_shouldReturnFalseWhenImageDoesNotExist() {
        // Given
        int tourPlanId = 1;
        String imageUrl = "http://example.com/new-image.jpg";

        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.of(tourPlanOneMock));
        when(tourPlanImageRepository.existsByTourPlanAndImageUrl(tourPlanOneMock, imageUrl)).thenReturn(false);

        // When
        boolean result = tourPlanImageService.existsImageWithUrlForTourPlan(tourPlanId, imageUrl);

        // Then
        assertFalse(result);
        verify(tourPlanRepository).findById(tourPlanId);
        verify(tourPlanImageRepository).existsByTourPlanAndImageUrl(tourPlanOneMock, imageUrl);
    }

    @Test
    void existsImageWithUrlForTourPlan_shouldThrowExceptionWhenTourPlanNotFound() {
        // Given
        int tourPlanId = 999;
        String imageUrl = "http://example.com/image.jpg";

        when(tourPlanRepository.findById(tourPlanId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> tourPlanImageService.existsImageWithUrlForTourPlan(tourPlanId, imageUrl));
    }
}
