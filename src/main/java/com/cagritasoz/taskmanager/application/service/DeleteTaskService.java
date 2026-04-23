package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.DeleteTaskUseCase;
import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadTaskPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.WriteTaskPort;
import com.cagritasoz.taskmanager.application.service.logbuilder.TaskModificationLogBuilder;
import com.cagritasoz.taskmanager.domain.exception.ForbiddenException;
import com.cagritasoz.taskmanager.domain.exception.TaskNotFoundException;
import com.cagritasoz.taskmanager.domain.exception.UserNotFoundException;
import com.cagritasoz.taskmanager.domain.model.Action;
import com.cagritasoz.taskmanager.domain.model.Role;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTaskService implements DeleteTaskUseCase {

    private final CurrentUserPort currentUserPort;

    private final ReadUserPort readUserPort;

    private final ReadTaskPort readTaskPort;

    private final WriteTaskPort writeTaskPort;

    private final TaskModificationLogBuilder logBuilder;

    private final Action action = Action.DELETE;

    @Override
    public void deleteTask(Long targetUserId, Long targetTaskId) {

        User currentUser = currentUserPort.getCurrentUser();

        TaskModificationLogBuilder.ModifyTaskContext context = logBuilder.createContext(currentUser,
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

        else if(!readTaskPort.existsById(targetTaskId)) {

            logBuilder.logTaskNotFound(context, action);

            throw new TaskNotFoundException();

        }

        logBuilder.logAccessGranted(context, action);

        writeTaskPort.deleteTask(targetTaskId);

        logBuilder.logSuccess(context, action);


    }

}


