package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.ReviewCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewCategoryRepository extends JpaRepository<ReviewCategory, Integer> {

    Optional<ReviewCategory> findByName(String name);

    List<ReviewCategory> findByNameContainingIgnoreCase(String namePart);

    @Query("SELECT rc FROM ReviewCategory rc WHERE LOWER(rc.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<ReviewCategory> findByDescriptionContaining(@Param("keyword") String keyword);

    boolean existsByName(String name);

    @Query("SELECT rc FROM ReviewCategory rc ORDER BY rc.name ASC")
    List<ReviewCategory> findAllSortedByName();

    @Query("SELECT COUNT(rc) FROM ReviewCategory rc")
    long countCategories();
}