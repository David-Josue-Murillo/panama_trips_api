package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.presentation.dto.AddressRequest;
import com.app.panama_trips.presentation.dto.AddressResponse;
import com.app.panama_trips.service.implementation.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<Page<AddressResponse>> findAllAddresses(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "false") Boolean enabledPagination

    ) {
        Pageable pageable = enabledPagination
                ? Pageable.ofSize(size).withPage(page)
                : Pageable.unpaged();
        return ResponseEntity.ok(this.addressService.getAllAddresses(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> findAddressById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.addressService.getAddressById(id));
    }

    @GetMapping("/address")
    public ResponseEntity<AddressResponse> findAddressByStreet(@RequestParam String street) {
        return ResponseEntity.ok(this.addressService.getAddressByStreet(street));
    }

    @GetMapping("/district/{districtId}")
    public ResponseEntity<List<AddressResponse>> findAddressesByDistrictId(@PathVariable Integer districtId) {
        return ResponseEntity.ok(this.addressService.getAddressesByDistrictId(districtId));
    }

    @GetMapping("/postal-code/{postalCode}")
    public ResponseEntity<List<AddressResponse>> findAddressesByPostalCode(@PathVariable String postalCode) {
        return ResponseEntity.ok(this.addressService.getAddressesByPostalCode(postalCode));
    }

    @GetMapping("/search")
    public ResponseEntity<List<AddressResponse>> findAddressByStreetContainingIgnoreCase(@RequestParam String street) {
        return ResponseEntity.ok(this.addressService.getAddressesByStreetContainingIgnoreCase(street));
    }

    @PostMapping
    public ResponseEntity<AddressResponse> saveAddress(@Valid @RequestBody AddressRequest addressRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.addressService.saveAddress(addressRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable Integer id, @Valid @RequestBody AddressRequest addressRequest) {
        return ResponseEntity.ok(this.addressService.updateAddress(id, addressRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Integer id) {
        this.addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
