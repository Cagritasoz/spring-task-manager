package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.CreateTaskUseCase;
import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.WriteTaskPort;
import com.cagritasoz.taskmanager.application.service.logbuilder.TaskCreationLogBuilder;
import com.cagritasoz.taskmanager.domain.exception.ForbiddenException;
import com.cagritasoz.taskmanager.domain.exception.UserNotFoundException;
import com.cagritasoz.taskmanager.domain.model.Action;
import com.cagritasoz.taskmanager.domain.model.Role;
import com.cagritasoz.taskmanager.domain.model.Task;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTaskService implements CreateTaskUseCase {

    private final CurrentUserPort currentUserPort;

    private final ReadUserPort readUserPort;

    private final WriteTaskPort writeTaskPort;

    private final TaskCreationLogBuilder logBuilder;

    private static final Action action = Action.CREATE;

    @Override
    public Task createTask(Long targetUserId, Task task) {

        User currentUser = currentUserPort.getCurrentUser();
        String newTaskTitle = task.getTitle();

        TaskCreationLogBuilder.CreateTaskContext context = logBuilder.createContext(currentUser, targetUserId, newTaskTitle);

        logBuilder.logAttempt(context, action);

        boolean canAccess = currentUser.getRole() == Role.ADMIN || currentUser.getId().equals(targetUserId);

        if(!canAccess) {

            logBuilder.logForbidden(context, action);

            throw new ForbiddenException();

        }

        else if(!readUserPort.existsById(targetUserId)) {

            logBuilder.logUserNotFound(context, action);

            throw new UserNotFoundException();

        }

        logBuilder.logAccessGranted(context, action);

        Task savedTask = writeTaskPort.saveTask(targetUserId, task);

        logBuilder.logSuccess(context, action);

        return savedTask;

    }
}
