package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.service.implementation.TourPlanImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TourPlanImageController.class)
public class TourPlanImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TourPlanImageService tourPlanImageService;

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
    }
}
