package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.GetTasksUseCase;
import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadTaskPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.service.handler.TaskReadHandler;
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

    private final TaskReadHandler readHandler;

    private static final Action action = Action.VIEW_LIST;

    @Override
    public Pagination<Task> getTasks(Long targetUserId, Pagination<Task> pagination) {

        User currentUser = currentUserPort.getCurrentUser();

        TaskReadHandler.ReadTasksContext context = readHandler.createContext(currentUser, targetUserId);

        readHandler.logListRetrievalAttempt(context, action);

        boolean canAccess = currentUser.getRole() == Role.ADMIN || currentUser.getId().equals(targetUserId);

        if(!canAccess) {

            readHandler.logForbiddenForListRetrieval(context, action);

            throw new ForbiddenException();

        }
        else if(!readUserPort.existsById(targetUserId)) {

            readHandler.logUserNotFoundForListRetrieval(context, action);

            throw new UserNotFoundException();

        }

        readHandler.logAccessGrantedForListRetrieval(context, action);

        Pagination<Task> paginatedTasks = readTaskPort.findAll(targetUserId, pagination);

        readHandler.logSuccessForListRetrieval(context, action);

        return paginatedTasks;

    }
}
