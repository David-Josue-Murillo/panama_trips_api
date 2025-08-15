package com.app.panama_trips.persistence.entity;

import java.math.BigDecimal;

/**
 * Constants and configuration values for PaymentInstallment.
 * This class centralizes all magic numbers and configuration values.
 */
public final class PaymentInstallmentConstants {

    // Private constructor to prevent instantiation
    private PaymentInstallmentConstants() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // Database constraints
    public static final int STATUS_MAX_LENGTH = 20;
    public static final int AMOUNT_PRECISION = 10;
    public static final int AMOUNT_SCALE = 2;

    // Business rules
    public static final int REMINDER_DAYS_BEFORE_DUE = 3;
    public static final BigDecimal MINIMUM_AMOUNT = new BigDecimal("0.01");
    public static final BigDecimal LATE_FEE_MONTHLY_RATE = new BigDecimal("0.05"); // 5% per month
    public static final int DAYS_PER_MONTH = 30;

    // Default values
    public static final String DEFAULT_STATUS = PaymentInstallmentStatus.PENDING.getCode();
    public static final boolean DEFAULT_REMINDER_SENT = false;

    // Validation messages
    public static final String ERROR_NULL_INSTALLMENT = "Payment installment cannot be null";
    public static final String ERROR_NULL_RESERVATION = "Reservation is required";
    public static final String ERROR_INVALID_AMOUNT = "Amount must be greater than zero";
    public static final String ERROR_INVALID_DUE_DATE = "Due date must be today or in the future";
    public static final String ERROR_INVALID_STATUS = "Invalid status. Must be one of: PENDING, PAID, OVERDUE, CANCELLED";
    public static final String ERROR_NULL_REMINDER_FLAG = "Reminder sent flag cannot be null";
    public static final String ERROR_NO_ID_FOR_UPDATE = "Cannot update installment without ID";
    public static final String ERROR_EXISTING_ID_FOR_CREATION = "Cannot create installment with existing ID";
    public static final String ERROR_NO_ID_FOR_DELETION = "Cannot delete installment without ID";
    public static final String ERROR_CANNOT_DELETE_PAID = "Cannot delete paid installment";

    // Status transition rules
    public static final String[] ALLOWED_PENDING_TRANSITIONS = { "PAID", "OVERDUE", "CANCELLED" };
    public static final String[] ALLOWED_OVERDUE_TRANSITIONS = { "PAID", "CANCELLED" };
    public static final String[] ALLOWED_PAID_TRANSITIONS = {}; // No transitions allowed
    public static final String[] ALLOWED_CANCELLED_TRANSITIONS = {}; // No transitions allowed

    // Database column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_RESERVATION_ID = "reservation_id";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DUE_DATE = "due_date";
    public static final String COLUMN_PAYMENT_ID = "payment_id";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_REMINDER_SENT = "reminder_sent";
    public static final String COLUMN_CREATED_AT = "created_at";

    // Foreign key names
    public static final String FK_RESERVATION = "fk_payment_installment_reservation";
    public static final String FK_PAYMENT = "fk_payment_installment_payment";

    // Table name
    public static final String TABLE_NAME = "payment_installments";
}