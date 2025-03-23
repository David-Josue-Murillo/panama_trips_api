package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.RegionRequest;
import com.app.panama_trips.presentation.dto.RegionResponse;
import com.app.panama_trips.service.implementation.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/region")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping()
    public ResponseEntity<Page<RegionResponse>> findAllRegions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? PageRequest.of(page, size)
                : Pageable.unpaged();
        return ResponseEntity.ok(regionService.getAllRegions(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionResponse> findRegionById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.regionService.getRegionById(id));
    }

    @PostMapping
    public ResponseEntity<RegionResponse> saveRegion(@RequestBody RegionRequest regionRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.regionService.saveRegion(regionRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionResponse> updateRegion(@PathVariable Integer id, @RequestBody RegionRequest regionRequest) {
        return ResponseEntity.ok(this.regionService.updateRegion(id, regionRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Integer id) {
        this.regionService.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/name")
    public ResponseEntity<RegionResponse> findAllRegionByName(@RequestParam String q) {
        return ResponseEntity.ok(this.regionService.getRegionByName(q));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<RegionResponse>> searchRegionByName(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean enabledPagination,
            @RequestParam String q
    ) {
        Pageable pageable = enabledPagination
                ? PageRequest.of(page, size)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.regionService.getRegionsByName(q, pageable));
    }

    @GetMapping("/province/{provinceId}")
    public ResponseEntity<Page<RegionResponse>> findRegionByProvinceId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean enabledPagination,
            @PathVariable Integer provinceId
    ) {
        Pageable pageable = enabledPagination
                ? PageRequest.of(page, size)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.regionService.getRegionByProvinceId(provinceId, pageable));
    }

    @GetMapping("/comarca/{comarcaId}")
    public ResponseEntity<Page<RegionResponse>> findRegionByComarcaId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false") boolean enabledPagination,
            @PathVariable Integer comarcaId
    ) {
        Pageable pageable = enabledPagination
                ? PageRequest.of(page, size)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.regionService.getRegionByComarcaId(comarcaId, pageable));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countRegions() {
        return ResponseEntity.ok(this.regionService.countRegions());
    }
}
