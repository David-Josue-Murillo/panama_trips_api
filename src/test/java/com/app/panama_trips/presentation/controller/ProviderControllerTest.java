package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.presentation.dto.ProviderRequest;
import com.app.panama_trips.presentation.dto.ProviderResponse;
import com.app.panama_trips.service.implementation.ProviderService;
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

import static com.app.panama_trips.DataProvider.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProviderController.class)
public class ProviderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProviderService providerService;

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllProviders_success() throws Exception{
        Page<ProviderResponse> page = new PageImpl<>(providerResponseListMocks);
        when(providerService.getAllProviders(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/providers")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(providerResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].name").value(providerResponseListMocks.getFirst().name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findProviderById_success() throws Exception {
        when(providerService.getProviderById(anyInt())).thenReturn(providerResponseMock);

        mockMvc.perform(get("/api/providers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(providerResponseMock.id()))
                .andExpect(jsonPath("$.name").value(providerResponseMock.name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findProviderById_failed() throws Exception {
        when(providerService.getProviderById(anyInt())).thenThrow(new ResourceNotFoundException("Provider with 999 not found"));

        mockMvc.perform(get("/api/providers/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Provider with 999 not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findProviderByName_success() throws Exception {
        when(providerService.getProviderByName(anyString())).thenReturn(providerResponseMock);

        mockMvc.perform(get("/api/providers/name?q=provider")
                        .param("q", "provider"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(providerResponseMock.id()))
                .andExpect(jsonPath("$.name").value(providerResponseMock.name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findProviderByName_failed() throws Exception {
        when(providerService.getProviderByName(anyString())).thenThrow(new ResourceNotFoundException("Provider with provider name not found"));

        mockMvc.perform(get("/api/providers/name?q=provider")
                        .param("q", "provider"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Provider with provider name not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void countProviders_success() throws Exception {
        when(providerService.countProviders()).thenReturn(100L);

        mockMvc.perform(get("/api/providers/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(100));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void saveProvider_success() throws Exception {
        when(providerService.saveProvider(any(ProviderRequest.class))).thenReturn(providerResponseMock);

        mockMvc.perform(post("/api/providers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(providerRequestMock))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(providerResponseMock.id()))
                .andExpect(jsonPath("$.name").value(providerResponseMock.name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateProvider_success() throws Exception {
        when(providerService.updateProvider(anyInt(), any(ProviderRequest.class))).thenReturn(providerResponseMock);

        mockMvc.perform(put("/api/providers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(providerRequestMock))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(providerResponseMock.id()))
                .andExpect(jsonPath("$.name").value(providerResponseMock.name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateProvider_failed() throws Exception {
        when(providerService.updateProvider(anyInt(), any(ProviderRequest.class))).thenThrow(new ResourceNotFoundException("Provider with provider name not found"));

        mockMvc.perform(put("/api/providers/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(providerRequestMock))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Provider with provider name not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteProvider_success() throws Exception {
        doNothing().when(providerService).deleteProvider(anyInt());

        mockMvc.perform(delete("/api/providers/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findProviderByRuc_success() throws Exception {
        when(providerService.getProviderByRuc(anyString())).thenReturn(providerResponseMock);

        mockMvc.perform(get("/api/providers/ruc?q=12345678901")
                        .param("q", "12345678901"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(providerResponseMock.id()))
                .andExpect(jsonPath("$.name").value(providerResponseMock.name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findProviderByRuc_failed() throws Exception {
        when(providerService.getProviderByRuc(anyString())).thenThrow(new ResourceNotFoundException("Provider with ruc not found"));

        mockMvc.perform(get("/api/providers/ruc?q=12345678901")
                        .param("q", "12345678901"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Provider with ruc not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findProviderByEmail_success() throws Exception {
        when(providerService.getProviderByEmail(anyString())).thenReturn(providerResponseMock);

        mockMvc.perform(get("/api/providers/email?q=user@example.com")
                        .param("q", "user@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(providerResponseMock.id()))
                .andExpect(jsonPath("$.name").value(providerResponseMock.name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findProviderByEmail_failed() throws Exception {
        when(providerService.getProviderByEmail(anyString())).thenThrow(new ResourceNotFoundException("Provider with email not found"));

        mockMvc.perform(get("/api/providers/email?q=user@")
                        .param("q", "user@"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Provider with email not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findProviderByPhone_success() throws Exception {
        when(providerService.getProviderByPhone(anyString())).thenReturn(providerResponseMock);

        mockMvc.perform(get("/api/providers/phone?q=61016101")
                        .param("q", "61016101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(providerResponseMock.id()))
                .andExpect(jsonPath("$.name").value(providerResponseMock.name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findProviderByPhone_failed() throws Exception {
        when(providerService.getProviderByPhone(anyString())).thenThrow(new ResourceNotFoundException("Provider with phone number not found"));

        mockMvc.perform(get("/api/providers/phone?q=01010101")
                        .param("q", "01010101"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Provider with phone number not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findProvidersByProvince_success() throws Exception {
        when(providerService.getProvidersByProvinceId(anyInt())).thenReturn(providerResponseListMocks);

        mockMvc.perform(get("/api/providers/province/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(providerResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$[0].name").value(providerResponseListMocks.getFirst().name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findProvidersByDistrict_success() throws Exception {
        when(providerService.getProvidersByDistrictId(anyInt())).thenReturn(providerResponseListMocks);

        mockMvc.perform(get("/api/providers/district/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(providerResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$[0].name").value(providerResponseListMocks.getFirst().name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findProvidersByAddress() throws Exception {
        when(providerService.getProvidersByAddressId(anyInt())).thenReturn(providerResponseListMocks);

        mockMvc.perform(get("/api/providers/address/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(providerResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$[0].name").value(providerResponseListMocks.getFirst().name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void searchProviders() throws Exception {
        when(providerService.getProvidersByNameFragment(anyString())).thenReturn(providerResponseListMocks);

        mockMvc.perform(get("/api/providers/search")
                        .param("q", "provider"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(providerResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$[0].name").value(providerResponseListMocks.getFirst().name()));
    }
}
