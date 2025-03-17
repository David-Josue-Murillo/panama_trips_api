package com.app.panama_trips.service;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Address;
import com.app.panama_trips.persistence.entity.Street;
import com.app.panama_trips.persistence.repository.AddressRepository;
import com.app.panama_trips.persistence.repository.DistrictRepository;
import com.app.panama_trips.presentation.dto.AddressResponse;
import com.app.panama_trips.service.implementation.AddressService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private DistrictRepository districtRepository;

    @InjectMocks
    private AddressService addressService;

    @Test
    void getAllAddresses_shouldReturnAllAddresses() {
        // When
        List<Address> addressList = DataProvider.addressListsMock;
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Address> pageMock = new PageImpl<>(addressList, pageable, addressList.size());

        when(addressRepository.findAll(pageable)).thenReturn(pageMock);
        Page<AddressResponse> result = addressService.getAllAddresses(Pageable.ofSize(10));

        // Then
        assertNotNull(result);
        assertEquals(10, result.getSize());
        assertEquals(3, result.getTotalElements());
    }

    @Test
    void getAddressById_shouldReturnAddressById() {
        // When
        when(addressRepository.findById(anyInt())).thenReturn(java.util.Optional.of(DataProvider.addressOneMock));
        AddressResponse address = addressService.getAddressById(1);

        // Then
        assertNotNull(address);
        assertEquals("Street One", address.street());
    }

    @Test
    void getAddressById_shouldThrowExceptionWhenNotFoundId() {
        // When
        when(addressRepository.findById(99)).thenReturn(java.util.Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> addressService.getAddressById(99));

        // Then
        assertEquals("Address with 99 not found", exception.getMessage());
    }

    @Test
    void getAddressByStreet_shouldReturnAddressByStreet() {
        // When
        when(addressRepository.findByStreet(anyString())).thenReturn(java.util.Optional.of(DataProvider.addressOneMock));
        AddressResponse address = addressService.getAddressByStreet("Street One");

        // Then
        assertNotNull(address);
        assertEquals("Street One", address.street());
    }

    @Test
    void getAddressByStreet_shouldThrowExceptionWhenNotFoundStreet() {
        // When
        when(addressRepository.findByStreet(anyString())).thenReturn(java.util.Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> addressService.getAddressByStreet("Street"));

        // Then
        assertEquals("Address with Street not found", exception.getMessage());
    }

    @Test
    void getAddressesByDistrictId_shouldReturnAddressesByDistrictId() {
        // When
        when(addressRepository.findByDistrictId(anyInt())).thenReturn(DataProvider.addressListsMock);
        List<AddressResponse> addressList = addressService.getAddressesByDistrictId(1);

        // Then
        assertNotNull(addressList);
        assertEquals(3, addressList.size());
    }

    @Test
    void getAddressesByPostalCode_shouldReturnAddressesByPostalCode() {
        // When
        when(addressRepository.findAddressesByPostalCode(anyString())).thenReturn(DataProvider.addressListsMock);
        List<AddressResponse> addressList = addressService.getAddressesByPostalCode("12345");

        // Then
        assertNotNull(addressList);
        assertEquals(3, addressList.size());
    }

    @Test
    void getAddressesByStreetContainingIgnoreCase_shouldReturnAddressesByStreetContainingIgnoreCase() {
        // When
        when(addressRepository.findByStreetContainingIgnoreCase(anyString())).thenReturn(DataProvider.addressListsMock);
        List<AddressResponse> addressList = addressService.getAddressesByStreetContainingIgnoreCase("Street");

        // Then
        assertNotNull(addressList);
        assertEquals(3, addressList.size());
    }

    @Test
    void saveAddress_shouldReturnAddress() {
        // When
        when(districtRepository.findById(anyInt())).thenReturn(Optional.ofNullable(DataProvider.districtAlmiranteMock));
        when(addressRepository.save(any(Address.class))).thenReturn(DataProvider.addressOneMock);
        AddressResponse address = addressService.saveAddress(DataProvider.addressRequestMock);

        // Then
        assertNotNull(address);
        assertEquals("Street One", address.street());
    }

    @Test
    void saveAddress_shouldThrowExceptionWhenDistrictDoesNotExist() {
        // When
        when(districtRepository.findById(anyInt())).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> addressService.saveAddress(DataProvider.addressRequestMock));


        // Then
        assertNotNull(exception);
        assertEquals("District with id 1 not found", exception.getMessage());
    }

    @Test
    void updateAddress_shouldUpdateAddressWhenAddressExists() {
        // Given
        Address address = DataProvider.addressOneMock;
        Address updatedAddress = DataProvider.addressOneMock;
        updatedAddress.setStreet("Updated");

        // When
        when(districtRepository.findById(anyInt())).thenReturn(Optional.ofNullable(DataProvider.districtAlmiranteMock));
        when(addressRepository.findById(anyInt())).thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class))).thenReturn(updatedAddress);

        // Then
        AddressResponse result = addressService.updateAddress(1, DataProvider.addressRequestMock);
        assertEquals(1, result.addressId());
    }

    @Test
    void deleteAddress_shouldDeleteAddressWhenAddressExists() {
        // Given
        Address address = DataProvider.addressOneMock;

        // When
        when(addressRepository.existsById(anyInt())).thenReturn(true);

        // Then
        addressService.deleteAddress(1);
        verify(addressRepository, times(1)).deleteById(1);
    }
}
