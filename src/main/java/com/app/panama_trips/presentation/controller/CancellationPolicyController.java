package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.service.implementation.CancellationPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cancellation-policies")
@RequiredArgsConstructor
public class CancellationPolicyController {

    private final CancellationPolicyService service;
}
