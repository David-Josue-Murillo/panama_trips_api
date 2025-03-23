package com.app.panama_trips.service;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.Region;
import com.app.panama_trips.persistence.repository.ComarcaRepository;
import com.app.panama_trips.persistence.repository.ProvinceRepository;
import com.app.panama_trips.persistence.repository.RegionRepository;
import com.app.panama_trips.presentation.dto.RegionResponse;
import com.app.panama_trips.service.implementation.RegionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static com.app.panama_trips.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegionServiceTest {

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private ProvinceRepository provinceRepository;

    @Mock
    private ComarcaRepository comarcaRepository;

    @InjectMocks
    private RegionService regionService;

    @Test
    void getAllRegions_shouldReturnAllRegions() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Region> page = new PageImpl<>(regionListsMock, pageable, regionListsMock.size());
        when(regionRepository.findAll(pageable)).thenReturn(page);

        // when
        Page<RegionResponse> result = regionService.getAllRegions(pageable);

        // then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(regionListsMock.size(), result.getContent().size());
    }

    @Test
    void getRegionById_shouldReturnRegion() {
        // given
        Region region = regionOneMock;
        when(regionRepository.findById(anyInt())).thenReturn(Optional.of(region));

        // when
        RegionResponse result = regionService.getRegionById(1);

        // then
        assertNotNull(result);
        assertEquals(region.getId(), result.id());
        assertEquals(region.getName(), result.name());
    }

    @Test
    void getRegionById_shouldThrowException() {
        // given
        when(regionRepository.findById(anyInt())).thenReturn(Optional.empty());

        // when
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> regionService.getRegionById(1));

        // then
        assertNotNull(exception);
        assertEquals("Region with id 1 not found", exception.getMessage());
    }

    @Test
    void saveRegion_shouldSaveRegion() {
        // given
        when(regionRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);
        when(provinceRepository.existsById(anyInt())).thenReturn(true);
        when(comarcaRepository.existsById(anyInt())).thenReturn(true);
        when(provinceRepository.findById(anyInt())).thenReturn(Optional.of(provinceBocasMock));
        when(comarcaRepository.findById(anyInt())).thenReturn(Optional.of(comarcaOneMock));
        when(regionRepository.save(any(Region.class))).thenReturn(regionOneMock);

        // when
        RegionResponse result = regionService.saveRegion(regionRequestMock);

        // then
        assertNotNull(result);
        assertEquals(regionRequestMock.name(), result.name());
    }

    @Test
    void updateRegion_shouldUpdateRegion() {
        // given
        when(regionRepository.findById(anyInt())).thenReturn(Optional.of(regionOneMock));
        when(provinceRepository.existsById(anyInt())).thenReturn(true);
        when(comarcaRepository.existsById(anyInt())).thenReturn(true);
        when(provinceRepository.findById(anyInt())).thenReturn(Optional.of(provinceBocasMock));
        when(comarcaRepository.findById(anyInt())).thenReturn(Optional.of(comarcaOneMock));
        when(regionRepository.save(any(Region.class))).thenReturn(regionOneMock);

        // when
        RegionResponse result = regionService.updateRegion(1, regionRequestMock);

        // then
        assertNotNull(result);
        assertEquals(regionRequestMock.name(), result.name());
    }

    @Test
    void deleteRegion_shouldDeleteRegion() {
        // given
        when(regionRepository.existsById(anyInt())).thenReturn(true);

        // when
        regionService.deleteRegion(1);

        // then
        verify(regionRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void getRegionByName_shouldReturnRegion() {
        // given
        Region region = regionOneMock;
        when(regionRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(region));

        // when
        RegionResponse result = regionService.getRegionByName("Region Name");

        // then
        assertNotNull(result);
        assertEquals(region.getName(), result.name());
    }

    @Test
    void getRegionsByName_shouldReturnRegions() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Region> page = new PageImpl<>(regionListsMock, pageable, regionListsMock.size());
        when(regionRepository.findByNameContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(page);

        // when
        Page<RegionResponse> result = regionService.getRegionsByName("Region", pageable);

        // then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(regionListsMock.size(), result.getContent().size());
    }

    @Test
    void getRegionByProvinceId_shouldReturnRegions() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Region> page = new PageImpl<>(regionListsMock, pageable, regionListsMock.size());
        when(regionRepository.findByProvince_Id(anyInt(), any(Pageable.class))).thenReturn(page);

        // when
        Page<RegionResponse> result = regionService.getRegionByProvinceId(1, pageable);

        // then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(regionListsMock.size(), result.getContent().size());
    }

    @Test
    void getRegionByComarcaId_shouldReturnRegions() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Region> page = new PageImpl<>(regionListsMock, pageable, regionListsMock.size());
        when(regionRepository.findByComarca_Id(anyInt(), any(Pageable.class))).thenReturn(page);

        // when
        Page<RegionResponse> result = regionService.getRegionByComarcaId(1, pageable);

        // then
        assertNotNull(result);
        assertEquals(3, result.getContent().size());
        assertEquals(regionListsMock.size(), result.getContent().size());
    }

    @Test
    void countRegions_shouldReturnCount() {
        // given
        when(regionRepository.count()).thenReturn(10L);

        // when
        Long result = regionService.countRegions();

        // then
        assertNotNull(result);
        assertEquals(10L, result);
    }

    @Test
    void existsRegionByName_shouldReturnTrue() {
        // given
        when(regionRepository.existsByNameIgnoreCase(anyString())).thenReturn(true);

        // when
        boolean result = regionService.existsRegionByName("Region Name");

        // then
        assertTrue(result);
    }
}