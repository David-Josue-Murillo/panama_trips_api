package com.app.panama_trips.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.panama_trips.service.implementation.ReviewCategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/review-categories")
@RequiredArgsConstructor
public class ReviewCategoryController {
  private final ReviewCategoryService service;

  

}
