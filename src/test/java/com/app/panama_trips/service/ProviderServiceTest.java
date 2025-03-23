package com.app.panama_trips.service;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Provider;
import com.app.panama_trips.persistence.repository.AddressRepository;
import com.app.panama_trips.persistence.repository.DistrictRepository;
import com.app.panama_trips.persistence.repository.ProviderRepository;
import com.app.panama_trips.persistence.repository.ProvinceRepository;
import com.app.panama_trips.presentation.dto.ProviderResponse;
import com.app.panama_trips.service.implementation.ProviderService;
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

import static com.app.panama_trips.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private ProvinceRepository provinceRepository;

    @Mock
    private DistrictRepository districtRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private ProviderService providerService;

    @Test
    void getAllProviders_shouldReturnAllProviders() {
        // Given
        List<Provider> providerList = providerListsMock;
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Provider> pageMock = new PageImpl<>(providerList, pageable, providerList.size());

        // When
        when(providerRepository.findAll(pageable)).thenReturn(pageMock);
        Page<ProviderResponse> result = providerService.getAllProviders(pageable);

        // Then
        assertNotNull(result);
        assertEquals(10, result.getSize());
        assertEquals(providerList.size(), result.getTotalElements());
    }

    @Test
    void getProviderById_shouldReturnProviderById() {
        // When
        when(providerRepository.findById(anyInt())).thenReturn(java.util.Optional.of(providerOneMock));
        ProviderResponse result = providerService.getProviderById(1);

        // Then
        assertNotNull(result);
        assertEquals(providerOneMock.getId(), result.id());
        assertEquals(providerOneMock.getName(), result.name());
    }

    @Test
    void getProviderById_shouldThrowExceptionWhenNotFoundId() {
        // When
        when(providerRepository.findById(anyInt())).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> providerService.getProviderById(999));

        // Then
        assertNotNull(exception);
        assertEquals("Provider with 999 not found", exception.getMessage());
    }

    @Test
    void saveProvider_shouldReturnProviderResponse() {
        // When
        when(provinceRepository.findById(anyInt())).thenReturn(Optional.of(provinceCocleMock));
        when(districtRepository.findById(anyInt())).thenReturn(Optional.of(districtBocasMock));
        when(addressRepository.findById(anyInt())).thenReturn(Optional.of(addressOneMock));
        when(providerRepository.save(any(Provider.class))).thenReturn(providerOneMock);
        ProviderResponse result = providerService.saveProvider(DataProvider.providerRequestMock);

        // Then
        assertNotNull(result);
        assertEquals(providerOneMock.getId(), result.id());
        assertEquals(providerOneMock.getName(), result.name());
    }

    @Test
    void saveProvider_shouldThrowExceptionWhenExistsEmail() {
        // When
        when(providerRepository.existsProviderByEmail(any())).thenReturn(true);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> providerService.saveProvider(providerRequestMock));

        // Then
        assertNotNull(exception);
        assertEquals("Provider with email "+ providerRequestMock.email() +" already exists", exception.getMessage());
    }

    @Test
    void saveProvider_shouldThrowExceptionWhenExistsRuc() {
        // When
        when(providerRepository.existsProviderByRuc(any())).thenReturn(true);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> providerService.saveProvider(providerRequestMock));

        // Then
        assertNotNull(exception);
        assertEquals("Provider with RUC "+ providerRequestMock.ruc() +" already exists", exception.getMessage());
    }

    @Test
    void updateProvider_shouldReturnProviderResponse() {
        // When
        when(providerRepository.findById(anyInt())).thenReturn(Optional.of(providerOneMock));
        when(provinceRepository.findById(anyInt())).thenReturn(Optional.of(provinceCocleMock));
        when(districtRepository.findById(anyInt())).thenReturn(Optional.of(districtBocasMock));
        when(addressRepository.findById(anyInt())).thenReturn(Optional.of(addressOneMock));
        when(providerRepository.save(any(Provider.class))).thenReturn(providerOneMock);
        ProviderResponse result = providerService.updateProvider(1, providerRequestMock);

        // Then
        assertNotNull(result);
        assertEquals(providerOneMock.getId(), result.id());
        assertEquals(providerOneMock.getName(), result.name());
    }

    @Test
    void updateProvider_shouldThrowExceptionWhenNotFoundId() {
        // When
        when(providerRepository.findById(anyInt())).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> providerService.updateProvider(999, providerRequestMock));

        // Then
        assertNotNull(exception);
        assertEquals("Provider with 999 not found", exception.getMessage());
    }

    @Test
    void deleteProvider_shouldDeleteProviderWhenExists() {
        // When
        when(providerRepository.existsById(anyInt())).thenReturn(true);

        // Then
        providerService.deleteProvider(1);
        verify(providerRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteProvider_shouldThrowExceptionWhenNotFoundId() {
        // When
        when(providerRepository.existsById(anyInt())).thenReturn(false);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> providerService.deleteProvider(999));

        // Then
        assertNotNull(exception);
        assertEquals("Provider with 999 not found", exception.getMessage());
    }

    @Test
    void getProviderByRuc_shouldReturnProviderByRuc() {
        // When
        when(providerRepository.findByRuc(any())).thenReturn(Optional.of(providerOneMock));
        ProviderResponse result = providerService.getProviderByRuc("1-111-1111");

        // Then
        assertNotNull(result);
        assertEquals(providerOneMock.getId(), result.id());
        assertEquals(providerOneMock.getName(), result.name());
    }

    @Test
    void getProviderByRuc_shouldThrowExceptionWhenNotFoundRuc() {
        // When
        when(providerRepository.findByRuc(any())).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> providerService.getProviderByRuc("1-111-1111"));

        // Then
        assertNotNull(exception);
        assertEquals("Provider with 1-111-1111 not found", exception.getMessage());
    }

    @Test
    void getProviderByName_shouldReturnProviderByName() {
        // When
        when(providerRepository.findByName(any())).thenReturn(Optional.of(providerOneMock));
        ProviderResponse result = providerService.getProviderByName("Provider One");

        // Then
        assertNotNull(result);
        assertEquals(providerOneMock.getId(), result.id());
        assertEquals(providerOneMock.getName(), result.name());
    }

    @Test
    void getProviderByName_shouldThrowExceptionWhenNotFoundName() {
        // When
        when(providerRepository.findByName(any())).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> providerService.getProviderByName("Provider One"));

        // Then
        assertNotNull(exception);
        assertEquals("Provider with Provider One not found", exception.getMessage());
    }

    @Test
    void getProviderByEmail_shouldReturnProviderByEmail() {
        when(providerRepository.findByEmail(any())).thenReturn(Optional.of(providerOneMock));
        ProviderResponse result = providerService.getProviderByEmail("provideone@example.com");

        // Then
        assertNotNull(result);
        assertEquals(providerOneMock.getId(), result.id());
        assertEquals(providerOneMock.getName(), result.name());
    }

    @Test
    void getProviderByEmail_shouldThrowExceptionWhenNotFoundEmail() {
        // When
        when(providerRepository.findByEmail(any())).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> providerService.getProviderByEmail("provideone@example.com"));

        // Then
        assertNotNull(exception);
        assertEquals("Provider with provideone@example.com not found", exception.getMessage());
    }

    @Test
    void getProviderByPhone_shouldReturnProviderByPhone() {
        // When
        when(providerRepository.findByPhone(any())).thenReturn(Optional.of(providerOneMock));
        ProviderResponse result = providerService.getProviderByPhone("+507 6111-1111");

        // Then
        assertNotNull(result);
        assertEquals(providerOneMock.getId(), result.id());
        assertEquals(providerOneMock.getName(), result.name());
    }

    @Test
    void getProviderByPhone_shouldThrowExceptionWhenNotFoundPhone() {
        // When
        when(providerRepository.findByPhone(any())).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> providerService.getProviderByPhone("507 6111-1111"));

        // Then
        assertNotNull(exception);
        assertEquals("Provider with +507 6111-1111 not found", exception.getMessage());
    }

    @Test
    void getProvidersByProvinceId_shouldReturnProvidersByProvinceId() {
        // Given
        List<Provider> providerList = providerListsMock;

        // When
        when(providerRepository.findProvidersByProvince_Id(anyInt())).thenReturn(providerList);
        List<ProviderResponse> result = providerService.getProvidersByProvinceId(1);

        // Then
        assertNotNull(result);
        assertEquals(providerList.size(), result.size());
    }

    @Test
    void getProvidersByDistrictId_shouldReturnProvidersByDistrictId() {
        // Given
        List<Provider> providerList = providerListsMock;

        // When
        when(providerRepository.findProvidersByDistrict_Id(anyInt())).thenReturn(providerList);
        List<ProviderResponse> result = providerService.getProvidersByDistrictId(1);

        // Then
        assertNotNull(result);
        assertEquals(providerList.size(), result.size());
    }

    @Test
    void getProvidersByAddressId_shouldReturnProvidersByAddressId() {
        // Given
        List<Provider> providerList = providerListsMock;

        // When
        when(providerRepository.findProvidersByAddress_AddressId(anyInt())).thenReturn(providerList);
        List<ProviderResponse> result = providerService.getProvidersByAddressId(1);

        // Then
        assertNotNull(result);
        assertEquals(providerList.size(), result.size());
    }
    @Test
    void getProvidersByNameFragment_shouldReturnProvidersByNameFragment() {
        // Given
        List<Provider> providerList = providerListsMock;

        // When
        when(providerRepository.findProvidersByNameContainingIgnoreCase(any())).thenReturn(providerList);
        List<ProviderResponse> result = providerService.getProvidersByNameFragment("Provider");

        // Then
        assertNotNull(result);
        assertEquals(providerList.size(), result.size());
    }
}
