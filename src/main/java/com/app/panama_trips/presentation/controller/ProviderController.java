package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.ProviderRequest;
import com.app.panama_trips.presentation.dto.ProviderResponse;
import com.app.panama_trips.service.implementation.ProviderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @GetMapping
    public ResponseEntity<Page<ProviderResponse>> findAllProviders(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination
    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.providerService.getAllProviders(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderResponse> findProviderById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.providerService.getProviderById(id));
    }

    @GetMapping("/name")
    public ResponseEntity<ProviderResponse> findProviderByName(@RequestParam String q) {
        return ResponseEntity.ok(this.providerService.getProviderByName(q));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countProviders() {
        return ResponseEntity.ok(this.providerService.countProviders());
    }

    @PostMapping
    public ResponseEntity<ProviderResponse> saveProvider(@Valid @RequestBody ProviderRequest providerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.providerService.saveProvider(providerRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProviderResponse> updateProvider(@PathVariable Integer id, @Valid @RequestBody ProviderRequest providerRequest) {
        return ResponseEntity.ok(this.providerService.updateProvider(id, providerRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable Integer id) {
        this.providerService.deleteProvider(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ruc")
    public ResponseEntity<ProviderResponse> findProviderByRuc(@RequestParam String q) {
        return ResponseEntity.ok(this.providerService.getProviderByRuc(q));
    }

    @GetMapping("/email")
    public ResponseEntity<ProviderResponse> findProviderByEmail(@RequestParam String q) {
        return ResponseEntity.ok(this.providerService.getProviderByEmail(q));
    }

    @GetMapping("/phone")
    public ResponseEntity<ProviderResponse> findProviderByPhone(@RequestParam String q) {
        return ResponseEntity.ok(this.providerService.getProviderByPhone(q));
    }

    @GetMapping("/province/{id}")
    public ResponseEntity<List<ProviderResponse>> findProvidersByProvince(@PathVariable Integer id) {
        return ResponseEntity.ok(this.providerService.getProvidersByProvinceId(id));
    }

    @GetMapping("/district/{id}")
    public ResponseEntity<List<ProviderResponse>> findProvidersByDistrict(@PathVariable Integer id) {
        return ResponseEntity.ok(this.providerService.getProvidersByDistrictId(id));
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<List<ProviderResponse>> findProvidersByAddress(@PathVariable Integer id) {
        return ResponseEntity.ok(this.providerService.getProvidersByAddressId(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProviderResponse>> searchProviders(@RequestParam String q) {
        return ResponseEntity.ok(this.providerService.getProvidersByNameFragment(q));
    }
}
