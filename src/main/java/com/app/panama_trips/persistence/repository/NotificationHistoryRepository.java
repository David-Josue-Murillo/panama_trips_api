package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.NotificationHistory;
import com.app.panama_trips.persistence.entity.NotificationTemplate;
import com.app.panama_trips.persistence.entity.Reservation;
import com.app.panama_trips.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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
}