package com.app.panama_trips.security.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityConfigIntegrationTest {

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJwYW5hbWEtdHJpcHMiLCJzdWIiOiJhZG1pbiIsImF1dGhvcml0aWVzIjoiQk9PS0lOR19DUkVBVEUsQk9PS0lOR19ERUxFVEUsQk9PS0lOR19SRUFELEJPT0tJTkdfVVBEQVRFLENPTlRFTlRfQ1JFQVRFLENPTlRFTlRfREVMRVRFLENPTlRFTlRfVVBEQVRFLFBBWU1FTlRfUkVBRCxQQVlNRU5UX1JFRlVORCxST0xFX0FETUlOLFNVUFBPUlRfVElDS0VUX1JFQUQsU1VQUE9SVF9USUNLRVRfUkVTUE9ORCxTWVNURU1fTE9HX1JFQUQsVVNFUl9DUkVBVEUsVVNFUl9ERUxFVEUsVVNFUl9SRUFELFVTRVJfVVBEQVRFIiwiaWF0IjoxNzQwNDI3NDg4LCJleHAiOjE3NDA0MjkyODgsImp0aSI6IjZkMjI0YTYwLTM4MjUtNDg0MC1iNDdiLThhYjUyNTZjZWM3MyIsIm5iZiI6MTc0MDQyNzQ4OH0.LLWT0vGqYCJbfjndWHiD0pJ_VKLxxhNOfzCHZ1hO6U4";

    @Test
    void shouldAllowAccessToAuthEndpointsWithoutAuthentication() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(JWT_TOKEN);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/user",
                HttpMethod.GET,
                entity,
                String.class
        );
    }
}
