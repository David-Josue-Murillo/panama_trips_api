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
    // Search by user, travel plan, status, and date
    Page<Reservation> findByUser_Id(Long userId, Pageable pageable);
    Page<Reservation> findByTourPlan_Id(Integer tourPlanId, Pageable pageable);
    Page<Reservation> findByReservationStatus(ReservationStatus reservationStatus, Pageable pageable);
    Page<Reservation> findByReservationDate(LocalDate reservationDate, Pageable pageable);

    // Searched by user and status, travel plan and status
    Page<Reservation> findByUser_IdAndReservationStatus(Long userId, ReservationStatus reservationStatus, Pageable pageable);
    Page<Reservation> findByTourPlan_IdAndReservationStatus(Integer tourPlanId, ReservationStatus reservationStatus, Pageable pageable);

    // Count reservations by status and travel plan
    Long countByReservationStatus(ReservationStatus reservationStatus);
    Long countByTourPlan_Id(Integer tourPlanId);
    boolean existsByUser_IdAndTourPlanId(Long userId, Integer tourPlanId);

    // Search by date range, month, year
    Page<Reservation> findByReservationDateBetween(LocalDate reservationDateAfter, LocalDate reservationDateBefore, Pageable pageable);
    Page<Reservation> findByReservationDate_Month(short reservationDateMonth, Pageable pageable);
    Page<Reservation> findByReservationDate_Year(int reservationDateYear, Pageable pageable);

    // Search by price
    Page<Reservation> findByTotalPriceGreaterThan(BigDecimal totalPriceIsGreaterThan, Pageable pageable);
    Page<Reservation> findByTotalPriceBetween(BigDecimal totalPriceAfter, BigDecimal totalPriceBefore, Pageable pageable);

    // Search by price range with specific status
    Page<Reservation> findByTotalPriceBetweenAndReservationStatus(
            BigDecimal minPrice,
            BigDecimal maxPrice,
            ReservationStatus status,
            Pageable pageable
    );

    // Custom query to search for a user's recent reservations
    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.createdAt >= :recentDate")
    Page<Reservation> findRecentReservationsByUser(
            @Param("userId") Long userId,
            @Param("recentDate") LocalDate recentDate,
            Pageable pageable
    );

    // Search for reservations by day of the week
    @Query("SELECT r FROM Reservation r WHERE DAYOFWEEK(r.reservationDate) = :dayOfWeek")
    Page<Reservation> findByDayOfWeek(@Param("dayOfWeek") int dayOfWeek, Pageable pageable);

    // Search for tour reservations in a specific province
    @Query("SELECT r FROM Reservation r JOIN r.tourPlan tp JOIN tp.provider.province reg WHERE reg.id = :provinceId")
    Page<Reservation> findByRegion(@Param("provinceId") Integer provinceId, Pageable pageable);

    // Booking statistics
    @Query("SELECT " +
            "COUNT(r) as totalReservations, " +
            "AVG(r.totalPrice) as averagePrice, " +
            "SUM(r.totalPrice) as totalRevenue " +
            "FROM Reservation r")
    Object[] getReservationStatistics();
}
