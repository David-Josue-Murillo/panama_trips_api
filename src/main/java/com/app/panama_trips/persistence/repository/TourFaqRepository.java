package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.TourFaq;
import com.app.panama_trips.persistence.entity.TourPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourFaqRepository extends JpaRepository<TourFaq, Integer> {

    List<TourFaq> findByTourPlan(TourPlan tourPlan);

    List<TourFaq> findByTourPlanOrderByDisplayOrderAsc(TourPlan tourPlan);

    @Query("SELECT tf FROM TourFaq tf WHERE tf.tourPlan.id = :tourPlanId ORDER BY tf.displayOrder ASC")
    List<TourFaq> findByTourPlanIdOrderByDisplayOrder(@Param("tourPlanId") Long tourPlanId);

    @Query("SELECT tf FROM TourFaq tf WHERE LOWER(tf.question) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(tf.answer) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<TourFaq> searchByKeyword(@Param("keyword") String keyword);

    List<TourFaq> findByTourPlanAndDisplayOrderGreaterThan(TourPlan tourPlan, Integer displayOrder);

    Long countByTourPlan(TourPlan tourPlan);

    void deleteByTourPlan(TourPlan tourPlan);
}