package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;

import com.cagritasoz.taskmanager.application.ports.inbound.GetTaskUseCase;
import com.cagritasoz.taskmanager.application.ports.inbound.GetTasksUseCase;
import com.cagritasoz.taskmanager.domain.model.Pagination;
import com.cagritasoz.taskmanager.domain.model.Task;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.assembler.TaskEntityModelAssembler;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.assembler.TaskPagedModelAssembler;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.TaskResponse;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.InboundPaginationMapper;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.TaskDomainToDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindTaskEndpointAdapter {

    private final GetTaskUseCase getTaskUseCase;

    private final GetTasksUseCase getTasksUseCase;

    private final TaskEntityModelAssembler taskEntityModelAssembler;

    private final TaskPagedModelAssembler taskPagedModelAssembler;

    private final InboundPaginationMapper paginationMapper;

    private final TaskDomainToDtoMapper domainToDtoMapper;

    public EntityModel<TaskResponse> getTask(Long userId, Long taskId) {

        Task foundTask = getTaskUseCase.getTask(userId, taskId);

        return taskEntityModelAssembler.toModel(domainToDtoMapper.toDtoModel(foundTask));

    }

    public PagedModel<EntityModel<TaskResponse>> getTasks(Long userId, Pageable pageable) {

        Pagination<Task> pagination = getTasksUseCase.getTasks(userId,
                paginationMapper.fromPageableToPagination(pageable));

        return taskPagedModelAssembler.toModel(pagination);

    }
}
