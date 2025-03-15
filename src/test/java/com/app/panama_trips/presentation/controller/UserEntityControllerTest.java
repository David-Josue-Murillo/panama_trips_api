package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.UserNotFoundException;
import com.app.panama_trips.presentation.dto.UserRequest;
import com.app.panama_trips.presentation.dto.UserResponse;
import com.app.panama_trips.service.implementation.UserEntityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserEntityController.class)
public class UserEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    UserEntityService userEntityService;

    // Método auxiliar para convertir objetos a JSON
    private String asJsonString(Object obj) throws Exception {
        return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllUsers_success() throws Exception {
        Page<UserResponse> page = new PageImpl<>(DataProvider.userResponseListMocks());
        when(userEntityService.getAllUser(anyInt(), anyInt(), anyBoolean())).thenReturn(page);

        mockMvc.perform(get("/api/user")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(DataProvider.userResponseListMocks().getFirst().id()))
                .andExpect(jsonPath("$.content[0].name").value(DataProvider.userResponseListMocks().getFirst().name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findAllUsers_successTrue() throws Exception {
        Page<UserResponse> page = new PageImpl<>(DataProvider.userResponseListMocks());
        when(userEntityService.getAllUser(anyInt(), anyInt(), anyBoolean())).thenReturn(page);

        mockMvc.perform(get("/api/user")
                        .param("page", "0")
                        .param("size", "10")
                        .param("enabledPagination", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(DataProvider.userResponseListMocks().getFirst().id()))
                .andExpect(jsonPath("$.content[0].name").value(DataProvider.userResponseListMocks().getFirst().name()));

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void saveUser_success() throws Exception {
        UserResponse userResponse = DataProvider.userResponseMock;
        when(userEntityService.saveUser(any(UserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(DataProvider.userRequestMock))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userResponse.id()))
                .andExpect(jsonPath("$.name").value(userResponse.name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void saveUser_failed() throws Exception {
        UserResponse userResponse = DataProvider.userResponseMock;
        when(userEntityService.saveUser(any(UserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(DataProvider.userRequestMock).replace("admin", ""))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findUserById_success() throws Exception {
        UserResponse userResponse = DataProvider.userResponseMock;
        when(userEntityService.getUserById(anyLong())).thenReturn(userResponse);

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userResponse.id()))
                .andExpect(jsonPath("$.name").value(userResponse.name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void findUserById_failed() throws Exception {
        when(userEntityService.getUserById(anyLong())).thenThrow(new UserNotFoundException("User not found with id: 999"));

        mockMvc.perform(get("/api/user/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value())) // 404
                .andExpect(jsonPath("$.message").value("User not found with id: 999"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateUser_success() throws Exception {
        UserResponse userResponse = DataProvider.userResponseMock;
        when(userEntityService.updateUser(anyLong(), any(UserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(put("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(DataProvider.userRequestMock))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userResponse.id()))
                .andExpect(jsonPath("$.name").value(userResponse.name()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateUser_failed() throws Exception {
        when(userEntityService.updateUser(anyLong(), any(UserRequest.class))).thenThrow(new UserNotFoundException("User not found with id: 100"));

        mockMvc.perform(put("/api/user/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(DataProvider.userRequestMock))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value())) // 404
                .andExpect(jsonPath("$.message").value("User not found with id: 100"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteUser_success() throws Exception {
        doNothing().when(userEntityService).deleteUser(anyLong());

        mockMvc.perform(delete("/api/user/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNoContent());
    }
}
