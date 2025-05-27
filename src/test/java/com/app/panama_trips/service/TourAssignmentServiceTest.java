package com.app.panama_trips.service;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Guide;
import com.app.panama_trips.persistence.entity.TourAssignment;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.repository.GuideRepository;
import com.app.panama_trips.persistence.repository.TourAssignmentRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.presentation.dto.TourAssignmentRequest;
import com.app.panama_trips.presentation.dto.TourAssignmentResponse;
import com.app.panama_trips.service.implementation.TourAssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.app.panama_trips.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TourAssignmentServiceTest {

    @Mock
    private TourAssignmentRepository repository;

    @Mock
    private GuideRepository guideRepository;

    @Mock
    private TourPlanRepository tourPlanRepository;

    @InjectMocks
    private TourAssignmentService service;

    private TourAssignment tourAssignment;
    private TourAssignmentRequest tourAssignmentRequest;
    private List<TourAssignment> tourAssignmentsList;
    private Guide guide;
    private TourPlan tourPlan;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        // Initialize test data
        guide = guideOneMock;
        tourPlan = tourPlanOneMock;
        tourAssignment = tourAssignmentOneMock;
        tourAssignmentsList = tourAssignmentListMock;
        tourAssignmentRequest = tourAssignmentRequestMock;
        pageable = PageRequest.of(0, 10);

        // Setup common mock behaviors
        when(guideRepository.findById(any(Integer.class))).thenReturn(Optional.of(guide));
        when(tourPlanRepository.findById(any(Integer.class))).thenReturn(Optional.of(tourPlan));
        when(repository.save(any(TourAssignment.class))).thenReturn(tourAssignment);
    }
}
