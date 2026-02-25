package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.TourFaqRequest;
import com.app.panama_trips.presentation.dto.TourFaqResponse;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Contrato de servicio para la gestion de preguntas frecuentes (FAQ) de tours.
 * Define operaciones CRUD, busquedas, consultas especializadas y operaciones masivas.
 */
public interface ITourFaqService {
    // CRUD operations

    /**
     * Obtiene todas las preguntas frecuentes de forma paginada.
     *
     * @param pageable informacion de paginacion
     * @return pagina de preguntas frecuentes
     */
    Page<TourFaqResponse> getAllFaqs(Pageable pageable);

    /**
     * Obtiene una pregunta frecuente por su identificador.
     *
     * @param id identificador de la FAQ
     * @return la FAQ encontrada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la FAQ no existe
     */
    TourFaqResponse getFaqById(Integer id);

    /**
     * Guarda una nueva pregunta frecuente.
     *
     * @param request datos de la FAQ a crear
     * @return la FAQ creada
     */
    TourFaqResponse saveFaq(TourFaqRequest request);

    /**
     * Actualiza una pregunta frecuente existente.
     *
     * @param id identificador de la FAQ a actualizar
     * @param request nuevos datos de la FAQ
     * @return la FAQ actualizada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la FAQ no existe
     */
    TourFaqResponse updateFaq(Integer id, TourFaqRequest request);

    /**
     * Elimina una pregunta frecuente por su identificador.
     *
     * @param id identificador de la FAQ a eliminar
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la FAQ no existe
     */
    void deleteFaq(Integer id);

    // Find operations

    /**
     * Obtiene las preguntas frecuentes asociadas a un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return lista de FAQs del plan de tour
     */
    List<TourFaqResponse> findByTourPlanId(Integer tourPlanId);

    /**
     * Obtiene las preguntas frecuentes de un plan de tour ordenadas por orden de visualizacion ascendente.
     *
     * @param tourPlanId identificador del plan de tour
     * @return lista de FAQs ordenadas
     */
    List<TourFaqResponse> findByTourPlanIdOrderByDisplayOrderAsc(Integer tourPlanId);

    /**
     * Busca preguntas frecuentes cuya pregunta o respuesta contenga la palabra clave.
     *
     * @param keyword palabra clave a buscar
     * @return lista de FAQs que coinciden
     */
    List<TourFaqResponse> searchByQuestionOrAnswer(String keyword);

    /**
     * Busca una FAQ especifica por plan de tour y texto de la pregunta.
     *
     * @param tourPlanId identificador del plan de tour
     * @param question texto de la pregunta
     * @return la FAQ encontrada o vacio si no existe
     */
    Optional<TourFaqResponse> findByTourPlanIdAndQuestion(Integer tourPlanId, String question);

    // Specialized queries

    /**
     * Obtiene las FAQs principales de un plan de tour limitadas por cantidad.
     *
     * @param tourPlanId identificador del plan de tour
     * @param limit cantidad maxima de resultados
     * @return lista de FAQs principales
     */
    List<TourFaqResponse> getTopFaqsByTourPlan(Integer tourPlanId, int limit);

    /**
     * Reordena las preguntas frecuentes de un plan de tour segun el orden proporcionado.
     *
     * @param tourPlanId identificador del plan de tour
     * @param faqIdsInOrder lista de IDs de FAQs en el nuevo orden deseado
     */
    void reorderFaqs(Integer tourPlanId, List<Integer> faqIdsInOrder);

    // Bulk operations

    /**
     * Crea multiples preguntas frecuentes de forma masiva.
     *
     * @param requests lista de datos de FAQs a crear
     */
    void bulkCreateFaqs(List<TourFaqRequest> requests);

    /**
     * Elimina multiples preguntas frecuentes de forma masiva.
     *
     * @param faqIds lista de identificadores de FAQs a eliminar
     */
    void bulkDeleteFaqs(List<Integer> faqIds);

    // Check operations

    /**
     * Verifica si existe una FAQ con la pregunta dada para un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @param question texto de la pregunta
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsByTourPlanIdAndQuestion(Integer tourPlanId, String question);

    /**
     * Verifica si el orden de visualizacion es unico dentro de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @param displayOrder orden de visualizacion a verificar
     * @return {@code true} si es unico, {@code false} en caso contrario
     */
    boolean isDisplayOrderUniqueWithinTourPlan(Integer tourPlanId, Integer displayOrder);

    /**
     * Cuenta la cantidad de preguntas frecuentes de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return cantidad de FAQs
     */
    long countByTourPlanId(Integer tourPlanId);
}
