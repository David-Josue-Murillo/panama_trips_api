package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.BusinessRuleException;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Review;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.repository.ReviewRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.presentation.dto.ReviewRequest;
import com.app.panama_trips.presentation.dto.ReviewResponse;
import com.app.panama_trips.service.interfaces.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final UserEntityRepository userRepository;
    private final TourPlanRepository tourPlanRepository;

    // ==================== CRUD Operations ====================

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getAllReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable)
                .map(ReviewResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review no encontrado con ID: " + id));
        return new ReviewResponse(review);
    }

    @Override
    @Transactional
    public ReviewResponse saveReview(ReviewRequest request) {
        // Validar que el usuario existe
        UserEntity user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + request.userId()));

        // Validar que el tour plan existe
        TourPlan tourPlan = tourPlanRepository.findById(request.tourPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Tour plan no encontrado con ID: " + request.tourPlanId()));

        // Validar que el usuario no haya escrito ya un review para este tour
        Optional<Review> existingReview = reviewRepository.findByUserIdAndTourPlanId(user, tourPlan);
        if (existingReview.isPresent()) {
            throw new BusinessRuleException("El usuario ya escribió un review para este tour plan");
        }

        // Validar que el rating esté entre 1 y 5
        if (request.rating() < 1 || request.rating() > 5) {
            throw new BusinessRuleException("La calificación debe estar entre 1 y 5");
        }

        // Crear el review
        Review review = new Review();
        review.setUserId(user);
        review.setTourPlanId(tourPlan);
        review.setTitle(request.title());
        review.setRating(request.rating());
        review.setComment(request.comment());
        review.setVerifiedPurchase(request.verifiedPurchase() != null ? request.verifiedPurchase() : false);
        review.setHelpfulVotes(0);
        review.setReported(false);
        review.setStatus("ACTIVE");

        Review savedReview = reviewRepository.save(review);

        // Actualizar estadísticas del tour plan
        updateTourPlanStatistics(tourPlan);

        return new ReviewResponse(savedReview);
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(Long id, ReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review no encontrado con ID: " + id));

        // Validar que el rating esté entre 1 y 5
        if (request.rating() != null && (request.rating() < 1 || request.rating() > 5)) {
            throw new BusinessRuleException("La calificación debe estar entre 1 y 5");
        }

        // Actualizar campos
        if (request.title() != null) {
            review.setTitle(request.title());
        }
        if (request.rating() != null) {
            review.setRating(request.rating());
        }
        if (request.comment() != null) {
            review.setComment(request.comment());
        }

        Review updatedReview = reviewRepository.save(review);
        return new ReviewResponse(updatedReview);
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Review no encontrado con ID: " + id);
        }

        Review review = reviewRepository.findById(id).get();
        TourPlan tourPlan = review.getTourPlanId();

        reviewRepository.deleteById(id);

        // Actualizar estadísticas del tour plan después de eliminar
        updateTourPlanStatistics(tourPlan);
    }

    // ==================== Query Methods ====================

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewsByTourPlanId(Integer tourPlanId, Pageable pageable) {
        TourPlan tourPlan = tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour plan no encontrado con ID: " + tourPlanId));

        return reviewRepository.findByTourPlanId(tourPlan, pageable)
                .map(ReviewResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewsByUserId(Long userId, Pageable pageable) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));

        return reviewRepository.findByUserId(user, pageable)
                .map(ReviewResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getReviewsByStatus(String status, Pageable pageable) {
        return reviewRepository.findByStatus(status, pageable)
                .map(ReviewResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getTopReviewsByTourPlanId(Integer tourPlanId, Pageable pageable) {
        TourPlan tourPlan = tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour plan no encontrado con ID: " + tourPlanId));

        return reviewRepository.findTopReviewsByTourPlan(tourPlan, pageable)
                .map(ReviewResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getVerifiedReviewsByTourPlanId(Integer tourPlanId, Pageable pageable) {
        TourPlan tourPlan = tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour plan no encontrado con ID: " + tourPlanId));

        return reviewRepository.findByTourPlanIdAndVerifiedPurchaseTrue(tourPlan, pageable)
                .map(ReviewResponse::new);
    }

    // ==================== Statistics ====================

    @Override
    @Transactional(readOnly = true)
    public Double getAverageRatingByTourPlanId(Integer tourPlanId) {
        TourPlan tourPlan = tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour plan no encontrado con ID: " + tourPlanId));

        return reviewRepository.findAverageRatingByTourPlan(tourPlan)
                .orElse(0.0);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getReviewStatisticsByTourPlanId(Integer tourPlanId) {
        TourPlan tourPlan = tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour plan no encontrado con ID: " + tourPlanId));

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalReviews", reviewRepository.countByTourPlanId(tourPlan));
        statistics.put("averageRating", reviewRepository.findAverageRatingByTourPlan(tourPlan).orElse(0.0));
        statistics.put("verifiedReviewsCount", reviewRepository.findByTourPlanIdAndVerifiedPurchaseTrue(tourPlan, Pageable.unpaged()).getTotalElements());

        return statistics;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> countReviewsByStatusForTourPlan(Integer tourPlanId) {
        TourPlan tourPlan = tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour plan no encontrado con ID: " + tourPlanId));

        Map<String, Long> counts = new HashMap<>();
        counts.put("ACTIVE", reviewRepository.countByTourPlanIdAndStatus(tourPlan, "ACTIVE"));
        counts.put("PENDING", reviewRepository.countByTourPlanIdAndStatus(tourPlan, "PENDING"));
        counts.put("REMOVED", reviewRepository.countByTourPlanIdAndStatus(tourPlan, "REMOVED"));

        return counts;
    }

    // ==================== Actions ====================

    @Override
    @Transactional
    public ReviewResponse respondToReview(Long reviewId, String response, Long updatedByUserId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review no encontrado con ID: " + reviewId));

        review.setResponseByProvider(response);
        review.setResponseDate(LocalDateTime.now());

        Review updatedReview = reviewRepository.save(review);
        return new ReviewResponse(updatedReview);
    }

    @Override
    @Transactional
    public ReviewResponse updateReviewStatus(Long reviewId, String status) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review no encontrado con ID: " + reviewId));

        // Validar estado válido
        if (!isValidStatus(status)) {
            throw new BusinessRuleException("Estado inválido. Los estados válidos son: ACTIVE, PENDING, REMOVED");
        }

        review.setStatus(status);
        Review updatedReview = reviewRepository.save(review);
        return new ReviewResponse(updatedReview);
    }

    @Override
    @Transactional
    public ReviewResponse reportReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review no encontrado con ID: " + reviewId));

        review.setReported(true);
        Review updatedReview = reviewRepository.save(review);
        return new ReviewResponse(updatedReview);
    }

    @Override
    @Transactional
    public ReviewResponse addHelpfulVote(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review no encontrado con ID: " + reviewId));

        review.setHelpfulVotes(review.getHelpfulVotes() + 1);
        Review updatedReview = reviewRepository.save(review);
        return new ReviewResponse(updatedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userAlreadyReviewed(Long userId, Integer tourPlanId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        Optional<TourPlan> tourPlan = tourPlanRepository.findById(tourPlanId);

        if (user.isEmpty() || tourPlan.isEmpty()) {
            return false;
        }

        return reviewRepository.findByUserIdAndTourPlanId(user.get(), tourPlan.get()).isPresent();
    }

    // ==================== Helper Methods ====================

    /**
     * Actualiza las estadísticas de un tour plan después de crear/eliminar un review.
     */
    private void updateTourPlanStatistics(TourPlan tourPlan) {
        Double averageRating = reviewRepository.findAverageRatingByTourPlan(tourPlan).orElse(0.0);
        Long totalReviews = reviewRepository.countByTourPlanId(tourPlan);

        tourPlan.setAverageRating(java.math.BigDecimal.valueOf(averageRating));
        tourPlan.setTotalReviews(totalReviews.intValue());

        tourPlanRepository.save(tourPlan);
    }

    /**
     * Valida si un estado es válido.
     */
    private boolean isValidStatus(String status) {
        return "ACTIVE".equals(status) || "PENDING".equals(status) || "REMOVED".equals(status);
    }
}
