package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.ReservationStatus;
import com.app.panama_trips.persistence.repository.ReservationRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.presentation.dto.ReservationResponse;
import com.app.panama_trips.service.interfaces.IReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReservationService implements IReservationService {

    private final ReservationRepository reservationRepository;
    private final UserEntityRepository userEntityRepository;
    private final TourPlanRepository tourPlanRepository;

    @Override
    public Page<ReservationResponse> getAllReservations(Pageable pageable) {
        return this.reservationRepository.findAll(pageable).map(ReservationResponse::new);
    }

    @Override
    public ReservationResponse getReservationById(Integer id) {
        return this.reservationRepository.findById(id)
                .map(ReservationResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException(" "));
    }

    @Override
    public ReservationResponse saveReservation(ReservationResponse reservationResponse) {
        return null;
    }

    @Override
    public ReservationResponse updateReservation(Integer id, ReservationResponse reservationResponse) {
        return null;
    }

    @Override
    public void deleteReservation(Integer id) {

    }

    @Override
    public Page<ReservationResponse> getReservationByUserId(Long userId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ReservationResponse> getReservationByTourPlanId(Long tourPlanId, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ReservationResponse> getReservationByReservationStatus(String reservationStatus, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ReservationResponse> getReservationByReservationDate(String reservationDate, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ReservationResponse> getReservationsByUserAndStatus(Long userId, ReservationStatus status, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ReservationResponse> getReservationsByTourPlanAndStatus(Integer tourPlanId, ReservationStatus status, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ReservationResponse> getReservationsBetweenDates(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ReservationResponse> getReservationsByMonth(short month, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ReservationResponse> getReservationsByYear(int year, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ReservationResponse> getReservationsWithPriceGreaterThan(BigDecimal price, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ReservationResponse> getReservationsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ReservationResponse> getRecentReservationsByUser(Long userId, LocalDate recentDate, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ReservationResponse> getReservationsByDayOfWeek(int dayOfWeek, Pageable pageable) {
        return null;
    }

    @Override
    public Page<ReservationResponse> getReservationsByProvince(Integer ProvinceId, Pageable pageable) {
        return null;
    }

    @Override
    public Long countReservationsByStatus(ReservationStatus status) {
        return 0L;
    }

    @Override
    public Long countReservationsByTourPlan(Integer tourPlanId) {
        return 0L;
    }

    @Override
    public Object[] getReservationStatistics() {
        return new Object[0];
    }

    @Override
    public ReservationResponse cancelReservation(Integer id) {
        return null;
    }

    @Override
    public ReservationResponse confirmReservation(Integer id) {
        return null;
    }
}
