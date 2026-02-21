package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.TourPriceHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TourPriceHistoryRepository extends JpaRepository<TourPriceHistory, Integer> {

    // ID-based queries (avoid fetching full entities)
    @Query("SELECT tph FROM TourPriceHistory tph WHERE tph.tourPlan.id = :tourPlanId ORDER BY tph.changedAt DESC")
    List<TourPriceHistory> findByTourPlanIdOrderByChangedAtDesc(@Param("tourPlanId") Integer tourPlanId);

    Optional<TourPriceHistory> findFirstByTourPlan_IdOrderByChangedAtDesc(Integer tourPlanId);

    Page<TourPriceHistory> findByTourPlan_IdOrderByChangedAtDesc(Integer tourPlanId, Pageable pageable);

    List<TourPriceHistory> findByTourPlan_IdAndChangedAtBetween(Integer tourPlanId, LocalDateTime startDate, LocalDateTime endDate);

    List<TourPriceHistory> findByChangedBy_Id(Long userId);

    List<TourPriceHistory> findByChangedBy_IdAndChangedAtBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    // Paginated recent changes
    Page<TourPriceHistory> findAllByOrderByChangedAtDesc(Pageable pageable);

    // Aggregate queries
    @Query("SELECT AVG((tph.newPrice - tph.previousPrice) / tph.previousPrice * 100) FROM TourPriceHistory tph WHERE tph.tourPlan.id = :tourPlanId")
    Double calculateAveragePriceChangePercentageByTourPlanId(@Param("tourPlanId") Integer tourPlanId);

    @Query("SELECT MAX(tph.newPrice) FROM TourPriceHistory tph WHERE tph.tourPlan.id = :tourPlanId")
    BigDecimal findMaxNewPriceByTourPlanId(@Param("tourPlanId") Integer tourPlanId);

    @Query("SELECT MIN(tph.newPrice) FROM TourPriceHistory tph WHERE tph.tourPlan.id = :tourPlanId")
    BigDecimal findMinNewPriceByTourPlanId(@Param("tourPlanId") Integer tourPlanId);

    @Query("SELECT AVG(tph.newPrice) FROM TourPriceHistory tph WHERE tph.tourPlan.id = :tourPlanId")
    Double findAvgNewPriceByTourPlanId(@Param("tourPlanId") Integer tourPlanId);

    @Query("SELECT COALESCE(SUM(tph.newPrice - tph.previousPrice), 0) FROM TourPriceHistory tph WHERE tph.tourPlan.id = :tourPlanId AND tph.newPrice > tph.previousPrice")
    BigDecimal sumPriceIncreaseByTourPlanId(@Param("tourPlanId") Integer tourPlanId);

    @Query("SELECT COALESCE(SUM(tph.previousPrice - tph.newPrice), 0) FROM TourPriceHistory tph WHERE tph.tourPlan.id = :tourPlanId AND tph.previousPrice > tph.newPrice")
    BigDecimal sumPriceDecreaseByTourPlanId(@Param("tourPlanId") Integer tourPlanId);

    List<TourPriceHistory> findByNewPriceGreaterThan(BigDecimal price);

    @Query("SELECT COUNT(tph) FROM TourPriceHistory tph WHERE tph.tourPlan.id = :tourPlanId")
    Long countPriceChangesByTourPlanId(@Param("tourPlanId") Integer tourPlanId);

    // Search by price (matches either new or previous price)
    List<TourPriceHistory> findByNewPriceOrPreviousPrice(BigDecimal newPrice, BigDecimal previousPrice);

    // Top tour plans by change count
    @Query("SELECT tph.tourPlan.id FROM TourPriceHistory tph GROUP BY tph.tourPlan.id ORDER BY COUNT(tph) DESC")
    List<Integer> findTopTourPlanIdsByChangeCount(Pageable pageable);
}
