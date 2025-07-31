package com.app.panama_trips.presentation.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.presentation.dto.NotificationHistoryRequest;
import com.app.panama_trips.presentation.dto.NotificationHistoryResponse;
import com.app.panama_trips.service.implementation.NotificationHistoryService;

import static com.app.panama_trips.DataProvider.*;

@WebMvcTest(NotificationHistoryController.class)
public class NotificationHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationHistoryService service;

    private NotificationHistoryRequest request;
    private NotificationHistoryResponse response;
    private List<NotificationHistoryResponse> responseList;

    @BeforeEach
    void setUp() {
        request = notificationHistoryRequest;
        response = notificationHistoryResponse;
        responseList = notificationHistoryListResponse;
    }

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(obj);
    }
}
