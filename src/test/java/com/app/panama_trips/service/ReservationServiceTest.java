package com.app.panama_trips.service;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.exception.UserNotFoundException;
import com.app.panama_trips.persistence.entity.Reservation;
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

import javax.xml.crypto.Data;
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
}
