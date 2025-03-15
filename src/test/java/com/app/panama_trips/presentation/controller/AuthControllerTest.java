package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.ValidationException;
import com.app.panama_trips.presentation.dto.AuthCreateUserRequest;
import com.app.panama_trips.presentation.dto.AuthLoginRequest;
import com.app.panama_trips.presentation.dto.AuthResponse;
import com.app.panama_trips.service.implementation.UserAuthService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static com.app.panama_trips.DataProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    UserAuthService userAuthService;

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void register_success() throws Exception {
        when(userAuthService.create(any(AuthCreateUserRequest.class))).thenReturn(userAuthResponseMock());

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userAuthCreateUserRequestMock()))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.jwt").value("jwt"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void register_badRequest() throws Exception {
        when(userAuthService.create(any(AuthCreateUserRequest.class))).thenThrow(new BadCredentialsException("Email already exists"));

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userAuthCreateUserRequestMock()))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email already exists"))
                .andExpect(jsonPath("$.errorCode").value("BAD_CREDENTIALS"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void login_success() throws Exception {
        when(userAuthService.login(any(AuthLoginRequest.class))).thenReturn(userAuthResponseMock());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userAuthLoginRequestMock()))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.jwt").value("jwt"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void login_failed() throws Exception {
        when(userAuthService.login(any(AuthLoginRequest.class))).thenThrow(new BadCredentialsException("Invalid password"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userAuthLoginRequestMock()))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid password"))
                .andExpect(jsonPath("$.errorCode").value("BAD_CREDENTIALS"));
    }
}
