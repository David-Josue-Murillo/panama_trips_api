package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.AuditLog;
import com.app.panama_trips.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de servicio para la gestion de registros de auditoria del sistema.
 * Provee operaciones CRUD, consultas especializadas, seguimiento de actividad,
 * monitoreo de seguridad, estadisticas y operaciones de integridad de datos.
 */
public interface IAuditLogService {

    // ==================== CRUD operations ====================

    /**
     * Obtiene todos los registros de auditoria de forma paginada.
     *
     * @param pageable parametros de paginacion
     * @return pagina de registros de auditoria
     */
    Page<AuditLog> getAllAuditLogs(Pageable pageable);

    /**
     * Obtiene un registro de auditoria por su identificador.
     *
     * @param id identificador del registro
     * @return el registro de auditoria encontrado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra el registro
     */
    AuditLog getAuditLogById(Integer id);

    /**
     * Guarda un nuevo registro de auditoria.
     *
     * @param auditLog entidad del registro a guardar
     * @return el registro de auditoria persistido
     */
    AuditLog saveAuditLog(AuditLog auditLog);

    /**
     * Actualiza un registro de auditoria existente.
     *
     * @param id identificador del registro a actualizar
     * @param auditLog entidad con los datos actualizados
     * @return el registro de auditoria actualizado
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra el registro
     */
    AuditLog updateAuditLog(Integer id, AuditLog auditLog);

    /**
     * Elimina un registro de auditoria por su identificador.
     *
     * @param id identificador del registro a eliminar
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra el registro
     */
    void deleteAuditLog(Integer id);

    // ==================== Find operations by entity relationships ====================

