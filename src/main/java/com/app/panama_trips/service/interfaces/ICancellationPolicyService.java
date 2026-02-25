package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.CancellationPolicyRequest;
import com.app.panama_trips.presentation.dto.CancellationPolicyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de servicio para la gestion de politicas de cancelacion.
 * Provee operaciones CRUD, consultas de elegibilidad, calculo de reembolsos y operaciones masivas.
 */
public interface ICancellationPolicyService {

    // CRUD operations

    /**
     * Obtiene todas las politicas de cancelacion de forma paginada.
     *
     * @param pageable configuracion de paginacion
     * @return pagina de politicas de cancelacion
     */
    Page<CancellationPolicyResponse> getAllCancellationPolicies(Pageable pageable);

    /**
     * Obtiene una politica de cancelacion por su identificador.
     *
     * @param id identificador de la politica
     * @return la politica encontrada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la politica no existe
     */
    CancellationPolicyResponse getCancellationPolicyById(Integer id);

    /**
     * Crea una nueva politica de cancelacion.
     *
     * @param request datos de la politica a crear
     * @return la politica creada
     */
    CancellationPolicyResponse saveCancellationPolicy(CancellationPolicyRequest request);

    /**
     * Actualiza una politica de cancelacion existente.
     *
     * @param id identificador de la politica
     * @param request datos actualizados de la politica
     * @return la politica actualizada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la politica no existe
     */
    CancellationPolicyResponse updateCancellationPolicy(Integer id, CancellationPolicyRequest request);

    /**
     * Elimina una politica de cancelacion por su identificador.
     *
     * @param id identificador de la politica
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la politica no existe
     */
    void deleteCancellationPolicy(Integer id);

    // Find operations

    /**
     * Busca politicas con porcentaje de reembolso mayor o igual al indicado.
     *
     * @param minRefundPercentage porcentaje minimo de reembolso
     * @return lista de politicas que cumplen el criterio
     */
    List<CancellationPolicyResponse> findByRefundPercentageGreaterThanEqual(Integer minRefundPercentage);

    /**
     * Busca politicas con dias antes del tour mayor o igual al indicado.
     *
     * @param minDaysBeforeTour dias minimos antes del tour
     * @return lista de politicas que cumplen el criterio
     */
    List<CancellationPolicyResponse> findByDaysBeforeTourGreaterThanEqual(Integer minDaysBeforeTour);

    /**
     * Busca una politica por su nombre.
     *
     * @param name nombre de la politica
     * @return la politica encontrada o vacio si no existe
     */
    Optional<CancellationPolicyResponse> findByName(String name);

    /**
     * Busca politicas elegibles segun porcentaje minimo y dias maximos.
     *
     * @param minPercentage porcentaje minimo de reembolso
     * @param maxDays dias maximos antes del tour
     * @return lista de politicas elegibles
     */
    List<CancellationPolicyResponse> findEligiblePolicies(Integer minPercentage, Integer maxDays);

    // Specialized queries

    /**
     * Obtiene la politica recomendada segun los dias restantes antes del viaje.
     *
     * @param daysBeforeTrip dias restantes antes del viaje
     * @return la politica recomendada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no hay politica aplicable
     */
    CancellationPolicyResponse getRecommendedPolicy(Integer daysBeforeTrip);

    /**
     * Verifica si una politica es elegible para reembolso dados los dias restantes.
     *
     * @param policyId identificador de la politica
     * @param daysRemaining dias restantes antes del tour
     * @return {@code true} si es elegible, {@code false} en caso contrario
     */
    boolean isPolicyEligibleForRefund(Integer policyId, Integer daysRemaining);

    /**
     * Calcula el monto de reembolso aplicando la politica al monto total.
     *
     * @param policyId identificador de la politica
     * @param totalAmount monto total de la reserva
     * @param daysRemaining dias restantes antes del tour
     * @return monto de reembolso calculado
     */
    Integer calculateRefundAmount(Integer policyId, Integer totalAmount, Integer daysRemaining);

    /**
     * Obtiene todas las politicas activas.
     *
     * @return lista de politicas activas
     */
    List<CancellationPolicyResponse> getActivePolicies();

    // Bulk operations

    /**
     * Crea multiples politicas de cancelacion en lote.
     *
     * @param requests lista de politicas a crear
     */
    void bulkCreatePolicies(List<CancellationPolicyRequest> requests);

    /**
     * Actualiza multiples politicas de cancelacion en lote.
     *
     * @param requests lista de politicas a actualizar
     */
    void bulkUpdatePolicies(List<CancellationPolicyRequest> requests);

    // Check operations

    /**
     * Verifica si existe una politica con el nombre dado.
     *
     * @param name nombre de la politica
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsPolicyWithName(String name);

    /**
     * Verifica si una politica esta siendo utilizada por algun tour.
     *
     * @param policyId identificador de la politica
     * @return {@code true} si esta en uso, {@code false} en caso contrario
     */
    boolean isPolicyUsedByAnyTour(Integer policyId);
}