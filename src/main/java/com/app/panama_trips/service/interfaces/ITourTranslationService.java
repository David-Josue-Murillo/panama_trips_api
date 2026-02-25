package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.TourTranslationRequest;
import com.app.panama_trips.presentation.dto.TourTranslationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Contrato de servicio para la gestion de traducciones de tours.
 * Define operaciones CRUD y metodos adicionales para consultar, contar y eliminar traducciones.
 */
public interface ITourTranslationService {
    // CRUD operations

    /**
     * Obtiene todas las traducciones de tours de forma paginada.
     *
     * @param pageable informacion de paginacion
     * @return pagina de traducciones
     */
    Page<TourTranslationResponse> getAllTourTranslations(Pageable pageable);

    /**
     * Obtiene una traduccion por plan de tour y codigo de idioma.
     *
     * @param tourPlanId identificador del plan de tour
     * @param languageCode codigo del idioma (ej. "es", "en")
     * @return la traduccion encontrada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la traduccion no existe
     */
    TourTranslationResponse getTourTranslationByTourPlanIdAndLanguageCode(Integer tourPlanId, String languageCode);

    /**
     * Guarda una nueva traduccion de tour.
     *
     * @param request datos de la traduccion a crear
     * @return la traduccion creada
     */
    TourTranslationResponse saveTourTranslation(TourTranslationRequest request);

    /**
     * Actualiza una traduccion existente identificada por plan de tour y codigo de idioma.
     *
     * @param tourPlanId identificador del plan de tour
     * @param languageCode codigo del idioma
     * @param request nuevos datos de la traduccion
     * @return la traduccion actualizada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la traduccion no existe
     */
    TourTranslationResponse updateTourTranslation(Integer tourPlanId, String languageCode,TourTranslationRequest request);

    /**
     * Elimina una traduccion por plan de tour y codigo de idioma.
     *
     * @param tourPlanId identificador del plan de tour
     * @param languageCode codigo del idioma
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la traduccion no existe
     */
    void deleteTourTranslation(Integer tourPlanId, String languageCode);

    // Additional service methods

    /**
     * Obtiene todas las traducciones de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return lista de traducciones del plan de tour
     */
    List<TourTranslationResponse> getTourTranslationsByTourPlanId(Integer tourPlanId);

    /**
     * Obtiene todas las traducciones en un idioma especifico.
     *
     * @param languageCode codigo del idioma
     * @return lista de traducciones en el idioma indicado
     */
    List<TourTranslationResponse> getTourTranslationsByLanguageCode(String languageCode);

    /**
     * Verifica si existe una traduccion para un plan de tour en un idioma dado.
     *
     * @param tourPlanId identificador del plan de tour
     * @param languageCode codigo del idioma
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsByTourPlanIdAndLanguageCode(Integer tourPlanId, String languageCode);

    /**
     * Elimina todas las traducciones de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     */
    void deleteAllTranslationsByTourPlanId(Integer tourPlanId);

    /**
     * Elimina todas las traducciones en un idioma especifico.
     *
     * @param languageCode codigo del idioma
     */
    void deleteAllTranslationsByLanguageCode(String languageCode);

    /**
     * Cuenta las traducciones de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return cantidad de traducciones
     */
    Long countTranslationsByTourPlanId(Integer tourPlanId);

    /**
     * Cuenta el total de traducciones de tours en el sistema.
     *
     * @return cantidad total de traducciones
     */
    long countAllTourTranslations();
}
