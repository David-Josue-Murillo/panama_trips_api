package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.NotificationTemplate;
import com.app.panama_trips.persistence.repository.NotificationHistoryRepository;
import com.app.panama_trips.persistence.repository.NotificationTemplateRepository;
import com.app.panama_trips.presentation.dto.NotificationTemplateRequest;
import com.app.panama_trips.presentation.dto.NotificationTemplateResponse;
import com.app.panama_trips.service.interfaces.INotificationTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationTemplateService implements INotificationTemplateService {

    private final NotificationTemplateRepository notificationTemplateRepository;
    private final NotificationHistoryRepository notificationHistoryRepository;

    private static final Pattern VARIABLE_PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");

    // CRUD operations
    @Override
    public Page<NotificationTemplateResponse> getAllTemplates(Pageable pageable) {
        return notificationTemplateRepository.findAll(pageable)
                .map(NotificationTemplateResponse::new);
    }

    @Override
    public NotificationTemplateResponse getTemplateById(Integer id) {
        return new NotificationTemplateResponse(findTemplateOrThrow(id));
    }

    @Override
    @Transactional
    public NotificationTemplateResponse saveTemplate(NotificationTemplateRequest request) {
        validateNameUniqueness(request.name());
        validateVariableConsistency(request);
        return new NotificationTemplateResponse(notificationTemplateRepository.save(buildFromRequest(request)));
    }

    @Override
    @Transactional
    public NotificationTemplateResponse updateTemplate(Integer id, NotificationTemplateRequest request) {
        NotificationTemplate template = findTemplateOrThrow(id);
        validateNameUniquenessForUpdate(request.name(), id);
        validateVariableConsistency(request);
        updateFromRequest(template, request);
        return new NotificationTemplateResponse(notificationTemplateRepository.save(template));
    }

    @Override
    @Transactional
    public void deleteTemplate(Integer id) {
        if (!notificationTemplateRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification template not found with id: " + id);
        }
        if (notificationHistoryRepository.existsByTemplate_Id(id)) {
            throw new IllegalArgumentException("Cannot delete a template that is being used by notifications");
        }
        notificationTemplateRepository.deleteById(id);
    }

    // Find operations
    @Override
    public List<NotificationTemplateResponse> findByType(String type) {
        return toResponseList(notificationTemplateRepository.findByType(type));
    }

    @Override
    public Optional<NotificationTemplateResponse> findByName(String name) {
        return notificationTemplateRepository.findByName(name)
                .map(NotificationTemplateResponse::new);
    }

    @Override
    public List<NotificationTemplateResponse> searchBySubjectOrBody(String keyword) {
        return toResponseList(notificationTemplateRepository.searchBySubjectOrBody(keyword));
    }

    @Override
    public List<NotificationTemplateResponse> findByTypeAndNameContaining(String type, String name) {
        return toResponseList(notificationTemplateRepository.findByTypeAndNameContaining(type, name));
    }

    // Specialized queries
    @Override
    public List<NotificationTemplateResponse> getTemplatesByTypeAndContent(String type, String content) {
        return toResponseList(notificationTemplateRepository.findByTypeAndContentInSubjectOrBody(type, content));
    }

    @Override
    public List<NotificationTemplateResponse> getActiveTemplates() {
        return toResponseList(notificationTemplateRepository.findAll());
    }

    @Override
    public List<NotificationTemplateResponse> getTemplatesByVariable(String variable) {
        return toResponseList(notificationTemplateRepository.findByVariablesContaining(variable));
    }

    @Override
    public NotificationTemplateResponse getTemplateForNotification(String type, String event) {
        return notificationTemplateRepository.findByTypeAndNameContaining(type, event)
                .stream()
                .findFirst()
                .map(NotificationTemplateResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException(
                    String.format("No template found for type '%s' and event '%s'", type, event)));
    }

    // Bulk operations
    @Override
    @Transactional
    public void bulkCreateTemplates(List<NotificationTemplateRequest> requests) {
        requests.forEach(request -> {
            validateNameUniqueness(request.name());
            validateVariableConsistency(request);
        });
        List<NotificationTemplate> templates = requests.stream()
                .map(this::buildFromRequest)
                .toList();
        notificationTemplateRepository.saveAll(templates);
    }

    @Override
    @Transactional
    public void bulkUpdateTemplates(List<NotificationTemplateRequest> requests) {
        List<NotificationTemplate> toSave = new ArrayList<>();
        for (NotificationTemplateRequest request : requests) {
            notificationTemplateRepository.findByName(request.name()).ifPresent(template -> {
                validateVariableConsistency(request);
                updateFromRequest(template, request);
                toSave.add(template);
            });
        }
        notificationTemplateRepository.saveAll(toSave);
    }

    @Override
    @Transactional
    public void bulkDeleteTemplates(List<Integer> templateIds) {
        for (Integer id : templateIds) {
            if (notificationHistoryRepository.existsByTemplate_Id(id)) {
                throw new IllegalArgumentException("Cannot delete template with id " + id + " as it is being used by notifications");
            }
        }
        notificationTemplateRepository.deleteAllById(templateIds);
    }

    // Check operations
    @Override
    public boolean existsByName(String name) {
        return notificationTemplateRepository.existsByName(name);
    }

    @Override
    public boolean isTemplateUsedByAnyNotification(Integer templateId) {
        return notificationHistoryRepository.existsByTemplate_Id(templateId);
    }

    @Override
    public long countByType(String type) {
        return notificationTemplateRepository.countByType(type);
    }

    @Override
    public boolean validateTemplateVariables(Integer templateId, List<String> variables) {
        NotificationTemplate template = findTemplateOrThrow(templateId);
        if (template.getVariables() == null) {
            return variables.isEmpty();
        }
        Set<String> templateVars = Set.of(template.getVariables().split(","));
        return variables.stream().allMatch(var -> templateVars.contains(var.trim()));
    }

    // Private helper methods
    private NotificationTemplate findTemplateOrThrow(Integer id) {
        return notificationTemplateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification template not found with id: " + id));
    }

    private void validateNameUniqueness(String name) {
        if (notificationTemplateRepository.existsByName(name)) {
            throw new IllegalArgumentException("A template with this name already exists");
        }
    }

    private void validateNameUniquenessForUpdate(String name, Integer excludeId) {
        if (notificationTemplateRepository.existsByNameAndIdNot(name, excludeId)) {
            throw new IllegalArgumentException("A template with this name already exists");
        }
    }

    private void validateVariableConsistency(NotificationTemplateRequest request) {
        if (request.variables() == null || request.variables().isEmpty()) {
            return;
        }

        Set<String> declaredVars = Set.of(request.variables().split(","));

        for (String variable : declaredVars) {
            String placeholder = "${" + variable.trim() + "}";
            if (!request.body().contains(placeholder)) {
                throw new IllegalArgumentException("Variable '" + variable.trim() + "' is declared but not used in the template body");
            }
        }

        Matcher matcher = VARIABLE_PLACEHOLDER_PATTERN.matcher(request.body());
        while (matcher.find()) {
            String variable = matcher.group(1);
            if (!declaredVars.contains(variable)) {
                throw new IllegalArgumentException("Variable '" + variable + "' is used in the template body but not declared");
            }
        }
    }

    private List<NotificationTemplateResponse> toResponseList(List<NotificationTemplate> templates) {
        return templates.stream()
                .map(NotificationTemplateResponse::new)
                .toList();
    }

    private NotificationTemplate buildFromRequest(NotificationTemplateRequest request) {
        return NotificationTemplate.builder()
                .name(request.name())
                .subject(request.subject())
                .body(request.body())
                .type(request.types())
                .variables(request.variables())
                .build();
    }

    private void updateFromRequest(NotificationTemplate template, NotificationTemplateRequest request) {
        template.setName(request.name());
        template.setSubject(request.subject());
        template.setBody(request.body());
        template.setType(request.types());
        template.setVariables(request.variables());
    }
}
