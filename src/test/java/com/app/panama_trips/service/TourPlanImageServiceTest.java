package com.app.panama_trips.service;

import com.app.panama_trips.persistence.repository.TourPlanImageRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.service.implementation.TourPlanImageService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TourPlanImageServiceTest {

    @Mock
    private TourPlanImageRepository tourPlanImageRepository;

    @Mock
    private TourPlanRepository tourPlanRepository;

    @InjectMocks
    private TourPlanImageService tourPlanImageService;

}
