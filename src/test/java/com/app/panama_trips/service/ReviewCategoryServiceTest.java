package com.app.panama_trips.service;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.panama_trips.persistence.entity.ReviewCategory;
import com.app.panama_trips.persistence.repository.ReviewCategoryRepository;
import com.app.panama_trips.presentation.dto.ReviewCategoryRequest;
import com.app.panama_trips.service.implementation.ReviewCategoryService;

import static com.app.panama_trips.DataProvider.*;

@ExtendWith(MockitoExtension.class)
public class ReviewCategoryServiceTest {
    
    @Mock
    private ReviewCategoryRepository repository;

    @InjectMocks
    private ReviewCategoryService service;

    @Captor
    private ArgumentCaptor<ReviewCategory> categoryCaptor;

    private ReviewCategory category;
    private List<ReviewCategory> categoriesList;
    private ReviewCategoryRequest request;

    @BeforeEach
    void setUp() {
        category = reviewCategoryOneMock();
        category.setId(1);
        categoriesList = reviewCategoryListMock();
        request = reviewCategoryRequestMock();
    }
}
