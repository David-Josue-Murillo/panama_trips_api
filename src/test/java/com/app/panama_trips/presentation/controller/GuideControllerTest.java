package com.app.panama_trips.presentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.app.panama_trips.presentation.dto.GuideRequest;
import com.app.panama_trips.presentation.dto.GuideResponse;
import com.app.panama_trips.service.implementation.GuideService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static com.app.panama_trips.DataProvider.*;

import java.util.List;


@WebMvcTest(GuideController.class)
public class GuideControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GuideService guideService;

    private GuideRequest request;
    private GuideResponse response;
    private List<GuideResponse> responseList;

    @BeforeEach
    void setUp() {
        request = guideRequestMock;
        response = guideResponseMock;
        responseList = guideResponseListMock;
    }

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(obj);
    }
}
