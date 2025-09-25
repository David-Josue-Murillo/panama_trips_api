package com.app.panama_trips.presentation.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.app.panama_trips.presentation.dto.TourPriceHistoryRequest;
import com.app.panama_trips.presentation.dto.TourPriceHistoryResponse;
import com.app.panama_trips.service.implementation.TourPriceHistoryService;

import static com.app.panama_trips.DataProvider.*;

@WebMvcTest(TourPriceHistoryController.class)
public class TourPriceHistoryControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TourPriceHistoryService service;

    private TourPriceHistoryRequest request;
    private TourPriceHistoryResponse response;
    private List<TourPriceHistoryResponse> responsesList;

    @BeforeEach
    void setUp() {
        request = tourPriceHistoryRequestMock;
        response = tourPriceHistoryResponseMock;
        responsesList = tourPriceHistoryResponseListMock;
    }
}
