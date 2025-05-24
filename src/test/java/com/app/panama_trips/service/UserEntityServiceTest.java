package com.app.panama_trips.service;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.UserNotFoundException;
import com.app.panama_trips.persistence.entity.RoleEnum;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.repository.RoleRepository;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.presentation.dto.UserResponse;
import com.app.panama_trips.service.implementation.UserEntityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserEntityServiceTest {

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserEntityService userEntityService;


    @Test
    void getAllUser_shouldReturnAllUsers() {
        // Given
        List<UserEntity> userList = DataProvider.userListMocks();
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<UserEntity> pageMock = new PageImpl<>(userList, pageable, userList.size());

        // When
        when(userEntityRepository.findAll(pageable)).thenReturn(pageMock);
        Page<UserResponse> result = userEntityService.getAllUser(0, 10, true);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(10, result.getSize());
        assertEquals(userList.size(), result.getTotalElements());
    }

    @Test
    void getAllUser_shouldReturnAllUsersWhenPaginationDisabled() {
        // Given
        List<UserEntity> userEntityList = DataProvider.userListMocks();
        Pageable pageable = Pageable.unpaged();
        PageImpl<UserEntity> pageMock = new PageImpl<>(userEntityList, pageable, userEntityList.size());

        // When
        when(userEntityRepository.findAll(pageable)).thenReturn(pageMock);
        Page<UserResponse> result = userEntityService.getAllUser(0, 10, false);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(userEntityList.size(), result.getTotalElements());}

    @Test
    void getAllUser_shouldReturnTheFirstPageWhenPageIsZero() {
        // Given
        List<UserEntity> userEntityList = DataProvider.userListMocks();
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<UserEntity> pageMock = new PageImpl<>(userEntityList, pageable, userEntityList.size());

        // When
        when(userEntityRepository.findAll(pageable)).thenReturn(pageMock);
        Page<UserResponse> result = userEntityService.getAllUser(0, 10, true);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(10, result.getSize());
    }

    @Test
    void getAllUser_shouldReturnOnlyOneElementWhenPageSizeIsOne() {
        // Given
        List<UserEntity> userEntityList = DataProvider.userListMocks();
        Pageable pageable = PageRequest.of(0, 1);
        PageImpl<UserEntity> pageMock = new PageImpl<>(userEntityList, pageable, userEntityList.size());

        // When
        when(userEntityRepository.findAll(pageable)).thenReturn(pageMock);
        Page<UserResponse> result = userEntityService.getAllUser(0, 1, true);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getSize());
    }

    @Test
    void getUserById_shouldReturnUserWhenUserExists() {
        // Given
        UserEntity user = DataProvider.userAdmin();

        // When
        when(userEntityRepository.findById(1L)).thenReturn(Optional.of(user));

        // Then
        UserResponse result = userEntityService.getUserById(1L);
        assertEquals(1L, result.id());
    }

    @Test
    void getUserById_shouldReturnExceptionWhenUserNotExits() {
        // When
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userEntityService.getUserById(1L));

        // Then
        assertEquals("User not found with id: 1", exception.getMessage());
        assertTrue(exception.getMessage().contains("1"));
    }

    @Test
    void saveUser_shouldSaveAndReturnUser() {
        // Given
        UserEntity user = DataProvider.userAdmin();

        // When
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(user);
        when(roleRepository.findByRoleEnum(any(RoleEnum.class))).thenReturn(DataProvider.userCustomer().getRole_id());


        // Then
        UserResponse result = userEntityService.saveUser(DataProvider.userRequestMock);
        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void updateUser_shouldUpdateUserWhenUserExists() {
        // Given
        UserEntity user = DataProvider.userAdmin();
        UserEntity updatedUser = DataProvider.userAdmin();
        updatedUser.setName("Updated");

        // When
        when(userEntityRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userEntityRepository.save(user)).thenReturn(updatedUser);

        // Then
        UserResponse result = userEntityService.updateUser(1L, DataProvider.userRequestMock);
        assertEquals("Updated", result.name());
        assertEquals(1L, result.id());
    }

    @Test
    void updateUser_shouldThrowExceptionWhenUserDoesNotExist() {
        // Given
        UserEntity updatedUser = DataProvider.userAdmin();
        updatedUser.setName("Updated");

        // When
        when(userEntityRepository.findById(1L)).thenReturn(Optional.empty());

        // Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userEntityService.updateUser(1L, DataProvider.userRequestMock));
        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    void deleteUser_shouldDeleteUserWhenUserExists() {
        // When
        when(userEntityRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userEntityRepository).deleteById(anyLong());

        // Then
        userEntityService.deleteUser(1L);
        verify(userEntityRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_shouldReturnFalseWhenUserDoesNotExist() {
        // When
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userEntityService.deleteUser(1L));

        // Then
        assertEquals("User not found with id: 1", exception.getMessage());
        assertTrue(exception.getMessage().contains("1"));
    }
}
