package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.persistence.entity.District;
import com.app.panama_trips.presentation.dto.DistrictRequest;
import com.app.panama_trips.service.implementation.DistrictService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DistrictController.class)
public class DistrictControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DistrictService districtService;

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllDistricts_success() throws Exception {
        when(districtService.getAllDistricts()).thenReturn(DataProvider.districtListsMock);

        mockMvc.perform(get("/api/districts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(DataProvider.districtListsMock.getFirst().getId()))
                .andExpect(jsonPath("$[0].name").value(DataProvider.districtListsMock.getFirst().getName()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void saveDistrict_success() throws Exception{
        DistrictRequest request = DataProvider.districtRequestMock;
        District response = DataProvider.districtBocasMock;
        when(districtService.saveDistrict(request)).thenReturn(response);

        mockMvc.perform(post("/api/districts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.name").value(response.getName()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findDistrictById_success() throws Exception {
        when(districtService.getDistrictById(anyInt())).thenReturn(DataProvider.districtAlmiranteMock);

        mockMvc.perform(get("/api/districts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(DataProvider.districtAlmiranteMock.getId()))
                .andExpect(jsonPath("$.name").value(DataProvider.districtAlmiranteMock.getName()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findDistrictsByProvinceId_success() throws Exception {
        when(districtService.getDistrictsByProvinceId(anyInt())).thenReturn(DataProvider.districtListsMock);

        mockMvc.perform(get("/api/districts/province/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateDistrict_success() throws Exception {
        DistrictRequest request = DataProvider.districtRequestMock;
        when(districtService.updateDistrict(anyInt(), any(DistrictRequest.class))).thenReturn(DataProvider.districtAlmiranteMock);

        mockMvc.perform(put("/api/districts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(DataProvider.districtAlmiranteMock.getId()))
                .andExpect(jsonPath("$.name").value(DataProvider.districtAlmiranteMock.getName()));

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteDistrict_success() throws Exception {
        mockMvc.perform(delete("/api/districts/1")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }
}
