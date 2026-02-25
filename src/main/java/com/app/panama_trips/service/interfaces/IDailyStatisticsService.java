package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.DailyStatisticsRequest;
import com.app.panama_trips.presentation.dto.DailyStatisticsResponse;
import java.time.LocalDate;
import java.util.List;

/**
 * Contrato de servicio para la gestion de estadisticas diarias.
 * Provee operaciones CRUD y consulta por rango de fechas para
 * el seguimiento de metricas del sistema.
 */
public interface IDailyStatisticsService {

    /**
     * Obtiene todas las estadisticas diarias registradas.
     *
     * @return lista de todas las estadisticas diarias
     */
    List<DailyStatisticsResponse> getAllDailyStatistics();

    /**
     * Obtiene una estadistica diaria por su identificador.
     *
     * @param id identificador de la estadistica
     * @return la estadistica encontrada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    DailyStatisticsResponse getDailyStatisticsById(Long id);

    /**
     * Obtiene estadisticas diarias dentro de un rango de fechas.
     *
     * @param dateA fecha de inicio del rango
     * @param dateB fecha de fin del rango
     * @return lista de estadisticas dentro del rango
     */
    List<DailyStatisticsResponse> getDailyStatisticsByDate(LocalDate dateA, LocalDate dateB);

    /**
     * Guarda un nuevo registro de estadisticas diarias.
     *
     * @param dailyStatisticsRequest datos de la estadistica a guardar
     * @return la estadistica persistida
     */
    DailyStatisticsResponse saveDailyStatistics(DailyStatisticsRequest dailyStatisticsRequest);

    /**
     * Actualiza un registro de estadisticas diarias existente.
     *
     * @param id identificador de la estadistica a actualizar
     * @param dailyStatisticsRequest datos actualizados de la estadistica
     * @return la estadistica actualizada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    DailyStatisticsResponse updateDailyStatistics(Long id, DailyStatisticsRequest dailyStatisticsRequest);

    /**
     * Elimina un registro de estadisticas diarias por su identificador.
     *
     * @param id identificador de la estadistica a eliminar
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    void deleteDailyStatistics(Long id);
}