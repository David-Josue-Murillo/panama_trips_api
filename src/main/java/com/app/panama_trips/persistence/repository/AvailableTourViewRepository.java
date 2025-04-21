package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.AvailableTourView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailableTourViewRepository extends JpaRepository<AvailableTourView, Long> {

    List<AvailableTourView> findByStatus(String status);

    List<AvailableTourView> findByProviderId(Long providerId);

    List<AvailableTourView> findByFeaturedTrue();

    Optional<AvailableTourView> findBySlug(String slug);

    List<AvailableTourView> findByPriceLessThanEqual(BigDecimal maxPrice);

    List<AvailableTourView> findByRemainingSpotGreaterThan(Long minSpots);

    @Query("SELECT a FROM AvailableTourView a WHERE a.remainingSpots > 0 AND a.price >= :minPrice AND a.price <= :maxPrice")
    List<AvailableTourView> findAvailableToursByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT a FROM AvailableTourView a WHERE " +
            "(:keyword IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(a.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "a.remainingSpots > 0 ORDER BY a.featuredOrder DESC, a.averageRating DESC")
    List<AvailableTourView> searchAvailableTours(@Param("keyword") String keyword);

    @Query("SELECT a FROM AvailableTourView a WHERE a.tags LIKE CONCAT('%', :tag, '%') AND a.remainingSpots > 0")
    List<AvailableTourView> findByTag(@Param("tag") String tag);

    List<AvailableTourView> findBySeasonalTrueAndSeasonStartDateLessThanEqualAndSeasonEndDateGreaterThanEqual(
            LocalDate currentDate, LocalDate currentDate2);
}