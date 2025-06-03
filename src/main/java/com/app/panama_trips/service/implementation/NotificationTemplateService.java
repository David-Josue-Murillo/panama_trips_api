package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
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
        return notificationTemplateRepository.findAll(pageable)
                .map(NotificationTemplateResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationTemplateResponse getTemplateById(Integer id) {
        return notificationTemplateRepository.findById(id)
                .map(NotificationTemplateResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("Notification template not found with id: " + id));
    }

    @Override
    @Transactional
    public NotificationTemplateResponse saveTemplate(NotificationTemplateRequest request) {
        validateRequest(request);
        NotificationTemplate template = builderFromRequest(request);
        return new NotificationTemplateResponse(notificationTemplateRepository.save(template));
    }

    @Override
    @Transactional
    public NotificationTemplateResponse updateTemplate(Integer id, NotificationTemplateRequest request) {
        NotificationTemplate template = findTemplateOrFail(id);
        validateRequest(request);
        updateFromRequest(template, request);
        return new NotificationTemplateResponse(notificationTemplateRepository.save(template));
    }

    @Override
    @Transactional
    public void deleteTemplate(Integer id) {
        if (!notificationTemplateRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification template not found with id: " + id);
        }

        if (isTemplateUsedByAnyNotification(id)) {
            throw new IllegalArgumentException("Cannot delete a template that is being used by notifications");
        }

        notificationTemplateRepository.deleteById(id);
    }

    // Find operations
    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> findByType(String type) {
        return notificationTemplateRepository.findByType(type)
                .stream()
                .map(NotificationTemplateResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationTemplateResponse> findByName(String name) {
        return notificationTemplateRepository.findByName(name)
                .map(NotificationTemplateResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> searchBySubjectOrBody(String keyword) {
        return notificationTemplateRepository.searchBySubjectOrBody(keyword)
                .stream()
                .map(NotificationTemplateResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> findByTypeAndNameContaining(String type, String name) {
        return notificationTemplateRepository.findByTypeAndNameContaining(type, name)
                .stream()
                .map(NotificationTemplateResponse::new)
                .toList();
    }

    // Specialized queries
    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> getTemplatesByTypeAndContent(String type, String content) {
        return notificationTemplateRepository.findByTypeAndNameContaining(type, content)
                .stream()
                .filter(template -> template.getSubject().contains(content) || template.getBody().contains(content))
                .map(NotificationTemplateResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> getActiveTemplates() {
        // En este contexto, todas las plantillas se consideran activas
        // Podría extenderse con un flag 'active' en la entidad si es necesario
        return notificationTemplateRepository.findAll()
                .stream()
                .map(NotificationTemplateResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationTemplateResponse> getTemplatesByVariable(String variable) {
        return notificationTemplateRepository.findAll()
                .stream()
                .filter(template -> template.getVariables() != null && 
                        template.getVariables().contains(variable))
                .map(NotificationTemplateResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationTemplateResponse getTemplateForNotification(String type, String event) {
        return notificationTemplateRepository.findByType(type)
                .stream()
                .filter(template -> template.getName().toLowerCase().contains(event.toLowerCase()))
                .findFirst()
                .map(NotificationTemplateResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("No template found for type '%s' and event '%s'", type, event)));
    }

    // Bulk operations
    @Override
    @Transactional
    public void bulkCreateTemplates(List<NotificationTemplateRequest> requests) {
        List<NotificationTemplate> templates = requests.stream()
                .peek(this::validateRequest)
                .map(this::builderFromRequest)
                .toList();

        notificationTemplateRepository.saveAll(templates);
    }

    @Override
    @Transactional
    public void bulkUpdateTemplates(List<NotificationTemplateRequest> requests) {
        for (NotificationTemplateRequest request : requests) {
            Optional<NotificationTemplate> existingTemplate = notificationTemplateRepository.findByName(request.name());
            if (existingTemplate.isPresent()) {
                validateRequest(request);
                updateFromRequest(existingTemplate.get(), request);
                notificationTemplateRepository.save(existingTemplate.get());
            }
        }
    }

    @Override
    @Transactional
    public void bulkDeleteTemplates(List<Integer> templateIds) {
        for (Integer id : templateIds) {
            if (isTemplateUsedByAnyNotification(id)) {
                throw new IllegalArgumentException("Cannot delete template with id " + id + " as it is being used by notifications");
            }
        }
        notificationTemplateRepository.deleteAllById(templateIds);
    }

    // Check operations
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return notificationTemplateRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isTemplateUsedByAnyNotification(Integer templateId) {
        // Implementación mock - reemplazar con verificación real
        // En una implementación real, verificaría las relaciones con las notificaciones
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public long countByType(String type) {
        return notificationTemplateRepository.countByType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateTemplateVariables(Integer templateId, List<String> variables) {
        NotificationTemplate template = findTemplateOrFail(templateId);
        if (template.getVariables() == null) {
            return variables.isEmpty();
        }

        String[] templateVars = template.getVariables().split(",");
        return variables.stream()
                .allMatch(var -> List.of(templateVars).contains(var.trim()));
    }

    // Private methods
    private NotificationTemplate findTemplateOrFail(Integer id) {
        return notificationTemplateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification template not found with id: " + id));
    }   

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

    private void updateFromRequest(NotificationTemplate existingTemplate, NotificationTemplateRequest request) {
        existingTemplate.setName(request.name());
        existingTemplate.setSubject(request.subject());
        existingTemplate.setBody(request.body());
        existingTemplate.setType(request.types());
        existingTemplate.setVariables(request.variables());
    }
}
