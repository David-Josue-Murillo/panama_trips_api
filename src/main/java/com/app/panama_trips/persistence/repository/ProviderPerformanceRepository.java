package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.ProviderPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderPerformanceRepository extends JpaRepository<ProviderPerformance, Integer> {

    Optional<ProviderPerformance> findByProviderId(Integer providerId);

    List<ProviderPerformance> findByTotalReservationsGreaterThan(Long reservationCount);

    List<ProviderPerformance> findByAverageRatingGreaterThanEqual(BigDecimal minRating);

    @Query("SELECT pp FROM ProviderPerformance pp ORDER BY pp.totalRevenue DESC")
    List<ProviderPerformance> findAllOrderByRevenueDesc();

    @Query("SELECT pp FROM ProviderPerformance pp WHERE pp.averageRating >= :minRating AND pp.totalTours >= :minTours")
    List<ProviderPerformance> findTopPerformers(@Param("minRating") BigDecimal minRating, @Param("minTours") Long minTours);

    @Query("SELECT pp FROM ProviderPerformance pp WHERE LOWER(pp.providerName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<ProviderPerformance> searchByProviderName(@Param("keyword") String keyword);
}