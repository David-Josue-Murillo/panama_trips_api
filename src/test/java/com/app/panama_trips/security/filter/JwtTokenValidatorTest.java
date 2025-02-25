package com.app.panama_trips.security.filter;

import com.app.panama_trips.utility.JwtUtil;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtTokenValidatorTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private DecodedJWT decodedJWT;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Claim claim;

    @InjectMocks
    private JwtTokenValidator jwtTokenValidator;

    private static final String VALID_JWT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwYW5hbWEtdHJpcHMiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjoiQk9PS0lOR19DUkVBVEUsQk9PS0lOR19ERUxFVEUsQk9PS0lOR19SRUFELEJPT0tJTkdfVVBEQVRFLENPTlRFTlRfQ1JFQVRFLENPTlRFTlRfREVMRVRFLENPTlRFTlRfVVBEQVRFLFBBWU1FTlRfUkVBRCxQQVlNRU5UX1JFRlVORCxST0xFX0FETUlOLFNVUFBPUlRfVElDS0VUX1JFQUQsU1VQUE9SVF9USUNLRVRfUkVTUE9ORCxTWVNURU1fTE9HX1JFQUQsVVNFUl9DUkVBVEUsVVNFUl9ERUxFVEUsVVNFUl9SRUFELFVTRVJfVVBEQVRFIiwiaWF0IjoxNzQwNDI3NDg4LCJleHAiOjE3NDA0MjkyODgsImp0aSI6IjZkMjI0YTYwLTM4MjUtNDg0MC1iNDdiLThhYjUyNTZjZWM3MyIsIm5iZiI6MTc0MDQyNzQ4OH0.LLWT0vGqYCJbfjndWHiD0pJ_VKLxxhNOfzCHZ1hO6U4";

    @BeforeEach
    void setUp() {
        // Limpiar el contexto de seguridad antes de cada prueba
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterChain_shouldSetAuthentication_whenTokenIsValid() throws ServletException, IOException {
        // Given
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer ".concat(VALID_JWT));
        when(jwtUtil.validateToken((VALID_JWT))).thenReturn(decodedJWT);
        when(jwtUtil.getUsernameFromToken(decodedJWT)).thenReturn("username");
        when(jwtUtil.getSpecificClaim(decodedJWT, "authorities")).thenReturn(claim);
        when(claim.asString()).thenReturn("ROLE_ADMIN");

        // When
        jwtTokenValidator.doFilterInternal(request, response, filterChain);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        // Then
        assertNotNull(authentication);
        assertEquals("username", authentication.getName());
        assertEquals("ROLE_ADMIN", authentication.getAuthorities().iterator().next().getAuthority());

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterChain_shouldNotSetAuthentication_whenTokenIsInvalid() throws ServletException, IOException {
        // Given
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer invalid.jwt.token");
        when(jwtUtil.validateToken("invalid.jwt.token")).thenReturn(null);

        // When
        jwtTokenValidator.doFilterInternal(request, response, filterChain);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        // Then
        assertEquals(null, authentication); // No debe establecer autenticación
        verify(filterChain).doFilter(request, response);
    }
}
