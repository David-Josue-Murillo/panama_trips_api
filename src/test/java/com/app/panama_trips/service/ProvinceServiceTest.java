package com.app.panama_trips.service;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Province;
import com.app.panama_trips.persistence.repository.ProvinceRepository;
import com.app.panama_trips.presentation.dto.ProvinceResponse;
import com.app.panama_trips.service.implementation.ProvinceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static com.app.panama_trips.DataProvider.provinceRequestMock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProvinceServiceTest {

    @Mock
    private ProvinceRepository provinceRepository;

    @InjectMocks
    private ProvinceService provinceService;

    @Test
    void getAllProvinces_shouldReturnAllRecords() {
        // Given
        List<Province> provinceList = DataProvider.provinceListsMock;
        when(provinceRepository.findAll()).thenReturn(provinceList);

        // When
        List<ProvinceResponse> response = provinceService.getAllProvinces();

        // Then
        assertNotNull(response);
        assertEquals(provinceList.size(), response.size());
    }

    @Test
    void getAllProvinces_shouldReturnEmptyList() {
        // Given
        when(provinceRepository.findAll()).thenReturn(List.of());

        // When
        List<ProvinceResponse> response = provinceService.getAllProvinces();

        // Then
        assertTrue(response.isEmpty());
    }

    @Test
    void getProvinceById_shouldReturnOneProvince() {
        // Given
        Province provinceBocas = DataProvider.provinceBocasMock;
        when(provinceRepository.findById(1)).thenReturn(Optional.ofNullable(provinceBocas));

        // When
        ProvinceResponse response = provinceService.getProvinceById(1);

        // Then
        assertNotNull(response);
        assertEquals(provinceBocas.getName(), response.name());
    }

    @Test
    void getProvinceByName_shouldReturnOneProvinceByName() {
        // Given
        Province provinceBocas = DataProvider.provinceBocasMock;
        when(provinceRepository.findByName("Bocas del Toro")).thenReturn(Optional.ofNullable(provinceBocas));

        // When
        ProvinceResponse response = provinceService.getProvinceByName("Bocas del Toro");

        // Then
        assertNotNull(response);
        assertEquals(provinceBocas.getName(), response.name());
    }

    @Test
    void getProvinceByName_shouldReturnEmptyProvince() {
        // Given
        when(provinceRepository.findByName("EEUU")).thenReturn(Optional.empty());

        // When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> provinceService.getProvinceByName("EEUU"));

        // Then
        assertNotNull(exception);
        assertEquals("Province not found with name EEUU", exception.getMessage());
    }

    @Test
    void saveProvince_shouldSaveAndReturnOneProvince() {
        // Given
        when(provinceRepository.save(any(Province.class))).thenReturn(DataProvider.provinceBocasMock);

        // When
        ProvinceResponse response = provinceService.saveProvince(provinceRequestMock);

        // Then
        assertNotNull(response);
        assertEquals(DataProvider.provinceBocasMock.getName(), response.name());
    }

    @Test
    void saveProvince_shouldThrowExceptionWhenAlreadyExistTheProvince() {
        // Given
        Province provinceBocas = DataProvider.provinceBocasMock;
        when(provinceRepository.findByName(any())).thenReturn(Optional.ofNullable(provinceBocas));

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> provinceService.saveProvince(provinceRequestMock));

        // Then
        assertNotNull(exception);
        assertEquals("Province with name Bocas del Toro already exists", exception.getMessage());
    }

    @Test
    void updateProvince_shouldUpdateAndReturnOneProvince() {
        // Given
        Province provinceBocas = DataProvider.provinceBocasMock;
        Province provinceChiriqui = DataProvider.provinceChiriquiMock;
        when(provinceRepository.findById(1)).thenReturn(Optional.ofNullable(provinceBocas));
        when(provinceRepository.save(provinceBocas)).thenReturn(provinceChiriqui);

        // When
        ProvinceResponse response = provinceService.updateProvince(1, provinceRequestMock);

        // Then
        assertNotNull(response);
        assertEquals(provinceChiriqui.getId(), response.id());
    }

    @Test
    void updateProvince_shouldThrowExceptionWhenProvinceNotExist() {
        // Given
        when(provinceRepository.findById(1)).thenReturn(Optional.empty());

        // When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> provinceService.updateProvince(1, provinceRequestMock));

        // Then
        assertNotNull(exception);
        assertEquals("Province not found with id 1", exception.getMessage());
    }

    @Test
    void deleteProvince_shouldDeleteProvince() {
        // Given
        Province provinceBocas = DataProvider.provinceBocasMock;
        when(provinceRepository.findById(anyInt())).thenReturn(Optional.ofNullable(provinceBocas));
        doNothing().when(provinceRepository).deleteById(1);

        // When
        provinceService.deleteProvince(1);

        // Then
        assertTrue(true);
        verify(provinceRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteProvince_shouldThrowExceptionWhenProvinceNotExist() {
        // Given
        when(provinceRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> provinceService.deleteProvince(99));

        // Then
        assertNotNull(exception);
        assertEquals("Province not found with id 99", exception.getMessage());
    }
}
