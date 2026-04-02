package com.cagritasoz.taskmanager.application.service.handler;

import com.cagritasoz.taskmanager.application.ports.outbound.LoggerPort;
import com.cagritasoz.taskmanager.domain.model.Action;
import com.cagritasoz.taskmanager.domain.model.Role;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskModificationLogBuilder {

    private final LoggerPort loggerPort;

    public record ModifyTaskContext(Long currentUserId,
                                    Role currentUserRole,
                                    Long targetUserId,
                                    Long targetTaskId) {}

    public ModifyTaskContext createContext(User currentUser,
                                           Long targetUserId,
                                           Long targetTaskId) {
        return new ModifyTaskContext(currentUser.getId(),
                currentUser.getRole(),
                targetUserId,
                targetTaskId);
    }

    public void logAttempt(ModifyTaskContext context, Action action) {

        loggerPort.logInfo("Task {} attempt. User Id: {}, Role: {}," +
                        " Target User Id: {}, Target Task Id: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId,
                context.targetTaskId);
    }
    public void logForbidden(ModifyTaskContext context, Action action) {

        loggerPort.logWarn("Forbidden task {} attempt. User Id: {}, Role: {}," +
                        " Target User Id: {}, Target Task Id: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId,
                context.targetTaskId);

    }

    public void logUserNotFound(ModifyTaskContext context, Action action) {

        loggerPort.logWarn("{} task attempt failed, user does not exist. User Id: {}, Role: {}," +
                        " Target User Id: {}, Target Task Id: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId,
                context.targetTaskId);

    }

    public void logAccessGranted(ModifyTaskContext context, Action action) {

        loggerPort.logInfo("Access granted for task {}. User Id: {}, Role: {}," +
                        " Target User Id: {}, Target Task Id: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId,
                context.targetTaskId);

    }

    public void logTaskNotFound(ModifyTaskContext context, Action action) {

        loggerPort.logWarn("Task {} attempt failed, task does not exist. User Id: {}, Role: {}," +
                        " Target User Id: {}, Target Task Id: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId,
                context.targetTaskId);

    }

    public void logSuccess(ModifyTaskContext context, Action action) {

        loggerPort.logInfo("Successful task {} attempt. User Id: {}, Role: {}," +
                        " Target User Id: {}, Target Task Id: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId,
                context.targetTaskId);
    }
}
