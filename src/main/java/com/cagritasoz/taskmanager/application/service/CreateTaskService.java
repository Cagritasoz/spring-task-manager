package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.CreateTaskUseCase;
import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.WriteTaskPort;
import com.cagritasoz.taskmanager.application.service.handler.TaskCreationHandler;
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

    private final TaskCreationHandler creationHandler;

    private static final Action action = Action.CREATE;

    @Override
    public Task createTask(Long targetUserId, Task task) {

        User currentUser = currentUserPort.getCurrentUser();
        String newTaskTitle = task.getTitle();

        TaskCreationHandler.CreateTaskContext context = creationHandler.createContext(currentUser, targetUserId, newTaskTitle);

        creationHandler.logAttempt(context, action);

        boolean canAccess = currentUser.getRole() == Role.ADMIN || currentUser.getId().equals(targetUserId);

        if(!canAccess) {

            creationHandler.logForbidden(context, action);

            throw new ForbiddenException();

        }

        else if(!readUserPort.existsById(targetUserId)) {

            creationHandler.logUserNotFound(context, action);

            throw new UserNotFoundException();

        }

        creationHandler.logAccessGranted(context, action);

        Task savedTask = writeTaskPort.saveTask(targetUserId, task);

        creationHandler.logSuccess(context, action);

        return savedTask;

    }
}
