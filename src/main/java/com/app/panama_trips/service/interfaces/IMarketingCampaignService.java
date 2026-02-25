package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.enums.CampaignStatus;
import com.app.panama_trips.persistence.entity.enums.CampaignType;
import com.app.panama_trips.presentation.dto.MarketingCampaignRequest;
import com.app.panama_trips.presentation.dto.MarketingCampaignResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de servicio para la gestion de campanas de marketing.
 * Provee operaciones CRUD, consultas por creador/estado/tipo, calculos financieros
 * de presupuesto, gestion de estados, seguimiento de clics, estadisticas y operaciones en lote.
 */
public interface IMarketingCampaignService {

    // ==================== CRUD operations ====================

    /**
     * Obtiene todas las campanas de marketing de forma paginada.
     *
     * @param pageable parametros de paginacion
     * @return pagina de campanas de marketing
     */
    Page<MarketingCampaignResponse> getAllMarketingCampaigns(Pageable pageable);

    /**
     * Obtiene una campana de marketing por su identificador.
     *
     * @param id identificador de la campana
     * @return la campana encontrada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    MarketingCampaignResponse getMarketingCampaignById(Integer id);

    /**
     * Guarda una nueva campana de marketing.
     *
     * @param request datos de la campana a guardar
     * @return la campana persistida
     */
    MarketingCampaignResponse saveMarketingCampaign(MarketingCampaignRequest request);

    /**
     * Actualiza una campana de marketing existente.
     *
     * @param id identificador de la campana a actualizar
     * @param request datos actualizados de la campana
     * @return la campana actualizada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    MarketingCampaignResponse updateMarketingCampaign(Integer id, MarketingCampaignRequest request);

    /**
     * Elimina una campana de marketing por su identificador.
     *
     * @param id identificador de la campana a eliminar
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    void deleteMarketingCampaign(Integer id);

    // ==================== Find operations by entity relationships ====================

    /**
     * Busca campanas por identificador del creador.
     *
     * @param createdById identificador del usuario creador
     * @return lista de campanas creadas por el usuario
     */
    List<MarketingCampaignResponse> findByCreatedById(Integer createdById);

    /**
     * Busca campanas por estado.
     *
     * @param status estado de la campana
     * @return lista de campanas con el estado indicado
     */
    List<MarketingCampaignResponse> findByStatus(CampaignStatus status);

    /**
     * Busca campanas por tipo.
     *
     * @param type tipo de campana
     * @return lista de campanas del tipo indicado
     */
    List<MarketingCampaignResponse> findByType(CampaignType type);

    /**
     * Busca campanas con fecha de inicio posterior a la fecha dada.
     *
     * @param date fecha limite inferior
     * @return lista de campanas que inician despues de la fecha
     */
    List<MarketingCampaignResponse> findByStartDateAfter(LocalDateTime date);

    /**
     * Busca campanas con fecha de fin anterior a la fecha dada.
     *
     * @param date fecha limite superior
     * @return lista de campanas que finalizan antes de la fecha
     */
    List<MarketingCampaignResponse> findByEndDateBefore(LocalDateTime date);

    /**
     * Busca campanas actualmente activas.
     *
     * @return lista de campanas activas
     */
    List<MarketingCampaignResponse> findActiveCampaigns();

    // ==================== Specialized queries from repository ====================

    /**
     * Busca campanas proximas a iniciar.
     *
     * @return lista de campanas proximas
     */
    List<MarketingCampaignResponse> findUpcomingCampaigns();

    /**
     * Busca campanas expiradas.
     *
     * @return lista de campanas expiradas
     */
    List<MarketingCampaignResponse> findExpiredCampaigns();

    /**
     * Suma el presupuesto total de campanas por estado.
     *
     * @param status estado de la campana
     * @return suma total del presupuesto
     */
    BigDecimal sumBudgetByStatus(CampaignStatus status);

    /**
     * Cuenta la cantidad de campanas activas.
     *
     * @return cantidad de campanas activas
     */
    Long countActiveCampaigns();

    // ==================== Business logic operations ====================

    /**
     * Obtiene todas las campanas activas.
     *
     * @return lista de campanas activas
     */
    List<MarketingCampaignResponse> getActiveCampaigns();

    /**
     * Obtiene todas las campanas en estado borrador.
     *
     * @return lista de campanas en borrador
     */
    List<MarketingCampaignResponse> getDraftCampaigns();

    /**
     * Obtiene todas las campanas pausadas.
     *
     * @return lista de campanas pausadas
     */
    List<MarketingCampaignResponse> getPausedCampaigns();

