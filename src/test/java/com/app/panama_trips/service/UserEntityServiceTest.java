package com.app.panama_trips.service;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.exception.UserNotFoundException;
import com.app.panama_trips.exception.ValidationException;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserEntityServiceTest {

    @Mock
    private UserEntityRepository userEntityRepository;

    @InjectMocks
    private UserEntityService userEntityService;

    @Test
    void validateUser_shouldThrowExceptionWhenNameIsNull() {
        // Given
        UserEntity user = DataProvider.userAdmin();
        user.setName("");

        // When
        ValidationException exception = assertThrows(ValidationException.class, () -> userEntityService.saveUser(user));
        List<String> errors = exception.getErrors();

        assertEquals("User validation failed", exception.getMessage());
        assertTrue(exception.getErrors().contains("Username is required"));
    }

    @Test
    void validateUser_shouldThrowExceptionWhenPasswordIsNull() {
        // Given
        UserEntity user = DataProvider.userAdmin();
        user.setPasswordHash("");

        // When
        ValidationException exception = assertThrows(ValidationException.class, () -> userEntityService.saveUser(user));

        // Then
        assertEquals("User validation failed", exception.getMessage());
        assertTrue(exception.getErrors().contains("Password is required"));
    }

    @Test
    void validateUser_shouldThrowExceptionWhenPasswordIsLessThanFourCharacters() {
        // Given
        UserEntity user = DataProvider.userAdmin();
        user.setPasswordHash("123");

        // When
        ValidationException exception = assertThrows(ValidationException.class, () -> userEntityService.saveUser(user));

        // Then
        assertEquals("User validation failed", exception.getMessage());
        assertTrue(exception.getErrors().contains("Password must be at least 4 characters long"));
    }

    @Test
    void validateUser_shouldPassAllValidations() {
        // Given
        UserEntity user = DataProvider.userAdmin();

        // When
        assertDoesNotThrow(() -> userEntityService.saveUser(user));

        // Then
        verify(userEntityRepository, times(1)).save(user);
    }

    @Test
    void getAllUser_shouldReturnAllUsers() {
        // Given
        List<UserEntity> userList = DataProvider.userListMocks();
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl pageMock = new PageImpl<>(userList, pageable, userList.size());

        // When
        when(userEntityRepository.findAll(pageable)).thenReturn(pageMock);
        Page<UserEntity> result = userEntityService.getAllUser(0, 10, true);

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
        PageImpl pageMock = new PageImpl<>(userEntityList, pageable, userEntityList.size());

        // When
        when(userEntityRepository.findAll(pageable)).thenReturn(pageMock);
        Page<UserEntity> result = userEntityService.getAllUser(0, 10, false);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(userEntityList.size(), result.getTotalElements());}

    @Test
    void getAllUser_shouldReturnTheFirstPageWhenPageIsZero() {
        // Given
        List<UserEntity> userEntityList = DataProvider.userListMocks();
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl pageMock = new PageImpl<>(userEntityList, pageable, userEntityList.size());

        // When
        when(userEntityRepository.findAll(pageable)).thenReturn(pageMock);
        Page<UserEntity> result = userEntityService.getAllUser(0, 10, true);

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
        PageImpl pageMock = new PageImpl<>(userEntityList, pageable, userEntityList.size());

        // When
        when(userEntityRepository.findAll(pageable)).thenReturn(pageMock);
        Page<UserEntity> result = userEntityService.getAllUser(0, 1, true);

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
        UserEntity result = userEntityService.getUserById(1L);
        assertEquals(1L, result.getId());
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
        when(userEntityRepository.save(user)).thenReturn(user);

        // Then
        UserEntity result = userEntityService.saveUser(user);
        assertNotNull(result);
        assertEquals(1L, result.getId());
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
        UserEntity result = userEntityService.updateUser(1L, updatedUser);
        assertEquals("Updated", result.getName());
        assertEquals(1L, result.getId());
    }

    @Test
    void updateUser_shouldThrowExceptionWhenUserDoesNotExist() {
        // Given
        UserEntity user = DataProvider.userAdmin();
        UserEntity updatedUser = DataProvider.userAdmin();
        updatedUser.setName("Updated");

        // When
        when(userEntityRepository.findById(1L)).thenReturn(Optional.empty());

        // Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userEntityService.updateUser(1L, updatedUser));
        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    void deleteUser_shouldDeleteUserWhenUserExists() {
        // Given
        UserEntity user = DataProvider.userAdmin();

        // When
        when(userEntityRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userEntityRepository).delete(user);

        // Then
        userEntityService.deleteUser(1L);
        verify(userEntityRepository, times(1)).delete(user);
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
