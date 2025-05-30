package com.app.panama_trips.service.implementation;

import com.app.panama_trips.persistence.entity.NotificationTemplate;
import com.app.panama_trips.persistence.repository.NotificationTemplateRepository;
import com.app.panama_trips.presentation.dto.NotificationTemplateRequest;
import com.app.panama_trips.presentation.dto.NotificationTemplateResponse;
import com.app.panama_trips.service.interfaces.INotificationTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationTemplateService implements INotificationTemplateService {

    private final NotificationTemplateRepository notificationTemplateRepository;

    // CRUD operations
    @Override
    @Transactional(readOnly = true)
    public Page<NotificationTemplateResponse> getAllTemplates(Pageable pageable) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationTemplateResponse getTemplateById(Integer id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional
    public NotificationTemplateResponse saveTemplate(NotificationTemplateRequest request) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional
    public NotificationTemplateResponse updateTemplate(Integer id, NotificationTemplateRequest request) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional
    public void deleteTemplate(Integer id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // Find operations
    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> findByType(String type) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationTemplateResponse> findByName(String name) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> searchBySubjectOrBody(String keyword) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> findByTypeAndNameContaining(String type, String name) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // Specialized queries
    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> getTemplatesByTypeAndContent(String type, String content) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> getActiveTemplates() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> getTemplatesByVariable(String variable) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationTemplateResponse getTemplateForNotification(String type, String event) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // Bulk operations
    @Override
    @Transactional
    public void bulkCreateTemplates(List<NotificationTemplateRequest> requests) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional
    public void bulkUpdateTemplates(List<NotificationTemplateRequest> requests) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional
    public void bulkDeleteTemplates(List<Integer> templateIds) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // Check operations
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTemplateUsedByAnyNotification(Integer templateId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public long countByType(String type) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateTemplateVariables(Integer templateId, List<String> variables) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // Private methods
    private void validateRequest(NotificationTemplateRequest request) {
        // Validar que el nombre no exista
        if (existsByName(request.name())) {
            throw new IllegalArgumentException("A template with this name already exists");
        }

        // Validar que el tipo sea uno de los permitidos
        String[] validTypes = {"EMAIL", "SMS", "PUSH", "ALL"};
        String[] requestTypes = request.types().split(",");
        for (String type : requestTypes) {
            boolean isValidType = false;
            for (String validType : validTypes) {
                if (type.trim().equals(validType)) {
                    isValidType = true;
                    break;
                }
            }
            if (!isValidType) {
                throw new IllegalArgumentException("Invalid template type: " + type + ". Valid types are: " + String.join(", ", validTypes));
            }
        }

        // Validar que las variables sean un JSON válido si se proporcionan
        if (request.variables() != null && !request.variables().isEmpty()) {
            try {
                // Validar que las variables sean un array de strings válido
                String[] variables = request.variables().split(",");
                for (String variable : variables) {
                    if (!variable.trim().matches("^[a-zA-Z0-9_]+$")) {
                        throw new IllegalArgumentException("Invalid variable name: " + variable + ". Variables must contain only letters, numbers and underscores");
                    }
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid variables format. Variables must be comma-separated values");
            }
        }

        // Validar que el cuerpo no contenga variables no declaradas
        if (request.variables() != null && !request.variables().isEmpty()) {
            String[] declaredVariables = request.variables().split(",");
            for (String variable : declaredVariables) {
                String placeholder = "${" + variable.trim() + "}";
                if (!request.body().contains(placeholder)) {
                    throw new IllegalArgumentException("Variable '" + variable.trim() + "' is declared but not used in the template body");
                }
            }
        }

        // Validar que todas las variables usadas en el cuerpo estén declaradas
        if (request.body() != null) {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\\$\\{([^}]+)\\}");
            java.util.regex.Matcher matcher = pattern.matcher(request.body());
            while (matcher.find()) {
                String variable = matcher.group(1);
                if (request.variables() == null || !request.variables().contains(variable)) {
                    throw new IllegalArgumentException("Variable '" + variable + "' is used in the template body but not declared");
                }
            }
        }
    }

    private NotificationTemplate builderFromRequest(NotificationTemplateRequest request) {
        return NotificationTemplate.builder()
            .name(request.name())
            .subject(request.subject())
            .body(request.body())
            .type(request.types())
            .variables(request.variables())
            .build();
    }

    private void updateFromRequest(NotificationTemplate existingNotificaiton, NotificationTemplateRequest request) {
        existingNotificaiton.setName(request.name());
        existingNotificaiton.setSubject(request.subject());
        existingNotificaiton.setBody(request.body());
        existingNotificaiton.setType(request.types());
        existingNotificaiton.setVariables(request.variables());
    }
}
