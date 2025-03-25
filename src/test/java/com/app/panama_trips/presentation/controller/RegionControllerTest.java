package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.presentation.dto.RegionRequest;
import com.app.panama_trips.presentation.dto.RegionResponse;
import com.app.panama_trips.service.implementation.RegionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.app.panama_trips.DataProvider.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegionController.class)
public class RegionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegionService regionService;

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllTourPlan_success() throws Exception {
        Page<RegionResponse> page = new PageImpl<>(regionResponseListMocks, PageRequest.of(0, 10), regionResponseListMocks.size());
        when(regionService.getAllRegions(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/region")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(regionResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].name").value(regionResponseListMocks.getFirst().name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findRegionById_success() throws Exception {
        when(regionService.getRegionById(anyInt())).thenReturn(regionResponseMock);

        mockMvc.perform(get("/api/region/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(regionResponseMock.id()))
                .andExpect(jsonPath("$.name").value(regionResponseMock.name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findRegionById_failed() throws Exception {
        when(regionService.getRegionById(anyInt())).thenThrow(new ResourceNotFoundException("Region not found"));

        mockMvc.perform(get("/api/region/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Region not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void saveRegion_success() throws Exception {
        when(regionService.saveRegion(any(RegionRequest.class))).thenReturn(regionResponseMock);

        mockMvc.perform(post("/api/region")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(regionRequestMock))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(regionResponseMock.id()))
                .andExpect(jsonPath("$.name").value(regionResponseMock.name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateRegion_success() throws Exception {
        when(regionService.updateRegion(anyInt(), any(RegionRequest.class))).thenReturn(regionResponseMock);

        mockMvc.perform(put("/api/region/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(regionRequestMock))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(regionResponseMock.id()))
                .andExpect(jsonPath("$.name").value(regionResponseMock.name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateRegion_failed() throws Exception {
        when(regionService.updateRegion(anyInt(), any(RegionRequest.class))).thenThrow(new ResourceNotFoundException("Region not found"));

        mockMvc.perform(put("/api/region/1")
                        .contentType("application/json")
                        .content(asJsonString(regionRequestMock))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Region not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteRegion_success() throws Exception {
        mockMvc.perform(delete("/api/region/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllRegionByName() throws Exception {
        when(regionService.getRegionByName(anyString())).thenReturn(regionResponseMock);

        mockMvc.perform(get("/api/region/name")
                        .param("q", "region"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(regionResponseMock.id()))
                .andExpect(jsonPath("$.name").value(regionResponseMock.name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void searchRegionByName_success() throws Exception {
        Page<RegionResponse> page = new PageImpl<>(regionResponseListMocks, PageRequest.of(0, 10), regionResponseListMocks.size());
        when(regionService.getRegionsByName(anyString(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/region/search")
                        .param("q", "region")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(regionResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].name").value(regionResponseListMocks.getFirst().name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void searchRegionByName_failed() throws Exception {
        when(regionService.getRegionsByName(anyString(), any(Pageable.class))).thenThrow(new ResourceNotFoundException("Region not found"));

        mockMvc.perform(get("/api/region/search")
                        .param("q", "nonexistent")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "true"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Region not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findRegionByProvinceId_success() throws Exception {
        Page<RegionResponse> page = new PageImpl<>(regionResponseListMocks, PageRequest.of(0, 10), regionResponseListMocks.size());
        when(regionService.getRegionByProvinceId(anyInt(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/region/province/1")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(regionResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].name").value(regionResponseListMocks.getFirst().name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findRegionByProvinceId_failed() throws Exception {
        when(regionService.getRegionByProvinceId(anyInt(), any(Pageable.class))).thenThrow(new ResourceNotFoundException("Region not found"));

        mockMvc.perform(get("/api/region/province/999")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "true"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Region not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findRegionByComarcaId_success() throws Exception {
        Page<RegionResponse> page = new PageImpl<>(regionResponseListMocks, PageRequest.of(0, 10), regionResponseListMocks.size());
        when(regionService.getRegionByComarcaId(anyInt(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/region/comarca/1")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(regionResponseListMocks.getFirst().id()))
                .andExpect(jsonPath("$.content[0].name").value(regionResponseListMocks.getFirst().name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findRegionByComarcaId_failed() throws Exception {
        when(regionService.getRegionByComarcaId(anyInt(), any(Pageable.class))).thenThrow(new ResourceNotFoundException("Region not found"));

        mockMvc.perform(get("/api/region/comarca/999")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "true"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Region not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void countRegions_success() throws Exception {
        when(regionService.countRegions()).thenReturn(10L);

        mockMvc.perform(get("/api/region/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(10L));
    }
}
