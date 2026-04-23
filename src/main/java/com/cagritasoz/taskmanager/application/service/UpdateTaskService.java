package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.UpdateTaskUseCase;
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
import com.cagritasoz.taskmanager.domain.model.Task;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateTaskService implements UpdateTaskUseCase {

    private final CurrentUserPort currentUserPort;

    private final ReadUserPort readUserPort;

    private final ReadTaskPort readTaskPort;

    private final WriteTaskPort writeTaskPort;

    private final TaskModificationLogBuilder logBuilder;

    private final Action action = Action.UPDATE;

    @Override
    public Task updateTask(Long targetUserId, Long targetTaskId, Task task) {

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

        Task updatedTask = writeTaskPort.updateTask(targetUserId, targetTaskId, task);

        logBuilder.logSuccess(context, action);

        return updatedTask;
    }

}
