package com.app.panama_trips.security.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityConfigIntegrationTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    void shouldRedirectToLoginForProtectedEndpoints() {
        // Given
        HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());

        // When
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/auth/login",
                HttpMethod.POST,
                entity,
                String.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
