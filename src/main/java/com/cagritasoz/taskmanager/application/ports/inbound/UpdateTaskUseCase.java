package com.cagritasoz.taskmanager.application.ports.inbound;

import com.cagritasoz.taskmanager.domain.model.Task;

public interface UpdateTaskUseCase {

    Task updateTask(Long userId, Long taskId, Task task);

}
