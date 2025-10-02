package com.app.panama_trips.persistence.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Business logic class for PaymentInstallment operations.
 * This class handles calculations, validations, and business rules
 * separate from the entity itself.
 */
public class PaymentInstallmentBusinessLogic {

    /**
     * Calculate if an installment is overdue based on its due date
     */
    public static boolean isOverdue(LocalDate dueDate) {
        return dueDate != null && dueDate.isBefore(LocalDate.now());
    }

    /**
     * Calculate days overdue for an installment
     */
    public static long calculateDaysOverdue(LocalDate dueDate) {
        if (dueDate == null || !isOverdue(dueDate)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    /**
     * Calculate days until due date
     */
    public static long calculateDaysUntilDue(LocalDate dueDate) {
        if (dueDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
    }

    /**
     * Validate if the amount is valid for a payment installment
     */
    public static boolean isValidAmount(BigDecimal amount) {
        return amount != null && amount.compareTo(PaymentInstallmentConstants.MINIMUM_AMOUNT) >= 0;
    }

    /**
     * Validate if the due date is valid (not in the past for new installments)
     */
    public static boolean isValidDueDate(LocalDate dueDate) {
        return dueDate != null && !dueDate.isBefore(LocalDate.now());
    }

    /**
     * Calculate late fee based on days overdue and original amount
     */
    public static BigDecimal calculateLateFee(BigDecimal originalAmount, long daysOverdue) {
        if (daysOverdue <= 0 || originalAmount == null) {
            return BigDecimal.ZERO;
        }

        // 5% late fee per month (30 days)
        BigDecimal monthlyRate = PaymentInstallmentConstants.LATE_FEE_MONTHLY_RATE;
        BigDecimal dailyRate = monthlyRate.divide(new BigDecimal(PaymentInstallmentConstants.DAYS_PER_MONTH), 4,
                RoundingMode.HALF_UP);

        return originalAmount.multiply(dailyRate).multiply(new BigDecimal(daysOverdue));
    }

    /**
     * Determine if a reminder should be sent based on business rules
     */
    public static boolean shouldSendReminder(PaymentInstallment installment) {
        if (installment == null || installment.getReminderSent()) {
            return false;
        }

        LocalDate dueDate = installment.getDueDate();
        if (dueDate == null) {
            return false;
        }

        // Send reminder 3 days before due date
        LocalDate reminderDate = dueDate.minusDays(PaymentInstallmentConstants.REMINDER_DAYS_BEFORE_DUE);
        return LocalDate.now().isAfter(reminderDate) || LocalDate.now().isEqual(reminderDate);
    }

    /**
     * Calculate the total amount including late fees
     */
    public static BigDecimal calculateTotalAmount(PaymentInstallment installment) {
        if (installment == null || installment.getAmount() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal baseAmount = installment.getAmount();
        long daysOverdue = calculateDaysOverdue(installment.getDueDate());
        BigDecimal lateFee = calculateLateFee(baseAmount, daysOverdue);

        return baseAmount.add(lateFee);
    }

    /**
     * Validate if an installment can be marked as paid
     */
    public static boolean canBeMarkedAsPaid(PaymentInstallment installment) {
        if (installment == null) {
            return false;
        }
        String status = installment.getStatus();
        return !PaymentInstallmentStatus.PAID.getCode().equals(status) &&
                !PaymentInstallmentStatus.CANCELLED.getCode().equals(status);
    }

    /**
     * Validate if an installment can be cancelled
     */
    public static boolean canBeCancelled(PaymentInstallment installment) {
        if (installment == null) {
            return false;
        }
        String status = installment.getStatus();
        return !PaymentInstallmentStatus.PAID.getCode().equals(status) &&
                !PaymentInstallmentStatus.CANCELLED.getCode().equals(status);
    }
}