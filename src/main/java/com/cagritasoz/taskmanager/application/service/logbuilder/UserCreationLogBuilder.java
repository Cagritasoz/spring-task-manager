package com.cagritasoz.taskmanager.application.service.logbuilder;

import com.cagritasoz.taskmanager.application.ports.outbound.LoggerPort;
import com.cagritasoz.taskmanager.domain.model.Action;
import com.cagritasoz.taskmanager.domain.model.Role;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreationLogBuilder { //Only CreateUserService uses it.

    private final LoggerPort loggerPort;

    public record CreateUserContext(Long currentUserId,
                                    Role currentUserRole,
                                    String newUserEmail) {}

    public CreateUserContext createContext(User currentUser, String newUserEmail) {

        return new CreateUserContext(currentUser.getId(),
                currentUser.getRole(),
                newUserEmail);

    }

    public void logAttempt(CreateUserContext context, Action action) {

        loggerPort.logInfo("User {} attempt. User Id: {}, Role: {}, New User Email: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.newUserEmail);

    }

    public void logEmailAlreadyExists(CreateUserContext context, Action action) {

        loggerPort.logWarn("{} attempt failed, email already exists. User Id: {}, Role: {}, New User Email: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.newUserEmail);

    }

    public void logSuccess(CreateUserContext context, Action action) {

        loggerPort.logInfo("Successful user {} attempt. User Id: {}, Role: {}, New User Email: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.newUserEmail);

    }

}
