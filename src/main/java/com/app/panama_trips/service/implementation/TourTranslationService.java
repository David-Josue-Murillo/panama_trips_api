package com.app.panama_trips.service.implementation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.panama_trips.presentation.dto.TourTranslationRequest;
import com.app.panama_trips.presentation.dto.TourTranslationResponse;
import com.app.panama_trips.service.interfaces.ITourTranslationService;

public class TourTranslationService implements ITourTranslationService{

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

}
