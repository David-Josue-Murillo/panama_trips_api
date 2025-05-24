package com.app.panama_trips.service.implementation;

import com.app.panama_trips.persistence.entity.Guide;
import com.app.panama_trips.persistence.entity.Provider;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.repository.GuideRepository;
import com.app.panama_trips.persistence.repository.ProviderRepository;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.presentation.dto.GuideRequest;
import com.app.panama_trips.presentation.dto.GuideResponse;
import com.app.panama_trips.service.interfaces.IGuideService;
import com.app.panama_trips.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuideService implements IGuideService {

    private final GuideRepository guideRepository;
    private final ProviderRepository providerRepository;
    private final UserEntityRepository userRepository;


    @Override
    @Transactional(readOnly = true)
    public Page<GuideResponse> findAll(Pageable pageable) {
        return guideRepository.findAll(pageable)
                .map(GuideResponse::new);
    }

    @Override
    @Transactional
    public GuideResponse createGuide(GuideRequest guideRequest) {
        Guide guide = builderFromRequest(guideRequest);
        return new GuideResponse(guideRepository.save(guide));
    }

    @Override
    @Transactional
    public GuideResponse updateGuide(Integer id, GuideRequest guideRequest) {
        Guide guide = guideRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Guide not found"));
        updateGuideFields(guide, guideRequest);
        return new GuideResponse(guideRepository.save(guide));
    }

    @Override
    @Transactional
    public void deleteGuide(Integer id) {
        if (!guideRepository.existsById(id)) {
            throw new ResourceNotFoundException("Guide not found");
        }
        guideRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GuideResponse> findById(Integer id) {
        return guideRepository.findById(id)
                .map(GuideResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuideResponse> findAllActive() {
        return guideRepository.findByIsActiveTrue()
                .stream()
                .map(GuideResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuideResponse> findByProvider(Provider provider) {
        return guideRepository.findByProvider(provider)
                .stream()
                .map(GuideResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GuideResponse> findByUser(UserEntity user) {
        return guideRepository.findByUser(user)
                .map(GuideResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuideResponse> findByYearsExperienceGreaterThanEqual(Integer years) {
        return guideRepository.findByYearsExperienceGreaterThanEqual(years)
                .stream()
                .map(GuideResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuideResponse> findActiveGuidesByProvider(Integer providerId) {
        return guideRepository.findActiveGuidesByProvider(providerId)
                .stream()
                .map(GuideResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuideResponse> findByLanguageAndActive(String language) {
        return guideRepository.findByLanguageAndActive(language)
                .stream()
                .map(GuideResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GuideResponse> findBySpecialtyAndActive(String specialty) {
        return guideRepository.findBySpecialtyAndActive(specialty)
                .stream()
                .map(GuideResponse::new)
                .toList();
    }

    @Override
    @Transactional
    public void activateGuide(Integer id) {
        Guide guide = guideRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Guide not found"));
        guide.setIsActive(true);
        guideRepository.save(guide);
    }

    @Override
    @Transactional
    public void deactivateGuide(Integer id) {
        Guide guide = guideRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Guide not found"));
        guide.setIsActive(false);
        guideRepository.save(guide);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return guideRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUser(UserEntity user) {
        return false;
    }

    // Helper methods
    private Guide builderFromRequest(GuideRequest request) {
        return Guide.builder()
            .user(findUserByIdOrFail(request.userId()))
            .provider(findProviderByIdOrFail(request.providerId()))
            .bio(request.bio())
            .specialties(request.specialties().toString())
            .languages(request.languages().toString())
            .yearsExperience(request.yearsExperience())
            .certificationDetails(request.certificationDetails())
            .isActive(request.isActive())
            .build();
    }

    private void updateGuideFields(Guide guide, GuideRequest request) {
        guide.setUser(findUserByIdOrFail(request.userId()));
        guide.setProvider(findProviderByIdOrFail(request.providerId()));
        guide.setBio(request.bio());
        guide.setSpecialties(request.specialties().toString());
        guide.setLanguages(request.languages().toString());
        guide.setYearsExperience(request.yearsExperience());
        guide.setCertificationDetails(request.certificationDetails());
        guide.setIsActive(request.isActive());
    }

    private UserEntity findUserByIdOrFail(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Provider findProviderByIdOrFail(Integer providerId) {
        return providerRepository.findById(providerId)
            .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
    }
}
