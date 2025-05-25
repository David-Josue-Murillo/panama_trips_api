package com.app.panama_trips.service.implementation;

import org.springframework.stereotype.Service;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Guide;
import com.app.panama_trips.persistence.entity.TourAssignment;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.repository.GuideRepository;
import com.app.panama_trips.persistence.repository.TourAssignmentRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.presentation.dto.TourAssignmentRequest;

import lombok.RequiredArgsConstructor;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TourAssignmentService {

    private final GuideRepository guideRepository;
    private final TourPlanRepository tourPlanRepository;
    private final TourAssignmentRepository repository;

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
