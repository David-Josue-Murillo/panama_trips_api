package com.app.panama_trips.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.panama_trips.presentation.dto.ReviewCategoryRequest;
import com.app.panama_trips.presentation.dto.ReviewCategoryResponse;
import com.app.panama_trips.service.implementation.ReviewCategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/review-categories")
@RequiredArgsConstructor
public class ReviewCategoryController {

  private final ReviewCategoryService service;

  @GetMapping
  public ResponseEntity<Page<ReviewCategoryResponse>> getAllReviewCategories(Pageable pageable) {
    return ResponseEntity.ok(service.getAllReviewCategories(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ReviewCategoryResponse> getReviewCategoryById(@PathVariable Integer id) {
    return ResponseEntity.ok(service.getReviewCategoryById(id));
  }

  @PostMapping
  public ResponseEntity<ReviewCategoryResponse> saveReviewCategory(@RequestBody ReviewCategoryRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(service.saveReviewCategory(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ReviewCategoryResponse> updateReviewCategory(
      @PathVariable Integer id,
      @RequestBody ReviewCategoryRequest request) {
    return ResponseEntity.ok(service.updateReviewCategory(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteReviewCategory(@PathVariable Integer id) {
    service.deleteReviewCategory(id);
    return ResponseEntity.noContent().build();
  }
}
