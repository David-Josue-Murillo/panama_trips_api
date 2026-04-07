package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
  List<Discount> findByReservationId_Id(Integer reservationId);
}
