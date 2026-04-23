package com.cagritasoz.taskmanager.application.service.logbuilder;

import com.cagritasoz.taskmanager.application.ports.outbound.LoggerPort;
import com.cagritasoz.taskmanager.domain.model.Action;
import com.cagritasoz.taskmanager.domain.model.Role;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskCreationLogBuilder {

    private final LoggerPort loggerPort;

    public record CreateTaskContext(Long currentUserId,
                                    Role currentUserRole,
                                    Long targetUserId,
                                    String newTaskTitle) {}

    public CreateTaskContext createContext(User currentUser, Long targetUserId, String newTaskTitle) {

        return new CreateTaskContext(currentUser.getId(),
                currentUser.getRole(),
                targetUserId,
                newTaskTitle);

    }

    public void logAttempt(CreateTaskContext context, Action action) {

        loggerPort.logInfo("Task {} attempt. User Id: {}, Role: {}," +
                        " Target User Id: {}, New Task Title: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId,
                context.newTaskTitle);
    }

    public void logForbidden(CreateTaskContext context, Action action) {

        loggerPort.logWarn("Forbidden task {} attempt. User Id: {}, Role: {}," +
                        " Target User Id: {}, New Task Title: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId,
                context.newTaskTitle);

    }

    public void logUserNotFound(CreateTaskContext context, Action action) {

        loggerPort.logWarn("{} attempt failed, user does not exist. User Id: {}, Role: {}," +
                        " Target User Id: {}, New Task Title: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId,
                context.newTaskTitle);

    }

    public void logAccessGranted(CreateTaskContext context, Action action) {

        loggerPort.logInfo("Access granted for {}. User Id: {}, Role: {}," +
                        " Target User Id: {}, New Task Title: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId,
                context.newTaskTitle);

    }

    public void logSuccess(CreateTaskContext context, Action action) {

        loggerPort.logInfo("Successful task {} attempt. User Id: {}, Role: {}," +
                        " Target User Id: {}, New Task Title: {}",
                action,
                context.currentUserId,
                context.currentUserRole,
                context.targetUserId,
                context.newTaskTitle);
    }



}
