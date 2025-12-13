package com.app.panama_trips.service.implementation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.app.panama_trips.presentation.dto.LanguageRequest;
import com.app.panama_trips.presentation.dto.LanguageResponse;
import com.app.panama_trips.service.interfaces.ILanguageService;

public class LanguageService implements ILanguageService {

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

}
