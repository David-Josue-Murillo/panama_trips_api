package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Payment;
import com.app.panama_trips.persistence.entity.PaymentInstallment;
import com.app.panama_trips.persistence.entity.Reservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentInstallmentRepository extends JpaRepository<PaymentInstallment, Integer> {

    List<PaymentInstallment> findByReservation(Reservation reservation);

    List<PaymentInstallment> findByPayment(Payment payment);

    List<PaymentInstallment> findByStatus(String status);

    List<PaymentInstallment> findByDueDateBefore(LocalDate date);

    List<PaymentInstallment> findByDueDateBetween(LocalDate startDate, LocalDate endDate);

    List<PaymentInstallment> findByReminderSent(Boolean reminderSent);

    @Query("SELECT pi FROM PaymentInstallment pi WHERE pi.reservation.id = :reservationId AND pi.status = :status")
    List<PaymentInstallment> findByReservationIdAndStatus(@Param("reservationId") Integer reservationId, @Param("status") String status);

    @Query("SELECT pi FROM PaymentInstallment pi WHERE pi.reminderSent = false AND pi.dueDate <= :date AND pi.status = 'PENDING'")
    List<PaymentInstallment> findPendingInstallmentsWithoutReminder(@Param("date") LocalDate date);

    @Query("SELECT SUM(pi.amount) FROM PaymentInstallment pi WHERE pi.reservation.id = :reservationId AND pi.status = 'PENDING'")
    BigDecimal sumPendingAmountByReservation(@Param("reservationId") Integer reservationId);

    @Query("SELECT COUNT(pi) FROM PaymentInstallment pi WHERE pi.status = :status AND pi.dueDate <= :date")
    Long countOverdueInstallments(@Param("status") String status, @Param("date") LocalDate date);

    // Derived query methods for existence checks
    boolean existsByReservation_Id(Integer reservationId);

    boolean existsByPayment_Id(Integer paymentId);

    // Derived query methods for counting
    long countByReservation_Id(Integer reservationId);

    long countByStatus(String status);

    long countByDueDateBefore(LocalDate date);

    long countByReminderSent(Boolean reminderSent);

    // Derived query methods for finding
    List<PaymentInstallment> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

    List<PaymentInstallment> findByAmount(BigDecimal amount);

    List<PaymentInstallment> findByPayment_Id(Integer paymentId);

    List<PaymentInstallment> findByReservation_Id(Integer reservationId);

    List<PaymentInstallment> findByStatusAndDueDateBefore(String status, LocalDate date);

    Optional<PaymentInstallment> findFirstByReservation_IdOrderByDueDateDesc(Integer reservationId);

    List<PaymentInstallment> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // @Query methods for JOINs and aggregations
    @Query("SELECT pi FROM PaymentInstallment pi JOIN pi.reservation r WHERE r.user.id = :userId")
    List<PaymentInstallment> findByReservationUserId(@Param("userId") Long userId);

    @Query("SELECT pi FROM PaymentInstallment pi JOIN pi.reservation r WHERE r.tourPlan.id = :tourPlanId")
    List<PaymentInstallment> findByReservationTourPlanId(@Param("tourPlanId") Integer tourPlanId);

    @Query("SELECT COALESCE(SUM(pi.amount), 0) FROM PaymentInstallment pi WHERE pi.reservation.id = :reservationId")
    BigDecimal sumAmountByReservationId(@Param("reservationId") Integer reservationId);

    @Query("SELECT COALESCE(SUM(pi.amount), 0) FROM PaymentInstallment pi WHERE pi.reservation.id = :reservationId AND pi.status = :status")
    BigDecimal sumAmountByReservationIdAndStatus(@Param("reservationId") Integer reservationId, @Param("status") String status);

    @Query("SELECT COALESCE(SUM(pi.amount), 0) FROM PaymentInstallment pi WHERE pi.status = :status")
    BigDecimal sumAmountByStatus(@Param("status") String status);
}
