package com.app.panama_trips.security.filter;

import com.app.panama_trips.utility.JwtUtil;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenValidatorTest {

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
        SecurityContextHolder.clearContext();
    }

    @Nested
    @DisplayName("Valid Token Tests")
    class ValidTokenTests {

        @Test
        @DisplayName("Should set authentication when token is valid with single role")
        void doFilterChain_shouldSetAuthentication_whenTokenIsValid() throws ServletException, IOException {
            // Given
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer ".concat(VALID_JWT));
            when(jwtUtil.validateToken(VALID_JWT)).thenReturn(decodedJWT);
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
        @DisplayName("Should set authentication with multiple authorities")
        void doFilterChain_shouldSetMultipleAuthorities_whenTokenHasMultipleRoles() throws ServletException, IOException {
            // Given
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer ".concat(VALID_JWT));
            when(jwtUtil.validateToken(VALID_JWT)).thenReturn(decodedJWT);
            when(jwtUtil.getUsernameFromToken(decodedJWT)).thenReturn("admin");
            when(jwtUtil.getSpecificClaim(decodedJWT, "authorities")).thenReturn(claim);
            when(claim.asString()).thenReturn("ROLE_ADMIN,USER_READ,USER_CREATE");

            // When
            jwtTokenValidator.doFilterInternal(request, response, filterChain);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();

            // Then
            assertNotNull(authentication);
            assertEquals("admin", authentication.getName());
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            assertEquals(3, authorities.size());
            String authoritiesString = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));
            assertTrue(authoritiesString.contains("ROLE_ADMIN"));
            assertTrue(authoritiesString.contains("USER_READ"));
            assertTrue(authoritiesString.contains("USER_CREATE"));
            verify(filterChain).doFilter(request, response);
        }

        @Test
        @DisplayName("Should set credentials to null in authentication")
        void doFilterChain_shouldSetCredentialsToNull_whenAuthenticated() throws ServletException, IOException {
            // Given
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer ".concat(VALID_JWT));
            when(jwtUtil.validateToken(VALID_JWT)).thenReturn(decodedJWT);
            when(jwtUtil.getUsernameFromToken(decodedJWT)).thenReturn("username");
            when(jwtUtil.getSpecificClaim(decodedJWT, "authorities")).thenReturn(claim);
            when(claim.asString()).thenReturn("ROLE_CUSTOMER");

            // When
            jwtTokenValidator.doFilterInternal(request, response, filterChain);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Then
            assertNotNull(authentication);
            assertNull(authentication.getCredentials());
            verify(filterChain).doFilter(request, response);
        }
    }

    @Nested
    @DisplayName("Invalid Token Tests")
    class InvalidTokenTests {

        @Test
        @DisplayName("Should not set authentication when token is invalid")
        void doFilterChain_shouldNotSetAuthentication_whenTokenIsInvalid() throws ServletException, IOException {
            // Given
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer invalid.jwt.token");
            when(jwtUtil.validateToken("invalid.jwt.token")).thenReturn(null);

            // When
            jwtTokenValidator.doFilterInternal(request, response, filterChain);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();

            // Then
            assertNull(authentication);
            verify(filterChain).doFilter(request, response);
        }

        @Test
        @DisplayName("Should not set authentication when authorization header is null")
        void doFilterChain_shouldNotSetAuthentication_whenHeaderIsNull() throws ServletException, IOException {
            // Given
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

            // When
            jwtTokenValidator.doFilterInternal(request, response, filterChain);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Then
            assertNull(authentication);
            verify(filterChain).doFilter(request, response);
            verify(jwtUtil, never()).validateToken(anyString());
        }

        @Test
        @DisplayName("Should not set authentication when header does not start with Bearer")
        void doFilterChain_shouldNotSetAuthentication_whenHeaderDoesNotStartWithBearer() throws ServletException, IOException {
            // Given
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Basic dXNlcm5hbWU6cGFzc3dvcmQ=");

            // When
            jwtTokenValidator.doFilterInternal(request, response, filterChain);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Then
            assertNull(authentication);
            verify(filterChain).doFilter(request, response);
            verify(jwtUtil, never()).validateToken(anyString());
        }

        @Test
        @DisplayName("Should not set authentication when header is just Bearer without token")
        void doFilterChain_shouldNotSetAuthentication_whenHeaderIsOnlyBearer() throws ServletException, IOException {
            // Given
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer ");
            when(jwtUtil.validateToken("")).thenReturn(null);

            // When
            jwtTokenValidator.doFilterInternal(request, response, filterChain);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Then
            assertNull(authentication);
            verify(filterChain).doFilter(request, response);
        }

        @Test
        @DisplayName("Should not set authentication when authorization header is empty string")
        void doFilterChain_shouldNotSetAuthentication_whenHeaderIsEmpty() throws ServletException, IOException {
            // Given
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("");

            // When
            jwtTokenValidator.doFilterInternal(request, response, filterChain);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Then
            assertNull(authentication);
            verify(filterChain).doFilter(request, response);
            verify(jwtUtil, never()).validateToken(anyString());
        }
    }

    @Nested
    @DisplayName("Filter Chain Tests")
    class FilterChainTests {

        @Test
        @DisplayName("Should always continue filter chain regardless of authentication result")
        void doFilterChain_shouldAlwaysContinueFilterChain() throws ServletException, IOException {
            // Given - no authorization header
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

            // When
            jwtTokenValidator.doFilterInternal(request, response, filterChain);

            // Then
            verify(filterChain, times(1)).doFilter(request, response);
        }

        @Test
        @DisplayName("Should continue filter chain after successful authentication")
        void doFilterChain_shouldContinueFilterChain_afterSuccessfulAuth() throws ServletException, IOException {
            // Given
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer ".concat(VALID_JWT));
            when(jwtUtil.validateToken(VALID_JWT)).thenReturn(decodedJWT);
            when(jwtUtil.getUsernameFromToken(decodedJWT)).thenReturn("username");
            when(jwtUtil.getSpecificClaim(decodedJWT, "authorities")).thenReturn(claim);
            when(claim.asString()).thenReturn("ROLE_ADMIN");

            // When
            jwtTokenValidator.doFilterInternal(request, response, filterChain);

            // Then
            verify(filterChain, times(1)).doFilter(request, response);
        }
    }
}
