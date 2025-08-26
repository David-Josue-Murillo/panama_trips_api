package com.app.panama_trips.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.panama_trips.service.implementation.TourFaqService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tour-faq")
@RequiredArgsConstructor
public class TourFaqController {

    private final TourFaqService service;

}
