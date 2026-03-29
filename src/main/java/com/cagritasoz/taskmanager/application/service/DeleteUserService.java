package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.DeleteUserUseCase;
import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.WriteUserPort;
import com.cagritasoz.taskmanager.application.service.handler.UserModificationHandler;
import com.cagritasoz.taskmanager.domain.exception.ForbiddenException;
import com.cagritasoz.taskmanager.domain.exception.UserNotFoundException;
import com.cagritasoz.taskmanager.domain.model.Action;
import com.cagritasoz.taskmanager.domain.model.Role;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUserService implements DeleteUserUseCase {

    private final CurrentUserPort currentUserPort;

    private final ReadUserPort readUserPort;

    private final WriteUserPort writeUserPort;

    private final UserModificationHandler modificationHandler;

    private static final Action action = Action.DELETE;

    @Override
    public void deleteUser(Long targetUserId) {

        User currentUser = currentUserPort.getCurrentUser();

        UserModificationHandler.ModifyUserContext context =  modificationHandler
                .createContext(currentUser, targetUserId);

        modificationHandler.logAttempt(context, action);

        boolean canAccess = currentUser.getRole() == Role.ADMIN || currentUser.getId().equals(targetUserId);

        if(!canAccess) {

            modificationHandler.logForbidden(context, action);

            throw new ForbiddenException();

        } else if (!readUserPort.existsById(targetUserId)) {

            modificationHandler.logUserNotFound(context, action);

            throw new UserNotFoundException();

        }

        modificationHandler.logAccessGranted(context, action);

        writeUserPort.deleteUser(targetUserId);

        modificationHandler.logSuccess(context, action);

    }
}
