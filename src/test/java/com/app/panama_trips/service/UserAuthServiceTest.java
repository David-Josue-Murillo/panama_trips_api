package com.app.panama_trips.service;

import com.app.panama_trips.DataProvider;
import com.app.panama_trips.persistence.repository.RoleRepository;
import com.app.panama_trips.persistence.repository.UserEntityRepository;
import com.app.panama_trips.presentation.dto.AuthLoginRequest;
import com.app.panama_trips.presentation.dto.AuthResponse;
import com.app.panama_trips.service.implementation.UserAuthService;
import com.app.panama_trips.service.implementation.UserDetailServiceImpl;
import com.app.panama_trips.utility.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDetailServiceImpl userDetailService;

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserAuthService userAuthService;

    private UserDetails mockUserDetails;

    @Test
    void login_shouldReturnValidAuthResponse() {
        mockUserDetails = new User(
                "admin",
                "Admin123!", // Contraseña encriptada
                List.of(() -> "ROLE_ADMIN")
        );

        // Given
        when(userDetailService.loadUserByUsername("admin")).thenReturn(mockUserDetails);
        when(jwtUtil.generateToken(any(Authentication.class))).thenReturn("mocked_jwt_token");
        when(passwordEncoder.matches("Admin123!", mockUserDetails.getPassword())).thenReturn(true);
        AuthLoginRequest authLoginRequest = DataProvider.userAuthLoginRequestMock();

        // When
        AuthResponse response = userAuthService.login(authLoginRequest);

        // Then
        assertNotNull(response);
        assertEquals("admin", response.username());
        assertEquals("Logged in successfully", response.message());
        assertEquals("mocked_jwt_token", response.jwt());
    }

    @Test
    void login_shouldThrowExceptionForNonExistentUser() {
        // Given: Usuario no encontrado
        when(userDetailService.loadUserByUsername("unknownUser")).thenReturn(null);

        // When & Then: Se espera una excepción BadCredentialsException
        AuthLoginRequest request = new AuthLoginRequest("unknownUser", "testPassword");
        assertThrows(BadCredentialsException.class, () -> userAuthService.login(request));
    }

    @Test
    void create_shouldCreateUser() {
        // Given
        when(userEntityRepository.save(any())).thenReturn(DataProvider.userAdmin());
        when(jwtUtil.generateToken(any(Authentication.class))).thenReturn("mocked_jwt_token");

        // When
        AuthResponse response = userAuthService.create(DataProvider.userAuthCreateUserRequestMock());

        // Then
        verify(userEntityRepository, times(1)).save(any());
        assertEquals("User created successfully", response.message());
        assertEquals("mocked_jwt_token", response.jwt());
    }
}
