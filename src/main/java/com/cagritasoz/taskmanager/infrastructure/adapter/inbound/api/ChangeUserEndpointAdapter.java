package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;


import com.cagritasoz.taskmanager.application.ports.inbound.CreateUserUseCase;
import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.assembler.UserModelAssembler;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.CreateUserRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.UserResponse;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.UserDomainToDtoMapper;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.UserDtoToDomainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChangeUserEndpointAdapter {

    private final CreateUserUseCase createUserUseCase;

    private final UserModelAssembler userModelAssembler;

    private final UserDtoToDomainMapper userDtoToDomainMapper;

    private final UserDomainToDtoMapper userDomainToDtoMapper;

    public EntityModel<UserResponse> createUser(CreateUserRequest createUserRequest) {

        User savedUser = createUserUseCase.createUser(userDtoToDomainMapper.toDomainModel(createUserRequest));

        return userModelAssembler.toModel(userDomainToDtoMapper.toDtoModel(savedUser));

    }


}
