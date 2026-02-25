package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.BusinessRuleException;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.exception.UnauthorizedException;
import com.app.panama_trips.exception.UserNotFoundException;
import com.app.panama_trips.persistence.entity.*;
import com.app.panama_trips.persistence.repository.ReservationRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.presentation.dto.ReservationRequest;
import com.app.panama_trips.presentation.dto.ReservationResponse;
import com.app.panama_trips.service.interfaces.IReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService implements IReservationService {

    private final ReservationRepository reservationRepository;
    private final UserEntityRepository userEntityRepository;
    private final TourPlanRepository tourPlanRepository;

    @Override
    public Page<ReservationResponse> getAllReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable).map(ReservationResponse::new);
    }

    @Override
    public ReservationResponse getReservationById(Integer id) {
        return new ReservationResponse(findReservationOrThrow(id));
    }

    @Override
    @Transactional
    public ReservationResponse saveReservation(ReservationRequest reservationRequest) {
        TourPlan tourPlan = findTourPlanOrFail(reservationRequest.tourPlanId());
        UserEntity user = findUserEntityOrFail(reservationRequest.userId());
        validateReservation(reservationRequest, tourPlan);
        Reservation reservation = buildReservationFromRequest(reservationRequest, user, tourPlan);
        return new ReservationResponse(reservationRepository.save(reservation));
    }

    @Override
    @Transactional
    public ReservationResponse updateStatusReservation(Integer id, String status, String username) {
        ReservationStatus newStatus = getReservationStatus(status);

        Reservation reservation = findReservationOrThrow(id);

        UserEntity currentUser = userEntityRepository.findUserEntitiesByName(username)
                .orElseThrow(() -> new UserNotFoundException("User with name " + username + " not found"));

        if (!isAuthorizedToModifyReservation(currentUser, reservation)) {
            throw new UnauthorizedException("You are not authorized to modify this reservation");
        }

        if (!canChangeToStatus(reservation, newStatus)) {
            throw new BusinessRuleException("Cannot change reservation from " +
                    reservation.getReservationStatus() + " to " + newStatus);
        }

        switch (newStatus) {
            case cancelled:
                handleCancellation(reservation);
                break;
            case confirmed:
                handleConfirmation(reservation);
                break;
            default:
                reservation.setReservationStatus(newStatus);
        }

        return new ReservationResponse(reservationRepository.save(reservation));
    }

    @Override
    @Transactional
    public void deleteReservation(Integer id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation with id " + id + " not found");
        }
        reservationRepository.deleteById(id);
    }

    @Override
    public Page<ReservationResponse> getReservationByUserId(Long userId, Pageable pageable) {
        return reservationRepository.findRecentReservationsByUser(userId, LocalDate.now(), pageable)
                .map(ReservationResponse::new);
    }

    @Override
    public Page<ReservationResponse> getReservationByTourPlanId(Integer tourPlanId, Pageable pageable) {
        return reservationRepository.findByTourPlan_Id(tourPlanId, pageable)
                .map(ReservationResponse::new);
    }

    @Override
    public Page<ReservationResponse> getReservationByReservationStatus(String reservationStatus, Pageable pageable) {
        ReservationStatus status = getReservationStatus(reservationStatus);
        return reservationRepository.findByReservationStatus(status, pageable)
                .map(ReservationResponse::new);
    }

    @Override
    public Page<ReservationResponse> getReservationByReservationDate(String reservationDate, Pageable pageable) {
        LocalDate date;
        try {
            date = LocalDate.parse(reservationDate);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid reservation date: " + reservationDate);
        }
        return reservationRepository.findByReservationDate(date, pageable)
                .map(ReservationResponse::new);
    }

    @Override
    public Page<ReservationResponse> getReservationsByUserAndStatus(Long userId, String status, Pageable pageable) {
        ReservationStatus reservationStatus = getReservationStatus(status);
        return reservationRepository.findByUser_IdAndReservationStatus(userId, reservationStatus, pageable)
                .map(ReservationResponse::new);
    }

    @Override
    public Page<ReservationResponse> getReservationsByTourPlanAndStatus(Integer tourPlanId, String status, Pageable pageable) {
        ReservationStatus reservationStatus = getReservationStatus(status);
        return reservationRepository.findByTourPlan_IdAndReservationStatus(tourPlanId, reservationStatus, pageable)
                .map(ReservationResponse::new);
    }

    @Override
    public Page<ReservationResponse> getReservationsBetweenDates(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return reservationRepository.findByReservationDateBetween(startDate, endDate, pageable)
                .map(ReservationResponse::new);
    }

    @Override
    public Page<ReservationResponse> getReservationsByMonth(short month, Pageable pageable) {
        return reservationRepository.findByReservationDate_Month(month, pageable)
                .map(ReservationResponse::new);
    }

    @Override
    public Page<ReservationResponse> getReservationsByYear(int year, Pageable pageable) {
        return reservationRepository.findByReservationDate_Year(year, pageable)
                .map(ReservationResponse::new);
    }

    @Override
    public Page<ReservationResponse> getReservationsWithPriceGreaterThan(BigDecimal price, Pageable pageable) {
        return reservationRepository.findByTotalPriceGreaterThan(price, pageable)
                .map(ReservationResponse::new);
    }

    @Override
    public Page<ReservationResponse> getReservationsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return reservationRepository.findByTotalPriceBetween(minPrice, maxPrice, pageable)
                .map(ReservationResponse::new);
    }

    @Override
    public Page<ReservationResponse> getRecentReservationsByUser(Long userId, LocalDate recentDate, Pageable pageable) {
        return reservationRepository.findRecentReservationsByUser(userId, recentDate, pageable)
                .map(ReservationResponse::new);
    }

    @Override
    public Page<ReservationResponse> getReservationsByDayOfWeek(int dayOfWeek, Pageable pageable) {
        return reservationRepository.findByDayOfWeek(dayOfWeek, pageable)
                .map(ReservationResponse::new);
    }

    @Override
    public Page<ReservationResponse> getReservationsByProvince(Integer provinceId, Pageable pageable) {
        return reservationRepository.findByRegion(provinceId, pageable)
                .map(ReservationResponse::new);
    }

    @Override
    public Long countReservationsByStatus(ReservationStatus status) {
        return reservationRepository.countByReservationStatus(status);
    }

    @Override
    public Long countReservationsByTourPlan(Integer tourPlanId) {
        return reservationRepository.countByTourPlan_Id(tourPlanId);
    }

    @Override
    public Object[] getReservationStatistics() {
        return reservationRepository.getReservationStatistics();
    }

    @Override
    @Transactional
    public ReservationResponse cancelReservation(Integer id) {
        Reservation reservation = findReservationOrThrow(id);
        handleCancellation(reservation);
        return new ReservationResponse(reservationRepository.save(reservation));
    }

    @Override
    @Transactional
    public ReservationResponse confirmReservation(Integer id) {
        Reservation reservation = findReservationOrThrow(id);
        handleConfirmation(reservation);
        return new ReservationResponse(reservationRepository.save(reservation));
    }

    // Private helpers

    private Reservation findReservationOrThrow(Integer id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation with id " + id + " not found"));
    }

    private UserEntity findUserEntityOrFail(Long userId) {
        return userEntityRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
    }

    private TourPlan findTourPlanOrFail(Integer tourPlanId) {
        return tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour Plan with id " + tourPlanId + " not found"));
    }

    // Validation

    private void validateReservation(ReservationRequest request, TourPlan tourPlan) {
        if (request.reservationDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("The booking date cannot be in the past");
        }

        if (request.totalPrice().compareTo(tourPlan.getPrice()) != 0) {
            throw new IllegalArgumentException("The booking price must match the tour price");
        }

        long reservationCount = reservationRepository.countByTourPlan_Id(request.tourPlanId());
        if (reservationCount >= tourPlan.getAvailableSpots()) {
            throw new IllegalStateException("No hay cupos disponibles para este tour");
        }

        if (reservationRepository.existsByUser_IdAndTourPlanId(request.userId(), request.tourPlanId())) {
            throw new IllegalArgumentException("User with id " + request.userId() + " already reserved this tour");
        }
    }

    private Reservation buildReservationFromRequest(ReservationRequest request, UserEntity user, TourPlan tourPlan) {
        return Reservation.builder()
                .user(user)
                .tourPlan(tourPlan)
                .reservationStatus(ReservationStatus.pending)
                .reservationDate(request.reservationDate())
                .totalPrice(request.totalPrice())
                .build();
    }

    // Business logic

    private boolean isAuthorizedToModifyReservation(UserEntity user, Reservation reservation) {
        return user.getRole_id().getRoleEnum() == RoleEnum.ADMIN ||
                reservation.getUser().getId().equals(user.getId()) ||
                user.getRole_id().getRoleEnum() == RoleEnum.OPERATOR;
    }

    private boolean canChangeToStatus(Reservation reservation, ReservationStatus newStatus) {
        ReservationStatus currentStatus = reservation.getReservationStatus();

        if (currentStatus == ReservationStatus.confirmed && newStatus == ReservationStatus.cancelled) {
            return false;
        }

        if (newStatus == ReservationStatus.cancelled) {
            LocalDate tourDate = reservation.getTourPlan().getSeasonStartDate();
            return ChronoUnit.DAYS.between(LocalDate.now(), tourDate) >= 2;
        }

        return true;
    }

    private void handleCancellation(Reservation reservation) {
        reservation.setReservationStatus(ReservationStatus.cancelled);
    }

    private void handleConfirmation(Reservation reservation) {
        reservation.setReservationStatus(ReservationStatus.confirmed);
    }

    private ReservationStatus getReservationStatus(String status) {
        try {
            return ReservationStatus.valueOf(status.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid reservation status: " + status);
        }
    }
}
