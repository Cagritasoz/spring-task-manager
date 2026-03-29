package com.cagritasoz.taskmanager.application.ports.outbound;

import com.cagritasoz.taskmanager.domain.model.Task;

public interface WriteTaskPort {

    Task saveTask(Long userId, Task task);

}
