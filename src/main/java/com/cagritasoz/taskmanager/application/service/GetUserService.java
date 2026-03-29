package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.GetUserUseCase;
import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.service.handler.UserReadHandler;
import com.cagritasoz.taskmanager.domain.exception.ForbiddenException;
import com.cagritasoz.taskmanager.domain.exception.UserNotFoundException;
import com.cagritasoz.taskmanager.domain.model.Action;
import com.cagritasoz.taskmanager.domain.model.Role;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserService implements GetUserUseCase {

    private final CurrentUserPort currentUserPort;

    private final ReadUserPort readUserPort;

    private final UserReadHandler readHandler;

    private static final Action action = Action.VIEW;

    @Override
    public User getUser(Long id) {

        User currentUser = currentUserPort.getCurrentUser();

        UserReadHandler.ReadUserContext context = readHandler.createContext(currentUser, id);

        readHandler.logAttempt(context, action);

        boolean canAccess = currentUser.getRole() == Role.ADMIN || currentUser.getId().equals(id);

        if(!canAccess) {

            readHandler.logForbidden(context, action);

            throw new ForbiddenException();

        }

        readHandler.logAccessGranted(context, action);

        User retrievedUser = readUserPort.findById(id)
                        .orElseThrow(() -> {

                            readHandler.logUserNotFound(context, action);

                            return new UserNotFoundException();
                        });

        readHandler.logSuccess(context, action);

        return retrievedUser;
    }

}
