package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.persistence.entity.Province;
import com.app.panama_trips.service.implementation.ProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/provinces")
@RequiredArgsConstructor
public class ProvinceController {

    private final ProvinceService provinceService;

    @GetMapping
    public ResponseEntity<List<Province>> findAllProvinces() {
        return ResponseEntity.ok(this.provinceService.getAllProvinces());
    }

    @PostMapping
    public ResponseEntity<Province> saveProvince(@RequestBody Province province) {
        return province == null
                ? ResponseEntity.badRequest().build()
                : ResponseEntity.status(HttpStatus.CREATED).body(this.provinceService.saveProvince(province));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Province> findProvinceById(@PathVariable Integer id) {
        Province province = this.provinceService.getProvinceById(id);
        return province == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(province);
    }

    @GetMapping("/search")
    public ResponseEntity<Province> findProvinceByName(@RequestParam String name) {
        Province province = this.provinceService.getProvinceByName(name);
        return province == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(province);
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
