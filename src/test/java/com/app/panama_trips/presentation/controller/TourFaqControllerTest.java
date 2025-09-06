package com.app.panama_trips.presentation.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.app.panama_trips.presentation.dto.TourFaqRequest;
import com.app.panama_trips.presentation.dto.TourFaqResponse;
import com.app.panama_trips.service.implementation.TourFaqService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static com.app.panama_trips.DataProvider.*;

@WebMvcTest(TourFaqController.class)
public class TourFaqControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TourFaqService service;

    private TourFaqRequest request;
    private TourFaqResponse response;
    private List<TourFaqResponse> responsesList;
   
    @BeforeEach
    void setUp() {
        request = tourFaqRequestMock;
        response = tourFaqResponseMock;
        responsesList = tourFaqResponseListMock;
    }

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(obj);
    }
}
