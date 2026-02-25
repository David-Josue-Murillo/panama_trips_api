package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.TourPlanImageRequest;
import com.app.panama_trips.presentation.dto.TourPlanImageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Contrato de servicio para la gestion de imagenes de planes de tour.
 * Provee operaciones CRUD, ordenamiento por prioridad de visualizacion y gestion de imagen principal.
 */
public interface ITourPlanImageService {

    // CRUD operations

    /**
     * Obtiene todas las imagenes de planes de tour de forma paginada.
     *
     * @param pageable configuracion de paginacion
     * @return pagina de imagenes
     */
    Page<TourPlanImageResponse> getAllTourPlanImages(Pageable pageable);

    /**
     * Obtiene una imagen por su identificador.
     *
     * @param id identificador de la imagen
     * @return la imagen encontrada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la imagen no existe
     */
    TourPlanImageResponse getTourPlanImageById(Integer id);

    /**
     * Registra una nueva imagen para un plan de tour.
     *
     * @param tourPlanImageRequest datos de la imagen a crear
     * @return la imagen creada
     */
    TourPlanImageResponse saveTourPlanImage(TourPlanImageRequest tourPlanImageRequest);

    /**
     * Actualiza una imagen existente.
     *
     * @param id identificador de la imagen
     * @param tourPlanImageRequest datos actualizados de la imagen
     * @return la imagen actualizada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la imagen no existe
     */
    TourPlanImageResponse updateTourPlanImage(Integer id, TourPlanImageRequest tourPlanImageRequest);

    /**
     * Elimina una imagen por su identificador.
     *
     * @param id identificador de la imagen
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la imagen no existe
     */
    void deleteTourPlanImage(Integer id);

    // Additional service methods

    /**
     * Obtiene todas las imagenes de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return lista de imagenes del plan
     */
    List<TourPlanImageResponse> getTourPlanImagesByTourPlanId(Integer tourPlanId);

    /**
     * Obtiene las imagenes de un plan de tour ordenadas por orden de visualizacion.
     *
     * @param tourPlanId identificador del plan de tour
     * @return lista de imagenes ordenadas por display order
     */
    List<TourPlanImageResponse> getTourPlanImagesByTourPlanIdOrderByDisplayOrder(Integer tourPlanId);

    /**
     * Obtiene la imagen principal de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return la imagen principal
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no tiene imagen principal
     */
    TourPlanImageResponse getMainImageByTourPlanId(Integer tourPlanId);

    /**
     * Obtiene las imagenes secundarias de un plan de tour, ordenadas por display order.
     *
     * @param tourPlanId identificador del plan de tour
     * @return lista de imagenes no principales ordenadas
     */
    List<TourPlanImageResponse> getNonMainImagesByTourPlanIdOrdered(Integer tourPlanId);

    /**
     * Obtiene el mayor orden de visualizacion asignado a las imagenes de un plan.
     *
     * @param tourPlanId identificador del plan de tour
     * @return valor maximo de display order, o {@code null} si no hay imagenes
     */
    Integer getMaxDisplayOrderForTourPlan(Integer tourPlanId);

    /**
     * Cuenta el numero de imagenes asociadas a un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return cantidad de imagenes
     */
    Long countImagesByTourPlanId(Integer tourPlanId);

    /**
     * Elimina todas las imagenes de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     */
    void deleteAllImagesByTourPlanId(Integer tourPlanId);

    /**
     * Verifica si existe una imagen con la URL dada para un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @param imageUrl URL de la imagen a verificar
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsImageWithUrlForTourPlan(Integer tourPlanId, String imageUrl);
}
