package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.service.implementation.TourPlanAvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tour-plan-availability")
@RequiredArgsConstructor
public class TourPlanAvailabilityController {

    private final TourPlanAvailabilityService tourPlanAvailabilityService;
}
