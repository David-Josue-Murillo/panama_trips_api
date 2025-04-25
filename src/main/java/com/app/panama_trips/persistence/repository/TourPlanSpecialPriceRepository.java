package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourPlanSpecialPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TourPlanSpecialPriceRepository extends JpaRepository<TourPlanSpecialPrice, Integer> {

    List<TourPlanSpecialPrice> findByTourPlan(TourPlan tourPlan);

    List<TourPlanSpecialPrice> findByTourPlanOrderByStartDateAsc(TourPlan tourPlan);

    List<TourPlanSpecialPrice> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    List<TourPlanSpecialPrice> findByEndDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT tpsp FROM TourPlanSpecialPrice tpsp WHERE tpsp.tourPlan.id = :tourPlanId AND :date BETWEEN tpsp.startDate AND tpsp.endDate")
    Optional<TourPlanSpecialPrice> findByTourPlanIdAndDate(@Param("tourPlanId") Integer tourPlanId, @Param("date") LocalDate date);

    @Query("SELECT tpsp FROM TourPlanSpecialPrice tpsp WHERE tpsp.tourPlan.id = :tourPlanId AND " +
            "((tpsp.startDate BETWEEN :startDate AND :endDate) OR " +
            "(tpsp.endDate BETWEEN :startDate AND :endDate) OR " +
            "(:startDate BETWEEN tpsp.startDate AND tpsp.endDate))")
    List<TourPlanSpecialPrice> findOverlappingPricePeriodsForTourPlan(@Param("tourPlanId") Integer tourPlanId,
                                                                      @Param("startDate") LocalDate startDate,
                                                                      @Param("endDate") LocalDate endDate);

    List<TourPlanSpecialPrice> findByTourPlanAndPriceGreaterThan(TourPlan tourPlan, BigDecimal price);

    List<TourPlanSpecialPrice> findByTourPlanAndStartDateGreaterThanEqual(TourPlan tourPlan, LocalDate date);

    void deleteByTourPlan(TourPlan tourPlan);

    boolean existsByTourPlanAndStartDateAndEndDate(TourPlan tourPlan, LocalDate startDate, LocalDate endDate);
}