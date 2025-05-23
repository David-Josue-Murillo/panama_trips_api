package com.app.panama_trips.service;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.persistence.entity.Guide;
import com.app.panama_trips.persistence.repository.GuideRepository;
import com.app.panama_trips.presentation.dto.GuideRequest;
import com.app.panama_trips.service.implementation.GuideService;

import static com.app.panama_trips.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GuideServiceTest {

    @Mock
    private GuideRepository repository;

    @InjectMocks
    private GuideService service;

    private Guide guide;
    private GuideRequest request;
    private List<Guide> guides;

    @BeforeEach
    void setUp() {
        guide = guideOneMock;
        guides = guideListMock;
        request = guideRequestMock;
    }
}
