package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;

import com.cagritasoz.taskmanager.domain.model.JwtUser;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.LoginRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.RegisterRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.AuthResponse;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthControllerAdapter authControllerAdapter;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {

        AuthResponse authResponse = authControllerAdapter.registerUser(registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse); //add links

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest loginRequest) {

        AuthResponse authResponse = authControllerAdapter.loginUser(loginRequest);
        //add links
        return ResponseEntity.ok(authResponse);

    }
}
