package com.app.panama_trips.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import com.app.panama_trips.persistence.entity.Payment;
import com.app.panama_trips.persistence.entity.PaymentInstallment;
import com.app.panama_trips.persistence.entity.Reservation;

public record PaymentInstallmentResponse(
        Integer id,
        Integer reservationId,
        String reservationCode,
        LocalDate reservationDate,
        BigDecimal amount,
        LocalDate dueDate,
        Integer paymentId,
        String paymentStatus,
        String status,
        Boolean reminderSent,
        LocalDate createdAt) {

    public PaymentInstallmentResponse(PaymentInstallment paymentInstallment) {
        this(
                paymentInstallment.getId(),
                extractReservationId(paymentInstallment.getReservation()),
                extractReservationCode(paymentInstallment.getReservation()),
                extractReservationDate(paymentInstallment.getReservation()),
                paymentInstallment.getAmount(),
                paymentInstallment.getDueDate(),
                extractPaymentId(paymentInstallment.getPayment()),
                extractPaymentStatus(paymentInstallment.getPayment()),
                paymentInstallment.getStatus(),
                paymentInstallment.getReminderSent(),
                paymentInstallment.getCreatedAt());
    }

    // Helper methods with proper Optional handling and descriptive names
    private static Integer extractReservationId(Reservation reservation) {
        return Optional.ofNullable(reservation)
                .map(Reservation::getId)
                .orElse(null);
    }

    private static String extractReservationCode(Reservation reservation) {
        return Optional.ofNullable(reservation)
                .map(res -> "RES-" + res.getId())
                .orElse(null);
    }

    private static LocalDate extractReservationDate(Reservation reservation) {
        return Optional.ofNullable(reservation)
                .map(Reservation::getReservationDate)
                .orElse(null);
    }

    private static Integer extractPaymentId(Payment payment) {
        return Optional.ofNullable(payment)
                .map(Payment::getId)
                .map(Long::intValue)
                .orElse(null);
    }

    private static String extractPaymentStatus(Payment payment) {
        return Optional.ofNullable(payment)
                .map(Payment::getPaymentStatus)
                .map(Enum::name)
                .orElse(null);
    }
}