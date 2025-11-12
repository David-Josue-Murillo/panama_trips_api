package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.ReviewCategory;

public record ReviewCategoryResponse(
    Integer id,
    String name,
    String description) {

  public ReviewCategoryResponse(ReviewCategory reviewCategory) {
    this(
        reviewCategory.getId(),
        reviewCategory.getName(),
        reviewCategory.getDescription());
  }
}
