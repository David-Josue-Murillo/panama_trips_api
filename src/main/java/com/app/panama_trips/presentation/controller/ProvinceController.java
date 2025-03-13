package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.persistence.entity.Province;
import com.app.panama_trips.presentation.dto.ProvinceRequest;
import com.app.panama_trips.presentation.dto.ProvinceResponse;
import com.app.panama_trips.service.implementation.ProvinceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/provinces")
@RequiredArgsConstructor
@Validated
@Tag(name = "Province", description = "Endpoints for provinces")
public class ProvinceController {

    private final ProvinceService provinceService;

    @GetMapping
    @Operation(
            summary = "Get all provinces",
            description = "Get all provinces in the system",
            tags = {"Province"},
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Provinces found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Province.class)
                    )
            )
    )
    public ResponseEntity<List<ProvinceResponse>> findAllProvinces() {
        return ResponseEntity.ok(this.provinceService.getAllProvinces());
    }

    @PostMapping
    @Operation(
            summary = "Create a new province",
            description = "Create a new province in the system",
            tags = {"Province"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Province data to create",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Province.class)
                    )
            ),
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Province created",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Province.class)
                    )
            )
    )
    public ResponseEntity<ProvinceResponse> saveProvince(@RequestBody ProvinceRequest provinceRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.provinceService.saveProvince(provinceRequest));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a province by id",
            description = "Get a province in the system by its id",
            tags = {"Province"},
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Province found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Province.class)
                    )
            )
    )
    public ResponseEntity<ProvinceResponse> findProvinceById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.provinceService.getProvinceById(id));
    }

    @GetMapping("/search")
    @Operation(
            summary = "Get a province by name",
            description = "Get a province in the system by its name",
            tags = {"Province"},
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Province found",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Province.class)
                    )
            )
    )
    public ResponseEntity<ProvinceResponse> findProvinceByName(@RequestParam String name) {
        return ResponseEntity.ok(this.provinceService.getProvinceByName(name));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a province",
            description = "Update a province in the system",
            tags = {"Province"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Province data to update",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Province.class)
                    )
            ),
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Province updated",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Province.class)
                    )
            )
    )
    public ResponseEntity<ProvinceResponse> updateProvince(@PathVariable Integer id, @RequestBody ProvinceRequest provinceRequest) {
        return ResponseEntity.ok(this.provinceService.updateProvince(id, provinceRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a province",
            description = "Delete a province in the system",
            tags = {"Province"},
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "Province deleted"
            )
    )
    public ResponseEntity<Void> deleteProvince(@PathVariable Integer id) {
        this.provinceService.deleteProvince(id);
        return ResponseEntity.noContent().build();
    }
}
