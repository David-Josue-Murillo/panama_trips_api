package com.app.panama_trips.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Validator class for PaymentInstallment entities.
 * This class handles all validation logic separate from the entity itself.
 */
public class PaymentInstallmentValidator {

    /**
     * Validate a PaymentInstallment entity and return a list of validation errors
     */
    public static List<String> validate(PaymentInstallment installment) {
        List<String> errors = new ArrayList<>();

        if (installment == null) {
            errors.add("Payment installment cannot be null");
            return errors;
        }

        // Validate reservation
        if (installment.getReservation() == null) {
            errors.add(PaymentInstallmentConstants.ERROR_NULL_RESERVATION);
        }

        // Validate amount
        if (!PaymentInstallmentBusinessLogic.isValidAmount(installment.getAmount())) {
            errors.add(PaymentInstallmentConstants.ERROR_INVALID_AMOUNT);
        }

        // Validate due date
        if (!PaymentInstallmentBusinessLogic.isValidDueDate(installment.getDueDate())) {
            errors.add(PaymentInstallmentConstants.ERROR_INVALID_DUE_DATE);
        }

        // Validate status
        if (!isValidStatus(installment.getStatus())) {
            errors.add(PaymentInstallmentConstants.ERROR_INVALID_STATUS);
        }

        // Validate reminder sent
        if (installment.getReminderSent() == null) {
            errors.add(PaymentInstallmentConstants.ERROR_NULL_REMINDER_FLAG);
        }

        return errors;
    }

    /**
     * Validate amount for a payment installment
     */
    public static boolean isValidAmount(BigDecimal amount) {
        return PaymentInstallmentBusinessLogic.isValidAmount(amount);
    }

    /**
     * Validate due date for a payment installment
     */
    public static boolean isValidDueDate(LocalDate dueDate) {
        return PaymentInstallmentBusinessLogic.isValidDueDate(dueDate);
    }

    /**
     * Validate status string
     */
    public static boolean isValidStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return false;
        }

        try {
            PaymentInstallmentStatus.fromString(status);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Validate if an installment can be updated
     */
    public static List<String> validateForUpdate(PaymentInstallment installment) {
        List<String> errors = new ArrayList<>();

        if (installment == null) {
            errors.add("Payment installment cannot be null");
            return errors;
        }

        // Check if installment exists (has ID)
        if (installment.getId() == null) {
            errors.add("Cannot update installment without ID");
        }

        // Validate basic fields
        errors.addAll(validate(installment));

        return errors;
    }

    /**
     * Validate if an installment can be created
     */
    public static List<String> validateForCreation(PaymentInstallment installment) {
        List<String> errors = new ArrayList<>();

        if (installment == null) {
            errors.add("Payment installment cannot be null");
            return errors;
        }

        // Check if installment already exists (has ID)
        if (installment.getId() != null) {
            errors.add("Cannot create installment with existing ID");
        }

        // Validate basic fields
        errors.addAll(validate(installment));

        return errors;
    }

    /**
     * Validate if an installment can be deleted
     */
    public static List<String> validateForDeletion(PaymentInstallment installment) {
        List<String> errors = new ArrayList<>();

        if (installment == null) {
            errors.add("Payment installment cannot be null");
            return errors;
        }

        // Check if installment exists
        if (installment.getId() == null) {
            errors.add("Cannot delete installment without ID");
        }

        // Check if installment is already paid
        if (PaymentInstallmentStatus.PAID.getCode().equals(installment.getStatus())) {
            errors.add("Cannot delete paid installment");
        }

        return errors;
    }

    /**
     * Validate if status transition is allowed
     */
    public static boolean isValidStatusTransition(String currentStatus, String newStatus) {
        if (!isValidStatus(currentStatus) || !isValidStatus(newStatus)) {
            return false;
        }

        // Define allowed transitions
        switch (currentStatus.toUpperCase()) {
            case "PENDING":
                return "PAID".equals(newStatus.toUpperCase()) ||
                        "OVERDUE".equals(newStatus.toUpperCase()) ||
                        "CANCELLED".equals(newStatus.toUpperCase());

            case "OVERDUE":
                return "PAID".equals(newStatus.toUpperCase()) ||
                        "CANCELLED".equals(newStatus.toUpperCase());

            case "PAID":
                return false; // Cannot change from PAID

            case "CANCELLED":
                return false; // Cannot change from CANCELLED

            default:
                return false;
        }
    }
}