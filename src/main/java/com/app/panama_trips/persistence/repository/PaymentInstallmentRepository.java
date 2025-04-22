package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Payment;
import com.app.panama_trips.persistence.entity.PaymentInstallment;
import com.app.panama_trips.persistence.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentInstallmentRepository extends JpaRepository<PaymentInstallment, Integer> {

    List<PaymentInstallment> findByReservation(Reservation reservation);

    List<PaymentInstallment> findByPayment(Payment payment);

    List<PaymentInstallment> findByStatus(String status);

    List<PaymentInstallment> findByDueDateBefore(LocalDate date);

    List<PaymentInstallment> findByDueDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT pi FROM PaymentInstallment pi WHERE pi.reservation.id = :reservationId AND pi.status = :status")
    List<PaymentInstallment> findByReservationIdAndStatus(@Param("reservationId") Integer reservationId, @Param("status") String status);

    @Query("SELECT pi FROM PaymentInstallment pi WHERE pi.reminderSent = false AND pi.dueDate <= :date AND pi.status = 'PENDING'")
    List<PaymentInstallment> findPendingInstallmentsWithoutReminder(@Param("date") LocalDate date);

    @Query("SELECT SUM(pi.amount) FROM PaymentInstallment pi WHERE pi.reservation.id = :reservationId AND pi.status = 'PENDING'")
    BigDecimal sumPendingAmountByReservation(@Param("reservationId") Integer reservationId);

    List<PaymentInstallment> findByReminderSent(Boolean reminderSent);

    @Query("SELECT COUNT(pi) FROM PaymentInstallment pi WHERE pi.status = :status AND pi.dueDate <= :date")
    Long countOverdueInstallments(@Param("status") String status, @Param("date") LocalDate date);
}