package com.cagritasoz.taskmanager.application.ports.inbound;

import com.cagritasoz.taskmanager.domain.model.Task;

public interface CreateTaskUseCase {

    Task createTask(Long userId, Task task);

}
