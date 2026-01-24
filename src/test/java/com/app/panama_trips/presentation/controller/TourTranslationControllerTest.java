package com.app.panama_trips.presentation.controller;

import java.util.List;

import static com.app.panama_trips.DataProvider.tourTranslationRequestMock;
import static com.app.panama_trips.DataProvider.tourTranslationResponseListMock;
import static com.app.panama_trips.DataProvider.tourTranslationResponseMock;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.app.panama_trips.presentation.dto.TourTranslationRequest;
import com.app.panama_trips.presentation.dto.TourTranslationResponse;
import com.app.panama_trips.service.interfaces.ITourTranslationService;

@WebMvcTest(TourTranslationController.class)
public class TourTranslationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ITourTranslationService tourTranslationService;

    private TourTranslationRequest request;
    private TourTranslationResponse response;
    private List<TourTranslationResponse> responseList;

    @BeforeEach
    void setUp() {
        request = tourTranslationRequestMock();
        response = tourTranslationResponseMock();
        responseList = tourTranslationResponseListMock();
    }
}
