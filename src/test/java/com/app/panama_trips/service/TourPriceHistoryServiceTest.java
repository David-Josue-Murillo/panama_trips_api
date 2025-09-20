package com.app.panama_trips.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourPriceHistory;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.presentation.dto.TourPriceHistoryRequest;
import com.app.panama_trips.service.implementation.TourPriceHistoryService;

import static com.app.panama_trips.DataProvider.*;

@ExtendWith(MockitoExtension.class)
public class TourPriceHistoryServiceTest {
    
    @Mock
    private TourPriceHistory repository;

    @Mock
    private TourPlanRepository tourPlanRepository;

    @Mock
    private UserEntityRepository userRepository;

    @InjectMocks
    private TourPriceHistoryService service;

    private TourPlan tourPlan;
    private TourPriceHistory priceHistory1, priceHistory2;
    private TourPriceHistoryRequest request;

    @BeforeEach
    void setUp() {
        tourPlan = tourPlanOneMock;
        priceHistory1 = tourPriceHistoryOneMock();
        priceHistory2 = tourPriceHistoryTwoMock();
        request = tourPriceHistoryRequestMock;
    }
}
