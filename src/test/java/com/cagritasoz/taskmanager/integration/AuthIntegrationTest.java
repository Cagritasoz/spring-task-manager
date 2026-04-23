package com.cagritasoz.taskmanager.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AuthIntegrationTest extends AbstractIntegrationTest {

    @Test
    void shouldRegisterNewUserAndReturn201() {
        Map<String, String> request = Map.of(
                "username", "alice",
                "email", "alice@example.com",
                "password", "password123"
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(authUrl("/register"), request, Map.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().get("token"));
        Map<?, ?> user = (Map<?, ?>) response.getBody().get("user");
        assertEquals("alice", user.get("username"));
        assertEquals("USER", user.get("role")); // register always creates USER role
        assertTrue(response.getHeaders().containsKey("Location"));
    }

    @Test
    void shouldReturn409WhenRegisteringWithAlreadyUsedEmail() {
        Map<String, String> request = Map.of(
                "username", "duplicate",
                "email", "alice@example.com",
                "password", "password123"
        );
        restTemplate.postForEntity(authUrl("/register"), request, Map.class); // first registration

        ResponseEntity<Map> response = restTemplate.postForEntity(authUrl("/register"), request, Map.class); // duplicate

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void shouldReturn400WhenRegisteringWithInvalidData() {
        Map<String, String> request = Map.of(
                "username", "ab", // too short (min 3)
                "email", "not-an-email",
                "password", ""   // blank
        );

        ResponseEntity<Map> response = restTemplate.postForEntity(authUrl("/register"), request, Map.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldLoginSuccessfullyAndReturnToken() {
        ResponseEntity<Map> response = restTemplate.postForEntity(
                authUrl("/login"),
                Map.of("email", "admin@gmail.com", "password", "admin123"),
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        String token = (String) response.getBody().get("token");
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void shouldReturn401WhenLoginWithWrongPassword() {
        ResponseEntity<Map> response = restTemplate.postForEntity(
                authUrl("/login"),
                Map.of("email", "admin@gmail.com", "password", "wrong_password"),
                Map.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
