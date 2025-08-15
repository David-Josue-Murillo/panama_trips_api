package com.app.panama_trips.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment_installments")
public class PaymentInstallment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false, foreignKey = @ForeignKey(name = "fk_payment_installment_reservation"))
    private Reservation reservation;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", foreignKey = @ForeignKey(name = "fk_payment_installment_payment"))
    private Payment payment;

    @Column(name = "status", length = 20, nullable = false)
    @Builder.Default
    private String status = PaymentInstallmentStatus.PENDING.getCode();

    @Column(name = "reminder_sent", nullable = false)
    @Builder.Default
    private Boolean reminderSent = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDate createdAt;

    /**
     * Check if this installment is overdue
     */
    public boolean isOverdue() {
        return PaymentInstallmentBusinessLogic.isOverdue(this.dueDate);
    }

    /**
     * Get the total amount including late fees
     */
    public BigDecimal getTotalAmount() {
        return PaymentInstallmentBusinessLogic.calculateTotalAmount(this);
    }

    /**
     * Check if a reminder should be sent
     */
    public boolean shouldSendReminder() {
        return PaymentInstallmentBusinessLogic.shouldSendReminder(this);
    }

    /**
     * Check if this installment can be marked as paid
     */
    public boolean canBeMarkedAsPaid() {
        return PaymentInstallmentBusinessLogic.canBeMarkedAsPaid(this);
    }

    /**
     * Check if this installment can be cancelled
     */
    public boolean canBeCancelled() {
        return PaymentInstallmentBusinessLogic.canBeCancelled(this);
    }

    /**
     * Get days overdue
     */
    public long getDaysOverdue() {
        return PaymentInstallmentBusinessLogic.calculateDaysOverdue(this.dueDate);
    }

    /**
     * Get days until due
     */
    public long getDaysUntilDue() {
        return PaymentInstallmentBusinessLogic.calculateDaysUntilDue(this.dueDate);
    }
}