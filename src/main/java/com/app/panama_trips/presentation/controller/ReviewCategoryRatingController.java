package com.app.panama_trips.presentation.controller;


import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.panama_trips.service.implementation.ReviewCategoryRatingService;

@RestController
@RequestMapping("/api/review-category-ratings")
@RequiredArgsConstructor
public class ReviewCategoryRatingController {

    private final ReviewCategoryRatingService reviewCategoryRatingService;
}
