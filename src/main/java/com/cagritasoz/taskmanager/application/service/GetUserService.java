package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.GetUserUseCase;
import com.cagritasoz.taskmanager.application.ports.outbound.CurrentUserPort;
import com.cagritasoz.taskmanager.application.ports.outbound.ReadUserPort;
import com.cagritasoz.taskmanager.application.service.logbuilder.UserReadLogBuilder;
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

    private final UserReadLogBuilder logBuilder;

    private static final Action action = Action.VIEW;

    @Override
    public User getUser(Long id) {

        User currentUser = currentUserPort.getCurrentUser();

        UserReadLogBuilder.ReadUserContext context = logBuilder.createContext(currentUser, id);

        logBuilder.logAttempt(context, action);

        boolean canAccess = currentUser.getRole() == Role.ADMIN || currentUser.getId().equals(id);

        if(!canAccess) {

            logBuilder.logForbidden(context, action);

            throw new ForbiddenException();

        }

        logBuilder.logAccessGranted(context, action);

        User retrievedUser = readUserPort.findById(id)
                        .orElseThrow(() -> {

                            logBuilder.logUserNotFound(context, action);

                            return new UserNotFoundException();
                        });

        logBuilder.logSuccess(context, action);

        return retrievedUser;
    }

}
