package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.GetTasksUseCase;
import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadTaskPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.service.logbuilder.TaskReadLogBuilder;
import com.cagritasoz.taskmanager.domain.exception.ForbiddenException;
import com.cagritasoz.taskmanager.domain.exception.UserNotFoundException;
import com.cagritasoz.taskmanager.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTasksService implements GetTasksUseCase {

    private final CurrentUserPort currentUserPort;

    private final ReadUserPort readUserPort;

    private final ReadTaskPort readTaskPort;

    private final TaskReadLogBuilder logBuilder;

    private static final Action action = Action.VIEW_LIST;

    @Override
    public Pagination<Task> getTasks(Long targetUserId, Pagination<Task> pagination) {

        User currentUser = currentUserPort.getCurrentUser();

        TaskReadLogBuilder.ReadTasksContext context = logBuilder.createContext(currentUser, targetUserId);

        logBuilder.logListRetrievalAttempt(context, action);

        boolean canAccess = currentUser.getRole() == Role.ADMIN || currentUser.getId().equals(targetUserId);

        if(!canAccess) {

            logBuilder.logForbiddenForListRetrieval(context, action);

            throw new ForbiddenException();

        }
        else if(!readUserPort.existsById(targetUserId)) {

            logBuilder.logUserNotFoundForListRetrieval(context, action);

            throw new UserNotFoundException();

        }

        logBuilder.logAccessGrantedForListRetrieval(context, action);

        Pagination<Task> paginatedTasks = readTaskPort.findAll(targetUserId, pagination);

        logBuilder.logSuccessForListRetrieval(context, action);

        return paginatedTasks;

    }
}
