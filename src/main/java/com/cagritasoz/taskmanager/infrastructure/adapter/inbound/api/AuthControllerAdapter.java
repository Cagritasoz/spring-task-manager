package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;


import com.cagritasoz.taskmanager.application.ports.inbound.AuthUseCase;
import com.cagritasoz.taskmanager.domain.model.JwtUser;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.assembler.UserModelAssembler;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.LoginRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.RegisterRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.AuthResponse;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.JwtResponse;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.UserDomainToDtoMapper;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.UserDtoToDomainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthControllerAdapter {

    private final AuthUseCase authUseCase;

    private final UserModelAssembler userModelAssembler;

    private final UserDtoToDomainMapper userDtoToDomainMapper;

    private final UserDomainToDtoMapper userDomainToDtoMapper;

    public AuthResponse registerUser(RegisterRequest registerRequest) {

        JwtUser jwtUser = authUseCase.registerUser(userDtoToDomainMapper.toDomainModel(registerRequest));

        return new AuthResponse(userModelAssembler
                .toModel(userDomainToDtoMapper.toDtoModel(jwtUser.getUser())),
                new JwtResponse(jwtUser.getJwt()));

    }

    public AuthResponse loginUser(LoginRequest loginRequest) {

        JwtUser jwtUser = authUseCase.loginUser(loginRequest.getEmail(), loginRequest.getPassword());

        return new AuthResponse(userModelAssembler
                .toModel(userDomainToDtoMapper.toDtoModel(jwtUser.getUser())),
                new JwtResponse(jwtUser.getJwt()));

    }





}
