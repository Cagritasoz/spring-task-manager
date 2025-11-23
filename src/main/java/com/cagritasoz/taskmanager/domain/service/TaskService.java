package com.cagritasoz.taskmanager.domain.service;

import com.cagritasoz.taskmanager.domain.ports.inbound.TaskUseCase;
import com.cagritasoz.taskmanager.domain.ports.outbound.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService implements TaskUseCase {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
}
