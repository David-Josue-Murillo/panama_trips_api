package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.Guide;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.presentation.dto.TourAssignmentRequest;
import com.app.panama_trips.presentation.dto.TourAssignmentResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Contrato de servicio para la gestion de asignaciones de tours a guias.
 * Define operaciones CRUD, consultas de negocio, operaciones avanzadas y gestion de estados.
 */
public interface ITourAssignmentService {

    // Basic CRUD operations

    /**
     * Obtiene todas las asignaciones de forma paginada.
     *
     * @param pageable informacion de paginacion
     * @return pagina de asignaciones
     */
    Page<TourAssignmentResponse> getAllAssignments(Pageable pageable);

    /**
     * Obtiene una asignacion por su identificador.
     *
     * @param id identificador de la asignacion
     * @return la asignacion encontrada o vacio si no existe
     */
    Optional<TourAssignmentResponse> getAssignmentById(Integer id);

    /**
     * Crea una nueva asignacion de tour.
     *
     * @param request datos de la asignacion a crear
     * @return la asignacion creada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si el guia o plan de tour no existe
     */
    TourAssignmentResponse createAssignment(TourAssignmentRequest request);

    /**
     * Actualiza una asignacion existente.
     *
     * @param id identificador de la asignacion a actualizar
     * @param request nuevos datos de la asignacion
     * @return la asignacion actualizada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la asignacion no existe
     */
    TourAssignmentResponse updateAssignment(Integer id, TourAssignmentRequest request);

    /**
     * Elimina una asignacion por su identificador.
     *
     * @param id identificador de la asignacion a eliminar
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la asignacion no existe
     */
    void deleteAssignment(Integer id);

    // Business operations

    /**
     * Obtiene todas las asignaciones de un guia especifico.
     *
     * @param guide entidad del guia
     * @return lista de asignaciones del guia
     */
    List<TourAssignmentResponse> getAssignmentsByGuide(Guide guide);

    /**
     * Obtiene todas las asignaciones de un plan de tour especifico.
     *
     * @param tourPlan entidad del plan de tour
     * @return lista de asignaciones del plan de tour
     */
    List<TourAssignmentResponse> getAssignmentsByTourPlan(TourPlan tourPlan);

    /**
     * Obtiene asignaciones filtradas por estado.
     *
     * @param status estado de la asignacion
     * @return lista de asignaciones con el estado indicado
     */
    List<TourAssignmentResponse> getAssignmentsByStatus(String status);

    /**
     * Obtiene asignaciones para una fecha especifica.
     *
     * @param date fecha de la asignacion
     * @return lista de asignaciones en la fecha indicada
     */
    List<TourAssignmentResponse> getAssignmentsByDate(LocalDate date);

    /**
     * Obtiene asignaciones dentro de un rango de fechas.
     *
     * @param startDate fecha de inicio del rango
     * @param endDate fecha de fin del rango
     * @return lista de asignaciones en el rango
     */
    List<TourAssignmentResponse> getAssignmentsByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Obtiene asignaciones de un guia filtradas por estado.
     *
     * @param guide entidad del guia
     * @param status estado de la asignacion
     * @return lista de asignaciones que coinciden con el guia y estado
     */
    List<TourAssignmentResponse> getAssignmentsByGuideAndStatus(Guide guide, String status);

    /**
     * Obtiene asignaciones de un plan de tour dentro de un rango de fechas.
     *
     * @param tourPlan entidad del plan de tour
     * @param startDate fecha de inicio del rango
     * @param endDate fecha de fin del rango
     * @return lista de asignaciones que coinciden
     */
    List<TourAssignmentResponse> getAssignmentsByTourPlanAndDateRange(TourPlan tourPlan, LocalDate startDate, LocalDate endDate);

    /**
     * Obtiene una asignacion especifica por guia, plan de tour y fecha.
     *
     * @param guide entidad del guia
     * @param tourPlan entidad del plan de tour
     * @param date fecha de la asignacion
     * @return la asignacion encontrada o vacio si no existe
     */
    Optional<TourAssignmentResponse> getAssignmentByGuideTourPlanAndDate(Guide guide, TourPlan tourPlan, LocalDate date);

    // Advanced operations

    /**
     * Obtiene las proximas asignaciones de un guia a partir de una fecha.
     *
     * @param guideId identificador del guia
     * @param startDate fecha a partir de la cual buscar
     * @return lista de asignaciones futuras del guia
     */
    List<TourAssignmentResponse> getUpcomingAssignmentsByGuide(Integer guideId, LocalDate startDate);

    /**
     * Cuenta las asignaciones de un guia por estado.
     *
     * @param guideId identificador del guia
     * @param status estado de la asignacion
     * @return cantidad de asignaciones
     */
    Long countAssignmentsByGuideAndStatus(Integer guideId, String status);

    /**
     * Verifica si un guia esta disponible para una fecha determinada.
     *
     * @param guide entidad del guia
     * @param date fecha a verificar
     * @return {@code true} si el guia esta disponible, {@code false} en caso contrario
     */
    boolean isGuideAvailableForDate(Guide guide, LocalDate date);

    // Status management

    /**
     * Actualiza el estado de una asignacion.
     *
     * @param assignmentId identificador de la asignacion
     * @param newStatus nuevo estado a asignar
     * @return la asignacion con el estado actualizado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la asignacion no existe
     */
    TourAssignmentResponse updateAssignmentStatus(Integer assignmentId, String newStatus);

    /**
     * Agrega notas a una asignacion existente.
     *
     * @param assignmentId identificador de la asignacion
     * @param notes notas a agregar
     * @return la asignacion con las notas actualizadas
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si la asignacion no existe
     */
    TourAssignmentResponse addNotesToAssignment(Integer assignmentId, String notes);
}
