package com.cagritasoz.taskmanager.application.ports.inbound;

import com.cagritasoz.taskmanager.domain.model.Task;

public interface GetTaskUseCase {

    Task getTask(Long userId, Long taskId);

}
