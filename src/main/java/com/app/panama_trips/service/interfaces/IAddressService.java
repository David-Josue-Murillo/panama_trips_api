package com.app.panama_trips.service.interfaces;

import com.app.panama_trips.presentation.dto.AddressRequest;
import com.app.panama_trips.presentation.dto.AddressResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAddressService {
    Page<AddressResponse> getAllAddresses(Pageable pageable);
    AddressResponse getAddressById(Integer id);
    AddressResponse getAddressByStreet(String street);
    List<AddressResponse> getAddressesByDistrictId(Integer districtId);
    List<AddressResponse> getAddressesByPostalCode(String postalCode);
    List<AddressResponse> getAddressesByStreetContainingIgnoreCase(String streetFragment);
    AddressResponse saveAddress(AddressRequest address);
    AddressResponse updateAddress(Integer id, AddressRequest address);
    void deleteAddress(Integer id);
}
