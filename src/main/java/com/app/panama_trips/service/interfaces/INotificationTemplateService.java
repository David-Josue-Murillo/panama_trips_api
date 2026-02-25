package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.NotificationTemplateRequest;
import com.app.panama_trips.presentation.dto.NotificationTemplateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de servicio para la gestion de plantillas de notificacion.
 * Provee operaciones CRUD, busquedas por tipo/nombre, operaciones en lote
 * y validacion de variables de plantilla.
 */
public interface INotificationTemplateService {

    // ==================== CRUD operations ====================

    /**
     * Obtiene todas las plantillas de notificacion de forma paginada.
     *
     * @param pageable parametros de paginacion
     * @return pagina de plantillas de notificacion
     */
    Page<NotificationTemplateResponse> getAllTemplates(Pageable pageable);

    /**
     * Obtiene una plantilla de notificacion por su identificador.
     *
     * @param id identificador de la plantilla
     * @return la plantilla encontrada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    NotificationTemplateResponse getTemplateById(Integer id);

    /**
     * Guarda una nueva plantilla de notificacion.
     *
     * @param request datos de la plantilla a guardar
     * @return la plantilla persistida
     */
    NotificationTemplateResponse saveTemplate(NotificationTemplateRequest request);

    /**
     * Actualiza una plantilla de notificacion existente.
     *
     * @param id identificador de la plantilla a actualizar
     * @param request datos actualizados de la plantilla
     * @return la plantilla actualizada
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    NotificationTemplateResponse updateTemplate(Integer id, NotificationTemplateRequest request);

    /**
     * Elimina una plantilla de notificacion por su identificador.
     *
     * @param id identificador de la plantilla a eliminar
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    void deleteTemplate(Integer id);

    // ==================== Find operations ====================

    /**
     * Busca plantillas de notificacion por tipo.
     *
     * @param type tipo de plantilla
     * @return lista de plantillas del tipo indicado
     */
    List<NotificationTemplateResponse> findByType(String type);

    /**
     * Busca una plantilla de notificacion por su nombre.
     *
     * @param name nombre de la plantilla
     * @return la plantilla encontrada, o vacio si no existe
     */
    Optional<NotificationTemplateResponse> findByName(String name);

    /**
     * Busca plantillas por palabra clave en el asunto o cuerpo.
     *
     * @param keyword palabra clave a buscar
     * @return lista de plantillas que contienen la palabra clave
     */
    List<NotificationTemplateResponse> searchBySubjectOrBody(String keyword);

    /**
     * Busca plantillas por tipo y nombre parcial.
     *
     * @param type tipo de plantilla
     * @param name nombre parcial a buscar
     * @return lista de plantillas que coinciden
     */
    List<NotificationTemplateResponse> findByTypeAndNameContaining(String type, String name);

    // ==================== Specialized queries ====================

    /**
     * Obtiene plantillas por tipo y contenido.
     *
     * @param type tipo de plantilla
     * @param content contenido a buscar
     * @return lista de plantillas que coinciden
     */
    List<NotificationTemplateResponse> getTemplatesByTypeAndContent(String type, String content);

    /**
     * Obtiene todas las plantillas activas.
     *
     * @return lista de plantillas activas
     */
    List<NotificationTemplateResponse> getActiveTemplates();

    /**
     * Obtiene plantillas que usan una variable especifica.
     *
     * @param variable nombre de la variable a buscar
     * @return lista de plantillas que contienen la variable
     */
    List<NotificationTemplateResponse> getTemplatesByVariable(String variable);

    /**
     * Obtiene la plantilla adecuada para un tipo de notificacion y evento.
     *
     * @param type tipo de notificacion
     * @param event evento que dispara la notificacion
     * @return la plantilla correspondiente
     * @throws com.app.panama_trips.exception.ResourceNotFoundException si no se encuentra
     */
    NotificationTemplateResponse getTemplateForNotification(String type, String event);

    // ==================== Bulk operations ====================

    /**
     * Crea multiples plantillas de notificacion en lote.
     *
     * @param requests lista de solicitudes de plantillas a crear
     */
    void bulkCreateTemplates(List<NotificationTemplateRequest> requests);

    /**
     * Actualiza multiples plantillas de notificacion en lote.
     *
     * @param requests lista de solicitudes con datos actualizados
     */
    void bulkUpdateTemplates(List<NotificationTemplateRequest> requests);

    /**
     * Elimina multiples plantillas de notificacion por sus identificadores.
     *
     * @param templateIds lista de identificadores a eliminar
     */
    void bulkDeleteTemplates(List<Integer> templateIds);

    // ==================== Check operations ====================

    /**
     * Verifica si existe una plantilla con el nombre dado.
     *
     * @param name nombre de la plantilla
     * @return {@code true} si existe, {@code false} en caso contrario
     */
    boolean existsByName(String name);

    /**
     * Verifica si una plantilla esta siendo utilizada por alguna notificacion.
     *
     * @param templateId identificador de la plantilla
     * @return {@code true} si esta en uso, {@code false} en caso contrario
     */
    boolean isTemplateUsedByAnyNotification(Integer templateId);

    /**
     * Cuenta las plantillas por tipo.
     *
     * @param type tipo de plantilla
     * @return cantidad de plantillas del tipo indicado
     */
    long countByType(String type);

    /**
     * Valida que las variables proporcionadas sean compatibles con la plantilla.
     *
     * @param templateId identificador de la plantilla
     * @param variables lista de nombres de variables a validar
     * @return {@code true} si todas las variables son validas, {@code false} en caso contrario
     */
    boolean validateTemplateVariables(Integer templateId, List<String> variables);
}
