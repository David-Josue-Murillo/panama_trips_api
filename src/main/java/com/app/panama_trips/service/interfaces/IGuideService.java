package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.persistence.entity.Provider;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.presentation.dto.GuideRequest;
import com.app.panama_trips.presentation.dto.GuideResponse;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IGuideService {

    // CRUD operations
    Page<GuideResponse> findAll(Pageable pageable);
    GuideResponse createGuide(GuideRequest guideRequest);
    GuideResponse updateGuide(Integer id, GuideRequest guideRequest);
    void deleteGuide(Integer id);
    Optional<GuideResponse> findById(Integer id);
    
    // Repository-based specific methods
    List<GuideResponse> findAllActive();
    List<GuideResponse> findByProvider(Provider provider);
    Optional<GuideResponse> findByUser(UserEntity user);
    List<GuideResponse> findByYearsExperienceGreaterThanEqual(Integer years);
    List<GuideResponse> findActiveGuidesByProvider(Integer providerId);
    List<GuideResponse> findByLanguageAndActive(String language);
    List<GuideResponse> findBySpecialtyAndActive(String specialty);
    
    // Additional useful methods
    void activateGuide(Integer id);
    void deactivateGuide(Integer id);
    boolean existsById(Integer id);
    boolean existsByUser(UserEntity user);
}
