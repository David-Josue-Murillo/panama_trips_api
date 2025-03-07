package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.persistence.entity.District;
import com.app.panama_trips.presentation.dto.DistrictRequest;
import com.app.panama_trips.service.implementation.DistrictService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DistrictControllerTest {

    @Mock
    private DistrictService districtService;

    @InjectMocks
    private DistrictController districtController;

    @Test
    void findAllDistricts_shouldReturnAllDistricts() {
        // Given
        when(districtService.getAllDistricts()).thenReturn(DataProvider.districtListsMock);

        // When
        ResponseEntity<List<District>> response = districtController.findAllDistricts();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(DataProvider.districtListsMock, response.getBody());
    }

    @Test
    void saveDistrict_shouldReturnCreatedDistrict() {
        // Given
        DistrictRequest districtRequest = new DistrictRequest("Almirante", 1);
        when(districtService.saveDistrict(districtRequest)).thenReturn(DataProvider.districtBocasMock);

        // When
        ResponseEntity<District> response = districtController.saveDistrict(districtRequest);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(DataProvider.districtBocasMock, response.getBody());
    }

    @Test
    void findDistrictById_shouldReturnDistrictById() {
        // Given
        when(districtService.getDistrictById(1)).thenReturn(DataProvider.districtBocasMock);

        // When
        ResponseEntity<District> response = districtController.findDistrictById(1);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(DataProvider.districtBocasMock, response.getBody());
    }

    @Test
    void findDistrictByName_shouldReturnDistrictByName() {
        // Given
        when(districtService.getDistrictByName("Almirante")).thenReturn(DataProvider.districtBocasMock);

        // When
        ResponseEntity<District> response = districtController.findDistrictByName("Almirante");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(DataProvider.districtBocasMock, response.getBody());
    }

    @Test
    void findDistrictsByProvinceId_shouldReturnDistrictsByProvinceId() {
        // Given
        when(districtService.getDistrictsByProvinceId(1)).thenReturn(DataProvider.districtListsMock);

        // When
        ResponseEntity<List<District>> response = districtController.findDistrictsByProvinceId(1);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(DataProvider.districtListsMock, response.getBody());
    }

    @Test
    void updateDistrict_shouldReturnUpdatedDistrict() {
        // Given
        DistrictRequest districtRequest = new DistrictRequest("Almirante", 1);
        when(districtService.updateDistrict(1, districtRequest)).thenReturn(DataProvider.districtBocasMock);

        // When
        ResponseEntity<District> response = districtController.updateDistrict(1, districtRequest);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(DataProvider.districtBocasMock, response.getBody());
    }

    @Test
    void deleteDistrict_shouldReturnNoContent() {
        // When
        ResponseEntity<Void> response = districtController.deleteDistrict(1);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}
