package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.LanguageRequest;
import com.app.panama_trips.presentation.dto.LanguageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ILanguageService {
    // CRUD operations
    Page<LanguageResponse> getAllLanguages(Pageable pageable);
    LanguageResponse getLanguageByCode(String code);
    LanguageResponse saveLanguage(LanguageRequest request);
    LanguageResponse updateLanguage(String code, LanguageRequest request);
    void deleteLanguage(String code);

    // Business operations
    List<LanguageResponse> getAllActiveLanguages();
    LanguageResponse getLanguageByName(String name);
    List<LanguageResponse> searchActiveLanguages(String keyword);

}
