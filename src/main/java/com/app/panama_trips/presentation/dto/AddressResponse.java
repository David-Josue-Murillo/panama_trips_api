package com.app.panama_trips.presentation.dto;

import com.app.panama_trips.persistence.entity.Address;

public record AddressResponse(
        Integer addressId,
        String street,
        String postalCode,
        String districtName,
        String provinceName,
        String additionalInfo
) {
    public AddressResponse(Address address) {
        this(
                address.getAddressId(),
                address.getStreet(),
                address.getPostalCode(),
                address.getDistrict().getName(),  // Nombre del distrito
                address.getDistrict().getProvince().getName(),  // Nombre de la provincia
                address.getAdditionalInfo()
        );
    }
}
