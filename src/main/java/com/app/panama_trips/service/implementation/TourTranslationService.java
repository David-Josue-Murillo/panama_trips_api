package com.app.panama_trips.service.implementation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Language;
import com.app.panama_trips.persistence.entity.TourPlan;
import com.app.panama_trips.persistence.entity.TourTranslation;
import com.app.panama_trips.persistence.entity.TourTranslationId;
import com.app.panama_trips.persistence.repository.LanguageRepository;
import com.app.panama_trips.persistence.repository.TourPlanRepository;
import com.app.panama_trips.persistence.repository.TourTranslationRepository;
import com.app.panama_trips.presentation.dto.TourTranslationRequest;
import com.app.panama_trips.presentation.dto.TourTranslationResponse;
import com.app.panama_trips.service.interfaces.ITourTranslationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TourTranslationService implements ITourTranslationService{

    private final TourTranslationRepository tourTranslationRepository;
    private final TourPlanRepository tourPlanRepository;
    private final LanguageRepository languageRepository;


    @Override
    @Transactional(readOnly = true)
    public Page<TourTranslationResponse> getAllTourTranslations(Pageable pageable) {
        return tourTranslationRepository.findAll(pageable)
                .map(TourTranslationResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public TourTranslationResponse getTourTranslationByTourPlanIdAndLanguageCode(Integer tourPlanId,
            String languageCode) {
        TourTranslation tourTranslation = tourTranslationRepository
                .findByTourPlanIdAndLanguageCode(tourPlanId, languageCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tour Translation not found with tourPlanId " + tourPlanId + " and languageCode "
                                + languageCode));
        return new TourTranslationResponse(tourTranslation);
    }

    @Override
    @Transactional
    public TourTranslationResponse saveTourTranslation(TourTranslationRequest request) {
        validateTourTranslationRequest(request);
        TourTranslation tourTranslation = buildTourTranslationFromRequest(request);
        return new TourTranslationResponse(tourTranslationRepository.save(tourTranslation));
    }

    @Override
    @Transactional
    public TourTranslationResponse updateTourTranslation(Integer tourPlanId, String languageCode,
            TourTranslationRequest request) {
        // Validar que los IDs del request coincidan con los parámetros
        if (!tourPlanId.equals(request.tourPlanId()) || !languageCode.equals(request.languageCode())) {
            throw new IllegalArgumentException(
                    "The tourPlanId and languageCode in the request must match the path parameters");
        }

        TourTranslation existingTranslation = tourTranslationRepository
                .findByTourPlanIdAndLanguageCode(tourPlanId, languageCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tour Translation not found with tourPlanId " + tourPlanId + " and languageCode "
                                + languageCode));
        updateTourTranslationFields(existingTranslation, request);
        return new TourTranslationResponse(tourTranslationRepository.save(existingTranslation));
    }

    @Override
    public void deleteTourTranslation(Integer tourPlanId, String languageCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteTourTranslation'");
    }

    @Override
    public List<TourTranslationResponse> getTourTranslationsByTourPlanId(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTourTranslationsByTourPlanId'");
    }

    @Override
    public List<TourTranslationResponse> getTourTranslationsByLanguageCode(String languageCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTourTranslationsByLanguageCode'");
    }

    @Override
    public boolean existsByTourPlanIdAndLanguageCode(Integer tourPlanId, String languageCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsByTourPlanIdAndLanguageCode'");
    }

    @Override
    public void deleteAllTranslationsByTourPlanId(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllTranslationsByTourPlanId'");
    }

    @Override
    public void deleteAllTranslationsByLanguageCode(String languageCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAllTranslationsByLanguageCode'");
    }

    @Override
    public Long countTranslationsByTourPlanId(Integer tourPlanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'countTranslationsByTourPlanId'");
    }

    @Override
    public long countAllTourTranslations() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'countAllTourTranslations'");
    }

    // Private helper methods
    private void validateTourTranslationRequest(TourTranslationRequest request) {
        // Validar que el TourPlan existe
        if (!tourPlanRepository.existsById(request.tourPlanId())) {
            throw new ResourceNotFoundException("Tour Plan not found with id " + request.tourPlanId());
        }

        // Validar que el Language existe
        if (!languageRepository.existsById(request.languageCode())) {
            throw new ResourceNotFoundException("Language not found with code " + request.languageCode());
        }

        // Validar que no exista ya una traducción para esta combinación
        if (tourTranslationRepository.findByTourPlanIdAndLanguageCode(request.tourPlanId(), request.languageCode())
                .isPresent()) {
            throw new IllegalArgumentException("Tour Translation already exists for tourPlanId " + request.tourPlanId()
                    + " and languageCode " + request.languageCode());
        }
    }

    private TourTranslation buildTourTranslationFromRequest(TourTranslationRequest request) {
        TourPlan tourPlan = tourPlanRepository.findById(request.tourPlanId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Tour Plan not found with id " + request.tourPlanId()));

        Language language = languageRepository.findById(request.languageCode())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Language not found with code " + request.languageCode()));

        TourTranslationId id = TourTranslationId.builder()
                .tourPlanId(request.tourPlanId())
                .languageCode(request.languageCode())
                .build();

        return TourTranslation.builder()
                .id(id)
                .tourPlan(tourPlan)
                .language(language)
                .title(request.title())
                .shortDescription(request.shortDescription())
                .description(request.description())
                .includedServices(request.includedServices())
                .excludedServices(request.excludedServices())
                .whatToBring(request.whatToBring())
                .meetingPoint(request.meetingPoint())
                .build();
    }

    private void updateTourTranslationFields(TourTranslation tourTranslation, TourTranslationRequest request) {
        tourTranslation.setTitle(request.title());
        tourTranslation.setShortDescription(request.shortDescription());
        tourTranslation.setDescription(request.description());
        tourTranslation.setIncludedServices(request.includedServices());
        tourTranslation.setExcludedServices(request.excludedServices());
        tourTranslation.setWhatToBring(request.whatToBring());
        tourTranslation.setMeetingPoint(request.meetingPoint());
    }
}
