package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.persistence.entity.DailyStatistics;
import com.app.panama_trips.presentation.dto.DailyStatisticsRequest;
import com.app.panama_trips.presentation.dto.DailyStatisticsResponse;
import com.app.panama_trips.service.implementation.DailyStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/daily-statistics")
@RequiredArgsConstructor
@Validated
@Tag(name = "DailyStatistics", description = "Endpoints for daily statistics")
public class DailyStatisticsController {

    private final DailyStatisticsService dailyStatisticsService;

    @GetMapping
    @Operation(
            summary = "Get all daily statistics",
            description = "Get all daily statistics in the system",
            tags = {"DailyStatistics"},
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Daily statistics found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = DailyStatistics.class)
                    )
            )
    )
    public ResponseEntity<List<DailyStatisticsResponse>> findAllDailyStatistics() {
        return ResponseEntity.ok(this.dailyStatisticsService.getAllDailyStatistics());
    }

    @PostMapping
    @Operation(
            summary = "Create a new daily statistic",
            description = "Create a new daily statistic in the system",
            tags = {"DailyStatistics"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Daily statistic data to create",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = DailyStatistics.class)
                    )
            ),
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Daily statistic created",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = DailyStatistics.class)
                    )
            )
    )
    public ResponseEntity<DailyStatisticsResponse> saveDailyStatistic(@RequestBody @Valid DailyStatisticsRequest dailyStatisticsRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.dailyStatisticsService.saveDailyStatistics(dailyStatisticsRequest));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a daily statistic by id",
            description = "Get a daily statistic in the system by its id",
            tags = {"DailyStatistics"},
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Daily statistic found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = DailyStatistics.class)
                    )
            )
    )
    public ResponseEntity<DailyStatisticsResponse> findDailyStatisticById(@PathVariable Long id) {
        return ResponseEntity.ok(this.dailyStatisticsService.getDailyStatisticsById(id));
    }

    @GetMapping("/search")
    @Operation(
            summary = "Get a daily statistic by date",
            description = "Get a daily statistic in the system by its date",
            tags = {"DailyStatistics"},
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Daily statistic found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = DailyStatistics.class)
                    )
            )
    )
    public ResponseEntity<List<DailyStatisticsResponse>> findDailyStatisticByDate(@RequestParam LocalDate dateA, @RequestParam LocalDate dateB) {
        return ResponseEntity.ok(this.dailyStatisticsService.getDailyStatisticsByDate(dateA, dateB));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a daily statistic",
            description = "Update a daily statistic in the system",
            tags = {"DailyStatistics"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Daily statistic data to update",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = DailyStatistics.class)
                    )
            ),
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Daily statistic updated",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = DailyStatistics.class)
                    )
            )
    )
    public ResponseEntity<DailyStatisticsResponse> updateDailyStatistic(@PathVariable Long id, @RequestBody @Valid DailyStatisticsRequest dailyStatisticsRequest) {
        return ResponseEntity.ok(this.dailyStatisticsService.updateDailyStatistics(id, dailyStatisticsRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a daily statistic",
            description = "Delete a daily statistic in the system",
            tags = {"DailyStatistics"},
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "Daily statistic deleted"
            )
    )
    public ResponseEntity<Void> deleteDailyStatistic(@PathVariable Long id) {
        this.dailyStatisticsService.deleteDailyStatistics(id);
        return ResponseEntity.noContent().build();
    }
}
