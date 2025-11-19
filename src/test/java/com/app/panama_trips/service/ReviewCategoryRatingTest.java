package com.app.panama_trips.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.panama_trips.persistence.repository.ReviewCategoryRatingRepository;
import com.app.panama_trips.persistence.repository.ReviewCategoryRepository;
import com.app.panama_trips.persistence.repository.ReviewRepository;
import com.app.panama_trips.service.implementation.ReviewCategoryRatingService;

@ExtendWith(MockitoExtension.class)
public class ReviewCategoryRatingTest {

    @Mock
    ReviewCategoryRatingRepository repository;

    @Mock
    ReviewRepository reviewRepository;

    ReviewCategoryRepository reviewCategoryRepository;

    @InjectMocks
    private ReviewCategoryRatingService service;
}
