package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.PaymentStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentRequest(
        @NotNull(message = "Reservation ID is required")
        Integer reservationId,

        @NotBlank(message = "Transaction ID is required")
        @Size(max = 100, message = "Transaction ID must be less than 100 characters")
        String transactionId,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
        BigDecimal amount,

        PaymentStatus paymentStatus,

        @DecimalMin(value = "0.0", inclusive = true, message = "Refund amount must be zero or greater")
        BigDecimal refundAmount,

        String refundReason,

        LocalDateTime refundDate,

        @Size(max = 50, message = "Payment method must be less than 50 characters")
        String paymentMethod,

        String paymentDetails
) { }
