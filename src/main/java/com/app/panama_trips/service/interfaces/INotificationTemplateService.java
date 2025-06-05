package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.NotificationTemplateRequest;
import com.app.panama_trips.presentation.dto.NotificationTemplateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface INotificationTemplateService {
    // CRUD operations
    Page<NotificationTemplateResponse> getAllTemplates(Pageable pageable);
    NotificationTemplateResponse getTemplateById(Integer id);
    NotificationTemplateResponse saveTemplate(NotificationTemplateRequest request);
    NotificationTemplateResponse updateTemplate(Integer id, NotificationTemplateRequest request);
    void deleteTemplate(Integer id);

    // Find operations
    List<NotificationTemplateResponse> findByType(String type);
    Optional<NotificationTemplateResponse> findByName(String name);
    List<NotificationTemplateResponse> searchBySubjectOrBody(String keyword);
    List<NotificationTemplateResponse> findByTypeAndNameContaining(String type, String name);

    // Specialized queries
    List<NotificationTemplateResponse> getTemplatesByTypeAndContent(String type, String content);
    List<NotificationTemplateResponse> getActiveTemplates();
    List<NotificationTemplateResponse> getTemplatesByVariable(String variable);
    NotificationTemplateResponse getTemplateForNotification(String type, String event);

    // Bulk operations
    void bulkCreateTemplates(List<NotificationTemplateRequest> requests);
    void bulkUpdateTemplates(List<NotificationTemplateRequest> requests);
    void bulkDeleteTemplates(List<Integer> templateIds);

    // Check operations
    boolean existsByName(String name);
    boolean isTemplateUsedByAnyNotification(Integer templateId);
    long countByType(String type);
    boolean validateTemplateVariables(Integer templateId, List<String> variables);
}
