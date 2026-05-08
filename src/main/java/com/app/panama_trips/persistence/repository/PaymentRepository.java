package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Payment;
import com.app.panama_trips.persistence.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Busca pagos por su estado.
     */
    List<Payment> findByPaymentStatus(PaymentStatus status);

    /**
     * Busca pagos asociados a una reservación por su ID.
     */
    List<Payment> findByReservationId_Id(Long reservationId);

    /**
     * Busca pagos asociados a un usuario específico a través de su reservación.
     */
    @Query("SELECT p FROM Payment p WHERE p.reservationId.userEntity.id = :userId")
    List<Payment> findByUserId(@Param("userId") Long userId);

    /**
     * Busca pagos cuya fecha de creación esté dentro de un rango.
     */
    List<Payment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Cuenta la cantidad de pagos que tienen un estado determinado.
     */
    long countByPaymentStatus(PaymentStatus status);
}

