package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.BusinessRuleException;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.exception.UnauthorizedException;
import com.app.panama_trips.exception.UserNotFoundException;
import com.app.panama_trips.persistence.entity.*;
import com.app.panama_trips.persistence.repository.ReservationRepository;
import com.app.panama_trips.persistence.repository.RoleRepository;
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
public class ReservationService implements IReservationService {

    private final ReservationRepository reservationRepository;
    private final UserEntityRepository userEntityRepository;
    private final TourPlanRepository tourPlanRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ReservationResponse> getAllReservations(Pageable pageable) {
        return this.reservationRepository.findAll(pageable).map(ReservationResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponse getReservationById(Integer id) {
        return this.reservationRepository.findById(id)
                .map(ReservationResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException(" "));
    }

    @Override
    @Transactional
    public ReservationResponse saveReservation(ReservationRequest reservationRequest) {
        validateReservation(reservationRequest);
        Reservation reservation = builderReservationFromRequest(reservationRequest);
        return new ReservationResponse(this.reservationRepository.save(reservation));
    }

    @Override
    @Transactional
    public ReservationResponse updateStatusReservation(Integer id, String status, String username) {
        ReservationStatus newStatus;

        // Validate the status
        try {
            newStatus = ReservationStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid reservation status: " + status);
        }

        // Check if the reservation exists
        Reservation reservation = this.reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation with id " + id + " not found"));

        UserEntity currentUser = this.userEntityRepository.findUserEntitiesByName(username)
                .orElseThrow(() -> new UserNotFoundException("User with name " + username + " not found"));

        if(!isAuthorizedToModifyReservation(currentUser, reservation)){
            throw new UnauthorizedException("You are not authorized to modify this reservation");
        }

        // Verify business rules for status change
        if (!canChangeToStatus(reservation, newStatus)) {
            throw new BusinessRuleException("Cannot change reservation from " +
                    reservation.getReservationStatus() + " to " + newStatus);
        }

        // Apply specific logic based on the new status
        switch (newStatus) {
            case CANCELLED:
                handleCancellation(reservation);
                break;
            case CONFIRMED:
                handleConfirmation(reservation);
                break;
            // otros casos según sea necesario
            default:
                reservation.setReservationStatus(newStatus);
        }

        return new ReservationResponse(this.reservationRepository.save(reservation));
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

    // Private Methods
    private void validateReservation(ReservationRequest reservationRequest) {
        TourPlan tourPlan = tourPlanRepository.findById(reservationRequest.tourPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Tour with ID: " + reservationRequest.tourPlanId() + " not found"));

        if(!this.userEntityRepository.existsById(reservationRequest.userId())) {
            throw new UserNotFoundException("User with id " + reservationRequest.userId() + " not found");
        }

        if (reservationRequest.reservationDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("The booking date cannot be in the past");
        }

        // Validate that the price is consistent with the tour price
        if (reservationRequest.totalPrice().compareTo(tourPlan.getPrice()) != 0) {
            throw new IllegalArgumentException("The booking price must match the tour price");
        }

        // Validate tour availability
        long reservationCount = reservationRepository.countByTourPlan_Id(reservationRequest.tourPlanId());
        if (reservationCount >= tourPlan.getAvailableSpots()) {
            throw new IllegalStateException("No hay cupos disponibles para este tour");
        }

        // Verify that the user does not already have a booking for this tour
        boolean existingReservation = reservationRepository
                .existsByUser_IdAndTourPlanId(reservationRequest.userId(), reservationRequest.tourPlanId());

        if (existingReservation) {
            throw new IllegalArgumentException("Ya tienes una reserva para este tour");
        }
    }

    private Reservation builderReservationFromRequest(ReservationRequest reservationRequest) {
        return Reservation.builder()
                .user(findUserEntityOrFail(reservationRequest.userId()))
                .tourPlan(findTourPlanOrFail(reservationRequest.tourPlanId()))
                .reservationStatus(ReservationStatus.PENDING)
                .reservationDate(reservationRequest.reservationDate())
                .totalPrice(reservationRequest.totalPrice())
                .build();
    }

    private UserEntity findUserEntityOrFail(Long userId) {
        return this.userEntityRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found"));
    }

    private TourPlan findTourPlanOrFail(Integer tourPlanId) {
        return this.tourPlanRepository.findById(tourPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour Plan with id: " + tourPlanId + " not found"));
    }

    // Business logic methods
    private boolean isAuthorizedToModifyReservation(UserEntity user, Reservation reservation) {
        // If admin, always allow
        // If the owner of the booking, allow
        // If the tour provider, allow certain changes

        return user.getRole_id().getRoleEnum() == RoleEnum.ADMIN ||
                reservation.getUser().getRole_id().equals(user.getRole_id()) ||
                user.getRole_id().getRoleEnum() == RoleEnum.OPERATOR;
    }

    private boolean canChangeToStatus(Reservation reservation, ReservationStatus newStatus) {
        ReservationStatus currentStatus = reservation.getReservationStatus();

        if (currentStatus == ReservationStatus.CONFIRMED && newStatus == ReservationStatus.CANCELLED) {
            return false;
        }

        // Validate cancellations by date
        if (newStatus == ReservationStatus.CANCELLED) {
            // Do not allow cancellation if there are less than X days left until the tour
            LocalDate tourDate = reservation.getTourPlan().getSeasonStartDate();
            return ChronoUnit.DAYS.between(LocalDate.now(), tourDate) >= 2;
        }

        return true;
    }

    private void handleCancellation(Reservation reservation) {
        // Pending logic for cancellations
    }

    private void handleConfirmation(Reservation reservation) {
        // Pending logic for confirmations
    }
}
