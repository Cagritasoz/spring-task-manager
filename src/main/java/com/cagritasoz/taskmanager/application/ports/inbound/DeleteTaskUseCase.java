package com.cagritasoz.taskmanager.application.ports.inbound;

public interface DeleteTaskUseCase {

    void deleteTask(Long userId, Long taskId);

}
