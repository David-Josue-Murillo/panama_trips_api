package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.ReviewCategoryRequest;
import com.app.panama_trips.presentation.dto.ReviewCategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IReviewCategoryService {
  // CRUD operations
  Page<ReviewCategoryResponse> getAllReviewCategories(Pageable pageable);
  ReviewCategoryResponse getReviewCategoryById(Integer id);
  ReviewCategoryResponse saveReviewCategory(ReviewCategoryRequest request);
  ReviewCategoryResponse updateReviewCategory(Integer id, ReviewCategoryRequest request);
  void deleteReviewCategory(Integer id);
}
