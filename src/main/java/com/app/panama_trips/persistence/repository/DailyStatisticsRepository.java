package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.DailyStatistics;
import com.app.panama_trips.persistence.entity.Region;
import com.app.panama_trips.persistence.entity.TourPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyStatisticsRepository extends JpaRepository<DailyStatistics, LocalDate> {

    List<DailyStatistics> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<DailyStatistics> findByTotalRevenueGreaterThan(BigDecimal amount);

    List<DailyStatistics> findByTopTour(TourPlan tourPlan);

    List<DailyStatistics> findByTopRegion(Region region);

    @Query("SELECT ds FROM DailyStatistics ds WHERE ds.totalReservations > :minReservations ORDER BY ds.date DESC")
    List<DailyStatistics> findByMinimumReservations(@Param("minReservations") Integer minReservations);

    @Query("SELECT SUM(ds.totalRevenue) FROM DailyStatistics ds WHERE ds.date BETWEEN :startDate AND :endDate")
    BigDecimal calculateRevenueForPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    Optional<DailyStatistics> findTopByOrderByTotalRevenueDesc();
}