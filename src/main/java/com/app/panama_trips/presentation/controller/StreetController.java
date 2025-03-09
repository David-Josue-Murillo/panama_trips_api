package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.StreetRequest;
import com.app.panama_trips.presentation.dto.StreetResponse;
import com.app.panama_trips.service.implementation.StreetService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/streets")
@RequiredArgsConstructor
@Validated
public class StreetController {

    private final StreetService streetService;

    @GetMapping
    public ResponseEntity<Page<StreetResponse>> findAllStreet(
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination) {
        return ResponseEntity.ok(streetService.getAllStreet(page, size, enabledPagination));
    }

    @PostMapping
    public ResponseEntity<StreetResponse> createStreet(@RequestBody @Valid StreetRequest streetRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.streetService.saveStreet(streetRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StreetResponse> findStreetById(@PathVariable @Min(1) Integer id) {
        return ResponseEntity.ok(this.streetService.getStreetById(id));
    }

    @GetMapping("/district/{districtId}")
    public ResponseEntity<List<StreetResponse>> findStreetByDistrictId(@PathVariable @Min(1) Integer districtId) {
        return ResponseEntity.ok(this.streetService.getAllStreetByDistrictId(districtId));
    }

    @GetMapping("/search")
    public ResponseEntity<StreetResponse> findStreetByName(@RequestParam @Pattern(regexp = "^[A-Za-zÀ-ÿ0-9 ]+$") String name) {
        return ResponseEntity.ok(this.streetService.getStreetByName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StreetResponse> updateStreet(@PathVariable @Min(1) Integer id, @RequestBody @Valid StreetRequest streetRequest) {
        return ResponseEntity.ok(this.streetService.updateStreet(id, streetRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStreet(@PathVariable @Min(1) Integer id) {
        this.streetService.deleteStreet(id);
        return ResponseEntity.noContent().build();
    }
}
