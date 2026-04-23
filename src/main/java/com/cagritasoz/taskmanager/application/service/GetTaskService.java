package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.GetTaskUseCase;
import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadTaskPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.service.logbuilder.TaskReadLogBuilder;
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

    private final TaskReadLogBuilder logBuilder;

    private static final Action action = Action.VIEW;


    @Override
    public Task getTask(Long targetUserId, Long targetTaskId) {

        User currentUser = currentUserPort.getCurrentUser();

        TaskReadLogBuilder.ReadTaskContext context = logBuilder.createContext(currentUser,
                targetUserId,
                targetTaskId);

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

        Task foundTask = readTaskPort.findById(targetTaskId)
                .orElseThrow(() -> {

                    logBuilder.logTaskNotFound(context, action);

                    return new TaskNotFoundException();

                });

        logBuilder.logSuccess(context, action);

        return foundTask;

    }
}
