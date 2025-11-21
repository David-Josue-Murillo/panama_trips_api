package com.app.panama_trips.service;

import static com.app.panama_trips.DataProvider.reviewCategoryOneMock;
import static com.app.panama_trips.DataProvider.reviewCategoryRatingListMock;
import static com.app.panama_trips.DataProvider.reviewCategoryRatingOneMock;
import static com.app.panama_trips.DataProvider.reviewCategoryRatingRequestMock;
import static com.app.panama_trips.DataProvider.reviewOneMock;

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
import com.app.panama_trips.persistence.entity.Review;
import com.app.panama_trips.persistence.entity.ReviewCategory;
import com.app.panama_trips.persistence.entity.ReviewCategoryRating;
import com.app.panama_trips.persistence.entity.ReviewCategoryRatingId;
import com.app.panama_trips.persistence.repository.ReviewCategoryRatingRepository;
import com.app.panama_trips.persistence.repository.ReviewCategoryRepository;
import com.app.panama_trips.persistence.repository.ReviewRepository;
import com.app.panama_trips.presentation.dto.ReviewCategoryRatingRequest;
import com.app.panama_trips.presentation.dto.ReviewCategoryRatingResponse;
import com.app.panama_trips.service.implementation.ReviewCategoryRatingService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewCategoryRatingServiceTest {

    @Mock
    ReviewCategoryRatingRepository repository;

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    ReviewCategoryRepository reviewCategoryRepository;

    @InjectMocks
    private ReviewCategoryRatingService service;

    @Captor
    private ArgumentCaptor<ReviewCategoryRating> categoryRatingCaptor;

    private ReviewCategoryRating categoryRating;
    private List<ReviewCategoryRating> categoryRatingList;
    private ReviewCategoryRatingRequest request;
    private Review review;
    private ReviewCategory category;

    @BeforeEach
    void setUp() {
        categoryRating = reviewCategoryRatingOneMock();
        categoryRatingList = reviewCategoryRatingListMock();
        request = reviewCategoryRatingRequestMock();
        review = reviewOneMock();
        category = reviewCategoryOneMock();
        category.setId(1);
    }

    // CRUD Operations Tests
    @Test
    @DisplayName("Should return all review category ratings when getAllReviewCategoryRatings is called with pagination")
    void getAllReviewCategoryRatings_shouldReturnAllData() {
        // Given
        Page<ReviewCategoryRating> page = new PageImpl<>(categoryRatingList);
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(page);

        // When
        Page<ReviewCategoryRatingResponse> result = service.getAllReviewCategoryRatings(pageable);

        // Then
        assertNotNull(result);
        assertEquals(categoryRatingList.size(), result.getTotalElements());
        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("Should return review category rating by id when exists")
    void getReviewCategoryRatingById_whenExists_shouldReturnRating() {
        // Given
        Integer reviewId = 1;
        Integer categoryId = 1;
        ReviewCategoryRatingId id = ReviewCategoryRatingId.builder()
                .reviewId(reviewId)
                .categoryId(categoryId)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(categoryRating));

        // When
        ReviewCategoryRatingResponse result = service.getReviewCategoryRatingById(reviewId, categoryId);

        // Then
        assertNotNull(result);
        assertEquals(categoryRating.getId().getReviewId(), result.reviewId());
        assertEquals(categoryRating.getId().getCategoryId(), result.categoryId());
        assertEquals(categoryRating.getRating(), result.rating());
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should throw exception when getting review category rating by id that doesn't exist")
    void getReviewCategoryRatingById_whenNotExists_shouldThrowException() {
        // Given
        Integer reviewId = 999;
        Integer categoryId = 999;
        ReviewCategoryRatingId id = ReviewCategoryRatingId.builder()
                .reviewId(reviewId)
                .categoryId(categoryId)
                .build();

        when(repository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getReviewCategoryRatingById(reviewId, categoryId));
        assertEquals("Review category rating not found", exception.getMessage());
        verify(repository).findById(id);
    }
}
