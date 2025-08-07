package com.app.panama_trips.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.PaymentInstallment;
import com.app.panama_trips.persistence.entity.Payment;
import com.app.panama_trips.persistence.entity.Reservation;
import com.app.panama_trips.persistence.repository.PaymentInstallmentRepository;
import com.app.panama_trips.persistence.repository.PaymentRepository;
import com.app.panama_trips.persistence.repository.ReservationRepository;
import com.app.panama_trips.presentation.dto.PaymentInstallmentRequest;
import com.app.panama_trips.presentation.dto.PaymentInstallmentResponse;
import com.app.panama_trips.service.interfaces.IPaymentInstallmentService;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<PaymentInstallmentResponse> findByReservationId(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        return repository.findByReservation(reservation).stream()
                .map(PaymentInstallmentResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentInstallmentResponse> findByPaymentId(Integer paymentId) {
        // This method requires finding the payment first, but we'll implement a basic
        // version
        // that returns empty list since the repository doesn't have a direct
        // findByPaymentId method
        return List.of();
    }

    @Override
    public List<PaymentInstallmentResponse> findByStatus(String status) {
        return repository.findByStatus(status)
                .stream()
                .map(PaymentInstallmentResponse::new)
                .toList();
    }

    @Override
    public List<PaymentInstallmentResponse> findByDueDateBefore(LocalDate date) {
        return repository.findByDueDateBefore(date)
                .stream()
                .map(PaymentInstallmentResponse::new)
                .toList();
    }

    @Override
    public List<PaymentInstallmentResponse> findByDueDateBetween(LocalDate startDate, LocalDate endDate) {
        return repository.findByDueDateBetween(startDate, endDate)
                .stream()
                .map(PaymentInstallmentResponse::new)
                .toList();

    }

    @Override
    public List<PaymentInstallmentResponse> findByReminderSent(Boolean reminderSent) {
        return repository.findByReminderSent(reminderSent)
                .stream()
                .map(PaymentInstallmentResponse::new)
                .toList();

    }

    // Specialized queries from repository
    @Override
    public List<PaymentInstallmentResponse> findByReservationIdAndStatus(Integer reservationId, String status) {
        return repository.findByReservationIdAndStatus(reservationId, status)
                .stream()
                .map(PaymentInstallmentResponse::new)
                .toList();
    }

    @Override
    public List<PaymentInstallmentResponse> findPendingInstallmentsWithoutReminder(LocalDate date) {
        return repository.findPendingInstallmentsWithoutReminder(date)
                .stream()
                .map(PaymentInstallmentResponse::new)
                .toList();
    }

    @Override
    public BigDecimal sumPendingAmountByReservation(Integer reservationId) {
        return repository.sumPendingAmountByReservation(reservationId);
    }

    @Override
    public Long countOverdueInstallments(String status, LocalDate date) {
        return repository.countOverdueInstallments(status, date);
    }

    // Business logic operations
    @Override
    public List<PaymentInstallmentResponse> getOverdueInstallments() {
        return repository.findByStatus("OVERDUE")
                .stream()
                .map(PaymentInstallmentResponse::new)
                .toList();
    }

    @Override
    public List<PaymentInstallmentResponse> getPendingInstallments() {
        return repository.findByStatus("PENDING")
                .stream()
                .map(PaymentInstallmentResponse::new)
                .toList();
    }

    @Override
    public List<PaymentInstallmentResponse> getPaidInstallments() {
        return repository.findByStatus("PAID")
                .stream()
                .map(PaymentInstallmentResponse::new)
                .toList();
    }

    @Override
    public List<PaymentInstallmentResponse> getCancelledInstallments() {
        return repository.findByStatus("CANCELLED")
                .stream()
                .map(PaymentInstallmentResponse::new)
                .toList();
    }

    @Override
    public List<PaymentInstallmentResponse> getInstallmentsRequiringReminder() {
        return repository.findByReminderSent(false)
                .stream()
                .map(PaymentInstallmentResponse::new)
                .toList();
    }

    @Override
    public List<PaymentInstallmentResponse> getInstallmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return repository.findByDueDateBetween(startDate, endDate)
                .stream()
                .map(PaymentInstallmentResponse::new)
                .toList();
    }

    @Override
    public List<PaymentInstallmentResponse> getInstallmentsByReservationAndStatus(Integer reservationId,
            String status) {
        return repository.findByReservationIdAndStatus(reservationId, status)
                .stream()
                .map(PaymentInstallmentResponse::new)
                .toList();
    }

    // Advanced queries
    @Override
    public List<PaymentInstallmentResponse> getRecentInstallments(int limit) {
        // Basic implementation - return all installments and limit the result
        return repository.findAll()
                .stream()
                .limit(limit)
                .map(PaymentInstallmentResponse::new)
                .toList();
    }

    @Override
    public List<PaymentInstallmentResponse> getInstallmentsByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        // Basic implementation - filter all installments by amount range
        return repository.findAll()
                .stream()
                .filter(installment -> installment.getAmount().compareTo(minAmount) >= 0 &&
                        installment.getAmount().compareTo(maxAmount) <= 0)
                .map(PaymentInstallmentResponse::new)
                .toList();
    }

    @Override
    public List<PaymentInstallmentResponse> getInstallmentsByUser(Long userId) {
        // Basic implementation - filter all installments by user
        return repository.findAll()
                .stream()
                .filter(installment -> installment.getReservation().getUser().getId().equals(userId))
                .map(PaymentInstallmentResponse::new)
                .toList();
    }

    @Override
    public List<PaymentInstallmentResponse> getInstallmentsByTourPlan(Integer tourPlanId) {
        // Basic implementation - filter all installments by tour plan
        return repository.findAll()
                .stream()
                .filter(installment -> installment.getReservation().getTourPlan().getId().equals(tourPlanId))
                .map(PaymentInstallmentResponse::new)
                .toList();
    }

    // Bulk operations
    @Override
    public void bulkCreatePaymentInstallments(List<PaymentInstallmentRequest> requests) {
        List<PaymentInstallment> installments = requests.stream()
                .map(this::buildFromRequest)
                .toList();
        repository.saveAll(installments);
    }

    @Override
    public void bulkUpdatePaymentInstallments(List<PaymentInstallmentRequest> requests) {
        // Implementation would depend on how to identify which records to update
    }

    @Override
    public void bulkDeletePaymentInstallments(List<Integer> installmentIds) {
        repository.deleteAllById(installmentIds);
    }

    @Override
    public void bulkUpdateStatus(List<Integer> installmentIds, String newStatus) {
        List<PaymentInstallment> installments = repository.findAllById(installmentIds);
        installments.forEach(installment -> installment.setStatus(newStatus));
        repository.saveAll(installments);
    }

    @Override
    public void bulkMarkAsReminderSent(List<Integer> installmentIds) {
        List<PaymentInstallment> installments = repository.findAllById(installmentIds);
        installments.forEach(installment -> installment.setReminderSent(true));
        repository.saveAll(installments);
    }

    // Check operations
    @Override
    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByReservationId(Integer reservationId) {
        return repository.findAll().stream()
                .anyMatch(installment -> installment.getReservation().getId().equals(reservationId));
    }

    @Override
    public boolean existsByPaymentId(Integer paymentId) {
        return repository.findAll().stream()
                .anyMatch(installment -> installment.getPayment() != null &&
                        installment.getPayment().getId().equals(paymentId));
    }

    @Override
    public long countByReservationId(Integer reservationId) {
        return repository.findAll().stream()
                .filter(installment -> installment.getReservation().getId().equals(reservationId))
                .count();
    }

    @Override
    public long countByStatus(String status) {
        return repository.findByStatus(status).size();
    }

    @Override
    public long countByDueDateBefore(LocalDate date) {
        return repository.findByDueDateBefore(date).size();
    }

    @Override
    public long countByReminderSent(Boolean reminderSent) {
        return repository.findByReminderSent(reminderSent).size();
    }

    // Financial operations
    @Override
    public BigDecimal calculateTotalAmountForReservation(Integer reservationId) {
        return repository.findAll()
                .stream()
                .filter(installment -> installment.getReservation().getId().equals(reservationId))
                .map(PaymentInstallment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calculateTotalPendingAmountForReservation(Integer reservationId) {
        return repository.sumPendingAmountByReservation(reservationId);
    }

    @Override
    public BigDecimal calculateTotalOverdueAmountForReservation(Integer reservationId) {
        return repository.findAll()
                .stream()
                .filter(installment -> installment.getReservation().getId().equals(reservationId) &&
                        "OVERDUE".equals(installment.getStatus()))
                .map(PaymentInstallment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calculateTotalLateFeesForReservation(Integer reservationId) {
        // Basic implementation - calculate late fees based on overdue days
        return repository.findAll()
                .stream()
                .filter(installment -> installment.getReservation().getId().equals(reservationId) &&
                        installment.isOverdue())
                .map(installment -> {
                    long daysOverdue = installment.getDaysOverdue();
                    return installment.getAmount().multiply(BigDecimal.valueOf(0.05))
                            .multiply(BigDecimal.valueOf(daysOverdue));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calculateTotalAmountByDateRange(LocalDate startDate, LocalDate endDate) {
        return repository.findByDueDateBetween(startDate, endDate)
                .stream()
                .map(PaymentInstallment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Statistics and analytics
    @Override
    public long getTotalInstallments() {
        return repository.count();
    }

    @Override
    public long getTotalPendingInstallments() {
        return repository.findByStatus("PENDING").size();
    }

    @Override
    public long getTotalPaidInstallments() {
        return repository.findByStatus("PAID").size();
    }

    @Override
    public long getTotalOverdueInstallments() {
        return repository.findByStatus("OVERDUE").size();
    }

    @Override
    public long getTotalCancelledInstallments() {
        return repository.findByStatus("CANCELLED").size();
    }

    @Override
    public BigDecimal getTotalAmountPending() {
        return repository.findByStatus("PENDING")
                .stream()
                .map(PaymentInstallment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalAmountPaid() {
        return repository.findByStatus("PAID")
                .stream()
                .map(PaymentInstallment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalAmountOverdue() {
        return repository.findByStatus("OVERDUE")
                .stream()
                .map(PaymentInstallment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalLateFees() {
        return repository.findAll()
                .stream()
                .filter(PaymentInstallment::isOverdue)
                .map(installment -> {
                    long daysOverdue = installment.getDaysOverdue();
                    return installment.getAmount().multiply(BigDecimal.valueOf(0.05))
                            .multiply(BigDecimal.valueOf(daysOverdue));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public double getPaymentSuccessRate() {
        long total = getTotalInstallments();
        if (total == 0)
            return 0.0;
        long paid = getTotalPaidInstallments();
        return (double) paid / total * 100;
    }

    @Override
    public List<PaymentInstallmentResponse> getTopReservationsByInstallmentCount(int limit) {
        // Basic implementation - return all installments limited by count
        return repository.findAll()
                .stream()
                .limit(limit)
                .map(PaymentInstallmentResponse::new)
                .toList();
    }

    @Override
    public List<PaymentInstallmentResponse> getInstallmentsByMonth() {
        // Basic implementation - return all installments
        return repository.findAll()
                .stream()
                .map(PaymentInstallmentResponse::new)
                .toList();
    }

    @Override
    public List<PaymentInstallmentResponse> getInstallmentsByDayOfWeek() {
        // Basic implementation - return all installments
        return repository.findAll()
                .stream()
                .map(PaymentInstallmentResponse::new)
                .toList();
    }

    // Status management operations
    @Override
    public PaymentInstallmentResponse markAsPaid(Integer installmentId) {
        PaymentInstallment installment = repository.findById(installmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment installment not found"));
        installment.setStatus("PAID");
        return new PaymentInstallmentResponse(repository.save(installment));
    }

    @Override
    public PaymentInstallmentResponse markAsOverdue(Integer installmentId) {
        PaymentInstallment installment = repository.findById(installmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment installment not found"));
        installment.setStatus("OVERDUE");
        return new PaymentInstallmentResponse(repository.save(installment));
    }

    @Override
    public PaymentInstallmentResponse markAsCancelled(Integer installmentId) {
        PaymentInstallment installment = repository.findById(installmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment installment not found"));
        installment.setStatus("CANCELLED");
        return new PaymentInstallmentResponse(repository.save(installment));
    }

    @Override
    public PaymentInstallmentResponse markAsPending(Integer installmentId) {
        PaymentInstallment installment = repository.findById(installmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment installment not found"));
        installment.setStatus("PENDING");
        return new PaymentInstallmentResponse(repository.save(installment));
    }

    @Override
    public boolean isValidStatusTransition(Integer installmentId, String newStatus) {
        return getValidStatusTransitions(installmentId).contains(newStatus);
    }

    @Override
    public List<String> getValidStatusTransitions(Integer installmentId) {
        PaymentInstallment installment = repository.findById(installmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment installment not found"));

        return switch (installment.getStatus()) {
            case "PENDING" -> List.of("PAID", "OVERDUE", "CANCELLED");
            case "OVERDUE" -> List.of("PAID", "CANCELLED");
            case "PAID" -> List.of("CANCELLED");
            case "CANCELLED" -> List.of();
            default -> List.of();
        };
    }

    // Reminder operations
    @Override
    public PaymentInstallmentResponse markReminderAsSent(Integer installmentId) {
        PaymentInstallment installment = repository.findById(installmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment installment not found"));
        installment.setReminderSent(true);
        return new PaymentInstallmentResponse(repository.save(installment));
    }

    @Override
    public List<PaymentInstallmentResponse> getInstallmentsNeedingReminder() {
        return repository.findByReminderSent(false)
                .stream()
                .map(PaymentInstallmentResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public void sendRemindersForDueInstallments() {
        // Implementation for sending reminders
    }

    @Override
    public void sendRemindersForOverdueInstallments() {
        // Implementation for sending overdue reminders
    }

    // Utility operations
    @Override
    public void recalculateOverdueStatus() {
        List<PaymentInstallment> pendingInstallments = repository.findByStatus("PENDING");
        LocalDate today = LocalDate.now();

        pendingInstallments.stream()
                .filter(installment -> installment.getDueDate().isBefore(today))
                .forEach(installment -> {
                    installment.setStatus("OVERDUE");
                    repository.save(installment);
                });
    }

    @Override
    public void cleanupOldInstallments(int daysToKeep) {
        // Basic implementation - no cleanup for now
    }

    @Override
    public List<PaymentInstallmentResponse> searchInstallmentsByAmount(BigDecimal amount) {
        return repository.findAll()
                .stream()
                .filter(installment -> installment.getAmount().equals(amount))
                .map(PaymentInstallmentResponse::new)
                .toList();
    }

    @Override
    public Optional<PaymentInstallmentResponse> findLatestInstallmentByReservation(Integer reservationId) {
        return repository.findAll()
                .stream()
                .filter(installment -> installment.getReservation().getId().equals(reservationId))
                .max((a, b) -> a.getDueDate().compareTo(b.getDueDate()))
                .map(PaymentInstallmentResponse::new);
    }

    @Override
    public List<PaymentInstallmentResponse> getInstallmentsWithLateFees() {
        return repository.findAll()
                .stream()
                .filter(PaymentInstallment::isOverdue)
                .map(PaymentInstallmentResponse::new)
                .toList();
    }

    @Override
    public BigDecimal calculateLateFeeForInstallment(Integer installmentId) {
        PaymentInstallment installment = repository.findById(installmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment installment not found"));

        if (installment.getStatus().equals("OVERDUE")) {
            LocalDate today = LocalDate.now();
            long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(installment.getDueDate(), today);
            return installment.getAmount().multiply(BigDecimal.valueOf(0.05)).multiply(BigDecimal.valueOf(daysOverdue));
        }
        return BigDecimal.ZERO;
    }

    // Helper methods
    private PaymentInstallment buildFromRequest(PaymentInstallmentRequest request) {
        return null;
    }

    private void updateFromRequest(PaymentInstallment existing, PaymentInstallmentRequest request) {

    }
}
