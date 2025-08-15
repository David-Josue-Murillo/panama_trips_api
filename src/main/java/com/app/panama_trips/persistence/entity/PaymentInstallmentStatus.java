package com.app.panama_trips.persistence.entity;

/**
 * Enum representing the possible statuses of a payment installment.
 * This enum centralizes the status logic and provides type safety.
 */
public enum PaymentInstallmentStatus {
    PENDING("PENDING", "Payment installment is pending"),
    PAID("PAID", "Payment installment has been paid"),
    OVERDUE("OVERDUE", "Payment installment is overdue"),
    CANCELLED("CANCELLED", "Payment installment has been cancelled");

    private final String code;
    private final String description;

    PaymentInstallmentStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Check if the status indicates the installment is active (not cancelled)
     */
    public boolean isActive() {
        return this != CANCELLED;
    }

    /**
     * Check if the status indicates the installment requires attention
     */
    public boolean requiresAttention() {
        return this == PENDING || this == OVERDUE;
    }

    /**
     * Check if the status indicates the installment is completed
     */
    public boolean isCompleted() {
        return this == PAID;
    }

    /**
     * Convert from string to enum
     */
    public static PaymentInstallmentStatus fromString(String status) {
        for (PaymentInstallmentStatus installmentStatus : values()) {
            if (installmentStatus.code.equalsIgnoreCase(status)) {
                return installmentStatus;
            }
        }
        throw new IllegalArgumentException("Unknown payment installment status: " + status);
    }
}