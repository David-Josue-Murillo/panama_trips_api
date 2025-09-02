package com.app.panama_trips.service;

import static com.app.panama_trips.DataProvider.tourFaqOneMock;
import static com.app.panama_trips.DataProvider.tourFaqRequestMock;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.app.panama_trips.persistence.entity.TourFaq;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.repository.TourFaqRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.presentation.dto.TourFaqRequest;
import com.app.panama_trips.service.implementation.TourFaqService;

import static com.app.panama_trips.DataProvider.*;

@ExtendWith(MockitoExtension.class)
public class TourFaqServiceTest {

    @Mock
    private TourFaqRepository repository;

    @Mock
    private TourPlanRepository tourPlanRepository;

    @InjectMocks
    private TourFaqService service;

    @Captor
    private ArgumentCaptor<TourFaq> tourFaqCaptor;

    private TourFaq tourFaq;
    private TourFaqRequest tourFaqRequest;
    private List<TourFaq> tourFaqList;
    private TourPlan tourPlan;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        tourPlan = tourPlanOneMock;
        tourFaq = tourFaqOneMock();
        tourFaqRequest = tourFaqRequestMock;
        tourFaqList = tourFaqListMock();
        pageable = PageRequest.of(0, 10);
    }
}
