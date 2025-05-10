package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourPriceHistory;
import com.app.panama_trips.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TourPriceHistoryRepository extends JpaRepository<TourPriceHistory, Integer> {

    List<TourPriceHistory> findByTourPlan(TourPlan tourPlan);

    Page<TourPriceHistory> findByTourPlanOrderByChangedAtDesc(TourPlan tourPlan, Pageable pageable);

    List<TourPriceHistory> findByTourPlanAndChangedAtBetween(TourPlan tourPlan,
                                                             LocalDateTime startDate,
                                                             LocalDateTime endDate);

    List<TourPriceHistory> findByChangedBy(UserEntity user);

    @Query("SELECT tph FROM TourPriceHistory tph WHERE tph.tourPlan.id = :tourPlanId ORDER BY tph.changedAt DESC")
    List<TourPriceHistory> findByTourPlanIdOrderByChangedAtDesc(@Param("tourPlanId") Integer tourPlanId);

    @Query("SELECT AVG((tph.newPrice - tph.previousPrice) / tph.previousPrice * 100) FROM TourPriceHistory tph WHERE tph.tourPlan.id = :tourPlanId")
    Double calculateAveragePriceChangePercentageByTourPlanId(@Param("tourPlanId") Integer tourPlanId);

    List<TourPriceHistory> findByNewPriceGreaterThan(BigDecimal price);

    @Query("SELECT COUNT(tph) FROM TourPriceHistory tph WHERE tph.tourPlan.id = :tourPlanId")
    Long countPriceChangesByTourPlanId(@Param("tourPlanId") Integer tourPlanId);
}