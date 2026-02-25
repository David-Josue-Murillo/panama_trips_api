package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.NotificationHistoryRequest;
import com.app.panama_trips.presentation.dto.NotificationHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de servicio para la gestion del historial de notificaciones.
 * Provee operaciones CRUD, consultas por usuario/reserva/canal, operaciones
 * en lote, estadisticas de entrega y utilidades de mantenimiento.
 */
public interface INotificationHistoryService {

    // ==================== CRUD operations ====================

    /**
     * Obtiene todo el historial de notificaciones de forma paginada.
     *
     * @param pageable parametros de paginacion
     * @return pagina de notificaciones
     */
    Page<NotificationHistoryResponse> getAllNotificationHistory(Pageable pageable);

    /**
     * Obtiene una notificacion del historial por su identificador.
     *
     * @param id identificador de la notificacion
     * @return la notificacion encontrada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    NotificationHistoryResponse getNotificationHistoryById(Integer id);

    /**
     * Guarda un nuevo registro en el historial de notificaciones.
     *
     * @param request datos de la notificacion a guardar
     * @return la notificacion persistida
     */
    NotificationHistoryResponse saveNotificationHistory(NotificationHistoryRequest request);

    /**
     * Actualiza un registro existente en el historial de notificaciones.
     *
     * @param id identificador de la notificacion a actualizar
     * @param request datos actualizados de la notificacion
     * @return la notificacion actualizada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    NotificationHistoryResponse updateNotificationHistory(Integer id, NotificationHistoryRequest request);

    /**
     * Elimina un registro del historial de notificaciones.
     *
     * @param id identificador de la notificacion a eliminar
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    void deleteNotificationHistory(Integer id);

    // ==================== Find operations ====================

    /**
     * Busca notificaciones por identificador de usuario.
     *
     * @param userId identificador del usuario
     * @return lista de notificaciones del usuario
     */
    List<NotificationHistoryResponse> findByUserId(Long userId);

    /**
     * Busca notificaciones por identificador de plantilla.
     *
     * @param templateId identificador de la plantilla
     * @return lista de notificaciones que usan la plantilla
     */
    List<NotificationHistoryResponse> findByTemplateId(Integer templateId);

    /**
     * Busca notificaciones por identificador de reserva.
     *
     * @param reservationId identificador de la reserva
     * @return lista de notificaciones asociadas a la reserva
     */
    List<NotificationHistoryResponse> findByReservationId(Integer reservationId);

    /**
     * Busca notificaciones por estado de entrega.
     *
     * @param deliveryStatus estado de entrega a filtrar
     * @return lista de notificaciones con el estado indicado
     */
    List<NotificationHistoryResponse> findByDeliveryStatus(String deliveryStatus);

    /**
     * Busca notificaciones por canal de envio.
     *
     * @param channel canal de notificacion (email, sms, etc.)
     * @return lista de notificaciones enviadas por el canal
     */
    List<NotificationHistoryResponse> findByChannel(String channel);

    /**
     * Busca notificaciones dentro de un rango de fechas.
     *
     * @param startDate fecha y hora de inicio
     * @param endDate fecha y hora de fin
     * @return lista de notificaciones dentro del rango
     */
    List<NotificationHistoryResponse> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Busca notificaciones de un usuario dentro de un rango de fechas.
     *
     * @param startDate fecha y hora de inicio
     * @param endDate fecha y hora de fin
     * @param userId identificador del usuario
     * @return lista de notificaciones que coinciden
     */
    List<NotificationHistoryResponse> findByDateRangeAndUser(LocalDateTime startDate, LocalDateTime endDate, Long userId);

    // ==================== Specialized queries ====================

    /**
     * Obtiene todas las notificaciones con estado de entrega fallido.
     *
     * @return lista de notificaciones fallidas
     */
    List<NotificationHistoryResponse> getFailedNotifications();

    /**
     * Obtiene todas las notificaciones pendientes de envio.
     *
     * @return lista de notificaciones pendientes
     */
    List<NotificationHistoryResponse> getPendingNotifications();

    /**
     * Obtiene todas las notificaciones entregadas exitosamente.
     *
     * @return lista de notificaciones entregadas
     */
    List<NotificationHistoryResponse> getDeliveredNotifications();

