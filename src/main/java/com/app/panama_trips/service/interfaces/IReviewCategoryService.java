package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.ReviewCategoryRequest;
import com.app.panama_trips.presentation.dto.ReviewCategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Contrato de servicio para la gestion de categorias de resenas.
 * Define las operaciones CRUD basicas para categorias de evaluacion.
 */
public interface IReviewCategoryService {
  // CRUD operations

  /**
   * Obtiene todas las categorias de resenas de forma paginada.
   *
   * @param pageable informacion de paginacion
   * @return pagina de categorias de resenas
   */
  Page<ReviewCategoryResponse> getAllReviewCategories(Pageable pageable);

  /**
   * Obtiene una categoria de resena por su identificador.
   *
   * @param id identificador de la categoria
   * @return la categoria encontrada
   * @throws com.app.panama_trips.exception.ResourceNotFoundException si la categoria no existe
   */
  ReviewCategoryResponse getReviewCategoryById(Integer id);

  /**
   * Guarda una nueva categoria de resena.
   *
   * @param request datos de la categoria a crear
   * @return la categoria creada
   */
  ReviewCategoryResponse saveReviewCategory(ReviewCategoryRequest request);

  /**
   * Actualiza una categoria de resena existente.
   *
   * @param id identificador de la categoria a actualizar
   * @param request nuevos datos de la categoria
   * @return la categoria actualizada
   * @throws com.app.panama_trips.exception.ResourceNotFoundException si la categoria no existe
   */
  ReviewCategoryResponse updateReviewCategory(Integer id, ReviewCategoryRequest request);

  /**
   * Elimina una categoria de resena por su identificador.
   *
   * @param id identificador de la categoria a eliminar
   * @throws com.app.panama_trips.exception.ResourceNotFoundException si la categoria no existe
   */
  void deleteReviewCategory(Integer id);
}
