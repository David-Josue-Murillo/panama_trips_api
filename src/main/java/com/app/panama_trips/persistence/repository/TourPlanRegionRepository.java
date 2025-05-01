package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Region;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourPlanRegion;
import com.app.panama_trips.persistence.entity.TourPlanRegionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourPlanRegionRepository extends JpaRepository<TourPlanRegion, TourPlanRegionId> {

    List<TourPlanRegion> findByTourPlan(TourPlan tourPlan);

    List<TourPlanRegion> findByRegion(Region region);

    @Query("SELECT tpr.region FROM TourPlanRegion tpr WHERE tpr.tourPlan.id = :tourPlanId")
    List<Region> findRegionsByTourPlanId(@Param("tourPlanId") Integer tourPlanId);

    @Query("SELECT tpr.tourPlan FROM TourPlanRegion tpr WHERE tpr.region.id = :regionId")
    List<TourPlan> findTourPlansByRegionId(@Param("regionId") Integer regionId);

    void deleteByTourPlan(TourPlan tourPlan);

    void deleteByRegion(Region region);

    boolean existsByTourPlanAndRegion(TourPlan tourPlan, Region region);
}