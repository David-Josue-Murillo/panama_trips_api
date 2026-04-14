package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.ReviewRequest;
import com.app.panama_trips.presentation.dto.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Contrato de servicio para la gestión de reviews de tours.
 * Define las operaciones CRUD básicas y búsquedas especializadas para reviews.
 */
public interface IReviewService {

    // ==================== CRUD Operations ====================

    /**
     * Obtiene todos los reviews de forma paginada.
     *
     * @param pageable información de paginación
     * @return página de reviews
     */
    Page<ReviewResponse> getAllReviews(Pageable pageable);

    /**
     * Obtiene un review por su identificador.
     *
     * @param id identificador del review
     * @return el review encontrado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el review no existe
     */
    ReviewResponse getReviewById(Long id);

    /**
     * Guarda un nuevo review.
     *
     * @param request datos del review a crear
     * @return el review creado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el usuario o tour plan no existen
     * @throws com.app.panama_trips.exception.BusinessRuleException si el usuario ya escribió un review para este tour
     */
    ReviewResponse saveReview(ReviewRequest request);

    /**
     * Actualiza un review existente.
     *
     * @param id      identificador del review a actualizar
     * @param request nuevos datos del review
     * @return el review actualizado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el review no existe
     */
    ReviewResponse updateReview(Long id, ReviewRequest request);

    /**
     * Elimina un review por su identificador.
     *
     * @param id identificador del review a eliminar
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el review no existe
     */
    void deleteReview(Long id);

    // ==================== Query Methods ====================

    /**
     * Obtiene los reviews de un tour plan específico de forma paginada.
     *
     * @param tourPlanId ID del tour plan
     * @param pageable   información de paginación
     * @return página de reviews del tour plan
     */
    Page<ReviewResponse> getReviewsByTourPlanId(Integer tourPlanId, Pageable pageable);

    /**
     * Obtiene los reviews de un usuario específico de forma paginada.
     *
     * @param userId   ID del usuario
     * @param pageable información de paginación
     * @return página de reviews del usuario
     */
    Page<ReviewResponse> getReviewsByUserId(Long userId, Pageable pageable);

    /**
     * Obtiene los reviews por estado de forma paginada.
     *
     * @param status   estado del review (ACTIVE, PENDING, REMOVED)
     * @param pageable información de paginación
     * @return página de reviews
     */
    Page<ReviewResponse> getReviewsByStatus(String status, Pageable pageable);

    /**
     * Obtiene los reviews más útiles (mayor helpful_votes) de un tour plan.
     *
     * @param tourPlanId ID del tour plan
     * @param pageable   información de paginación
     * @return página de reviews ordenados por votos útiles
     */
    Page<ReviewResponse> getTopReviewsByTourPlanId(Integer tourPlanId, Pageable pageable);

    /**
     * Obtiene los reviews verificados (verified_purchase = true) de un tour plan.
     *
     * @param tourPlanId ID del tour plan
     * @param pageable   información de paginación
     * @return página de reviews verificados
     */
    Page<ReviewResponse> getVerifiedReviewsByTourPlanId(Integer tourPlanId, Pageable pageable);

    // ==================== Statistics ====================

    /**
     * Calcula el promedio de calificación de un tour plan.
     *
     * @param tourPlanId ID del tour plan
     * @return promedio de calificación (0.0 - 5.0)
     */
    Double getAverageRatingByTourPlanId(Integer tourPlanId);

    /**
     * Obtiene estadísticas de reviews de un tour plan.
     *
     * @param tourPlanId ID del tour plan
     * @return mapa con estadísticas: totalReviews, averageRating, verifiedReviewsCount
     */
    Map<String, Object> getReviewStatisticsByTourPlanId(Integer tourPlanId);

    /**
     * Cuenta la cantidad de reviews por estado para un tour plan.
     *
     * @param tourPlanId ID del tour plan
     * @return mapa con estado y cantidad
     */
    Map<String, Long> countReviewsByStatusForTourPlan(Integer tourPlanId);

    // ==================== Actions ====================

    /**
     * Responde a un review como proveedor.
     *
     * @param reviewId         ID del review a responder
     * @param response         respuesta del proveedor
     * @param updatedByUserId  ID del usuario que responde (proveedor)
     * @return el review actualizado con la respuesta
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el review no existe
     */
    ReviewResponse respondToReview(Long reviewId, String response, Long updatedByUserId);

    /**
     * Actualiza el estado de un review.
     *
     * @param reviewId ID del review
     * @param status   nuevo estado (ACTIVE, PENDING, REMOVED)
     * @return el review actualizado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el review no existe
     */
    ReviewResponse updateReviewStatus(Long reviewId, String status);

    /**
     * Marca un review como reportado.
     *
     * @param reviewId ID del review a reportar
     * @return el review actualizado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el review no existe
     */
    ReviewResponse reportReview(Long reviewId);

    /**
     * Incrementa el contador de votos útiles de un review.
     *
     * @param reviewId ID del review
     * @return el review actualizado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el review no existe
     */
    ReviewResponse addHelpfulVote(Long reviewId);

    /**
     * Verifica si un usuario ya escribió un review para un tour plan.
     *
     * @param userId   ID del usuario
     * @param tourPlanId ID del tour plan
     * @return true si ya existe un review, false en caso contrario
     */
    boolean userAlreadyReviewed(Long userId, Integer tourPlanId);
}
