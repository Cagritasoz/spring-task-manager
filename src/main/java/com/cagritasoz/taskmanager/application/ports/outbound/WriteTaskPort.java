package com.cagritasoz.taskmanager.application.ports.outbound;

import com.cagritasoz.taskmanager.domain.model.Task;

public interface WriteTaskPort {

    Task saveTask(Long userId, Task task);

    Task updateTask(Long userId, Long taskId, Task task);

    void deleteTask(Long taskId);

}
