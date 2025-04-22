package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, String> {

    List<Language> findByIsActiveTrue();

    Optional<Language> findByNameIgnoreCase(String name);

    @Query("SELECT l FROM Language l WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :keyword, '%')) AND l.isActive = true")
    List<Language> searchActiveLanguages(@Param("keyword") String keyword);

    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT COUNT(l) FROM Language l WHERE l.isActive = true")
    long countActiveLanguages();
}