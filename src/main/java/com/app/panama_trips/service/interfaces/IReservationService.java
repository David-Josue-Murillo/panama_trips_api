package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.ReservationStatus;
import com.app.panama_trips.presentation.dto.ReservationRequest;
import com.app.panama_trips.presentation.dto.ReservationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Contrato de servicio para la gestion de reservaciones de tours.
 * Define operaciones CRUD, busquedas por usuario/tour/fecha/precio, estadisticas y cambios de estado.
 */
public interface IReservationService {
    // CRUD operations

    /**
     * Obtiene todas las reservaciones de forma paginada.
     *
     * @param pageable informacion de paginacion
     * @return pagina de reservaciones
     */
    Page<ReservationResponse> getAllReservations(Pageable pageable);

    /**
     * Obtiene una reservacion por su identificador.
     *
     * @param id identificador de la reservacion
     * @return la reservacion encontrada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la reservacion no existe
     */
    ReservationResponse getReservationById(Integer id);

    /**
     * Guarda una nueva reservacion.
     *
     * @param reservationRequest datos de la reservacion a crear
     * @return la reservacion creada
     */
    ReservationResponse saveReservation(ReservationRequest reservationRequest);

    /**
     * Actualiza el estado de una reservacion.
     *
     * @param id identificador de la reservacion
     * @param status nuevo estado
     * @param username nombre de usuario que realiza el cambio
     * @return la reservacion con el estado actualizado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la reservacion no existe
     */
    ReservationResponse updateStatusReservation(Integer id, String status, String username);

    /**
     * Elimina una reservacion por su identificador.
     *
     * @param id identificador de la reservacion a eliminar
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la reservacion no existe
     */
    void deleteReservation(Integer id);

    // Additional service methods

    /**
     * Obtiene las reservaciones de un usuario de forma paginada.
     *
     * @param userId identificador del usuario
     * @param pageable informacion de paginacion
     * @return pagina de reservaciones del usuario
     */
    Page<ReservationResponse> getReservationByUserId(Long userId, Pageable pageable);

    /**
     * Obtiene las reservaciones de un plan de tour de forma paginada.
     *
     * @param tourPlanId identificador del plan de tour
     * @param pageable informacion de paginacion
     * @return pagina de reservaciones del plan de tour
     */
    Page<ReservationResponse> getReservationByTourPlanId(Integer tourPlanId, Pageable pageable);

    /**
     * Obtiene reservaciones filtradas por estado de forma paginada.
     *
     * @param reservationStatus estado de la reservacion
     * @param pageable informacion de paginacion
     * @return pagina de reservaciones con el estado indicado
     */
    Page<ReservationResponse> getReservationByReservationStatus(String reservationStatus, Pageable pageable);

    /**
     * Obtiene reservaciones filtradas por fecha de reservacion de forma paginada.
     *
     * @param reservationDate fecha de reservacion en formato String
     * @param pageable informacion de paginacion
     * @return pagina de reservaciones en la fecha indicada
     */
    Page<ReservationResponse> getReservationByReservationDate(String reservationDate, Pageable pageable);

    // Specific searches

    /**
     * Obtiene reservaciones de un usuario filtradas por estado de forma paginada.
     *
     * @param userId identificador del usuario
     * @param status estado de la reservacion
     * @param pageable informacion de paginacion
     * @return pagina de reservaciones que coinciden
     */
    Page<ReservationResponse> getReservationsByUserAndStatus(Long userId, String status, Pageable pageable);

    /**
     * Obtiene reservaciones de un plan de tour filtradas por estado de forma paginada.
     *
     * @param tourPlanId identificador del plan de tour
     * @param status estado de la reservacion
     * @param pageable informacion de paginacion
     * @return pagina de reservaciones que coinciden
     */
    Page<ReservationResponse> getReservationsByTourPlanAndStatus(Integer tourPlanId, String status, Pageable pageable);

    // Searches by dates

