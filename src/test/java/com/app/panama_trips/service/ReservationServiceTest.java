package com.app.panama_trips.service;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.ResourceNotFoundException;
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

import java.util.Optional;

import static com.app.panama_trips.DataProvider.reservationListsMock;
import static com.app.panama_trips.DataProvider.reservationOneMock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

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
}
