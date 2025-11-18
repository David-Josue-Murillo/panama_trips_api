package com.app.panama_trips.service.implementation;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
  public Page<ReviewCategoryRatingResponse> getAllReviewCategoryRatings(Pageable pageable) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAllReviewCategoryRatings'");
  }

  @Override
  public ReviewCategoryRatingResponse getReviewCategoryRatingById(Integer reviewId, Integer categoryId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getReviewCategoryRatingById'");
  }

  @Override
  public ReviewCategoryRatingResponse saveReviewCategoryRating(ReviewCategoryRatingRequest request) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'saveReviewCategoryRating'");
  }

  @Override
  public ReviewCategoryRatingResponse updateReviewCategoryRating(Integer reviewId, Integer categoryId,
      ReviewCategoryRatingRequest request) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateReviewCategoryRating'");
  }

  @Override
  public void deleteReviewCategoryRating(Integer reviewId, Integer categoryId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteReviewCategoryRating'");
  }

  @Override
  public List<ReviewCategoryRatingResponse> getRatingsByReview(Integer reviewId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getRatingsByReview'");
  }

  @Override
  public List<ReviewCategoryRatingResponse> getRatingsByCategory(Integer categoryId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getRatingsByCategory'");
  }

  @Override
  public Double getAverageRatingByCategory(Integer categoryId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAverageRatingByCategory'");
  }

  @Override
  public Map<Integer, Double> getAverageRatingsByCategoryForTour(Long tourPlanId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAverageRatingsByCategoryForTour'");
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

}
