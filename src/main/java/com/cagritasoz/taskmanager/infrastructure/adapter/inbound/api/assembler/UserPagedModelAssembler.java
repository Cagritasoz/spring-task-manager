package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.assembler;

import com.cagritasoz.taskmanager.domain.model.Pagination;
import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.UserResponse;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.InboundPaginationMapper;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.UserDomainToDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserPagedModelAssembler
        implements RepresentationModelAssembler<Pagination<User>, PagedModel<EntityModel<UserResponse>>> {

    private final InboundPaginationMapper paginationMapper;

    private final UserDomainToDtoMapper userDomainToDtoMapper;

    private final UserEntityModelAssembler userEntityModelAssembler;

    private final PagedResourcesAssembler<UserResponse> pagedResourcesAssembler;

    @Override
    public @NonNull PagedModel<EntityModel<UserResponse>> toModel(@NonNull Pagination<User> pagination) {

        List<UserResponse> users = userDomainToDtoMapper.toDtoModels(pagination.getContent());

        Pageable pageable = paginationMapper.fromPaginationToPageable(pagination);

        Page<UserResponse> page = new PageImpl<>(users,
                pageable,
                pagination.getTotalElements());

        return pagedResourcesAssembler.toModel(page, userEntityModelAssembler);

    }
}
