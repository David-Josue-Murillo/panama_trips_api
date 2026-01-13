package com.app.panama_trips.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.panama_trips.service.implementation.TourTranslationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tour-translations")
@RequiredArgsConstructor
public class TourTranslationController {
    private final TourTranslationService service;

}
