package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.presentation.dto.TourPlanSpecialPriceRequest;
import com.app.panama_trips.presentation.dto.TourPlanSpecialPriceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de servicio para la gestion de precios especiales de planes de tour.
 * Provee operaciones CRUD, deteccion de periodos solapados y consultas por rango de fechas y precios.
 */
public interface ITourPlanSpecialPriceService {

    // CRUD operations

    /**
     * Obtiene todos los precios especiales de forma paginada.
     *
     * @param pageable configuracion de paginacion
     * @return pagina de precios especiales
     */
    Page<TourPlanSpecialPriceResponse> getAll(Pageable pageable);

    /**
     * Obtiene un precio especial por su identificador.
     *
     * @param id identificador del precio especial
     * @return el precio especial encontrado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    TourPlanSpecialPriceResponse findById(Integer id);

    /**
     * Crea un nuevo precio especial para un plan de tour.
     *
     * @param request datos del precio especial a crear
     * @return el precio especial creado
     */
    TourPlanSpecialPriceResponse save(TourPlanSpecialPriceRequest request);

    /**
     * Actualiza un precio especial existente.
     *
     * @param id identificador del precio especial
     * @param tourPlanSpecialPrice datos actualizados del precio especial
     * @return el precio especial actualizado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    TourPlanSpecialPriceResponse update(Integer id, TourPlanSpecialPriceRequest tourPlanSpecialPrice);

    /**
     * Elimina un precio especial por su identificador.
     *
     * @param id identificador del precio especial
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    void deleteById(Integer id);

    // Find operations

    /**
     * Obtiene todos los precios especiales de un plan de tour.
     *
     * @param tourPlan entidad del plan de tour
     * @return lista de precios especiales del plan
     */
    List<TourPlanSpecialPriceResponse> findByTourPlan(TourPlan tourPlan);

    /**
     * Obtiene los precios especiales de un plan de tour ordenados por fecha de inicio ascendente.
     *
     * @param tourPlan entidad del plan de tour
     * @return lista de precios especiales ordenados por fecha de inicio
     */
    List<TourPlanSpecialPriceResponse> findByTourPlanOrderByStartDateAsc(TourPlan tourPlan);

    /**
     * Obtiene precios especiales cuya fecha de inicio este dentro del rango indicado.
     *
     * @param startDate fecha de inicio del rango
     * @param endDate fecha de fin del rango
     * @return lista de precios especiales en el rango
     */
    List<TourPlanSpecialPriceResponse> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Detecta periodos de precio que se solapan con el rango dado para un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @param startDate fecha de inicio del periodo a verificar
     * @param endDate fecha de fin del periodo a verificar
     * @return lista de precios especiales que se solapan
     */
    List<TourPlanSpecialPriceResponse> findOverlappingPricePeriodsForTourPlan(Integer tourPlanId, LocalDate startDate, LocalDate endDate);

    /**
     * Obtiene precios especiales de un plan cuyo precio sea mayor al indicado.
     *
     * @param tourPlan entidad del plan de tour
     * @param price precio de referencia
     * @return lista de precios especiales superiores al precio indicado
     */
    List<TourPlanSpecialPriceResponse> findByTourPlanAndPriceGreaterThan(TourPlan tourPlan, BigDecimal price);

    /**
     * Obtiene precios especiales de un plan con fecha de inicio mayor o igual a la indicada.
     *
     * @param tourPlan entidad del plan de tour
     * @param date fecha minima de inicio
     * @return lista de precios especiales a partir de la fecha
     */
    List<TourPlanSpecialPriceResponse> findByTourPlanAndStartDateGreaterThanEqual(TourPlan tourPlan, LocalDate date);

    // Specialized queries

    /**
     * Obtiene precios especiales cuya fecha de fin este dentro del rango indicado.
     *
     * @param startDate fecha de inicio del rango
     * @param endDate fecha de fin del rango
     * @return lista de precios especiales cuya fecha de fin esta en el rango
     */
    List<TourPlanSpecialPriceResponse> findByEndDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Busca el precio especial vigente para un plan de tour en una fecha especifica.
     *
     * @param tourPlanId identificador del plan de tour
     * @param date fecha a consultar
     * @return el precio especial vigente o vacio si no existe
     */
    Optional<TourPlanSpecialPriceResponse> findByTourPlanIdAndDate(Integer tourPlanId, LocalDate date);

    // Bulk operations

    /**
     * Elimina todos los precios especiales de un plan de tour.
     *
     * @param tourPlan entidad del plan de tour
     */
    void deleteByTourPlan(TourPlan tourPlan);

    // Check operations

    /**
     * Verifica si existe un precio especial para un plan con las fechas de inicio y fin dadas.
     *
     * @param tourPlan entidad del plan de tour
     * @param startDate fecha de inicio del periodo
     * @param endDate fecha de fin del periodo
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsByTourPlanAndStartDateAndEndDate(TourPlan tourPlan, LocalDate startDate, LocalDate endDate);
}