package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.assembler;

import com.cagritasoz.taskmanager.domain.model.Pagination;
import com.cagritasoz.taskmanager.domain.model.Task;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.TaskResponse;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.InboundPaginationMapper;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.TaskDomainToDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskPagedModelAssembler
        implements RepresentationModelAssembler<Pagination<Task>, PagedModel<EntityModel<TaskResponse>>> {

    private final InboundPaginationMapper paginationMapper;

    private final TaskDomainToDtoMapper taskDomainToDtoMapper;

    private final TaskEntityModelAssembler taskEntityModelAssembler;

    private final PagedResourcesAssembler<TaskResponse> pagedResourcesAssembler;

    @Override
    public @NonNull PagedModel<EntityModel<TaskResponse>> toModel(@NonNull Pagination<Task> pagination) {

        List<TaskResponse> tasks = taskDomainToDtoMapper.toDtoModels(pagination.getContent());

        Pageable pageable = paginationMapper.fromPaginationToPageable(pagination);

        Page<TaskResponse> page = new PageImpl<>(tasks,
                pageable,
                pagination.getTotalElements());

        return pagedResourcesAssembler.toModel(page, taskEntityModelAssembler);

    }
}
