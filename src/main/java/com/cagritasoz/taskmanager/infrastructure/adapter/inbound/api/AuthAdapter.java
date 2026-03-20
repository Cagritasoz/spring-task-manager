package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;


import com.cagritasoz.taskmanager.application.ports.inbound.AuthUseCase;
import com.cagritasoz.taskmanager.domain.model.AuthenticatedUser;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.LoginRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.RegisterRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthAdapter {

    private final AuthUseCase authUseCase;

    private final UserMapper userMapper;

    public AuthenticatedUser registerUser(RegisterRequest registerRequest) {

        return authUseCase.registerUser(userMapper.toDomainModel(registerRequest));

    }

    public AuthenticatedUser loginUser(LoginRequest loginRequest) {

        return authUseCase.loginUser(loginRequest.getEmail(), loginRequest.getPassword());

    }





}