    /**
     * Busca registros de auditoria por tipo de entidad e identificador de entidad.
     *
     * @param entityType tipo de entidad auditada
     * @param entityId identificador de la entidad auditada
     * @return lista de registros que coinciden
     */
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, Integer entityId);

    /**
     * Busca registros de auditoria asociados a un usuario.
     *
     * @param user entidad del usuario
     * @return lista de registros del usuario
     */
    List<AuditLog> findByUser(UserEntity user);

    /**
     * Busca registros de auditoria por identificador de usuario.
     *
     * @param userId identificador del usuario
     * @return lista de registros del usuario
     */
    List<AuditLog> findByUserId(Integer userId);

    /**
     * Busca registros de auditoria por tipo de accion.
     *
     * @param action tipo de accion realizada
     * @return lista de registros con la accion indicada
     */
    List<AuditLog> findByAction(String action);

    /**
     * Busca registros de auditoria dentro de un rango de fechas.
     *
     * @param start fecha y hora de inicio del rango
     * @param end fecha y hora de fin del rango
     * @return lista de registros dentro del rango
     */
    List<AuditLog> findByActionTimestampBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Busca registros de auditoria por direccion IP.
     *
     * @param ipAddress direccion IP de origen
     * @return lista de registros desde esa IP
     */
    List<AuditLog> findByIpAddress(String ipAddress);

    // ==================== Specialized queries from repository ====================

    /**
     * Busca actividad reciente por tipo de entidad desde una fecha dada.
     *
     * @param entityType tipo de entidad
     * @param since fecha desde la cual buscar
     * @return lista de registros recientes para el tipo de entidad
     */
    List<AuditLog> findRecentActivityByEntityType(String entityType, LocalDateTime since);

    /**
     * Busca registros de auditoria por tipo de entidad.
     *
     * @param entityType tipo de entidad auditada
     * @return lista de registros del tipo indicado
     */
    List<AuditLog> findByEntityType(String entityType);

    /**
     * Busca registros de auditoria por identificador de entidad.
     *
     * @param entityId identificador de la entidad auditada
     * @return lista de registros para la entidad indicada
     */
    List<AuditLog> findByEntityId(Integer entityId);

    /**
     * Busca registros de auditoria por usuario y accion.
     *
     * @param user entidad del usuario
     * @param action tipo de accion realizada
     * @return lista de registros que coinciden
     */
    List<AuditLog> findByUserAndAction(UserEntity user, String action);

    /**
     * Busca registros de auditoria por usuario y tipo de entidad.
     *
     * @param user entidad del usuario
     * @param entityType tipo de entidad auditada
     * @return lista de registros que coinciden
     */
    List<AuditLog> findByUserAndEntityType(UserEntity user, String entityType);

    /**
     * Busca registros de auditoria por accion y tipo de entidad.
     *
     * @param action tipo de accion realizada
     * @param entityType tipo de entidad auditada
     * @return lista de registros que coinciden
     */
    List<AuditLog> findByActionAndEntityType(String action, String entityType);

    /**
     * Busca registros de auditoria posteriores a una fecha dada.
     *
     * @param timestamp fecha y hora limite inferior
     * @return lista de registros posteriores a la fecha
     */
    List<AuditLog> findByTimestampAfter(LocalDateTime timestamp);

    /**
     * Busca registros de auditoria anteriores a una fecha dada.
     *
     * @param timestamp fecha y hora limite superior
     * @return lista de registros anteriores a la fecha
     */
    List<AuditLog> findByTimestampBefore(LocalDateTime timestamp);

    // ==================== Business logic operations ====================

    /**
     * Obtiene la actividad mas reciente limitada por cantidad.
     *
     * @param limit cantidad maxima de registros a retornar
     * @return lista de los registros mas recientes
     */
    List<AuditLog> getRecentActivity(int limit);

    /**
     * Obtiene actividad dentro de un rango de fechas.
     *
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de registros dentro del rango
     */
    List<AuditLog> getActivityByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Obtiene actividad asociada a un usuario especifico.
     *
     * @param userId identificador del usuario
     * @return lista de registros del usuario
     */
    List<AuditLog> getActivityByUser(Integer userId);

    /**
     * Obtiene actividad filtrada por tipo de entidad.
     *
     * @param entityType tipo de entidad
     * @return lista de registros del tipo de entidad
     */
    List<AuditLog> getActivityByEntityType(String entityType);

    /**
     * Obtiene actividad filtrada por tipo de accion.
     *
     * @param action tipo de accion
     * @return lista de registros con la accion indicada
     */
    List<AuditLog> getActivityByAction(String action);

    /**
     * Obtiene actividad filtrada por direccion IP.
     *
     * @param ipAddress direccion IP
     * @return lista de registros desde esa IP
     */
    List<AuditLog> getActivityByIpAddress(String ipAddress);

    /**
     * Obtiene actividad de un usuario dentro de un rango de fechas.
     *
     * @param userId identificador del usuario
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de registros que coinciden
     */
    List<AuditLog> getActivityByUserAndDateRange(Integer userId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Obtiene actividad de una entidad especifica dentro de un rango de fechas.
     *
     * @param entityType tipo de entidad
     * @param entityId identificador de la entidad
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de registros que coinciden
     */
    List<AuditLog> getActivityByEntityAndDateRange(String entityType, Integer entityId, LocalDateTime startDate, LocalDateTime endDate);

    // ==================== Advanced queries ====================

    /**
     * Obtiene actividad reciente por tipo de entidad limitada por cantidad.
     *
     * @param entityType tipo de entidad
     * @param limit cantidad maxima de registros
     * @return lista de los registros mas recientes del tipo indicado
     */
    List<AuditLog> getRecentActivityByEntityType(String entityType, int limit);

    /**
     * Obtiene actividad filtrada por user agent exacto.
     *
     * @param userAgent cadena del user agent
     * @return lista de registros con ese user agent
     */
    List<AuditLog> getActivityByUserAgent(String userAgent);

    /**
     * Obtiene actividad donde el user agent contiene un patron.
     *
     * @param userAgentPattern patron a buscar en el user agent
     * @return lista de registros que coinciden
     */
    List<AuditLog> getActivityByUserAgentContaining(String userAgentPattern);

    /**
     * Obtiene actividad por IP dentro de un rango de fechas.
     *
     * @param ipAddress direccion IP
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de registros que coinciden
     */
    List<AuditLog> getActivityByIpAddressAndDateRange(String ipAddress, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Obtiene actividad por accion dentro de un rango de fechas.
     *
     * @param action tipo de accion
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de registros que coinciden
     */
    List<AuditLog> getActivityByActionAndDateRange(String action, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Obtiene actividad por tipo de entidad y accion.
     *
     * @param entityType tipo de entidad
     * @param action tipo de accion
     * @return lista de registros que coinciden
     */
    List<AuditLog> getActivityByEntityTypeAndAction(String entityType, String action);

    /**
     * Obtiene actividad por usuario y accion.
     *
     * @param userId identificador del usuario
     * @param action tipo de accion
     * @return lista de registros que coinciden
     */
    List<AuditLog> getActivityByUserAndAction(Integer userId, String action);

    /**
     * Obtiene actividad por usuario y tipo de entidad.
     *
     * @param userId identificador del usuario
     * @param entityType tipo de entidad
     * @return lista de registros que coinciden
     */
    List<AuditLog> getActivityByUserAndEntityType(Integer userId, String entityType);

    // ==================== Bulk operations ====================

    /**
     * Crea multiples registros de auditoria en lote.
     *
     * @param auditLogs lista de registros a crear
     */
    void bulkCreateAuditLogs(List<AuditLog> auditLogs);

    /**
     * Elimina multiples registros de auditoria por sus identificadores.
     *
     * @param auditLogIds lista de identificadores a eliminar
     */
    void bulkDeleteAuditLogs(List<Integer> auditLogIds);

    /**
     * Elimina todos los registros de auditoria de un tipo de entidad.
     *
     * @param entityType tipo de entidad cuyos registros se eliminaran
     */
    void bulkDeleteAuditLogsByEntityType(String entityType);

    /**
     * Elimina todos los registros de auditoria de un usuario.
     *
     * @param userId identificador del usuario
     */
    void bulkDeleteAuditLogsByUser(Integer userId);

    /**
     * Elimina registros de auditoria dentro de un rango de fechas.
     *
     * @param startDate fecha de inicio del rango
     * @param endDate fecha de fin del rango
     */
    void bulkDeleteAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Elimina todos los registros de auditoria de una accion especifica.
     *
     * @param action tipo de accion cuyos registros se eliminaran
     */
    void bulkDeleteAuditLogsByAction(String action);

    // ==================== Check operations ====================

    /**
     * Verifica si existe un registro de auditoria por su identificador.
     *
     * @param id identificador del registro
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsById(Integer id);

    /**
     * Verifica si existen registros para un tipo de entidad e identificador.
     *
     * @param entityType tipo de entidad
     * @param entityId identificador de la entidad
     * @return {@code true} si existen registros, {@code false} en caso contrario
     */
    boolean existsByEntityTypeAndEntityId(String entityType, Integer entityId);

    /**
     * Verifica si existen registros de auditoria para un usuario.
     *
     * @param userId identificador del usuario
     * @return {@code true} si existen registros, {@code false} en caso contrario
     */
    boolean existsByUser(Integer userId);

    /**
     * Verifica si existen registros de auditoria para una accion.
     *
     * @param action tipo de accion
     * @return {@code true} si existen registros, {@code false} en caso contrario
     */
    boolean existsByAction(String action);

    /**
     * Verifica si existen registros de auditoria para una IP.
     *
     * @param ipAddress direccion IP
     * @return {@code true} si existen registros, {@code false} en caso contrario
     */
    boolean existsByIpAddress(String ipAddress);

    /**
     * Cuenta los registros de auditoria por tipo de entidad.
     *
     * @param entityType tipo de entidad
     * @return cantidad de registros
     */
    long countByEntityType(String entityType);

    /**
     * Cuenta los registros de auditoria por usuario.
     *
     * @param userId identificador del usuario
     * @return cantidad de registros
     */
    long countByUser(Integer userId);

    /**
     * Cuenta los registros de auditoria por accion.
     *
     * @param action tipo de accion
     * @return cantidad de registros
     */
    long countByAction(String action);

    /**
     * Cuenta los registros de auditoria dentro de un rango de fechas.
     *
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return cantidad de registros
     */
    long countByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Cuenta los registros de auditoria por direccion IP.
     *
     * @param ipAddress direccion IP
     * @return cantidad de registros
     */
    long countByIpAddress(String ipAddress);

    // ==================== Audit trail operations ====================

    /**
     * Obtiene el rastro de auditoria completo para una entidad.
     *
     * @param entityType tipo de entidad
     * @param entityId identificador de la entidad
     * @return lista ordenada de registros de auditoria
     */
    List<AuditLog> getAuditTrailForEntity(String entityType, Integer entityId);

    /**
     * Obtiene el rastro de auditoria completo para un usuario.
     *
     * @param userId identificador del usuario
     * @return lista ordenada de registros de auditoria
     */
    List<AuditLog> getAuditTrailForUser(Integer userId);

    /**
     * Obtiene el rastro de auditoria para una entidad filtrado por usuario.
     *
     * @param entityType tipo de entidad
     * @param entityId identificador de la entidad
     * @param userId identificador del usuario
     * @return lista de registros que coinciden
     */
    List<AuditLog> getAuditTrailForEntityAndUser(String entityType, Integer entityId, Integer userId);

    /**
     * Obtiene el rastro de auditoria para una entidad filtrado por accion.
     *
     * @param entityType tipo de entidad
     * @param entityId identificador de la entidad
     * @param action tipo de accion
     * @return lista de registros que coinciden
     */
    List<AuditLog> getAuditTrailForEntityAndAction(String entityType, Integer entityId, String action);

    /**
     * Obtiene el rastro de auditoria para un usuario filtrado por accion.
     *
     * @param userId identificador del usuario
     * @param action tipo de accion
     * @return lista de registros que coinciden
     */
    List<AuditLog> getAuditTrailForUserAndAction(Integer userId, String action);

    /**
     * Obtiene el rastro de auditoria para una entidad dentro de un rango de fechas.
     *
     * @param entityType tipo de entidad
     * @param entityId identificador de la entidad
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de registros que coinciden
     */
    List<AuditLog> getAuditTrailForEntityAndDateRange(String entityType, Integer entityId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Obtiene el rastro de auditoria para un usuario dentro de un rango de fechas.
     *
     * @param userId identificador del usuario
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de registros que coinciden
     */
    List<AuditLog> getAuditTrailForUserAndDateRange(Integer userId, LocalDateTime startDate, LocalDateTime endDate);

    // ==================== Statistics and analytics ====================

    /**
     * Obtiene el total de registros de auditoria en el sistema.
     *
     * @return cantidad total de registros
     */
    long getTotalAuditLogs();

    /**
     * Obtiene el total de registros de auditoria por tipo de entidad.
     *
     * @param entityType tipo de entidad
     * @return cantidad de registros
     */
    long getTotalAuditLogsByEntityType(String entityType);

    /**
     * Obtiene el total de registros de auditoria por usuario.
     *
     * @param userId identificador del usuario
     * @return cantidad de registros
     */
    long getTotalAuditLogsByUser(Integer userId);

    /**
     * Obtiene el total de registros de auditoria por accion.
     *
     * @param action tipo de accion
     * @return cantidad de registros
     */
    long getTotalAuditLogsByAction(String action);

    /**
     * Obtiene el total de registros de auditoria dentro de un rango de fechas.
     *
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return cantidad de registros
     */
    long getTotalAuditLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Obtiene el total de registros de auditoria por direccion IP.
     *
     * @param ipAddress direccion IP
     * @return cantidad de registros
     */
    long getTotalAuditLogsByIpAddress(String ipAddress);

    /**
     * Obtiene los usuarios con mayor actividad.
     *
     * @param limit cantidad maxima de resultados
     * @return lista de registros de los usuarios mas activos
     */
    List<AuditLog> getTopUsersByActivity(int limit);

    /**
     * Obtiene los tipos de entidad con mayor actividad.
     *
     * @param limit cantidad maxima de resultados
     * @return lista de registros de los tipos de entidad mas activos
     */
    List<AuditLog> getTopEntityTypesByActivity(int limit);

    /**
     * Obtiene las acciones con mayor actividad.
     *
     * @param limit cantidad maxima de resultados
     * @return lista de registros de las acciones mas frecuentes
     */
    List<AuditLog> getTopActionsByActivity(int limit);

    /**
     * Obtiene las direcciones IP con mayor actividad.
     *
     * @param limit cantidad maxima de resultados
     * @return lista de registros de las IPs mas activas
     */
    List<AuditLog> getTopIpAddressesByActivity(int limit);

    /**
     * Obtiene la actividad agrupada por mes.
     *
     * @return lista de registros agrupados por mes
     */
    List<AuditLog> getActivityByMonth();

    /**
     * Obtiene la actividad agrupada por dia de la semana.
     *
     * @return lista de registros agrupados por dia de la semana
     */
    List<AuditLog> getActivityByDayOfWeek();

    /**
     * Obtiene la actividad agrupada por hora del dia.
     *
     * @return lista de registros agrupados por hora
     */
    List<AuditLog> getActivityByHour();

    // ==================== Security and monitoring operations ====================

    /**
     * Obtiene actividad sospechosa desde una direccion IP.
     *
     * @param ipAddress direccion IP a investigar
     * @return lista de registros sospechosos
     */
    List<AuditLog> getSuspiciousActivity(String ipAddress);

    /**
     * Obtiene actividad desde multiples direcciones IP.
     *
     * @param ipAddresses lista de direcciones IP
     * @return lista de registros desde esas IPs
     */
    List<AuditLog> getActivityByMultipleIpAddresses(List<String> ipAddresses);

    /**
     * Obtiene actividad filtrada por patron de user agent.
     *
     * @param pattern patron a buscar en el user agent
     * @return lista de registros que coinciden con el patron
     */
    List<AuditLog> getActivityByUserAgentPattern(String pattern);

    /**
     * Obtiene los intentos de inicio de sesion fallidos.
     *
     * @return lista de registros de intentos fallidos
     */
    List<AuditLog> getFailedLoginAttempts();

    /**
     * Obtiene los intentos de inicio de sesion exitosos.
     *
     * @return lista de registros de inicios de sesion exitosos
     */
    List<AuditLog> getSuccessfulLoginAttempts();

    /**
     * Obtiene la actividad de modificacion de datos.
     *
     * @return lista de registros de modificaciones
     */
    List<AuditLog> getDataModificationActivity();

    /**
     * Obtiene la actividad de acceso a datos.
     *
     * @return lista de registros de accesos
     */
    List<AuditLog> getDataAccessActivity();

    /**
     * Obtiene la actividad de eliminacion de datos.
     *
     * @return lista de registros de eliminaciones
     */
    List<AuditLog> getDataDeletionActivity();

    // ==================== Data integrity operations ====================

    /**
     * Obtiene registros de auditoria sin usuario asociado.
     *
     * @return lista de registros con usuario faltante
     */
    List<AuditLog> getAuditLogsWithMissingUser();

    /**
     * Obtiene registros de auditoria sin direccion IP.
     *
     * @return lista de registros con IP faltante
     */
    List<AuditLog> getAuditLogsWithMissingIpAddress();

    /**
     * Obtiene registros de auditoria con datos JSON invalidos.
     *
     * @return lista de registros con JSON invalido
     */
    List<AuditLog> getAuditLogsWithInvalidJsonData();

    /**
     * Obtiene registros de auditoria con valores anteriores vacios.
     *
     * @return lista de registros sin valores anteriores
     */
    List<AuditLog> getAuditLogsWithEmptyOldValues();

    /**
     * Obtiene registros de auditoria con valores nuevos vacios.
     *
     * @return lista de registros sin valores nuevos
     */
    List<AuditLog> getAuditLogsWithEmptyNewValues();

    /**
     * Obtiene registros de auditoria con ambos valores (anterior y nuevo) vacios.
     *
     * @return lista de registros sin valores anteriores ni nuevos
     */
    List<AuditLog> getAuditLogsWithBothEmptyValues();

    // ==================== Utility operations ====================

    /**
     * Elimina registros de auditoria antiguos segun la cantidad de dias a conservar.
     *
     * @param daysToKeep cantidad de dias a mantener
     */
    void cleanupOldAuditLogs(int daysToKeep);

    /**
     * Elimina registros de auditoria anteriores a una fecha especifica.
     *
     * @param beforeDate fecha limite para la eliminacion
     */
    void deleteOldAuditLogs(LocalDateTime beforeDate);

    /**
     * Busca registros de auditoria por palabra clave.
     *
     * @param keyword palabra clave a buscar
     * @return lista de registros que contienen la palabra clave
     */
    List<AuditLog> searchAuditLogsByKeyword(String keyword);

    /**
     * Busca registros de auditoria por user agent.
     *
     * @param userAgent cadena del user agent a buscar
     * @return lista de registros que coinciden
     */
    List<AuditLog> searchAuditLogsByUserAgent(String userAgent);

    /**
     * Busca el registro de auditoria mas reciente para una entidad.
     *
     * @param entityType tipo de entidad
     * @param entityId identificador de la entidad
     * @return el registro mas reciente, o vacio si no existe
     */
    Optional<AuditLog> findLatestAuditLogByEntity(String entityType, Integer entityId);

    /**
     * Obtiene registros de auditoria que contienen datos JSON.
     *
     * @return lista de registros con datos JSON
     */
    List<AuditLog> getAuditLogsWithJsonData();

    /**
     * Obtiene registros de auditoria sin datos JSON.
     *
     * @return lista de registros sin datos JSON
     */
    List<AuditLog> getAuditLogsWithoutJsonData();

    /**
     * Busca registros de auditoria por un campo y valor JSON especificos.
     *
     * @param fieldName nombre del campo JSON
     * @param fieldValue valor exacto del campo
     * @return lista de registros que coinciden
     */
    List<AuditLog> getAuditLogsByJsonField(String fieldName, String fieldValue);

    /**
     * Busca registros de auditoria donde un campo JSON contiene un valor.
     *
     * @param fieldName nombre del campo JSON
     * @param fieldValue valor parcial a buscar
     * @return lista de registros que coinciden
     */
    List<AuditLog> getAuditLogsByJsonFieldContaining(String fieldName, String fieldValue);

    /**
     * Busca registros de auditoria donde existe un campo JSON especifico.
     *
     * @param fieldName nombre del campo JSON a verificar
     * @return lista de registros que contienen el campo
     */
    List<AuditLog> getAuditLogsByJsonFieldExists(String fieldName);

    /**
     * Busca registros de auditoria donde no existe un campo JSON especifico.
     *
     * @param fieldName nombre del campo JSON a verificar
     * @return lista de registros que no contienen el campo
     */
    List<AuditLog> getAuditLogsByJsonFieldNotExists(String fieldName);
}
