package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.assembler;

import com.cagritasoz.taskmanager.domain.model.Pagination;
import com.cagritasoz.taskmanager.domain.model.SortField;
import com.cagritasoz.taskmanager.domain.model.User;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.UserResponse;
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
public class UserPagedModelAssembler implements RepresentationModelAssembler<Pagination<User>, PagedModel<EntityModel<UserResponse>>> {

    private final UserDomainToDtoMapper userDomainToDtoMapper;

    private final UserModelAssembler userModelAssembler;

    private final PagedResourcesAssembler<UserResponse> pagedResourcesAssembler;

    @Override
    public @NonNull PagedModel<EntityModel<UserResponse>> toModel(@NonNull Pagination<User> pagination) {

        List<UserResponse> users = userDomainToDtoMapper.toDtoModels(pagination.getContent());

        Pageable pageable = fromPaginationToPageable(pagination);

        Page<UserResponse> page = new PageImpl<>(users,
                pageable,
                pagination.getTotalElements());

        return pagedResourcesAssembler.toModel(page, userModelAssembler);

    }

    private <T> Pageable fromPaginationToPageable(Pagination<T> pagination) {

        Sort sort;

        if(!pagination.getSortFields().isEmpty()) {

            List<Sort.Order> orders = new ArrayList<>();

            for(SortField sortField : pagination.getSortFields()) {

                Sort.Order order = sortField.isAscending()
                        ? Sort.Order.asc(sortField.getProperty())
                        : Sort.Order.desc(sortField.getProperty());

                orders.add(order);

            }

            sort = Sort.by(orders);

            return PageRequest.of(pagination.getPageNumber(),
                    pagination.getSize(),
                    sort);

        }

        return PageRequest.of(pagination.getPageNumber(),
                pagination.getSize());
    }
}
