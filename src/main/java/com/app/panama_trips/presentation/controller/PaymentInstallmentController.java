package com.app.panama_trips.presentation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.panama_trips.presentation.dto.PaymentInstallmentRequest;
import com.app.panama_trips.presentation.dto.PaymentInstallmentResponse;
import com.app.panama_trips.service.implementation.PaymentInstallmentService;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payment-installments")
@RequiredArgsConstructor
public class PaymentInstallmentController {
    private final PaymentInstallmentService service;

    // CRUD operations
    @GetMapping
    public ResponseEntity<Page<PaymentInstallmentResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(service.getAllPaymentInstallments(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentInstallmentResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getPaymentInstallmentById(id));
    }

    @PostMapping
    public ResponseEntity<PaymentInstallmentResponse> create(@RequestBody PaymentInstallmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.savePaymentInstallment(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentInstallmentResponse> update(@PathVariable Integer id,
            @RequestBody PaymentInstallmentRequest request) {
        return ResponseEntity.ok(service.updatePaymentInstallment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deletePaymentInstallment(id);
        return ResponseEntity.noContent().build();
    }

    // Find operations by entity relationships
    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<List<PaymentInstallmentResponse>> findByReservationId(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(service.findByReservationId(reservationId));
    }

    @GetMapping("/payment/{paymentId}")
    public ResponseEntity<List<PaymentInstallmentResponse>> findByPaymentId(@PathVariable Integer paymentId) {
        return ResponseEntity.ok(service.findByPaymentId(paymentId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentInstallmentResponse>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.findByStatus(status));
    }

    @GetMapping("/due-before/{date}")
    public ResponseEntity<List<PaymentInstallmentResponse>> findByDueDateBefore(@PathVariable LocalDate date) {
        return ResponseEntity.ok(service.findByDueDateBefore(date));
    }

    @GetMapping("/due-between")
    public ResponseEntity<List<PaymentInstallmentResponse>> findByDueDateBetween(
            @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(service.findByDueDateBetween(startDate, endDate));
    }

    @GetMapping("/reminder-sent/{reminderSent}")
    public ResponseEntity<List<PaymentInstallmentResponse>> findByReminderSent(@PathVariable Boolean reminderSent) {
        return ResponseEntity.ok(service.findByReminderSent(reminderSent));
    }

    // Specialized queries
    @GetMapping("/reservation/{reservationId}/status/{status}")
    public ResponseEntity<List<PaymentInstallmentResponse>> findByReservationIdAndStatus(
            @PathVariable Integer reservationId, @PathVariable String status) {
        return ResponseEntity.ok(service.findByReservationIdAndStatus(reservationId, status));
    }

    @GetMapping("/pending-without-reminder/{date}")
    public ResponseEntity<List<PaymentInstallmentResponse>> findPendingInstallmentsWithoutReminder(
            @PathVariable LocalDate date) {
        return ResponseEntity.ok(service.findPendingInstallmentsWithoutReminder(date));
    }

    @GetMapping("/sum-pending/{reservationId}")
    public ResponseEntity<BigDecimal> sumPendingAmountByReservation(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(service.sumPendingAmountByReservation(reservationId));
    }

    @GetMapping("/count-overdue/{status}/{date}")
    public ResponseEntity<Long> countOverdueInstallments(@PathVariable String status, @PathVariable LocalDate date) {
        return ResponseEntity.ok(service.countOverdueInstallments(status, date));
    }

    // Business logic operations
    @GetMapping("/overdue")
    public ResponseEntity<List<PaymentInstallmentResponse>> getOverdueInstallments() {
        return ResponseEntity.ok(service.getOverdueInstallments());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<PaymentInstallmentResponse>> getPendingInstallments() {
        return ResponseEntity.ok(service.getPendingInstallments());
    }

    @GetMapping("/paid")
    public ResponseEntity<List<PaymentInstallmentResponse>> getPaidInstallments() {
        return ResponseEntity.ok(service.getPaidInstallments());
    }

    @GetMapping("/cancelled")
    public ResponseEntity<List<PaymentInstallmentResponse>> getCancelledInstallments() {
        return ResponseEntity.ok(service.getCancelledInstallments());
    }

    @GetMapping("/requiring-reminder")
    public ResponseEntity<List<PaymentInstallmentResponse>> getInstallmentsRequiringReminder() {
        return ResponseEntity.ok(service.getInstallmentsRequiringReminder());
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<PaymentInstallmentResponse>> getInstallmentsByDateRange(
            @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(service.getInstallmentsByDateRange(startDate, endDate));
    }

    @GetMapping("/reservation/{reservationId}/status/{status}/installments")
    public ResponseEntity<List<PaymentInstallmentResponse>> getInstallmentsByReservationAndStatus(
            @PathVariable Integer reservationId, @PathVariable String status) {
        return ResponseEntity.ok(service.getInstallmentsByReservationAndStatus(reservationId, status));
    }

    // Advanced queries
    @GetMapping("/recent/{limit}")
    public ResponseEntity<List<PaymentInstallmentResponse>> getRecentInstallments(@PathVariable int limit) {
        return ResponseEntity.ok(service.getRecentInstallments(limit));
    }

    @GetMapping("/amount-range")
    public ResponseEntity<List<PaymentInstallmentResponse>> getInstallmentsByAmountRange(
            @RequestParam BigDecimal minAmount, @RequestParam BigDecimal maxAmount) {
        return ResponseEntity.ok(service.getInstallmentsByAmountRange(minAmount, maxAmount));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentInstallmentResponse>> getInstallmentsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getInstallmentsByUser(userId));
    }

    @GetMapping("/tour-plan/{tourPlanId}")
    public ResponseEntity<List<PaymentInstallmentResponse>> getInstallmentsByTourPlan(
            @PathVariable Integer tourPlanId) {
        return ResponseEntity.ok(service.getInstallmentsByTourPlan(tourPlanId));
    }

    // Bulk operations
    @PostMapping("/bulk")
    public ResponseEntity<Void> bulkCreate(@RequestBody List<PaymentInstallmentRequest> requests) {
        service.bulkCreatePaymentInstallments(requests);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/bulk")
    public ResponseEntity<Void> bulkUpdate(@RequestBody List<PaymentInstallmentRequest> requests) {
        service.bulkUpdatePaymentInstallments(requests);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<Void> bulkDelete(@RequestBody List<Integer> installmentIds) {
        service.bulkDeletePaymentInstallments(installmentIds);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/bulk/status")
    public ResponseEntity<Void> bulkUpdateStatus(@RequestBody List<Integer> installmentIds,
            @RequestParam String newStatus) {
        service.bulkUpdateStatus(installmentIds, newStatus);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/bulk/reminder-sent")
    public ResponseEntity<Void> bulkMarkAsReminderSent(@RequestBody List<Integer> installmentIds) {
        service.bulkMarkAsReminderSent(installmentIds);
        return ResponseEntity.ok().build();
    }

    // Check operations
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> existsById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.existsById(id));
    }

    @GetMapping("/exists/reservation/{reservationId}")
    public ResponseEntity<Boolean> existsByReservationId(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(service.existsByReservationId(reservationId));
    }

    @GetMapping("/exists/payment/{paymentId}")
    public ResponseEntity<Boolean> existsByPaymentId(@PathVariable Integer paymentId) {
        return ResponseEntity.ok(service.existsByPaymentId(paymentId));
    }

    @GetMapping("/count/reservation/{reservationId}")
    public ResponseEntity<Long> countByReservationId(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(service.countByReservationId(reservationId));
    }

    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> countByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.countByStatus(status));
    }

    @GetMapping("/count/due-before/{date}")
    public ResponseEntity<Long> countByDueDateBefore(@PathVariable LocalDate date) {
        return ResponseEntity.ok(service.countByDueDateBefore(date));
    }

    @GetMapping("/count/reminder-sent/{reminderSent}")
    public ResponseEntity<Long> countByReminderSent(@PathVariable Boolean reminderSent) {
        return ResponseEntity.ok(service.countByReminderSent(reminderSent));
    }

    // Financial operations
    @GetMapping("/total-amount/reservation/{reservationId}")
    public ResponseEntity<BigDecimal> calculateTotalAmountForReservation(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(service.calculateTotalAmountForReservation(reservationId));
    }

    @GetMapping("/total-pending/reservation/{reservationId}")
    public ResponseEntity<BigDecimal> calculateTotalPendingAmountForReservation(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(service.calculateTotalPendingAmountForReservation(reservationId));
    }

    @GetMapping("/total-overdue/reservation/{reservationId}")
    public ResponseEntity<BigDecimal> calculateTotalOverdueAmountForReservation(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(service.calculateTotalOverdueAmountForReservation(reservationId));
    }

    @GetMapping("/total-late-fees/reservation/{reservationId}")
    public ResponseEntity<BigDecimal> calculateTotalLateFeesForReservation(@PathVariable Integer reservationId) {
        return ResponseEntity.ok(service.calculateTotalLateFeesForReservation(reservationId));
    }

    @GetMapping("/total-amount/date-range")
    public ResponseEntity<BigDecimal> calculateTotalAmountByDateRange(
            @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(service.calculateTotalAmountByDateRange(startDate, endDate));
    }

    // Statistics and analytics
    @GetMapping("/stats/total")
    public ResponseEntity<Long> getTotalInstallments() {
        return ResponseEntity.ok(service.getTotalInstallments());
    }

    @GetMapping("/stats/pending")
    public ResponseEntity<Long> getTotalPendingInstallments() {
        return ResponseEntity.ok(service.getTotalPendingInstallments());
    }

    @GetMapping("/stats/paid")
    public ResponseEntity<Long> getTotalPaidInstallments() {
        return ResponseEntity.ok(service.getTotalPaidInstallments());
    }

    @GetMapping("/stats/overdue")
    public ResponseEntity<Long> getTotalOverdueInstallments() {
        return ResponseEntity.ok(service.getTotalOverdueInstallments());
    }

    @GetMapping("/stats/cancelled")
    public ResponseEntity<Long> getTotalCancelledInstallments() {
        return ResponseEntity.ok(service.getTotalCancelledInstallments());
    }

    @GetMapping("/stats/amount-pending")
    public ResponseEntity<BigDecimal> getTotalAmountPending() {
        return ResponseEntity.ok(service.getTotalAmountPending());
    }

    @GetMapping("/stats/amount-paid")
    public ResponseEntity<BigDecimal> getTotalAmountPaid() {
        return ResponseEntity.ok(service.getTotalAmountPaid());
    }

    @GetMapping("/stats/amount-overdue")
    public ResponseEntity<BigDecimal> getTotalAmountOverdue() {
        return ResponseEntity.ok(service.getTotalAmountOverdue());
    }

    @GetMapping("/stats/late-fees")
    public ResponseEntity<BigDecimal> getTotalLateFees() {
        return ResponseEntity.ok(service.getTotalLateFees());
    }

    @GetMapping("/stats/success-rate")
    public ResponseEntity<Double> getPaymentSuccessRate() {
        return ResponseEntity.ok(service.getPaymentSuccessRate());
    }

    @GetMapping("/stats/top-reservations/{limit}")
    public ResponseEntity<List<PaymentInstallmentResponse>> getTopReservationsByInstallmentCount(
            @PathVariable int limit) {
        return ResponseEntity.ok(service.getTopReservationsByInstallmentCount(limit));
    }

    @GetMapping("/stats/by-month")
    public ResponseEntity<List<PaymentInstallmentResponse>> getInstallmentsByMonth() {
        return ResponseEntity.ok(service.getInstallmentsByMonth());
    }

    @GetMapping("/stats/by-day-of-week")
    public ResponseEntity<List<PaymentInstallmentResponse>> getInstallmentsByDayOfWeek() {
        return ResponseEntity.ok(service.getInstallmentsByDayOfWeek());
    }
}
