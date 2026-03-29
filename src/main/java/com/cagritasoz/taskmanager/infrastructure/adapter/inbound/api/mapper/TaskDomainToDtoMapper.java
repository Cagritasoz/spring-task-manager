package com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.mapper;

import com.cagritasoz.taskmanager.domain.model.Task;
import com.cagritasoz.taskmanager.infrastructure.adapter.inbound.api.dto.response.TaskResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskDomainToDtoMapper {

    public TaskResponse toDtoModel(Task task) {

        return new TaskResponse(task.getId(),
                task.getDescription(),
                task.getDescription(),
                task.getDueDate(),
                task.getUserId());

    }

    public List<TaskResponse> toDtoModels(List<Task> taskList) {

        return taskList.stream()
                .map(this::toDtoModel)
                .toList();

    }

}
