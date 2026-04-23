package com.cagritasoz.taskmanager.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TaskControllerIntegrationTest extends AbstractIntegrationTest {

    private Long userId;
    private String userToken;

    @BeforeEach
    void setUpUser() {
        Map<String, String> registerRequest = Map.of(
                "username", "taskuser",
                "email", "taskuser@example.com",
                "password", "password123"
        );
        ResponseEntity<Map> registerResponse = restTemplate.postForEntity(
                authUrl("/register"), registerRequest, Map.class);
        Map<?, ?> user = (Map<?, ?>) registerResponse.getBody().get("user");
        userId = ((Number) user.get("id")).longValue();
        userToken = obtainToken("taskuser@example.com", "password123");
    }

    private Map<String, Object> taskCreateRequest() {
        return Map.of(
                "title", "Fix bug #42",
                "description", "Investigate and fix the null pointer exception in login flow",
                "dueDate", "31-12-2030 10:00:00"
        );
    }

    private Long createTask() {
        ResponseEntity<Map> response = restTemplate.exchange(
                usersUrl("/" + userId + "/tasks"),
                HttpMethod.POST,
                withAuth(taskCreateRequest(), userToken),
                Map.class
        );
        return ((Number) response.getBody().get("taskId")).longValue();
    }

    @Test
    void userShouldCreateTaskForOwnAccountAndReturn201() {
        ResponseEntity<Map> response = restTemplate.exchange(
                usersUrl("/" + userId + "/tasks"),
                HttpMethod.POST,
                withAuth(taskCreateRequest(), userToken),
                Map.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Fix bug #42", response.getBody().get("title"));
        assertEquals(userId.intValue(), response.getBody().get("userId"));
        assertTrue(response.getHeaders().containsKey("Location"));
    }

    @Test
    void userShouldGetOwnTaskAndReturn200() {
        Long taskId = createTask();

        ResponseEntity<Map> response = restTemplate.exchange(
                usersUrl("/" + userId + "/tasks/" + taskId),
                HttpMethod.GET,
                withAuth(userToken),
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskId.intValue(), response.getBody().get("taskId"));
        assertEquals("Fix bug #42", response.getBody().get("title"));
    }

    @Test
    void userShouldGetPaginatedTaskListAndReturn200() {
        createTask();
        createTask();

        ResponseEntity<Map> response = restTemplate.exchange(
                usersUrl("/" + userId + "/tasks?page=0&size=10"),
                HttpMethod.GET,
                withAuth(userToken),
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<?, ?> page = (Map<?, ?>) response.getBody().get("page");
        assertTrue(((Number) page.get("totalElements")).intValue() >= 2);
    }

    @Test
    void userShouldUpdateOwnTaskAndReturn200() {
        Long taskId = createTask();
        Map<String, Object> updateRequest = Map.of(
                "title", "Fix bug #42 (resolved)",
                "description", "Null pointer was caused by missing null check in AuthService",
                "dueDate", "31-12-2030 10:00:00"
        );

        ResponseEntity<Map> response = restTemplate.exchange(
                usersUrl("/" + userId + "/tasks/" + taskId),
                HttpMethod.PUT,
                withAuth(updateRequest, userToken),
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Fix bug #42 (resolved)", response.getBody().get("title"));
    }

    @Test
    void userShouldDeleteOwnTaskAndReturn204() {
        Long taskId = createTask();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                usersUrl("/" + userId + "/tasks/" + taskId),
                HttpMethod.DELETE,
                withAuth(userToken),
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<Map> getResponse = restTemplate.exchange(
                usersUrl("/" + userId + "/tasks/" + taskId),
                HttpMethod.GET,
                withAuth(userToken),
                Map.class
        );
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void userShouldGetForbiddenWhenCreatingTaskForAnotherUser() {
        // Register a second user
        restTemplate.postForEntity(authUrl("/register"),
                Map.of("username", "other", "email", "other@example.com", "password", "password123"),
                Map.class);
        String otherToken = obtainToken("other@example.com", "password123");

        // otherToken user tries to create a task for userId (taskuser's account)
        ResponseEntity<Map> response = restTemplate.exchange(
                usersUrl("/" + userId + "/tasks"),
                HttpMethod.POST,
                withAuth(taskCreateRequest(), otherToken),
                Map.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void adminShouldCreateTaskForAnyUserAndReturn201() {
        ResponseEntity<Map> response = restTemplate.exchange(
                usersUrl("/" + userId + "/tasks"),
                HttpMethod.POST,
                withAuth(taskCreateRequest(), adminToken),
                Map.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userId.intValue(), response.getBody().get("userId"));
    }
}
