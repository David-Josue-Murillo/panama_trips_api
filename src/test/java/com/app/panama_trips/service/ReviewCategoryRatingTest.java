package com.app.panama_trips.service;

import static com.app.panama_trips.DataProvider.reviewCategoryRatingListMock;
import static com.app.panama_trips.DataProvider.reviewCategoryRatingOneMock;
import static com.app.panama_trips.DataProvider.reviewCategoryRatingRequestMock;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.panama_trips.persistence.entity.ReviewCategoryRating;
import com.app.panama_trips.persistence.repository.ReviewCategoryRatingRepository;
import com.app.panama_trips.persistence.repository.ReviewCategoryRepository;
import com.app.panama_trips.persistence.repository.ReviewRepository;
import com.app.panama_trips.presentation.dto.ReviewCategoryRatingRequest;
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

    @Captor
    private ArgumentCaptor<ReviewCategoryRating> categoryRatingCaptor;

    private ReviewCategoryRating categoryRating;
    private List<ReviewCategoryRating> categoryRatingList;
    private ReviewCategoryRatingRequest request;

    @BeforeEach
    void setUp() {
        categoryRating = reviewCategoryRatingOneMock();
        categoryRatingList = reviewCategoryRatingListMock();
        request = reviewCategoryRatingRequestMock();
    }
}
