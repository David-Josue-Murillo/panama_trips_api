package com.app.panama_trips.presentation.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.panama_trips.service.implementation.LanguageService;

@RestController
@RequestMapping("/api/languages")
public class LanguageController {
    private final LanguageService service;
}
