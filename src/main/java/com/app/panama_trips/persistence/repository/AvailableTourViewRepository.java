package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.AvailableTourView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AvailableTourViewRepository extends JpaRepository<AvailableTourView, Long> {

    // Find available tours by provider
    List<AvailableTourView> findByProviderId(Long providerId);

    // Find available tours with remaining spots - Fixed method name
    List<AvailableTourView> findByRemainingSpotsGreaterThan(Long spots);

    // Find featured available tours
    List<AvailableTourView> findByFeaturedTrueOrderByFeaturedOrderAsc();

    // Find tours by price range
    List<AvailableTourView> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Find tours by status
    List<AvailableTourView> findByStatus(String status);

    // Find wheelchair accessible tours
    List<AvailableTourView> findByWheelchairAccessibleTrue();

    // Search by title or description
    @Query("SELECT a FROM AvailableTourView a WHERE LOWER(a.title) LIKE LOWER(concat('%', :keyword, '%')) OR LOWER(a.description) LIKE LOWER(concat('%', :keyword, '%'))")
    List<AvailableTourView> searchByTitleOrDescription(@Param("keyword") String keyword);

    // Find tours by tags
    @Query("SELECT a FROM AvailableTourView a WHERE a.tags LIKE %:tag%")
    List<AvailableTourView> findByTag(@Param("tag") String tag);

    // Find highly rated tours
    List<AvailableTourView> findByAverageRatingGreaterThanEqual(BigDecimal minRating);

    // Find seasonal tours available on a specific date
    @Query("SELECT a FROM AvailableTourView a WHERE (a.isSeasonal = false OR (a.seasonStartDate <= :date AND a.seasonEndDate >= :date))")
    List<AvailableTourView> findAvailableOnDate(@Param("date") LocalDate date);
}