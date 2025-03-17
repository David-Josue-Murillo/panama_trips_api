package com.app.panama_trips.service.implementation;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Address;
import com.app.panama_trips.persistence.entity.District;
import com.app.panama_trips.persistence.repository.AddressRepository;
import com.app.panama_trips.persistence.repository.DistrictRepository;
import com.app.panama_trips.presentation.dto.AddressRequest;
import com.app.panama_trips.presentation.dto.AddressResponse;
import com.app.panama_trips.service.interfaces.IAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService implements IAddressService {

    private final AddressRepository addressRepository;
    private final DistrictRepository districtRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<AddressResponse> getAllAddresses(Pageable pageable) {
        return this.addressRepository.findAll(pageable)
                .map(AddressResponse::new);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getAddressById(Integer id) {
        return this.addressRepository.findById(id)
                .map(AddressResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("Address with " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getAddressByStreet(String street) {
        return this.addressRepository.findByStreet(street)
                .map(AddressResponse::new)
                .orElseThrow(() -> new ResourceNotFoundException("Address with " + street + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getAddressesByDistrictId(Integer districtId) {
        return this.addressRepository.findByDistrictId(districtId)
                .stream()
                .map(AddressResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getAddressesByPostalCode(String postalCode) {
        return this.addressRepository.findAddressesByPostalCode(postalCode)
                .stream()
                .map(AddressResponse::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getAddressesByStreetContainingIgnoreCase(String streetFragment) {
        return this.addressRepository.findByStreetContainingIgnoreCase(streetFragment)
                .stream()
                .map(AddressResponse::new)
                .toList();
    }

    @Override
    @Transactional
    public AddressResponse saveAddress(AddressRequest address) {
        validateAddress(address);
        Address newAddress = builderAddressFromRequest(address);
        return new AddressResponse(this.addressRepository.save(newAddress));
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(Integer id, AddressRequest address) {
        Address existingAddress = this.addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address with id " + id + " not found"));

        updateAddressFields(existingAddress, address);
        return new AddressResponse(this.addressRepository.save(existingAddress));
    }

    @Override
    @Transactional
    public void deleteAddress(Integer id) {
        if(!this.addressRepository.existsById(id)) {
            throw new ResourceNotFoundException("Address with id " + id + " not found");
        }
        this.addressRepository.deleteById(id);
    }


    // === Private methods ===
    private void validateAddress(AddressRequest addressRequest) {
        if (this.addressRepository.findByStreet(addressRequest.street()).isPresent()) {
            throw new IllegalArgumentException("Address with street " + addressRequest.street() + " already exists");
        }
    }

    private Address builderAddressFromRequest(AddressRequest request) {
        return Address.builder()
                .street(request.street())
                .postalCode(request.postalCode())
                .district(findDistrictOrFail(request.districtId()))
                .additionalInfo(request.additionalInfo())
                .build();
    }

    private District findDistrictOrFail(Integer districtId) {
        return this.districtRepository.findById(districtId)
                .orElseThrow(() -> new ResourceNotFoundException("District with id " + districtId + " not found"));
    }

    private void updateAddressFields(Address address, AddressRequest request) {
        address.setStreet(request.street());
        address.setPostalCode(request.postalCode());
        address.setDistrict(
                this.districtRepository.findById(request.districtId())
                        .orElseThrow(() -> new ResourceNotFoundException("District with id " + request.districtId() + " not found"))
        );
        address.setAdditionalInfo(request.additionalInfo());
    }
}