    /**
     * Obtiene reservaciones dentro de un rango de fechas de forma paginada.
     *
     * @param startDate fecha de inicio del rango
     * @param endDate fecha de fin del rango
     * @param pageable informacion de paginacion
     * @return pagina de reservaciones en el rango
     */
    Page<ReservationResponse> getReservationsBetweenDates(LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * Obtiene reservaciones de un mes especifico de forma paginada.
     *
     * @param month numero del mes (1-12)
     * @param pageable informacion de paginacion
     * @return pagina de reservaciones del mes
     */
    Page<ReservationResponse> getReservationsByMonth(short month, Pageable pageable);

    /**
     * Obtiene reservaciones de un anio especifico de forma paginada.
     *
     * @param year anio a consultar
     * @param pageable informacion de paginacion
     * @return pagina de reservaciones del anio
     */
    Page<ReservationResponse> getReservationsByYear(int year, Pageable pageable);

    // Searchers by price

    /**
     * Obtiene reservaciones cuyo precio total es mayor al valor proporcionado.
     *
     * @param price precio de referencia
     * @param pageable informacion de paginacion
     * @return pagina de reservaciones con precio mayor
     */
    Page<ReservationResponse> getReservationsWithPriceGreaterThan(BigDecimal price, Pageable pageable);

    /**
     * Obtiene reservaciones dentro de un rango de precios de forma paginada.
     *
     * @param minPrice precio minimo
     * @param maxPrice precio maximo
     * @param pageable informacion de paginacion
     * @return pagina de reservaciones en el rango de precios
     */
    Page<ReservationResponse> getReservationsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    // Special searches

    /**
     * Obtiene las reservaciones recientes de un usuario a partir de una fecha de forma paginada.
     *
     * @param userId identificador del usuario
     * @param recentDate fecha a partir de la cual buscar
     * @param pageable informacion de paginacion
     * @return pagina de reservaciones recientes del usuario
     */
    Page<ReservationResponse> getRecentReservationsByUser(Long userId, LocalDate recentDate, Pageable pageable);

    /**
     * Obtiene reservaciones realizadas en un dia de la semana especifico de forma paginada.
     *
     * @param dayOfWeek dia de la semana (1=lunes, 7=domingo)
     * @param pageable informacion de paginacion
     * @return pagina de reservaciones del dia indicado
     */
    Page<ReservationResponse> getReservationsByDayOfWeek(int dayOfWeek, Pageable pageable);

    /**
     * Obtiene reservaciones asociadas a una provincia de forma paginada.
     *
     * @param provinceId identificador de la provincia
     * @param pageable informacion de paginacion
     * @return pagina de reservaciones de la provincia
     */
    Page<ReservationResponse> getReservationsByProvince(Integer provinceId, Pageable pageable);

    // Counting and statistical operations

    /**
     * Cuenta la cantidad de reservaciones por estado.
     *
     * @param status estado de la reservacion
     * @return cantidad de reservaciones con ese estado
     */
    Long countReservationsByStatus(ReservationStatus status);

    /**
     * Cuenta la cantidad de reservaciones de un plan de tour.
     *
     * @param tourPlanId identificador del plan de tour
     * @return cantidad de reservaciones
     */
    Long countReservationsByTourPlan(Integer tourPlanId);

    /**
     * Obtiene estadisticas generales de las reservaciones.
     *
     * @return arreglo con datos estadisticos de reservaciones
     */
    Object[] getReservationStatistics();

    // Status change

    /**
     * Cancela una reservacion.
     *
     * @param id identificador de la reservacion a cancelar
     * @return la reservacion cancelada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la reservacion no existe
     */
    ReservationResponse cancelReservation(Integer id);

    /**
     * Confirma una reservacion.
     *
     * @param id identificador de la reservacion a confirmar
     * @return la reservacion confirmada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la reservacion no existe
     */
    ReservationResponse confirmReservation(Integer id);
}
