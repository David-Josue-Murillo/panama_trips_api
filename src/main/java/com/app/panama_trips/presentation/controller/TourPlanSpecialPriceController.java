package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.service.implementation.TourPlanSpecialPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tour-plan-special-price")
@RequiredArgsConstructor
public class TourPlanSpecialPriceController {

    private final TourPlanSpecialPriceService service;

    // Define your endpoints here, for example:
}
