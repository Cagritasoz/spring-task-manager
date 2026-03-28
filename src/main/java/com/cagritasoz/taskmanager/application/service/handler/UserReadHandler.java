package com.cagritasoz.taskmanager.application.service.handler;

import com.cagritasoz.taskmanager.application.ports.outbound.LoggerPort;
import com.cagritasoz.taskmanager.domain.model.Action;
import com.cagritasoz.taskmanager.domain.model.Role;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReadHandler { //Get services use it.

    private final LoggerPort loggerPort;

    public record ReadUserContext(Long currentUserId, Role currentUserRole, Long targetUserId) {}

    public record ReadUsersContext(Long currentUserId, Role currentUserRole) {}

    public ReadUserContext createContext(User currentUser, Long targetUserId) {

        return new ReadUserContext(currentUser.getId(), currentUser.getRole(), targetUserId);

    }

    public ReadUsersContext createContext(User currentUser) {

        return new ReadUsersContext(currentUser.getId(), currentUser.getRole());

    }

    public void logAttempt(ReadUserContext context, Action action) {

        loggerPort.logInfo("User {} attempt. User Id: {}, Role: {}, Target User Id: {}",
                action, context.currentUserId, context.currentUserRole, context.targetUserId);

    }

    public void logListRetrievalAttempt(ReadUsersContext context, Action action) {

        loggerPort.logInfo("{} attempt. User Id: {}, Role: {}",
                action, context.currentUserId, context.currentUserRole);

    }

    public void logUserNotFound(ReadUserContext context, Action action) {

        loggerPort.logWarn("{} attempt failed, user does not exist. User Id: {}, Role: {}, Target User Id: {}",
                action, context.currentUserId, context.currentUserRole, context.targetUserId);

    }

    public void logForbidden(ReadUserContext context, Action action) {

        loggerPort.logWarn("Forbidden {} attempt. User Id: {}, Role: {}, Target User Id: {}",
                action, context.currentUserId, context.currentUserRole, context.targetUserId);

    }

    public void logAccessGranted(ReadUserContext context, Action action) {

        loggerPort.logInfo("Access granted for {}. User Id: {}, Role: {}, Target User Id: {}",
                action, context.currentUserId, context.currentUserRole, context.targetUserId);

    }

    public void logSuccess(ReadUserContext context, Action action) {

        loggerPort.logInfo("Successful {} attempt. User Id: {}, Role: {}, Target User Id: {}",
                action, context.currentUserId, context.currentUserRole, context.targetUserId);

    }

    public void logListRetrievalSuccess(ReadUsersContext context, Action action) {

        loggerPort.logInfo("{} attempt successful. User Id: {}, Role: {}",
                action, context.currentUserId, context.currentUserRole);

    }
}
