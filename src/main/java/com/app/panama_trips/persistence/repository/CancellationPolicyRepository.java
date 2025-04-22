package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.CancellationPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CancellationPolicyRepository extends JpaRepository<CancellationPolicy, Integer> {

    List<CancellationPolicy> findByRefundPercentageGreaterThanEqual(Integer minRefundPercentage);

    List<CancellationPolicy> findByDaysBeforeTourGreaterThanEqual(Integer minDaysBeforeTour);

    Optional<CancellationPolicy> findByName(String name);

    @Query("SELECT cp FROM CancellationPolicy cp WHERE cp.refundPercentage >= :minPercentage AND cp.daysBeforeTour <= :maxDays")
    List<CancellationPolicy> findEligiblePolicies(@Param("minPercentage") Integer minPercentage, @Param("maxDays") Integer maxDays);
}