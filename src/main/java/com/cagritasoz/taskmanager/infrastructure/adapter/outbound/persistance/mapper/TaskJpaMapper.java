package com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance.mapper;

import com.cagritasoz.taskmanager.domain.model.Task;
import com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance.entity.TaskEntity;
import com.cagritasoz.taskmanager.infrastructure.adapter.outbound.persistance.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskJpaMapper {

    public TaskEntity toEntityModel(Task task, UserEntity userEntity) {

        return new TaskEntity(task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                userEntity);

    }

    public Task toDomainModel(TaskEntity taskEntity, Long userId) {

        return new Task(taskEntity.getId(),
                taskEntity.getTitle(),
                taskEntity.getDescription(),
                taskEntity.getDueDate(),
                userId);

    }

    public List<Task> toDomainModels(List<TaskEntity> taskEntityList, Long userId) {

        return taskEntityList.stream()
                .map(taskEntity -> toDomainModel(taskEntity, userId))
                .toList();

    }

}
