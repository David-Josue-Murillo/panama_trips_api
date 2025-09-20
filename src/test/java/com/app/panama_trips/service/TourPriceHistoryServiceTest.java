package com.app.panama_trips.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.panama_trips.persistence.entity.TourPriceHistory;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.service.implementation.TourPriceHistoryService;

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

    
}
