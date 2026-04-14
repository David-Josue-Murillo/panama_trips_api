package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Review;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Busca reviews por tour plan.
     */
    Page<Review> findByTourPlanId(TourPlan tourPlan, Pageable pageable);

    /**
     * Busca reviews por usuario.
     */
    Page<Review> findByUserId(UserEntity user, Pageable pageable);

    /**
     * Busca reviews por estado.
     */
    Page<Review> findByStatus(String status, Pageable pageable);

    /**
     * Busca reviews por tour plan y estado.
     */
    Page<Review> findByTourPlanIdAndStatus(TourPlan tourPlan, String status, Pageable pageable);

    /**
     * Verifica si un usuario ya escribió un review para un tour plan específico.
     */
    Optional<Review> findByUserIdAndTourPlanId(UserEntity user, TourPlan tourPlan);

    /**
     * Cuenta la cantidad de reviews por tour plan.
     */
    Long countByTourPlanId(TourPlan tourPlan);

    /**
     * Cuenta la cantidad de reviews por usuario.
     */
    Long countByUserId(UserEntity user);

    /**
     * Calcula el promedio de rating por tour plan.
     */
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.tourPlanId = :tourPlan AND r.status = 'ACTIVE'")
    Optional<Double> findAverageRatingByTourPlan(@Param("tourPlan") TourPlan tourPlan);

    /**
     * Obtiene los reviews más útiles (mayor helpful_votes) para un tour plan.
     */
    @Query("SELECT r FROM Review r WHERE r.tourPlanId = :tourPlan AND r.status = 'ACTIVE' ORDER BY r.helpfulVotes DESC")
    Page<Review> findTopReviewsByTourPlan(@Param("tourPlan") TourPlan tourPlan, Pageable pageable);

    /**
     * Busca reviews verificados (verified_purchase = true) por tour plan.
     */
    Page<Review> findByTourPlanIdAndVerifiedPurchaseTrue(TourPlan tourPlan, Pageable pageable);

    /**
     * Verifica si existe al menos un review activo para un tour plan.
     */
    boolean existsByTourPlanIdAndStatus(TourPlan tourPlan, String status);

    /**
     * Cuenta la cantidad de reviews por tour plan y estado.
     */
    Long countByTourPlanIdAndStatus(TourPlan tourPlan, String status);
}
