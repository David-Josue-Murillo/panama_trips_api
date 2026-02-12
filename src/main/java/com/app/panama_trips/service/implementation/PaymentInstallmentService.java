package com.app.panama_trips.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.PaymentInstallment;
import com.app.panama_trips.persistence.entity.PaymentInstallmentBusinessLogic;
import com.app.panama_trips.persistence.entity.PaymentInstallmentStatus;
import com.app.panama_trips.persistence.entity.Payment;
import com.app.panama_trips.persistence.entity.Reservation;
import com.app.panama_trips.persistence.repository.PaymentInstallmentRepository;
import com.app.panama_trips.persistence.repository.PaymentRepository;
import com.app.panama_trips.persistence.repository.ReservationRepository;
import com.app.panama_trips.presentation.dto.PaymentInstallmentRequest;
import com.app.panama_trips.presentation.dto.PaymentInstallmentResponse;
import com.app.panama_trips.service.interfaces.IPaymentInstallmentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.app.panama_trips.persistence.entity.PaymentInstallmentConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentInstallmentService implements IPaymentInstallmentService {

    private final PaymentInstallmentRepository repository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;

    // CRUD operations
    @Override
    @Transactional(readOnly = true)
    public Page<PaymentInstallmentResponse> getAllPaymentInstallments(Pageable pageable) {
        return repository.findAll(pageable).map(PaymentInstallmentResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentInstallmentResponse getPaymentInstallmentById(Integer id) {
        return repository.findById(id)
                .map(PaymentInstallmentResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("Payment installment not found"));
    }

    @Override
    @Transactional
    public PaymentInstallmentResponse savePaymentInstallment(PaymentInstallmentRequest request) {
        PaymentInstallment installment = buildFromRequest(request);
        return new PaymentInstallmentResponse(repository.save(installment));
    }

    @Override
    @Transactional
    public PaymentInstallmentResponse updatePaymentInstallment(Integer id, PaymentInstallmentRequest request) {
        PaymentInstallment existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment installment not found"));
        updateFromRequest(existing, request);
        return new PaymentInstallmentResponse(repository.save(existing));
    }

    @Override
    @Transactional
    public void deletePaymentInstallment(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Payment installment not found");
        }
        repository.deleteById(id);
    }

    // Find operations by entity relationships
    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> findByReservationId(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        return toResponseList(repository.findByReservation(reservation));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> findByPaymentId(Integer paymentId) {
        return toResponseList(repository.findByPayment_Id(paymentId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> findByStatus(String status) {
        return toResponseList(repository.findByStatus(status));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> findByDueDateBefore(LocalDate date) {
        return toResponseList(repository.findByDueDateBefore(date));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> findByDueDateBetween(LocalDate startDate, LocalDate endDate) {
        return toResponseList(repository.findByDueDateBetween(startDate, endDate));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> findByReminderSent(Boolean reminderSent) {
        return toResponseList(repository.findByReminderSent(reminderSent));
    }

    // Specialized queries from repository
    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> findByReservationIdAndStatus(Integer reservationId, String status) {
        return toResponseList(repository.findByReservationIdAndStatus(reservationId, status));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> findPendingInstallmentsWithoutReminder(LocalDate date) {
        return toResponseList(repository.findPendingInstallmentsWithoutReminder(date));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal sumPendingAmountByReservation(Integer reservationId) {
        return repository.sumPendingAmountByReservation(reservationId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countOverdueInstallments(String status, LocalDate date) {
        return repository.countOverdueInstallments(status, date);
    }

    // Business logic operations
    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> getOverdueInstallments() {
        return toResponseList(repository.findByStatus(PaymentInstallmentStatus.OVERDUE.getCode()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> getPendingInstallments() {
        return toResponseList(repository.findByStatus(PaymentInstallmentStatus.PENDING.getCode()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> getPaidInstallments() {
        return toResponseList(repository.findByStatus(PaymentInstallmentStatus.PAID.getCode()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> getCancelledInstallments() {
        return toResponseList(repository.findByStatus(PaymentInstallmentStatus.CANCELLED.getCode()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> getInstallmentsRequiringReminder() {
        return toResponseList(repository.findByReminderSent(false));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> getInstallmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return toResponseList(repository.findByDueDateBetween(startDate, endDate));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> getInstallmentsByReservationAndStatus(Integer reservationId, String status) {
        return toResponseList(repository.findByReservationIdAndStatus(reservationId, status));
    }

    // Advanced queries
    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> getRecentInstallments(int limit) {
        return toResponseList(repository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, limit)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> getInstallmentsByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        return toResponseList(repository.findByAmountBetween(minAmount, maxAmount));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> getInstallmentsByUser(Long userId) {
        return toResponseList(repository.findByReservationUserId(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> getInstallmentsByTourPlan(Integer tourPlanId) {
        return toResponseList(repository.findByReservationTourPlanId(tourPlanId));
    }

    // Bulk operations
    @Override
    @Transactional
    public void bulkCreatePaymentInstallments(List<PaymentInstallmentRequest> requests) {
        List<PaymentInstallment> installments = requests.stream()
                .map(this::buildFromRequest)
                .toList();
        repository.saveAll(installments);
    }

    @Override
    @Transactional
    public void bulkDeletePaymentInstallments(List<Integer> installmentIds) {
        repository.deleteAllById(installmentIds);
    }

    @Override
    @Transactional
    public void bulkUpdateStatus(List<Integer> installmentIds, String newStatus) {
        List<PaymentInstallment> installments = repository.findAllById(installmentIds);
        installments.forEach(installment -> installment.setStatus(newStatus));
        repository.saveAll(installments);
    }

    @Override
    @Transactional
    public void bulkMarkAsReminderSent(List<Integer> installmentIds) {
        List<PaymentInstallment> installments = repository.findAllById(installmentIds);
        installments.forEach(installment -> installment.setReminderSent(true));
        repository.saveAll(installments);
    }

    // Check operations
    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByReservationId(Integer reservationId) {
        return repository.existsByReservation_Id(reservationId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByPaymentId(Integer paymentId) {
        return repository.existsByPayment_Id(paymentId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByReservationId(Integer reservationId) {
        return repository.countByReservation_Id(reservationId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(String status) {
        return repository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByDueDateBefore(LocalDate date) {
        return repository.countByDueDateBefore(date);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByReminderSent(Boolean reminderSent) {
        return repository.countByReminderSent(reminderSent);
    }

    // Financial operations
    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalAmountForReservation(Integer reservationId) {
        return repository.sumAmountByReservationId(reservationId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalPendingAmountForReservation(Integer reservationId) {
        return repository.sumPendingAmountByReservation(reservationId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalOverdueAmountForReservation(Integer reservationId) {
        return repository.sumAmountByReservationIdAndStatus(reservationId, PaymentInstallmentStatus.OVERDUE.getCode());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalLateFeesForReservation(Integer reservationId) {
        List<PaymentInstallment> overdueInstallments = repository.findByReservationIdAndStatus(
                reservationId, PaymentInstallmentStatus.OVERDUE.getCode());
        return overdueInstallments.stream()
                .map(installment -> PaymentInstallmentBusinessLogic.calculateLateFee(
                        installment.getAmount(),
                        PaymentInstallmentBusinessLogic.calculateDaysOverdue(installment.getDueDate())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalAmountByDateRange(LocalDate startDate, LocalDate endDate) {
        return repository.findByDueDateBetween(startDate, endDate)
                .stream()
                .map(PaymentInstallment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Statistics and analytics
    @Override
    @Transactional(readOnly = true)
    public long getTotalInstallments() {
        return repository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalPendingInstallments() {
        return repository.countByStatus(PaymentInstallmentStatus.PENDING.getCode());
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalPaidInstallments() {
        return repository.countByStatus(PaymentInstallmentStatus.PAID.getCode());
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalOverdueInstallments() {
        return repository.countByStatus(PaymentInstallmentStatus.OVERDUE.getCode());
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalCancelledInstallments() {
        return repository.countByStatus(PaymentInstallmentStatus.CANCELLED.getCode());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalAmountPending() {
        return repository.sumAmountByStatus(PaymentInstallmentStatus.PENDING.getCode());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalAmountPaid() {
        return repository.sumAmountByStatus(PaymentInstallmentStatus.PAID.getCode());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalAmountOverdue() {
        return repository.sumAmountByStatus(PaymentInstallmentStatus.OVERDUE.getCode());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalLateFees() {
        List<PaymentInstallment> overdueInstallments = repository.findByStatus(PaymentInstallmentStatus.OVERDUE.getCode());
        return overdueInstallments.stream()
                .filter(PaymentInstallment::isOverdue)
                .map(installment -> PaymentInstallmentBusinessLogic.calculateLateFee(
                        installment.getAmount(),
                        PaymentInstallmentBusinessLogic.calculateDaysOverdue(installment.getDueDate())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public double getPaymentSuccessRate() {
        long total = getTotalInstallments();
        if (total == 0) return 0.0;
        long paid = getTotalPaidInstallments();
        return (double) paid / total * 100;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> getTopReservationsByInstallmentCount(int limit) {
        return toResponseList(repository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, limit)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> getInstallmentsByMonth() {
        return toResponseList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> getInstallmentsByDayOfWeek() {
        return toResponseList(repository.findAll());
    }

    // Status management operations
    @Override
    @Transactional
    public PaymentInstallmentResponse markAsPaid(Integer installmentId) {
        PaymentInstallment installment = findInstallmentOrFail(installmentId);
        installment.setStatus(PaymentInstallmentStatus.PAID.getCode());
        return new PaymentInstallmentResponse(repository.save(installment));
    }

    @Override
    @Transactional
    public PaymentInstallmentResponse markAsOverdue(Integer installmentId) {
        PaymentInstallment installment = findInstallmentOrFail(installmentId);
        installment.setStatus(PaymentInstallmentStatus.OVERDUE.getCode());
        return new PaymentInstallmentResponse(repository.save(installment));
    }

    @Override
    @Transactional
    public PaymentInstallmentResponse markAsCancelled(Integer installmentId) {
        PaymentInstallment installment = findInstallmentOrFail(installmentId);
        installment.setStatus(PaymentInstallmentStatus.CANCELLED.getCode());
        return new PaymentInstallmentResponse(repository.save(installment));
    }

    @Override
    @Transactional
    public PaymentInstallmentResponse markAsPending(Integer installmentId) {
        PaymentInstallment installment = findInstallmentOrFail(installmentId);
        installment.setStatus(PaymentInstallmentStatus.PENDING.getCode());
        return new PaymentInstallmentResponse(repository.save(installment));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValidStatusTransition(Integer installmentId, String newStatus) {
        return getValidStatusTransitions(installmentId).contains(newStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getValidStatusTransitions(Integer installmentId) {
        PaymentInstallment installment = findInstallmentOrFail(installmentId);
        String currentStatus = installment.getStatus();

        return switch (currentStatus) {
            case "PENDING" -> Arrays.asList(ALLOWED_PENDING_TRANSITIONS);
            case "OVERDUE" -> Arrays.asList(ALLOWED_OVERDUE_TRANSITIONS);
            case "PAID" -> Arrays.asList(ALLOWED_PAID_TRANSITIONS);
            case "CANCELLED" -> Arrays.asList(ALLOWED_CANCELLED_TRANSITIONS);
            default -> List.of();
        };
    }

    // Reminder operations
    @Override
    @Transactional
    public PaymentInstallmentResponse markReminderAsSent(Integer installmentId) {
        PaymentInstallment installment = findInstallmentOrFail(installmentId);
        installment.setReminderSent(true);
        return new PaymentInstallmentResponse(repository.save(installment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> getInstallmentsNeedingReminder() {
        return toResponseList(repository.findByReminderSent(false));
    }

    @Override
    public void sendRemindersForDueInstallments() {
        log.info("sendRemindersForDueInstallments: not yet implemented");
    }

    @Override
    public void sendRemindersForOverdueInstallments() {
        log.info("sendRemindersForOverdueInstallments: not yet implemented");
    }

    // Utility operations
    @Override
    @Transactional
    public void recalculateOverdueStatus() {
        List<PaymentInstallment> pendingOverdue = repository.findByStatusAndDueDateBefore(
                PaymentInstallmentStatus.PENDING.getCode(), LocalDate.now());
        pendingOverdue.forEach(installment -> installment.setStatus(PaymentInstallmentStatus.OVERDUE.getCode()));
        repository.saveAll(pendingOverdue);
    }

    @Override
    @Transactional
    public void cleanupOldInstallments(int daysToKeep) {
        LocalDate cutoffDate = LocalDate.now().minusDays(daysToKeep);
        List<PaymentInstallment> oldInstallments = repository.findByDueDateBefore(cutoffDate);
        repository.deleteAll(oldInstallments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> searchInstallmentsByAmount(BigDecimal amount) {
        return toResponseList(repository.findByAmount(amount));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentInstallmentResponse> findLatestInstallmentByReservation(Integer reservationId) {
        return repository.findFirstByReservation_IdOrderByDueDateDesc(reservationId)
                .map(PaymentInstallmentResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentInstallmentResponse> getInstallmentsWithLateFees() {
        return toResponseList(repository.findByStatusAndDueDateBefore(
                PaymentInstallmentStatus.OVERDUE.getCode(), LocalDate.now()));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateLateFeeForInstallment(Integer installmentId) {
        PaymentInstallment installment = findInstallmentOrFail(installmentId);

        if (PaymentInstallmentStatus.OVERDUE.getCode().equals(installment.getStatus())) {
            long daysOverdue = PaymentInstallmentBusinessLogic.calculateDaysOverdue(installment.getDueDate());
            return PaymentInstallmentBusinessLogic.calculateLateFee(installment.getAmount(), daysOverdue);
        }
        return BigDecimal.ZERO;
    }

    // Helper methods
    private PaymentInstallment findInstallmentOrFail(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment installment not found"));
    }

    private List<PaymentInstallmentResponse> toResponseList(List<PaymentInstallment> installments) {
        return installments.stream().map(PaymentInstallmentResponse::new).toList();
    }

    private PaymentInstallment buildFromRequest(PaymentInstallmentRequest request) {
        Reservation reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        Payment payment = null;
        if (request.paymentId() != null) {
            payment = paymentRepository.findById(request.paymentId().longValue())
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        }

        return PaymentInstallment.builder()
                .reservation(reservation)
                .payment(payment)
                .amount(request.amount())
                .dueDate(request.dueDate())
                .status(request.status())
                .reminderSent(request.reminderSent() != null ? request.reminderSent() : false)
                .build();
    }

    private void updateFromRequest(PaymentInstallment existing, PaymentInstallmentRequest request) {
        Reservation reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        Payment payment = null;
        if (request.paymentId() != null) {
            payment = paymentRepository.findById(request.paymentId().longValue())
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        }

        existing.setReservation(reservation);
        existing.setPayment(payment);
        existing.setAmount(request.amount());
        existing.setDueDate(request.dueDate());
        existing.setStatus(request.status());
        existing.setReminderSent(request.reminderSent() != null ? request.reminderSent() : false);
    }
}
