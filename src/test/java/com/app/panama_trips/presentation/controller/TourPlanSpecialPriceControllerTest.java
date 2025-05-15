package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.service.implementation.TourPlanSpecialPriceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TourPlanSpecialPriceController.class)
public class TourPlanSpecialPriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TourPlanSpecialPriceService service;

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(obj);
    }
}
