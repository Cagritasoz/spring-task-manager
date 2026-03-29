package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.UpdateUserUseCase;
import com.cagritasoz.taskmanager.application.ports.outbound.*;
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
public class UpdateUserService implements UpdateUserUseCase {

    private final PasswordEncoderPort passwordEncoderPort;

    private final CurrentUserPort currentUserPort;

    private final ReadUserPort readUserPort;

    private final WriteUserPort writeUserPort;

    private final UserModificationHandler modificationHandler;

    private static final Action action = Action.UPDATE;

    @Override
    public User updateUser(Long targetUserId, User targetUser) {

        User currentUser = currentUserPort.getCurrentUser(); //Get logged-in user.

        UserModificationHandler.ModifyUserContext context = modificationHandler.createContext(currentUser, targetUserId);

        modificationHandler.logAttempt(context, action);

        boolean canAccess = currentUser.getRole() == Role.ADMIN || currentUser.getId().equals(targetUserId);

        if(!canAccess) {

            modificationHandler.logForbidden(context, action);

            throw new ForbiddenException();

        }

        modificationHandler.logAccessGranted(context, action);

        User actualUserToBeUpdated = readUserPort.findById(targetUserId)
                .orElseThrow(() -> {

                    modificationHandler.logUserNotFound(context, action);

                    return new UserNotFoundException();

                });

        enforceRole(targetUser, context);

        encodePassword(targetUser, actualUserToBeUpdated, context);

        User updatedUser = writeUserPort.updateUser(targetUserId, targetUser);

        modificationHandler.logSuccess(context, action);

        return updatedUser;

    }

    private void enforceRole(User targetUser, UserModificationHandler.ModifyUserContext context) {

        if(context.currentUserRole() == Role.USER && !targetUser.getRole().equals(Role.USER)) {

            modificationHandler.logMaliciousRequest(context);

            targetUser.setRole(Role.USER);

        }
    }

    private void encodePassword(User targetUser, User actualUserToBeUpdated, UserModificationHandler.ModifyUserContext context) {

        if(!passwordEncoderPort.matches(targetUser.getPassword(), actualUserToBeUpdated.getPassword())) { //Password has changed.

            modificationHandler.logPasswordChanged(context);

        }

        targetUser.setPassword(passwordEncoderPort.encodePassword(targetUser.getPassword()));

    }
}
