package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReservationResponse(
        Integer id,
        String username,
        String nameTourPlan,
        String providerName,
        String reservationStatus,
        LocalDate reservationDate,
        Integer duration,
        BigDecimal price,
        BigDecimal totalPrice
) {
    public ReservationResponse(Reservation reservation) {
        this(
                reservation.getId(),
                reservation.getUser().getName() + " " + reservation.getUser().getLastname(),
                reservation.getTourPlan().getTitle(),
                reservation.getTourPlan().getProvider().getName(),
                reservation.getReservationStatus().name(),
                reservation.getReservationDate(),
                reservation.getTourPlan().getDuration(),
                reservation.getTourPlan().getPrice(),
                reservation.getTourPlan().getPrice()
        );
    }
}
