package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;


import com.cagritasoz.taskmanager.application.ports.inbound.AuthUseCase;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.LoginRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.RegisterRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.AuthResponse;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthControllerAdapter {

    private final AuthUseCase authUseCase;

    private final UserMapper userMapper;

    public AuthResponse registerUser(RegisterRequest registerRequest) {

        return userMapper.toDtoModel(authUseCase.registerUser(userMapper.toDomainModel(registerRequest)));

    }

    public AuthResponse loginUser(LoginRequest loginRequest) {

        return userMapper.toDtoModel(authUseCase.loginUser(loginRequest.getEmail(), loginRequest.getPassword()));

    }





}
