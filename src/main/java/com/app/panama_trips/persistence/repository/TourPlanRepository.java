package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.TourPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TourPlanRepository extends JpaRepository<TourPlan, Integer> {

        @EntityGraph(attributePaths = { "provider", "provider.address" })
        Optional<TourPlan> findById(Integer id);

        @EntityGraph(attributePaths = { "provider", "provider.address" })
        Page<TourPlan> findAll(Pageable pageable);

        Optional<TourPlan> findByTitleIgnoreCase(String title);

        boolean existsByTitleIgnoreCase(String title);

        @EntityGraph(attributePaths = { "provider", "provider.address" })
        List<TourPlan> findByPricing_Price(BigDecimal price);

        @EntityGraph(attributePaths = { "provider", "provider.address" })
        Page<TourPlan> findByPricing_PriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

        @EntityGraph(attributePaths = { "provider", "provider.address" })
        List<TourPlan> findByDuration(Integer duration);

        @EntityGraph(attributePaths = { "provider", "provider.address" })
        Page<TourPlan> findByDurationBetween(Integer minDuration, Integer maxDuration, Pageable pageable);

        @EntityGraph(attributePaths = { "provider", "provider.address" })
        List<TourPlan> findByAvailableSpots(Integer availableSpots);

        @EntityGraph(attributePaths = { "provider", "provider.address" })
        Page<TourPlan> findByAvailableSpotsBetween(Integer minSpots, Integer maxSpots, Pageable pageable);

        List<TourPlan> findByProvider_Id(Integer providerId);

        @EntityGraph(attributePaths = { "provider", "provider.address" })
        List<TourPlan> findByTitleContainingIgnoreCaseAndPricing_Price(String title, BigDecimal price);

        @EntityGraph(attributePaths = { "provider", "provider.address" })
        Page<TourPlan> findByTitleContainingIgnoreCaseAndPricing_PriceBetween(String title, BigDecimal minPrice,
                        BigDecimal maxPrice, Pageable pageable);

        @EntityGraph(attributePaths = { "provider", "provider.address" })
        Page<TourPlan> findByTitleContainingIgnoreCaseAndPricing_PriceBetweenAndDurationBetween(String title,
                        BigDecimal minPrice, BigDecimal maxPrice, Integer minDuration, Integer maxDuration,
                        Pageable pageable);

        @Query("SELECT t FROM TourPlan t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY t.title ASC")
        List<TourPlan> findTop10ByTitleContainingIgnoreCaseOrderByTitleAsc(@Param("keyword") String keyword,
                        Pageable pageable);
}
