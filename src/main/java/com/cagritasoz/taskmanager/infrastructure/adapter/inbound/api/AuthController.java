package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;

import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.LoginRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.RegisterRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthControllerAdapter authControllerAdapter;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {

        AuthResponse authResponse = authControllerAdapter.registerUser(registerRequest);

        URI location = authResponse.getUser().getRequiredLink("self").toUri();

        return ResponseEntity.created(location).body(authResponse);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest loginRequest) {

        AuthResponse authResponse = authControllerAdapter.loginUser(loginRequest);

        return ResponseEntity.ok(authResponse);

    }
}
