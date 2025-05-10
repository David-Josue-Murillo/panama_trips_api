package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Integer> {

    Optional<NotificationTemplate> findByName(String name);

    List<NotificationTemplate> findByType(String type);

    @Query("SELECT nt FROM NotificationTemplate nt WHERE LOWER(nt.subject) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(nt.body) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<NotificationTemplate> searchBySubjectOrBody(@Param("keyword") String keyword);

    @Query("SELECT nt FROM NotificationTemplate nt WHERE nt.type = :type AND LOWER(nt.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<NotificationTemplate> findByTypeAndNameContaining(@Param("type") String type, @Param("name") String name);

    boolean existsByName(String name);

    @Query("SELECT COUNT(nt) FROM NotificationTemplate nt WHERE nt.type = :type")
    long countByType(@Param("type") String type);
}