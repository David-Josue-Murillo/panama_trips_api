package com.app.panama_trips.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.panama_trips.persistence.repository.LanguageRepository;

@ExtendWith(MockitoExtension.class)
public class LanguageServiceTest {
    
    @Mock
    private LanguageRepository repository;
}
