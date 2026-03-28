package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;


import com.cagritasoz.taskmanager.application.ports.inbound.CreateUserUseCase;
import com.cagritasoz.taskmanager.application.ports.inbound.DeleteUserUseCase;
import com.cagritasoz.taskmanager.application.ports.inbound.UpdateUserUseCase;
import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.assembler.UserEntityModelAssembler;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.CreateUserRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.UpdateUserRequest;
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

    private final UpdateUserUseCase updateUserUseCase;

    private final DeleteUserUseCase deleteUserUseCase;

    private final UserEntityModelAssembler userEntityModelAssembler;

    private final UserDtoToDomainMapper userDtoToDomainMapper;

    private final UserDomainToDtoMapper userDomainToDtoMapper;

    public EntityModel<UserResponse> createUser(CreateUserRequest createUserRequest) {

        User savedUser = createUserUseCase.createUser(userDtoToDomainMapper.toDomainModel(createUserRequest));

        return userEntityModelAssembler.toModel(userDomainToDtoMapper.toDtoModel(savedUser));

    }

    public EntityModel<UserResponse> updateUser(Long id, UpdateUserRequest updateUserRequest) {

        User updatedUser = updateUserUseCase.updateUser(id, userDtoToDomainMapper.toDomainModel(updateUserRequest));

        return userEntityModelAssembler.toModel(userDomainToDtoMapper.toDtoModel(updatedUser));

    }


    public void deleteUser(Long id) {

        deleteUserUseCase.deleteUser(id);

    }
}
