package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;


import com.cagritasoz.taskmanager.application.ports.inbound.AuthUseCase;
import com.cagritasoz.taskmanager.domain.model.UserWithToken;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.assembler.UserEntityModelAssembler;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.LoginRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.RegisterRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.AuthResponse;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.UserDomainToDtoMapper;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.UserDtoToDomainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthControllerAdapter {

    private final AuthUseCase authUseCase;

    private final UserEntityModelAssembler userEntityModelAssembler;

    private final UserDtoToDomainMapper dtoToDomainMapper;

    private final UserDomainToDtoMapper domainToDtoMapper;

    public AuthResponse registerUser(RegisterRequest registerRequest) {

        UserWithToken userWithToken = authUseCase.registerUser(dtoToDomainMapper.toDomainModel(registerRequest));

        return new AuthResponse(userWithToken.getToken(),
                userEntityModelAssembler.toModel(domainToDtoMapper.toDtoModel(userWithToken.getUser())));

    }

    public AuthResponse loginUser(LoginRequest loginRequest) {

        UserWithToken userWithToken = authUseCase.loginUser(loginRequest.getEmail(), loginRequest.getPassword());

        return new AuthResponse(userWithToken.getToken(),
                userEntityModelAssembler.toModel(domainToDtoMapper.toDtoModel(userWithToken.getUser())));

    }





}
