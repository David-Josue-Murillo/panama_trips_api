package com.app.panama_trips.service;

import static com.app.panama_trips.DataProvider.reviewCategoryOneMock;
import static com.app.panama_trips.DataProvider.reviewCategoryRatingListMock;
import static com.app.panama_trips.DataProvider.reviewCategoryRatingOneMock;
import static com.app.panama_trips.DataProvider.reviewCategoryRatingRequestMock;
import static com.app.panama_trips.DataProvider.reviewOneMock;

import java.util.List;
import java.util.Map;
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

    @Test
    @DisplayName("Should save review category rating successfully")
    void saveReviewCategoryRating_success() {
        // Given
        ReviewCategoryRating savedRating = reviewCategoryRatingOneMock();
        when(reviewRepository.findById(request.reviewId().longValue())).thenReturn(Optional.of(review));
        when(reviewCategoryRepository.findById(request.categoryId())).thenReturn(Optional.of(category));
        when(repository.save(any(ReviewCategoryRating.class))).thenReturn(savedRating);

        // When
        ReviewCategoryRatingResponse result = service.saveReviewCategoryRating(request);

        // Then
        assertNotNull(result);
        assertEquals(savedRating.getRating(), result.rating());
        verify(reviewRepository).findById(request.reviewId().longValue());
        verify(reviewCategoryRepository).findById(request.categoryId());
        verify(repository).save(categoryRatingCaptor.capture());
        ReviewCategoryRating savedRatingFromCapture = categoryRatingCaptor.getValue();
        assertEquals(request.rating(), savedRatingFromCapture.getRating());
    }

    @Test
    @DisplayName("Should throw exception when saving review category rating with non-existent review")
    void saveReviewCategoryRating_whenReviewNotExists_shouldThrowException() {
        // Given
        when(reviewRepository.findById(request.reviewId().longValue())).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.saveReviewCategoryRating(request));
        assertEquals("Review not found", exception.getMessage());
        verify(reviewRepository).findById(request.reviewId().longValue());
        verify(reviewCategoryRepository, never()).findById(anyInt());
        verify(repository, never()).save(any(ReviewCategoryRating.class));
    }

    @Test
    @DisplayName("Should throw exception when saving review category rating with non-existent category")
    void saveReviewCategoryRating_whenCategoryNotExists_shouldThrowException() {
        // Given
        when(reviewRepository.findById(request.reviewId().longValue())).thenReturn(Optional.of(review));
        when(reviewCategoryRepository.findById(request.categoryId())).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.saveReviewCategoryRating(request));
        assertEquals("Review category not found", exception.getMessage());
        verify(reviewRepository).findById(request.reviewId().longValue());
        verify(reviewCategoryRepository).findById(request.categoryId());
        verify(repository, never()).save(any(ReviewCategoryRating.class));
    }

    @Test
    @DisplayName("Should update review category rating successfully")
    void updateReviewCategoryRating_success() {
        // Given
        Integer reviewId = 1;
        Integer categoryId = 1;
        ReviewCategoryRatingRequest updateRequest = new ReviewCategoryRatingRequest(1, 1, 5);
        ReviewCategoryRatingId id = ReviewCategoryRatingId.builder()
                .reviewId(reviewId)
                .categoryId(categoryId)
                .build();

        ReviewCategoryRating updatedRating = reviewCategoryRatingOneMock();
        updatedRating.setRating(5);

        when(repository.findById(id)).thenReturn(Optional.of(categoryRating));
        when(repository.save(any(ReviewCategoryRating.class))).thenReturn(updatedRating);

        // When
        ReviewCategoryRatingResponse result = service.updateReviewCategoryRating(reviewId, categoryId, updateRequest);

        // Then
        assertNotNull(result);
        assertEquals(updateRequest.rating(), result.rating());
        verify(repository).findById(id);
        verify(repository).save(categoryRatingCaptor.capture());
        ReviewCategoryRating updatedRatingFromCapture = categoryRatingCaptor.getValue();
        assertEquals(updateRequest.rating(), updatedRatingFromCapture.getRating());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent review category rating")
    void updateReviewCategoryRating_whenNotExists_shouldThrowException() {
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
                () -> service.updateReviewCategoryRating(reviewId, categoryId, request));
        assertEquals("Review category rating not found", exception.getMessage());
        verify(repository).findById(id);
        verify(repository, never()).save(any(ReviewCategoryRating.class));
    }

    @Test
    @DisplayName("Should delete review category rating successfully when exists")
    void deleteReviewCategoryRating_whenExists_success() {
        // Given
        Integer reviewId = 1;
        Integer categoryId = 1;
        ReviewCategoryRatingId id = ReviewCategoryRatingId.builder()
                .reviewId(reviewId)
                .categoryId(categoryId)
                .build();

        when(repository.existsById(id)).thenReturn(true);

        // When
        service.deleteReviewCategoryRating(reviewId, categoryId);

        // Then
        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent review category rating")
    void deleteReviewCategoryRating_whenNotExists_shouldThrowException() {
        // Given
        Integer reviewId = 999;
        Integer categoryId = 999;
        ReviewCategoryRatingId id = ReviewCategoryRatingId.builder()
                .reviewId(reviewId)
                .categoryId(categoryId)
                .build();

        when(repository.existsById(id)).thenReturn(false);

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteReviewCategoryRating(reviewId, categoryId));
        assertEquals("Review category rating not found", exception.getMessage());
        verify(repository).existsById(id);
        verify(repository, never()).deleteById(any(ReviewCategoryRatingId.class));
    }

    // Business Operations Tests
    @Test
    @DisplayName("Should return ratings by review when review exists")
    void getRatingsByReview_whenReviewExists_shouldReturnRatings() {
        // Given
        Integer reviewId = 1;
        List<ReviewCategoryRating> ratings = List.of(categoryRating);

        when(reviewRepository.findById(reviewId.longValue())).thenReturn(Optional.of(review));
        when(repository.findByReview(review)).thenReturn(ratings);

        // When
        List<ReviewCategoryRatingResponse> result = service.getRatingsByReview(reviewId);

        // Then
        assertNotNull(result);
        assertEquals(ratings.size(), result.size());
        verify(reviewRepository).findById(reviewId.longValue());
        verify(repository).findByReview(review);
    }

    @Test
    @DisplayName("Should throw exception when getting ratings by non-existent review")
    void getRatingsByReview_whenReviewNotExists_shouldThrowException() {
        // Given
        Integer reviewId = 999;
        when(reviewRepository.findById(reviewId.longValue())).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getRatingsByReview(reviewId));
        assertEquals("Review not found", exception.getMessage());
        verify(reviewRepository).findById(reviewId.longValue());
        verify(repository, never()).findByReview(any(Review.class));
    }

    @Test
    @DisplayName("Should return ratings by category when category exists")
    void getRatingsByCategory_whenCategoryExists_shouldReturnRatings() {
        // Given
        Integer categoryId = 1;
        List<ReviewCategoryRating> ratings = List.of(categoryRating);

        when(reviewCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(repository.findByCategory(category)).thenReturn(ratings);

        // When
        List<ReviewCategoryRatingResponse> result = service.getRatingsByCategory(categoryId);

        // Then
        assertNotNull(result);
        assertEquals(ratings.size(), result.size());
        verify(reviewCategoryRepository).findById(categoryId);
        verify(repository).findByCategory(category);
    }

    @Test
    @DisplayName("Should throw exception when getting ratings by non-existent category")
    void getRatingsByCategory_whenCategoryNotExists_shouldThrowException() {
        // Given
        Integer categoryId = 999;
        when(reviewCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getRatingsByCategory(categoryId));
        assertEquals("Review category not found", exception.getMessage());
        verify(reviewCategoryRepository).findById(categoryId);
        verify(repository, never()).findByCategory(any(ReviewCategory.class));
    }

    @Test
    @DisplayName("Should return average rating by category when category exists")
    void getAverageRatingByCategory_whenCategoryExists_shouldReturnAverage() {
        // Given
        Integer categoryId = 1;
        Double average = 4.5;

        when(reviewCategoryRepository.existsById(categoryId)).thenReturn(true);
        when(repository.getAverageRatingByCategory(categoryId)).thenReturn(average);

        // When
        Double result = service.getAverageRatingByCategory(categoryId);

        // Then
        assertNotNull(result);
        assertEquals(average, result);
        verify(reviewCategoryRepository).existsById(categoryId);
        verify(repository).getAverageRatingByCategory(categoryId);
    }

    @Test
    @DisplayName("Should throw exception when getting average rating by non-existent category")
    void getAverageRatingByCategory_whenCategoryNotExists_shouldThrowException() {
        // Given
        Integer categoryId = 999;
        when(reviewCategoryRepository.existsById(categoryId)).thenReturn(false);

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getAverageRatingByCategory(categoryId));
        assertEquals("Review category not found", exception.getMessage());
        verify(reviewCategoryRepository).existsById(categoryId);
        verify(repository, never()).getAverageRatingByCategory(anyInt());
    }

    @Test
    @DisplayName("Should return average ratings by category for tour")
    void getAverageRatingsByCategoryForTour_shouldReturnMap() {
        // Given
        Long tourPlanId = 1L;
        List<Object[]> results = List.of(
                new Object[] { 1, 4.5 },
                new Object[] { 2, 3.8 });

        when(repository.getAverageRatingsByCategoryForTour(tourPlanId)).thenReturn(results);

        // When
        Map<Integer, Double> result = service.getAverageRatingsByCategoryForTour(tourPlanId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(4.5, result.get(1));
        assertEquals(3.8, result.get(2));
        verify(repository).getAverageRatingsByCategoryForTour(tourPlanId);
    }

    @Test
    @DisplayName("Should return empty map when no ratings exist for tour")
    void getAverageRatingsByCategoryForTour_whenNoRatings_shouldReturnEmptyMap() {
        // Given
        Long tourPlanId = 999L;
        when(repository.getAverageRatingsByCategoryForTour(tourPlanId)).thenReturn(List.of());

        // When
        Map<Integer, Double> result = service.getAverageRatingsByCategoryForTour(tourPlanId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository).getAverageRatingsByCategoryForTour(tourPlanId);
    }

    @Test
    @DisplayName("Should count ratings greater than or equal to min rating")
    void countRatingsGreaterThanEqual_shouldReturnCount() {
        // Given
        Integer minRating = 4;
        Long count = 10L;

        when(repository.countByRatingGreaterThanEqual(minRating)).thenReturn(count);

        // When
        Long result = service.countRatingsGreaterThanEqual(minRating);

        // Then
        assertNotNull(result);
        assertEquals(count, result);
        verify(repository).countByRatingGreaterThanEqual(minRating);
    }

    @Test
    @DisplayName("Should delete ratings by review when review exists")
    void deleteRatingsByReview_whenReviewExists_success() {
        // Given
        Integer reviewId = 1;
        when(reviewRepository.findById(reviewId.longValue())).thenReturn(Optional.of(review));

        // When
        service.deleteRatingsByReview(reviewId);

        // Then
        verify(reviewRepository).findById(reviewId.longValue());
        verify(repository).deleteByReview(review);
    }

    @Test
    @DisplayName("Should throw exception when deleting ratings by non-existent review")
    void deleteRatingsByReview_whenReviewNotExists_shouldThrowException() {
        // Given
        Integer reviewId = 999;
        when(reviewRepository.findById(reviewId.longValue())).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteRatingsByReview(reviewId));
        assertEquals("Review not found", exception.getMessage());
        verify(reviewRepository).findById(reviewId.longValue());
        verify(repository, never()).deleteByReview(any(Review.class));
    }

    @Test
    @DisplayName("Should return true when rating exists by review and category")
    void existsByReviewAndCategory_whenExists_shouldReturnTrue() {
        // Given
        Integer reviewId = 1;
        Integer categoryId = 1;

        when(reviewRepository.findById(reviewId.longValue())).thenReturn(Optional.of(review));
        when(reviewCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(repository.existsByReviewAndCategory(review, category)).thenReturn(true);

        // When
        boolean result = service.existsByReviewAndCategory(reviewId, categoryId);

        // Then
        assertTrue(result);
        verify(reviewRepository).findById(reviewId.longValue());
        verify(reviewCategoryRepository).findById(categoryId);
        verify(repository).existsByReviewAndCategory(review, category);
    }

    @Test
    @DisplayName("Should return false when rating does not exist by review and category")
    void existsByReviewAndCategory_whenNotExists_shouldReturnFalse() {
        // Given
        Integer reviewId = 1;
        Integer categoryId = 1;

        when(reviewRepository.findById(reviewId.longValue())).thenReturn(Optional.of(review));
        when(reviewCategoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(repository.existsByReviewAndCategory(review, category)).thenReturn(false);

        // When
        boolean result = service.existsByReviewAndCategory(reviewId, categoryId);

        // Then
        assertFalse(result);
        verify(reviewRepository).findById(reviewId.longValue());
        verify(reviewCategoryRepository).findById(categoryId);
        verify(repository).existsByReviewAndCategory(review, category);
    }

    @Test
    @DisplayName("Should throw exception when checking existence with non-existent review")
    void existsByReviewAndCategory_whenReviewNotExists_shouldThrowException() {
        // Given
        Integer reviewId = 999;
        Integer categoryId = 1;
        when(reviewRepository.findById(reviewId.longValue())).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.existsByReviewAndCategory(reviewId, categoryId));
        assertEquals("Review not found", exception.getMessage());
        verify(reviewRepository).findById(reviewId.longValue());
        verify(reviewCategoryRepository, never()).findById(anyInt());
        verify(repository, never()).existsByReviewAndCategory(any(Review.class), any(ReviewCategory.class));
    }

    @Test
    @DisplayName("Should throw exception when checking existence with non-existent category")
    void existsByReviewAndCategory_whenCategoryNotExists_shouldThrowException() {
        // Given
        Integer reviewId = 1;
        Integer categoryId = 999;
        when(reviewRepository.findById(reviewId.longValue())).thenReturn(Optional.of(review));
        when(reviewCategoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.existsByReviewAndCategory(reviewId, categoryId));
        assertEquals("Review category not found", exception.getMessage());
        verify(reviewRepository).findById(reviewId.longValue());
        verify(reviewCategoryRepository).findById(categoryId);
        verify(repository, never()).existsByReviewAndCategory(any(Review.class), any(ReviewCategory.class));
    }

    // Additional Tests
    @Test
    @DisplayName("Should return empty page when no review category ratings exist")
    void getAllReviewCategoryRatings_whenNoRatingsExist_shouldReturnEmptyPage() {
        // Given
        Page<ReviewCategoryRating> emptyPage = new PageImpl<>(List.of());
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenReturn(emptyPage);

        // When
        Page<ReviewCategoryRatingResponse> result = service.getAllReviewCategoryRatings(pageable);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("Should handle pagination correctly with multiple pages")
    void getAllReviewCategoryRatings_withMultiplePages_shouldHandlePagination() {
        // Given
        List<ReviewCategoryRating> singleRating = List.of(categoryRating);
        Page<ReviewCategoryRating> page = new PageImpl<>(singleRating, PageRequest.of(1, 1), categoryRatingList.size());
        Pageable pageable = PageRequest.of(1, 1);
        when(repository.findAll(pageable)).thenReturn(page);

        // When
        Page<ReviewCategoryRatingResponse> result = service.getAllReviewCategoryRatings(pageable);

        // Then
        assertNotNull(result);
        assertEquals(categoryRatingList.size(), result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getNumber());
        verify(repository).findAll(pageable);
    }
}
