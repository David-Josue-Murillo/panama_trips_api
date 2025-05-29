package com.app.panama_trips.presentation;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.app.panama_trips.presentation.controller.TourAssignmentController;
import com.app.panama_trips.presentation.dto.TourAssignmentRequest;
import com.app.panama_trips.presentation.dto.TourAssignmentResponse;
import com.app.panama_trips.service.implementation.TourAssignmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static com.app.panama_trips.DataProvider.*;

@WebMvcTest(TourAssignmentController.class)
public class TourAssignmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TourAssignmentService service;

    private TourAssignmentRequest request;
    private TourAssignmentResponse response;
    private List<TourAssignmentResponse> responsesList;

    @BeforeEach
    void setUp() {
        request = tourAssignmentRequestMock;
        response = tourAssignmentResponseMock;
        responsesList = tourAssignmentResponseListMock;
    }
    
    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(obj);
    }
}
