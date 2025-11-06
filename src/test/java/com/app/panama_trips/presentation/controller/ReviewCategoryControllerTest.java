package com.app.panama_trips.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.app.panama_trips.service.implementation.ReviewCategoryService;

@WebMvcTest(ReviewCategoryController.class)
public class ReviewCategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReviewCategoryService reviewCategoryService;
}