package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.presentation.dto.ProvinceRequest;
import com.app.panama_trips.service.implementation.ProvinceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.app.panama_trips.DataProvider.provinceResponseListMocks;
import static com.app.panama_trips.DataProvider.provinceResponseMock;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProvinceController.class)
public class ProvinceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProvinceService provinceService;

    private String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllProvince_success() throws Exception{
        when(provinceService.getAllProvinces()).thenReturn(provinceResponseListMocks);

        mockMvc.perform(get("/api/provinces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(DataProvider.provinceListsMock.getFirst().getId()))
                .andExpect(jsonPath("$[0].name").value(DataProvider.provinceListsMock.getFirst().getName()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void saveProvince_success() throws Exception{
        when(provinceService.saveProvince(any(ProvinceRequest.class))).thenReturn(provinceResponseMock);

        mockMvc.perform(post("/api/provinces")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(DataProvider.provinceBocasMock))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(DataProvider.provinceBocasMock.getId()))
                .andExpect(jsonPath("$.name").value(DataProvider.provinceBocasMock.getName()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findProvinceById_success() throws Exception {
        when(provinceService.getProvinceById(anyInt())).thenReturn(provinceResponseMock);

        mockMvc.perform(get("/api/provinces/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(DataProvider.provinceBocasMock.getId()))
                .andExpect(jsonPath("$.name").value(DataProvider.provinceBocasMock.getName()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findProvinceById_failed() throws Exception {
        when(provinceService.getProvinceById(anyInt())).thenThrow(new ResourceNotFoundException("Province not found"));

        mockMvc.perform(get("/api/provinces/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.message").value("Province not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findProvinceByName_success() throws Exception {
        when(provinceService.getProvinceByName(anyString())).thenReturn(provinceResponseMock);

        mockMvc.perform(get("/api/provinces/search")
                .param("name", "Bocas del Toro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(DataProvider.provinceBocasMock.getId()))
                .andExpect(jsonPath("$.name").value(DataProvider.provinceBocasMock.getName()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findProvinceByName_failed() throws Exception {
        when(provinceService.getProvinceByName(anyString())).thenThrow(new ResourceNotFoundException("Province not found"));

        mockMvc.perform(get("/api/provinces/search")
                        .param("name", "EEUU"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.message").value("Province not found"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateProvince_success() throws Exception {
        when(provinceService.updateProvince(anyInt(), any(ProvinceRequest.class))).thenReturn(provinceResponseMock);

        mockMvc.perform(put("/api/provinces/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(DataProvider.provinceBocasMock))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(DataProvider.provinceBocasMock.getId()))
                .andExpect(jsonPath("$.name").value(DataProvider.provinceBocasMock.getName()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateProvince_failed() throws Exception {
        when(provinceService.updateProvince(anyInt(), any(ProvinceRequest.class))).thenReturn(null);

        mockMvc.perform(put("/api/provinces/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(null))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is5xxServerError());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteProvince_success() throws Exception {
        mockMvc.perform(delete("/api/provinces/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }
}
