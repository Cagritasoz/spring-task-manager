package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;

import com.cagritasoz.taskmanager.application.ports.inbound.GetUserCase;
import com.cagritasoz.taskmanager.application.ports.inbound.GetUsersUseCase;
import com.cagritasoz.taskmanager.domain.model.Pagination;
import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.assembler.UserModelAssembler;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.assembler.UserPagedModelAssembler;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.UserResponse;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.InboundPaginationMapper;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.UserDomainToDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindUserEndpointAdapter {

    private final GetUserCase getUserCase;

    private final GetUsersUseCase getUsersUseCase;

    private final UserDomainToDtoMapper userDomainToDtoMapper;

    private final InboundPaginationMapper inboundPaginationMapper;

    private final UserModelAssembler userModelAssembler;

    private final UserPagedModelAssembler userPagedModelAssembler;

    public EntityModel<UserResponse> getUser(Long id) {

        User user = getUserCase.getUser(id);

        return userModelAssembler.toModel(userDomainToDtoMapper.toDtoModel(user));

    }

    public PagedModel<EntityModel<UserResponse>> getUsers(Pageable pageable) {

        Pagination<User> pagination = getUsersUseCase.getUsers(inboundPaginationMapper.fromPageableToPagination(pageable));

        return userPagedModelAssembler.toModel(pagination);

    }

}
