package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.TourPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TourPlanRepository extends JpaRepository<TourPlan, Integer> {
    Optional<TourPlan> findByTitleIgnoreCase(String title);
    boolean existsByTitleIgnoreCase(String title);

    List<TourPlan> findByPrice(BigDecimal price);
    Page<TourPlan> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    List<TourPlan> findByDuration(Integer duration);
    Page<TourPlan> findByDurationBetween(Integer minDuration, Integer maxDuration, Pageable pageable);

    List<TourPlan> findByAvailableSpots(Integer availableSpots);
    Page<TourPlan> findByAvailableSpotsBetween(Integer minSpots, Integer maxSpots, Pageable pageable);

    List<TourPlan> findByProvider_Id(Integer providerId);

    List<TourPlan> findByTitleContainingIgnoreCaseAndPrice(String title, BigDecimal price);
    Page<TourPlan> findByTitleContainingIgnoreCaseAndPriceBetween(String title, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    Page<TourPlan> findByTitleContainingIgnoreCaseAndPriceBetweenAndDurationBetween(String title, BigDecimal minPrice, BigDecimal maxPrice, Integer minDuration, Integer maxDuration, Pageable pageable);

    @Query("SELECT t FROM TourPlan t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY t.title ASC")
    List<TourPlan> findTop10ByTitleContainingIgnoreCaseOrderByTitleAsc(@Param("keyword") String keyword, Pageable pageable);
}
