package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;


import com.cagritasoz.taskmanager.application.ports.inbound.CreateUserUseCase;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.CreateUserRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.UserResponse;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChangeUserEndpointAdapter {

    private final CreateUserUseCase createUserUseCase;

    private final UserMapper userMapper;

    public UserResponse createUser(CreateUserRequest createUserRequest) {

        return null;

    }



}