    /**
     * Obtiene todas las campanas completadas.
     *
     * @return lista de campanas completadas
     */
    List<MarketingCampaignResponse> getCompletedCampaigns();

    /**
     * Obtiene campanas dentro de un rango de fechas.
     *
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de campanas dentro del rango
     */
    List<MarketingCampaignResponse> getCampaignsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Obtiene campanas por creador y estado.
     *
     * @param createdById identificador del creador
     * @param status estado de la campana
     * @return lista de campanas que coinciden
     */
    List<MarketingCampaignResponse> getCampaignsByCreatedByAndStatus(Integer createdById, CampaignStatus status);

    // ==================== Advanced queries ====================

    /**
     * Obtiene las campanas mas recientes limitadas por cantidad.
     *
     * @param limit cantidad maxima de resultados
     * @return lista de las campanas mas recientes
     */
    List<MarketingCampaignResponse> getRecentCampaigns(int limit);

    /**
     * Obtiene campanas dentro de un rango de presupuesto.
     *
     * @param minBudget presupuesto minimo
     * @param maxBudget presupuesto maximo
     * @return lista de campanas dentro del rango de presupuesto
     */
    List<MarketingCampaignResponse> getCampaignsByBudgetRange(BigDecimal minBudget, BigDecimal maxBudget);

    /**
     * Obtiene campanas por audiencia objetivo.
     *
     * @param targetAudience audiencia objetivo a buscar
     * @return lista de campanas dirigidas a la audiencia
     */
    List<MarketingCampaignResponse> getCampaignsByTargetAudience(String targetAudience);

    /**
     * Obtiene las campanas con mayor cantidad de clics.
     *
     * @param limit cantidad maxima de resultados
     * @return lista de campanas con mas clics
     */
    List<MarketingCampaignResponse> getTopCampaignsByClicks(int limit);

    // ==================== Bulk operations ====================

    /**
     * Crea multiples campanas de marketing en lote.
     *
     * @param requests lista de solicitudes de campanas a crear
     */
    void bulkCreateCampaigns(List<MarketingCampaignRequest> requests);

    /**
     * Elimina multiples campanas de marketing por sus identificadores.
     *
     * @param campaignIds lista de identificadores a eliminar
     */
    void bulkDeleteCampaigns(List<Integer> campaignIds);

    /**
     * Actualiza el estado de multiples campanas.
     *
     * @param campaignIds lista de identificadores de campanas
     * @param newStatus nuevo estado a asignar
     */
    void bulkUpdateStatus(List<Integer> campaignIds, CampaignStatus newStatus);

    /**
     * Incrementa los clics de multiples campanas.
     *
     * @param campaignIds lista de identificadores de campanas
     */
    void bulkIncrementClicks(List<Integer> campaignIds);

    // ==================== Check operations ====================

    /**
     * Verifica si existe una campana por su identificador.
     *
     * @param id identificador de la campana
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsById(Integer id);

    /**
     * Verifica si existe una campana con el nombre dado.
     *
     * @param name nombre de la campana
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsByName(String name);

    /**
     * Verifica si existen campanas creadas por un usuario.
     *
     * @param createdById identificador del creador
     * @return {@code true} si existen campanas, {@code false} en caso contrario
     */
    boolean existsByCreatedById(Integer createdById);

    /**
     * Cuenta las campanas creadas por un usuario.
     *
     * @param createdById identificador del creador
     * @return cantidad de campanas
     */
    long countByCreatedById(Integer createdById);

    /**
     * Cuenta las campanas por estado.
     *
     * @param status estado de la campana
     * @return cantidad de campanas
     */
    long countByStatus(CampaignStatus status);

    /**
     * Cuenta las campanas con fecha de inicio posterior a la fecha dada.
     *
     * @param date fecha limite
     * @return cantidad de campanas
     */
    long countByStartDateAfter(LocalDateTime date);

    // ==================== Financial operations ====================

    /**
     * Calcula el presupuesto total de todas las campanas.
     *
     * @return presupuesto total
     */
    BigDecimal calculateTotalBudget();

    /**
     * Calcula el presupuesto total por estado de campana.
     *
     * @param status estado de la campana
     * @return presupuesto total para el estado
     */
    BigDecimal calculateTotalBudgetByStatus(CampaignStatus status);

    /**
     * Calcula el presupuesto restante por estado de campana.
     *
     * @param status estado de la campana
     * @return presupuesto restante
     */
    BigDecimal calculateRemainingBudgetByStatus(CampaignStatus status);

