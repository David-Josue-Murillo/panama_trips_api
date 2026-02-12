package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.PaymentInstallmentRequest;
import com.app.panama_trips.presentation.dto.PaymentInstallmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IPaymentInstallmentService {
    // CRUD operations
    Page<PaymentInstallmentResponse> getAllPaymentInstallments(Pageable pageable);
    PaymentInstallmentResponse getPaymentInstallmentById(Integer id);
    PaymentInstallmentResponse savePaymentInstallment(PaymentInstallmentRequest request);
    PaymentInstallmentResponse updatePaymentInstallment(Integer id, PaymentInstallmentRequest request);
    void deletePaymentInstallment(Integer id);

    // Find operations by entity relationships
    List<PaymentInstallmentResponse> findByReservationId(Integer reservationId);
    List<PaymentInstallmentResponse> findByPaymentId(Integer paymentId);
    List<PaymentInstallmentResponse> findByStatus(String status);
    List<PaymentInstallmentResponse> findByDueDateBefore(LocalDate date);
    List<PaymentInstallmentResponse> findByDueDateBetween(LocalDate startDate, LocalDate endDate);
    List<PaymentInstallmentResponse> findByReminderSent(Boolean reminderSent);

    // Specialized queries from repository
    List<PaymentInstallmentResponse> findByReservationIdAndStatus(Integer reservationId, String status);
    List<PaymentInstallmentResponse> findPendingInstallmentsWithoutReminder(LocalDate date);
    BigDecimal sumPendingAmountByReservation(Integer reservationId);
    Long countOverdueInstallments(String status, LocalDate date);

    // Business logic operations
    List<PaymentInstallmentResponse> getOverdueInstallments();
    List<PaymentInstallmentResponse> getPendingInstallments();
    List<PaymentInstallmentResponse> getPaidInstallments();
    List<PaymentInstallmentResponse> getCancelledInstallments();
    List<PaymentInstallmentResponse> getInstallmentsRequiringReminder();
    List<PaymentInstallmentResponse> getInstallmentsByDateRange(LocalDate startDate, LocalDate endDate);
    List<PaymentInstallmentResponse> getInstallmentsByReservationAndStatus(Integer reservationId, String status);

    // Advanced queries
    List<PaymentInstallmentResponse> getRecentInstallments(int limit);
    List<PaymentInstallmentResponse> getInstallmentsByAmountRange(BigDecimal minAmount, BigDecimal maxAmount);
    List<PaymentInstallmentResponse> getInstallmentsByUser(Long userId);
    List<PaymentInstallmentResponse> getInstallmentsByTourPlan(Integer tourPlanId);

    // Bulk operations
    void bulkCreatePaymentInstallments(List<PaymentInstallmentRequest> requests);
    void bulkDeletePaymentInstallments(List<Integer> installmentIds);
    void bulkUpdateStatus(List<Integer> installmentIds, String newStatus);
    void bulkMarkAsReminderSent(List<Integer> installmentIds);

    // Check operations
    boolean existsById(Integer id);
    boolean existsByReservationId(Integer reservationId);
    boolean existsByPaymentId(Integer paymentId);
    long countByReservationId(Integer reservationId);
    long countByStatus(String status);
    long countByDueDateBefore(LocalDate date);
    long countByReminderSent(Boolean reminderSent);

    // Financial operations
    BigDecimal calculateTotalAmountForReservation(Integer reservationId);
    BigDecimal calculateTotalPendingAmountForReservation(Integer reservationId);
    BigDecimal calculateTotalOverdueAmountForReservation(Integer reservationId);
    BigDecimal calculateTotalLateFeesForReservation(Integer reservationId);
    BigDecimal calculateTotalAmountByDateRange(LocalDate startDate, LocalDate endDate);

    // Statistics and analytics
    long getTotalInstallments();
    long getTotalPendingInstallments();
    long getTotalPaidInstallments();
    long getTotalOverdueInstallments();
    long getTotalCancelledInstallments();
    BigDecimal getTotalAmountPending();
    BigDecimal getTotalAmountPaid();
    BigDecimal getTotalAmountOverdue();
    BigDecimal getTotalLateFees();
    double getPaymentSuccessRate();
    List<PaymentInstallmentResponse> getTopReservationsByInstallmentCount(int limit);
    List<PaymentInstallmentResponse> getInstallmentsByMonth();
    List<PaymentInstallmentResponse> getInstallmentsByDayOfWeek();

    // Status management operations
    PaymentInstallmentResponse markAsPaid(Integer installmentId);
    PaymentInstallmentResponse markAsOverdue(Integer installmentId);
    PaymentInstallmentResponse markAsCancelled(Integer installmentId);
    PaymentInstallmentResponse markAsPending(Integer installmentId);
    boolean isValidStatusTransition(Integer installmentId, String newStatus);
    List<String> getValidStatusTransitions(Integer installmentId);

    // Reminder operations
    PaymentInstallmentResponse markReminderAsSent(Integer installmentId);
    List<PaymentInstallmentResponse> getInstallmentsNeedingReminder();
    void sendRemindersForDueInstallments();
    void sendRemindersForOverdueInstallments();

    // Utility operations
    void recalculateOverdueStatus();
    void cleanupOldInstallments(int daysToKeep);
    List<PaymentInstallmentResponse> searchInstallmentsByAmount(BigDecimal amount);
    Optional<PaymentInstallmentResponse> findLatestInstallmentByReservation(Integer reservationId);
    List<PaymentInstallmentResponse> getInstallmentsWithLateFees();
    BigDecimal calculateLateFeeForInstallment(Integer installmentId);
}
