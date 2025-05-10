package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourPlanAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TourPlanAvailabilityRepository extends JpaRepository<TourPlanAvailability, Integer> {

    List<TourPlanAvailability> findByTourPlan(TourPlan tourPlan);

    List<TourPlanAvailability> findByTourPlanAndIsAvailableTrue(TourPlan tourPlan);

    List<TourPlanAvailability> findByAvailableDateBetween(LocalDate startDate, LocalDate endDate);

    List<TourPlanAvailability> findByTourPlanAndAvailableDateBetween(TourPlan tourPlan, LocalDate startDate, LocalDate endDate);

    Optional<TourPlanAvailability> findByTourPlanAndAvailableDate(TourPlan tourPlan, LocalDate availableDate);

    @Query("SELECT tpa FROM TourPlanAvailability tpa WHERE tpa.tourPlan.id = :tourPlanId AND tpa.isAvailable = true AND tpa.availableDate >= :today ORDER BY tpa.availableDate ASC")
    List<TourPlanAvailability> findAvailableDatesByTourPlanId(@Param("tourPlanId") Integer tourPlanId, @Param("today") LocalDate today);

    @Query("SELECT tpa FROM TourPlanAvailability tpa WHERE tpa.tourPlan.id = :tourPlanId AND tpa.availableSpots >= :requiredSpots AND tpa.isAvailable = true AND tpa.availableDate >= :today ORDER BY tpa.availableDate ASC")
    List<TourPlanAvailability> findDatesByTourPlanWithSufficientSpots(@Param("tourPlanId") Integer tourPlanId, @Param("requiredSpots") Integer requiredSpots, @Param("today") LocalDate today);

    @Query("SELECT COUNT(tpa) FROM TourPlanAvailability tpa WHERE tpa.tourPlan.id = :tourPlanId AND tpa.isAvailable = true AND tpa.availableDate >= :today")
    Long countAvailableDatesByTourPlan(@Param("tourPlanId") Integer tourPlanId, @Param("today") LocalDate today);

    List<TourPlanAvailability> findByTourPlanAndPriceOverrideIsNotNull(TourPlan tourPlan);

    List<TourPlanAvailability> findByTourPlanAndPriceOverrideGreaterThan(TourPlan tourPlan, BigDecimal price);

    void deleteByTourPlan(TourPlan tourPlan);

    boolean existsByTourPlanAndAvailableDate(TourPlan tourPlan, LocalDate availableDate);
}