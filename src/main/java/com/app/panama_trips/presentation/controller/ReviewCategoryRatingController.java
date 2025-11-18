package com.app.panama_trips.presentation.controller;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import com.app.panama_trips.presentation.dto.ReviewCategoryRatingRequest;
import com.app.panama_trips.presentation.dto.ReviewCategoryRatingResponse;
import com.app.panama_trips.service.implementation.ReviewCategoryRatingService;

@RestController
@RequestMapping("/api/review-category-ratings")
@RequiredArgsConstructor
public class ReviewCategoryRatingController {

  private final ReviewCategoryRatingService service;
  
  // CRUD operations
  @GetMapping
  public ResponseEntity<Page<ReviewCategoryRatingResponse>> getAllReviewCategoryRatings(Pageable pageable) {
    return ResponseEntity.ok(service.getAllReviewCategoryRatings(pageable));
  }

  @GetMapping("/reviews/{reviewId}/categories/{categoryId}")
  public ResponseEntity<ReviewCategoryRatingResponse> getReviewCategoryRatingById(
      @PathVariable Integer reviewId,
      @PathVariable Integer categoryId) {
    return ResponseEntity.ok(service.getReviewCategoryRatingById(reviewId, categoryId));
  }

  @PostMapping
  public ResponseEntity<ReviewCategoryRatingResponse> saveReviewCategoryRating(
      @RequestBody ReviewCategoryRatingRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(service.saveReviewCategoryRating(request));
  }

  @PutMapping("/reviews/{reviewId}/categories/{categoryId}")
  public ResponseEntity<ReviewCategoryRatingResponse> updateReviewCategoryRating(
      @PathVariable Integer reviewId,
      @PathVariable Integer categoryId,
      @RequestBody ReviewCategoryRatingRequest request) {
    return ResponseEntity.ok(service.updateReviewCategoryRating(reviewId, categoryId, request));
  }

  @DeleteMapping("/reviews/{reviewId}/categories/{categoryId}")
  public ResponseEntity<Void> deleteReviewCategoryRating(
      @PathVariable Integer reviewId,
      @PathVariable Integer categoryId) {
    service.deleteReviewCategoryRating(reviewId, categoryId);
    return ResponseEntity.noContent().build();
  }

  // Business operations
  @GetMapping("/reviews/{reviewId}")
  public ResponseEntity<List<ReviewCategoryRatingResponse>> getRatingsByReview(
      @PathVariable Integer reviewId) {
    return ResponseEntity.ok(service.getRatingsByReview(reviewId));
  }

  @GetMapping("/categories/{categoryId}")
  public ResponseEntity<List<ReviewCategoryRatingResponse>> getRatingsByCategory(
      @PathVariable Integer categoryId) {
    return ResponseEntity.ok(service.getRatingsByCategory(categoryId));
  }

  @GetMapping("/categories/{categoryId}/average")
  public ResponseEntity<Double> getAverageRatingByCategory(@PathVariable Integer categoryId) {
    return ResponseEntity.ok(service.getAverageRatingByCategory(categoryId));
  }

  @GetMapping("/tour-plans/{tourPlanId}/averages")
  public ResponseEntity<Map<Integer, Double>> getAverageRatingsByCategoryForTour(
      @PathVariable Long tourPlanId) {
    return ResponseEntity.ok(service.getAverageRatingsByCategoryForTour(tourPlanId));
  }
}
