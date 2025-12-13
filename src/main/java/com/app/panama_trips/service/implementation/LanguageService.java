package com.app.panama_trips.service.implementation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.panama_trips.persistence.entity.Language;
import com.app.panama_trips.persistence.repository.LanguageRepository;
import com.app.panama_trips.presentation.dto.LanguageRequest;
import com.app.panama_trips.presentation.dto.LanguageResponse;
import com.app.panama_trips.service.interfaces.ILanguageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LanguageService implements ILanguageService {

    private final LanguageRepository languageRepository;
    
    @Override
    public Page<LanguageResponse> getAllLanguages(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllLanguages'");
    }

    @Override
    public LanguageResponse getLanguageByCode(String code) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLanguageByCode'");
    }

    @Override
    public LanguageResponse saveLanguage(LanguageRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveLanguage'");
    }

    @Override
    public LanguageResponse updateLanguage(String code, LanguageRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateLanguage'");
    }

    @Override
    public void deleteLanguage(String code) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteLanguage'");
    }

    @Override
    public List<LanguageResponse> getAllActiveLanguages() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllActiveLanguages'");
    }

    @Override
    public LanguageResponse getLanguageByName(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLanguageByName'");
    }

    @Override
    public List<LanguageResponse> searchActiveLanguages(String keyword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchActiveLanguages'");
    }

    @Override
    public long countActiveLanguages() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'countActiveLanguages'");
    }

    @Override
    public boolean existsByCode(String code) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsByCode'");
    }

    @Override
    public boolean existsByName(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existsByName'");
    }

    // Private helper methods
    private void validateLanguage(LanguageRequest request) {
        if (languageRepository.existsById(request.code())) {
            throw new IllegalArgumentException("Language with code " + request.code() + " already exists");
        }
        if (languageRepository.existsByNameIgnoreCase(request.name())) {
            throw new IllegalArgumentException("Language with name " + request.name() + " already exists");
        }
    }

    private Language buildLanguageFromRequest(LanguageRequest request) {
        return Language.builder()
                .code(request.code())
                .name(request.name())
                .isActive(request.isActive() != null ? request.isActive() : true)
                .build();
    }
}
