package com.app.panama_trips.service.implementation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public Page<TourTranslationResponse> getAllTourTranslations(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllTourTranslations'");
    }

    @Override
    public TourTranslationResponse getTourTranslationByTourPlanIdAndLanguageCode(Integer tourPlanId,
            String languageCode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTourTranslationByTourPlanIdAndLanguageCode'");
    }

    @Override
    public TourTranslationResponse saveTourTranslation(TourTranslationRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveTourTranslation'");
    }

    @Override
    public TourTranslationResponse updateTourTranslation(Integer tourPlanId, String languageCode,
            TourTranslationRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateTourTranslation'");
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
