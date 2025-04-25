package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourPlanImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TourPlanImageRepository extends JpaRepository<TourPlanImage, Integer> {

    List<TourPlanImage> findByTourPlan(TourPlan tourPlan);

    List<TourPlanImage> findByTourPlanOrderByDisplayOrderAsc(TourPlan tourPlan);

    Optional<TourPlanImage> findByTourPlanAndIsMainTrue(TourPlan tourPlan);

    List<TourPlanImage> findByTourPlanAndIsMainFalseOrderByDisplayOrderAsc(TourPlan tourPlan);

    @Query("SELECT tpi FROM TourPlanImage tpi WHERE tpi.tourPlan.id = :tourPlanId ORDER BY tpi.displayOrder ASC")
    List<TourPlanImage> findByTourPlanIdOrderByDisplayOrder(@Param("tourPlanId") Integer tourPlanId);

    @Query("SELECT MAX(tpi.displayOrder) FROM TourPlanImage tpi WHERE tpi.tourPlan.id = :tourPlanId")
    Integer findMaxDisplayOrderByTourPlanId(@Param("tourPlanId") Integer tourPlanId);

    @Query("SELECT COUNT(tpi) FROM TourPlanImage tpi WHERE tpi.tourPlan.id = :tourPlanId")
    Long countByTourPlanId(@Param("tourPlanId") Integer tourPlanId);

    void deleteByTourPlan(TourPlan tourPlan);

    boolean existsByTourPlanAndImageUrl(TourPlan tourPlan, String imageUrl);
}