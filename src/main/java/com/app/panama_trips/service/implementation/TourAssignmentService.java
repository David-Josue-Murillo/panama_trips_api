package com.app.panama_trips.service.implementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Guide;
import com.app.panama_trips.persistence.entity.TourAssignment;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.repository.GuideRepository;
import com.app.panama_trips.persistence.repository.TourAssignmentRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.presentation.dto.TourAssignmentRequest;
import com.app.panama_trips.presentation.dto.TourAssignmentResponse;
import com.app.panama_trips.service.interfaces.ITourAssignmentService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TourAssignmentService implements ITourAssignmentService{

    private final GuideRepository guideRepository;
    private final TourPlanRepository tourPlanRepository;
    private final TourAssignmentRepository repository;

    public Page<TourAssignmentResponse> getAllAssignments(Pageable pageable) {
        return this.repository.findAll(pageable)
            .map(TourAssignmentResponse::new);
    }

    public Optional<TourAssignmentResponse> getAssignmentById(Integer assignmentId) {
        return this.repository.findById(assignmentId)
            .map(TourAssignmentResponse::new);
    }

    public TourAssignmentResponse createAssignment(TourAssignmentRequest assignment) {
        return null;
    }

    public TourAssignmentResponse updateAssignment(TourAssignmentRequest assignment) {
        return null;
    }

    public void deleteAssignment(Integer id) {
    }

    public List<TourAssignmentResponse> getAssignmentsByGuide(Guide guide) {
        return null;
    }    
    public List<TourAssignmentResponse> getAssignmentsByTourPlan(TourPlan tourPlan) {
        return null;
    }
    
    public List<TourAssignmentResponse> getAssignmentsByStatus(String status) {
        return null;
    }
    
    public List<TourAssignmentResponse> getAssignmentsByDate(LocalDate date) {
        return null;
    }
    
    public List<TourAssignmentResponse> getAssignmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return null;
    }
    
    public List<TourAssignmentResponse> getAssignmentsByGuideAndStatus(Guide guide, String status) {
        return null;
    }
    
    public List<TourAssignmentResponse> getAssignmentsByTourPlanAndDateRange(TourPlan tourPlan, LocalDate startDate, LocalDate endDate) {
        return null;
    }
    
    public Optional<TourAssignmentResponse> getAssignmentByGuideTourPlanAndDate(Guide guide, TourPlan tourPlan, LocalDate date) {
        return null;
    }

    public List<TourAssignmentResponse> getUpcomingAssignmentsByGuide(Integer guideId, LocalDate startDate) {
        return null;
    }

    public Long countAssignmentsByGuideAndStatus(Integer guideId, String status) {
        return null;
    }

    public boolean isGuideAvailableForDate(Guide guide, LocalDate date) {
        return false;
    }

    public TourAssignmentResponse updateAssignmentStatus(Integer assignmentId, String newStatus) {
        return null;
    }

    public TourAssignmentResponse addNotesToAssignment(Integer assignmentId, String notes) {
        return null;
    }

    // Private methods
    private void validateRequest(TourAssignmentRequest request) {
        Guide guide = findGuideOrFail(request.guideId());
        TourPlan tourPlan = findTourPlanOrFail(request.tourPlanId());

        // Validate guide availability
        if (repository.existsByGuideAndReservationDate(guide, request.reservationDate())) {
            throw new IllegalStateException("Guide is already assigned for this date");
        }

        // Validate that the reservation date is not in the past
        if (request.reservationDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Reservation date cannot be in the past");
        }

        // Validate that the guide is active (assuming there is a status field in Guide)
        if (!guide.getIsActive().equals(Boolean.TRUE)) {
            throw new IllegalStateException("Guide is not active");
        }

        // Validate that the tour plan is active
        if (!tourPlan.getStatus().equals("ACTIVE")) {
            throw new IllegalStateException("Tour plan is not active");
        }
    }

    private TourAssignment buildFromRequest(TourAssignmentRequest request) {
        return TourAssignment.builder()
            .guide(findGuideOrFail(request.guideId()))
            .tourPlan(findTourPlanOrFail(request.tourPlanId()))
            .reservationDate(request.reservationDate())
            .status(request.status())
            .notes(request.notes())
            .build();
    }

    private void updateFromRequest(TourAssignment assignment, TourAssignmentRequest request) {
        assignment.setGuide(findGuideOrFail(request.guideId()));
        assignment.setTourPlan(findTourPlanOrFail(request.tourPlanId()));
        assignment.setReservationDate(request.reservationDate());
        assignment.setStatus(request.status());
        assignment.setNotes(request.notes());
    }

    private Guide findGuideOrFail(Integer guideId) {
        return guideRepository.findById(guideId)
            .orElseThrow(() -> new ResourceNotFoundException("Guide not found with id: " + guideId));
    }

    private TourPlan findTourPlanOrFail(Integer tourPlanId) {
        return tourPlanRepository.findById(tourPlanId)
            .orElseThrow(() -> new ResourceNotFoundException("TourPlan not found with id: " + tourPlanId));
    }
}
