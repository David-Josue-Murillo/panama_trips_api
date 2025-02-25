package com.app.panama_trips.util;

import com.app.panama_trips.utility.JwtUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JwtUtilTest {

    private JwtUtil jwtUtil;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        this.jwtUtil = new JwtUtil();
        this.jwtUtil.setPrivateKey("testPrivateKey");
        this.jwtUtil.setUseGenerator("testIssuer");
        this.authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("testUser");
        when(authentication.getAuthorities()).thenReturn(Collections.emptyList());
    }

    @Test
    void generateToken_shouldReturnValidToken() {
        String token = jwtUtil.generateToken(authentication);
        assertNotNull(token);
    }

    @Test
    void generateToken_shouldContainCorrectClaims() {
        String token = jwtUtil.generateToken(authentication);
        DecodedJWT decodedJWT = jwtUtil.validateToken(token);

        assertEquals("testIssuer", decodedJWT.getIssuer());
        assertEquals("testUser", decodedJWT.getSubject());
    }

    @Test
    void validateToken_shouldReturnDecodedJWT() {
        String token = jwtUtil.generateToken(authentication);
        DecodedJWT decodedJWT = jwtUtil.validateToken(token);

        assertNotNull(decodedJWT);
    }

    @Test
    void validateToken_shouldThrowExceptionForInvalidToken() {
        assertThrows(JWTVerificationException.class, () -> jwtUtil.validateToken("invalidToken"));
    }

    @Test
    void getUsernameFromToken_shouldReturnCorrectUsername() {
        String token = jwtUtil.generateToken(authentication);
        DecodedJWT decodedJWT = jwtUtil.validateToken(token);

        assertEquals("testUser", jwtUtil.getUsernameFromToken(decodedJWT));
    }

    @Test
    void getSpecificClaim_shouldReturnCorrectClaim() {
        String token = jwtUtil.generateToken(authentication);
        DecodedJWT decodedJWT = jwtUtil.validateToken(token);

        assertEquals("", jwtUtil.getSpecificClaim(decodedJWT, "authorities").asString());
    }

    @Test
    void getAllClaims_shouldReturnAllClaims() {
        String token = jwtUtil.generateToken(authentication);
        DecodedJWT decodedJWT = jwtUtil.validateToken(token);

        assertNotNull(jwtUtil.getAllClaims(decodedJWT));
        assertTrue(jwtUtil.getAllClaims(decodedJWT).containsKey("authorities"));
    }
}
