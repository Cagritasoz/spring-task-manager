package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.GetTaskUseCase;
import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadTaskPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.service.handler.TaskReadHandler;
import com.cagritasoz.taskmanager.domain.exception.ForbiddenException;
import com.cagritasoz.taskmanager.domain.exception.TaskNotFoundException;
import com.cagritasoz.taskmanager.domain.exception.UserNotFoundException;
import com.cagritasoz.taskmanager.domain.model.Action;
import com.cagritasoz.taskmanager.domain.model.Role;
import com.cagritasoz.taskmanager.domain.model.Task;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTaskService implements GetTaskUseCase {

    private final CurrentUserPort currentUserPort;

    private final ReadUserPort readUserPort;

    private final ReadTaskPort readTaskPort;

    private final TaskReadHandler readHandler;

    private static final Action action = Action.VIEW;


    @Override
    public Task getTask(Long targetUserId, Long targetTaskId) {

        User currentUser = currentUserPort.getCurrentUser();

        TaskReadHandler.ReadTaskContext context = readHandler.createContext(currentUser,
                targetUserId,
                targetTaskId);

        readHandler.logAttempt(context, action);

        boolean canAccess = currentUser.getRole() == Role.ADMIN || currentUser.getId().equals(targetUserId);

        if(!canAccess) {

            readHandler.logForbidden(context, action);

            throw new ForbiddenException();

        }

        else if(!readUserPort.existsById(targetUserId)) {

            readHandler.logUserNotFound(context, action);

            throw new UserNotFoundException();

        }

        readHandler.logAccessGranted(context, action);

        Task foundTask = readTaskPort.findById(targetTaskId)
                .orElseThrow(() -> {

                    readHandler.logTaskNotFound(context, action);

                    return new TaskNotFoundException();

                });

        readHandler.logSuccess(context, action);

        return foundTask;

    }
}
