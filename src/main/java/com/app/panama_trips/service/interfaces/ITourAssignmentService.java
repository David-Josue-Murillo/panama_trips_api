package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.Guide;
import com.app.panama_trips.persistence.entity.TourAssignment;
import com.app.panama_trips.persistence.entity.TourPlan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITourAssignmentService {
    
    // Basic CRUD operations
    Page<TourAssignment> getAllAssignments(Pageable pageable);
    Optional<TourAssignment> getAssignmentById(Integer id);
    TourAssignment createAssignment(TourAssignment assignment);
    TourAssignment updateAssignment(TourAssignment assignment);
    void deleteAssignment(Integer id);
    
    // Business operations
    List<TourAssignment> getAssignmentsByGuide(Guide guide);
    List<TourAssignment> getAssignmentsByTourPlan(TourPlan tourPlan);
    List<TourAssignment> getAssignmentsByStatus(String status);
    List<TourAssignment> getAssignmentsByDate(LocalDate date);
    List<TourAssignment> getAssignmentsByDateRange(LocalDate startDate, LocalDate endDate);
    List<TourAssignment> getAssignmentsByGuideAndStatus(Guide guide, String status);
    List<TourAssignment> getAssignmentsByTourPlanAndDateRange(TourPlan tourPlan, LocalDate startDate, LocalDate endDate);
    Optional<TourAssignment> getAssignmentByGuideTourPlanAndDate(Guide guide, TourPlan tourPlan, LocalDate date);
    
    // Advanced operations
    List<TourAssignment> getUpcomingAssignmentsByGuide(Integer guideId, LocalDate startDate);
    Long countAssignmentsByGuideAndStatus(Integer guideId, String status);
    boolean isGuideAvailableForDate(Guide guide, LocalDate date);
    
    // Status management
    TourAssignment updateAssignmentStatus(Integer assignmentId, String newStatus);
    TourAssignment addNotesToAssignment(Integer assignmentId, String notes);
}
