package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Reservation;
import com.app.panama_trips.persistence.entity.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Page<Reservation> findByUser_Id(Long userId, Pageable pageable);
    Page<Reservation> findByUser_IdAndReservationStatus(Long userId, ReservationStatus reservationStatus, Pageable pageable);
    Page<Reservation> findByTourPlan_Id(Integer tourPlanId, Pageable pageable);
    Page<Reservation> findByTourPlan_IdAndReservationStatus(Integer tourPlanId, ReservationStatus reservationStatus, Pageable pageable);
    Page<Reservation> findByReservationStatus(ReservationStatus reservationStatus, Pageable pageable);

    // Contar reservas por estado
    Long countByReservationStatus(ReservationStatus reservationStatus);

    // Contar reservas por tour
    Long countByTourPlan_Id(Integer tourPlanId);

    Page<Reservation> findByReservationDateBetween(LocalDate reservationDateAfter, LocalDate reservationDateBefore, Pageable pageable);
    Page<Reservation> findByReservationDate_Month(short reservationDateMonth, Pageable pageable);
    Page<Reservation> findByReservationDate_Year(int reservationDateYear, Pageable pageable);
    Page<Reservation> findByTotalPriceGreaterThan(BigDecimal totalPriceIsGreaterThan, Pageable pageable);
    Page<Reservation> findByTotalPriceBetween(BigDecimal totalPriceAfter, BigDecimal totalPriceBefore, Pageable pageable);

    // Buscar por rango de precios con estado específico
    Page<Reservation> findByTotalPriceBetweenAndReservationStatus(
            BigDecimal minPrice,
            BigDecimal maxPrice,
            ReservationStatus status,
            Pageable pageable
    );

    // Consulta personalizada para buscar reservas recientes de un usuario
    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.createdAt >= :recentDate")
    Page<Reservation> findRecentReservationsByUser(
            @Param("userId") Long userId,
            @Param("recentDate") LocalDate recentDate,
            Pageable pageable
    );

    // Buscar reservas por día de la semana
    @Query("SELECT r FROM Reservation r WHERE DAYOFWEEK(r.reservationDate) = :dayOfWeek")
    Page<Reservation> findByDayOfWeek(@Param("dayOfWeek") int dayOfWeek, Pageable pageable);

    // Buscar reservas de tours en una región específica
    @Query("SELECT r FROM Reservation r JOIN r.tourPlan tp JOIN tp.provider.province reg WHERE reg.id = :provinceId")
    Page<Reservation> findByRegion(@Param("provinceId") Integer provinceId, Pageable pageable);

    // Estadísticas de reservas
    @Query("SELECT " +
            "COUNT(r) as totalReservations, " +
            "AVG(r.totalPrice) as averagePrice, " +
            "SUM(r.totalPrice) as totalRevenue " +
            "FROM Reservation r")
    Object[] getReservationStatistics();
}
