package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper;

import com.cagritasoz.taskmanager.domain.model.Task;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.request.CreateTaskRequest;
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
}
