package com.app.panama_trips.service;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Payment;
import com.app.panama_trips.persistence.entity.PaymentStatus;
import com.app.panama_trips.persistence.entity.Reservation;
import com.app.panama_trips.persistence.repository.PaymentRepository;
import com.app.panama_trips.persistence.repository.ReservationRepository;
import com.app.panama_trips.presentation.dto.PaymentRequest;
import com.app.panama_trips.presentation.dto.PaymentResponse;
import com.app.panama_trips.service.implementation.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Payment paymentMock;
    private PaymentRequest paymentRequestMock;
    private Reservation reservationMock;

    @BeforeEach
    void setUp() {
        reservationMock = new Reservation();
        reservationMock.setId(1);

        paymentMock = new Payment();
        paymentMock.setId(1L);
        paymentMock.setReservationId(reservationMock);
        paymentMock.setTransactionId("TX123");
        paymentMock.setAmount(BigDecimal.valueOf(100.00));
        paymentMock.setPaymentStatus(PaymentStatus.PENDING);
        paymentMock.setCreatedAt(LocalDateTime.now());

        paymentRequestMock = new PaymentRequest(
                1,
                "TX123",
                BigDecimal.valueOf(100.00),
                PaymentStatus.PENDING,
                BigDecimal.ZERO,
                null,
                null,
                "CREDIT_CARD",
                null
        );
    }

    @Test
    void getAllPayments_shouldReturnAllRecords() {
        when(paymentRepository.findAll()).thenReturn(List.of(paymentMock));

        List<PaymentResponse> response = paymentService.getAllPayments();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("TX123", response.get(0).transactionId());
    }

    @Test
    void getPaymentById_shouldReturnOnePayment() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(paymentMock));

        PaymentResponse response = paymentService.getPaymentById(1L);

        assertNotNull(response);
        assertEquals(paymentMock.getId(), response.id());
    }

    @Test
    void getPaymentById_shouldThrowExceptionWhenNotFound() {
        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> paymentService.getPaymentById(99L));
    }

    @Test
    void savePayment_shouldSaveAndReturnPayment() {
        when(reservationRepository.findById(1)).thenReturn(Optional.of(reservationMock));
        when(paymentRepository.save(any(Payment.class))).thenReturn(paymentMock);

        PaymentResponse response = paymentService.savePayment(paymentRequestMock);

        assertNotNull(response);
        assertEquals(paymentMock.getTransactionId(), response.transactionId());
        verify(reservationRepository, times(1)).findById(1);
    }

    @Test
    void savePayment_shouldThrowExceptionWhenReservationNotFound() {
        when(reservationRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> paymentService.savePayment(paymentRequestMock));
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void updatePayment_shouldUpdateAndReturnPayment() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(paymentMock));
        when(paymentRepository.save(any(Payment.class))).thenReturn(paymentMock);

        PaymentResponse response = paymentService.updatePayment(1L, paymentRequestMock);

        assertNotNull(response);
        assertEquals(paymentMock.getId(), response.id());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void updatePayment_shouldUpdateReservationWhenReservationIdChanges() {
        PaymentRequest updateRequest = new PaymentRequest(
                2, "TX123", BigDecimal.valueOf(100.00), null, null, null, null, null, null
        );
        Reservation newRes = new Reservation();
        newRes.setId(2);
        
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(paymentMock));
        when(reservationRepository.findById(2)).thenReturn(Optional.of(newRes));
        when(paymentRepository.save(any(Payment.class))).thenReturn(paymentMock);

        PaymentResponse response = paymentService.updatePayment(1L, updateRequest);

        assertNotNull(response);
        verify(reservationRepository, times(1)).findById(2);
    }

    @Test
    void deletePayment_shouldDeletePayment() {
        when(paymentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(paymentRepository).deleteById(1L);

        paymentService.deletePayment(1L);

        verify(paymentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePayment_shouldThrowExceptionWhenPaymentNotExist() {
        when(paymentRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> paymentService.deletePayment(99L));
    }
}
