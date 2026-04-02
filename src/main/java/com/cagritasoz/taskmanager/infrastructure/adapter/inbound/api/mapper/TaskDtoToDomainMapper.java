package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper;

import com.cagritasoz.taskmanager.domain.model.Task;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.CreateTaskRequest;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.UpdateTaskRequest;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoToDomainMapper {

    public Task toDomain(CreateTaskRequest createTaskRequest) {

        return new Task(null,
                createTaskRequest.getTitle(),
                createTaskRequest.getDescription(),
                createTaskRequest.getDueDate(),
                null);

    }

    public Task toDomain(UpdateTaskRequest updateTaskRequest) {

        return new Task(null,
                updateTaskRequest.getTitle(),
                updateTaskRequest.getDescription(),
                updateTaskRequest.getDueDate(),
                null);

    }
}