    /**
     * Obtiene notificaciones de un usuario dentro de un rango de fechas (por dia).
     *
     * @param userId identificador del usuario
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de notificaciones que coinciden
     */
    List<NotificationHistoryResponse> getNotificationsByUserAndDateRange(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * Obtiene notificaciones por reserva y canal de envio.
     *
     * @param reservationId identificador de la reserva
     * @param channel canal de notificacion
     * @return lista de notificaciones que coinciden
     */
    List<NotificationHistoryResponse> getNotificationsByReservationAndChannel(Integer reservationId, String channel);

    /**
     * Obtiene las notificaciones mas recientes limitadas por cantidad.
     *
     * @param limit cantidad maxima de resultados
     * @return lista de las notificaciones mas recientes
     */
    List<NotificationHistoryResponse> getRecentNotifications(int limit);

    // ==================== Bulk operations ====================

    /**
     * Crea multiples registros de notificacion en lote.
     *
     * @param requests lista de solicitudes de notificacion a crear
     */
    void bulkCreateNotificationHistory(List<NotificationHistoryRequest> requests);

    /**
     * Elimina multiples registros de notificacion por sus identificadores.
     *
     * @param notificationIds lista de identificadores a eliminar
     */
    void bulkDeleteNotificationHistory(List<Integer> notificationIds);

    /**
     * Actualiza el estado de entrega de multiples notificaciones.
     *
     * @param notificationIds lista de identificadores de notificaciones
     * @param newStatus nuevo estado de entrega
     */
    void bulkUpdateDeliveryStatus(List<Integer> notificationIds, String newStatus);

    // ==================== Check operations ====================

    /**
     * Verifica si existe una notificacion por su identificador.
     *
     * @param id identificador de la notificacion
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsById(Integer id);

    /**
     * Verifica si existe una notificacion para un usuario y plantilla especificos.
     *
     * @param userId identificador del usuario
     * @param templateId identificador de la plantilla
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsByUserIdAndTemplateId(Long userId, Integer templateId);

    /**
     * Cuenta las notificaciones de un usuario.
     *
     * @param userId identificador del usuario
     * @return cantidad de notificaciones
     */
    long countByUserId(Long userId);

    /**
     * Cuenta las notificaciones por estado de entrega.
     *
     * @param deliveryStatus estado de entrega
     * @return cantidad de notificaciones
     */
    long countByDeliveryStatus(String deliveryStatus);

    /**
     * Cuenta las notificaciones por canal de envio.
     *
     * @param channel canal de notificacion
     * @return cantidad de notificaciones
     */
    long countByChannel(String channel);

    /**
     * Cuenta las notificaciones dentro de un rango de fechas.
     *
     * @param startDate fecha y hora de inicio
     * @param endDate fecha y hora de fin
     * @return cantidad de notificaciones
     */
    long countByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    // ==================== Statistics and analytics ====================

    /**
     * Obtiene el total de notificaciones enviadas.
     *
     * @return cantidad total de notificaciones enviadas
     */
    long getTotalNotificationsSent();

    /**
     * Obtiene el total de notificaciones entregadas exitosamente.
     *
     * @return cantidad total de notificaciones entregadas
     */
    long getTotalNotificationsDelivered();

    /**
     * Obtiene el total de notificaciones con entrega fallida.
     *
     * @return cantidad total de notificaciones fallidas
     */
    long getTotalNotificationsFailed();

    /**
     * Calcula la tasa de exito de entrega de notificaciones.
     *
     * @return porcentaje de exito de entrega (0.0 a 100.0)
     */
    double getDeliverySuccessRate();

    // ==================== Utility operations ====================

    /**
     * Marca una notificacion como entregada.
     *
     * @param notificationId identificador de la notificacion
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    void markAsDelivered(Integer notificationId);

    /**
     * Marca una notificacion como fallida con su razon de fallo.
     *
     * @param notificationId identificador de la notificacion
     * @param failureReason razon del fallo de entrega
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    void markAsFailed(Integer notificationId, String failureReason);

    /**
     * Reintenta el envio de todas las notificaciones fallidas.
     */
    void retryFailedNotifications();

    /**
     * Elimina notificaciones antiguas segun la cantidad de dias a conservar.
     *
     * @param daysToKeep cantidad de dias a mantener
     */
    void cleanupOldNotifications(int daysToKeep);

    /**
     * Busca notificaciones por contenido usando una palabra clave.
     *
     * @param keyword palabra clave a buscar en el contenido
     * @return lista de notificaciones que contienen la palabra clave
     */
    List<NotificationHistoryResponse> searchNotificationsByContent(String keyword);

    /**
     * Busca la notificacion mas reciente de un usuario.
     *
     * @param userId identificador del usuario
     * @return la notificacion mas reciente, o vacio si no existe
     */
    Optional<NotificationHistoryResponse> findLatestNotificationByUser(Long userId);
}
