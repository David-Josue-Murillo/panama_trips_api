package com.app.panama_trips.service;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.panama_trips.persistence.entity.NotificationTemplate;
import com.app.panama_trips.persistence.repository.NotificationTemplateRepository;
import com.app.panama_trips.presentation.dto.NotificationTemplateRequest;
import com.app.panama_trips.service.implementation.NotificationTemplateService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.app.panama_trips.DataProvider.*;

import java.util.List;


@ExtendWith(MockitoExtension.class)
public class NotificationTemplateServiceTest {

    @Mock
    private NotificationTemplateRepository repository;

    @InjectMocks
    private NotificationTemplateService service;

    @Captor
    private ArgumentCaptor<NotificationTemplate> notificationTemplateCaptor;

    private NotificationTemplate notificationTemplate;
    private NotificationTemplateRequest request;
    private List<NotificationTemplate> notificationTemplates;

    @BeforeEach
    void setUp() {
        notificationTemplate = notificationTemplateOneMock();
        notificationTemplates = notificationTemplateListMock();
        request = notificationTemplateRequestMock;
    }
}
