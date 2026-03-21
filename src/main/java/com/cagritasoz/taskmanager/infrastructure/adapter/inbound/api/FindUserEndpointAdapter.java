package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;

import com.cagritasoz.taskmanager.application.ports.inbound.GetUserCase;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.UserResponse;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindUserEndpointAdapter {

    private final GetUserCase getUserCase;

    private final UserMapper userMapper;

    public UserResponse getUser(Long id) {

        return userMapper.toDtoModel(getUserCase.getUser(id));

    }
}