    // ==================== Statistics and analytics ====================

    /**
     * Obtiene el total de campanas en el sistema.
     *
     * @return cantidad total de campanas
     */
    long getTotalCampaigns();

    /**
     * Obtiene el total de campanas activas.
     *
     * @return cantidad de campanas activas
     */
    long getTotalActiveCampaigns();

    /**
     * Obtiene el total de campanas completadas.
     *
     * @return cantidad de campanas completadas
     */
    long getTotalCompletedCampaigns();

    /**
     * Obtiene el presupuesto total gastado en campanas.
     *
     * @return presupuesto total gastado
     */
    BigDecimal getTotalBudgetSpent();

    /**
     * Calcula la tasa de exito de las campanas.
     *
     * @return porcentaje de exito (0.0 a 100.0)
     */
    double getCampaignSuccessRate();

    /**
     * Obtiene las mejores campanas por mes limitadas por cantidad.
     *
     * @param limit cantidad maxima de resultados
     * @return lista de campanas destacadas por mes
     */
    List<MarketingCampaignResponse> getTopCampaignsByMonth(int limit);

    /**
     * Obtiene las campanas agrupadas por mes.
     *
     * @return lista de campanas agrupadas por mes
     */
    List<MarketingCampaignResponse> getCampaignsByMonth();

    // ==================== Status management operations ====================

    /**
     * Activa una campana de marketing.
     *
     * @param campaignId identificador de la campana
     * @return la campana activada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    MarketingCampaignResponse activateCampaign(Integer campaignId);

    /**
     * Pausa una campana de marketing.
     *
     * @param campaignId identificador de la campana
     * @return la campana pausada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    MarketingCampaignResponse pauseCampaign(Integer campaignId);

    /**
     * Marca una campana como completada.
     *
     * @param campaignId identificador de la campana
     * @return la campana completada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    MarketingCampaignResponse completeCampaign(Integer campaignId);

    /**
     * Cancela una campana de marketing.
     *
     * @param campaignId identificador de la campana
     * @return la campana cancelada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    MarketingCampaignResponse cancelCampaign(Integer campaignId);

    /**
     * Verifica si una transicion de estado es valida para una campana.
     *
     * @param campaignId identificador de la campana
     * @param newStatus nuevo estado propuesto
     * @return {@code true} si la transicion es valida, {@code false} en caso contrario
     */
    boolean isValidStatusTransition(Integer campaignId, CampaignStatus newStatus);

    /**
     * Obtiene las transiciones de estado validas para una campana.
     *
     * @param campaignId identificador de la campana
     * @return lista de estados validos para transicionar
     */
    List<CampaignStatus> getValidStatusTransitions(Integer campaignId);

    // ==================== Clicks operations ====================

    /**
     * Incrementa el contador de clics de una campana.
     *
     * @param campaignId identificador de la campana
     * @return la campana con clics actualizados
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    MarketingCampaignResponse incrementClicks(Integer campaignId);

    /**
     * Obtiene campanas que necesitan actualizacion de clics.
     *
     * @return lista de campanas pendientes de actualizacion de clics
     */
    List<MarketingCampaignResponse> getCampaignsNeedingClicksUpdate();

    /**
     * Actualiza los clics de todas las campanas activas.
     */
    void updateClicksForActiveCampaigns();

    // ==================== Utility operations ====================

    /**
     * Recalcula el estado de todas las campanas segun sus fechas y condiciones.
     */
    void recalculateCampaignStatus();

    /**
     * Elimina campanas expiradas segun la cantidad de dias a conservar.
     *
     * @param daysToKeep cantidad de dias a mantener
     */
    void cleanupExpiredCampaigns(int daysToKeep);

    /**
     * Busca campanas por presupuesto exacto.
     *
     * @param budget presupuesto a buscar
     * @return lista de campanas con el presupuesto indicado
     */
    List<MarketingCampaignResponse> searchCampaignsByBudget(BigDecimal budget);

    /**
     * Busca la campana mas reciente creada por un usuario.
     *
     * @param createdById identificador del creador
     * @return la campana mas reciente, o vacio si no existe
     */
    Optional<MarketingCampaignResponse> findLatestCampaignByCreatedBy(Integer createdById);

    /**
     * Obtiene campanas con presupuesto alto.
     *
     * @return lista de campanas con alto presupuesto
     */
    List<MarketingCampaignResponse> getHighBudgetCampaigns();
}