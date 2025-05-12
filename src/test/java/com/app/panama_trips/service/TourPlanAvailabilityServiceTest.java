package com.app.panama_trips.service;

import com.app.panama_trips.persistence.repository.TourPlanAvailabilityRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.service.implementation.TourPlanAvailabilityService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TourPlanAvailabilityServiceTest {

    @Mock
    private TourPlanAvailabilityRepository tourPlanAvailabilityRepository;

    @Mock
    private TourPlanRepository tourPlanRepository;

    @InjectMocks
    private TourPlanAvailabilityService tourPlanAvailabilityService;

    // Add test methods here
}
