package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.Payment;
import com.app.panama_trips.persistence.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long reservationId,
        String transactionId,
        BigDecimal amount,
        PaymentStatus paymentStatus,
        LocalDateTime createdAt,
        BigDecimal refundAmount,
        String refundReason,
        LocalDateTime refundDate,
        String paymentMethod,
        String paymentDetails
) {
    public PaymentResponse(Payment payment) {
        this(
                payment.getId(),
                payment.getReservationId() != null ? payment.getReservationId().getId() : null,
                payment.getTransactionId(),
                payment.getAmount(),
                payment.getPaymentStatus(),
                payment.getCreatedAt(),
                payment.getRefundAmount(),
                payment.getRefundReason(),
                payment.getRefundDate(),
                payment.getPaymentMethod(),
                payment.getPaymentDetails()
        );
    }
}
