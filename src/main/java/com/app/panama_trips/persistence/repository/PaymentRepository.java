package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
