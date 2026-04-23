package com.cagritasoz.taskmanager.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerIntegrationTest extends AbstractIntegrationTest {

    private Long registerAndGetId(String username, String email, String password) {
        Map<String, String> request = Map.of("username", username, "email", email, "password", password);
        ResponseEntity<Map> response = restTemplate.postForEntity(authUrl("/register"), request, Map.class);
        Map<?, ?> user = (Map<?, ?>) response.getBody().get("user");
        return ((Number) user.get("id")).longValue();
    }

    private String loginAndGetToken(String email, String password) {
        return obtainToken(email, password);
    }

    @Test
    void adminShouldCreateUserAndReturn201() {
        Map<String, Object> request = Map.of(
                "username", "bob",
                "email", "bob@example.com",
                "password", "password123",
                "role", "USER"
        );

        ResponseEntity<Map> response = restTemplate.exchange(
                usersUrl(""),
                HttpMethod.POST,
                withAuth(request, adminToken),
                Map.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("bob", response.getBody().get("username"));
        assertTrue(response.getHeaders().containsKey("Location"));
    }

    @Test
    void adminShouldGetUserByIdAndReturn200() {
        Long userId = registerAndGetId("carol", "carol@example.com", "password123");

        ResponseEntity<Map> response = restTemplate.exchange(
                usersUrl("/" + userId),
                HttpMethod.GET,
                withAuth(adminToken),
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userId.intValue(), response.getBody().get("id"));
        assertEquals("carol", response.getBody().get("username"));
    }

    @Test
    void userShouldGetOwnProfileAndReturn200() {
        Long userId = registerAndGetId("dan", "dan@example.com", "password123");
        String userToken = loginAndGetToken("dan@example.com", "password123");

        ResponseEntity<Map> response = restTemplate.exchange(
                usersUrl("/" + userId),
                HttpMethod.GET,
                withAuth(userToken),
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("dan", response.getBody().get("username"));
    }

    @Test
    void userShouldGetForbiddenWhenAccessingAnotherUsersProfile() {
        Long userId1 = registerAndGetId("eve", "eve@example.com", "password123");
        registerAndGetId("frank", "frank@example.com", "password123");
        String frankToken = loginAndGetToken("frank@example.com", "password123");

        ResponseEntity<Map> response = restTemplate.exchange(
                usersUrl("/" + userId1),
                HttpMethod.GET,
                withAuth(frankToken),
                Map.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void adminShouldGetPaginatedUserListAndReturn200() {
        registerAndGetId("user1", "user1@example.com", "password123");
        registerAndGetId("user2", "user2@example.com", "password123");

        ResponseEntity<Map> response = restTemplate.exchange(
                usersUrl("?page=0&size=10"),
                HttpMethod.GET,
                withAuth(adminToken),
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<?, ?> page = (Map<?, ?>) response.getBody().get("page");
        // admin + 2 users = 3 total
        assertTrue(((Number) page.get("totalElements")).intValue() >= 3);
    }

    @Test
    void adminShouldUpdateUserAndReturn200() {
        Long userId = registerAndGetId("grace", "grace@example.com", "password123");
        Map<String, Object> updateRequest = Map.of(
                "username", "grace_updated",
                "email", "grace_updated@example.com",
                "password", "newpassword123",
                "role", "USER"
        );

        ResponseEntity<Map> response = restTemplate.exchange(
                usersUrl("/" + userId),
                HttpMethod.PUT,
                withAuth(updateRequest, adminToken),
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("grace_updated", response.getBody().get("username"));
    }

    @Test
    void adminShouldDeleteUserAndReturn204() {
        Long userId = registerAndGetId("henry", "henry@example.com", "password123");

        ResponseEntity<Void> response = restTemplate.exchange(
                usersUrl("/" + userId),
                HttpMethod.DELETE,
                withAuth(adminToken),
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Verify user is actually gone
        ResponseEntity<Map> getResponse = restTemplate.exchange(
                usersUrl("/" + userId),
                HttpMethod.GET,
                withAuth(adminToken),
                Map.class
        );
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void shouldReturn401WhenAccessingProtectedEndpointWithoutToken() {
        ResponseEntity<Map> response = restTemplate.getForEntity(usersUrl("/1"), Map.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
