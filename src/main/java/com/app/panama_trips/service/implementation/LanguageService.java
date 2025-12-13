package com.app.panama_trips.service.implementation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.panama_trips.exception.ResourceNotFoundException;
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
    @Transactional(readOnly = true)
    public Page<LanguageResponse> getAllLanguages(Pageable pageable) {
        return languageRepository.findAll(pageable)
                .map(LanguageResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public LanguageResponse getLanguageByCode(String code) {
        Language language = languageRepository.findById(code)
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with code " + code));
        return new LanguageResponse(language);
    }

    @Override
    @Transactional
    public LanguageResponse saveLanguage(LanguageRequest request) {
        validateLanguage(request);
        Language language = buildLanguageFromRequest(request);
        return new LanguageResponse(languageRepository.save(language));
    }

    @Override
    @Transactional
    public LanguageResponse updateLanguage(String code, LanguageRequest request) {
        Language existingLanguage = languageRepository.findById(code)
                .orElseThrow(() -> new ResourceNotFoundException("Language not found with code " + code));
        updateLanguageFields(existingLanguage, request);
        return new LanguageResponse(languageRepository.save(existingLanguage));
    }

    @Override
    @Transactional
    public void deleteLanguage(String code) {
        if (!languageRepository.existsById(code)) {
            throw new ResourceNotFoundException("Language not found with code " + code);
        }
        languageRepository.deleteById(code);
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

    private void updateLanguageFields(Language language, LanguageRequest request) {
        // Validar que el nombre no esté duplicado (excepto si es el mismo idioma)
        if (!language.getName().equalsIgnoreCase(request.name())
                && languageRepository.existsByNameIgnoreCase(request.name())) {
            throw new IllegalArgumentException("Language with name " + request.name() + " already exists");
        }
        language.setName(request.name());
        if (request.isActive() != null) {
            language.setIsActive(request.isActive());
        }
    }
}
