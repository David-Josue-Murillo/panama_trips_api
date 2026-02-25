package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.TourPlanAvailabilityRequest;
import com.app.panama_trips.presentation.dto.TourPlanAvailabilityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Contrato de servicio para la gestion de disponibilidad de planes de tour.
 * Provee operaciones CRUD, consultas por rango de fechas, verificacion de cupos y precios sobreescritos.
 */
public interface ITourPlanAvailabilityService {

    // CRUD operations

    /**
     * Obtiene todas las disponibilidades de forma paginada.
     *
     * @param pageable configuracion de paginacion
     * @return pagina de disponibilidades
     */
    Page<TourPlanAvailabilityResponse> getAllTourPlanAvailabilities(Pageable pageable);

    /**
     * Obtiene una disponibilidad por su identificador.
     *
     * @param id identificador de la disponibilidad
     * @return la disponibilidad encontrada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    TourPlanAvailabilityResponse getTourPlanAvailabilityById(Integer id);

    /**
     * Crea una nueva disponibilidad para un plan de tour.
     *
     * @param request datos de la disponibilidad a crear
     * @return la disponibilidad creada
     */
    TourPlanAvailabilityResponse saveTourPlanAvailability(TourPlanAvailabilityRequest request);

    /**
     * Actualiza una disponibilidad existente.
     *
     * @param id identificador de la disponibilidad
     * @param request datos actualizados de la disponibilidad
     * @return la disponibilidad actualizada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    TourPlanAvailabilityResponse updateTourPlanAvailability(Integer id, TourPlanAvailabilityRequest request);

    /**
     * Elimina una disponibilidad por su identificador.
     *
     * @param id identificador de la disponibilidad
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    void deleteTourPlanAvailability(Integer id);

    // Find operations

    /**
     * Obtiene todas las disponibilidades de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return lista de disponibilidades del plan
     */
    List<TourPlanAvailabilityResponse> getTourPlanAvailabilitiesByTourPlanId(Integer tourPlanId);

    /**
     * Obtiene las fechas disponibles (con cupos) de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return lista de disponibilidades con cupos abiertos
     */
    List<TourPlanAvailabilityResponse> getAvailableDatesByTourPlanId(Integer tourPlanId);

    /**
     * Obtiene disponibilidades dentro de un rango de fechas.
     *
     * @param startDate fecha de inicio del rango
     * @param endDate fecha de fin del rango
     * @return lista de disponibilidades en el rango
     */
    List<TourPlanAvailabilityResponse> getAvailabilitiesByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Obtiene disponibilidades de un plan de tour dentro de un rango de fechas.
     *
     * @param tourPlanId identificador del plan de tour
     * @param startDate fecha de inicio del rango
     * @param endDate fecha de fin del rango
     * @return lista de disponibilidades que coinciden
     */
    List<TourPlanAvailabilityResponse> getAvailabilitiesByTourPlanIdAndDateRange(Integer tourPlanId, LocalDate startDate, LocalDate endDate);

    /**
     * Obtiene la disponibilidad de un plan de tour en una fecha especifica.
     *
     * @param tourPlanId identificador del plan de tour
     * @param date fecha a consultar
     * @return la disponibilidad encontrada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no existe
     */
    TourPlanAvailabilityResponse getAvailabilityByTourPlanIdAndDate(Integer tourPlanId, LocalDate date);

    // Specialized queries

    /**
     * Obtiene las fechas disponibles futuras de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return lista de disponibilidades futuras
     */
    List<TourPlanAvailabilityResponse> getUpcomingAvailableDatesByTourPlanId(Integer tourPlanId);

    /**
     * Obtiene fechas disponibles con cupos suficientes para la cantidad requerida.
     *
     * @param tourPlanId identificador del plan de tour
     * @param requiredSpots numero de cupos requeridos
     * @return lista de disponibilidades con cupos suficientes
     */
    List<TourPlanAvailabilityResponse> getAvailableDatesWithSufficientSpots(Integer tourPlanId, Integer requiredSpots);

    /**
     * Cuenta las fechas disponibles futuras de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return cantidad de fechas disponibles futuras
     */
    Long countUpcomingAvailableDatesByTourPlanId(Integer tourPlanId);

    /**
     * Obtiene disponibilidades que tienen un precio sobreescrito.
     *
     * @param tourPlanId identificador del plan de tour
     * @return lista de disponibilidades con precio sobreescrito
     */
    List<TourPlanAvailabilityResponse> getAvailabilitiesWithPriceOverride(Integer tourPlanId);

    /**
     * Obtiene disponibilidades con precio sobreescrito superior al indicado.
     *
     * @param tourPlanId identificador del plan de tour
     * @param price precio minimo de referencia
     * @return lista de disponibilidades con precio superior
     */
    List<TourPlanAvailabilityResponse> getAvailabilitiesWithPriceAbove(Integer tourPlanId, BigDecimal price);

    // Bulk operations

    /**
     * Elimina todas las disponibilidades de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     */
    void deleteAllAvailabilitiesByTourPlanId(Integer tourPlanId);

    // Check operations

    /**
     * Verifica si existe una disponibilidad para un plan de tour en una fecha especifica.
     *
     * @param tourPlanId identificador del plan de tour
     * @param date fecha a verificar
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsAvailabilityForTourPlanAndDate(Integer tourPlanId, LocalDate date);
}
