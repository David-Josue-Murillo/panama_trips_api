package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.TourPlanRequest;
import com.app.panama_trips.presentation.dto.TourPlanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Contrato de servicio para la gestion de planes de tour.
 * Provee operaciones CRUD y busquedas avanzadas por precio, duracion, cupos y proveedor.
 */
public interface ITourPlanService {

    // CRUD operations

    /**
     * Obtiene todos los planes de tour de forma paginada.
     *
     * @param pageable configuracion de paginacion
     * @return pagina de planes de tour
     */
    Page<TourPlanResponse> getAllTourPlan(Pageable pageable);

    /**
     * Obtiene un plan de tour por su identificador.
     *
     * @param id identificador del plan de tour
     * @return el plan de tour encontrado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el plan no existe
     */
    TourPlanResponse getTourPlanById(Integer id);

    /**
     * Crea un nuevo plan de tour.
     *
     * @param tourPlanRequest datos del plan de tour a crear
     * @return el plan de tour creado
     */
    TourPlanResponse saveTourPlan(TourPlanRequest tourPlanRequest);

    /**
     * Actualiza un plan de tour existente.
     *
     * @param id identificador del plan de tour
     * @param tourPlanRequest datos actualizados del plan
     * @return el plan de tour actualizado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el plan no existe
     */
    TourPlanResponse updateTourPlan(Integer id, TourPlanRequest tourPlanRequest);

    /**
     * Elimina un plan de tour por su identificador.
     *
     * @param id identificador del plan de tour
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el plan no existe
     */
    void deleteTourPlan(Integer id);

    // Additional service methods

    /**
     * Busca un plan de tour por su titulo exacto.
     *
     * @param title titulo del plan de tour
     * @return el plan de tour encontrado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    TourPlanResponse getTourPlanByTitle(String title);

    /**
     * Obtiene planes de tour con un precio especifico.
     *
     * @param price precio exacto a buscar
     * @return lista de planes con el precio indicado
     */
    List<TourPlanResponse> getTourPlanByPrice(BigDecimal price);

    /**
     * Obtiene planes de tour con precio en un rango, de forma paginada.
     *
     * @param minPrice precio minimo
     * @param maxPrice precio maximo
     * @param pageable configuracion de paginacion
     * @return pagina de planes dentro del rango de precio
     */
    Page<TourPlanResponse> getTourPlanByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    /**
     * Obtiene planes de tour con una duracion especifica.
     *
     * @param duration duracion exacta en dias
     * @return lista de planes con la duracion indicada
     */
    List<TourPlanResponse> getTourPlanByDuration(Integer duration);

    /**
     * Obtiene planes de tour con duracion en un rango, de forma paginada.
     *
     * @param minDuration duracion minima
     * @param maxDuration duracion maxima
     * @param pageable configuracion de paginacion
     * @return pagina de planes dentro del rango de duracion
     */
    Page<TourPlanResponse> getTourPlanByDurationBetween(Integer minDuration, Integer maxDuration, Pageable pageable);

    /**
     * Obtiene planes de tour con un numero especifico de cupos disponibles.
     *
     * @param availableSpots numero exacto de cupos disponibles
     * @return lista de planes con los cupos indicados
     */
    List<TourPlanResponse> getTourPlanByAvailableSpots(Integer availableSpots);

    /**
     * Obtiene planes de tour con cupos disponibles en un rango, de forma paginada.
     *
     * @param minSpots cupos minimos
     * @param maxSpots cupos maximos
     * @param pageable configuracion de paginacion
     * @return pagina de planes dentro del rango de cupos
     */
    Page<TourPlanResponse> getTourPlanByAvailableSpotsBetween(Integer minSpots, Integer maxSpots, Pageable pageable);

    /**
     * Obtiene planes de tour de un proveedor especifico.
     *
     * @param providerId identificador del proveedor
     * @return lista de planes del proveedor
     */
    List<TourPlanResponse> getTourPlanByProviderId(Integer providerId);

    /**
     * Busca planes de tour por titulo y precio exactos.
     *
     * @param title titulo del plan
     * @param price precio del plan
     * @return lista de planes que coinciden
     */
    List<TourPlanResponse> getTourPlanByTitleAndPrice(String title, BigDecimal price);

    /**
     * Busca planes de tour por titulo con precio en un rango, de forma paginada.
     *
     * @param title titulo del plan
     * @param minPrice precio minimo
     * @param maxPrice precio maximo
     * @param pageable configuracion de paginacion
     * @return pagina de planes que coinciden
     */
    Page<TourPlanResponse> getTourPlanByTitleAndPriceBetween(String title, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    /**
     * Busca planes por titulo, rango de precio y rango de duracion, de forma paginada.
     *
     * @param title titulo del plan
     * @param minPrice precio minimo
     * @param maxPrice precio maximo
     * @param minDuration duracion minima
     * @param maxDuration duracion maxima
     * @param pageable configuracion de paginacion
     * @return pagina de planes que coinciden con todos los criterios
     */
    Page<TourPlanResponse> getTourPlanByTitleAndPriceBetweenAndDurationBetween(String title, BigDecimal minPrice, BigDecimal maxPrice, Integer minDuration, Integer maxDuration, Pageable pageable);

    /**
     * Obtiene los 10 primeros planes de tour cuyo titulo contenga la palabra clave.
     *
     * @param keyword palabra clave a buscar en el titulo
     * @param pageable configuracion de paginacion
     * @return lista de hasta 10 planes que coinciden
     */
    List<TourPlanResponse> getTop10TourPlanByTitleContaining(String keyword, Pageable pageable);

    /**
     * Verifica si existe un plan de tour con el titulo dado.
     *
     * @param title titulo a verificar
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsTourPlanByTitle(String title);

    /**
     * Cuenta el numero total de planes de tour registrados.
     *
     * @return cantidad total de planes de tour
     */
    long countTourPlan();
}
