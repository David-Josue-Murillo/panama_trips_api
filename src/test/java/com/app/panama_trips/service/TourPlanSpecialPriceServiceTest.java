package com.app.panama_trips.service;

import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.persistence.repository.TourPlanSpecialPriceRepository;
import com.app.panama_trips.service.implementation.TourPlanSpecialPriceService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TourPlanSpecialPriceServiceTest {

    @Mock
    private TourPlanSpecialPriceRepository tourPlanSpecialPriceRepository;

    @Mock
    private TourPlanRepository tourPlanRepository;

    @InjectMocks
    private TourPlanSpecialPriceService service;
}
