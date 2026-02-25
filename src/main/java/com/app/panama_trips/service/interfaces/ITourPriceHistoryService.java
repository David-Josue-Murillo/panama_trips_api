package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.TourPriceHistoryRequest;
import com.app.panama_trips.presentation.dto.TourPriceHistoryResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Contrato de servicio para la gestion del historial de precios de tours.
 * Define operaciones CRUD, consultas por relaciones, logica de negocio, analiticas y operaciones masivas.
 */
public interface ITourPriceHistoryService {

    // CRUD operations

    /**
     * Obtiene todo el historial de precios de forma paginada.
     *
     * @param pageable informacion de paginacion
     * @return pagina de registros del historial de precios
     */
    Page<TourPriceHistoryResponse> getAllTourPriceHistories(Pageable pageable);

    /**
     * Obtiene un registro del historial de precios por su identificador.
     *
     * @param id identificador del registro
     * @return el registro encontrado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el registro no existe
     */
    TourPriceHistoryResponse getTourPriceHistoryById(Integer id);

    /**
     * Guarda un nuevo registro en el historial de precios.
     *
     * @param request datos del registro a crear
     * @return el registro creado
     */
    TourPriceHistoryResponse saveTourPriceHistory(TourPriceHistoryRequest request);

    /**
     * Actualiza un registro existente del historial de precios.
     *
     * @param id identificador del registro a actualizar
     * @param request nuevos datos del registro
     * @return el registro actualizado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el registro no existe
     */
    TourPriceHistoryResponse updateTourPriceHistory(Integer id, TourPriceHistoryRequest request);

    /**
     * Elimina un registro del historial de precios por su identificador.
     *
     * @param id identificador del registro a eliminar
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el registro no existe
     */
    void deleteTourPriceHistory(Integer id);

    // Find operations by entity relationships

    /**
     * Obtiene el historial de precios de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return lista de registros del historial
     */
    List<TourPriceHistoryResponse> findByTourPlanId(Integer tourPlanId);

    /**
     * Obtiene el historial de precios de un plan de tour ordenado por fecha de cambio descendente, paginado.
     *
     * @param tourPlanId identificador del plan de tour
     * @param pageable informacion de paginacion
     * @return pagina de registros ordenados por fecha descendente
     */
    Page<TourPriceHistoryResponse> findByTourPlanIdOrderByChangedAtDesc(Integer tourPlanId, Pageable pageable);

    /**
     * Obtiene el historial de precios de un plan de tour dentro de un rango de fechas.
     *
     * @param tourPlanId identificador del plan de tour
     * @param startDate fecha y hora de inicio del rango
     * @param endDate fecha y hora de fin del rango
     * @return lista de registros dentro del rango
     */
    List<TourPriceHistoryResponse> findByTourPlanIdAndChangedAtBetween(Integer tourPlanId, LocalDateTime startDate,LocalDateTime endDate);

    /**
     * Obtiene los cambios de precio realizados por un usuario especifico.
     *
     * @param userId identificador del usuario
     * @return lista de registros del historial
     */
    List<TourPriceHistoryResponse> findByChangedById(Long userId);

    // Specialized queries from repository

    /**
     * Calcula el porcentaje promedio de cambio de precio para un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return porcentaje promedio de cambio, o {@code null} si no hay datos
     */
    Double calculateAveragePriceChangePercentageByTourPlanId(Integer tourPlanId);

    /**
     * Obtiene registros cuyo nuevo precio es mayor al valor proporcionado.
     *
     * @param price precio de referencia
     * @return lista de registros con precio mayor
     */
    List<TourPriceHistoryResponse> findByNewPriceGreaterThan(BigDecimal price);

    /**
     * Cuenta la cantidad de cambios de precio para un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return cantidad de cambios de precio
     */
    Long countPriceChangesByTourPlanId(Integer tourPlanId);

    // Business logic operations

