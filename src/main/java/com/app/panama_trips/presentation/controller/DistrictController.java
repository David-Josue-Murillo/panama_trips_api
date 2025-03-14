package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.persistence.entity.District;
import com.app.panama_trips.presentation.dto.DistrictRequest;
import com.app.panama_trips.presentation.dto.DistrictResponse;
import com.app.panama_trips.service.implementation.DistrictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/districts")
@RequiredArgsConstructor
@Tag(name = "District", description = "Endpoints for districts")
public class DistrictController {

    private final DistrictService districtService;

    @GetMapping
    @Operation(
            summary = "Get all districts",
            description = "Get all districts in the system",
            tags = {"District"},
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Districts found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = District.class)
                    )
            )
    )
    public ResponseEntity<List<DistrictResponse>> findAllDistricts() {
        return ResponseEntity.ok(this.districtService.getAllDistricts());
    }

    @PostMapping
    @Operation(
            summary = "Create a new district",
            description = "Create a new district in the system",
            tags = {"District"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "District data to create",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = DistrictRequest.class)
                    )
            ),
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "District created",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = District.class)
                    )
            )
    )
    public ResponseEntity<DistrictResponse> saveDistrict(@RequestBody DistrictRequest districtRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.districtService.saveDistrict(districtRequest));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a district by id",
            description = "Get a district in the system by its id",
            tags = {"District"},
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "District found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = District.class)
                    )
            )
    )
    public ResponseEntity<DistrictResponse> findDistrictById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.districtService.getDistrictById(id));
    }

    @GetMapping("/search")
    @Operation(
            summary = "Get a district by name",
            description = "Get a district in the system by its name",
            tags = {"District"},
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "District found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = District.class)
                    )
            )
    )
    public ResponseEntity<DistrictResponse> findDistrictByName(@RequestParam String name) {
        return ResponseEntity.ok(this.districtService.getDistrictByName(name));
    }

    @GetMapping("/province/{provinceId}")
    @Operation(
            summary = "Get districts by province id",
            description = "Get districts in the system by province id",
            tags = {"District"},
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Districts found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = District.class)
                    )
            )
    )
    public ResponseEntity<List<DistrictResponse>> findDistrictsByProvinceId(@PathVariable Integer provinceId) {
        List<DistrictResponse> districts = this.districtService.getDistrictsByProvinceId(provinceId);
        return districts == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(districts);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a district",
            description = "Update a district in the system",
            tags = {"District"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "District data to update",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = DistrictRequest.class)
                    )
            ),
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "District updated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = District.class)
                    )
            )
    )
    public ResponseEntity<DistrictResponse> updateDistrict(@PathVariable Integer id, @RequestBody DistrictRequest districtRequest) {
        return ResponseEntity.ok(this.districtService.updateDistrict(id, districtRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a district",
            description = "Delete a district in the system",
            tags = {"District"},
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "District deleted"
            )
    )
    public ResponseEntity<Void> deleteDistrict(@PathVariable Integer id) {
        this.districtService.deleteDistrict(id);
        return ResponseEntity.noContent().build();
    }
}
