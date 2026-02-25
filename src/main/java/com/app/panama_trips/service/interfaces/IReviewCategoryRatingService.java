package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.ReviewCategoryRatingRequest;
import com.app.panama_trips.presentation.dto.ReviewCategoryRatingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Contrato de servicio para la gestion de calificaciones por categoria de resena.
 * Define operaciones CRUD y operaciones de negocio como promedios, conteos y consultas por resena o categoria.
 */
public interface IReviewCategoryRatingService {
  // CRUD operations

  /**
   * Obtiene todas las calificaciones por categoria de forma paginada.
   *
   * @param pageable informacion de paginacion
   * @return pagina de calificaciones por categoria
   */
  Page<ReviewCategoryRatingResponse> getAllReviewCategoryRatings(Pageable pageable);

  /**
   * Obtiene una calificacion por categoria usando la clave compuesta resena-categoria.
   *
   * @param reviewId identificador de la resena
   * @param categoryId identificador de la categoria
   * @return la calificacion encontrada
   * @throws com.app.panama_trips.exception.ResourceNotFoundException si la calificacion no existe
   */
  ReviewCategoryRatingResponse getReviewCategoryRatingById(Integer reviewId, Integer categoryId);

  /**
   * Guarda una nueva calificacion por categoria de resena.
   *
   * @param request datos de la calificacion a crear
   * @return la calificacion creada
   */
  ReviewCategoryRatingResponse saveReviewCategoryRating(ReviewCategoryRatingRequest request);

  /**
   * Actualiza una calificacion existente por categoria de resena.
   *
   * @param reviewId identificador de la resena
   * @param categoryId identificador de la categoria
   * @param request nuevos datos de la calificacion
   * @return la calificacion actualizada
   * @throws com.app.panama_trips.exception.ResourceNotFoundException si la calificacion no existe
   */
  ReviewCategoryRatingResponse updateReviewCategoryRating(Integer reviewId, Integer categoryId, ReviewCategoryRatingRequest request);

  /**
   * Elimina una calificacion por categoria de resena.
   *
   * @param reviewId identificador de la resena
   * @param categoryId identificador de la categoria
   * @throws com.app.panama_trips.exception.ResourceNotFoundException si la calificacion no existe
   */
  void deleteReviewCategoryRating(Integer reviewId, Integer categoryId);

  // Business operations

  /**
   * Obtiene todas las calificaciones asociadas a una resena.
   *
   * @param reviewId identificador de la resena
   * @return lista de calificaciones de la resena
   */
  List<ReviewCategoryRatingResponse> getRatingsByReview(Integer reviewId);

  /**
   * Obtiene todas las calificaciones de una categoria especifica.
   *
   * @param categoryId identificador de la categoria
   * @return lista de calificaciones de la categoria
   */
  List<ReviewCategoryRatingResponse> getRatingsByCategory(Integer categoryId);

  /**
   * Obtiene el promedio de calificacion de una categoria.
   *
   * @param categoryId identificador de la categoria
   * @return promedio de calificacion, o {@code null} si no hay datos
   */
  Double getAverageRatingByCategory(Integer categoryId);

  /**
   * Obtiene los promedios de calificacion por categoria para un tour especifico.
   *
   * @param tourPlanId identificador del plan de tour
   * @return mapa de identificador de categoria a promedio de calificacion
   */
  Map<Integer, Double> getAverageRatingsByCategoryForTour(Long tourPlanId);

  /**
   * Cuenta las calificaciones iguales o mayores a un valor minimo.
   *
   * @param minRating calificacion minima
   * @return cantidad de calificaciones que cumplen el criterio
   */
  Long countRatingsGreaterThanEqual(Integer minRating);

  /**
   * Elimina todas las calificaciones asociadas a una resena.
   *
   * @param reviewId identificador de la resena
   */
  void deleteRatingsByReview(Integer reviewId);

  /**
   * Verifica si existe una calificacion para una combinacion de resena y categoria.
   *
   * @param reviewId identificador de la resena
   * @param categoryId identificador de la categoria
   * @return {@code true} si existe, {@code false} en caso contrario
   */
  boolean existsByReviewAndCategory(Integer reviewId, Integer categoryId);
}
