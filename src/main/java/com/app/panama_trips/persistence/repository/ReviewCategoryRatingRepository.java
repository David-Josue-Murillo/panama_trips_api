package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Review;
import com.app.panama_trips.persistence.entity.ReviewCategory;
import com.app.panama_trips.persistence.entity.ReviewCategoryRating;
import com.app.panama_trips.persistence.entity.ReviewCategoryRatingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewCategoryRatingRepository extends JpaRepository<ReviewCategoryRating, ReviewCategoryRatingId> {

    List<ReviewCategoryRating> findByReview(Review review);

    List<ReviewCategoryRating> findByCategory(ReviewCategory category);

    Optional<ReviewCategoryRating> findByReviewAndCategory(Review review, ReviewCategory category);

    @Query("SELECT AVG(rcr.rating) FROM ReviewCategoryRating rcr WHERE rcr.category.id = :categoryId")
    Double getAverageRatingByCategory(@Param("categoryId") Integer categoryId);

    @Query("SELECT rcr.category.id, AVG(rcr.rating) FROM ReviewCategoryRating rcr WHERE rcr.review.tourPlanId.id = :tourPlanId GROUP BY rcr.category.id")
    List<Object[]> getAverageRatingsByCategoryForTour(@Param("tourPlanId") Long tourPlanId);

    @Query("SELECT COUNT(rcr) FROM ReviewCategoryRating rcr WHERE rcr.rating >= :minRating")
    Long countByRatingGreaterThanEqual(@Param("minRating") Integer minRating);

    void deleteByReview(Review review);

    boolean existsByReviewAndCategory(Review review, ReviewCategory category);
}