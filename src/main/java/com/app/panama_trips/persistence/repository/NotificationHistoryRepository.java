package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.NotificationHistory;
import com.app.panama_trips.persistence.entity.NotificationTemplate;
import com.app.panama_trips.persistence.entity.Reservation;
import com.app.panama_trips.persistence.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Integer> {

    List<NotificationHistory> findByUser(UserEntity user);

    List<NotificationHistory> findByReservation(Reservation reservation);

    List<NotificationHistory> findByTemplate(NotificationTemplate template);

    List<NotificationHistory> findByDeliveryStatus(String deliveryStatus);

    List<NotificationHistory> findByChannel(String channel);

    List<NotificationHistory> findBySentAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT nh FROM NotificationHistory nh WHERE nh.user.id = :userId AND nh.sentAt >= :since ORDER BY nh.sentAt DESC")
    List<NotificationHistory> findRecentNotifications(@Param("userId") Integer userId, @Param("since") LocalDateTime since);

    @Query("SELECT COUNT(nh) FROM NotificationHistory nh WHERE nh.user.id = :userId AND nh.deliveryStatus = 'DELIVERED'")
    long countDeliveredNotifications(@Param("userId") Integer userId);

    @Query("SELECT nh FROM NotificationHistory nh WHERE LOWER(nh.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<NotificationHistory> searchByContent(@Param("keyword") String keyword);

    // Count queries (replace findByX().size() N+1 queries)
    long countByUser(UserEntity user);

    long countByDeliveryStatus(String deliveryStatus);

    long countByChannel(String channel);

    long countBySentAtBetween(LocalDateTime start, LocalDateTime end);

    // Filtered find queries
    List<NotificationHistory> findByUserAndSentAtBetween(UserEntity user, LocalDateTime start, LocalDateTime end);

    List<NotificationHistory> findByReservationAndChannel(Reservation reservation, String channel);

    // Single-result optimized (replaces stream().max())
    Optional<NotificationHistory> findFirstByUserOrderBySentAtDesc(UserEntity user);

    // Existence check
    boolean existsByUserAndTemplate(UserEntity user, NotificationTemplate template);

    // Paginated (replaces findAll() + sort + limit)
    List<NotificationHistory> findAllByOrderBySentAtDesc(Pageable pageable);

    // Delete old records
    void deleteBySentAtBefore(LocalDateTime cutoff);
}
