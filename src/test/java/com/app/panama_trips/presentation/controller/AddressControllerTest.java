package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.presentation.dto.AddressRequest;
import com.app.panama_trips.presentation.dto.AddressResponse;
import com.app.panama_trips.service.implementation.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.app.panama_trips.DataProvider.addressResponsesListMocks;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
public class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AddressService addressService;

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllAddresses_success() throws Exception {
        Page<AddressResponse> page = new PageImpl<>(addressResponsesListMocks);
        when(addressService.getAllAddresses(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/address")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].addressId").value(addressResponsesListMocks.getFirst().addressId()))
                .andExpect(jsonPath("$.content[0].street").value(addressResponsesListMocks.getFirst().street()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAddressById_success() throws Exception {
        when(addressService.getAddressById(anyInt())).thenReturn(addressResponsesListMocks.getFirst());

        mockMvc.perform(get("/api/address/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressId").value(addressResponsesListMocks.getFirst().addressId()))
                .andExpect(jsonPath("$.street").value(addressResponsesListMocks.getFirst().street()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAddressById_failed() throws Exception {
        when(addressService.getAddressById(anyInt())).thenThrow(new ResourceNotFoundException("Address not found"));

        mockMvc.perform(get("/api/address/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Address not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAddressByStreet_success() throws Exception {
        when(addressService.getAddressByStreet(any(String.class))).thenReturn(addressResponsesListMocks.getFirst());

        mockMvc.perform(get("/api/address/address")
                        .param("street", "Calle 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressId").value(addressResponsesListMocks.getFirst().addressId()))
                .andExpect(jsonPath("$.street").value(addressResponsesListMocks.getFirst().street()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAddressByStreet_failed() throws Exception {
        when(addressService.getAddressByStreet(any(String.class))).thenThrow(new ResourceNotFoundException("Address not found"));

        mockMvc.perform(get("/api/address/address")
                        .param("street", "Calle 999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Address not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAddressesByDistrictId_success() throws Exception {
        when(addressService.getAddressesByDistrictId(anyInt())).thenReturn(addressResponsesListMocks);

        mockMvc.perform(get("/api/address/district/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].addressId").value(addressResponsesListMocks.getFirst().addressId()))
                .andExpect(jsonPath("$[0].street").value(addressResponsesListMocks.getFirst().street()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAddressesByDistrict_failed() throws Exception {
        when(addressService.getAddressesByDistrictId(anyInt())).thenThrow(new ResourceNotFoundException("Address not found"));

        mockMvc.perform(get("/api/address/district/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Address not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAddressesByPostalCode() throws Exception {
        when(addressService.getAddressesByPostalCode(any(String.class))).thenReturn(addressResponsesListMocks);

        mockMvc.perform(get("/api/address/postal-code/08001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].addressId").value(addressResponsesListMocks.getFirst().addressId()))
                .andExpect(jsonPath("$[0].street").value(addressResponsesListMocks.getFirst().street()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAddressesByPostalCode_failed() throws Exception {
        when(addressService.getAddressesByPostalCode(any(String.class))).thenThrow(new ResourceNotFoundException("Address not found"));

        mockMvc.perform(get("/api/address/postal-code/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Address not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAddressByStreetContainingIgnoreCase_success() throws Exception {
        when(addressService.getAddressesByStreetContainingIgnoreCase(any(String.class))).thenReturn(addressResponsesListMocks);

        mockMvc.perform(get("/api/address/search")
                        .param("street", "Calle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].addressId").value(addressResponsesListMocks.getFirst().addressId()))
                .andExpect(jsonPath("$[0].street").value(addressResponsesListMocks.getFirst().street()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAddressByStreetContainingIgnoreCase_failed() throws Exception {
        when(addressService.getAddressesByStreetContainingIgnoreCase(any(String.class))).thenThrow(new ResourceNotFoundException("Address not found"));

        mockMvc.perform(get("/api/address/search")
                        .param("street", "Calle 999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Address not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void saveAddress_success() throws Exception {
        AddressRequest address = DataProvider.addressRequestMock;
        AddressResponse response = DataProvider.addressResponseMock;
        when(addressService.saveAddress(any(AddressRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(address))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.addressId").value(response.addressId()))
                .andExpect(jsonPath("$.street").value(response.street()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateAddress_success() throws Exception {
        AddressRequest address = DataProvider.addressRequestMock;
        when(addressService.updateAddress(anyInt(), any(AddressRequest.class))).thenReturn(DataProvider.addressResponseMock);

        mockMvc.perform(put("/api/address/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(address))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressId").value(DataProvider.addressResponseMock.addressId()))
                .andExpect(jsonPath("$.street").value(DataProvider.addressResponseMock.street()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteAddress_success() throws Exception {
        doNothing().when(addressService).deleteAddress(anyInt());

        mockMvc.perform(delete("/api/address/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }
}
