package com.app.panama_trips.service;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.NotificationTemplate;
import com.app.panama_trips.persistence.repository.NotificationTemplateRepository;
import com.app.panama_trips.presentation.dto.NotificationTemplateRequest;
import com.app.panama_trips.presentation.dto.NotificationTemplateResponse;
import com.app.panama_trips.service.implementation.NotificationTemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.app.panama_trips.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationTemplateServiceTest {

    @Mock
    private NotificationTemplateRepository repository;

    @InjectMocks
    private NotificationTemplateService service;

    @Captor
    private ArgumentCaptor<NotificationTemplate> templateCaptor;

    @Captor
    private ArgumentCaptor<List<NotificationTemplate>> templatesCaptor;

    private NotificationTemplate template;
    private NotificationTemplateRequest request;
    private List<NotificationTemplate> templates;

    @BeforeEach
    void setUp() {
        template = notificationTemplateOneMock();
        templates = notificationTemplateListMock();
        request = notificationTemplateRequestMock;
    }

    @Test
    @DisplayName("Should return all templates when getAllTemplates is called with pagination")
    void getAllTemplates_shouldReturnAllData() {
        // Given
        Page<NotificationTemplate> page = new PageImpl<>(templates);
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(page);

        // When
        Page<NotificationTemplateResponse> response = service.getAllTemplates(pageable);

        // Then
        assertNotNull(response);
        assertEquals(templates.size(), response.getTotalElements());
        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("Should return template by id when exists")
    void getTemplateById_whenExists_shouldReturnTemplate() {
        // Given
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.of(template));

        // When
        NotificationTemplateResponse result = service.getTemplateById(id);

        // Then
        assertNotNull(result);
        assertEquals(template.getId(), result.id());
        assertEquals(template.getName(), result.name());
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should throw exception when getting template by id that doesn't exist")
    void getTemplateById_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getTemplateById(id)
        );
        assertEquals("Notification template not found with id: " + id, exception.getMessage());
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should save template successfully")
    void saveTemplate_success() {
        // Given
        when(repository.existsByName(request.name())).thenReturn(false);
        when(repository.save(any(NotificationTemplate.class))).thenReturn(template);

        // When
        NotificationTemplateResponse result = service.saveTemplate(request);

        // Then
        assertNotNull(result);
        assertEquals(template.getId(), result.id());
        assertEquals(template.getName(), result.name());

        verify(repository).save(templateCaptor.capture());

        NotificationTemplate savedTemplate = templateCaptor.getValue();
        assertEquals(request.name(), savedTemplate.getName());
        assertEquals(request.subject(), savedTemplate.getSubject());
        assertEquals(request.body(), savedTemplate.getBody());
        assertEquals(request.types(), savedTemplate.getType());
        assertEquals(request.variables(), savedTemplate.getVariables());
    }

    @Test
    @DisplayName("Should throw exception when saving template with duplicate name")
    void saveTemplate_withDuplicateName_shouldThrowException() {
        // Given
        when(repository.existsByName(request.name())).thenReturn(true);

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.saveTemplate(request)
        );
        assertEquals("A template with this name already exists", exception.getMessage());
        verify(repository).existsByName(request.name());
        verify(repository, never()).save(any(NotificationTemplate.class));
    }

    @Test
    @DisplayName("Should update template successfully")
    void updateTemplate_success() {
        // Given
        Integer id = 1;
        NotificationTemplateRequest updateRequest = new NotificationTemplateRequest(
                "Updated Template",
                "Updated Subject",
                "Hello ${name}, your phone is ${phone}",
                "SMS",
                "name,phone"
        );

        when(repository.findById(id)).thenReturn(Optional.of(template));
        when(repository.existsByName(updateRequest.name())).thenReturn(false);
        when(repository.save(any(NotificationTemplate.class))).thenReturn(template);

        // When
        NotificationTemplateResponse result = service.updateTemplate(id, updateRequest);

        // Then
        assertNotNull(result);

        verify(repository).findById(id);
        verify(repository).existsByName(updateRequest.name());
        verify(repository).save(templateCaptor.capture());

        NotificationTemplate updatedTemplate = templateCaptor.getValue();
        assertEquals(updateRequest.name(), updatedTemplate.getName());
        assertEquals(updateRequest.subject(), updatedTemplate.getSubject());
        assertEquals(updateRequest.body(), updatedTemplate.getBody());
        assertEquals(updateRequest.types(), updatedTemplate.getType());
        assertEquals(updateRequest.variables(), updatedTemplate.getVariables());
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent template")
    void deleteTemplate_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(repository.existsById(id)).thenReturn(false);

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteTemplate(id)
        );
        assertEquals("Notification template not found with id: " + id, exception.getMessage());
        verify(repository).existsById(id);
        verify(repository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Should delete template successfully when not in use")
    void deleteTemplate_whenNotInUse_success() {
        // Given
        Integer id = 1;
        when(repository.existsById(id)).thenReturn(true);

        // When
        service.deleteTemplate(id);

        // Then
        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw exception when deleting template in use")
    void deleteTemplate_whenInUse_shouldThrowException() {
        // Given
        Integer id = 1;

        // Mock the service to return true for isTemplateUsedByAnyNotification
        NotificationTemplateService spyService = spy(service);
        doReturn(true).when(spyService).isTemplateUsedByAnyNotification(id);
        when(repository.existsById(id)).thenReturn(true);

        // When/Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> spyService.deleteTemplate(id)
        );
        assertEquals("Cannot delete a template that is being used by notifications", exception.getMessage());
        verify(repository).existsById(id);
        verify(repository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Should find templates by type")
    void findByType_shouldReturnMatchingTemplates() {
        // Given
        String type = "EMAIL";
        when(repository.findByType(type)).thenReturn(templates);

        // When
        List<NotificationTemplateResponse> result = service.findByType(type);

        // Then
        assertNotNull(result);
        assertEquals(templates.size(), result.size());
        verify(repository).findByType(type);
    }

    @Test
    @DisplayName("Should find template by name when exists")
    void findByName_whenExists_shouldReturnTemplate() {
        // Given
        String name = "Test one";
        when(repository.findByName(name)).thenReturn(Optional.of(template));

        // When
        Optional<NotificationTemplateResponse> result = service.findByName(name);

        // Then
        assertTrue(result.isPresent());
        assertEquals(template.getId(), result.get().id());
        assertEquals(name, result.get().name());
        verify(repository).findByName(name);
    }

    @Test
    @DisplayName("Should return empty optional when template name doesn't exist")
    void findByName_whenNotExists_shouldReturnEmpty() {
        // Given
        String name = "Non-existent Template";
        when(repository.findByName(name)).thenReturn(Optional.empty());

        // When
        Optional<NotificationTemplateResponse> result = service.findByName(name);

        // Then
        assertFalse(result.isPresent());
        verify(repository).findByName(name);
    }

    @Test
    @DisplayName("Should search templates by subject or body")
    void searchBySubjectOrBody_shouldReturnMatchingTemplates() {
        // Given
        String keyword = "test";
        when(repository.searchBySubjectOrBody(keyword)).thenReturn(templates);

        // When
        List<NotificationTemplateResponse> result = service.searchBySubjectOrBody(keyword);

        // Then
        assertNotNull(result);
        assertEquals(templates.size(), result.size());
        verify(repository).searchBySubjectOrBody(keyword);
    }

    @Test
    @DisplayName("Should find templates by type and name")
    void findByTypeAndNameContaining_shouldReturnMatchingTemplates() {
        // Given
        String type = "EMAIL";
        String name = "test";
        when(repository.findByTypeAndNameContaining(type, name)).thenReturn(templates);

        // When
        List<NotificationTemplateResponse> result = service.findByTypeAndNameContaining(type, name);

        // Then
        assertNotNull(result);
        assertEquals(templates.size(), result.size());
        verify(repository).findByTypeAndNameContaining(type, name);
    }

    @Test
    @DisplayName("Should get templates by type and content")
    void getTemplatesByTypeAndContent_shouldReturnMatchingTemplates() {
        // Given
        String type = "EMAIL";
        String content = "test";
        when(repository.findByTypeAndNameContaining(type, content)).thenReturn(templates);

        // When
        List<NotificationTemplateResponse> result = service.getTemplatesByTypeAndContent(type, content);

        // Then
        assertNotNull(result);
        verify(repository).findByTypeAndNameContaining(type, content);
    }

    @Test
    @DisplayName("Should get all active templates")
    void getActiveTemplates_shouldReturnAllTemplates() {
        // Given
        when(repository.findAll()).thenReturn(templates);

        // When
        List<NotificationTemplateResponse> result = service.getActiveTemplates();

        // Then
        assertNotNull(result);
        assertEquals(templates.size(), result.size());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get templates by variable")
    void getTemplatesByVariable_shouldReturnMatchingTemplates() {
        // Given
        String variable = "name";
        when(repository.findAll()).thenReturn(templates);

        // When
        List<NotificationTemplateResponse> result = service.getTemplatesByVariable(variable);

        // Then
        assertNotNull(result);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should get template for notification")
    void getTemplateForNotification_shouldReturnMatchingTemplate() {
        // Given
        String type = "EMAIL";
        String event = "test";
        when(repository.findByType(type)).thenReturn(templates);

        // When
        NotificationTemplateResponse result = service.getTemplateForNotification(type, event);

        // Then
        assertNotNull(result);
        verify(repository).findByType(type);
    }

    @Test
    @DisplayName("Should throw exception when no template found for notification")
    void getTemplateForNotification_whenNoTemplateFound_shouldThrowException() {
        // Given
        String type = "EMAIL";
        String event = "nonexistent";
        when(repository.findByType(type)).thenReturn(Collections.emptyList());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getTemplateForNotification(type, event)
        );
        assertEquals(String.format("No template found for type '%s' and event '%s'", type, event), 
                exception.getMessage());
        verify(repository).findByType(type);
    }

    @Test
    @DisplayName("Should bulk create multiple templates")
    void bulkCreateTemplates_success() {
        // Given
        List<NotificationTemplateRequest> requests = List.of(
                notificationTemplateRequestMock,
                new NotificationTemplateRequest("Test two", "Subject two", "Hello ${name}, your phone is ${phone}", "SMS", "name,phone")
        );

        when(repository.existsByName(any())).thenReturn(false);

        // When
        service.bulkCreateTemplates(requests);

        // Then
        verify(repository, times(2)).existsByName(any());
        verify(repository).saveAll(templatesCaptor.capture());

        List<NotificationTemplate> savedTemplates = templatesCaptor.getValue();
        assertEquals(2, savedTemplates.size());
        assertEquals(requests.get(0).name(), savedTemplates.get(0).getName());
        assertEquals(requests.get(1).name(), savedTemplates.get(1).getName());
    }

    @Test
    @DisplayName("Should bulk update multiple templates")
    void bulkUpdateTemplates_success() {
        // Given
        List<NotificationTemplateRequest> requests = List.of(
                notificationTemplateRequestMock,
                new NotificationTemplateRequest("Test two", "Subject two", "Hello ${name}, your phone is ${phone}", "SMS", "name,phone")
        );

        when(repository.findByName(any())).thenReturn(Optional.of(template));

        // When
        service.bulkUpdateTemplates(requests);

        // Then
        verify(repository, times(2)).findByName(any());
        verify(repository, times(2)).save(any(NotificationTemplate.class));
    }

    @Test
    @DisplayName("Should bulk delete multiple templates")
    void bulkDeleteTemplates_success() {
        // Given
        List<Integer> templateIds = List.of(1, 2, 3);

        // When
        service.bulkDeleteTemplates(templateIds);

        // Then
        verify(repository).deleteAllById(templateIds);
    }

    @Test
    @DisplayName("Should check if template exists by name")
    void existsByName_whenExists_returnsTrue() {
        // Given
        String name = "Test one";
        when(repository.existsByName(name)).thenReturn(true);

        // When
        boolean result = service.existsByName(name);

        // Then
        assertTrue(result);
        verify(repository).existsByName(name);
    }

    @Test
    @DisplayName("Should check if template does not exist by name")
    void existsByName_whenNotExists_returnsFalse() {
        // Given
        String name = "Non-existent Template";
        when(repository.existsByName(name)).thenReturn(false);

        // When
        boolean result = service.existsByName(name);

        // Then
        assertFalse(result);
        verify(repository).existsByName(name);
    }

    @Test
    @DisplayName("Should count templates by type")
    void countByType_shouldReturnCorrectCount() {
        // Given
        String type = "EMAIL";
        when(repository.countByType(type)).thenReturn(3L);

        // When
        long result = service.countByType(type);

        // Then
        assertEquals(3L, result);
        verify(repository).countByType(type);
    }

    @Test
    @DisplayName("Should validate template variables")
    void validateTemplateVariables_shouldReturnTrue_whenVariablesMatch() {
        // Given
        Integer templateId = 1;
        List<String> variables = List.of("name", "email");
        when(repository.findById(templateId)).thenReturn(Optional.of(template));

        // When
        boolean result = service.validateTemplateVariables(templateId, variables);

        // Then
        assertTrue(result);
        verify(repository).findById(templateId);
    }

    @Test
    @DisplayName("Should validate template variables and return false when variables don't match")
    void validateTemplateVariables_shouldReturnFalse_whenVariablesDontMatch() {
        // Given
        Integer templateId = 1;
        List<String> variables = List.of("name", "phone"); // phone is not in template variables
        when(repository.findById(templateId)).thenReturn(Optional.of(template));

        // When
        boolean result = service.validateTemplateVariables(templateId, variables);

        // Then
        assertFalse(result);
        verify(repository).findById(templateId);
    }

    @Test
    @DisplayName("Should check if template is used by any notification")
    void isTemplateUsedByAnyNotification_returnsFalse() {
        // Given/When
        boolean result = service.isTemplateUsedByAnyNotification(1);

        // Then - currently implemented to always return false
        assertFalse(result);
    }
}
