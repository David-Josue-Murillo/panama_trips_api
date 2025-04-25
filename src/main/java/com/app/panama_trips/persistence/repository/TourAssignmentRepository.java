package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Guide;
import com.app.panama_trips.persistence.entity.TourAssignment;
import com.app.panama_trips.persistence.entity.TourPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TourAssignmentRepository extends JpaRepository<TourAssignment, Integer> {

    List<TourAssignment> findByGuide(Guide guide);

    List<TourAssignment> findByTourPlan(TourPlan tourPlan);

    List<TourAssignment> findByStatus(String status);

    List<TourAssignment> findByReservationDate(LocalDate reservationDate);

    List<TourAssignment> findByReservationDateBetween(LocalDate startDate, LocalDate endDate);

    List<TourAssignment> findByGuideAndStatus(Guide guide, String status);

    List<TourAssignment> findByTourPlanAndReservationDateBetween(TourPlan tourPlan, LocalDate startDate, LocalDate endDate);

    Optional<TourAssignment> findByGuideAndTourPlanAndReservationDate(Guide guide, TourPlan tourPlan, LocalDate reservationDate);

    @Query("SELECT ta FROM TourAssignment ta WHERE ta.guide.id = :guideId AND ta.reservationDate >= :startDate")
    List<TourAssignment> findUpcomingAssignmentsByGuide(@Param("guideId") Integer guideId, @Param("startDate") LocalDate startDate);

    @Query("SELECT COUNT(ta) FROM TourAssignment ta WHERE ta.guide.id = :guideId AND ta.status = :status")
    Long countAssignmentsByGuideAndStatus(@Param("guideId") Integer guideId, @Param("status") String status);

    boolean existsByGuideAndReservationDate(Guide guide, LocalDate reservationDate);
}