    /**
     * Obtiene los cambios de precio mas recientes limitados por cantidad.
     *
     * @param limit cantidad maxima de resultados
     * @return lista de cambios recientes
     */
    List<TourPriceHistoryResponse> getRecentChanges(int limit);

    /**
     * Obtiene el ultimo cambio de precio de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return el ultimo cambio o vacio si no hay registros
     */
    Optional<TourPriceHistoryResponse> getLatestChangeForTourPlan(Integer tourPlanId);

    /**
     * Obtiene el precio actual de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return precio actual
     */
    BigDecimal getCurrentPriceForTourPlan(Integer tourPlanId);

    /**
     * Obtiene el precio anterior de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return precio anterior
     */
    BigDecimal getPreviousPriceForTourPlan(Integer tourPlanId);

    /**
     * Obtiene el precio maximo historico de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return precio maximo
     */
    BigDecimal getMaxPriceForTourPlan(Integer tourPlanId);

    /**
     * Obtiene el precio minimo historico de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return precio minimo
     */
    BigDecimal getMinPriceForTourPlan(Integer tourPlanId);

    /**
     * Obtiene el precio promedio historico de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return precio promedio
     */
    BigDecimal getAveragePriceForTourPlan(Integer tourPlanId);

    /**
     * Obtiene los cambios de precio de un plan de tour en una fecha especifica.
     *
     * @param tourPlanId identificador del plan de tour
     * @param date fecha a consultar
     * @return lista de cambios de precio en esa fecha
     */
    List<TourPriceHistoryResponse> getPriceChangesOnDate(Integer tourPlanId, LocalDate date);

    /**
     * Obtiene cambios de precio realizados por un usuario dentro de un rango de fechas.
     *
     * @param userId identificador del usuario
     * @param startDate fecha y hora de inicio del rango
     * @param endDate fecha y hora de fin del rango
     * @return lista de cambios de precio
     */
    List<TourPriceHistoryResponse> getPriceChangesByUserAndDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    // Bulk operations

    /**
     * Crea multiples registros de historial de precios de forma masiva.
     *
     * @param requests lista de datos de registros a crear
     */
    void bulkCreateTourPriceHistories(List<TourPriceHistoryRequest> requests);

    /**
     * Elimina multiples registros de historial de precios de forma masiva.
     *
     * @param tourPriceHistoryIds lista de identificadores a eliminar
     */
    void bulkDeleteTourPriceHistories(List<Integer> tourPriceHistoryIds);

    // Check operations

    /**
     * Verifica si existe un registro del historial por su identificador.
     *
     * @param id identificador del registro
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsById(Integer id);

    /**
     * Cuenta la cantidad de registros de historial para un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return cantidad de registros
     */
    long countByTourPlanId(Integer tourPlanId);

    // Analytics and statistics

    /**
     * Obtiene el total de incrementos de precio para un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return suma total de incrementos de precio
     */
    BigDecimal getTotalPriceIncreaseForTourPlan(Integer tourPlanId);

    /**
     * Obtiene el total de decrementos de precio para un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return suma total de decrementos de precio
     */
    BigDecimal getTotalPriceDecreaseForTourPlan(Integer tourPlanId);

    /**
     * Obtiene el porcentaje promedio de cambio de precio para un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return porcentaje promedio de cambio
     */
    double getAverageChangePercentageForTourPlan(Integer tourPlanId);

    /**
     * Obtiene los planes de tour con mayor cantidad de cambios de precio.
     *
     * @param limit cantidad maxima de resultados
     * @return lista de registros de los planes con mas cambios
     */
    List<TourPriceHistoryResponse> getTopTourPlansByChangeCount(int limit);

    // Utility operations

    /**
     * Busca cambios de precio que coincidan con un precio especifico.
     *
     * @param price precio a buscar
     * @return lista de registros que coinciden
     */
    List<TourPriceHistoryResponse> searchChangesByPrice(BigDecimal price);
}
