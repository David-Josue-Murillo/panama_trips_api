package com.app.panama_trips.service.implementation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.app.panama_trips.service.interfaces.IReviewCategoryRatingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewCategoryRatingService implements IReviewCategoryRatingService {

  private final ReviewCategoryRatingRepository repository;
  private final ReviewRepository reviewRepository;
  private final ReviewCategoryRepository reviewCategoryRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<ReviewCategoryRatingResponse> getAllReviewCategoryRatings(Pageable pageable) {
    return repository.findAll(pageable)
        .map(ReviewCategoryRatingResponse::new);
  }

  @Override
  @Transactional(readOnly = true)
  public ReviewCategoryRatingResponse getReviewCategoryRatingById(Integer reviewId, Integer categoryId) {
    ReviewCategoryRatingId id = ReviewCategoryRatingId.builder()
        .reviewId(reviewId)
        .categoryId(categoryId)
        .build();
    ReviewCategoryRating rating = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Review category rating not found"));
    return new ReviewCategoryRatingResponse(rating);
  }

  @Override
  @Transactional
  public ReviewCategoryRatingResponse saveReviewCategoryRating(ReviewCategoryRatingRequest request) {
    Review review = reviewRepository.findById(request.reviewId().longValue())
        .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

    ReviewCategory category = reviewCategoryRepository.findById(request.categoryId())
        .orElseThrow(() -> new ResourceNotFoundException("Review category not found"));

    ReviewCategoryRating rating = builderFromRequest(request, review, category);
    return new ReviewCategoryRatingResponse(repository.save(rating));
  }

  @Override
  @Transactional
  public ReviewCategoryRatingResponse updateReviewCategoryRating(Integer reviewId, Integer categoryId,
      ReviewCategoryRatingRequest request) {
    ReviewCategoryRatingId id = ReviewCategoryRatingId.builder()
        .reviewId(reviewId)
        .categoryId(categoryId)
        .build();

    ReviewCategoryRating rating = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Review category rating not found"));

    updateRatingFields(rating, request);
    return new ReviewCategoryRatingResponse(repository.save(rating));
  }

  @Override
  @Transactional
  public void deleteReviewCategoryRating(Integer reviewId, Integer categoryId) {
    ReviewCategoryRatingId id = ReviewCategoryRatingId.builder()
        .reviewId(reviewId)
        .categoryId(categoryId)
        .build();

    if (!repository.existsById(id)) {
      throw new ResourceNotFoundException("Review category rating not found");
    }
    repository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReviewCategoryRatingResponse> getRatingsByReview(Integer reviewId) {
    Review review = reviewRepository.findById(reviewId.longValue())
        .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

    return repository.findByReview(review).stream()
        .map(ReviewCategoryRatingResponse::new)
        .toList();
      }

  @Override
  @Transactional(readOnly = true)
  public List<ReviewCategoryRatingResponse> getRatingsByCategory(Integer categoryId) {
    ReviewCategory category = reviewCategoryRepository.findById(categoryId)
        .orElseThrow(() -> new ResourceNotFoundException("Review category not found"));

    return repository.findByCategory(category).stream()
        .map(ReviewCategoryRatingResponse::new)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public Double getAverageRatingByCategory(Integer categoryId) {
    if (!reviewCategoryRepository.existsById(categoryId)) {
      throw new ResourceNotFoundException("Review category not found");
    }
    return repository.getAverageRatingByCategory(categoryId);
  }

  @Override
  @Transactional(readOnly = true)
  public Map<Integer, Double> getAverageRatingsByCategoryForTour(Long tourPlanId) {
    List<Object[]> results = repository.getAverageRatingsByCategoryForTour(tourPlanId);
    Map<Integer, Double> averages = new HashMap<>();

    for (Object[] result : results) {
        Integer categoryId = (Integer) result[0];
        Double average = (Double) result[1];
        averages.put(categoryId, average);
    }

    return averages;
  }

  @Override
  public Long countRatingsGreaterThanEqual(Integer minRating) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'countRatingsGreaterThanEqual'");
  }

  @Override
  public void deleteRatingsByReview(Integer reviewId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteRatingsByReview'");
  }

  @Override
  public boolean existsByReviewAndCategory(Integer reviewId, Integer categoryId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'existsByReviewAndCategory'");
  }

  // Helper methods
  private ReviewCategoryRating builderFromRequest(ReviewCategoryRatingRequest request, Review review,
      ReviewCategory category) {
    ReviewCategoryRatingId id = ReviewCategoryRatingId.builder()
        .reviewId(review.getId().intValue())
        .categoryId(category.getId())
        .build();

    return ReviewCategoryRating.builder()
        .id(id)
        .review(review)
        .category(category)
        .rating(request.rating())
        .build();
  }

  private void updateRatingFields(ReviewCategoryRating rating, ReviewCategoryRatingRequest request) {
    rating.setRating(request.rating());
  }
}
