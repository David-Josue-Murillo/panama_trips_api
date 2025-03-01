package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.persistence.entity.Province;
import com.app.panama_trips.service.implementation.ProvinceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {

    private final ProvinceService provinceService;

    public ProvinceController(ProvinceService provinceService) {
        this.provinceService = provinceService;
    }

    @GetMapping
    public ResponseEntity<List<Province>> findAllProvinces() {
        return ResponseEntity.ok(this.provinceService.getAllProvinces());
    }

    @PostMapping
    public ResponseEntity<Province> saveProvince(@RequestBody Province province) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.provinceService.saveProvince(province));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Province> findProvinceById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.provinceService.getProvinceById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Province> findProvinceByName(@RequestParam String name) {
        return ResponseEntity.ok(this.provinceService.getProvinceByName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Province> updateProvince(@PathVariable Integer id, @RequestBody Province province) {
        return ResponseEntity.ok(this.provinceService.updateProvince(id, province));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvince(@PathVariable Integer id) {
        this.provinceService.deleteProvince(id);
        return ResponseEntity.noContent().build();
    }
}
