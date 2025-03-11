package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.presentation.dto.StreetRequest;
import com.app.panama_trips.presentation.dto.StreetResponse;
import com.app.panama_trips.service.implementation.StreetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StreetController.class)
public class StreetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StreetService streetService;

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void finAllStreet_success() throws Exception {
        Page<StreetResponse> page = new PageImpl<>(DataProvider.streetResponseListsMock);
        when(streetService.getAllStreet(0, 10, false)).thenReturn(page);

        mockMvc.perform(get("/api/streets")
                .param("page", "0")
                .param("size", "10")
                .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(DataProvider.streetResponseListsMock.getFirst().id()))
                .andExpect(jsonPath("$.content[0].name").value(DataProvider.streetResponseListsMock.getFirst().name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createStreet_success() throws Exception {
        StreetRequest request = DataProvider.streetRequestOneMock;
        StreetResponse response = DataProvider.streetResponseOneMock;
        when(streetService.saveStreet(any(StreetRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/streets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()));
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createStreet_invalidRequest() throws Exception {
        StreetRequest invalidRequest = new StreetRequest("", 1);

        mockMvc.perform(post("/api/streets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(invalidRequest))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Incluye el token CSRF
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findStreetById_success() throws Exception {
        StreetResponse response = DataProvider.streetResponseOneMock;
        when(streetService.getStreetById(1)).thenReturn(response);

        mockMvc.perform(get("/api/streets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findStreetByDistrictId_success() throws Exception{
        List<StreetResponse>  streets = DataProvider.streetResponseListsMock;
        when(streetService.getAllStreetByDistrictId(anyInt())).thenReturn(streets);

        mockMvc.perform(get("/api/streets/district/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(streets.getFirst().id()))
                .andExpect(jsonPath("$[0].name").value(streets.getFirst().name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findStreetByName_success() throws Exception {
        StreetResponse response = DataProvider.streetResponseOneMock;
        when(streetService.getStreetByName(anyString())).thenReturn(response);

        mockMvc.perform(get("/api/streets/search")
                .param("name", "Calle 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateStreet_success() throws Exception {
        StreetRequest request = DataProvider.streetRequestOneMock;
        StreetResponse response = DataProvider.streetResponseOneMock;
        when(streetService.updateStreet(anyInt(), any(StreetRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/streets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.name").value(response.name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteStreet_Success() throws Exception {
        doNothing().when(streetService).deleteStreet(1);

        mockMvc.perform(delete("/api/streets/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }
}
