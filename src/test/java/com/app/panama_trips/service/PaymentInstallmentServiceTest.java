package com.app.panama_trips.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.PaymentInstallment;
import com.app.panama_trips.persistence.repository.PaymentInstallmentRepository;
import com.app.panama_trips.persistence.repository.PaymentRepository;
import com.app.panama_trips.persistence.repository.ReservationRepository;
import com.app.panama_trips.presentation.dto.PaymentInstallmentRequest;
import com.app.panama_trips.presentation.dto.PaymentInstallmentResponse;
import com.app.panama_trips.service.implementation.PaymentInstallmentService;

import static com.app.panama_trips.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentInstallmentServiceTest {

  @Mock
  private PaymentInstallmentRepository repository;

  @Mock
  private ReservationRepository reservationRepository;

  @Mock
  private PaymentRepository paymentRepository;

  @InjectMocks
  private PaymentInstallmentService service;

  @Captor
  private ArgumentCaptor<PaymentInstallment> installmentCaptor;

  @Captor
  private ArgumentCaptor<List<PaymentInstallment>> installmentsCaptor;

  private PaymentInstallment paymentInstallment;
  private PaymentInstallmentRequest request;
  private List<PaymentInstallment> paymentInstallments;

  @BeforeEach
  void setUp() {
    paymentInstallment = paymentInstallmentOneMock();
    request = paymentInstallmentRequestMock;
    paymentInstallments = paymentInstallmentListMock();
  }

  // CRUD Operations Tests
  @Test
  @DisplayName("Should return all payment installments when getAllPaymentInstallments is called with pagination")
  void getAllPaymentInstallments_shouldReturnAllData() {
    // Given
    Page<PaymentInstallment> page = new PageImpl<>(paymentInstallments);
    Pageable pageable = PageRequest.of(0, 10);

    when(repository.findAll(pageable)).thenReturn(page);

    // When
    Page<PaymentInstallmentResponse> response = service.getAllPaymentInstallments(pageable);

    // Then
    assertNotNull(response);
    assertEquals(paymentInstallments.size(), response.getTotalElements());
    verify(repository).findAll(pageable);
  }

  @Test
  @DisplayName("Should return payment installment by id when exists")
  void getPaymentInstallmentById_whenExists_shouldReturnInstallment() {
    // Given
    Integer id = 1;
    when(repository.findById(id)).thenReturn(Optional.of(paymentInstallment));

    // When
    PaymentInstallmentResponse result = service.getPaymentInstallmentById(id);

    // Then
    assertNotNull(result);
    assertEquals(paymentInstallment.getId(), result.id());
    assertEquals(paymentInstallment.getAmount(), result.amount());
    assertEquals(paymentInstallment.getStatus(), result.status());
    verify(repository).findById(id);
  }

  @Test
  @DisplayName("Should throw exception when getting payment installment by id that doesn't exist")
  void getPaymentInstallmentById_whenNotExists_shouldThrowException() {
    // Given
    Integer id = 999;
    when(repository.findById(id)).thenReturn(Optional.empty());

    // When/Then
    ResourceNotFoundException exception = assertThrows(
        ResourceNotFoundException.class,
        () -> service.getPaymentInstallmentById(id));
    assertEquals("Payment installment not found", exception.getMessage());
    verify(repository).findById(id);
  }

  @Test
  @DisplayName("Should save payment installment successfully")
  void savePaymentInstallment_success() {
    // Given
    when(reservationRepository.findById(request.reservationId())).thenReturn(Optional.of(reservationOneMock));
    when(repository.save(any(PaymentInstallment.class))).thenReturn(paymentInstallment);

    // When
    PaymentInstallmentResponse result = service.savePaymentInstallment(request);

    // Then
    assertNotNull(result);
    assertEquals(paymentInstallment.getId(), result.id());

    verify(repository).save(installmentCaptor.capture());
    PaymentInstallment savedInstallment = installmentCaptor.getValue();
    assertEquals(request.reservationId(), savedInstallment.getReservation().getId());
    assertEquals(request.amount(), savedInstallment.getAmount());
    assertEquals(request.dueDate(), savedInstallment.getDueDate());
    assertEquals(request.status(), savedInstallment.getStatus());
  }

  @Test
  @DisplayName("Should throw exception when saving payment installment with non-existent reservation")
  void savePaymentInstallment_withNonExistentReservation_shouldThrowException() {
    // Given
    when(reservationRepository.findById(request.reservationId())).thenReturn(Optional.empty());

    // When/Then
    ResourceNotFoundException exception = assertThrows(
        ResourceNotFoundException.class,
        () -> service.savePaymentInstallment(request));
    assertEquals("Reservation not found", exception.getMessage());
    verify(reservationRepository).findById(request.reservationId());
    verify(repository, never()).save(any(PaymentInstallment.class));
  }

  @Test
  @DisplayName("Should update payment installment successfully")
  void updatePaymentInstallment_success() {
    // Given
    Integer id = 1;
    PaymentInstallmentRequest updateRequest = new PaymentInstallmentRequest(
        2,
        BigDecimal.valueOf(200.00),
        LocalDate.now().plusDays(15),
        null,
        "PAID",
        true);

    when(repository.findById(id)).thenReturn(Optional.of(paymentInstallment));
    when(reservationRepository.findById(updateRequest.reservationId())).thenReturn(Optional.of(reservationTwoMock));
    when(repository.save(any(PaymentInstallment.class))).thenReturn(paymentInstallment);

    // When
    PaymentInstallmentResponse result = service.updatePaymentInstallment(id, updateRequest);

    // Then
    assertNotNull(result);

    verify(repository).findById(id);
    verify(repository).save(installmentCaptor.capture());
    PaymentInstallment updatedInstallment = installmentCaptor.getValue();
    assertEquals(updateRequest.reservationId(), updatedInstallment.getReservation().getId());
    assertEquals(updateRequest.amount(), updatedInstallment.getAmount());
    assertEquals(updateRequest.status(), updatedInstallment.getStatus());
  }

  @Test
  @DisplayName("Should throw exception when updating non-existent payment installment")
  void updatePaymentInstallment_whenNotExists_shouldThrowException() {
    // Given
    Integer id = 999;
    when(repository.findById(id)).thenReturn(Optional.empty());

    // When/Then
    ResourceNotFoundException exception = assertThrows(
        ResourceNotFoundException.class,
        () -> service.updatePaymentInstallment(id, request));
    assertEquals("Payment installment not found", exception.getMessage());
    verify(repository).findById(id);
    verify(repository, never()).save(any(PaymentInstallment.class));
  }

  @Test
  @DisplayName("Should delete payment installment successfully when exists")
  void deletePaymentInstallment_whenExists_success() {
    // Given
    Integer id = 1;
    when(repository.existsById(id)).thenReturn(true);

    // When
    service.deletePaymentInstallment(id);

    // Then
    verify(repository).existsById(id);
    verify(repository).deleteById(id);
  }

  @Test
  @DisplayName("Should throw exception when deleting non-existent payment installment")
  void deletePaymentInstallment_whenNotExists_shouldThrowException() {
    // Given
    Integer id = 999;
    when(repository.existsById(id)).thenReturn(false);

    // When/Then
    ResourceNotFoundException exception = assertThrows(
        ResourceNotFoundException.class,
        () -> service.deletePaymentInstallment(id));
    assertEquals("Payment installment not found", exception.getMessage());
    verify(repository).existsById(id);
    verify(repository, never()).deleteById(anyInt());
  }
}
