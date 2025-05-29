package com.app.panama_trips.service;

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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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

    @Captor
    private ArgumentCaptor<TourAssignment> tourAssignmentCaptor;

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
    }

    @Test
    @DisplayName("Should return all tour assignments when getAllAssignments is called with pagination")
    void getAllAssignments_shouldReturnAllData() {
        // Given
        Page<TourAssignment> page = new PageImpl<>(tourAssignmentsList);
        when(repository.findAll(pageable)).thenReturn(page);

        // When
        Page<TourAssignmentResponse> response = service.getAllAssignments(pageable);

        // Then
        assertNotNull(response);
        assertEquals(tourAssignmentsList.size(), response.getTotalElements());
        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("Should return tour assignment by id when exists")
    void getAssignmentById_whenExists_shouldReturnAssignment() {
        // Given
        Integer id = 1;
        when(repository.findById(id)).thenReturn(Optional.of(tourAssignment));

        // When
        Optional<TourAssignmentResponse> result = service.getAssignmentById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(tourAssignment.getId(), result.get().id());
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should return empty when tour assignment not found")
    void getAssignmentById_whenNotExists_shouldReturnEmpty() {
        // Given
        Integer id = 999;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<TourAssignmentResponse> result = service.getAssignmentById(id);

        // Then
        assertTrue(result.isEmpty());
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should create tour assignment successfully")
    void createAssignment_success() {
        // Given
        when(guideRepository.findById(anyInt())).thenReturn(Optional.of(guide));
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.of(tourPlan));
        when(repository.existsByGuideAndReservationDate(any(Guide.class), any(LocalDate.class)))
            .thenReturn(false);
        when(repository.save(any(TourAssignment.class))).thenReturn(tourAssignmentOneMock);

        // When
        TourAssignmentResponse result = service.createAssignment(tourAssignmentRequest);

        // Then
        assertNotNull(result);
        assertEquals(tourAssignment.getId(), result.id());
        verify(repository).save(tourAssignmentCaptor.capture());

        TourAssignment savedAssignment = tourAssignmentCaptor.getValue();
        assertEquals(tourAssignmentRequest.guideId(), savedAssignment.getGuide().getId());
        assertEquals(tourAssignmentRequest.tourPlanId(), savedAssignment.getTourPlan().getId());
        assertEquals(tourAssignmentRequest.reservationDate(), savedAssignment.getReservationDate());
        assertEquals(tourAssignmentRequest.status(), savedAssignment.getStatus());
        assertEquals(tourAssignmentRequest.notes(), savedAssignment.getNotes());
    }

    @Test
    @DisplayName("Should throw exception when guide is already assigned for the date")
    void createAssignment_whenGuideAlreadyAssigned_shouldThrowException() {
        // Given
        when(guideRepository.findById(anyInt())).thenReturn(Optional.of(guide));
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.of(tourPlan));
        when(repository.existsByGuideAndReservationDate(any(Guide.class), any(LocalDate.class)))
            .thenReturn(true);

        // When/Then
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> service.createAssignment(tourAssignmentRequest)
        );
        assertEquals("Guide is already assigned for this date", exception.getMessage());
        verify(repository, never()).save(any(TourAssignment.class));
    }

    @Test
    @DisplayName("Should throw exception when reservation date is in the past")
    void createAssignment_whenDateInPast_shouldThrowException() {
        // Given
        when(guideRepository.findById(anyInt())).thenReturn(Optional.of(guide));
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.of(tourPlan));
        when(repository.existsByGuideAndReservationDate(any(Guide.class), any(LocalDate.class)))
            .thenReturn(false);
        TourAssignmentRequest pastDateRequest = new TourAssignmentRequest(
            tourAssignmentRequest.guideId(),
            tourAssignmentRequest.tourPlanId(),
            LocalDate.now().minusDays(1),
            tourAssignmentRequest.status(),
            tourAssignmentRequest.notes()
        );

        // When/Then
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> service.createAssignment(pastDateRequest)
        );
        assertEquals("Reservation date cannot be in the past", exception.getMessage());
        verify(repository, never()).save(any(TourAssignment.class));
    }

    @Test
    @DisplayName("Should update tour assignment successfully")
    void updateAssignment_success() {
        // Given
        Integer id = 1;
        TourAssignmentRequest updateRequest = new TourAssignmentRequest(
            2, // different guide
            2, // different tour plan
            LocalDate.now().plusDays(14),
            "COMPLETED",
            "Updated notes"
        );

        when(repository.findById(anyInt())).thenReturn(Optional.of(tourAssignment));
        when(guideRepository.findById(anyInt())).thenReturn(Optional.of(guideTwoMock));
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.of(tourPlanTwoMock));
        when(repository.save(any(TourAssignment.class))).thenReturn(tourAssignment);

        // When
        TourAssignmentResponse result = service.updateAssignment(id, updateRequest);

        // Then
        assertNotNull(result);
        verify(repository).findById(id);
        verify(repository).save(tourAssignmentCaptor.capture());

        TourAssignment updatedAssignment = tourAssignmentCaptor.getValue();
        assertEquals(updateRequest.guideId(), updatedAssignment.getGuide().getId());
        assertEquals(updateRequest.tourPlanId(), updatedAssignment.getTourPlan().getId());
        assertEquals(updateRequest.reservationDate(), updatedAssignment.getReservationDate());
        assertEquals(updateRequest.status(), updatedAssignment.getStatus());
        assertEquals(updateRequest.notes(), updatedAssignment.getNotes());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent assignment")
    void updateAssignment_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> service.updateAssignment(id, tourAssignmentRequest)
        );
        assertEquals("TourAssignment not found with id: " + id, exception.getMessage());
        verify(repository, never()).save(any(TourAssignment.class));
    }

    @Test
    @DisplayName("Should delete tour assignment successfully")
    void deleteAssignment_success() {
        // Given
        Integer id = 1;
        when(repository.existsById(id)).thenReturn(true);

        // When
        service.deleteAssignment(id);

        // Then
        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent assignment")
    void deleteAssignment_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(repository.existsById(id)).thenReturn(false);

        // When/Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> service.deleteAssignment(id)
        );
        assertEquals("TourAssignment not found with id: " + id, exception.getMessage());
        verify(repository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Should get assignments by guide")
    void getAssignmentsByGuide_shouldReturnAssignments() {
        // Given
        when(repository.findByGuide(any(Guide.class))).thenReturn(tourAssignmentsList);

        // When
        List<TourAssignmentResponse> result = service.getAssignmentsByGuide(guide);

        // Then
        assertNotNull(result);
        assertEquals(tourAssignmentsList.size(), result.size());
        verify(repository).findByGuide(guide);
    }

    @Test
    @DisplayName("Should get assignments by tour plan")
    void getAssignmentsByTourPlan_shouldReturnAssignments() {
        // Given
        when(repository.findByTourPlan(any(TourPlan.class))).thenReturn(tourAssignmentsList);

        // When
        List<TourAssignmentResponse> result = service.getAssignmentsByTourPlan(tourPlan);

        // Then
        assertNotNull(result);
        assertEquals(tourAssignmentsList.size(), result.size());
        verify(repository).findByTourPlan(tourPlan);
    }

    @Test
    @DisplayName("Should get assignments by status")
    void getAssignmentsByStatus_shouldReturnAssignments() {
        // Given
        String status = "ASSIGNED";
        when(repository.findByStatus(status)).thenReturn(tourAssignmentsList);

        // When
        List<TourAssignmentResponse> result = service.getAssignmentsByStatus(status);

        // Then
        assertNotNull(result);
        assertEquals(tourAssignmentsList.size(), result.size());
        verify(repository).findByStatus(status);
    }

    @Test
    @DisplayName("Should get assignments by date")
    void getAssignmentsByDate_shouldReturnAssignments() {
        // Given
        LocalDate date = LocalDate.now();
        when(repository.findByReservationDate(date)).thenReturn(tourAssignmentsList);

        // When
        List<TourAssignmentResponse> result = service.getAssignmentsByDate(date);

        // Then
        assertNotNull(result);
        assertEquals(tourAssignmentsList.size(), result.size());
        verify(repository).findByReservationDate(date);
    }

    @Test
    @DisplayName("Should get assignments by date range")
    void getAssignmentsByDateRange_shouldReturnAssignments() {
        // Given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        when(repository.findByReservationDateBetween(startDate, endDate))
            .thenReturn(tourAssignmentsList);

        // When
        List<TourAssignmentResponse> result = service.getAssignmentsByDateRange(startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(tourAssignmentsList.size(), result.size());
        verify(repository).findByReservationDateBetween(startDate, endDate);
    }

    @Test
    @DisplayName("Should get assignments by guide and status")
    void getAssignmentsByGuideAndStatus_shouldReturnAssignments() {
        // Given
        String status = "ASSIGNED";
        when(repository.findByGuideAndStatus(any(Guide.class), eq(status)))
            .thenReturn(tourAssignmentsList);

        // When
        List<TourAssignmentResponse> result = service.getAssignmentsByGuideAndStatus(guide, status);

        // Then
        assertNotNull(result);
        assertEquals(tourAssignmentsList.size(), result.size());
        verify(repository).findByGuideAndStatus(guide, status);
    }

    @Test
    @DisplayName("Should get assignments by tour plan and date range")
    void getAssignmentsByTourPlanAndDateRange_shouldReturnAssignments() {
        // Given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        when(repository.findByTourPlanAndReservationDateBetween(any(TourPlan.class), eq(startDate), eq(endDate)))
            .thenReturn(tourAssignmentsList);

        // When
        List<TourAssignmentResponse> result = service.getAssignmentsByTourPlanAndDateRange(tourPlan, startDate, endDate);

        // Then
        assertNotNull(result);
        assertEquals(tourAssignmentsList.size(), result.size());
        verify(repository).findByTourPlanAndReservationDateBetween(tourPlan, startDate, endDate);
    }

    @Test
    @DisplayName("Should get assignment by guide, tour plan and date")
    void getAssignmentByGuideTourPlanAndDate_shouldReturnAssignment() {
        // Given
        LocalDate date = LocalDate.now();
        when(repository.findByGuideAndTourPlanAndReservationDate(any(Guide.class), any(TourPlan.class), eq(date)))
            .thenReturn(Optional.of(tourAssignment));

        // When
        Optional<TourAssignmentResponse> result = service.getAssignmentByGuideTourPlanAndDate(guide, tourPlan, date);

        // Then
        assertTrue(result.isPresent());
        assertEquals(tourAssignment.getId(), result.get().id());
        verify(repository).findByGuideAndTourPlanAndReservationDate(guide, tourPlan, date);
    }

    @Test
    @DisplayName("Should get upcoming assignments by guide")
    void getUpcomingAssignmentsByGuide_shouldReturnAssignments() {
        // Given
        Integer guideId = 1;
        LocalDate startDate = LocalDate.now();
        when(repository.findUpcomingAssignmentsByGuide(eq(guideId), eq(startDate)))
            .thenReturn(tourAssignmentsList);

        // When
        List<TourAssignmentResponse> result = service.getUpcomingAssignmentsByGuide(guideId, startDate);

        // Then
        assertNotNull(result);
        assertEquals(tourAssignmentsList.size(), result.size());
        verify(repository).findUpcomingAssignmentsByGuide(guideId, startDate);
    }

    @Test
    @DisplayName("Should count assignments by guide and status")
    void countAssignmentsByGuideAndStatus_shouldReturnCount() {
        // Given
        Integer guideId = 1;
        String status = "ASSIGNED";
        Long expectedCount = 5L;
        when(repository.countAssignmentsByGuideAndStatus(eq(guideId), eq(status)))
            .thenReturn(expectedCount);

        // When
        Long result = service.countAssignmentsByGuideAndStatus(guideId, status);

        // Then
        assertEquals(expectedCount, result);
        verify(repository).countAssignmentsByGuideAndStatus(guideId, status);
    }

    @Test
    @DisplayName("Should check if guide is available for date")
    void isGuideAvailableForDate_whenAvailable_returnsTrue() {
        // Given
        LocalDate date = LocalDate.now();
        when(repository.existsByGuideAndReservationDate(any(Guide.class), eq(date)))
            .thenReturn(true);

        // When
        boolean result = service.isGuideAvailableForDate(guide, date);

        // Then
        assertTrue(result);
        verify(repository).existsByGuideAndReservationDate(guide, date);
    }

    @Test
    @DisplayName("Should check if guide is not available for date")
    void isGuideAvailableForDate_whenNotAvailable_returnsFalse() {
        // Given
        LocalDate date = LocalDate.now();
        when(repository.existsByGuideAndReservationDate(any(Guide.class), eq(date)))
            .thenReturn(false);

        // When
        boolean result = service.isGuideAvailableForDate(guide, date);

        // Then
        assertFalse(result);
        verify(repository).existsByGuideAndReservationDate(guide, date);
    }

    @Test
    @DisplayName("Should update assignment status")
    void updateAssignmentStatus_success() {
        // Given
        Integer id = 1;
        String newStatus = "COMPLETED";
        when(repository.findById(id)).thenReturn(Optional.of(tourAssignment));
        when(repository.save(any(TourAssignment.class))).thenReturn(tourAssignment);

        // When
        TourAssignmentResponse result = service.updateAssignmentStatus(id, newStatus);

        // Then
        assertNotNull(result);
        assertEquals(newStatus, result.status());
        verify(repository).save(tourAssignmentCaptor.capture());

        TourAssignment updatedAssignment = tourAssignmentCaptor.getValue();
        assertEquals(newStatus, updatedAssignment.getStatus());
    }

    @Test
    @DisplayName("Should add notes to assignment")
    void addNotesToAssignment_success() {
        // Given
        Integer id = 1;
        String notes = "New notes";
        when(repository.findById(id)).thenReturn(Optional.of(tourAssignment));
        when(repository.save(any(TourAssignment.class))).thenReturn(tourAssignment);

        // When
        TourAssignmentResponse result = service.addNotesToAssignment(id, notes);

        // Then
        assertNotNull(result);
        assertEquals(notes, result.notes());
        verify(repository).save(tourAssignmentCaptor.capture());

        TourAssignment updatedAssignment = tourAssignmentCaptor.getValue();
        assertEquals(notes, updatedAssignment.getNotes());
    }

    @Test
    @DisplayName("Should throw exception when updating status of non-existent assignment")
    void updateAssignmentStatus_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> service.updateAssignmentStatus(id, "COMPLETED")
        );
        assertEquals("Assignment not found with id: " + id, exception.getMessage());
        verify(repository, never()).save(any(TourAssignment.class));
    }

    @Test
    @DisplayName("Should throw exception when adding notes to non-existent assignment")
    void addNotesToAssignment_whenNotExists_shouldThrowException() {
        // Given
        Integer id = 999;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When/Then
        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> service.addNotesToAssignment(id, "New notes")
        );
        assertEquals("Assignment not found with id: " + id, exception.getMessage());
        verify(repository, never()).save(any(TourAssignment.class));
    }
}
