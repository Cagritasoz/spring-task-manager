package com.cagritasoz.taskmanager.application.service.handler;

import com.cagritasoz.taskmanager.application.ports.outbound.LoggerPort;
import com.cagritasoz.taskmanager.domain.model.Action;
import com.cagritasoz.taskmanager.domain.model.Role;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserModificationHandler { //Update and Delete services use it.

    private final LoggerPort loggerPort;

    public record ModifyUserContext(Long currentUserId, Role currentUserRole, Long targetUserId) {}

    public ModifyUserContext createContext(User currentUser, Long targetUserId ) {

        return new ModifyUserContext(currentUser.getId(), currentUser.getRole(), targetUserId);

    }

    public void logAttempt(ModifyUserContext context, Action action) {

        loggerPort.logInfo("User {} attempt. User Id: {}, Role: {}, Target User Id: {}",
                action, context.currentUserId, context.currentUserRole, context.targetUserId);

    }

    public void logUserNotFound(ModifyUserContext context, Action action) {

        loggerPort.logWarn("{} attempt failed, user does not exist. User Id: {}, Role: {}, Target User Id: {}",
                action, context.currentUserId, context.currentUserRole, context.targetUserId);

    }

    public void logForbidden(ModifyUserContext context, Action action) {

        loggerPort.logWarn("Forbidden {} attempt. User Id: {}, Role: {}, Target User Id: {}",
                action, context.currentUserId, context.currentUserRole, context.targetUserId);

    }

    public void logAccessGranted(ModifyUserContext context, Action action) {

        loggerPort.logInfo("Access granted for {}. User Id: {}, Role: {}, Target User Id: {}",
                action, context.currentUserId, context.currentUserRole, context.targetUserId);

    }

    public void logMaliciousRequest(ModifyUserContext context) {

        loggerPort.logWarn("Malicious request. A non-admin user tried setting their role as ADMIN." +
                        " User Id: {}, Role: {}, Target User Id: {}",
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId);

    }

    public void logPasswordChanged(ModifyUserContext context) {

        loggerPort.logInfo("Password changed. User Id: {}, Role: {}, Target User Id: {}",
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId);

    }

    public void logSuccess(ModifyUserContext context, Action action) {

        loggerPort.logInfo("Successful {} attempt. User Id: {}, Role: {}, Target User Id: {}",
                action, context.currentUserId, context.currentUserRole, context.targetUserId);

    }

}
