package com.app.panama_trips.service;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.persistence.entity.UserEntity;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.service.implementation.UserDetailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserDetailServiceImplTest {

    @Mock
    private UserEntityRepository userEntityRepository;

    @InjectMocks
    private UserDetailServiceImpl userDetailServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetailsWhenUserExists() {
        // Given
        UserEntity user = DataProvider.userAdmin();

        // When
        when(userEntityRepository.findUserEntitiesByName("admin")).thenReturn(Optional.of(user));
        UserDetails userDetails = userDetailServiceImpl.loadUserByUsername("admin");

        // Then
        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertEquals(user.getPasswordHash(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))); // Ajusta si es necesario
    }

    @Test
    void loadUserByUsername_shouldThrowExceptionWhenUserDoesNotExist() {
        // When
        when(userEntityRepository.findUserEntitiesByName("nonExistentUser")).thenReturn(Optional.empty());

        // Then
        assertThrows(UsernameNotFoundException.class, () -> userDetailServiceImpl.loadUserByUsername("nonExistentUser"));
    }
}
