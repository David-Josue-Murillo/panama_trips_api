package com.app.panama_trips.service;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.persistence.entity.District;
import com.app.panama_trips.persistence.entity.Street;
import com.app.panama_trips.persistence.repository.DistrictRepository;
import com.app.panama_trips.persistence.repository.StreetRepository;
import com.app.panama_trips.presentation.dto.StreetRequest;
import com.app.panama_trips.presentation.dto.StreetResponse;
import com.app.panama_trips.service.implementation.StreetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class StreetServiceTest {

    @Mock
    private StreetRepository streetRepository;

    @Mock
    private DistrictRepository districtRepository;

    @InjectMocks
    private StreetService streetService;

    @Test
    void getAllStreet_WhenPaginationIsNotEnabled_ThenReturnAllStreets() {
        // Given
        Pageable pageable = Pageable.unpaged();
        PageImpl pageMock = new PageImpl(DataProvider.streetListsMock, pageable, DataProvider.userListMocks().size());
        when(streetRepository.findAll(pageable)).thenReturn(pageMock);

        // When
        Page<StreetResponse> response = this.streetService.getAllStreet(0, 10, false);

        // Then
        assertNotNull(response);
        assertEquals(DataProvider.streetListsMock.size(), response.getContent().size());
    }

    @Test
    void getAllUsers_WhenPaginationIsEnabled_ThenReturnAllStreetWithPagination() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl pageMock = new PageImpl(DataProvider.streetListsMock, pageable, DataProvider.userListMocks().size());
        when(streetRepository.findAll(pageable)).thenReturn(pageMock);

        // When
        Page<StreetResponse> response = this.streetService.getAllStreet(0, 10, true);

        // Then
        assertNotNull(response);
        assertEquals(10, response.getSize());
    }

    @Test
    void getAllStreetByDistrictId_WhenDistrictIdIsProvided_ThenReturnAllStreets() {
        // Given
        when(streetRepository.findByDistrictId_Id(anyInt())).thenReturn(DataProvider.streetListsMock);

        // When
        List<StreetResponse> response = this.streetService.getAllStreetByDistrictId(1);

        // Then
        assertNotNull(response);
        assertEquals(DataProvider.streetListsMock.size(), response.size());
    }

    @Test
    void getStreetById_WhenStreetIdIsProvided_ThenReturnStreet() {
        // Given
        when(streetRepository.findById(anyInt())).thenReturn(Optional.ofNullable(DataProvider.streetOneMock));

        // When
        StreetResponse response = this.streetService.getStreetById(1);

        // Then
        assertNotNull(response);
        assertEquals(DataProvider.streetOneMock.getId(), response.id());
    }

    @Test
    void getStreetById_WhenStreetIdIsNotProvided_ThenThrowResourceNotFoundException() {
        // Given
        when(streetRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> this.streetService.getStreetById(1));

        // Then
        assertEquals("Street with 1 not found", exception.getMessage());
    }

    @Test
    void getStreetByName_WhenStreetNameIsProvided_ThenReturnStreet() {
        // Given
        when(streetRepository.findStreetByName(anyString())).thenReturn(DataProvider.streetOneMock);

        // When
        StreetResponse response = this.streetService.getStreetByName("Street One");

        // Then
        assertNotNull(response);
        assertEquals(DataProvider.streetOneMock.getId(), response.id());
    }

    @Test
    void getStreetByName_WhenStreetNameIsNotProvided_ThenThrowResourceNotFoundException() {
        // Given
        when(streetRepository.findStreetByName(anyString())).thenReturn(null);

        // When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> this.streetService.getStreetByName("Street One"));

        // Then
        assertEquals("Street with name Street One not found", exception.getMessage());
    }

    @Test
    void saveStreet_WhenStreetRequestIsProvided_ThenReturnSavedStreet() {
        // Given
        Street street = DataProvider.streetOneMock;
        when(streetRepository.save(any(Street.class))).thenReturn(street);
        when(districtRepository.findById(anyInt())).thenReturn(Optional.ofNullable(DataProvider.districtAlmiranteMock));
        StreetRequest request = new StreetRequest(street.getName(), street.getDistrictId().getId());

        // When
        StreetResponse response = this.streetService.saveStreet(request);

        // Then
        assertNotNull(response);
        assertEquals(street.getId(), response.id());
        assertEquals(street.getName(), response.name());
    }

    @Test
    void saveStreet_WhenDistrictIdIsNotProvided_ThenThrowResourceNotFoundException() {
        // Given
        Street street = DataProvider.streetOneMock;
        StreetRequest request = new StreetRequest(street.getName(), street.getDistrictId().getId());

        // When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> this.streetService.saveStreet(request));

        //then
        assertEquals("District with id 1 not found", exception.getMessage());
    }

    @Test
    void updateStreet_WhenStreetIdAndStreetRequestIsProvided_ThenReturnUpdatedStreet() {
        // Given
        Street street = DataProvider.streetOneMock;
        Street updatedStreet = DataProvider.streetOneMock;
        updatedStreet.setName("Updated Street");
        StreetRequest request = new StreetRequest(updatedStreet.getName(), updatedStreet.getDistrictId().getId());
        when(streetRepository.findById(anyInt())).thenReturn(Optional.ofNullable(DataProvider.streetOneMock));
        when(streetRepository.save(any(Street.class))).thenReturn(updatedStreet);
        when(districtRepository.findById(anyInt())).thenReturn(Optional.of(DataProvider.districtAlmiranteMock));

        //When
        StreetResponse response = this.streetService.updateStreet(1, request);

        //Then
        assertNotNull(response);
        assertEquals(updatedStreet.getName(), response.name());
    }

    @Test
    void updateStreet_WhenStreetIdIsNotProvided_ThenThrowResourceNotFoundException() {
        // Given
        Street updatedStreet = DataProvider.streetOneMock;
        updatedStreet.setName("Updated Street");
        StreetRequest request = new StreetRequest(updatedStreet.getName(), updatedStreet.getDistrictId().getId());

        // When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> this.streetService.updateStreet(1, request));

        // Then
        assertEquals("Street with 1 not found", exception.getMessage());
    }

    @Test
    void deleteStreet_WhenStreetIdIsProvided_ThenDeleteStreet() {
        // Given
        assertDoesNotThrow( () ->streetService.deleteStreet(1));

        // Then
        verify(streetRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteStreet_WhenNotExists_ShouldThrowResourceNotFoundException() {
        // Given
        Integer id = 999;
        doThrow(new EmptyResultDataAccessException(1)).when(streetRepository).deleteById(id);

        // When
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> streetService.deleteStreet(id));

        // Then
        assertEquals("Street with ID " + id + " not found", exception.getMessage());
        verify(streetRepository, times(1)).deleteById(id);
    }
}
