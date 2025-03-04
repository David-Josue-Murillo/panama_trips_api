package com.app.panama_trips.service;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.District;
import com.app.panama_trips.persistence.repository.DistrictRepository;
import com.app.panama_trips.persistence.repository.ProvinceRepository;
import com.app.panama_trips.service.implementation.DistrictService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DistrictServiceTest {

    @Mock
    private DistrictRepository districtRepository;

    @Mock
    private ProvinceRepository provinceRepository;

    @InjectMocks
    private DistrictService districtService;

    @BeforeEach
    void setUp() {
        District district = DataProvider.districtAlmiranteMock;
    }

    @Test
    void getAllDistricts_shouldReturnAllDistricts() {
        // When
        when(districtRepository.findAll()).thenReturn(DataProvider.districtListsMock);
        List<District> districtList = districtService.getAllDistricts();

        // Then
        assertNotNull(districtList);
        assertEquals(5, districtList.size());
    }

    @Test
    void getDistrictById_shouldReturnDistrictById() {
        // When
        when(districtRepository.findById(1)).thenReturn(Optional.of(DataProvider.districtAlmiranteMock));
        District district = districtService.getDistrictById(1);

        // Then
        assertNotNull(district);
        assertEquals("Almirante", district.getName());
    }

    @Test
    void getDistrictById_shouldThrowExceptionWhenNotFoundId() {
        // When
        when(districtRepository.findById(99)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> districtService.getDistrictById(99));

        // Then
        assertEquals("District not found with id 99", exception.getMessage());
    }

    @Test
    void getDistrictByName_shouldReturnDistrictByName() {
        // When
        when(districtRepository.findByName("Almirante")).thenReturn(Optional.of(DataProvider.districtAlmiranteMock));
        District district = districtService.getDistrictByName("Almirante");

        // Then
        assertNotNull(district);
        assertEquals("Almirante", district.getName());
    }

    @Test
    void getDistrictByName_shouldThrowExceptionWhenNotFoundName() {
        // When
        when(districtRepository.findByName("Distrito")).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> districtService.getDistrictByName("Distrito"));

        // Then
        assertEquals("District not found with name Distrito", exception.getMessage());
    }

    @Test
    void getDistrictsByProvinceId_shouldReturnDistrictsByProvinceId() {
        // When
        when(districtRepository.findDistrictByProvinceId_Id(1)).thenReturn(DataProvider.districtListBocasMock);
        List<District> districtList = districtService.getDistrictsByProvinceId(1);

        // Then
        assertNotNull(districtList);
        assertEquals(3, districtList.size());
        assertEquals(1, districtList.get(0).getId());
    }

    @Test
    void getDistrictsByProvinceId_shouldThrowExceptionWhenNotFoundProvinceId() {
        // When
        when(districtRepository.findDistrictByProvinceId_Id(99)).thenReturn(List.of());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> districtService.getDistrictsByProvinceId(99));

        // Then
        assertEquals("District not found with province id 1", exception.getMessage());
    }
}
