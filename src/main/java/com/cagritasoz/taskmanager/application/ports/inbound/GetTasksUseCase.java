package com.cagritasoz.taskmanager.application.ports.inbound;

import com.cagritasoz.taskmanager.domain.model.Pagination;
import com.cagritasoz.taskmanager.domain.model.Task;

public interface GetTasksUseCase {

    Pagination<Task> getTasks(Long userId, Pagination<Task> pagination);

}
