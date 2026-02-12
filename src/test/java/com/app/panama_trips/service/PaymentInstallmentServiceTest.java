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
import com.app.panama_trips.persistence.entity.PaymentInstallmentStatus;
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

  // Find Operations Tests
  @Test
  @DisplayName("Should find payment installments by reservation id")
  void findByReservationId_shouldReturnMatchingInstallments() {
    // Given
    Integer reservationId = 1;
    when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservationOneMock));
    when(repository.findByReservation(reservationOneMock)).thenReturn(paymentInstallments);

    // When
    List<PaymentInstallmentResponse> result = service.findByReservationId(reservationId);

    // Then
    assertNotNull(result);
    assertEquals(paymentInstallments.size(), result.size());
    verify(reservationRepository).findById(reservationId);
    verify(repository).findByReservation(reservationOneMock);
  }

  @Test
  @DisplayName("Should throw exception when finding by non-existent reservation id")
  void findByReservationId_withNonExistentReservation_shouldThrowException() {
    // Given
    Integer reservationId = 999;
    when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

    // When/Then
    ResourceNotFoundException exception = assertThrows(
        ResourceNotFoundException.class,
        () -> service.findByReservationId(reservationId));
    assertEquals("Reservation not found", exception.getMessage());
    verify(reservationRepository).findById(reservationId);
  }

  @Test
  @DisplayName("Should find payment installments by payment id")
  void findByPaymentId_shouldReturnMatchingInstallments() {
    // Given
    Integer paymentId = 1;
    when(repository.findByPayment_Id(paymentId)).thenReturn(paymentInstallments);

    // When
    List<PaymentInstallmentResponse> result = service.findByPaymentId(paymentId);

    // Then
    assertNotNull(result);
    assertEquals(paymentInstallments.size(), result.size());
    verify(repository).findByPayment_Id(paymentId);
  }

  @Test
  @DisplayName("Should find payment installments by status")
  void findByStatus_shouldReturnMatchingInstallments() {
    // Given
    String status = PaymentInstallmentStatus.PENDING.getCode();
    when(repository.findByStatus(status)).thenReturn(pendingInstallmentsListMock());

    // When
    List<PaymentInstallmentResponse> result = service.findByStatus(status);

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    verify(repository).findByStatus(status);
  }

  @Test
  @DisplayName("Should find payment installments by due date before")
  void findByDueDateBefore_shouldReturnMatchingInstallments() {
    // Given
    LocalDate date = LocalDate.now();
    when(repository.findByDueDateBefore(date)).thenReturn(overdueInstallmentsListMock());

    // When
    List<PaymentInstallmentResponse> result = service.findByDueDateBefore(date);

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    verify(repository).findByDueDateBefore(date);
  }

  @Test
  @DisplayName("Should find payment installments by due date between")
  void findByDueDateBetween_shouldReturnMatchingInstallments() {
    // Given
    LocalDate startDate = LocalDate.now().minusDays(10);
    LocalDate endDate = LocalDate.now().plusDays(10);
    when(repository.findByDueDateBetween(startDate, endDate)).thenReturn(paymentInstallments);

    // When
    List<PaymentInstallmentResponse> result = service.findByDueDateBetween(startDate, endDate);

    // Then
    assertNotNull(result);
    assertEquals(paymentInstallments.size(), result.size());
    verify(repository).findByDueDateBetween(startDate, endDate);
  }

  @Test
  @DisplayName("Should find payment installments by reminder sent")
  void findByReminderSent_shouldReturnMatchingInstallments() {
    // Given
    Boolean reminderSent = true;
    when(repository.findByReminderSent(reminderSent)).thenReturn(List.of(paymentInstallmentTwoMock()));

    // When
    List<PaymentInstallmentResponse> result = service.findByReminderSent(reminderSent);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(repository).findByReminderSent(reminderSent);
  }

  // Business Logic Operations Tests
  @Test
  @DisplayName("Should get overdue installments")
  void getOverdueInstallments_shouldReturnOverdueInstallments() {
    // Given
    String status = PaymentInstallmentStatus.OVERDUE.getCode();
    when(repository.findByStatus(status)).thenReturn(overdueInstallmentsListMock());

    // When
    List<PaymentInstallmentResponse> result = service.getOverdueInstallments();

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    verify(repository).findByStatus(status);
  }

  @Test
  @DisplayName("Should get pending installments")
  void getPendingInstallments_shouldReturnPendingInstallments() {
    // Given
    String status = PaymentInstallmentStatus.PENDING.getCode();
    when(repository.findByStatus(status)).thenReturn(pendingInstallmentsListMock());

    // When
    List<PaymentInstallmentResponse> result = service.getPendingInstallments();

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    verify(repository).findByStatus(status);
  }

  @Test
  @DisplayName("Should get paid installments")
  void getPaidInstallments_shouldReturnPaidInstallments() {
    // Given
    String status = PaymentInstallmentStatus.PAID.getCode();
    when(repository.findByStatus(status)).thenReturn(paidInstallmentsListMock());

    // When
    List<PaymentInstallmentResponse> result = service.getPaidInstallments();

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    verify(repository).findByStatus(status);
  }

  @Test
  @DisplayName("Should get cancelled installments")
  void getCancelledInstallments_shouldReturnCancelledInstallments() {
    // Given
    String status = PaymentInstallmentStatus.CANCELLED.getCode();
    when(repository.findByStatus(status)).thenReturn(cancelledInstallmentsListMock());

    // When
    List<PaymentInstallmentResponse> result = service.getCancelledInstallments();

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    verify(repository).findByStatus(status);
  }

  @Test
  @DisplayName("Should get installments requiring reminder")
  void getInstallmentsRequiringReminder_shouldReturnInstallmentsWithoutReminder() {
    // Given
    when(repository.findByReminderSent(false)).thenReturn(List.of(paymentInstallmentOneMock()));

    // When
    List<PaymentInstallmentResponse> result = service.getInstallmentsRequiringReminder();

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(repository).findByReminderSent(false);
  }

  // Specialized Queries Tests
  @Test
  @DisplayName("Should find payment installments by reservation id and status")
  void findByReservationIdAndStatus_shouldReturnMatchingInstallments() {
    // Given
    Integer reservationId = 1;
    String status = PaymentInstallmentStatus.PENDING.getCode();
    when(repository.findByReservationIdAndStatus(reservationId, status))
        .thenReturn(List.of(paymentInstallmentOneMock()));

    // When
    List<PaymentInstallmentResponse> result = service.findByReservationIdAndStatus(reservationId, status);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(repository).findByReservationIdAndStatus(reservationId, status);
  }

  @Test
  @DisplayName("Should find pending installments without reminder")
  void findPendingInstallmentsWithoutReminder_shouldReturnMatchingInstallments() {
    // Given
    LocalDate date = LocalDate.now();
    when(repository.findPendingInstallmentsWithoutReminder(date)).thenReturn(List.of(paymentInstallmentOneMock()));

    // When
    List<PaymentInstallmentResponse> result = service.findPendingInstallmentsWithoutReminder(date);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(repository).findPendingInstallmentsWithoutReminder(date);
  }

  @Test
  @DisplayName("Should sum pending amount by reservation")
  void sumPendingAmountByReservation_shouldReturnCorrectAmount() {
    // Given
    Integer reservationId = 1;
    BigDecimal expectedAmount = BigDecimal.valueOf(150.00);
    when(repository.sumPendingAmountByReservation(reservationId)).thenReturn(expectedAmount);

    // When
    BigDecimal result = service.sumPendingAmountByReservation(reservationId);

    // Then
    assertEquals(expectedAmount, result);
    verify(repository).sumPendingAmountByReservation(reservationId);
  }

  @Test
  @DisplayName("Should count overdue installments")
  void countOverdueInstallments_shouldReturnCorrectCount() {
    // Given
    String status = PaymentInstallmentStatus.OVERDUE.getCode();
    LocalDate date = LocalDate.now();
    when(repository.countOverdueInstallments(status, date)).thenReturn(2L);

    // When
    Long result = service.countOverdueInstallments(status, date);

    // Then
    assertEquals(2L, result);
    verify(repository).countOverdueInstallments(status, date);
  }

  // Bulk Operations Tests
  @Test
  @DisplayName("Should bulk create payment installments")
  void bulkCreatePaymentInstallments_success() {
    // Given
    List<PaymentInstallmentRequest> requests = List.of(request, request);
    when(reservationRepository.findById(anyInt())).thenReturn(Optional.of(reservationOneMock));

    // When
    service.bulkCreatePaymentInstallments(requests);

    // Then
    verify(reservationRepository, times(2)).findById(anyInt());
    verify(repository).saveAll(installmentsCaptor.capture());
    List<PaymentInstallment> savedInstallments = installmentsCaptor.getValue();
    assertEquals(2, savedInstallments.size());
  }

  @Test
  @DisplayName("Should bulk delete payment installments")
  void bulkDeletePaymentInstallments_success() {
    // Given
    List<Integer> installmentIds = List.of(1, 2, 3);

    // When
    service.bulkDeletePaymentInstallments(installmentIds);

    // Then
    verify(repository).deleteAllById(installmentIds);
  }

  @Test
  @DisplayName("Should bulk update status")
  void bulkUpdateStatus_success() {
    // Given
    List<Integer> installmentIds = List.of(1, 2);
    String newStatus = PaymentInstallmentStatus.PAID.getCode();
    when(repository.findAllById(installmentIds)).thenReturn(paymentInstallments);

    // When
    service.bulkUpdateStatus(installmentIds, newStatus);

    // Then
    verify(repository).findAllById(installmentIds);
    verify(repository).saveAll(paymentInstallments);
    paymentInstallments.forEach(installment -> assertEquals(newStatus, installment.getStatus()));
  }

  @Test
  @DisplayName("Should bulk mark as reminder sent")
  void bulkMarkAsReminderSent_success() {
    // Given
    List<Integer> installmentIds = List.of(1, 2);
    when(repository.findAllById(installmentIds)).thenReturn(paymentInstallments);

    // When
    service.bulkMarkAsReminderSent(installmentIds);

    // Then
    verify(repository).findAllById(installmentIds);
    verify(repository).saveAll(paymentInstallments);
    paymentInstallments.forEach(installment -> assertTrue(installment.getReminderSent()));
  }

  // Check Operations Tests
  @Test
  @DisplayName("Should check if payment installment exists by id")
  void existsById_whenExists_returnsTrue() {
    // Given
    Integer id = 1;
    when(repository.existsById(id)).thenReturn(true);

    // When
    boolean result = service.existsById(id);

    // Then
    assertTrue(result);
    verify(repository).existsById(id);
  }

  @Test
  @DisplayName("Should check if payment installment does not exist by id")
  void existsById_whenNotExists_returnsFalse() {
    // Given
    Integer id = 999;
    when(repository.existsById(id)).thenReturn(false);

    // When
    boolean result = service.existsById(id);

    // Then
    assertFalse(result);
    verify(repository).existsById(id);
  }

  @Test
  @DisplayName("Should check existence by reservation id")
  void existsByReservationId_shouldDelegateToRepository() {
    // Given
    Integer reservationId = 1;
    when(repository.existsByReservation_Id(reservationId)).thenReturn(true);

    // When
    boolean result = service.existsByReservationId(reservationId);

    // Then
    assertTrue(result);
    verify(repository).existsByReservation_Id(reservationId);
  }

  @Test
  @DisplayName("Should check existence by payment id")
  void existsByPaymentId_shouldDelegateToRepository() {
    // Given
    Integer paymentId = 1;
    when(repository.existsByPayment_Id(paymentId)).thenReturn(true);

    // When
    boolean result = service.existsByPaymentId(paymentId);

    // Then
    assertTrue(result);
    verify(repository).existsByPayment_Id(paymentId);
  }

  @Test
  @DisplayName("Should count by reservation id")
  void countByReservationId_shouldReturnCorrectCount() {
    // Given
    Integer reservationId = 1;
    when(repository.countByReservation_Id(reservationId)).thenReturn(2L);

    // When
    long result = service.countByReservationId(reservationId);

    // Then
    assertEquals(2L, result);
    verify(repository).countByReservation_Id(reservationId);
  }

  @Test
  @DisplayName("Should count by status")
  void countByStatus_shouldReturnCorrectCount() {
    // Given
    String status = PaymentInstallmentStatus.PENDING.getCode();
    when(repository.countByStatus(status)).thenReturn(2L);

    // When
    long result = service.countByStatus(status);

    // Then
    assertEquals(2, result);
    verify(repository).countByStatus(status);
  }

  // Financial Operations Tests
  @Test
  @DisplayName("Should calculate total amount for reservation")
  void calculateTotalAmountForReservation_shouldReturnCorrectAmount() {
    // Given
    Integer reservationId = 1;
    BigDecimal expectedAmount = BigDecimal.valueOf(100.00);
    when(repository.sumAmountByReservationId(reservationId)).thenReturn(expectedAmount);

    // When
    BigDecimal result = service.calculateTotalAmountForReservation(reservationId);

    // Then
    assertEquals(expectedAmount, result);
    verify(repository).sumAmountByReservationId(reservationId);
  }

  @Test
  @DisplayName("Should calculate total pending amount for reservation")
  void calculateTotalPendingAmountForReservation_shouldReturnCorrectAmount() {
    // Given
    Integer reservationId = 1;
    BigDecimal expectedAmount = BigDecimal.valueOf(150.00);
    when(repository.sumPendingAmountByReservation(reservationId)).thenReturn(expectedAmount);

    // When
    BigDecimal result = service.calculateTotalPendingAmountForReservation(reservationId);

    // Then
    assertEquals(expectedAmount, result);
    verify(repository).sumPendingAmountByReservation(reservationId);
  }

  @Test
  @DisplayName("Should calculate total overdue amount for reservation")
  void calculateTotalOverdueAmountForReservation_shouldReturnCorrectAmount() {
    // Given
    Integer reservationId = 1;
    String overdueStatus = PaymentInstallmentStatus.OVERDUE.getCode();
    BigDecimal expectedAmount = BigDecimal.valueOf(200.00);
    when(repository.sumAmountByReservationIdAndStatus(reservationId, overdueStatus)).thenReturn(expectedAmount);

    // When
    BigDecimal result = service.calculateTotalOverdueAmountForReservation(reservationId);

    // Then
    assertEquals(expectedAmount, result);
    verify(repository).sumAmountByReservationIdAndStatus(reservationId, overdueStatus);
  }

  @Test
  @DisplayName("Should calculate total amount by date range")
  void calculateTotalAmountByDateRange_shouldReturnCorrectAmount() {
    // Given
    LocalDate startDate = LocalDate.now().minusDays(10);
    LocalDate endDate = LocalDate.now().plusDays(10);
    when(repository.findByDueDateBetween(startDate, endDate)).thenReturn(paymentInstallments);

    // When
    BigDecimal result = service.calculateTotalAmountByDateRange(startDate, endDate);

    // Then
    BigDecimal expectedAmount = paymentInstallments.stream()
        .map(PaymentInstallment::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    assertEquals(expectedAmount, result);
    verify(repository).findByDueDateBetween(startDate, endDate);
  }

  // Statistics Tests
  @Test
  @DisplayName("Should get total installments count")
  void getTotalInstallments_shouldReturnCorrectCount() {
    // Given
    when(repository.count()).thenReturn(3L);

    // When
    long result = service.getTotalInstallments();

    // Then
    assertEquals(3L, result);
    verify(repository).count();
  }

  @Test
  @DisplayName("Should get total pending installments count")
  void getTotalPendingInstallments_shouldReturnCorrectCount() {
    // Given
    String status = PaymentInstallmentStatus.PENDING.getCode();
    when(repository.countByStatus(status)).thenReturn(2L);

    // When
    long result = service.getTotalPendingInstallments();

    // Then
    assertEquals(2, result);
    verify(repository).countByStatus(status);
  }

  @Test
  @DisplayName("Should get total amount pending")
  void getTotalAmountPending_shouldReturnCorrectAmount() {
    // Given
    String status = PaymentInstallmentStatus.PENDING.getCode();
    BigDecimal expectedAmount = BigDecimal.valueOf(150.00);
    when(repository.sumAmountByStatus(status)).thenReturn(expectedAmount);

    // When
    BigDecimal result = service.getTotalAmountPending();

    // Then
    assertEquals(expectedAmount, result);
    verify(repository).sumAmountByStatus(status);
  }

  @Test
  @DisplayName("Should calculate payment success rate")
  void getPaymentSuccessRate_shouldReturnCorrectRate() {
    // Given
    String paidStatus = PaymentInstallmentStatus.PAID.getCode();
    when(repository.count()).thenReturn(4L);
    when(repository.countByStatus(paidStatus)).thenReturn(2L);

    // When
    double result = service.getPaymentSuccessRate();

    // Then
    assertEquals(50.0, result);
    verify(repository).count();
    verify(repository).countByStatus(paidStatus);
  }

  @Test
  @DisplayName("Should return zero payment success rate when no installments")
  void getPaymentSuccessRate_whenNoInstallments_shouldReturnZero() {
    // Given
    when(repository.count()).thenReturn(0L);

    // When
    double result = service.getPaymentSuccessRate();

    // Then
    assertEquals(0.0, result);
    verify(repository).count();
  }

  // Status Management Tests
  @Test
  @DisplayName("Should mark installment as paid")
  void markAsPaid_shouldUpdateStatus() {
    // Given
    Integer id = 1;
    when(repository.findById(id)).thenReturn(Optional.of(paymentInstallment));
    when(repository.save(any(PaymentInstallment.class))).thenReturn(paymentInstallment);

    // When
    PaymentInstallmentResponse result = service.markAsPaid(id);

    // Then
    assertNotNull(result);
    verify(repository).findById(id);
    verify(repository).save(installmentCaptor.capture());
    PaymentInstallment savedInstallment = installmentCaptor.getValue();
    assertEquals(PaymentInstallmentStatus.PAID.getCode(), savedInstallment.getStatus());
  }

  @Test
  @DisplayName("Should mark installment as overdue")
  void markAsOverdue_shouldUpdateStatus() {
    // Given
    Integer id = 1;
    when(repository.findById(id)).thenReturn(Optional.of(paymentInstallment));
    when(repository.save(any(PaymentInstallment.class))).thenReturn(paymentInstallment);

    // When
    PaymentInstallmentResponse result = service.markAsOverdue(id);

    // Then
    assertNotNull(result);
    verify(repository).findById(id);
    verify(repository).save(installmentCaptor.capture());
    PaymentInstallment savedInstallment = installmentCaptor.getValue();
    assertEquals(PaymentInstallmentStatus.OVERDUE.getCode(), savedInstallment.getStatus());
  }

  @Test
  @DisplayName("Should mark installment as cancelled")
  void markAsCancelled_shouldUpdateStatus() {
    // Given
    Integer id = 1;
    when(repository.findById(id)).thenReturn(Optional.of(paymentInstallment));
    when(repository.save(any(PaymentInstallment.class))).thenReturn(paymentInstallment);

    // When
    PaymentInstallmentResponse result = service.markAsCancelled(id);

    // Then
    assertNotNull(result);
    verify(repository).findById(id);
    verify(repository).save(installmentCaptor.capture());
    PaymentInstallment savedInstallment = installmentCaptor.getValue();
    assertEquals(PaymentInstallmentStatus.CANCELLED.getCode(), savedInstallment.getStatus());
  }

  @Test
  @DisplayName("Should mark installment as pending")
  void markAsPending_shouldUpdateStatus() {
    // Given
    Integer id = 1;
    when(repository.findById(id)).thenReturn(Optional.of(paymentInstallment));
    when(repository.save(any(PaymentInstallment.class))).thenReturn(paymentInstallment);

    // When
    PaymentInstallmentResponse result = service.markAsPending(id);

    // Then
    assertNotNull(result);
    verify(repository).findById(id);
    verify(repository).save(installmentCaptor.capture());
    PaymentInstallment savedInstallment = installmentCaptor.getValue();
    assertEquals(PaymentInstallmentStatus.PENDING.getCode(), savedInstallment.getStatus());
  }

  // Reminder Operations Tests
  @Test
  @DisplayName("Should mark reminder as sent")
  void markReminderAsSent_shouldUpdateReminderSent() {
    // Given
    Integer id = 1;
    when(repository.findById(id)).thenReturn(Optional.of(paymentInstallment));
    when(repository.save(any(PaymentInstallment.class))).thenReturn(paymentInstallment);

    // When
    PaymentInstallmentResponse result = service.markReminderAsSent(id);

    // Then
    assertNotNull(result);
    verify(repository).findById(id);
    verify(repository).save(installmentCaptor.capture());
    PaymentInstallment savedInstallment = installmentCaptor.getValue();
    assertTrue(savedInstallment.getReminderSent());
  }

  @Test
  @DisplayName("Should get installments needing reminder")
  void getInstallmentsNeedingReminder_shouldReturnInstallmentsWithoutReminder() {
    // Given
    when(repository.findByReminderSent(false)).thenReturn(List.of(paymentInstallmentOneMock()));

    // When
    List<PaymentInstallmentResponse> result = service.getInstallmentsNeedingReminder();

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(repository).findByReminderSent(false);
  }

  // Utility Operations Tests
  @Test
  @DisplayName("Should recalculate overdue status")
  void recalculateOverdueStatus_shouldUpdateOverdueInstallments() {
    // Given
    String pendingStatus = PaymentInstallmentStatus.PENDING.getCode();
    List<PaymentInstallment> pendingOverdue = List.of(paymentInstallmentTwoMock());
    when(repository.findByStatusAndDueDateBefore(eq(pendingStatus), any(LocalDate.class)))
        .thenReturn(pendingOverdue);

    // When
    service.recalculateOverdueStatus();

    // Then
    verify(repository).findByStatusAndDueDateBefore(eq(pendingStatus), any(LocalDate.class));
    verify(repository).saveAll(pendingOverdue);
  }

  @Test
  @DisplayName("Should search installments by amount")
  void searchInstallmentsByAmount_shouldReturnMatchingInstallments() {
    // Given
    BigDecimal amount = BigDecimal.valueOf(100.00);
    when(repository.findByAmount(amount)).thenReturn(List.of(paymentInstallmentOneMock()));

    // When
    List<PaymentInstallmentResponse> result = service.searchInstallmentsByAmount(amount);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(repository).findByAmount(amount);
  }

  @Test
  @DisplayName("Should find latest installment by reservation")
  void findLatestInstallmentByReservation_shouldReturnLatestInstallment() {
    // Given
    Integer reservationId = 1;
    when(repository.findFirstByReservation_IdOrderByDueDateDesc(reservationId))
        .thenReturn(Optional.of(paymentInstallmentOneMock()));

    // When
    Optional<PaymentInstallmentResponse> result = service.findLatestInstallmentByReservation(reservationId);

    // Then
    assertTrue(result.isPresent());
    verify(repository).findFirstByReservation_IdOrderByDueDateDesc(reservationId);
  }

  @Test
  @DisplayName("Should get installments with late fees")
  void getInstallmentsWithLateFees_shouldReturnOverdueInstallments() {
    // Given
    String overdueStatus = PaymentInstallmentStatus.OVERDUE.getCode();
    when(repository.findByStatusAndDueDateBefore(eq(overdueStatus), any(LocalDate.class)))
        .thenReturn(overdueInstallmentsListMock());

    // When
    List<PaymentInstallmentResponse> result = service.getInstallmentsWithLateFees();

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    verify(repository).findByStatusAndDueDateBefore(eq(overdueStatus), any(LocalDate.class));
  }

  @Test
  @DisplayName("Should calculate late fee for overdue installment")
  void calculateLateFeeForInstallment_whenOverdue_shouldReturnLateFee() {
    // Given
    Integer id = 1;
    PaymentInstallment overdueInstallment = paymentInstallmentTwoMock();
    when(repository.findById(id)).thenReturn(Optional.of(overdueInstallment));

    // When
    BigDecimal result = service.calculateLateFeeForInstallment(id);

    // Then
    assertNotNull(result);
    assertTrue(result.compareTo(BigDecimal.ZERO) > 0);
    verify(repository).findById(id);
  }

  @Test
  @DisplayName("Should return zero late fee for non-overdue installment")
  void calculateLateFeeForInstallment_whenNotOverdue_shouldReturnZero() {
    // Given
    Integer id = 1;
    PaymentInstallment pendingInstallment = paymentInstallmentOneMock();
    when(repository.findById(id)).thenReturn(Optional.of(pendingInstallment));

    // When
    BigDecimal result = service.calculateLateFeeForInstallment(id);

    // Then
    assertEquals(BigDecimal.ZERO, result);
    verify(repository).findById(id);
  }

  // Advanced Queries Tests
  @Test
  @DisplayName("Should get recent installments with limit")
  void getRecentInstallments_shouldReturnLimitedResults() {
    // Given
    int limit = 2;
    when(repository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, limit)))
        .thenReturn(paymentInstallments.subList(0, 2));

    // When
    List<PaymentInstallmentResponse> result = service.getRecentInstallments(limit);

    // Then
    assertNotNull(result);
    assertEquals(2, result.size());
    verify(repository).findAllByOrderByCreatedAtDesc(PageRequest.of(0, limit));
  }

  @Test
  @DisplayName("Should get installments by amount range")
  void getInstallmentsByAmountRange_shouldReturnMatchingInstallments() {
    // Given
    BigDecimal minAmount = BigDecimal.valueOf(50.00);
    BigDecimal maxAmount = BigDecimal.valueOf(150.00);
    when(repository.findByAmountBetween(minAmount, maxAmount)).thenReturn(paymentInstallments);

    // When
    List<PaymentInstallmentResponse> result = service.getInstallmentsByAmountRange(minAmount, maxAmount);

    // Then
    assertNotNull(result);
    assertEquals(paymentInstallments.size(), result.size());
    verify(repository).findByAmountBetween(minAmount, maxAmount);
  }

  @Test
  @DisplayName("Should get installments by user")
  void getInstallmentsByUser_shouldReturnMatchingInstallments() {
    // Given
    Long userId = 1L;
    when(repository.findByReservationUserId(userId)).thenReturn(paymentInstallments);

    // When
    List<PaymentInstallmentResponse> result = service.getInstallmentsByUser(userId);

    // Then
    assertNotNull(result);
    assertEquals(paymentInstallments.size(), result.size());
    verify(repository).findByReservationUserId(userId);
  }

  @Test
  @DisplayName("Should get installments by tour plan")
  void getInstallmentsByTourPlan_shouldReturnMatchingInstallments() {
    // Given
    Integer tourPlanId = 1;
    when(repository.findByReservationTourPlanId(tourPlanId)).thenReturn(paymentInstallments);

    // When
    List<PaymentInstallmentResponse> result = service.getInstallmentsByTourPlan(tourPlanId);

    // Then
    assertNotNull(result);
    assertEquals(paymentInstallments.size(), result.size());
    verify(repository).findByReservationTourPlanId(tourPlanId);
  }

  @Test
  @DisplayName("Should cleanup old installments")
  void cleanupOldInstallments_shouldDeleteOldRecords() {
    // Given
    int daysToKeep = 30;
    List<PaymentInstallment> oldInstallments = List.of(paymentInstallmentTwoMock());
    when(repository.findByDueDateBefore(any(LocalDate.class))).thenReturn(oldInstallments);

    // When
    service.cleanupOldInstallments(daysToKeep);

    // Then
    verify(repository).findByDueDateBefore(any(LocalDate.class));
    verify(repository).deleteAll(oldInstallments);
  }
}
