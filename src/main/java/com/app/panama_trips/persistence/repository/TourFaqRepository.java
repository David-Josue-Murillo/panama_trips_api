package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.TourFaq;
import com.app.panama_trips.persistence.entity.TourPlan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TourFaqRepository extends JpaRepository<TourFaq, Integer> {

    // Entity-based (kept for compatibility)
    List<TourFaq> findByTourPlan(TourPlan tourPlan);
    List<TourFaq> findByTourPlanOrderByDisplayOrderAsc(TourPlan tourPlan);
    Long countByTourPlan(TourPlan tourPlan);
    void deleteByTourPlan(TourPlan tourPlan);

    // ID-based queries (eliminates unnecessary entity loading)
    List<TourFaq> findByTourPlan_Id(Integer tourPlanId);

    List<TourFaq> findByTourPlan_IdOrderByDisplayOrderAsc(Integer tourPlanId);

    List<TourFaq> findByTourPlan_IdOrderByDisplayOrderAsc(Integer tourPlanId, Pageable pageable);

    Optional<TourFaq> findByTourPlan_IdAndQuestionIgnoreCase(Integer tourPlanId, String question);

    boolean existsByTourPlan_IdAndQuestionIgnoreCase(Integer tourPlanId, String question);

    boolean existsByTourPlan_IdAndDisplayOrder(Integer tourPlanId, Integer displayOrder);

    long countByTourPlan_Id(Integer tourPlanId);

    // Keyword search
    @Query("SELECT tf FROM TourFaq tf WHERE LOWER(tf.question) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(tf.answer) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<TourFaq> searchByKeyword(@Param("keyword") String keyword);
}
