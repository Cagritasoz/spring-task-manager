package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;

import com.cagritasoz.taskmanager.domain.model.AuthenticatedUser;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.RegisterRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    private final AuthAdapter authAdapter;

    @PostMapping("/register")
    public ResponseEntity<AuthenticatedUser> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {

        AuthenticatedUser authenticatedUser = authAdapter.registerUser(registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(authenticatedUser);

    }
}
