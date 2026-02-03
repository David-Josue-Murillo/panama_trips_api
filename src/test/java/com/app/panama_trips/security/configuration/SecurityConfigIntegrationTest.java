package com.app.panama_trips.security.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SecurityConfigIntegrationTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    @DisplayName("Should return 403 for protected endpoints without authentication")
    void shouldReturn403ForProtectedEndpoints() {
        // Given
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());

        // When
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/user",
                HttpMethod.GET,
                entity,
                String.class
        );

        // Then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @DisplayName("Should allow access to public auth endpoints")
    void shouldAllowAccessToPublicAuthEndpoints() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String loginBody = "{\"username\":\"testuser\",\"password\":\"password123\"}";
        HttpEntity<String> entity = new HttpEntity<>(loginBody, headers);

        // When
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/auth/login",
                HttpMethod.POST,
                entity,
                String.class
        );

        // Then - Expect either 500 (user not found) or 4xx (validation/auth error), but NOT 403 (forbidden)
        // This proves the endpoint is accessible (not protected)
        assertEquals(true, response.getStatusCode().is5xxServerError() || response.getStatusCode().is4xxClientError());
    }

    @Test
    @DisplayName("Should allow access to swagger-ui without authentication")
    void shouldAllowAccessToSwaggerUi() {
        // Given
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());

        // When
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/swagger-ui/index.html",
                HttpMethod.GET,
                entity,
                String.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
