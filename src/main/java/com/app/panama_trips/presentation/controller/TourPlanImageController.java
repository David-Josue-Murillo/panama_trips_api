package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.TourPlanImageResponse;
import com.app.panama_trips.service.implementation.TourPlanImageService;
import com.app.panama_trips.service.implementation.TourPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tour-plan-image")
@RequiredArgsConstructor
public class TourPlanImageController {

    private final TourPlanImageService tourPlanImageService;

    @GetMapping()
    public ResponseEntity<Page<TourPlanImageResponse>> findAllTourPlanImages(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.tourPlanImageService.getAllTourPlanImages(pageable));
    }


}
