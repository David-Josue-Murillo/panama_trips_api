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
import com.app.panama_trips.persistence.entity.ReviewCategory;
import com.app.panama_trips.persistence.repository.ReviewCategoryRepository;
import com.app.panama_trips.presentation.dto.ReviewCategoryRequest;
import com.app.panama_trips.presentation.dto.ReviewCategoryResponse;
import com.app.panama_trips.service.implementation.ReviewCategoryService;

import static com.app.panama_trips.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewCategoryServiceTest {
    
    @Mock
    private ReviewCategoryRepository repository;

    @InjectMocks
    private ReviewCategoryService service;

    @Captor
    private ArgumentCaptor<ReviewCategory> categoryCaptor;

    private ReviewCategory category;
    private List<ReviewCategory> categoriesList;
    private ReviewCategoryRequest request;

    @BeforeEach
    void setUp() {
        category = reviewCategoryOneMock();
        category.setId(1);
        categoriesList = reviewCategoryListMock();
        request = reviewCategoryRequestMock();
    }

    // CRUD Operations Tests
    @Test
    @DisplayName("Should return all review categories when getAllReviewCategories is called with pagination")
    void getAllReviewCategories_shouldReturnAllData() {
        // Given
        Page<ReviewCategory> page = new PageImpl<>(categoriesList);
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(page);

        // When
        Page<ReviewCategoryResponse> result = service.getAllReviewCategories(pageable);

        // Then
        assertNotNull(result);
        assertEquals(categoriesList.size(), result.getTotalElements());
        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("Should return review category by id when exists")
    void getReviewCategoryById_whenExistsEu_shouldReturnCategory() {
        // Given
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.of(category));

        // When
        ReviewCategoryResponse result = service.getReviewCategoryById(id);

        // Then
        assertNotNull(result);
        assertEquals(category.getId(), result.id());
        assertEquals(category.getName(), result.name());
        assertEquals(category.getDescription(), result.description());
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should throw exception when getting review category by id that doesn't exist")
    void getReviewCategoryById_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getReviewCategoryById(id));
        assertEquals("Review category not found", exception.getMessage());
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should save review category successfully")
    void saveReviewCategory_success() {
        // Given
        ReviewCategory savedCategory = reviewCategoryOneMock();
        savedCategory.setId(1);
        when(repository.save(any(ReviewCategory.class))).thenReturn(savedCategory);

        // When
        ReviewCategoryResponse result = service.saveReviewCategory(request);

        // Then
        assertNotNull(result);
        assertEquals(savedCategory.getId(), result.id());
        assertEquals(savedCategory.getName(), result.name());
        assertEquals(savedCategory.getDescription(), result.description());
        verify(repository).save(categoryCaptor.capture());
        ReviewCategory savedCategoryFromCapture = categoryCaptor.getValue();
        assertEquals(request.name(), savedCategoryFromCapture.getName());
        assertEquals(request.description(), savedCategoryFromCapture.getDescription());
    }

    @Test
    @DisplayName("Should update review category successfully")
    void updateReviewCategory_success() {
        // Given
        Integer id = 1;
        ReviewCategoryRequest updateRequest = new ReviewCategoryRequest("Updated Quality", "Updated description");
        ReviewCategory updatedCategory = reviewCategoryOneMock();
        updatedCategory.setId(id);
        updatedCategory.setName("Updated Quality");
        updatedCategory.setDescription("Updated description");

        when(repository.findById(id)).thenReturn(Optional.of(category));
        when(repository.save(any(ReviewCategory.class))).thenReturn(updatedCategory);

        // When
        ReviewCategoryResponse result = service.updateReviewCategory(id, updateRequest);

        // Then
        assertNotNull(result);
        assertEquals(updatedCategory.getId(), result.id());
        assertEquals(updateRequest.name(), result.name());
        assertEquals(updateRequest.description(), result.description());
        verify(repository).findById(id);
        verify(repository).save(categoryCaptor.capture());
        ReviewCategory updatedCategoryFromCapture = categoryCaptor.getValue();
        assertEquals(updateRequest.name(), updatedCategoryFromCapture.getName());
        assertEquals(updateRequest.description(), updatedCategoryFromCapture.getDescription());
    }
}
