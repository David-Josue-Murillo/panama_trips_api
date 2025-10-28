package com.app.panama_trips.service;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.panama_trips.persistence.entity.ReviewCategory;
import com.app.panama_trips.persistence.repository.ReviewCategoryRepository;
import com.app.panama_trips.service.implementation.ReviewCategoryService;

import static com.app.panama_trips.DataProvider.*;

@ExtendWith(MockitoExtension.class)
public class ReviewCategoryServiceTest {
    
    @Mock
    private ReviewCategoryRepository repository;

    @InjectMocks
    private ReviewCategoryService service;

    private ReviewCategory category;
    private List<ReviewCategory> categoriesList;

    @BeforeEach
    void setUp() {
        category = reviewCategoryOneMock();
        categoriesList = reviewCategoryListMock();
    }
}
