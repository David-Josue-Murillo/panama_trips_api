package com.app.panama_trips.service;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.exception.UserNotFoundException;
import com.app.panama_trips.persistence.entity.Reservation;
import com.app.panama_trips.persistence.entity.ReservationStatus;
import com.app.panama_trips.persistence.repository.ReservationRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.presentation.dto.ReservationResponse;
import com.app.panama_trips.service.implementation.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static com.app.panama_trips.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private TourPlanRepository tourPlanRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void getAllReservations_shouldReturnAllReservations() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Reservation> page = new PageImpl<>(reservationListsMock, pageable, reservationListsMock.size());
        when(reservationRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<ReservationResponse> result = reservationService.getAllReservations(pageable);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(reservationListsMock.size(), result.getContent().size());
    }

    @Test
    void getReservationById_shouldReturnReservation() {
        // Given
        when(reservationRepository.findById(anyInt())).thenReturn(Optional.of(reservationOneMock));

        // When
        ReservationResponse result = reservationService.getReservationById(1);

        // Then
        assertNotNull(result);
        assertEquals(reservationOneMock.getId(), result.id());
    }

    @Test
    void getReservationById_shouldThrowException_whenReservationNotFound() {
        // Given
        when(reservationRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> reservationService.getReservationById(1));

        assertNotNull(exception);
        assertEquals("Reservation with id 1 not found", exception.getMessage());
    }

    @Test
    void saveReservation_shouldReturnSavedReservation() {
        // Given
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.of(tourPlanOneMock));
        when(userEntityRepository.existsById(anyLong())).thenReturn(true);
        when(reservationRepository.countByTourPlan_Id(anyInt())).thenReturn(1L);
        when(reservationRepository.existsByUser_IdAndTourPlanId(anyLong(), anyInt())).thenReturn(false);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservationOneMock);
        when(userEntityRepository.findById(anyLong())).thenReturn(Optional.of(userAdmin()));
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.of(tourPlanOneMock));

        // When
        ReservationResponse result = reservationService.saveReservation(reservationRequestMock);

        // Then
        assertNotNull(result);
        assertEquals(reservationOneMock.getId(), result.id());
    }

    @Test
    void saveReservation_shouldThrowException_whenUserNotFound() {
        // Given
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.of(tourPlanOneMock));
        when(userEntityRepository.existsById(anyLong())).thenReturn(false);

        // When & Then
        Exception exception = assertThrows(UserNotFoundException.class, () -> reservationService.saveReservation(reservationRequestMock));

        assertNotNull(exception);
        assertEquals("User with id 1 not found", exception.getMessage());
    }

    @Test
    void saveReservation_shouldThrowException_whenTourPlanNotFound() {
        // Given
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> reservationService.saveReservation(reservationRequestMock));

        assertNotNull(exception);
        assertEquals("Tour with ID: 1 not found", exception.getMessage());
    }

    @Test
    void saveReservation_shouldThrowException_whenUserAlreadyReserved() {
        // Given
        when(tourPlanRepository.findById(anyInt())).thenReturn(Optional.of(tourPlanOneMock));
        when(userEntityRepository.existsById(anyLong())).thenReturn(true);
        when(reservationRepository.existsByUser_IdAndTourPlanId(anyLong(), anyInt())).thenReturn(true);

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> reservationService.saveReservation(reservationRequestMock));

        assertNotNull(exception);
        assertEquals("User with id 1 already reserved this tour", exception.getMessage());
    }

    @Test
    void updateStatusReservation_shouldUpdateReservation() {
        // Given
        when(reservationRepository.findById(anyInt())).thenReturn(Optional.of(reservationOneMock));
        when(userEntityRepository.findUserEntitiesByName(anyString())).thenReturn(Optional.of(userAdmin()));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservationOneMock);

        // When
        ReservationResponse result = reservationService.updateStatusReservation(1, "pending", "admin");

        // Then
        assertNotNull(result);
        assertEquals(reservationOneMock.getId(), result.id());
    }

    @Test
    void updateStatusReservation_shouldThrowException_whenReservationNotFound() {
        // Given
        when(reservationRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> reservationService.updateStatusReservation(1, "pending", "admin"));

        assertNotNull(exception);
        assertEquals("Reservation with id 1 not found", exception.getMessage());
    }

    @Test
    void updateStatusReservation_shouldThrowException_whenUserNotFound() {
        // Given
        when(reservationRepository.findById(anyInt())).thenReturn(Optional.of(reservationOneMock));
        when(userEntityRepository.findUserEntitiesByName(anyString())).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(UserNotFoundException.class, () -> reservationService.updateStatusReservation(1, "pending", "admin"));

        assertNotNull(exception);
        assertEquals("User with name admin not found", exception.getMessage());
    }

    @Test
    void updateStatusReservation_shouldThrowException_whenStatusNotFound() {
        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> reservationService.updateStatusReservation(1, "invalidStatus", "admin"));

        assertNotNull(exception);
        assertEquals("Invalid reservation status: invalidStatus", exception.getMessage());
    }

    @Test
    void deleteReservation_shouldDeleteReservation() {
        // Given
        when(reservationRepository.existsById(anyInt())).thenReturn(true);

        // When
        reservationService.deleteReservation(1);

        // Then
        assertTrue(true);
        verify(reservationRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void getReservationByUserId_shouldReturnReservation() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Reservation> pageImpl = new PageImpl<>(reservationListsMock, pageable, reservationListsMock.size());
        when(reservationRepository.findRecentReservationsByUser(anyLong(), any(LocalDate.class), any(Pageable.class))).thenReturn(pageImpl);

        // When
        Page<ReservationResponse> result = reservationService.getReservationByUserId(1L, pageable);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(reservationListsMock.size(), result.getContent().size());
        assertEquals(reservationListsMock.getFirst().getId(), result.getContent().getFirst().id());
    }

    @Test
    void getReservationByTourPlanId_shouldReturnReservations() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Reservation> pageImpl = new PageImpl<>(reservationListsMock, pageable, reservationListsMock.size());
        when(reservationRepository.findByTourPlan_Id(anyInt(), any(Pageable.class))).thenReturn(pageImpl);

        // When
        Page<ReservationResponse> result = reservationService.getReservationByTourPlanId(1, pageable);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(reservationListsMock.size(), result.getContent().size());
    }

    @Test
    void getReservationByReservationStatus_shouldReturnReservations() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Reservation> pageImpl = new PageImpl<>(reservationListsMock, pageable, reservationListsMock.size());
        when(reservationRepository.findByReservationStatus(any(), any(Pageable.class))).thenReturn(pageImpl);

        // When
        Page<ReservationResponse> result = reservationService.getReservationByReservationStatus("pending", pageable);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(reservationListsMock.size(), result.getContent().size());
    }

    @Test
    void getReservationByReservationDate_shouldReturnReservations() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Reservation> pageImpl = new PageImpl<>(reservationListsMock, pageable, reservationListsMock.size());
        when(reservationRepository.findByReservationDate(any(LocalDate.class), any(Pageable.class))).thenReturn(pageImpl);

        // When
        Page<ReservationResponse> result = reservationService.getReservationByReservationDate("2023-10-01", pageable);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(reservationListsMock.size(), result.getContent().size());
    }

    @Test
    void getReservationsByUserAndStatus_shouldReturnReservations() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Reservation> pageImpl = new PageImpl<>(reservationListsMock, pageable, reservationListsMock.size());
        when(reservationRepository.findByUser_IdAndReservationStatus(anyLong(), any(), any(Pageable.class))).thenReturn(pageImpl);

        // When
        Page<ReservationResponse> result = reservationService.getReservationsByUserAndStatus(1L, "pending", pageable);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(reservationListsMock.size(), result.getContent().size());
    }

    @Test
    void getReservationsByTourPlanAndStatus_shouldReturnReservations() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Reservation> pageImpl = new PageImpl<>(reservationListsMock, pageable, reservationListsMock.size());
        when(reservationRepository.findByTourPlan_IdAndReservationStatus(anyInt(), any(), any(Pageable.class))).thenReturn(pageImpl);

        // When
        Page<ReservationResponse> result = reservationService.getReservationsByTourPlanAndStatus(1, "pending", pageable);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(reservationListsMock.size(), result.getContent().size());
    }

    @Test
    void getReservationsBetweenDates_shouldReturnReservations() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Reservation> pageImpl = new PageImpl<>(reservationListsMock, pageable, reservationListsMock.size());
        when(reservationRepository.findByReservationDateBetween(any(LocalDate.class), any(LocalDate.class), any(Pageable.class))).thenReturn(pageImpl);

        // When
        Page<ReservationResponse> result = reservationService.getReservationsBetweenDates(LocalDate.now(), LocalDate.now().plusDays(10), pageable);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(reservationListsMock.size(), result.getContent().size());
    }

    @Test
    void getReservationsByMonth_shouldReturnReservations() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Reservation> pageImpl = new PageImpl<>(reservationListsMock, pageable, reservationListsMock.size());
        when(reservationRepository.findByReservationDate_Month(anyShort(), any(Pageable.class))).thenReturn(pageImpl);

        // When
        Page<ReservationResponse> result = reservationService.getReservationsByMonth((short) 10, pageable);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(reservationListsMock.size(), result.getContent().size());
    }

    @Test
    void getReservationsByYear_shouldReturnReservations() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Reservation> pageImpl = new PageImpl<>(reservationListsMock, pageable, reservationListsMock.size());
        when(reservationRepository.findByReservationDate_Year(anyInt(), any(Pageable.class))).thenReturn(pageImpl);

        // When
        Page<ReservationResponse> result = reservationService.getReservationsByYear(2023, pageable);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(reservationListsMock.size(), result.getContent().size());
    }

    @Test
    void getReservationsWithPriceGreaterThan_shouldReturnReservations() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Reservation> pageImpl = new PageImpl<>(reservationListsMock, pageable, reservationListsMock.size());
        when(reservationRepository.findByTotalPriceGreaterThan(any(), any(Pageable.class))).thenReturn(pageImpl);

        // When
        Page<ReservationResponse> result = reservationService.getReservationsWithPriceGreaterThan(BigDecimal.valueOf(100), pageable);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(reservationListsMock.size(), result.getContent().size());
    }

    @Test
    void getReservationsByPriceRange_shouldReturnReservations() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Reservation> pageImpl = new PageImpl<>(reservationListsMock, pageable, reservationListsMock.size());
        when(reservationRepository.findByTotalPriceBetween(any(), any(), any(Pageable.class))).thenReturn(pageImpl);

        // When
        Page<ReservationResponse> result = reservationService.getReservationsByPriceRange(BigDecimal.valueOf(50), BigDecimal.valueOf(150), pageable);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(reservationListsMock.size(), result.getContent().size());
    }

    @Test
    void getReservationsByDayOfWeek_shouldReturnReservations() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Reservation> pageImpl = new PageImpl<>(reservationListsMock, pageable, reservationListsMock.size());
        when(reservationRepository.findByDayOfWeek(anyInt(), any(Pageable.class))).thenReturn(pageImpl);

        // When
        Page<ReservationResponse> result = reservationService.getReservationsByDayOfWeek(1, pageable);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(reservationListsMock.size(), result.getContent().size());
    }

    @Test
    void getReservationsByProvince_shouldReturnReservations() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Reservation> pageImpl = new PageImpl<>(reservationListsMock, pageable, reservationListsMock.size());
        when(reservationRepository.findByRegion(anyInt(), any(Pageable.class))).thenReturn(pageImpl);

        // When
        Page<ReservationResponse> result = reservationService.getReservationsByProvince(1, pageable);

        // Then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(reservationListsMock.size(), result.getContent().size());
    }

    @Test
    void countReservationsByStatus_shouldReturnCount() {
        // Given
        when(reservationRepository.countByReservationStatus(any())).thenReturn(5L);

        // When
        Long result = reservationService.countReservationsByStatus(ReservationStatus.pending);

        // Then
        assertNotNull(result);
        assertEquals(5L, result);
    }

    @Test
    void countReservationsByTourPlan_shouldReturnCount() {
        // Given
        when(reservationRepository.countByTourPlan_Id(anyInt())).thenReturn(10L);

        // When
        Long result = reservationService.countReservationsByTourPlan(1);

        // Then
        assertNotNull(result);
        assertEquals(10L, result);
    }

    @Test
    void getReservationStatistics_shouldReturnStatistics() {
        // Given
        Object[] statisticsMock = new Object[]{1, 2, 3};
        when(reservationRepository.getReservationStatistics()).thenReturn(statisticsMock);

        // When
        Object[] result = reservationService.getReservationStatistics();

        // Then
        assertNotNull(result);
        assertEquals(3, result.length);
        assertArrayEquals(statisticsMock, result);
    }

    @Test
    void cancelReservation_shouldCancelReservation() {
        // Given
        when(reservationRepository.findById(anyInt())).thenReturn(Optional.of(reservationOneMock));

        // When
        ReservationResponse result = reservationService.cancelReservation(1);

        // Then
        assertNotNull(result);
        assertEquals(reservationOneMock.getId(), result.id());
    }

    @Test
    void confirmReservation_shouldConfirmReservation() {
        // Given
        when(reservationRepository.findById(anyInt())).thenReturn(Optional.of(reservationOneMock));

        // When
        ReservationResponse result = reservationService.confirmReservation(1);

        // Then
        assertNotNull(result);
        assertEquals(reservationOneMock.getId(), result.id());
    }
}
