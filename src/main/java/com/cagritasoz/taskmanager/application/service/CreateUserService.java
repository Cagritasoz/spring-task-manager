package com.cagritasoz.taskmanager.application.service;

import com.cagritasoz.taskmanager.application.ports.inbound.CreateUserUseCase;
import com.cagritasoz.taskmanager.application.ports.outbound.*;
import com.cagritasoz.taskmanager.application.service.handler.UserCreationHandler;
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

    private final UserCreationHandler creationHandler;

    private final Action action = Action.CREATE;

    @Override
    public User createUser(User user) {

        User currentUser = currentUserPort.getCurrentUser();
        String newUserEmail = user.getEmail();

        UserCreationHandler.CreateUserContext context = creationHandler.createContext(currentUser, newUserEmail);

        creationHandler.logAttempt(context, action);

        if(readUserPort.existsByEmail(newUserEmail)) {

            creationHandler.logEmailAlreadyExists(context, action);

            throw new EmailAlreadyExistsException();

        }

        user.setPassword(passwordEncoderPort.encodePassword(user.getPassword()));

        User savedUser = writeUserPort.saveUser(user);

        creationHandler.logSuccess(context, action);

        return savedUser;

    }

}
