package com.app.panama_trips.service;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.District;
import com.app.panama_trips.persistence.repository.DistrictRepository;
import com.app.panama_trips.persistence.repository.ProvinceRepository;
import com.app.panama_trips.presentation.dto.DistrictRequest;
import com.app.panama_trips.presentation.dto.DistrictResponse;
import com.app.panama_trips.service.implementation.DistrictService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DistrictServiceTest {

    @Mock
    private DistrictRepository districtRepository;

    @Mock
    private ProvinceRepository provinceRepository;

    @InjectMocks
    private DistrictService districtService;

    @Test
    void getAllDistricts_shouldReturnAllDistricts() {
        // When
        when(districtRepository.findAll()).thenReturn(DataProvider.districtListsMock);
        List<DistrictResponse> districtList = districtService.getAllDistricts();

        // Then
        assertNotNull(districtList);
        assertEquals(5, districtList.size());
    }

    @Test
    void getDistrictById_shouldReturnDistrictById() {
        // When
        when(districtRepository.findById(1)).thenReturn(Optional.of(DataProvider.districtAlmiranteMock));
        DistrictResponse district = districtService.getDistrictById(1);

        // Then
        assertNotNull(district);
        assertEquals("Almirante", district.name());
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
        DistrictResponse district = districtService.getDistrictByName("Almirante");

        // Then
        assertNotNull(district);
        assertEquals("Almirante", district.name());
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
        List<DistrictResponse> districtList = districtService.getDistrictsByProvinceId(1);

        // Then
        assertNotNull(districtList);
        assertEquals(3, districtList.size());
        assertEquals(1, districtList.getFirst().id());
    }

    @Test
    void saveDistrict_shouldSaveAndReturnDistrict() {
        // Given
        DistrictRequest request = new DistrictRequest("Almirante", 1);
        when(provinceRepository.findById(1)).thenReturn(Optional.of(DataProvider.provinceBocasMock));
        when(districtRepository.save(any(District.class))).thenReturn(DataProvider.districtAlmiranteMock);

        // When
        DistrictResponse district = districtService.saveDistrict(request);

        // Then
        assertNotNull(district);
        assertEquals("Almirante", district.name());
    }

    @Test
    void saveDistrict_shouldThrowExceptionWhenAlreadyExistTheDistrict() {
        // Given
        DistrictRequest request = new DistrictRequest("Almirante", 1);
        when(districtRepository.findByNameAndProvinceId_Id("Almirante", 1)).thenReturn(Optional.of(DataProvider.districtAlmiranteMock));

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> districtService.saveDistrict(request));

        // Then
        assertEquals("District with name Almirante already exists in the province", exception.getMessage());
    }

    @Test
    void saveDistrict_shouldThrowExceptionWhenNotFoundProvince() {
        // Given
        DistrictRequest request = new DistrictRequest("Almirante", 1);
        when(provinceRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> districtService.saveDistrict(request));

        // Then
        assertEquals("Province with id 1 not found", exception.getMessage());
    }

    @Test
    void updateDistrict_shouldUpdateAndReturnOneDistrict() {
        // Given
        DistrictRequest request = new DistrictRequest("Almirante", 1);
        when(districtRepository.findById(1)).thenReturn(Optional.of(DataProvider.districtAlmiranteMock));
        when(provinceRepository.findById(1)).thenReturn(Optional.of(DataProvider.provinceBocasMock));
        when(districtRepository.save(any(District.class))).thenReturn(DataProvider.districtAlmiranteMock);

        // When
        DistrictResponse district = districtService.updateDistrict(1, request);

        // Then
        assertNotNull(district);
        assertEquals("Almirante", district.name());
    }

    @Test
    void updateDistrict_shouldThrowExceptionWhenDistrictNotExist() {
        // Given
        DistrictRequest request = new DistrictRequest("Almirante", 1);
        when(districtRepository.findById(1)).thenReturn(Optional.empty());

        // When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> districtService.updateDistrict(1, request));

        // Then
        assertEquals("District not found with id 1", exception.getMessage());
    }

    @Test
    void deleteDistrict_shouldDeleteDistrict() {
        // Given
        when(districtRepository.existsById(1)).thenReturn(true);

        // When
        districtService.deleteDistrict(1);

        // Then
        assertTrue(true);
    }
}
