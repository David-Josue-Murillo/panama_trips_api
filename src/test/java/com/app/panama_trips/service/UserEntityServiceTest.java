package com.app.panama_trips.service;

import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.service.implementation.UserEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserEntityServiceTest {

    @Mock
    private UserEntityRepository userEntityRepository;

    @InjectMocks
    private UserEntityService userEntityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findUserById_shouldReturnUserWhenUserExists() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        when(userEntityRepository.findById(1L)).thenReturn(Optional.of(user));

        UserEntity result = userEntityService.getUserById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void findUserById_shouldReturnNullWhenUserDoesNotExist() {
        when(userEntityRepository.findById(1L)).thenReturn(Optional.empty());

        UserEntity result = userEntityService.getUserById(1L);
        assertNull(result);
    }

    @Test
    void saveUser_shouldSaveAndReturnUser() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        when(userEntityRepository.save(user)).thenReturn(user);

        UserEntity result = userEntityService.saveUser(user);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void deleteUser_shouldDeleteUserWhenUserExists() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        when(userEntityRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userEntityRepository).delete(user);

        userEntityService.deleteUser(1L);
        verify(userEntityRepository, times(1)).delete(user);
    }

    @Test
    void deleteUser_shouldReturnFalseWhenUserDoesNotExist() {
        when(userEntityRepository.findById(1L)).thenReturn(Optional.empty());

        userEntityService.deleteUser(1L);
        verify(userEntityRepository, never()).delete(any(UserEntity.class));
    }
}
