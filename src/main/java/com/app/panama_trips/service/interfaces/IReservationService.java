package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.ReservationStatus;
import com.app.panama_trips.presentation.dto.ReservationRequest;
import com.app.panama_trips.presentation.dto.ReservationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface IReservationService {
    // CRUD operations
    Page<ReservationResponse> getAllReservations(Pageable pageable);
    ReservationResponse getReservationById(Integer id);
    ReservationResponse saveReservation(ReservationRequest reservationRequest);
    ReservationResponse updateStatusReservation(Integer id, String status, String username);
    void deleteReservation(Integer id);

    // Additional service methods
    Page<ReservationResponse> getReservationByUserId(Long userId, Pageable pageable);
    Page<ReservationResponse> getReservationByTourPlanId(Integer tourPlanId, Pageable pageable);
    Page<ReservationResponse> getReservationByReservationStatus(String reservationStatus, Pageable pageable);
    Page<ReservationResponse> getReservationByReservationDate(String reservationDate, Pageable pageable);

    // Specific searches
    Page<ReservationResponse> getReservationsByUserAndStatus(Long userId, String status, Pageable pageable);
    Page<ReservationResponse> getReservationsByTourPlanAndStatus(Integer tourPlanId, String status, Pageable pageable);

    // Searches by dates
    Page<ReservationResponse> getReservationsBetweenDates(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<ReservationResponse> getReservationsByMonth(short month, Pageable pageable);
    Page<ReservationResponse> getReservationsByYear(int year, Pageable pageable);

    // Searchers by price
    Page<ReservationResponse> getReservationsWithPriceGreaterThan(BigDecimal price, Pageable pageable);
    Page<ReservationResponse> getReservationsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    // Special searches
    Page<ReservationResponse> getRecentReservationsByUser(Long userId, LocalDate recentDate, Pageable pageable);
    Page<ReservationResponse> getReservationsByDayOfWeek(int dayOfWeek, Pageable pageable);
    Page<ReservationResponse> getReservationsByProvince(Integer ProvinceId, Pageable pageable);

    // Counting and statistical operations
    Long countReservationsByStatus(ReservationStatus status);
    Long countReservationsByTourPlan(Integer tourPlanId);
    Object[] getReservationStatistics();

    // Status change
    ReservationResponse cancelReservation(Integer id);
    ReservationResponse confirmReservation(Integer id);
}
