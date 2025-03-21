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
    Page<TourPlan> findByPriceBetween(BigDecimal priceAfter, BigDecimal priceBefore, Pageable pageable);

    List<TourPlan> findByDuration(Integer duration);
    Page<TourPlan> findByDurationBetween(Integer durationAfter, Integer durationBefore, Pageable pageable);

    List<TourPlan> findByAvailableSpots(Integer availableSpots);
    Page<TourPlan> findByAvailableSpotsBetween(Integer availableSpotsAfter, Integer availableSpotsBefore, Pageable pageable);

    List<TourPlan> findByProvider_Id(Integer providerId);

    List<TourPlan> findByTitleContainingIgnoreCaseAndPrice(String title, BigDecimal price);
    Page<TourPlan> findByTitleContainingIgnoreCaseAndPriceBetween(String title, BigDecimal priceAfter, BigDecimal priceBefore, Pageable pageable);

    Page<TourPlan> findByTitleContainingIgnoreCaseAndPriceBetweenAndDurationBetween(String title, BigDecimal priceAfter, BigDecimal priceBefore, Integer durationAfter, Integer durationBefore, Pageable pageable);

    @Query("SELECT t FROM TourPlan t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY t.title ASC")
    List<TourPlan> findTop10ByTitleContainingIgnoreCaseOrderByTitleAsc(@Param("keyword") String keyword, Pageable pageable);
}
