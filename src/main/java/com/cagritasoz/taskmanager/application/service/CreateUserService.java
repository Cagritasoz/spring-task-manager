package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.CreateUserUseCase;
import com.cagritasoz.taskmanager.application.ports.outbound.*;
import com.cagritasoz.taskmanager.application.service.logbuilder.UserCreationLogBuilder;
import com.cagritasoz.taskmanager.domain.exception.EmailAlreadyExistsException;
import com.cagritasoz.taskmanager.domain.model.Action;
import com.cagritasoz.taskmanager.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserService implements CreateUserUseCase {

    private final PasswordEncoderPort passwordEncoderPort;

    private final CurrentUserPort currentUserPort;

    private final ReadUserPort readUserPort;

    private final WriteUserPort writeUserPort;

    private final UserCreationLogBuilder logBuilder;

    private static final Action action = Action.CREATE;

    @Override
    public User createUser(User user) {

        User currentUser = currentUserPort.getCurrentUser();
        String newUserEmail = user.getEmail();

        UserCreationLogBuilder.CreateUserContext context = logBuilder.createContext(currentUser, newUserEmail);

        logBuilder.logAttempt(context, action);

        if(readUserPort.existsByEmail(newUserEmail)) {

            logBuilder.logEmailAlreadyExists(context, action);

            throw new EmailAlreadyExistsException();

        }

        user.setPassword(passwordEncoderPort.encodePassword(user.getPassword()));

        User savedUser = writeUserPort.saveUser(user);

        logBuilder.logSuccess(context, action);

        return savedUser;

    }

}
