package com.app.panama_trips.presentation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.FutureOrPresent;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentInstallmentRequest(
    @NotNull(message = "Reservation ID is required") 
    Integer reservationId,

    @NotNull(message = "Amount is required") 
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0") 
    BigDecimal amount,

    @NotNull(message = "Due date is required") 
    @FutureOrPresent(message = "Due date must be today or in the future") 
    LocalDate dueDate,

    Integer paymentId,

    @Pattern(regexp = "^(PENDING|PAID|OVERDUE|CANCELLED)$", message = "Status must be one of: PENDING, PAID, OVERDUE, CANCELLED") 
    String status,

    Boolean reminderSent
) {}
