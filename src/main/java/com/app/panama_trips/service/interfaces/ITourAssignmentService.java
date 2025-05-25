package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.Guide;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.presentation.dto.TourAssignmentRequest;
import com.app.panama_trips.presentation.dto.TourAssignmentResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITourAssignmentService {
    
    // Basic CRUD operations
    Page<TourAssignmentResponse> getAllAssignments(Pageable pageable);
    Optional<TourAssignmentResponse> getAssignmentById(Integer id);
    TourAssignmentResponse createAssignment(TourAssignmentRequest assignment);
    TourAssignmentResponse updateAssignment(TourAssignmentRequest assignment);
    void deleteAssignment(Integer id);
    
    // Business operations
    List<TourAssignmentResponse> getAssignmentsByGuide(Guide guide);
    List<TourAssignmentResponse> getAssignmentsByTourPlan(TourPlan tourPlan);
    List<TourAssignmentResponse> getAssignmentsByStatus(String status);
    List<TourAssignmentResponse> getAssignmentsByDate(LocalDate date);
    List<TourAssignmentResponse> getAssignmentsByDateRange(LocalDate startDate, LocalDate endDate);
    List<TourAssignmentResponse> getAssignmentsByGuideAndStatus(Guide guide, String status);
    List<TourAssignmentResponse> getAssignmentsByTourPlanAndDateRange(TourPlan tourPlan, LocalDate startDate, LocalDate endDate);
    Optional<TourAssignmentResponse> getAssignmentByGuideTourPlanAndDate(Guide guide, TourPlan tourPlan, LocalDate date);
    
    // Advanced operations
    List<TourAssignmentResponse> getUpcomingAssignmentsByGuide(Integer guideId, LocalDate startDate);
    Long countAssignmentsByGuideAndStatus(Integer guideId, String status);
    boolean isGuideAvailableForDate(Guide guide, LocalDate date);
    
    // Status management
    TourAssignmentResponse updateAssignmentStatus(Integer assignmentId, String newStatus);
    TourAssignmentResponse addNotesToAssignment(Integer assignmentId, String notes);
}
