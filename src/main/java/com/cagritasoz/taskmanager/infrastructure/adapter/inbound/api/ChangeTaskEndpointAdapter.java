package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api;

import com.cagritasoz.taskmanager.application.ports.inbound.CreateTaskUseCase;
import com.cagritasoz.taskmanager.application.ports.inbound.DeleteTaskUseCase;
import com.cagritasoz.taskmanager.application.ports.inbound.UpdateTaskUseCase;
import com.cagritasoz.taskmanager.domain.model.Task;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.assembler.TaskEntityModelAssembler;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.CreateTaskRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.UpdateTaskRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.TaskResponse;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.TaskDomainToDtoMapper;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper.TaskDtoToDomainMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChangeTaskEndpointAdapter {

    private final CreateTaskUseCase createTaskUseCase;

    private final UpdateTaskUseCase updateTaskUseCase;

    private final DeleteTaskUseCase deleteTaskUseCase;

    private final TaskEntityModelAssembler taskEntityModelAssembler;

    private final TaskDtoToDomainMapper taskDtoToDomainMapper;

    private final TaskDomainToDtoMapper taskDomainToDtoMapper;

    public EntityModel<TaskResponse> createTask(Long userId,
                                                CreateTaskRequest createTaskRequest) {

        Task savedTask = createTaskUseCase.createTask(userId, taskDtoToDomainMapper.toDomain(createTaskRequest));

        return taskEntityModelAssembler.toModel(taskDomainToDtoMapper.toDtoModel(savedTask));

    }

    public EntityModel<TaskResponse> updateTask(Long userId,
                                                Long taskId,
                                                @Valid UpdateTaskRequest updateTaskRequest) {

        Task updatedTask = updateTaskUseCase.updateTask(userId, taskId, taskDtoToDomainMapper.toDomain(updateTaskRequest));

        return taskEntityModelAssembler.toModel(taskDomainToDtoMapper.toDtoModel(updatedTask));

    }

    public void deleteTask(Long userId, Long taskId) {

        deleteTaskUseCase.deleteTask(userId, taskId);

    }
}
