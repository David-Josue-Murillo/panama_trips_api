package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.TourTranslationRequest;
import com.app.panama_trips.presentation.dto.TourTranslationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITourTranslationService {
    // CRUD operations
    Page<TourTranslationResponse> getAllTourTranslations(Pageable pageable);
    TourTranslationResponse getTourTranslationByTourPlanIdAndLanguageCode(Integer tourPlanId, String languageCode);
    TourTranslationResponse saveTourTranslation(TourTranslationRequest request);
    TourTranslationResponse updateTourTranslation(Integer tourPlanId, String languageCode,TourTranslationRequest request);
    void deleteTourTranslation(Integer tourPlanId, String languageCode);

    // Additional service methods
    List<TourTranslationResponse> getTourTranslationsByTourPlanId(Integer tourPlanId);
    List<TourTranslationResponse> getTourTranslationsByLanguageCode(String languageCode);
    boolean existsByTourPlanIdAndLanguageCode(Integer tourPlanId, String languageCode);
    void deleteAllTranslationsByTourPlanId(Integer tourPlanId);
    void deleteAllTranslationsByLanguageCode(String languageCode);
    Long countTranslationsByTourPlanId(Integer tourPlanId);
    long countAllTourTranslations();
}
