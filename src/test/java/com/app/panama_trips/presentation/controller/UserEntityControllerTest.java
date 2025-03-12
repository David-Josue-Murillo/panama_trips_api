package com.app.panama_trips.presentation.controller;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.UserNotFoundException;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.presentation.dto.UserDeleteResponse;
import com.app.panama_trips.presentation.dto.UserRequest;
import com.app.panama_trips.service.implementation.UserEntityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserEntityControllerTest {

    @Mock
    UserEntityService userEntityService;

    @InjectMocks
    UserEntityController userEntityController;

    @Test
    void findAllUsers_shouldReturnAllUsersWhenTheEnabledPaginationIsFalse() {
        // Given
        Page<UserEntity> userEntityPage = new PageImpl<>(DataProvider.userListMocks());
        when(userEntityService.getAllUser(0, 10, false)).thenReturn(userEntityPage);

        // When
        ResponseEntity<Page<UserEntity>> response = userEntityController.findAllUsers(0, 10, false);

        // Then
        assertNotNull(response);
        assertEquals(userEntityPage, response.getBody());
    }

    @Test
    void findAllUsers_shouldReturnAllUsersWhenTheEnablePaginationIsTrue() {
        // Given
        Page<UserEntity> userEntityPage = new PageImpl<>(DataProvider.userListMocks());
        when(userEntityService.getAllUser(0, 10, true)).thenReturn(userEntityPage);

        // When
        ResponseEntity<Page<UserEntity>> response = userEntityController.findAllUsers(0, 10, true);

        // Then
        assertNotNull(response);
        assertEquals(userEntityPage, response.getBody());
    }

    @Test
    void saveUser_shouldReturnTheUserEntitySaved() {
        // Given
        UserEntity userEntity = DataProvider.userAdmin();
        when(userEntityService.saveUser(any(UserRequest.class))).thenReturn(userEntity);

        // When
        ResponseEntity<UserEntity> response  = userEntityController.saveUser(DataProvider.userRequestMock);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userEntity, response.getBody());
    }

    @Test
    void saveUser_shouldReturnOneExceptionIfUserExist() {
        // Given
        UserEntity userEntity = DataProvider.userAdmin();
        when(userEntityService.saveUser(any(UserRequest.class))).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"User already exist"));

        // When
        ResponseStatusException exception  = assertThrows(ResponseStatusException.class, () -> userEntityController.saveUser(DataProvider.userRequestMock));

        // Then
        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    void findUSerById_shouldReturnUserById() {
        // Given
        UserEntity userEntity = DataProvider.userAdmin();
        when(userEntityService.getUserById(anyLong())).thenReturn(userEntity);

        // When
        ResponseEntity<UserEntity> response = userEntityController.findUSerById(1L);

        // Then
        assertNotNull(response);
        assertEquals(userEntity, response.getBody());
    }

    @Test
    void findUserById_shouldThrowExceptionWhenUserNotExits() {
        // When
        when(userEntityService.getUserById(anyLong())).thenThrow(new UserNotFoundException("User not found"));
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userEntityController.findUSerById(1L));

        // Then
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void updateUser_shouldReturnAnUserUpdate() {
        // Given
        UserEntity userEntity = DataProvider.userAdmin();
        userEntity.setName("new name");

        // When
        when(userEntityService.updateUser(1L, DataProvider.userAuthCreateUserRequestMock())).thenReturn(userEntity);
        ResponseEntity<UserEntity> response = userEntityController.updatedUser(1L, DataProvider.userAuthCreateUserRequestMock());

        // Then
        assertNotNull(response);
        assertEquals(DataProvider.userAdmin().getId(), response.getBody().getId());
        assertNotEquals(DataProvider.userAdmin().getName(), response.getBody().getName());
    }

    @Test
    void updateUser_shouldThrowAnExceptionWhenTheIDsWereDiferente() {
        // Given
        UserEntity userEntity = DataProvider.userAdmin();

        // When
        when(userEntityService.updateUser(100L, DataProvider.userAuthCreateUserRequestMock())).thenThrow(new UserNotFoundException("User not found with id: 100"));
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userEntityController.updatedUser(100L, DataProvider.userAuthCreateUserRequestMock()));

        // Then
        assertEquals("User not found with id: 100", exception.getMessage());
    }

    @Test
    void deleteUser_shouldDeleteTheUser() {
        // When
        ResponseEntity<UserDeleteResponse> response = userEntityController.deleteUser(1L);

        // Then
        assertNotNull(response);
        assertEquals("User deleted successfully", Objects.requireNonNull(response.getBody()).message());
    }
}
