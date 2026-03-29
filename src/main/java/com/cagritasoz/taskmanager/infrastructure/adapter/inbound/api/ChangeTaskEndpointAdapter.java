package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;

import com.cagritasoz.taskmanager.application.ports.inbound.CreateTaskUseCase;
import com.cagritasoz.taskmanager.domain.model.Task;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.assembler.TaskEntityModelAssembler;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.CreateTaskRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.TaskResponse;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.TaskDomainToDtoMapper;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.TaskDtoToDomainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChangeTaskEndpointAdapter {

    private final CreateTaskUseCase createTaskUseCase;

    private final TaskEntityModelAssembler taskEntityModelAssembler;

    private final TaskDtoToDomainMapper taskDtoToDomainMapper;

    private final TaskDomainToDtoMapper taskDomainToDtoMapper;

    public EntityModel<TaskResponse> createTask(Long userId,
                                                CreateTaskRequest createTaskRequest) {

        Task savedTask = createTaskUseCase.createTask(userId, taskDtoToDomainMapper.toDomain(createTaskRequest));

        return taskEntityModelAssembler.toModel(taskDomainToDtoMapper.toDtoModel(savedTask));

    }
}
