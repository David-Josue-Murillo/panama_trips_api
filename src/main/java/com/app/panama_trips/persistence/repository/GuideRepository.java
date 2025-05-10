package com.app.panama_trips.persistence.repository;

import com.app.panama_trips.persistence.entity.Guide;
import com.app.panama_trips.persistence.entity.Provider;
import com.app.panama_trips.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Integer> {

    List<Guide> findByIsActiveTrue();

    List<Guide> findByProvider(Provider provider);

    Optional<Guide> findByUser(UserEntity user);

    List<Guide> findByYearsExperienceGreaterThanEqual(Integer years);

    @Query("SELECT g FROM Guide g WHERE g.isActive = true AND g.provider.id = :providerId")
    List<Guide> findActiveGuidesByProvider(@Param("providerId") Integer providerId);

    @Query("SELECT g FROM Guide g WHERE g.languages LIKE %:language% AND g.isActive = true")
    List<Guide> findByLanguageAndActive(@Param("language") String language);

    @Query("SELECT g FROM Guide g WHERE g.specialties LIKE %:specialty% AND g.isActive = true")
    List<Guide> findBySpecialtyAndActive(@Param("specialty") String specialty);
}