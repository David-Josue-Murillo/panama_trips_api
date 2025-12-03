package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.ReviewCategoryRatingRequest;
import com.app.panama_trips.presentation.dto.ReviewCategoryRatingResponse;
import com.app.panama_trips.service.implementation.ReviewCategoryRatingService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.app.panama_trips.DataProvider.reviewCategoryRatingRequestMock;
import static com.app.panama_trips.DataProvider.reviewCategoryRatingResponseListMock;
import static com.app.panama_trips.DataProvider.reviewCategoryRatingResponseMock;


@WebMvcTest(ReviewCategoryRatingController.class)
public class ReviewCategoryRatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewCategoryRatingService reviewCategoryService;

    private ReviewCategoryRatingRequest request;
    private ReviewCategoryRatingResponse response;
    private List<ReviewCategoryRatingResponse> responseList;

    @BeforeEach
    void setUp() {
        request = reviewCategoryRatingRequestMock();
        response = reviewCategoryRatingResponseMock();
        responseList = reviewCategoryRatingResponseListMock();
    }
}