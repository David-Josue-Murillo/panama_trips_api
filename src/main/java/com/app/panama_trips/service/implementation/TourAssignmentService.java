package com.app.panama_trips.service.implementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional(readOnly = true)
    public Page<TourAssignmentResponse> getAllAssignments(Pageable pageable) {
        return this.repository.findAll(pageable)
            .map(TourAssignmentResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TourAssignmentResponse> getAssignmentById(Integer id) {
        return this.repository.findById(id)
            .map(TourAssignmentResponse::new);
    }

    @Override
    @Transactional
    public TourAssignmentResponse createAssignment(TourAssignmentRequest request) {
        validateRequest(request);
        TourAssignment newAssignment = buildFromRequest(request);
        return new TourAssignmentResponse(this.repository.save(newAssignment));
    }

    @Override
    @Transactional
    public TourAssignmentResponse updateAssignment(Integer id,TourAssignmentRequest request) {
        TourAssignment existingAssignment = this.repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("TourAssignment not found with id: " + id));
        updateFromRequest(existingAssignment, request);
        return new TourAssignmentResponse(this.repository.save(existingAssignment));
    }

    @Override
    @Transactional
    public void deleteAssignment(Integer id) {
        if(!this.repository.existsById(id)) {
            throw new ResourceNotFoundException("TourAssignment not found with id: " + id);
        }
        this.repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourAssignmentResponse> getAssignmentsByGuide(Guide guide) {
        return this.repository.findByGuide(guide)
            .stream()
            .map(TourAssignmentResponse::new)
            .toList();
    }    

    @Override
    @Transactional(readOnly = true)
    public List<TourAssignmentResponse> getAssignmentsByTourPlan(TourPlan tourPlan) {
        return this.repository.findByTourPlan(tourPlan)
            .stream()
            .map(TourAssignmentResponse::new)
            .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TourAssignmentResponse> getAssignmentsByStatus(String status) {
        return this.repository.findByStatus(status)
            .stream()
            .map(TourAssignmentResponse::new)
            .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TourAssignmentResponse> getAssignmentsByDate(LocalDate date) {
        return this.repository.findByReservationDate(date)
            .stream()
            .map(TourAssignmentResponse::new)
            .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TourAssignmentResponse> getAssignmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return this.repository.findByReservationDateBetween(startDate, endDate)
            .stream()
            .map(TourAssignmentResponse::new)
            .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TourAssignmentResponse> getAssignmentsByGuideAndStatus(Guide guide, String status) {
        return this.repository.findByGuideAndStatus(guide, status)
            .stream()
            .map(TourAssignmentResponse::new)
            .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TourAssignmentResponse> getAssignmentsByTourPlanAndDateRange(TourPlan tourPlan, LocalDate startDate, LocalDate endDate) {
        return this.repository.findByTourPlanAndReservationDateBetween(tourPlan, startDate, endDate)
            .stream()
            .map(TourAssignmentResponse::new)
            .toList();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<TourAssignmentResponse> getAssignmentByGuideTourPlanAndDate(Guide guide, TourPlan tourPlan, LocalDate date) {
        return this.repository.findByGuideAndTourPlanAndReservationDate(guide, tourPlan, date)
            .map(TourAssignmentResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TourAssignmentResponse> getUpcomingAssignmentsByGuide(Integer guideId, LocalDate startDate) {
        return this.repository.findUpcomingAssignmentsByGuide(guideId, startDate)
            .stream()
            .map(TourAssignmentResponse::new)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAssignmentsByGuideAndStatus(Integer guideId, String status) {
        return this.repository.countAssignmentsByGuideAndStatus(guideId, status);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isGuideAvailableForDate(Guide guide, LocalDate date) {
        return this.repository.existsByGuideAndReservationDate(guide, date);
    }

    @Override
    @Transactional
    public TourAssignmentResponse updateAssignmentStatus(Integer assignmentId, String newStatus) {
        TourAssignment assignment = repository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + assignmentId));
        assignment.setStatus(newStatus);
        return new TourAssignmentResponse(repository.save(assignment));
    }

    @Override
    @Transactional
    public TourAssignmentResponse addNotesToAssignment(Integer assignmentId, String notes) {
        TourAssignment assignment = repository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + assignmentId));
        assignment.setNotes(notes);
        return new TourAssignmentResponse(repository.save(assignment));
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
