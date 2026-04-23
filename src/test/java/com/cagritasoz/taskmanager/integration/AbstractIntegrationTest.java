package com.cagritasoz.taskmanager.integration;

import com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance.TaskJpaRepository;
import com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected TaskJpaRepository taskJpaRepository;

    @Autowired
    protected UserJpaRepository userJpaRepository;

    protected String adminToken;

    @BeforeEach
    void baseSetUp() {
        taskJpaRepository.deleteAll();
        userJpaRepository.findAll().stream()
                .filter(u -> !u.getEmail().equals("admin@gmail.com"))
                .forEach(userJpaRepository::delete); //Delete all that are not the hardcoded admin.
        adminToken = obtainToken("admin@gmail.com", "admin123");
    }

    @SuppressWarnings("unchecked")
    protected String obtainToken(String email, String password) {
        Map<String, String> body = Map.of("email", email, "password", password);
        ResponseEntity<Map> response = restTemplate.postForEntity(authUrl("/login"), body, Map.class);
        return (String) response.getBody().get("token");
    }

    protected HttpHeaders bearerHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    protected <T> HttpEntity<T> withAuth(T body, String token) {
        return new HttpEntity<>(body, bearerHeaders(token));
    }

    protected HttpEntity<Void> withAuth(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

    protected String authUrl(String path) {
        return "http://localhost:" + port + "/api/v1/auth" + path;
    }

    protected String usersUrl(String path) {
        return "http://localhost:" + port + "/api/v1/users" + path;
    }
}
