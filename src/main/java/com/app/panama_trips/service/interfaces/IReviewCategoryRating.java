package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.ReviewCategoryRatingRequest;
import com.app.panama_trips.presentation.dto.ReviewCategoryRatingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IReviewCategoryRating {
  // CRUD operations
  Page<ReviewCategoryRatingResponse> getAllReviewCategoryRatings(Pageable pageable);
  ReviewCategoryRatingResponse getReviewCategoryRatingById(Integer reviewId, Integer categoryId);
  ReviewCategoryRatingResponse saveReviewCategoryRating(ReviewCategoryRatingRequest request);
  ReviewCategoryRatingResponse updateReviewCategoryRating(Integer reviewId, Integer categoryId, ReviewCategoryRatingRequest request);
  void deleteReviewCategoryRating(Integer reviewId, Integer categoryId);
}
