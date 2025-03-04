package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.persistence.entity.District;
import com.app.panama_trips.presentation.dto.DistrictRequest;
import com.app.panama_trips.service.implementation.DistrictService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/districts")
@RequiredArgsConstructor
public class DistrictController {

    private final DistrictService districtService;

    @GetMapping
    public ResponseEntity<List<District>> findAllDistricts() {
        return ResponseEntity.ok(this.districtService.getAllDistricts());
    }

    @PostMapping
    public ResponseEntity<District> saveDistrict(@RequestBody DistrictRequest districtRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.districtService.saveDistrict(districtRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<District> findDistrictById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.districtService.getDistrictById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<District> findDistrictByName(@RequestParam String name) {
        return ResponseEntity.ok(this.districtService.getDistrictByName(name));
    }

    @GetMapping("/province/{provinceId}")
    public ResponseEntity<List<District>> findDistrictsByProvinceId(@PathVariable Integer provinceId) {
        List<District> districts = this.districtService.getDistrictsByProvinceId(provinceId);
        return districts == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(districts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<District> updateDistrict(@PathVariable Integer id, @RequestBody DistrictRequest districtRequest) {
        return ResponseEntity.ok(this.districtService.updateDistrict(id, districtRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistrict(@PathVariable Integer id) {
        this.districtService.deleteDistrict(id);
        return ResponseEntity.noContent().build();
    }
}
