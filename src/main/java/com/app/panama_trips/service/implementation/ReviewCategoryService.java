package com.app.panama_trips.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.panama_trips.persistence.entity.ReviewCategory;
import com.app.panama_trips.persistence.repository.ReviewCategoryRepository;
import com.app.panama_trips.presentation.dto.ReviewCategoryRequest;
import com.app.panama_trips.presentation.dto.ReviewCategoryResponse;
import com.app.panama_trips.service.interfaces.IReviewCategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewCategoryService implements IReviewCategoryService {

  private final ReviewCategoryRepository repository;

  @Override
  @Transactional(readOnly = true)
  public Page<ReviewCategoryResponse> getAllReviewCategories(Pageable pageable) {
    return repository.findAll(pageable)
        .map(ReviewCategoryResponse::new);
  }

  @Override
  public ReviewCategoryResponse getReviewCategoryById(Integer id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getReviewCategoryById'");
  }

  @Override
  public ReviewCategoryResponse saveReviewCategory(ReviewCategoryRequest request) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'saveReviewCategory'");
  }

  @Override
  public ReviewCategoryResponse updateReviewCategory(Integer id, ReviewCategoryRequest request) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateReviewCategory'");
  }

  @Override
  public void deleteReviewCategory(Integer id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteReviewCategory'");
  }

  // Helper methods
  private ReviewCategory builderFromRequest(ReviewCategoryRequest request) {
    return ReviewCategory.builder()
        .name(request.name())
        .description(request.description())
        .build();
  }

  private void updateCategoryFields(ReviewCategory category, ReviewCategoryRequest request) {
    category.setName(request.name());
    category.setDescription(request.description());
  }
}
