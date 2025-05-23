package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.Guide;
import com.app.panama_trips.persistence.entity.Provider;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.presentation.dto.GuideRequest;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IGuideService {

    // CRUD operations
    Page<Guide> findAll(Pageable pageable);
    Guide createGuide(GuideRequest guideRequest);
    Guide updateGuide(Integer id, GuideRequest guideRequest);
    void deleteGuide(Integer id);
    Optional<Guide> findById(Integer id);
    
    // Repository-based specific methods
    List<Guide> findAllActive();
    List<Guide> findByProvider(Provider provider);
    Optional<Guide> findByUser(UserEntity user);
    List<Guide> findByYearsExperienceGreaterThanEqual(Integer years);
    List<Guide> findActiveGuidesByProvider(Integer providerId);
    List<Guide> findByLanguageAndActive(String language);
    List<Guide> findBySpecialtyAndActive(String specialty);
    
    // Additional useful methods
    void activateGuide(Integer id);
    void deactivateGuide(Integer id);
    boolean existsById(Integer id);
    boolean existsByUser(UserEntity user);
}
