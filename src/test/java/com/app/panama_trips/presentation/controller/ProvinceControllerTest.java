package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.persistence.entity.Province;
import com.app.panama_trips.service.implementation.ProvinceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ProvinceControllerTest {

    @Mock
    private ProvinceService provinceService;

    @InjectMocks
    private ProvinceController provinceController;

    @Test
    void findAllProvinces_shouldReturnAllProvinces() {
        // Given
        List<Province> provinces = DataProvider.provinceListsMock;
        when(provinceService.getAllProvinces()).thenReturn(provinces);

        // When
        ResponseEntity<List<Province>> response = provinceController.findAllProvinces();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(provinces, response.getBody());
    }

    @Test
    void saveProvince_shouldReturnCreatedProvince() {
        // Given
        when(provinceService.saveProvince(DataProvider.provinceBocasMock)).thenReturn(DataProvider.provinceBocasMock);

        // When
        ResponseEntity<Province> response = provinceController.saveProvince(DataProvider.provinceBocasMock);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(DataProvider.provinceBocasMock, response.getBody());
    }

    @Test
    void saveProvince_shouldReturnBadRequestWhenProvinceIsNull() {
        // Given
        when(provinceService.saveProvince(null)).thenReturn(null);

        // When
        ResponseEntity<Province> response = provinceController.saveProvince(null);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void findProvinceById_shouldReturnProvinceWhenFound() {
        // Given
        when(provinceService.getProvinceById(1)).thenReturn(DataProvider.provinceBocasMock);

        // When
        ResponseEntity<Province> response = provinceController.findProvinceById(1);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(DataProvider.provinceBocasMock, response.getBody());
    }

    @Test
    void findProvinceById_shouldReturnNotFoundWhenNotFound() {
        // Given
        when(provinceService.getProvinceById(anyInt())).thenReturn(null);

        // When
        ResponseEntity<Province> response = provinceController.findProvinceById(99);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findProvinceByName_shouldReturnProvinceWhenFound() {
        // Given
        when(provinceService.getProvinceByName("Province1")).thenReturn(DataProvider.provinceBocasMock);

        // When
        ResponseEntity<Province> response = provinceController.findProvinceByName("Province1");

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(DataProvider.provinceBocasMock, response.getBody());
    }

    @Test
    void findProvinceByName_shouldReturnNotFoundWhenNotFound() {
        // Given
        when(provinceService.getProvinceByName("Province1")).thenReturn(null);

        // When
        ResponseEntity<Province> response = provinceController.findProvinceByName("Province1");

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateProvince_shouldReturnUpdatedProvince() {
        // Given
        when(provinceService.updateProvince(1, DataProvider.provinceBocasMock)).thenReturn(DataProvider.provinceBocasMock);

        // When
        ResponseEntity<Province> response = provinceController.updateProvince(1, DataProvider.provinceBocasMock);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(DataProvider.provinceBocasMock, response.getBody());
    }

    @Test
    void updateProvince_shouldReturnNullWhenProvinceIsNull() {
        // Given
        when(provinceService.updateProvince(1, null)).thenReturn(null);

        // When
        ResponseEntity<Province> response = provinceController.updateProvince(1, null);

        // Then
        assertNull(response.getBody());
    }

    @Test
    void deleteProvince_shouldReturnNoContentWhenDeleted() {
        // When
        ResponseEntity<Void> response = provinceController.deleteProvince(1);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
