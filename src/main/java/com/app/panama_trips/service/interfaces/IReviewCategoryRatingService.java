package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.ReviewCategoryRatingRequest;
import com.app.panama_trips.presentation.dto.ReviewCategoryRatingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IReviewCategoryRatingService {
  // CRUD operations
  Page<ReviewCategoryRatingResponse> getAllReviewCategoryRatings(Pageable pageable);
  ReviewCategoryRatingResponse getReviewCategoryRatingById(Integer reviewId, Integer categoryId);
  ReviewCategoryRatingResponse saveReviewCategoryRating(ReviewCategoryRatingRequest request);
  ReviewCategoryRatingResponse updateReviewCategoryRating(Integer reviewId, Integer categoryId, ReviewCategoryRatingRequest request);
  void deleteReviewCategoryRating(Integer reviewId, Integer categoryId);

  // Business operations
  List<ReviewCategoryRatingResponse> getRatingsByReview(Integer reviewId);
  List<ReviewCategoryRatingResponse> getRatingsByCategory(Integer categoryId);
  Double getAverageRatingByCategory(Integer categoryId);
  Map<Integer, Double> getAverageRatingsByCategoryForTour(Long tourPlanId);
  Long countRatingsGreaterThanEqual(Integer minRating);
  void deleteRatingsByReview(Integer reviewId);
  boolean existsByReviewAndCategory(Integer reviewId, Integer categoryId);
}
