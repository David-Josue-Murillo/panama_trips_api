package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.ReviewCategoryRating;

public record ReviewCategoryRatingResponse(
    Integer reviewId,
    Integer categoryId,
    String categoryName,
    Integer rating
) {
    public ReviewCategoryRatingResponse(ReviewCategoryRating reviewCategoryRating) {
        this(
            reviewCategoryRating.getId().getReviewId(),
            reviewCategoryRating.getId().getCategoryId(),
            reviewCategoryRating.getCategory().getName(),
            reviewCategoryRating.getRating()
        );
    }
}
