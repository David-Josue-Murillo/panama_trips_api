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
}
