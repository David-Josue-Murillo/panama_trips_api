package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.ResourceNotFoundException;
import com.app.panama_trips.presentation.dto.ComarcaRequest;
import com.app.panama_trips.service.interfaces.IComarcaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComarcaController.class)
class ComarcaControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private IComarcaService comarcaService;

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("Should get all comarcas")
  void findAllComarcas_Success() throws Exception {
    when(comarcaService.getAllComarcas()).thenReturn(DataProvider.comarcaResponseListMocks);

    mockMvc.perform(get("/api/comarcas"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].name").value("Guna Yala"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("Should get comarca by ID")
  void findComarcaById_Success() throws Exception {
    when(comarcaService.getComarcaById(1)).thenReturn(DataProvider.comarcaResponseMock);

    mockMvc.perform(get("/api/comarcas/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Guna Yala"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  @DisplayName("Should get 404 when comarca ID not found")
  void findComarcaById_NotFound() throws Exception {
    when(comarcaService.getComarcaById(anyInt())).thenThrow(new ResourceNotFoundException("Comarca not found"));

    mockMvc.perform(get("/api/comarcas/999"))
        .andExpect(status().isNotFound());
  }

 
}
