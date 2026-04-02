package com.cagritasoz.taskmanager.application.service.handler;

import com.cagritasoz.taskmanager.application.ports.outbound.LoggerPort;
import com.cagritasoz.taskmanager.domain.model.Action;
import com.cagritasoz.taskmanager.domain.model.Role;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskReadLogBuilder {

    private final LoggerPort loggerPort;

    public record ReadTaskContext(Long currentUserId,
                                  Role currentUserRole,
                                  Long targetUserId,
                                  Long targetTaskId) {}

    public record ReadTasksContext(Long currentUserId,
                                   Role currentUserRole,
                                   Long targetUserId) {}

    public ReadTaskContext createContext(User currentUser, Long targetUserId, Long targetTaskId) {

        return new ReadTaskContext(currentUser.getId(),
                currentUser.getRole(),
                targetUserId,
                targetTaskId);

    }

    public ReadTasksContext createContext(User currentUser, Long targetUserId) {

        return new ReadTasksContext(currentUser.getId(),
                currentUser.getRole(),
                targetUserId);

    }

    public void logAttempt(ReadTaskContext context, Action action) {

        loggerPort.logInfo("Task {} attempt. User Id: {}, Role: {}," +
                        " Target User Id: {}, Target Task Id: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId,
                context.targetTaskId);
    }

    public void logListRetrievalAttempt(ReadTasksContext context, Action action) {

        loggerPort.logInfo("Task {} attempt. User Id: {}, Role: {}," +
                        " Target User Id: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId
        );


    }

    public void logForbidden(ReadTaskContext context, Action action) {

        loggerPort.logWarn("Forbidden task {} attempt. User Id: {}, Role: {}," +
                        " Target User Id: {}, Target Task Id: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId,
                context.targetTaskId);

    }

    public void logForbiddenForListRetrieval(ReadTasksContext context, Action action) {

        loggerPort.logWarn("Forbidden task {} attempt. User Id: {}, Role: {}," +
                        " Target User Id: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId);

    }

    public void logUserNotFound(ReadTaskContext context, Action action) {

        loggerPort.logWarn("{} attempt failed, user does not exist. User Id: {}, Role: {}," +
                        " Target User Id: {}, Target Task Id: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId,
                context.targetTaskId);

    }

    public void logUserNotFoundForListRetrieval(ReadTasksContext context, Action action) {

        loggerPort.logWarn("{} attempt failed, user does not exist. User Id: {}, Role: {}," +
                        " Target User Id: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId);

    }

    public void logAccessGranted(ReadTaskContext context, Action action) {

        loggerPort.logInfo("Access granted for {}. User Id: {}, Role: {}," +
                        " Target User Id: {}, Target Task Id: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId,
                context.targetTaskId);

    }

    public void logAccessGrantedForListRetrieval(ReadTasksContext context, Action action) {

        loggerPort.logInfo("Access granted for {}. User Id: {}, Role: {}," +
                        " Target User Id: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId);

    }

    public void logTaskNotFound(ReadTaskContext context, Action action) {

        loggerPort.logWarn("{} attempt failed, task does not exist. User Id: {}, Role: {}," +
                        " Target User Id: {}, Target Task Id: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId,
                context.targetTaskId);

    }

    public void logSuccess(ReadTaskContext context, Action action) {

        loggerPort.logInfo("Successful task {} attempt. User Id: {}, Role: {}," +
                        " Target User Id: {}, Target Task Id: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId,
                context.targetTaskId);
    }

    public void logSuccessForListRetrieval(ReadTasksContext context, Action action) {

        loggerPort.logInfo("Successful task {} attempt. User Id: {}, Role: {}," +
                        " Target User Id: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId);
    }





}
